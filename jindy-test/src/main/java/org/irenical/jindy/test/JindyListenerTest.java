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
  }

}
