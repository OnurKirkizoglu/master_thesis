package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.implementation.CloudFactory;

public class TestDelete {

  private Owner owner;
  private Tool tool;
  private static final Cloud cloud = CloudFactory.getInstance();
  private Workspace workspace;

  @Before
  public void setUp() throws Exception {
    this.owner = TestDelete.cloud.getOwner(DataStorage.ADMIN);
    if (this.owner == null) {
      this.owner = TestDelete.cloud.createOwner();
    }
    this.tool = TestDelete.cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    if (this.tool == null) {
      this.tool = TestDelete.cloud.createTool();
    }

    this.workspace = cloud.createWorkspace(owner, tool, "");
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testDeleteArtifactInWS() {
    final Artifact artifact = this.workspace.createArtifact();
    this.workspace.rollbackArtifact(artifact);
    this.workspace.getArtifact(artifact.getId());
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testDeleteArtifactPropertyInWS() {
    final Artifact artifact = this.workspace.createArtifact();
    artifact.setPropertyValue(this.workspace, "name", "blub");
    this.workspace.rollbackArtifact(artifact);
    this.workspace.getArtifact(artifact.getId());
  }

  @Test
  public void testDeleteArtifact() {
    final Artifact artifact = this.workspace.createArtifact();
    this.workspace.commitArtifact(artifact, "");
    artifact.delete(this.workspace);
    this.workspace.rollbackArtifact(artifact);
    assertTrue(this.workspace.getArtifact(artifact.getId()).isAlive());
  }
  //
  // @Test
  // public void commitArtifact() {
  // ArtifactGroup test = workspace.createArtifactGroup();
  // test.setPropertyValue(workspace, ModelAnalyzerTypeProperties.NAME, "http://sea.jku.at/test");
  // Artifact type = workspace.createArtifact();
  // type.setArtifactGroup(workspace, test);
  // workspace.commitArtifact(type);
  // assertTrue(workspace.getArtifacts().size() != 0);
  // }

}
