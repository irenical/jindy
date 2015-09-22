package org.irenical.jindy.test;

import org.irenical.jindy.ConfigBindingNotFoundException;
import org.irenical.jindy.ConfigFactory;
import org.junit.Test;

public class JindyTest {
    
    @Test(expected=ConfigBindingNotFoundException.class)
    public void testNoBind(){
        ConfigFactory.getConfig();
    }

}
