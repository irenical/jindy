package org.irenical.jindy.archaius;

import com.netflix.config.DynamicConfiguration;

public class ArchaiusConfigurationExample extends ArchaiusBaseFactory {
    
    @Override
    protected DynamicConfiguration getDynamicConfiguration() {
        return new DynamicConfiguration();
    }

}
