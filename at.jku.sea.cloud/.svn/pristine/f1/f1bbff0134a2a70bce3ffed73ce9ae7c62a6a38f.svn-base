package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.CloudFactory;
import at.jku.sea.cloud.implementation.DefaultCloud;
import at.jku.sea.cloud.implementation.sql.TestDataStorage;

public class TestInstantPushPullCommitWSContents {
  protected static final TestDataStorage DATA_STORAGE = new TestDataStorage();
  protected Workspace parent;
  protected Workspace child1;
  protected Workspace child2;

  protected static final String PROPERTY = "property";

  protected static Cloud cloud = new DefaultCloud(DATA_STORAGE);
  protected Tool tool;
  protected Owner owner;

  @Before
  public void before() {
    cloud = CloudFactory.getInstance();
    this.owner = cloud.getOwner(DataStorage.ADMIN);
    this.tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.instant, PropagationType.instant);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.instant, PropagationType.instant);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.instant, PropagationType.instant);
  }

  @Test
  public void testCommitArtifactParent() {
    Artifact artifact = parent.createArtifact();
    long newVersion = parent.commitAll("");
    assertFalse(DATA_STORAGE.existsArtifactInVersion(child1.getId(), artifact.getId()));
    assertFalse(DATA_STORAGE.existsArtifactInVersion(child2.getId(), artifact.getId()));
  }

  @Test
  public void testArtifactParent() {
    Artifact artifact = parent.createArtifact();
    Property prop = artifact.createProperty(parent, "name");
    long newVersion = parent.commitAll("");
    assertFalse(DATA_STORAGE.existsArtifactInVersion(child1.getId(), artifact.getId()));
    assertFalse(DATA_STORAGE.existsPropertyInVersion(child1.getId(), artifact.getId(), "name"));
    assertFalse(DATA_STORAGE.existsArtifactInVersion(child2.getId(), artifact.getId()));
    assertFalse(DATA_STORAGE.existsPropertyInVersion(child2.getId(), artifact.getId(), "name"));
  }
  
  @Test
  public void testArtifactChild() {
    Artifact artifact = child1.createArtifact();
    Property prop = artifact.createProperty(parent, "name");
    long newVersion = child1.commitAll("");
    assertFalse(DATA_STORAGE.existsArtifactInVersion(parent.getId(), artifact.getId()));
    assertFalse(DATA_STORAGE.existsPropertyInVersion(parent.getId(), artifact.getId(), "name"));
    assertFalse(DATA_STORAGE.existsArtifactInVersion(child2.getId(), artifact.getId()));
    assertFalse(DATA_STORAGE.existsPropertyInVersion(child2.getId(), artifact.getId(), "name"));
  }
}
