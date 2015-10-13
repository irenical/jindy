package org.irenical.jindy.archaius;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigFactory;
import org.junit.Assert;
import org.junit.Test;

public class ArchaiusTestGet {
    
    @Test
    public void testArchaius() {
        Config got = ConfigFactory.getConfig("application");
        Assert.assertEquals(got.getString("myprop1"), "one");
        Assert.assertEquals(got.getString("myprop2"), "2");
        Assert.assertEquals(got.getInt("myprop2", 0), 2);
        Assert.assertNull(got.getString("myprop3"));
    }
}
