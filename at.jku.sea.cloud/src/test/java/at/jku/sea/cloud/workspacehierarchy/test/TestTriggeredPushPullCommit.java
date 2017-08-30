package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Workspace;

public class TestTriggeredPushPullCommit extends WorkspaceHierarchy {

  private Workspace parent2;

  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.triggered, PropagationType.triggered);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.triggered, PropagationType.triggered);
    parent2 = cloud.createWorkspace(owner, tool, "parent2", null, PropagationType.triggered, PropagationType.instant);
  }

  @Test
  public void testCommitParent() {
    parent.createArtifact();
    long newVersion = parent.commitAll("");
    assertEquals(newVersion, child1.getBaseVersion().getVersionNumber());
    assertEquals(newVersion, child2.getBaseVersion().getVersionNumber());
  }
  
  @Test
  public void testCommitChild() {
    child1.createArtifact();
    long newVersion = child1.commitAll("");
    assertEquals(newVersion, parent.getBaseVersion().getVersionNumber());
    assertEquals(newVersion, child2.getBaseVersion().getVersionNumber());
  }

//  @Test
//  public void testUpdateOtherParent() {
//    parent.createArtifact();
//    long newVersion = parent.commitAll("");
//    assertEquals(newVersion, parent2.getBaseVersion().getVersionNumber());
//  }
}
