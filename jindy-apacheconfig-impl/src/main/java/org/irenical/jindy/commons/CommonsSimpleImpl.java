package org.irenical.jindy.commons;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.irenical.jindy.ConfigFactory;
import org.slf4j.LoggerFactory;

public class CommonsSimpleImpl extends CommonsConfigurationBaseFactory {

  @Override
  protected Configuration createConfiguration(String name) throws ConfigurationException {
    CompositeConfiguration config = new CompositeConfiguration();
    config.addConfiguration(new SystemConfiguration());
    try {
      if(ConfigFactory.DEFAULT_CONFIG_NAME.equals(name)){
        config.addConfiguration(new PropertiesConfiguration("config.properties"));
      } else 
        config.addConfiguration(new PropertiesConfiguration(name));
    } catch (ConfigurationException e) {
      if(!ConfigFactory.DEFAULT_CONFIG_NAME.equals(name)){
        LoggerFactory.getLogger(CommonsSimpleImpl.class).info("No default properties file found (config.properties)");
      } else {
        throw e;
      }
    }
    
    
    return config;
  }

}
