package org.irenical.jindy.archaius;

import org.apache.commons.configuration.AbstractConfiguration;
import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigFactory;
import org.irenical.jindy.IConfigFactory;

import com.netflix.config.ConfigurationManager;

/**
 * Creates a Netflix's Archaius based Config factory The returned config is a
 * singleton
 *
 * @author tgsimao
 */
public abstract class ArchaiusBaseFactory implements IConfigFactory {
  protected static final String DYNAMIC_CONFIG = "dynamicConfig";

  private static ArchaiusWrapper CONFIG;

  /**
   * Creates a Configuration with a initialized source and a scheduler already
   * set
   */
  protected abstract <ERROR extends Exception> AbstractConfiguration getConfiguration() throws ERROR;

  @Override
  public Config createConfig(String name) {
    if (CONFIG != null) {
      return CONFIG;
    }
    synchronized (ArchaiusBaseFactory.class) {
      if (CONFIG == null) {
        AbstractConfiguration configuration = getConfiguration();
        ConfigurationManager.install(configuration);
        CONFIG = new ArchaiusWrapper(configuration);
        ConfigFactory.setContext(CONFIG);
        configuration.addConfigurationListener(event -> {
          if (!event.isBeforeUpdate()) {
            CONFIG.fire(event.getPropertyName());
          }
        });
      }
    }
    return CONFIG;
  }
}
