package org.irenical.jindy.archaius;

import org.irenical.jindy.test.JindyListenerTest;
import org.junit.BeforeClass;

public class ArchaiusConsulListenerTest extends JindyListenerTest {

  @BeforeClass
  public static void setDeploymentContext() {
    System.setProperty("archaius.deployment.applicationId", "test");
    System.setProperty("dynamicConfig", "false");
  }

}
