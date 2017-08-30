package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Property;
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

public class TestTriggeredPushPullEvents extends WorkspaceHierarchy {
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
  public void testArtifactCreated() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
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
    assertEquals(parent.getId(), ((ArtifactCreated) parentChild2).origin);
  }

  @Test
  public void testArtifactCreatedWithPropertiesSet() {
    Artifact a1 = child1.createArtifact();
    Artifact a2 = child1.createArtifact();
    a1.setPropertyValue(child1, PROPERTY, 1);
    a1.setPropertyValue(child1, "reference", a2);
    a2.setPropertyValue(child1, "reference", a1);
    clearListeners();
    child1.pushAll();
    assertEquals(2, listenerParent.emittedChanges.size());
    Change[] array = listenerParent.emittedChanges.toArray(new Change[] {});
    // swap if necessary
    Change parentEvent = array[0];
    Change parentEvent1 = array[1];
    assertEquals(ChangeType.ArtifactCreated, parentEvent.getType());
    assertEquals(ChangeType.ArtifactCreated, parentEvent1.getType());
    ArtifactCreated artifactCreated = (ArtifactCreated) parentEvent;
    ArtifactCreated artifactCreated1 = (ArtifactCreated) parentEvent1;
    Artifact pA1 = artifactCreated.artifact.getId() < artifactCreated1.artifact.getId() ? artifactCreated.artifact : artifactCreated1.artifact;
    Artifact pA2 = artifactCreated.artifact.getId() < artifactCreated1.artifact.getId() ? artifactCreated1.artifact : artifactCreated.artifact;

    assertEquals(a1.getId(), pA1.getId());
    assertTrue(a1.hasProperty(PROPERTY));
    assertEquals(1, a1.getPropertyValue(PROPERTY));
    assertTrue(a1.hasProperty("reference"));
    assertEquals(a2.getId(), ((Artifact) a1.getPropertyValue("reference")).getId());
    assertEquals(a2.getId(), pA2.getId());
    assertTrue(a1.hasProperty("reference"));
    assertEquals(a1.getId(), ((Artifact) a2.getPropertyValue("reference")).getId());

  }

  @Test
  public void testArtifactType() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
    clearListeners();
    artifact.setType(child1, child1.getArtifact(DataStorage.COMPLEX_TYPE));
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
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
    assertEquals(parent.getId(), ((ArtifactTypeSet) parentChild2).origin);
  }

  @Test
  public void testArtifactPackage() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
    clearListeners();
    artifact.setPackage(child1, child1.getPackage(DataStorage.AT_PACKAGE_ID));
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
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
    assertEquals(parent.getId(), ((ArtifactContainerSet) parentChild2).origin);
  }

  @Test
  public void testArtifactAlive() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
    clearListeners();
    artifact.delete(child1);
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
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
    assertEquals(parent.getId(), ((ArtifactAliveSet) parentChild2).origin);
  }

  @Test
  public void testCollectionArtifactAddedElement() {
    CollectionArtifact artifact = child1.createCollection(false);
    artifact.addElement(child1, 2);
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
    assertEquals(2, listenerParent.emittedChanges.size());
    assertEquals(2, listenerChild1.emittedChanges.size());
    assertEquals(2, listenerChild2.emittedChanges.size());
    Iterator<Change> iterator = listenerParent.emittedChanges.iterator();
    Iterator<Change> iteratorChild1 = listenerChild1.emittedChanges.iterator();
    Iterator<Change> iteratorChild2 = listenerChild2.emittedChanges.iterator();
    Change parentEvent = iterator.next();
    assertEquals(ChangeType.ArtifactCreated, parentEvent.getType());
    Change parentChild1 = iteratorChild1.next();
    assertEquals(ChangeType.ArtifactCreated, parentChild1.getType());
    Change parentChild2 = iteratorChild2.next();
    assertEquals(ChangeType.ArtifactCreated, parentChild2.getType());
    parentEvent = iterator.next();
    assertEquals(ChangeType.CollectionAddedElement, parentEvent.getType());
    parentChild1 = iteratorChild1.next();
    assertEquals(ChangeType.CollectionAddedElement, parentChild1.getType());
    parentChild2 = iteratorChild2.next();
    assertEquals(ChangeType.CollectionAddedElement, parentChild2.getType());
    assertEquals(artifact.getId(), ((CollectionAddedElement) parentEvent).artifact.getId());
    assertEquals(artifact.getId(), ((CollectionAddedElement) parentChild1).artifact.getId());
    assertEquals(artifact.getId(), ((CollectionAddedElement) parentChild2).artifact.getId());
    assertEquals(2, ((CollectionAddedElement) parentEvent).value);
    assertEquals(2, ((CollectionAddedElement) parentChild1).value);
    assertEquals(2, ((CollectionAddedElement) parentChild2).value);

    assertEquals(child1.getId(), ((CollectionAddedElement) parentEvent).origin);
    assertEquals(child1.getId(), ((CollectionAddedElement) parentChild1).origin);
    assertEquals(parent.getId(), ((CollectionAddedElement) parentChild2).origin);
  }

  @Test
  public void testCollectionArtifactRemovedElement() {
    CollectionArtifact artifact = child1.createCollection(false);
    artifact.addElement(child1, 2);
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
    listenerParent.emittedChanges.clear();
    listenerChild1.emittedChanges.clear();
    listenerChild2.emittedChanges.clear();
    artifact.removeElement(child1, 2);
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
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
    assertEquals(parent.getId(), ((CollectionRemovedElement) parentChild2).origin);
  }

  @Test
  public void testCollectionArtifactComplex() {
    CollectionArtifact artifact = child1.createCollection(false);
    child1.pushArtifact(artifact);
    artifact.addElement(child1, 0);
    artifact.addElement(child1, 3);
    artifact.addElement(child1, 3);
    artifact.addElement(child1, 4);
    CollectionArtifact parentArtifact = parent.getCollectionArtifact(artifact.getId());
    parentArtifact.addElement(parent, 0);
    parentArtifact.addElement(parent, 3);
    parentArtifact.addElement(parent, 1);
    parentArtifact.addElement(parent, 2);
    parentArtifact.addElement(parent, 3);
    listenerParent.emittedChanges.clear();
    listenerChild1.emittedChanges.clear();
    child1.pushArtifact(artifact);
    assertEquals(3, listenerParent.emittedChanges.size());
    Iterator<Change> iterator = listenerParent.emittedChanges.iterator();
    Change parentEvent = iterator.next();
    assertEquals(ChangeType.CollectionRemovedElement, parentEvent.getType());
    assertEquals(1, ((CollectionRemovedElement) parentEvent).value);
    parentEvent = iterator.next();
    assertEquals(ChangeType.CollectionRemovedElement, parentEvent.getType());
    assertEquals(2, ((CollectionRemovedElement) parentEvent).value);
    parentEvent = iterator.next();
    assertEquals(ChangeType.CollectionAddedElement, parentEvent.getType());
    assertEquals(4, ((CollectionAddedElement) parentEvent).value);

  }

  @Test
  public void testCollectionArtifactOfArtifacts() {
    CollectionArtifact artifact = child1.createCollection(true);
    Artifact zero = child1.createArtifact();
    Artifact one = child1.createArtifact();
    Artifact two = child1.createArtifact();
    Artifact three = child1.createArtifact();
    Artifact four = child1.createArtifact();
    Artifact five = child1.createArtifact();
    child1.pushAll();
    CollectionArtifact parentArtifact = parent.getCollectionArtifact(artifact.getId());
    parentArtifact.addElement(parent, zero);
    parentArtifact.addElement(parent, one);
    parentArtifact.addElement(parent, two);
    parentArtifact.addElement(parent, four);
    parentArtifact.addElement(parent, five);
    listenerParent.emittedChanges.clear();
    artifact.addElement(child1, zero);
    artifact.addElement(child1, one);
    artifact.addElement(child1, two);
    artifact.addElement(child1, three);
    artifact.addElement(child1, three);
    listenerChild1.emittedChanges.clear();
    child1.pushArtifact(artifact);
    assertEquals(4, listenerParent.emittedChanges.size());
    Iterator<Change> iterator = listenerParent.emittedChanges.iterator();
    Change parentEvent = iterator.next();
    assertEquals(ChangeType.CollectionRemovedElement, parentEvent.getType());
    assertEquals(four.getId(), ((Artifact) ((CollectionRemovedElement) parentEvent).value).getId());
    parentEvent = iterator.next();
    assertEquals(ChangeType.CollectionRemovedElement, parentEvent.getType());
    assertEquals(five.getId(), ((Artifact) ((CollectionRemovedElement) parentEvent).value).getId());
    parentEvent = iterator.next();
    assertEquals(ChangeType.CollectionAddedElement, parentEvent.getType());
    assertEquals(three.getId(), ((Artifact) ((CollectionAddedElement) parentEvent).value).getId());
    parentEvent = iterator.next();
    assertEquals(ChangeType.CollectionAddedElement, parentEvent.getType());
    assertEquals(three.getId(), ((Artifact) ((CollectionAddedElement) parentEvent).value).getId());

  }

  @Test
  public void testPropertyValueSet() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
    Property prop = artifact.createProperty(child1, PROPERTY);
    clearListeners();
    prop.setValue(child1, PROPERTY);
    child1.pushProperty(prop);
    child2.pullProperty(prop);
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
    assertEquals(parent.getId(), ((PropertyValueSet) parentChild2).origin);
  }

  @Test
  public void testPropertyMapSet() {
    Artifact artifact = child1.createArtifact();
    Artifact artifact1 = child1.createArtifact();
    child1.pushAll();
    clearListeners();
    Map<String, Object> props = new HashMap<>();
    props.put(PROPERTY, 1);
    props.put("reference", artifact1);
    artifact.setPropertyValues(child1, props);
    child1.pushAll();
    assertEquals(1, listenerParent.emittedChanges.size());
    Change parentEvent = listenerParent.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyMapSet, parentEvent.getType());

    Collection<PropertyValueSet> changes = ((PropertyMapSet) parentEvent).changes;
    assertEquals(2, changes.size());
    Iterator<PropertyValueSet> iterator = changes.iterator();
    PropertyValueSet next = iterator.next();
    assertEquals(null, next.oldValue);
    assertEquals(1, next.value);
    next = iterator.next();
    assertEquals(null, next.oldValue);
    assertEquals(artifact1.getId(), ((Artifact) next.value).getId());

  }

  @Test
  public void testPropertyAliveSet() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    child2.pullArtifact(artifact);
    Property prop = artifact.createProperty(child1, PROPERTY);
    child1.pushProperty(prop);
    child2.pullProperty(prop);
    clearListeners();
    artifact.deleteProperty(child1, PROPERTY);
    child1.pushProperty(prop);
    child2.pullProperty(prop);
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
    assertEquals(parent.getId(), ((PropertyAliveSet) parentChild2).origin);
  }

}
