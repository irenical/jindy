package org.irenical.jindy.commons;

import org.irenical.jindy.ConfigFactory;
import org.irenical.jindy.test.JindyPropertyTest;
import org.junit.Before;

public class CommonsPropertyTest extends JindyPropertyTest {

  @Before
  public void setupContext() {
    ConfigFactory.getConfig().setProperty("application", "test");
  }

}
