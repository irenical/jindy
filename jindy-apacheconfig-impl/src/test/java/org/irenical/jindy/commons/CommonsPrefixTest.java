package org.irenical.jindy.commons;

import org.irenical.jindy.ConfigFactory;
import org.irenical.jindy.test.JindyPrefixTest;
import org.junit.Before;

public class CommonsPrefixTest extends JindyPrefixTest {

  @Before
  public void setupContext() {
    ConfigFactory.getConfig().setProperty("application", "test");
  }

}
