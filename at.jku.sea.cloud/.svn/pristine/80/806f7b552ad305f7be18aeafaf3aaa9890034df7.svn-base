package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.exceptions.WorkspaceConflictException;

public class TestWorkspaceSetPushPull extends WorkspaceHierarchy {

  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.triggered, PropagationType.triggered);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.triggered, PropagationType.triggered);
  }

  @Test
  public void testSetPush() {
    child1.setPush(PropagationType.instant);
    assertEquals(PropagationType.instant, child1.getPush());
  }

  @Test
  public void testSetPull() {
    child1.setPull(PropagationType.instant);
    assertEquals(PropagationType.instant, child1.getPull());
  }

  @Test(expected = WorkspaceConflictException.class)
  public void testSetPushWithConflict() {
    Artifact artifact = parent.getArtifact(DataStorage.TRACE_LINK);
    artifact.setPropertyValue(parent, PROPERTY, 1);
    artifact.setPropertyValue(child1, PROPERTY, 2);
    child1.setPush(PropagationType.instant);
  }

  @Test
  public void testSetPushToInstantWithPull() {
    Artifact artifact = parent.getArtifact(DataStorage.TRACE_LINK);
    artifact.setPropertyValue(parent, PROPERTY, 1);
    child1.setPull(PropagationType.instant);
    assertEquals(1, child1.getArtifact(artifact.getId()).getPropertyValue(PROPERTY));
  }

}
