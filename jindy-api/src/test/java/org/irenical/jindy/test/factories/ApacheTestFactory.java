package org.irenical.jindy.test.factories;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigChangedCallback;
import org.irenical.jindy.ConfigNotFoundException;
import org.irenical.jindy.IConfigFactory;

public class ApacheTestFactory implements IConfigFactory {
    
    

    @Override
    public Config createConfig(String name) {
        try {
            final CompositeConfiguration config = new CompositeConfiguration();
            config.addConfiguration(new SystemConfiguration());
            config.addConfiguration(new PropertiesConfiguration("test.properties"));
            return new Config() {
    
                @Override
                public void unListen(String listenerId) {
                    throw new UnsupportedOperationException();
                }
    
                @Override
                public String listen(String key, ConfigChangedCallback callback) {
                    throw new UnsupportedOperationException();
                }
    
                @Override
                public String[] getStringArray(String key) {
                    throw new UnsupportedOperationException();
                }
    
                @Override
                public String getString(String key, String defaultValue) {
                    return config.getString(key, defaultValue);
                }
    
                @Override
                public String getString(String key) {
                    return config.getString(key);
                }
    
                @Override
                public String getMandatoryString(String key) throws ConfigNotFoundException {
                    throw new UnsupportedOperationException();
                }
    
                @Override
                public int getMandatoryInt(String key) throws ConfigNotFoundException {
                    throw new UnsupportedOperationException();
                }
    
                @Override
                public float getMandatoryFloat(String key) throws ConfigNotFoundException {
                    throw new UnsupportedOperationException();
                }
    
                @Override
                public boolean getMandatoryBoolean(String key) throws ConfigNotFoundException {
                    throw new UnsupportedOperationException();
                }
    
                @Override
                public int getInt(String key, int defaultValue) {
                    return config.getInt(key, defaultValue);
                }
    
                @Override
                public float getFloat(String key, float defaultValue) {
                    throw new UnsupportedOperationException();
                }
    
                @Override
                public boolean getBoolean(String key, boolean defaultValue) {
                    throw new UnsupportedOperationException();
                }
            };
        } catch(ConfigurationException e){
            throw new RuntimeException(e);
        }
    }

}
