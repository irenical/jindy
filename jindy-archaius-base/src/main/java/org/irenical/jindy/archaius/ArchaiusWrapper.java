package org.irenical.jindy.archaius;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.configuration.Configuration;
import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigChangedCallback;
import org.irenical.jindy.ConfigContext;
import org.irenical.jindy.ConfigNotFoundException;
import org.irenical.jindy.PropertyChangedCallback;

import com.netflix.config.ConfigurationManager;

public class ArchaiusWrapper implements Config, ConfigContext {

  private final Map<String, Map<Match, List<Object>>> callbacksByMatch;

  private final Configuration configuration;

  private final String prefix;

  private final ArchaiusWrapper parent;

  public ArchaiusWrapper(String prefix, ArchaiusWrapper parent) {
    this.parent = parent;
    this.configuration = parent.configuration;
    this.prefix = prefix == null ? "" : prefix;
    this.callbacksByMatch = null;
  }

  public ArchaiusWrapper(Configuration configuration) {
    this.parent = null;
    this.configuration = configuration;
    this.prefix = "";
    callbacksByMatch = new ConcurrentHashMap<>();
  }

  private Map<String, Map<Match, List<Object>>> getAllCallbacks() {
    return parent != null ? parent.getAllCallbacks() : callbacksByMatch;
  }

  private List<Object> getCallbacks(String key, Match match, boolean create) {
    Map<String, Map<Match, List<Object>>> callbacksByMatch = getAllCallbacks();
    if (match == null) {
      match = Match.EXACT;
    }
    Map<Match, List<Object>> allMatches = callbacksByMatch.get(key);
    if (allMatches == null) {
      if (!create) {
        return null;
      }
      allMatches = new ConcurrentHashMap<>();
      if (null != callbacksByMatch.putIfAbsent(key, allMatches)) {
        allMatches = callbacksByMatch.get(key);
      }
    }
    List<Object> forKey = allMatches.get(match);
    if (forKey == null) {
      if (!create) {
        return null;
      }
      forKey = new CopyOnWriteArrayList<>();
      if (null != allMatches.putIfAbsent(match, forKey)) {
        forKey = allMatches.get(match);
      }
    }
    return forKey;
  }

  @Override
  public void listen(String key, Match keyMatchingRule, PropertyChangedCallback callback) {
    if (callback == null) {
      throw new IllegalArgumentException("Callback cannot be null");
    }
    PropertyChangedCallback prefixedCallback = null;
    if (parent != null) {
      prefixedCallback = (k) -> {
        callback.propertyChanged(k.substring(prefix.length()));
      };
    }
    getCallbacks(prefix + key, keyMatchingRule, true)
        .add(prefixedCallback == null ? callback : prefixedCallback);
  }

  @Override
  public void listen(String key, PropertyChangedCallback callback) {
    listen(key, Match.EXACT, callback);
  }

  @Override
  public void listen(String key, Match keyMatchingRule, ConfigChangedCallback callback) {
    if (callback == null) {
      throw new IllegalArgumentException("Callback cannot be null");
    }
    getCallbacks(prefix + key, keyMatchingRule, true).add(callback);
  }

  @Override
  public void listen(String key, ConfigChangedCallback callback) {
    listen(key, Match.EXACT, callback);
  }

  @Override
  public void unListen(ConfigChangedCallback callback) {
    for (Map<Match, List<Object>> allMatches : new LinkedList<>(getAllCallbacks().values())) {
      for (Match match : new LinkedList<>(allMatches.keySet())) {
        List<Object> forKey = allMatches.get(match);
        while (forKey.remove(callback))
          ;
      }
    }
  }

  protected void fire(String key) {
    fireMatch(prefix + key, Match.EXACT);
    firePrefixOrSuffix(prefix + key);
  }

  private void firePrefixOrSuffix(String key) {
    for (String k : new LinkedList<>(getAllCallbacks().keySet())) {
      if (key.startsWith(k)) {
        fireMatch(k, Match.PREFIX);
      }
      if (key.endsWith(k)) {
        fireMatch(k, Match.SUFFIX);
      }
    }
  }

  private void fireMatch(String key, Match match) {
    List<Object> callbacks = getCallbacks(key, match, false);
    if (callbacks != null) {
      for (Object callback : callbacks) {
        if (callback instanceof PropertyChangedCallback) {
          ((PropertyChangedCallback) callback).propertyChanged(key);
        } else if (callback instanceof ConfigChangedCallback) {
          ((ConfigChangedCallback) callback).propertyChanged();
        } else {
          throw new RuntimeException("Unsupported callback object: " + callback);
        }
      }
    }
  }

  private void assertKey(String key) throws ConfigNotFoundException {
    if (!configuration.containsKey(key)) {
      throw new ConfigNotFoundException(
          "Mandatory configuration property '" + key + "' was not found");
    }
  }

  @Override
  public String[] getStringArray(String key) {
    return configuration.getStringArray(prefix + key);
  }

  @Override
  public String getString(String key, String defaultValue) {
    return configuration.getString(prefix + key, defaultValue);
  }

  @Override
  public String getString(String key) {
    return configuration.getString(prefix + key);
  }

  @Override
  public String getMandatoryString(String key) throws ConfigNotFoundException {
    key = prefix + key;
    assertKey(key);
    return configuration.getString(key);
  }

  @Override
  public int getMandatoryInt(String key) throws ConfigNotFoundException {
    key = prefix + key;
    assertKey(key);
    return configuration.getInt(key);
  }

  @Override
  public float getMandatoryFloat(String key) throws ConfigNotFoundException {
    key = prefix + key;
    assertKey(key);
    return configuration.getFloat(key);
  }

  @Override
  public boolean getMandatoryBoolean(String key) throws ConfigNotFoundException {
    key = prefix + key;
    assertKey(key);
    return configuration.getBoolean(key);
  }

  @Override
  public int getInt(String key, int defaultValue) {
    return configuration.getInt(prefix + key, defaultValue);
  }

  @Override
  public float getFloat(String key, float defaultValue) {
    return configuration.getFloat(prefix + key, defaultValue);
  }

  @Override
  public boolean getBoolean(String key, boolean defaultValue) {
    return configuration.getBoolean(prefix + key, defaultValue);
  }

  @Override
  public void setProperty(String key, Object value) {
    configuration.setProperty(prefix + key, value);
  }

  @Override
  public Iterable<String> getKeys(String keyPrefix) {
    return () -> keyPrefix == null
        ? (prefix == null ? configuration.getKeys() : configuration.getKeys(prefix))
        : configuration.getKeys(prefix + keyPrefix);
  }

  @Override
  public Config filterPrefix(String prefix) {
    return new ArchaiusWrapper((prefix == null || prefix.endsWith(".")) ? prefix : (prefix + "."),
        this);
  }

  @Override
  public void clearProperty(String key) {
    configuration.clearProperty(key);
  }

  @Override
  public void clear() {
    configuration.clear();
  }

  @Override
  public String getApplicationId() {
    return ConfigurationManager.getDeploymentContext().getApplicationId();
  }

  @Override
  public String getDatacenter() {
    return ConfigurationManager.getDeploymentContext().getDeploymentDatacenter();
  }

  @Override
  public String getEnvironment() {
    return ConfigurationManager.getDeploymentContext().getDeploymentEnvironment();
  }

  @Override
  public String getRegion() {
    return ConfigurationManager.getDeploymentContext().getDeploymentRegion();
  }

  @Override
  public String getServerId() {
    return ConfigurationManager.getDeploymentContext().getDeploymentServerId();
  }

  @Override
  public String getStack() {
    return ConfigurationManager.getDeploymentContext().getDeploymentStack();
  }

}
