package at.jku.sea.cloud.rest.client.test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.sql.SQLDataStorage;
import at.jku.sea.cloud.rest.client.RestCloud;
import at.jku.sea.cloud.rest.client.test.navigator.TestNavigator;
import at.jku.sea.cloud.rest.client.test.stream.TestStreams;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@RunWith(Suite.class)
// @SuiteClasses({ TestNavigator.class, TestStreams.class })
@SuiteClasses({ TestCloud.class, TestWorkspace.class, TestResultType.class, TestWorkspaceStateChange.class,
    TestArtifact.class, TestProperty.class, TestMetaModel.class, TestPackage.class, TestProject.class,
    TestCollectionArtifact.class, TestMapArtifact.class, TestOwner.class, TestTool.class, TestWorkspacePushPull.class,
    TestCloudReset.class, TestNavigator.class, TestStreams.class })
public class TestAll {

  public static DataStorage DATASTORAGE;

  public static String ADMIN_PASSWORD = "";

  public static Owner OWNER;
  public static Tool TOOL;

  public static Cloud CLOUD;
  public static Workspace WS;
  public static long WSID = -1;
  public static long EXPIRED_WSID;
  public static String WS_IDENTIFIER = "ws";

  public static Workspace EXPIRED_WS;
  public static String EXPIRED_WS_IDENTIFIER = "expired";

  public static String PROPERTY_VALUE = "DesignSpaceMetaModel";
  public static String PROPERTY_DOESNOTEXIST = "non existing property";
  public static String PROPERTY_VALUE_SETTER = "temporary value";

  public static DataStorage getInstance() {
    try {
      return (DataStorage) trmi.Naming.lookup("//localhost:1099/DataStorage");
    } catch (final Exception e) {
      return new SQLDataStorage();
    }
  }

  @BeforeClass
  public static void setUpBeforeClass() {
    DATASTORAGE = TestAll.getInstance();
    CLOUD = RestCloud.getInstance();

    OWNER = RestCloud.getInstance().getOwner(DataStorage.ADMIN);
    TOOL = RestCloud.getInstance().getTool(DataStorage.ECLIPSE_TOOL_ID);

    WS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, WS_IDENTIFIER);

    EXPIRED_WS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, EXPIRED_WS_IDENTIFIER);
    EXPIRED_WSID = EXPIRED_WS.getId();
    EXPIRED_WS.close();
  }
}
