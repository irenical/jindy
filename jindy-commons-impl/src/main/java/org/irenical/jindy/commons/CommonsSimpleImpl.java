package org.irenical.jindy.commons;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.irenical.jindy.ConfigFactory;

public class CommonsSimpleImpl extends CommonsConfigurationBaseFactory {

  @Override
  protected Configuration createConfiguration(String name) throws ConfigurationException {
    if(ConfigFactory.DEFAULT_CONFIG_NAME.equals(name)){
      name="config.properties";
    }
    CompositeConfiguration config = new CompositeConfiguration();
    config.addConfiguration(new SystemConfiguration());
    config.addConfiguration(new PropertiesConfiguration(name));
    return config;
  }

}
