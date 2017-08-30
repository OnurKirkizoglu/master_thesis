package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.OWNER;
import static at.jku.sea.cloud.rest.client.test.TestAll.TOOL;
import static org.junit.Assert.*;

import org.junit.Test;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.rest.client.RestCloud;
import at.jku.sea.cloud.rest.client.test.utility.ResetUtility;

public class TestCloudReset {

  private ResetUtility utility = new ResetUtility();

  @Test
  public void testReset() {
    OWNER = RestCloud.getInstance().getOwner(DataStorage.ADMIN);
    TOOL = RestCloud.getInstance().getTool(DataStorage.ECLIPSE_TOOL_ID);
    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, "");
    tempWS.createArtifact();
    tempWS.commitAll("");
    utility.resetCloud();
    assertEquals(1, RestCloud.getInstance().getHeadVersionNumber());
  }
}
