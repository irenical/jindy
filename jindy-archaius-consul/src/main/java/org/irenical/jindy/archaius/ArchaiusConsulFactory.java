package org.irenical.jindy.archaius;

import com.boundary.config.ConsulWatchedConfigurationSource;
import com.ecwid.consul.v1.ConsulClient;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.netflix.config.DynamicWatchedConfiguration;

import org.apache.commons.configuration.AbstractConfiguration;
import org.irenical.jindy.ConfigFactory;
import org.irenical.jindy.commons.CommonsWrapper;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

public class ArchaiusConsulFactory extends ArchaiusBaseFactory {

  public static final String CONSUL_HOST = "consul.host";
  public static final String CONSUL_PORT = "consul.port";
  public static final String CONSUL_TOKEN = "consul.token";
  public static final String CONSUL_DEFAULT_HOST = "consul.service.consul";

  @Override
  protected DynamicWatchedConfiguration getConfiguration() {

    AbstractConfiguration config = ConfigurationManager.getConfigInstance();
    String consulHost = config.getString(CONSUL_HOST, CONSUL_DEFAULT_HOST);
    int consulPort = config.getInt(CONSUL_PORT, 8500);
    String consulAclToken = config.getString(CONSUL_TOKEN);
    String rootConfigPath = null;

    ConsulConfigStrategy strategy = findStrategy();

    if (strategy != null) {
      rootConfigPath = strategy.getBasePath(new CommonsWrapper(config));
    } else {
      String appName = Optional.ofNullable(ConfigurationManager.getDeploymentContext().getApplicationId()).orElse(ConfigFactory.getContext() == null ? null : ConfigFactory.getContext().getApplicationId());

      if (appName == null) {
        throw new RuntimeException(DeploymentContext.ContextKey.appId.getKey() + " was not set and no app name set");
      }
      rootConfigPath = appName;
    }

    ConsulWatchedConfigurationSource configSource = new ConsulWatchedConfigurationSource(rootConfigPath, new ConsulClient(consulHost, consulPort), 30, TimeUnit.SECONDS, consulAclToken);

    // do the first update synchronously
    try {
      configSource.runOnce();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    configSource.startAsync();

    return new DynamicWatchedConfiguration(configSource);
  }

  private ConsulConfigStrategy findStrategy() {
    ServiceLoader<ConsulConfigStrategy> sl = ServiceLoader.load(ConsulConfigStrategy.class);
    for (ConsulConfigStrategy consulConfigStrategy : sl) {
      return consulConfigStrategy;
    }
    return null;
  }

}
