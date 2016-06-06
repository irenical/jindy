package org.irenical.jindy.commons;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigContext;
import org.irenical.jindy.ConfigFactory;
import org.irenical.jindy.IConfigFactory;

/**
 * This factory handles the conversion from
 * org.apache.commons.configuration.Configuration to org.irenical.jindy.Config.
 * You must implement createConfiguration() to create the Commons Configuration
 * object You can override convert() if you want to use your own wrapper
 * 
 * @author tgsimao
 *
 */
public abstract class CommonsConfigurationBaseFactory implements IConfigFactory {

  protected abstract Configuration createConfiguration(String name) throws ConfigurationException;

  protected Config convert(Configuration configuration) {
    return configuration == null ? null : new CommonsWrapper(configuration);
  }

  @Override
  public Config createConfig(String name) {
    try {
      Configuration got = createConfiguration(name);
      ConfigContext context = new ConfigContext();
      context.setApplicationId(got.getString("application"));
      context.setEnvironment(got.getString("environment"));
      context.setStack(got.getString("stack"));
      context.setServerId(got.getString("serverId"));
      context.setRegion(got.getString("region"));
      context.setDatacenter(got.getString("datacenter"));
      ConfigFactory.setContext(context);
      return convert(got);
    } catch (ConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

}
