package org.irenical.jindy;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigFactory {

    public static final String DEFAULT_CONFIG_NAME = "DEFAULT";

    private static final Logger LOG = LoggerFactory.getLogger(ConfigFactory.class);

    private static final Map<String, Config> configs = new ConcurrentHashMap<>();

    /**
     * Returns a Config instance, instanciating it on first call.
     * 
     * @param name
     *            - the Config instance name
     * @return the Config instance with given name, instanciating one if needed
     */
    public static Config getConfig(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }
        Config got = configs.get(name);
        if (got == null) {
            LOG.debug("No Config instance named " + name + "... requesting a new one");
            // recheck in synchronized block to avoid double instantiation
            // will block concurrent calls to this Config until
            // the initialization is complete
            synchronized (name.intern()) {
                got = configs.get(name);
                if (got == null) {
                    got = load(name);
                    configs.put(name, got);
                }
            }
        }
        return got;
    }

    private static Config load(String name) {
        LOG.info("Looking for a IConfigFactory implementation");
        ServiceLoader<Config> loader = ServiceLoader.load(Config.class);
        Iterator<Config> implIterator = loader.iterator();
        Config got = null;
        if (implIterator.hasNext()) {
            got = implIterator.next();
        }
        
        // error on multiple bindings
        if (implIterator.hasNext()) {
            StringBuilder sb = new StringBuilder("Multiple bindings found on classpath for " + IConfigFactory.class.getName() + "[");
            boolean first = true;
            for (Config current : loader) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(current.getClass().getName());
                first = false;
            }
            sb.append("]");
            LOG.error(sb.toString());
            throw new ConfigMultipleBindingsException(sb.toString());
        }
        
        // ok on single binding
        if (got != null) {
            LOG.info("Found a IConfigFactory implementation: " + got.getClass().getName());
            return got;
        }
        
        // error on no binding
        LOG.error("No bindings found. Make sure you have an implementation class declared in META-INF/services/" + IConfigFactory.class.getName());
        throw new ConfigBindingNotFoundException("No bindings found. Make sure you have an implementation class declared in META-INF/services/" + IConfigFactory.class.getName());
    }

    /**
     * Returns the default Config, same as getConfig(DEFAULT_CONFIG_NAME)
     * 
     * @return the default Config instance
     */
    public static Config getConfig() {
        return getConfig(DEFAULT_CONFIG_NAME);
    }

}
