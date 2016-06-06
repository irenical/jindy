package org.irenical.jindy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigFactory {

  public static String DEFAULT_CONFIG_NAME;

  private static Logger LOG;

  private static Map<String, Config> configs;

  private static IConfigFactory defaultConfigFactory;
  
  private static ConfigContext context;

  private synchronized static void init() {
    if (configs == null) {
      DEFAULT_CONFIG_NAME = "DEFAULT";
      configs = new ConcurrentHashMap<>();
      LOG = LoggerFactory.getLogger(ConfigFactory.class);
    }
  }

  private static void log(boolean debug, String message, Exception error) {
    // during initialization, logging might not exist yet
    if (LOG != null) {
      if (error != null) {
        LOG.error(message, error);
      } else if (debug) {
        LOG.debug(message);
      } else {
        LOG.info(message);
      }
    }
  }

  /**
   * Returns the default Config, same as getConfig(null)
   *
   * @return the default Config instance
   */
  public static Config getConfig() {
    return getConfig(null);
  }

  /**
   * Returns Config for given name, same as getConfig(name,null)
   *
   * @param name
   *          - the Config name
   * @return the Config instance for given name
   */
  public static Config getConfig(String name) {
    return getConfig(name, null);
  }

  /**
   * Returns a Config instance, instantiating it on first call. If no factory
   * implementation is provided, one will be searched using Service Loader If a
   * Config with given name already exists, it will be simply returned
   * 
   * @param name
   *          - the Config instance name
   * @param factory
   *          - a custom factory implementation
   * @return the Config instance with given name, instanciating one if needed
   */
  public static Config getConfig(String name, IConfigFactory factory) {
    if (configs == null) {
      init();
    }
    if (name == null) {
      name = DEFAULT_CONFIG_NAME;
    }

    Config got = configs.get(name);
    if (got == null) {
      log(true, "No Config instance named " + name + "... requesting a new one", null);
      // recheck in synchronized block to avoid double instantiation
      // will block concurrent calls to this Config until
      // the initialization is complete
      synchronized (name.intern()) {
        got = configs.get(name);
        if (got == null) {
          got = factory == null ? load(name) : factory.createConfig(name);
          if (got == null) {
            log(false, "Factory " + factory + " returned a null Config", null);
            throw new InvalidConfigException(
                "Invalid Config returned by " + factory + " factory: null");
          }
          configs.put(name, got);
        }
      }
    }
    return got;
  }

  private static Config load(String name) {
    log(false, "Looking for a IConfigFactory implementation", null);
    ServiceLoader<IConfigFactory> loader = ServiceLoader.load(IConfigFactory.class);
    Iterator<IConfigFactory> implIterator = loader.iterator();
    IConfigFactory got = null;
    if (implIterator.hasNext()) {
      got = implIterator.next();
    }

    // error on multiple bindings
    if (implIterator.hasNext()) {
      StringBuilder sb = new StringBuilder(
          "Multiple bindings found on classpath for " + IConfigFactory.class.getName() + "[");
      boolean first = true;
      for (IConfigFactory current : loader) {
        if (!first) {
          sb.append(",");
        }
        sb.append(current.getClass().getName());
        first = false;
      }
      sb.append("]");
      log(false, sb.toString(), null);
      throw new ConfigMultipleBindingsException(sb.toString());
    }

    // ok on single binding
    if (got != null) {
      log(false, "Found a IConfigFactory implementation: " + got.getClass().getName(), null);
      return got.createConfig(name);
    }

    synchronized (ConfigFactory.class) {
      if (defaultConfigFactory != null) {
        return defaultConfigFactory.createConfig(name);
      }
    }

    // error on no binding
    log(false,
        "No bindings found. Make sure you have an implementation class declared in META-INF/services/"
            + IConfigFactory.class.getName(),
        null);
    throw new ConfigBindingNotFoundException(
        "No bindings found. Make sure you have an implementation class declared in META-INF/services/"
            + IConfigFactory.class.getName());
  }

  public synchronized static void setDefaultConfigFactory(IConfigFactory factory) {
    defaultConfigFactory = factory;
  }
  
  public static void setContext(ConfigContext context) {
    ConfigFactory.context = context;
  }
  
  public static ConfigContext getContext() {
    return context;
  }

  /**
   * Discards all Config instances
   */
  public synchronized static void clear() {
    if (configs != null) {
      configs.clear();
    }

    defaultConfigFactory = null;
  }

}
