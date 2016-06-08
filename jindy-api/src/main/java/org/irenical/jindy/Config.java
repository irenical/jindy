package org.irenical.jindy;

public interface Config {

  /**
   * Possible matching rules
   */
  public enum Match {
    EXACT, PREFIX, SUFFIX
  }

  /**
   * Registers a listener on given property. It will be call whenever the
   * property's value is changed
   * 
   * @param key
   *          - the property name
   * @param keyMatchingRule
   *          - the key matching rule
   * @param callback
   *          - the callback function
   */
  public void listen(String key, Match keyMatchingRule, PropertyChangedCallback callback);

  /**
   * Registers a listener on given property. It will be call whenever the
   * property's value is changed
   * 
   * @param key
   *          - the property name
   * @param callback
   *          - the callback function
   */
  public void listen(String key, PropertyChangedCallback callback);

  /**
   * Registers a listener on given property. It will be call whenever the
   * property's value is changed
   * 
   * @param key
   *          - the property name
   * @param keyMatchingRule
   *          - the key matching rule
   * @param callback
   *          - the callback function
   */
  public void listen(String key, Match keyMatchingRule, ConfigChangedCallback callback);

  /**
   * Registers a listener on given property. It will be call whenever the
   * property's value is changed
   * 
   * @param key
   *          - the property name
   * @param callback
   *          - the callback function
   */
  public void listen(String key, ConfigChangedCallback callback);

  /**
   * Unregisters listener on all keys
   * 
   * @param callback
   *          - the callback function earlier registered with listen()
   */
  public void unListen(ConfigChangedCallback callback);

  /**
   * Returns the boolean value for given property
   * 
   * @param key
   *          - the property name
   * @return true or false
   * @throws ConfigNotFoundException
   *           - if the property is not set
   */
  public boolean getMandatoryBoolean(String key) throws ConfigNotFoundException;

  /**
   * Returns the boolean value for given property
   * 
   * @param key
   *          - the property name
   * @param defaultValue
   *          - the returned value if the property is not set
   * @return true, false or defaultValue if not defined
   */
  public boolean getBoolean(String key, boolean defaultValue);

  /**
   * Returns the float value for given property
   * 
   * @param key
   *          - the property name
   * @return the float value
   * @throws ConfigNotFoundException
   *           - if the property is not set
   */
  public float getMandatoryFloat(String key) throws ConfigNotFoundException;

  /**
   * Returns the float value for given property
   * 
   * @param key
   *          - the property name
   * @param defaultValue
   *          - the returned value if the property is not set
   * @return a float or defaultValue if none is defined
   */
  public float getFloat(String key, float defaultValue);

  /**
   * Returns the long value for given property
   *
   * @param key
   *          - the property name
   * @return the long value
   * @throws ConfigNotFoundException
   *           - if the property is not set
   */
  public long getMandatoryLong(String key) throws ConfigNotFoundException;

  /**
   * Returns the long value for given property
   *
   * @param key
   *          - the property name
   * @param defaultValue
   *          - the returned value if the property is not set
   * @return  a long or defaultValue if none is defined
     */
  public long getLong(String key, long defaultValue);

  /**
   * Returns the int value for given property
   * 
   * @param key
   *          - the property name
   * @return the integer value
   * @throws ConfigNotFoundException
   *           - if the property is not set
   */
  public int getMandatoryInt(String key) throws ConfigNotFoundException;

  /**
   * Returns the int value for given property
   * 
   * @param key
   *          - the property name
   * @param defaultValue
   *          - the returned value if the property is not set
   * @return an int or defaultValue if none is defined
   */
  public int getInt(String key, int defaultValue);

  /**
   * Returns the string value for given property
   * 
   * @param key
   *          - the property name
   * @return the string value (can be empty)
   * @throws ConfigNotFoundException
   *           - if the property is not set
   */
  public String getMandatoryString(String key) throws ConfigNotFoundException;

  /**
   * Returns the string value for given property
   * 
   * @param key
   *          - the property name
   * @return a string or null if none is defined
   */
  public String getString(String key);

  /**
   * Returns the string value for given property
   * 
   * @param key
   *          - the property name
   * @param defaultValue
   *          - the returned value if the property is not set
   * @return a string or defaultValue if none is defined
   */
  public String getString(String key, String defaultValue);

  /**
   * Returns the string array for given property
   * 
   * @param key
   *          - the property name
   * @return a string array
   */
  public String[] getStringArray(String key);

  /**
   * Set a property, this will replace any previously set values.
   * 
   * @param key
   *          the key/name of the property to alter
   * @param value
   *          the value to be altered
   */
  public void setProperty(String key, Object value);

  /**
   * Remove a property from the configuration.
   *
   * @param key
   *          the key to remove along with corresponding value.
   */
  void clearProperty(String key);

  /**
   * Remove all properties from the configuration.
   */
  void clear();

  /**
   * Get the list of the keys contained in the configuration. The returned
   * iterable can be used to obtain all defined keys.
   * 
   * @param keyPrefix
   * @return
   */
  public Iterable<String> getKeys(String keyPrefix);

  /**
   * Returns a Config instance with the prefixed properties
   * 
   * @param prefix
   *          the properties prefix to filter
   * @return a subset config
   */
  public Config filterPrefix(String prefix);

}
