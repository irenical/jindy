package org.irenical.jindy.test;

import org.junit.Assert;
import org.junit.Test;

public class JindyListenerTest extends JindyBaseTest {

  @Test
  public void testListen() {
    String [] gotValue = new String[1];
    config.listen(P1, p -> {
      gotValue[0]=config.getString(P1);
    });
    config.setProperty(P1, "CHANGED");
    Assert.assertEquals("CHANGED", gotValue[0]);
    config.setProperty(P1, "CHANGEDAGAIN");
    Assert.assertEquals("CHANGEDAGAIN", gotValue[0]);
  }
  
  @Test
  public void testUnlisten() {
    String [] gotValue = new String[1];
    String id = config.listen(P1, p -> {
      gotValue[0]=config.getString(P1);
    });
    config.setProperty(P1, "CHANGED");
    
    config.unListen(id);
    
    config.setProperty(P1, "CHANGEDAGAIN");
    
    Assert.assertEquals("CHANGED", gotValue[0]);
  }

}
