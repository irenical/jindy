package org.irenical.jindy.archaius;

import org.irenical.jindy.test.JindyPrefixTest;
import org.junit.BeforeClass;

public class ArchaiusConsulPrefixTest extends JindyPrefixTest {

  @BeforeClass
  public static void setDeploymentContext() {
    System.setProperty("archaius.deployment.applicationId", "test");
    System.setProperty("dynamicConfig", "false");
  }

}
