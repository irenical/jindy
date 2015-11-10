package org.irenical.jindy.archaius;

import com.boundary.config.ConsulWatchedConfigurationSource;
import com.ecwid.consul.v1.ConsulClient;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.netflix.config.DynamicWatchedConfiguration;

import org.apache.commons.configuration.AbstractConfiguration;

import java.util.Optional;

public class ArchaiusConsulFactory extends ArchaiusBaseFactory {

  @Override
  protected DynamicWatchedConfiguration getConfiguration() {
    String appName = ConfigurationManager.getDeploymentContext().getApplicationId();
    if (appName == null) {
      throw new RuntimeException(DeploymentContext.ContextKey.appId.getKey() + " was not set");
    }

    AbstractConfiguration config = ConfigurationManager.getConfigInstance();
    String consulHost = Optional.ofNullable(config.getString("consul.host")).orElse("consul.service.consul");
    String consulAclToken = config.getString("consul.token");
    String consulConfigVersion = Optional.ofNullable(config.getString("consul.config.version")).orElse("v1");
    String consulConfigRoot = Optional.ofNullable(config.getString("consul.config.root")).orElse("applications");

    String rootConfigPath = consulConfigRoot + "/" + appName + "/" + consulConfigVersion;
    ConsulWatchedConfigurationSource configSource = new ConsulWatchedConfigurationSource(rootConfigPath, new ConsulClient(consulHost), consulAclToken);

    configSource.startAsync();

    return new DynamicWatchedConfiguration(configSource);

  }
}
