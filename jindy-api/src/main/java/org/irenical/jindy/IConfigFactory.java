package org.irenical.jindy;

public interface IConfigFactory {
    
    /**
     * Creates and initializes a new instance of a Config object
     * @param name - the Config's name and unique identifier
     * @return the new Config instance
     */
    Config createConfig(String name);
    
}
