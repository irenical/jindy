package org.irenical.jindy.archaius;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigChangedCallback;
import org.irenical.jindy.ConfigNotFoundException;

import com.netflix.config.ConfigurationManager;

public class ArchaiusWrapper implements Config {
    
    private final Map<String, List<ConfigChangedCallback>> callbacks = new ConcurrentHashMap<>();
    
    @Override
    public void listen(String key, ConfigChangedCallback callback) {
        List<ConfigChangedCallback> forKey = callbacks.get(key);
        if (forKey == null) {
            forKey = new CopyOnWriteArrayList<>();
            if (null != callbacks.putIfAbsent(key, forKey)) {
                forKey = callbacks.get(key);
            }
        }
        forKey.add(callback);
    }
    
    @Override
    public void unListen(ConfigChangedCallback callback) {
        for(String key : new LinkedList<>(callbacks.keySet())){
            unListen(key,callback);
        }
    }
    
    protected void fire(String key){
        List<ConfigChangedCallback> forKey = callbacks.get(key);
        if(forKey!=null){
            for(int i=0;i<forKey.size();++i){
                ConfigChangedCallback callback = forKey.get(i);
                callback.propertyChanged();
            }
        }
    }
    
    private void unListen(String key, ConfigChangedCallback callback) {
        List<ConfigChangedCallback> forKey = callbacks.get(key);
        while(forKey.remove(callback));
    }

    private void assertKey(String key) throws ConfigNotFoundException {
        if (!ConfigurationManager.getConfigInstance().containsKey(key)) {
            throw new ConfigNotFoundException("Mandatory configuration property '" + key + "' was not found");
        }
    }
    
    @Override
    public String[] getStringArray(String key) {
        return ConfigurationManager.getConfigInstance().getStringArray(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return ConfigurationManager.getConfigInstance().getString(key, defaultValue);
    }

    @Override
    public String getString(String key) {
        return ConfigurationManager.getConfigInstance().getString(key);
    }

    @Override
    public String getMandatoryString(String key) throws ConfigNotFoundException {
        assertKey(key);
        return ConfigurationManager.getConfigInstance().getString(key);
    }

    @Override
    public int getMandatoryInt(String key) throws ConfigNotFoundException {
        assertKey(key);
        return ConfigurationManager.getConfigInstance().getInt(key);
    }

    @Override
    public float getMandatoryFloat(String key) throws ConfigNotFoundException {
        assertKey(key);
        return ConfigurationManager.getConfigInstance().getFloat(key);
    }

    @Override
    public boolean getMandatoryBoolean(String key) throws ConfigNotFoundException {
        assertKey(key);
        return ConfigurationManager.getConfigInstance().getBoolean(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return ConfigurationManager.getConfigInstance().getInt(key, defaultValue);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return getFloat(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return ConfigurationManager.getConfigInstance().getBoolean(key,defaultValue);
    }

    @Override
    public void setProperty(String key, Object value) {
        ConfigurationManager.getConfigInstance().setProperty(key, value);
    }

}
