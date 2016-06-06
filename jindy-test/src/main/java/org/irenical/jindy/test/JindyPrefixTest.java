package org.irenical.jindy.test;

import org.irenical.jindy.Config;
import org.junit.Assert;
import org.junit.Test;

public class JindyPrefixTest extends JindyBaseTest {

  @Test
  public void testGetPrefixedProperty() throws Exception {
    Config prefix1 = config.filterPrefix("prefix1");
    Assert.assertEquals(P_V1, prefix1.getString(P1));
    Assert.assertNull(prefix1.getString(P2));

    Config prefix2 = config.filterPrefix("prefix2");
    Assert.assertEquals(P_V2, prefix2.getString(P2));
  }

  @Test
  public void testSetPrefixedProperty() throws Exception {
    Config prefix1 = config.filterPrefix("prefix1");
    Assert.assertEquals(P_V1, prefix1.getString(P1));
    prefix1.setProperty(P1, "qwerty");
    Assert.assertEquals("qwerty", prefix1.getString(P1));

    Assert.assertEquals(V1, config.getString(P1));
    Assert.assertEquals("qwerty", config.getString(P_P1));
  }

}
