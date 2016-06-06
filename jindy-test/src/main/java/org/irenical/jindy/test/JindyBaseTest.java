package org.irenical.jindy.test;

import org.irenical.jindy.Config;
import org.irenical.jindy.ConfigFactory;
import org.junit.After;
import org.junit.Before;

public class JindyBaseTest {

  static final String P1 = "myprop1";
  static final String P2 = "myprop2";
  static final String V1 = "one";
  static final String V2 = "2";

  static final String P_P1 = "prefix1.myprop1";
  static final String P_P2 = "prefix2.myprop2";
  static final String P_V1 = "one-prefixed";
  static final String P_V2 = "two-prefixed";

  static final String P3 = "prop3";

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

}
