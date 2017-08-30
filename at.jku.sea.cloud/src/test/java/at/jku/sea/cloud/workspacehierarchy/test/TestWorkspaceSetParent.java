package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.WorkspaceConflictException;
import at.jku.sea.cloud.exceptions.WorkspaceCycleException;

public class TestWorkspaceSetParent extends WorkspaceHierarchy {

  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.instant, PropagationType.instant);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.instant, PropagationType.instant);
  }

  @Test
  public void testSetParent() {
    child1.setParent(child2);
    assertEquals(child2, child1.getParent());
  }
  
  @Test
  public void testSetParentNull() {
    child1.setParent(null);
    assertEquals(null, child1.getParent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetParentToItself() {
    child1.setParent(child1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetParentWrongBaseVersion() {
    child1.setParent(null);
    parent.createArtifact();
    parent.commitAll("");
    child1.setParent(parent);
  }

  @Test(expected = WorkspaceConflictException.class)
  public void testSetParentWorkspaceConflict() {
    child1.setParent(null);
    child1.getArtifact(DataStorage.TEST_PROJECT_ID).setPropertyValue(child1, PROPERTY, "asdf");
    parent.getArtifact(DataStorage.TEST_PROJECT_ID).setPropertyValue(parent, PROPERTY, "asdf1");
    child1.setParent(parent);
  }

  @Test(expected = WorkspaceConflictException.class)
  public void testWorkspaceClose() {
    Workspace child3 = cloud.createWorkspace(owner, tool, "child3", PropagationType.triggered, PropagationType.instant);
    parent.getArtifact(DataStorage.TEST_PROJECT_ID).setPropertyValue(parent, PROPERTY, "asdf1");
    child3.setParent(child1);
    child3.getArtifact(DataStorage.TEST_PROJECT_ID).setPropertyValue(child3, PROPERTY, "asdf");
    try {
      child1.close();
    } catch (WorkspaceConflictException e) {
      assertFalse(child1.isClosed());
      child3.rollbackAll();
      child3.close();
      throw e;
    }
  }

  @Test(expected = WorkspaceCycleException.class)
  public void testWorkspaceCycle() {
    parent.setParent(child1);
  }
}
