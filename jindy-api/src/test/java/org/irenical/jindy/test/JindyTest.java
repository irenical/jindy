package org.irenical.jindy.test;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigBindingNotFoundException;
import org.irenical.jindy.ConfigFactory;
import org.irenical.jindy.InvalidConfigException;
import org.irenical.jindy.test.factories.ApacheTestFactory;
import org.irenical.jindy.test.factories.DumbTestFactory;
import org.irenical.jindy.test.factories.NullTestFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JindyTest {
    
    @Before
    public void clear(){
        ConfigFactory.clear();
    }

    @Test(expected = ConfigBindingNotFoundException.class)
    public void testNoBind() {
        ConfigFactory.getConfig();
    }

    @Test
    public void testFallbackBind() {
        ConfigFactory.setDefaultConfigFactory(new DumbTestFactory());
        Config got = ConfigFactory.getConfig();
        Assert.assertNotNull(got);
    }

    @Test(expected = ConfigBindingNotFoundException.class)
    public void testNamedNoBind() {
        ConfigFactory.getConfig("myconf");
    }

    @Test(expected = InvalidConfigException.class)
    public void testNullConfigFactory() {
        ConfigFactory.getConfig("myconf", new NullTestFactory());
    }

    @Test(expected = InvalidConfigException.class)
    public void testNullNamelessConfigFactory() {
        ConfigFactory.getConfig(null, new NullTestFactory());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDumbConfigFactory() {
        Config got = ConfigFactory.getConfig("myconf", new DumbTestFactory());
        Assert.assertNotNull(got);
        got.getString("myprop");
    }
    
    @Test
    public void testACCFile(){
        Config got = ConfigFactory.getConfig("myconf",new ApacheTestFactory());
        Assert.assertEquals(got.getString("myprop1"), "one");
        Assert.assertEquals(got.getString("myprop2"), "2");
        Assert.assertEquals(got.getInt("myprop2",0), 2);
        Assert.assertNull(got.getString("myprop3"));
    }

}
