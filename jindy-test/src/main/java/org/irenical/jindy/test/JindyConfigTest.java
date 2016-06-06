package org.irenical.jindy.test;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigContext;
import org.irenical.jindy.ConfigFactory;
import org.irenical.jindy.ConfigNotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JindyConfigTest {

  static final String P1 = "myprop1";
  static final String P2 = "myprop2";
  static final String V1 = "one";
  static final String V2 = "2";

  static final String P_P1 = "prefix1.myprop1";
  static final String P_P2 = "prefix2.myprop2";
  static final String P_V1 = "one-prefixed";
  static final String P_V2 = "two-prefixed";

  private static final String P3 = "prop3";

  protected Config config;

  @Before
  public void setupConfig() {
    config = ConfigFactory.getConfig();
    config.setProperty(P1, V1);
    config.setProperty(P2, V2);
    config.setProperty(P_P1, P_V1);
    config.setProperty(P_P2, P_V2);
  }

  @After
  public void clearConfig() {
    config.clear();
  }

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
  
  @Test
  public void testContextExists() {
    ConfigContext context = ConfigFactory.getContext();
    Assert.assertNotNull(context);
  }
  
  @Test
  public void testContextIsValid() {
    ConfigContext context = ConfigFactory.getContext();
    Assert.assertNotNull(context.getApplicationId());
  }

}
