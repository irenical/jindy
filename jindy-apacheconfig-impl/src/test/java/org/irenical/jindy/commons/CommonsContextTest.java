package org.irenical.jindy.commons;

import org.irenical.jindy.ConfigFactory;
import org.irenical.jindy.test.JindyContextTest;
import org.junit.Before;

public class CommonsContextTest extends JindyContextTest {

  @Before
  public void setupContext() {
    ConfigFactory.getConfig().setProperty("application", "test");
  }

}
