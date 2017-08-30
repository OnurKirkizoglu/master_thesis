package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.PropagationType;

public class TestInitialization extends WorkspaceHierarchy {

  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.triggered, PropagationType.instant);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.instant, PropagationType.triggered);
  }

  @Test
  public void testInitialization() {
    assertEquals(null, parent.getParent());
    assertEquals(parent, child1.getParent());
    assertEquals(parent, child2.getParent());
    assertEquals(PropagationType.triggered, parent.getPull());
    assertEquals(PropagationType.triggered, parent.getPush());
    assertEquals(PropagationType.triggered, child1.getPush());
    assertEquals(PropagationType.instant, child1.getPull());
    assertEquals(PropagationType.instant, child2.getPush());
    assertEquals(PropagationType.triggered, child2.getPull());
  }

}
