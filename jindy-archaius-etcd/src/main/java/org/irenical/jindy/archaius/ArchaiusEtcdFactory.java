package org.irenical.jindy.archaius;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.netflix.config.DynamicWatchedConfiguration;
import com.netflix.config.source.EtcdConfigurationSource;
import org.apache.commons.configuration.AbstractConfiguration;
import org.boon.etcd.ClientBuilder;
import org.boon.etcd.Etcd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Arrays;

public class ArchaiusEtcdFactory extends ArchaiusBaseFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ArchaiusEtcdFactory.class);

  public static final String ETCD_HOSTS = "etcd.hosts";

  @Override
  protected AbstractConfiguration getConfiguration() {

    AbstractConfiguration config = ConfigurationManager.getConfigInstance();
    String[] etcdHosts = config.getStringArray(ETCD_HOSTS);

    Etcd etcdClient;
    if(etcdHosts != null && etcdHosts.length > 0) {
      URI[] etcdHostURIs = Arrays.stream(etcdHosts).map(URI::create).toArray(URI[]::new);
      etcdClient = ClientBuilder.builder().hosts(etcdHostURIs).createClient();
    } else {
      etcdClient = ClientBuilder.builder().createClient();
    }


    String appId;
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

    EtcdConfigurationSource configSource = new EtcdConfigurationSource(etcdClient, appId);

    return new DynamicWatchedConfiguration(configSource);
  }
}
