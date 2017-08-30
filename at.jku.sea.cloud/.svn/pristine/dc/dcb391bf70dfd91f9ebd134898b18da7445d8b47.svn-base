package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.CloudFactory;

public class TestMapArtifact {
  private Owner owner;
  private Tool tool;
  private static final Cloud cloud = CloudFactory.getInstance();
  private Workspace workspace;
  private static final String PROPERTY = "PROPERTY";
  private static final String DEAD_PROPERTY = "DEAD_PROPERTY";

  @Before
  public void setUp() throws Exception {
    this.owner = cloud.getOwner(DataStorage.ADMIN);
    if (this.owner == null) {
      this.owner = cloud.createOwner();
    }
    this.tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    if (this.tool == null) {
      this.tool = cloud.createTool();
    }

    this.workspace = cloud.createWorkspace(owner, tool, "");
  }
  
  
  @Test
  public void testMapArtifactPut(){
    MapArtifact map = this.workspace.createMap();
    Artifact key = this.workspace.createArtifact();
    Artifact value = this.workspace.createArtifact();
    assertNull(map.put(this.workspace, key, value));
    assertEquals(map.get(key), value);
  }
}
