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

public class JindyApiTest {
    
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
        Assert.assertEquals("one", got.getString("myprop1"));
        Assert.assertEquals("2", got.getString("myprop2"));
        Assert.assertEquals(2, got.getInt("myprop2",0));
        Assert.assertNull(got.getString("myprop3"));
    }

    @Test
    public void testPrefixed() throws Exception {
        Config got = ConfigFactory.getConfig("test", new ApacheTestFactory());
        Assert.assertEquals("one", got.getString("myprop1"));
        Assert.assertEquals("prefixed-one-myprop", got.getString("prefix1.myprop1"));

        Config prefix1 = got.filterPrefix("prefix1");
        Assert.assertNotNull(prefix1);
        Assert.assertEquals("prefixed-one-myprop", prefix1.getString("myprop1"));
        Assert.assertNull(prefix1.getString("myprop2"));

        Config prefix2 = got.filterPrefix("prefix2");
        Assert.assertNotNull(prefix2);
        Assert.assertEquals("prefixed-two-myprop", prefix2.getString("myprop1"));
        Assert.assertEquals("prefixed-2", prefix2.getString("myprop2"));
    }

}
