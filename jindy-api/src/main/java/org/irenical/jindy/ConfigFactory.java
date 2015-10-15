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

  private static Map<String, Config> configs;

  private synchronized static void init() {
    if (configs == null) {
      configs = new ConcurrentHashMap<>();
    }
  }

  /**
   * Returns a Config instance, instanciating it on first call. If no factory
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
      LOG.debug("No Config instance named " + name + "... requesting a new one");
      // recheck in synchronized block to avoid double instantiation
      // will block concurrent calls to this Config until
      // the initialization is complete
      synchronized (name.intern()) {
        got = configs.get(name);
        if (got == null) {
          got = factory == null ? load(name) : factory.createConfig(name);
          if (got == null) {
            LOG.error("Factory " + factory + " returned a null Config");
            throw new InvalidConfigException("Invalid Config returned by " + factory + " factory: null");
          }
          configs.put(name, got);
        }
      }
    }
    return got;
  }

  private static Config load(String name) {
    LOG.info("Looking for a IConfigFactory implementation");
    ServiceLoader<IConfigFactory> loader = ServiceLoader.load(IConfigFactory.class);
    Iterator<IConfigFactory> implIterator = loader.iterator();
    IConfigFactory got = null;
    if (implIterator.hasNext()) {
      got = implIterator.next();
    }

    // error on multiple bindings
    if (implIterator.hasNext()) {
      StringBuilder sb = new StringBuilder("Multiple bindings found on classpath for " + IConfigFactory.class.getName() + "[");
      boolean first = true;
      for (IConfigFactory current : loader) {
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
      return got.createConfig(name);
    }

    // error on no binding
    LOG.error("No bindings found. Make sure you have an implementation class declared in META-INF/services/" + IConfigFactory.class.getName());
    throw new ConfigBindingNotFoundException("No bindings found. Make sure you have an implementation class declared in META-INF/services/" + IConfigFactory.class.getName());
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
   * Returns the default Config, same as getConfig(null)
   * 
   * @return the default Config instance
   */
  public static Config getConfig() {
    return getConfig(null);
  }

  /**
   * Discards all Config instances
   */
  public static void clear() {
    if (configs != null) {
      configs.clear();
    }
  }

}
