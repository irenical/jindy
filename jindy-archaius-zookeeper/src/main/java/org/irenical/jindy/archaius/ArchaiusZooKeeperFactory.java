package org.irenical.jindy.archaius;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicWatchedConfiguration;
import com.netflix.config.source.ZooKeeperConfigurationSource;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArchaiusZooKeeperFactory extends ArchaiusBaseFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ArchaiusZooKeeperFactory.class);

  public static final String ZOOKEEPER_HOSTS = "zookeeper.hosts";

  @Override
  protected AbstractConfiguration getConfiguration() {
    AbstractConfiguration config = ConfigurationManager.getConfigInstance();
    boolean dynamic = config.getBoolean(DYNAMIC_CONFIG, true);
    if (!dynamic) {
      return new DynamicConfiguration();
    }

    String appId = null;
    DeploymentContext context = ConfigurationManager.getDeploymentContext();
    appId = context.getApplicationId();
    if (appId == null) {
      LOG.info(
        "No applicationId set on archaius deployment context. Will try to use the 'application' property as fallback.");
      appId = config.getString("application");
    }

    if (appId == null) {
      throw new RuntimeException(
        "Archaius deployment context's applicationId not set nor property 'application' found");
    }

    String zooKeeperConnString = config.getString(ZOOKEEPER_HOSTS);
    String rootConfigPath = "/" + appId;

    CuratorFramework client = CuratorFrameworkFactory.newClient(zooKeeperConnString, new ExponentialBackoffRetry(1000, 3));
    ZooKeeperConfigurationSource configSource = new ZooKeeperConfigurationSource(client, rootConfigPath);

    // do the first update synchronously
    try {
      client.start();
      configSource.start();
    } catch (Exception e) {
      throw new RuntimeException("Error initializing Zookeeper config source", e);
    }

    return new DynamicWatchedConfiguration(configSource);
  }
}
