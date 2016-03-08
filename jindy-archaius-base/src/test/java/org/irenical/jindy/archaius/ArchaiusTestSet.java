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
        Assert.assertEquals("erty", config.getString("prop"));
    }

    @Test
    public void testPrefixed() throws Exception {
        Config config = ConfigFactory.getConfig("application");

        Config prefix1 = config.filterPrefix("prefix1");
        Assert.assertEquals("one-prefixed", prefix1.getString("myprop1"));
        prefix1.setProperty("myprop1", "qwerty");
        Assert.assertEquals("qwerty", prefix1.getString("myprop1"));

        Assert.assertEquals("one", config.getString("myprop1"));
        Assert.assertEquals("qwerty", config.getString("prefix1.myprop1"));
    }
}
