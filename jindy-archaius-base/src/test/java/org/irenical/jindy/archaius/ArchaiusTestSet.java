package org.irenical.jindy.archaius;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigFactory;
import org.junit.Assert;
import org.junit.Test;

public class ArchaiusTestSet {
    
    @Test
    public void testSetProperty() {
        Config config = ConfigFactory.getConfig("application");
        Assert.assertNull(config.getString("prop"));
        config.setProperty("prop", "erty");
        Assert.assertEquals(config.getString("prop"), "erty");
    }
}
