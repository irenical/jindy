package org.irenical.jindy.archaius;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.irenical.jindy.Config;
import org.irenical.jindy.IConfigFactory;

/**
 * Creates a Netflix's Archaius based Config factory The returned config is a singleton
 *
 * @author tgsimao
 */
public abstract class ArchaiusBaseFactory implements IConfigFactory {

  private ArchaiusWrapper config;

  /**
   * Creates a Configuration with a initialized source and a scheduler already set
   */
  protected abstract AbstractConfiguration getConfiguration();

  @Override
  public Config createConfig(String name) {
    synchronized (ArchaiusBaseFactory.class) {
      if (config == null) {
        init();
        config = new ArchaiusWrapper();
      }
    }
    return config;
  }

  private void init() {
    AbstractConfiguration abstractConfig = getConfiguration();
    abstractConfig.addConfigurationListener(new ConfigurationListener() {
      @Override
      public void configurationChanged(ConfigurationEvent event) {
        config.fire(event.getPropertyName());
      }
    });

    ConcurrentCompositeConfiguration finalConfig = new ConcurrentCompositeConfiguration();
    finalConfig.addConfiguration(abstractConfig);

    ConfigurationManager.install(finalConfig);
  }

}
