package org.irenical.jindy.archaius;

import org.apache.commons.configuration.Configuration;
import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigChangedCallback;
import org.irenical.jindy.ConfigNotFoundException;
import org.irenical.jindy.PropertyChangedCallback;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class ArchaiusWrapper implements Config {

  private final Map<String, Map<Match, List<Object>>> callbacksByMatch = new ConcurrentHashMap<>();

  private final Configuration configuration;

  public ArchaiusWrapper(Configuration configuration) {
    this.configuration = configuration;
  }

  private List<Object> getCallbacks(String key, Match match) {
    if (match == null) {
      match = Match.EXACT;
    }
    Map<Match, List<Object>> allMatches = callbacksByMatch.get(key);
    if (allMatches == null) {
      allMatches = new ConcurrentHashMap<>();
      if (null != callbacksByMatch.putIfAbsent(key, allMatches)) {
        allMatches = callbacksByMatch.get(key);
      }
    }
    List<Object> forKey = allMatches.get(match);
    if (forKey == null) {
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
    getCallbacks(key, keyMatchingRule).add(callback);
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
    getCallbacks(key, keyMatchingRule).add(callback);
  }

  @Override
  public void listen(String key, ConfigChangedCallback callback) {
    listen(key, Match.EXACT, callback);
  }

  @Override
  public void unListen(ConfigChangedCallback callback) {
    for (Map<Match, List<Object>> allMatches : new LinkedList<>(callbacksByMatch.values())) {
      for (Match match : new LinkedList<>(allMatches.keySet())) {
        List<Object> forKey = allMatches.get(match);
        while (forKey.remove(callback))
          ;
      }
    }
  }

  protected void fire(String key) {
    fireMatch(key, Match.EXACT);
    firePrefixOrSuffix(key);
  }

  private void firePrefixOrSuffix(String key) {
    for (String k : new LinkedList<>(callbacksByMatch.keySet())) {
      if (key.startsWith(k)) {
        fireMatch(key, Match.PREFIX);
      }
      if (key.endsWith(k)) {
        fireMatch(key, Match.SUFFIX);
      }
    }
  }

  private void fireMatch(String key, Match match) {
    List<Object> callbacks = getCallbacks(key, match);
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
      throw new ConfigNotFoundException("Mandatory configuration property '" + key + "' was not found");
    }
  }

  @Override
  public String[] getStringArray(String key) {
    return configuration.getStringArray(key);
  }

  @Override
  public String getString(String key, String defaultValue) {
    return configuration.getString(key, defaultValue);
  }

  @Override
  public String getString(String key) {
    return configuration.getString(key);
  }

  @Override
  public String getMandatoryString(String key) throws ConfigNotFoundException {
    assertKey(key);
    return configuration.getString(key);
  }

  @Override
  public int getMandatoryInt(String key) throws ConfigNotFoundException {
    assertKey(key);
    return configuration.getInt(key);
  }

  @Override
  public float getMandatoryFloat(String key) throws ConfigNotFoundException {
    assertKey(key);
    return configuration.getFloat(key);
  }

  @Override
  public boolean getMandatoryBoolean(String key) throws ConfigNotFoundException {
    assertKey(key);
    return configuration.getBoolean(key);
  }

  @Override
  public int getInt(String key, int defaultValue) {
    return configuration.getInt(key, defaultValue);
  }

  @Override
  public float getFloat(String key, float defaultValue) {
    return configuration.getFloat(key, defaultValue);
  }

  @Override
  public boolean getBoolean(String key, boolean defaultValue) {
    return configuration.getBoolean(key, defaultValue);
  }

  @Override
  public void setProperty(String key, Object value) {
    configuration.setProperty(key, value);
  }

  @Override
  public Iterable<String> getKeys(String keyPrefix) {
    return () -> keyPrefix == null ? configuration.getKeys() : configuration.getKeys(keyPrefix);
  }

  @Override
  public Config filterPrefix(String prefix) {
    return new ArchaiusWrapper( configuration.subset(prefix) );
  }
}
