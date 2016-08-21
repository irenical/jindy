import org.irenical.jindy.test.JindyPropertyTest;
import org.junit.BeforeClass;

public class ArchaiusEtcdPropertyTest extends JindyPropertyTest {

  @BeforeClass
  public static void setDeploymentContext() {
    System.setProperty("archaius.deployment.applicationId", "test");
    System.setProperty("dynamicConfig", "false");
  }

}
