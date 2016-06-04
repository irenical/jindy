package org.irenical.jindy.common;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.irenical.jindy.commons.CommonsConfigurationBaseFactory;

public class CommonsConfigurationExample extends CommonsConfigurationBaseFactory {
    
    @Override
    protected Configuration createConfiguration(String name) throws ConfigurationException {
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        config.addConfiguration(new PropertiesConfiguration(name+".properties"));
        return config;
    }
    
}
