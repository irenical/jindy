package org.irenical.jindy.archaius;

import com.netflix.config.ConfigurationManager;
import org.apache.commons.configuration.AbstractConfiguration;
import org.irenical.jindy.Config;
import org.irenical.jindy.IConfigFactory;

/**
 * Creates a Netflix's Archaius based Config factory The returned config is a singleton
 *
 * @author tgsimao
 */
public abstract class ArchaiusBaseFactory implements IConfigFactory {
  protected static final String DYNAMIC_CONFIG = "dynamicConfig";

  private static ArchaiusWrapper CONFIG;

  /**
   * Creates a Configuration with a initialized source and a scheduler already set
   */
  protected abstract AbstractConfiguration getConfiguration();

  @Override
  public Config createConfig(String name) {
    synchronized (ArchaiusBaseFactory.class) {
      if (CONFIG == null) {
        AbstractConfiguration configuration = getConfiguration();
        ConfigurationManager.install(configuration);
        CONFIG = new ArchaiusWrapper(configuration);
        configuration.addConfigurationListener(event -> {
          if(!event.isBeforeUpdate()) {
            CONFIG.fire(event.getPropertyName());
          }
        });
      }
    }
    return CONFIG;
  }
}
