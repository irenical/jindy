package org.irenical.jindy.archaius;

import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.irenical.jindy.Config;
import org.irenical.jindy.IConfigFactory;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;

/**
 * Creates a Netflix's Archaius based Config factory The returned config is a
 * singleton
 * 
 * @author tgsimao
 *
 */
public abstract class ArchaiusBaseFactory implements IConfigFactory {

    private ArchaiusWrapper config;

    /**
     * Creates a Dynamic Configuration with a initialized source and a scheduler already set
     */
    protected abstract DynamicConfiguration getDynamicConfiguration();

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
        DynamicConfiguration dynamicConfig = getDynamicConfiguration();
        dynamicConfig.addConfigurationListener(new ConfigurationListener() {
            @Override
            public void configurationChanged(ConfigurationEvent event) {
                config.fire(event.getPropertyName());
            }
        });

        ConcurrentCompositeConfiguration finalConfig = new ConcurrentCompositeConfiguration();
        finalConfig.addConfiguration(dynamicConfig);

        ConfigurationManager.install(finalConfig);
    }

}
