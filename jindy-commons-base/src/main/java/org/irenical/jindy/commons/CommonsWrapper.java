package org.irenical.jindy.commons;

import org.apache.commons.configuration.Configuration;
import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigChangedCallback;
import org.irenical.jindy.ConfigNotFoundException;

public class CommonsWrapper implements Config {
    
    private final Configuration config;
    
    public CommonsWrapper(Configuration config){
        this.config=config;
    }
    
    private void assertKey(String key) throws ConfigNotFoundException {
        if (!config.containsKey(key)) {
            throw new ConfigNotFoundException("Mandatory configuration property '" + key + "' was not found");
        }
    }
    
    @Override
    public void unListen(ConfigChangedCallback callback) {
        // commons configuration is immutable, do nothing
    }

    @Override
    public void listen(String key, ConfigChangedCallback callback) {
        // commons configuration is immutable, do nothing
    }

    @Override
    public String[] getStringArray(String key) {
        return config.getStringArray(key);
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
        assertKey(key);
        return config.getString(key);
    }

    @Override
    public int getMandatoryInt(String key) throws ConfigNotFoundException {
        assertKey(key);
        return config.getInt(key);
    }

    @Override
    public float getMandatoryFloat(String key) throws ConfigNotFoundException {
        assertKey(key);
        return config.getFloat(key);
    }

    @Override
    public boolean getMandatoryBoolean(String key) throws ConfigNotFoundException {
        assertKey(key);
        return config.getBoolean(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return config.getFloat(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key,defaultValue);
    }

    @Override
    public void setProperty(String key, Object value) {
        config.setProperty(key, value);
    }

}
