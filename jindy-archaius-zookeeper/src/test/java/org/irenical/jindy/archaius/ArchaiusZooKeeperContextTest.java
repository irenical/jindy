package org.irenical.jindy.archaius;

import org.irenical.jindy.test.JindyContextTest;
import org.junit.BeforeClass;

public class ArchaiusZooKeeperContextTest extends JindyContextTest {

  @BeforeClass
  public static void setDeploymentContext() {
    System.setProperty("archaius.deployment.applicationId", "test");
    System.setProperty("dynamicConfig", "false");
  }

}
