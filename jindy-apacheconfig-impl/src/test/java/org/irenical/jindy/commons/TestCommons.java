package org.irenical.jindy.commons;

import org.irenical.jindy.test.JindyConfigTest;
import org.junit.BeforeClass;

public class TestCommons extends JindyConfigTest {
  
  @BeforeClass
  public static void setupContext(){
    System.setProperty("application", "test");
  }

}
