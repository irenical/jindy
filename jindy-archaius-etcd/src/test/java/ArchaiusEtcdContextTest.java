import org.irenical.jindy.test.JindyContextTest;
import org.junit.BeforeClass;

public class ArchaiusEtcdContextTest extends JindyContextTest {

  @BeforeClass
  public static void setDeploymentContext() {
    System.setProperty("archaius.deployment.applicationId", "test");
    System.setProperty("dynamicConfig", "false");
  }

}
