package org.irenical.jindy.commons;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.irenical.jindy.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonsSimpleImpl extends CommonsConfigurationBaseFactory {

  private static Logger LOG = LoggerFactory.getLogger(CommonsSimpleImpl.class);

  @Override
  protected Configuration createConfiguration(String name) throws ConfigurationException {
    CompositeConfiguration config = new CompositeConfiguration();
    config.addConfiguration(new SystemConfiguration());
    try {
      if (ConfigFactory.DEFAULT_CONFIG_NAME.equals(name)) {
        config.addConfiguration(new PropertiesConfiguration("config.properties"));
      } else {
        config.addConfiguration(new PropertiesConfiguration(name));
      }
    } catch (ConfigurationException e) {
      String message = "No properties file found: " + name
          + ". Using an empty BaseConfiguration instead.";
      if (ConfigFactory.DEFAULT_CONFIG_NAME.equals(name)) {
        LOG.info(message);
      } else {
        LOG.warn(message);
      }
      BaseConfiguration base = new BaseConfiguration();
      config.addConfiguration(base);
    }
    return config;
  }

}
