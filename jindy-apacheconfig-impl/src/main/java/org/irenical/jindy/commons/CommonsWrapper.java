package org.irenical.jindy.commons;

import org.apache.commons.configuration.Configuration;
import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigChangedCallback;
import org.irenical.jindy.ConfigContext;
import org.irenical.jindy.ConfigNotFoundException;
import org.irenical.jindy.PropertyChangedCallback;

public class CommonsWrapper implements Config, ConfigContext {

  private final Configuration config;

  public CommonsWrapper(Configuration config) {
    this.config = config;
  }

  private void assertKey(String key) throws ConfigNotFoundException {
    if (!config.containsKey(key)) {
      throw new ConfigNotFoundException(
          "Mandatory configuration property '" + key + "' was not found");
    }
  }

  @Override
  public void unListen(ConfigChangedCallback callback) {
    // commons configuration is immutable, do nothing
  }

  @Override
  public void listen(String key, Match keyMatchingRule, PropertyChangedCallback callback) {
    // commons configuration is immutable, do nothing
  }

  @Override
  public void listen(String key, PropertyChangedCallback callback) {
    // commons configuration is immutable, do nothing
  }

  @Override
  public void listen(String key, Match keyMatchingRule, ConfigChangedCallback callback) {
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
    return config.getBoolean(key, defaultValue);
  }

  @Override
  public void setProperty(String key, Object value) {
    config.setProperty(key, value);
  }

  @Override
  public Iterable<String> getKeys(String keyPrefix) {
    return () -> keyPrefix == null ? config.getKeys() : config.getKeys(keyPrefix);
  }

  @Override
  public Config filterPrefix(String prefix) {
    return new CommonsWrapper(config.subset(prefix));
  }

  @Override
  public void clearProperty(String key) {
    config.clearProperty(key);
  }

  @Override
  public void clear() {
    config.clear();
  }

  @Override
  public String getApplicationId() {
    return config.getString("application");
  }

  @Override
  public String getDatacenter() {
    return config.getString("datacenter");
  }

  @Override
  public String getEnvironment() {
    return config.getString("environment");
  }

  @Override
  public String getRegion() {
    return config.getString("region");
  }

  @Override
  public String getServerId() {
    return config.getString("serverId");
  }

  @Override
  public String getStack() {
    return config.getString("stack");
  }

}
