package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCreated;
import at.jku.sea.cloud.listeners.events.property.PropertyValueSet;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRebased;

public class TestTriggeredPushPullCommitEvents extends WorkspaceHierarchy {

  private TestListener listenerChild1 = new TestListener();
  private TestListener listenerChild2 = new TestListener();
  private TestListener listenerParent = new TestListener();

  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.triggered, PropagationType.triggered);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.triggered, PropagationType.triggered);
    parent.addListener(listenerParent);
    child1.addListener(listenerChild1);
    child2.addListener(listenerChild2);
    clearListeners();
  }

  private void clearListeners() {
    listenerParent.emittedChanges.clear();
    listenerChild1.emittedChanges.clear();
    listenerChild2.emittedChanges.clear();
  }

  @Test
  public void testCommitParentArtifactCreated() {
    Artifact artifact = parent.createArtifact();
    long newVersion = parent.commitAll("");
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.WorkspaceRebased, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.WorkspaceRebased, parentChild2.getType());
    WorkspaceRebased wsRebased1 = (WorkspaceRebased) parentChild1;
    WorkspaceRebased wsRebased2 = (WorkspaceRebased) parentChild2;
    assertEquals(1, wsRebased1.changes.size());
    assertEquals(1, wsRebased2.changes.size());
    Change child1 = wsRebased1.changes.iterator().next();
    Change child2 = wsRebased2.changes.iterator().next();
    assertEquals(ChangeType.ArtifactCreated, child1.getType());
    assertEquals(ChangeType.ArtifactCreated, child2.getType());
    assertEquals(artifact.getId(), ((ArtifactCreated) child1).artifact.getId());
    assertEquals(artifact.getId(), ((ArtifactCreated) child2).artifact.getId());
  }

  @Test
  public void testCommitParentPropertyValueSet() {
    Artifact artifact = parent.getArtifact(DataStorage.JAVA_OBJECT_TYPE);
    artifact.setPropertyValue(parent, PROPERTY, PROPERTY);
    long newVersion = parent.commitAll("");
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.WorkspaceRebased, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.WorkspaceRebased, parentChild2.getType());
    WorkspaceRebased wsRebased1 = (WorkspaceRebased) parentChild1;
    WorkspaceRebased wsRebased2 = (WorkspaceRebased) parentChild2;
    assertEquals(1, wsRebased1.changes.size());
    assertEquals(1, wsRebased2.changes.size());
    Change child1 = wsRebased1.changes.iterator().next();
    Change child2 = wsRebased2.changes.iterator().next();
    assertEquals(ChangeType.PropertyValueSet, child1.getType());
    assertEquals(ChangeType.PropertyValueSet, child2.getType());
    assertEquals(artifact.getId(), ((PropertyValueSet) child1).property.getArtifact().getId());
    assertEquals(artifact.getId(), ((PropertyValueSet) child2).property.getArtifact().getId());
    assertEquals(null, ((PropertyValueSet) child1).oldValue);
    assertEquals(null, ((PropertyValueSet) child2).oldValue);
    assertEquals(PROPERTY, ((PropertyValueSet) child1).value);
    assertEquals(PROPERTY, ((PropertyValueSet) child2).value);
    assertEquals(parent, wsRebased1.origin);
    assertEquals(parent, wsRebased2.origin);
  }
}
