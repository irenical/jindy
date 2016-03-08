package org.irenical.jindy.archaius;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigFactory;
import org.junit.Assert;
import org.junit.Test;

public class ArchaiusTestGet {

    @Test
    public void testArchaius() {
        Config got = ConfigFactory.getConfig("application");
        Assert.assertEquals("one", got.getString("myprop1"));
        Assert.assertEquals("2", got.getString("myprop2"));
        Assert.assertEquals(2, got.getInt("myprop2", 0));
        Assert.assertNull(got.getString("myprop3"));
    }

    @Test
    public void testPrefixedArchaius() throws Exception {
        Config got = ConfigFactory.getConfig("application");

        Config prefix1 = got.filterPrefix("prefix1");
        Assert.assertEquals("one-prefixed", prefix1.getString("myprop1"));
        Assert.assertNull(prefix1.getString("myprop2"));
    }

}
