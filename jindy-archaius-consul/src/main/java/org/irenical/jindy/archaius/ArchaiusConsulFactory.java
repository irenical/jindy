package org.irenical.jindy.archaius;

import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.AbstractConfiguration;

import com.boundary.config.ConsulWatchedConfigurationSource;
import com.ecwid.consul.v1.ConsulClient;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicWatchedConfiguration;

public class ArchaiusConsulFactory extends ArchaiusBaseFactory {

  public static final String CONSUL_HOST = "consul.host";
  public static final String CONSUL_PORT = "consul.port";
  public static final String CONSUL_TOKEN = "consul.token";
  public static final String CONSUL_DEFAULT_HOST = "consul.service.consul";

  @Override
  protected AbstractConfiguration getConfiguration() {

    AbstractConfiguration config = ConfigurationManager.getConfigInstance();
    boolean dynamic = config.getBoolean(DYNAMIC_CONFIG, true);
    if (!dynamic) {
      return new DynamicConfiguration();
    } else {
      String appId = null;
      DeploymentContext context = ConfigurationManager.getDeploymentContext();
      appId = context.getApplicationId();
      
      if (appId == null) {
        throw new RuntimeException(
            "Archaius deployment context's applicationId not set (archaius.deployment.applicationId)");
      }

      String consulHost = config.getString(CONSUL_HOST, CONSUL_DEFAULT_HOST);
      int consulPort = config.getInt(CONSUL_PORT, 8500);
      String consulAclToken = config.getString(CONSUL_TOKEN);

      ConsulWatchedConfigurationSource configSource = new ConsulWatchedConfigurationSource(appId,
          new ConsulClient(consulHost, consulPort), 30, TimeUnit.SECONDS, consulAclToken);

      // do the first update synchronously
      try {
        configSource.runOnce();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      configSource.startAsync();

      return new DynamicWatchedConfiguration(configSource);
    }
  }

}
