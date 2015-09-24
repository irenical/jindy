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

}
