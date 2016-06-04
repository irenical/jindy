package org.irenical.jindy.common;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JindyCommonsTest {

    @Before
    public void clear() {
        ConfigFactory.clear();
    }

    @Test
    public void testACCFile() {
        Config got = ConfigFactory.getConfig("application");
        Assert.assertEquals("one", got.getString("myprop1"));
        Assert.assertEquals("2", got.getString("myprop2"));
        Assert.assertEquals(2, got.getInt("myprop2", 0));
        Assert.assertNull(got.getString("myprop3"));
    }

    @Test
    public void testSetProperty() {
        Config config = ConfigFactory.getConfig("application");
        Assert.assertNull(config.getString("prop"));
        config.setProperty("prop", "erty");
        Assert.assertEquals("erty", config.getString("prop"));
    }

    @Test
    public void testGetPrefixedProperty() throws Exception {
        Config config = ConfigFactory.getConfig("application");

        Config prefix1 = config.filterPrefix("prefix1");
        Assert.assertEquals("one-prefixed", prefix1.getString("myprop1"));
        Assert.assertNull(prefix1.getString("myprop2"));

        Config prefix2 = config.filterPrefix("prefix2");
        Assert.assertEquals("two-prefixed", prefix2.getString("myprop1"));
    }

    @Test
    public void testSetPrefixedProperty() throws Exception {
        Config config = ConfigFactory.getConfig("application");

        Config prefix1 = config.filterPrefix("prefix1");
        Assert.assertEquals("one-prefixed", prefix1.getString("myprop1"));
        prefix1.setProperty("myprop1", "qwerty");
        Assert.assertEquals("qwerty", prefix1.getString("myprop1"));

        Assert.assertEquals("one", config.getString("myprop1"));
        Assert.assertEquals("qwerty", config.getString("prefix1.myprop1"));
    }

}
