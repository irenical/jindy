package org.irenical.jindy.archaius;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArchaiusTest {
    
    @Before
    public void clear() {
        ConfigFactory.clear();
    }

    @Test
    public void testArchaius() {
        Config got = ConfigFactory.getConfig("application");
        Assert.assertEquals(got.getString("myprop1"), "one");
        Assert.assertEquals(got.getString("myprop2"), "2");
        Assert.assertEquals(got.getInt("myprop2", 0), 2);
        Assert.assertNull(got.getString("myprop3"));
    }

    @Test
    public void testSetProperty() {
        Config config = ConfigFactory.getConfig("application");
        Assert.assertNull(config.getString("prop"));
        config.setProperty("prop", "erty");
        Assert.assertEquals(config.getString("prop"), "erty");
    }
}
