package org.irenical.jindy.commons;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.irenical.jindy.Config;
import org.irenical.jindy.IConfigFactory;

/**
 * This factory handles the conversion from org.apache.commons.configuration.Configuration
 * to org.irenical.jindy.Config.
 * You must implement createConfiguration() to create the Commons Configuration object
 * You can override convert() if you want to use your own wrapper
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
            return convert(got);
        } catch(ConfigurationException e){
            throw new RuntimeException(e);
        }
    }

}
