package org.irenical.jindy.test;

import org.irenical.jindy.ConfigContext;
import org.irenical.jindy.ConfigFactory;
import org.junit.Assert;
import org.junit.Test;

public class JindyContextTest extends JindyBaseTest {

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
