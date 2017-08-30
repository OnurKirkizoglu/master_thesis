package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceParentSet;

public class TestWorkspaceSetParentEvent extends WorkspaceHierarchy {
  private TestListener listenerChild1 = new TestListener();
  private TestListener listenerChild2 = new TestListener();
  private TestListener listenerParent = new TestListener();
  private TestListener listenerGrandParent = new TestListener();
  private Workspace grandParent;

  @Before
  public void before() {
    super.before();
    grandParent = cloud.createWorkspace(owner, tool, "grandParent", PropagationType.triggered, PropagationType.triggered);
    parent = cloud.createWorkspace(owner, tool, "parent", grandParent, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.triggered, PropagationType.triggered);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.triggered, PropagationType.triggered);
    grandParent.addListener(listenerGrandParent);
    parent.addListener(listenerParent);
    child1.addListener(listenerChild1);
    child2.addListener(listenerChild2);
    clearListeners();
  }

  private void clearListeners() {
    listenerGrandParent.emittedChanges.clear();
    listenerParent.emittedChanges.clear();
    listenerChild1.emittedChanges.clear();
    listenerChild2.emittedChanges.clear();
  }
  
  @Test
  public void testWorkspaceParentSetEvent() {
    parent.close();
    assertEquals(0, listenerGrandParent.emittedChanges.size());
    assertEquals(0, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    
    Change child1Event = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.WorkspaceParentSet, child1Event.getType());
    Change child2Event = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.WorkspaceParentSet, child2Event.getType());
    
    assertEquals(child1.getId(), ((WorkspaceParentSet) child1Event).workspace.getId());
    assertEquals(child2.getId(), ((WorkspaceParentSet) child2Event).workspace.getId());
    assertEquals(grandParent.getId(), ((WorkspaceParentSet) child1Event).parent.getId());
    assertEquals(grandParent.getId(), ((WorkspaceParentSet) child2Event).parent.getId());
  }
  
  @After
  public void after() {
    grandParent.rollbackAll();
    child1.rollbackAll();
    child2.rollbackAll();
    grandParent.close();
    child1.close();
    child2.close();
  }
}
