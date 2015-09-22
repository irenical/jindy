package org.irenical.jindy;


public interface Config {
    
    /**
     * Registers a listener on given property. It will be call whenever the property's value is changed
     * @param key - the property name
     * @param callback - the callback function
     * @return the listener ID. You will need this if you want to unlisten later on.
     */
    public String listen(String key, ConfigChangedCallback callback);

    /**
     * Unregisters listener with given ID
     * @param listenerId - the listener ID retrieved by the listen method earlier
     */
    public void unListen(String listenerId);

    /**
     * Returns the boolean value for given property
     * @param key - the property name
     * @return true or false
     * @throws ConfigNotFoundException - if the property is not set
     */
    public boolean getMandatoryBoolean(String key) throws ConfigNotFoundException;

    /**
     * Returns the boolean value for given property
     * @param key - the property name
     * @param defaultValue - the returned value if the property is not set
     * @return true, false or defaultValue if not defined
     */
    public boolean getBoolean(String key, boolean defaultValue);

    /**
     * Returns the float value for given property
     * @param key - the property name
     * @return the float value
     * @throws ConfigNotFoundException - if the property is not set
     */
    public float getMandatoryFloat(String key) throws ConfigNotFoundException;

    /**
     * Returns the float value for given property
     * @param key - the property name
     * @param defaultValue - the returned value if the property is not set
     * @return a float or defaultValue if none is defined
     */
    public float getFloat(String key, float defaultValue);

    /**
     * Returns the int value for given property
     * @param key - the property name
     * @return the integer value
     * @throws ConfigNotFoundException - if the property is not set
     */
    public int getMandatoryInt(String key) throws ConfigNotFoundException;

    /**
     * Returns the int value for given property
     * @param key - the property name
     * @param defaultValue - the returned value if the property is not set
     * @return an int or defaultValue if none is defined
     */
    public int getInt(String key, int defaultValue);

    /**
     * Returns the string value for given property
     * @param key - the property name
     * @return the string value (can be empty)
     * @throws ConfigNotFoundException - if the property is not set
     */
    public String getMandatoryString(String key) throws ConfigNotFoundException;

    /**
     * Returns the string value for given property
     * @param key - the property name
     * @return a string or null if none is defined
     */
    public String getString(String key);
    
    /**
     * Returns the string value for given property
     * @param key - the property name
     * @param defaultValue - the returned value if the property is not set
     * @return a string or defaultValue if none is defined
     */
    public String getString(String key, String defaultValue);

    /**
     * Returns the string array for given property
     * @param key - the property name
     * @return a string array
     */
    public String[] getStringArray(String key);
    
}
