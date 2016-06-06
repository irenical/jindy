package org.irenical.jindy.test;

import org.irenical.jindy.ConfigNotFoundException;
import org.junit.Assert;
import org.junit.Test;

public class JindyPropertyTest extends JindyBaseTest {

  @Test
  public void testGetString() {
    Assert.assertEquals(V1, config.getString(P1));
    Assert.assertEquals(V2, config.getString(P2));
    Assert.assertNull(config.getString(P3));
  }

  @Test
  public void testGetExistingMandatoryString() throws ConfigNotFoundException {
    Assert.assertEquals(V1, config.getMandatoryString(P1));
  }

  @Test(expected = ConfigNotFoundException.class)
  public void testGetNonexistingMandatoryString() throws ConfigNotFoundException {
    config.getMandatoryString(P3);
  }

  @Test
  public void testInt() {
    Assert.assertEquals(Integer.valueOf(V2).intValue(), config.getInt(P2, 0));
    Assert.assertNotEquals(V2, config.getInt(P3, 0));
    Assert.assertEquals(0, config.getInt(P3, 0));
  }

  @Test
  public void testSetProperty() {
    Assert.assertNull(config.getString("prop"));
    config.setProperty("prop", "erty");
    Assert.assertEquals("erty", config.getString("prop"));
  }

}
