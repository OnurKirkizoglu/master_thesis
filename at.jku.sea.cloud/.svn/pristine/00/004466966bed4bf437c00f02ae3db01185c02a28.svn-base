package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.listeners.WorkspaceAdapter;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactAliveSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactContainerSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCreated;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactTypeSet;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElement;
import at.jku.sea.cloud.listeners.events.artifact.CollectionRemovedElement;
import at.jku.sea.cloud.listeners.events.property.PropertyAliveSet;
import at.jku.sea.cloud.listeners.events.property.PropertyMapSet;
import at.jku.sea.cloud.listeners.events.property.PropertyValueSet;

public class TestInstantPushPullEvents extends WorkspaceHierarchy {
  private TestListener listenerChild1 = new TestListener();
  private TestListener listenerChild2 = new TestListener();
  private TestListener listenerParent = new TestListener();

  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.instant, PropagationType.instant);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.instant, PropagationType.instant);
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
  public void testArtifactCreated() {
    Artifact artifact = child1.createArtifact();
    assertEquals(1, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactCreated, parentEvent.getType());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactCreated, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactCreated, parentChild2.getType());
    assertEquals(artifact.getId(), ((ArtifactCreated) parentEvent).artifact.getId());
    assertEquals(artifact.getId(), ((ArtifactCreated) parentChild1).artifact.getId());
    assertEquals(artifact.getId(), ((ArtifactCreated) parentChild2).artifact.getId());
    assertEquals(child1.getId(), ((ArtifactCreated) parentEvent).origin);
    assertEquals(child1.getId(), ((ArtifactCreated) parentChild1).origin);
    assertEquals(child1.getId(), ((ArtifactCreated) parentChild2).origin);

  }

  @Test
  public void testArtifactType() {
    Artifact artifact = child1.createArtifact();
    clearListeners();
    artifact.setType(child1, child1.getArtifact(DataStorage.COMPLEX_TYPE));
    assertEquals(1, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactTypeSet, parentEvent.getType());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactTypeSet, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactTypeSet, parentChild2.getType());
    assertEquals(artifact.getId(), ((ArtifactTypeSet) parentEvent).artifact.getId());
    assertEquals(artifact.getId(), ((ArtifactTypeSet) parentChild1).artifact.getId());
    assertEquals(artifact.getId(), ((ArtifactTypeSet) parentChild2).artifact.getId());
    assertEquals(DataStorage.COMPLEX_TYPE, ((ArtifactTypeSet) parentEvent).artifactType.getId());
    assertEquals(DataStorage.COMPLEX_TYPE, ((ArtifactTypeSet) parentChild1).artifactType.getId());
    assertEquals(DataStorage.COMPLEX_TYPE, ((ArtifactTypeSet) parentChild2).artifactType.getId());
    assertEquals(child1.getId(), ((ArtifactTypeSet) parentEvent).origin);
    assertEquals(child1.getId(), ((ArtifactTypeSet) parentChild1).origin);
    assertEquals(child1.getId(), ((ArtifactTypeSet) parentChild2).origin);
  }

  @Test
  public void testArtifactPackage() {
    Artifact artifact = child1.createArtifact();
    clearListeners();
    artifact.setPackage(child1, child1.getPackage(DataStorage.AT_PACKAGE_ID));
    assertEquals(1, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactContainerSet, parentEvent.getType());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactContainerSet, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactContainerSet, parentChild2.getType());
    assertEquals(artifact.getId(), ((ArtifactContainerSet) parentEvent).artifact.getId());
    assertEquals(artifact.getId(), ((ArtifactContainerSet) parentChild1).artifact.getId());
    assertEquals(artifact.getId(), ((ArtifactContainerSet) parentChild2).artifact.getId());
    assertEquals(DataStorage.AT_PACKAGE_ID, ((ArtifactContainerSet) parentEvent).container.getId());
    assertEquals(DataStorage.AT_PACKAGE_ID, ((ArtifactContainerSet) parentChild1).container.getId());
    assertEquals(DataStorage.AT_PACKAGE_ID, ((ArtifactContainerSet) parentChild2).container.getId());

    assertEquals(child1.getId(), ((ArtifactContainerSet) parentEvent).origin);
    assertEquals(child1.getId(), ((ArtifactContainerSet) parentChild1).origin);
    assertEquals(child1.getId(), ((ArtifactContainerSet) parentChild2).origin);
  }

  @Test
  public void testArtifactAlive() {
    Artifact artifact = child1.createArtifact();
    clearListeners();
    artifact.delete(child1);
    assertEquals(1, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactAliveSet, parentEvent.getType());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactAliveSet, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactAliveSet, parentChild2.getType());
    assertEquals(artifact.getId(), ((ArtifactAliveSet) parentEvent).artifact.getId());
    assertEquals(artifact.getId(), ((ArtifactAliveSet) parentChild1).artifact.getId());
    assertEquals(artifact.getId(), ((ArtifactAliveSet) parentChild2).artifact.getId());
    assertEquals(false, ((ArtifactAliveSet) parentEvent).alive);
    assertEquals(false, ((ArtifactAliveSet) parentChild1).alive);
    assertEquals(false, ((ArtifactAliveSet) parentChild2).alive);

    assertEquals(child1.getId(), ((ArtifactAliveSet) parentEvent).origin);
    assertEquals(child1.getId(), ((ArtifactAliveSet) parentChild1).origin);
    assertEquals(child1.getId(), ((ArtifactAliveSet) parentChild2).origin);
  }

  @Test
  public void testCollectionArtifactAddedElement() {
    CollectionArtifact artifact = child1.createCollection(false);
    clearListeners();
    artifact.addElement(child1, 2);
    assertEquals(1, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.CollectionAddedElement, parentEvent.getType());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.CollectionAddedElement, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.CollectionAddedElement, parentChild2.getType());
    assertEquals(artifact.getId(), ((CollectionAddedElement) parentEvent).artifact.getId());
    assertEquals(artifact.getId(), ((CollectionAddedElement) parentChild1).artifact.getId());
    assertEquals(artifact.getId(), ((CollectionAddedElement) parentChild2).artifact.getId());
    assertEquals(2, ((CollectionAddedElement) parentEvent).value);
    assertEquals(2, ((CollectionAddedElement) parentChild1).value);
    assertEquals(2, ((CollectionAddedElement) parentChild2).value);

    assertEquals(child1.getId(), ((CollectionAddedElement) parentEvent).origin);
    assertEquals(child1.getId(), ((CollectionAddedElement) parentChild1).origin);
    assertEquals(child1.getId(), ((CollectionAddedElement) parentChild2).origin);
  }

  @Test
  public void testCollectionArtifactRemovedElement() {
    CollectionArtifact artifact = child1.createCollection(false);
    artifact.addElement(child1, 2);
    clearListeners();
    artifact.removeElement(child1, 2);
    assertEquals(1, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.CollectionRemovedElement, parentEvent.getType());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.CollectionRemovedElement, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.CollectionRemovedElement, parentChild2.getType());
    assertEquals(artifact.getId(), ((CollectionRemovedElement) parentEvent).artifact.getId());
    assertEquals(artifact.getId(), ((CollectionRemovedElement) parentChild1).artifact.getId());
    assertEquals(artifact.getId(), ((CollectionRemovedElement) parentChild2).artifact.getId());
    assertEquals(2, ((CollectionRemovedElement) parentEvent).value);
    assertEquals(2, ((CollectionRemovedElement) parentChild1).value);
    assertEquals(2, ((CollectionRemovedElement) parentChild2).value);

    assertEquals(child1.getId(), ((CollectionRemovedElement) parentEvent).origin);
    assertEquals(child1.getId(), ((CollectionRemovedElement) parentChild1).origin);
    assertEquals(child1.getId(), ((CollectionRemovedElement) parentChild2).origin);
  }

  @Test
  public void testPropertyValueSet() {
    Artifact artifact = child1.createArtifact();
    clearListeners();
    artifact.setPropertyValue(child1, PROPERTY, PROPERTY);
    assertEquals(1, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyValueSet, parentEvent.getType());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyValueSet, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyValueSet, parentChild2.getType());
    assertEquals(artifact.getId(), ((PropertyValueSet) parentEvent).property.getArtifact().getId());
    assertEquals(artifact.getId(), ((PropertyValueSet) parentChild1).property.getArtifact().getId());
    assertEquals(artifact.getId(), ((PropertyValueSet) parentChild2).property.getArtifact().getId());
    assertEquals(null, ((PropertyValueSet) parentEvent).oldValue);
    assertEquals(null, ((PropertyValueSet) parentChild1).oldValue);
    assertEquals(null, ((PropertyValueSet) parentChild2).oldValue);
    assertEquals(PROPERTY, ((PropertyValueSet) parentEvent).value);
    assertEquals(PROPERTY, ((PropertyValueSet) parentChild1).value);
    assertEquals(PROPERTY, ((PropertyValueSet) parentChild2).value);

    assertEquals(child1.getId(), ((PropertyValueSet) parentEvent).origin);
    assertEquals(child1.getId(), ((PropertyValueSet) parentChild1).origin);
    assertEquals(child1.getId(), ((PropertyValueSet) parentChild2).origin);
  }

  @Test
  public void testPropertyValuesSet() {
    Artifact artifact = child1.createArtifact();
    clearListeners();
    Map<String, Object> properties = new HashMap<>();
    properties.put(PROPERTY, PROPERTY);
    artifact.setPropertyValues(child1, properties);
    assertEquals(1, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyMapSet, parentEvent.getType());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyMapSet, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyMapSet, parentChild2.getType());
    assertEquals(1, ((PropertyMapSet) parentEvent).changes.size());
    assertEquals(1, ((PropertyMapSet) parentChild1).changes.size());
    assertEquals(1, ((PropertyMapSet) parentChild2).changes.size());
    PropertyValueSet parentValue = ((PropertyMapSet) parentEvent).changes.iterator().next();
    PropertyValueSet child1Value = ((PropertyMapSet) parentChild1).changes.iterator().next();
    PropertyValueSet child2Value = ((PropertyMapSet) parentChild2).changes.iterator().next();
    assertEquals(artifact.getId(), parentValue.property.getArtifact().getId());
    assertEquals(artifact.getId(), child1Value.property.getArtifact().getId());
    assertEquals(artifact.getId(), child2Value.property.getArtifact().getId());
    assertEquals(null, parentValue.oldValue);
    assertEquals(null, child1Value.oldValue);
    assertEquals(null, child2Value.oldValue);
    assertEquals(PROPERTY, parentValue.value);
    assertEquals(PROPERTY, child1Value.value);
    assertEquals(PROPERTY, child2Value.value);
  }

  @Test
  public void testPropertyAliveSet() {
    Artifact artifact = child1.createArtifact();
    artifact.setPropertyValue(child1, PROPERTY, PROPERTY);
    clearListeners();
    artifact.deleteProperty(child1, PROPERTY);
    assertEquals(1, listenerParent.emittedChanges.size());
    assertEquals(1, listenerChild1.emittedChanges.size());
    assertEquals(1, listenerChild2.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyAliveSet, parentEvent.getType());
    Change parentChild1 = listenerChild1.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyAliveSet, parentChild1.getType());
    Change parentChild2 = listenerChild2.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyAliveSet, parentChild2.getType());
    assertEquals(artifact.getId(), ((PropertyAliveSet) parentEvent).property.getArtifact().getId());
    assertEquals(artifact.getId(), ((PropertyAliveSet) parentChild1).property.getArtifact().getId());
    assertEquals(artifact.getId(), ((PropertyAliveSet) parentChild2).property.getArtifact().getId());
    assertEquals(false, ((PropertyAliveSet) parentEvent).alive);
    assertEquals(false, ((PropertyAliveSet) parentChild1).alive);
    assertEquals(false, ((PropertyAliveSet) parentChild2).alive);

    assertEquals(child1.getId(), ((PropertyAliveSet) parentEvent).origin);
    assertEquals(child1.getId(), ((PropertyAliveSet) parentChild1).origin);
    assertEquals(child1.getId(), ((PropertyAliveSet) parentChild2).origin);
  }

  private class TestListener extends WorkspaceAdapter {

    public List<Change> emittedChanges = new ArrayList<Change>();

    public void artifactCreated(final ArtifactCreated event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void artifactAliveSet(final ArtifactAliveSet event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void artifactTypeSet(final ArtifactTypeSet event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void artifactContainerSet(final ArtifactContainerSet event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void propertyAliveSet(final PropertyAliveSet event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void propertyValueSet(final PropertyValueSet event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void propertyMapSet(final PropertyMapSet event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void collectionAddedElement(final CollectionAddedElement event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void collectionRemovedElement(final CollectionRemovedElement event) throws RemoteException {
      this.emittedChanges.add(event);
    }
  }
}
