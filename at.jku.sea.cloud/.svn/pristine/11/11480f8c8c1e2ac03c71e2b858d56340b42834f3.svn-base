package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.exceptions.PropertyNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.PushConflictException;

public class TestTriggeredPushPull extends WorkspaceHierarchy {

  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.triggered, PropagationType.triggered);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.triggered, PropagationType.triggered);
  }

  @Test
  public void testPushArtifact() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    assertEquals(artifact.getId(), artifact2.getId());
    assertEquals(parent.getVersionNumber(), artifact2.getVersionNumber());
  }

  @Test
  public void testPushPropertyNew() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    Property property = artifact.createProperty(child1, PROPERTY);
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    assertFalse(artifact2.hasProperty(PROPERTY));
    child1.pushProperty(property);
    assertTrue(artifact2.hasProperty(PROPERTY));
  }

  @Test
  public void testPushPropertyUpdate() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    Property property = artifact.createProperty(child1, PROPERTY);
    property.setValue(child1, PROPERTY);
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    artifact2.createProperty(parent, PROPERTY);
    assertTrue(artifact2.hasProperty(PROPERTY));
    assertEquals(null, artifact2.getPropertyValue(PROPERTY));
    child1.pushProperty(property);
    assertTrue(artifact2.hasProperty(PROPERTY));
    assertEquals(PROPERTY, artifact2.getPropertyValue(PROPERTY));
  }

  @Test
  public void testPushArtifactAlive() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    artifact.delete(child1);
    child1.pushArtifact(artifact);
    assertFalse(artifact2.isAlive());
  }

  @Test
  public void testPushPropertyAlive() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    Property property = artifact.createProperty(child1, PROPERTY);
    property.setValue(child1, PROPERTY);
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    child1.pushProperty(property);
    assertTrue(artifact2.hasProperty(PROPERTY));
    assertEquals(PROPERTY, artifact2.getPropertyValue(PROPERTY));
    property.delete(child1);
    child1.pushProperty(property);
    assertFalse(artifact2.isPropertyAlive(PROPERTY));
  }

  @Test(expected = PropertyNotPushOrPullableException.class)
  public void testPushPropertyFailure() {
    Artifact artifact = child1.createArtifact();
    Property property = artifact.createProperty(child1, PROPERTY);
    child1.pushProperty(property);
  }

  @Test
  public void testPushAll() {
    Artifact artifact1 = child1.createArtifact();
    artifact1.setPropertyValue(child1, PROPERTY, PROPERTY);
    Artifact artifact2 = child2.createArtifact();
    artifact2.setPropertyValue(child2, PROPERTY, PROPERTY);
    child1.pushAll();
    child2.pushAll();
    Artifact pArtifact1 = parent.getArtifact(artifact1.getId());
    Artifact pArtifact2 = parent.getArtifact(artifact2.getId());
    assertTrue(pArtifact1.hasProperty(PROPERTY));
    assertTrue(pArtifact2.hasProperty(PROPERTY));
  }

  @Test
  public void testPushAllProperty() {
    Artifact artifact1 = child1.getArtifact(DataStorage.TRACE_LINK);
    artifact1.setPropertyValue(child1, PROPERTY, PROPERTY);
    child1.pushAll();
    Artifact pArtifact1 = parent.getArtifact(DataStorage.TRACE_LINK);
    assertTrue(pArtifact1.hasProperty(PROPERTY));
    assertEquals(PROPERTY, pArtifact1.getPropertyValue(PROPERTY));
  }

  @Test
  public void testPushAllEmptyWorkspace() {
    parent.pushAll();
  }

  @Test
  public void testPushArtifactCycle() {
    Artifact artifact1 = child1.createArtifact();
    Artifact artifact2 = child1.createArtifact(artifact1);
    artifact1.setType(child1, artifact2);
    child1.pushAll();
    Artifact pArtifact1 = parent.getArtifact(artifact1.getId());
    Artifact pArtifact2 = parent.getArtifact(artifact2.getId());
    assertEquals(artifact1.getId(), pArtifact2.getType().getId());
    assertEquals(artifact2.getId(), pArtifact1.getType().getId());
  }

  @Test
  public void testPushPropertyCycle() {
    Artifact artifact1 = child1.createArtifact();
    Artifact artifact2 = child1.createArtifact();
    artifact1.setPropertyValue(child1, PROPERTY, artifact2);
    artifact2.setPropertyValue(child1, PROPERTY, artifact1);
    child1.pushAll();
    Artifact pArtifact1 = parent.getArtifact(artifact1.getId());
    Artifact pArtifact2 = parent.getArtifact(artifact2.getId());
    assertEquals(artifact1.getId(), ((Artifact) pArtifact2.getPropertyValue(PROPERTY)).getId());
    assertEquals(artifact2.getId(), ((Artifact) pArtifact1.getPropertyValue(PROPERTY)).getId());
  }

  @Test(expected = PushConflictException.class)
  public void testPushConflict() {
    Artifact artifact1 = child1.createArtifact();
    artifact1.setPropertyValue(child1, PROPERTY, null);
    child1.pushAll();
    Artifact pArtifact1 = parent.getArtifact(artifact1.getId());
    pArtifact1.setPropertyValue(parent, PROPERTY, PROPERTY);
    artifact1.setPropertyValue(child1, PROPERTY, 1);
    child1.pushAll();
  }

  @Test
  public void testPullArtifact() {
    Artifact artifact = parent.createArtifact();
    child1.pullArtifact(artifact);
    Artifact artifact2 = child1.getArtifact(artifact.getId());
    assertNotNull(artifact2);
    assertEquals(artifact.getId(), artifact2.getId());
    assertEquals(child1.getVersionNumber(), artifact2.getVersionNumber());
  }

  @Test(expected = PropertyNotPushOrPullableException.class)
  public void testPullPropertyFailure() {
    Artifact artifact = parent.createArtifact();
    Property property = artifact.createProperty(parent, PROPERTY);
    child1.pullProperty(property);
  }

  @Test
  public void testPullPropertyNew() {
    Artifact artifact = parent.createArtifact();
    child1.pullArtifact(artifact);
    Property property = artifact.createProperty(parent, PROPERTY);
    Artifact artifact2 = child1.getArtifact(artifact.getId());
    assertFalse(artifact2.hasProperty(PROPERTY));
    child1.pullProperty(property);
    assertTrue(artifact2.hasProperty(PROPERTY));
  }

  @Test
  public void testPullPropertyUpdate() {
    Artifact artifact = parent.createArtifact();
    child1.pullArtifact(artifact);
    Property property = artifact.createProperty(parent, PROPERTY);
    property.setValue(parent, PROPERTY);
    Artifact artifact2 = child1.getArtifact(artifact.getId());
    artifact2.createProperty(child1, PROPERTY);
    assertTrue(artifact2.hasProperty(PROPERTY));
    assertEquals(null, artifact2.getPropertyValue(PROPERTY));
    child1.pullProperty(property);
    assertTrue(artifact2.hasProperty(PROPERTY));
    assertEquals(PROPERTY, artifact2.getPropertyValue(PROPERTY));
  }

  @Test
  public void testPullArtifactAlive() {
    Artifact artifact = parent.createArtifact();
    child1.pullArtifact(artifact);
    Artifact artifact2 = child1.getArtifact(artifact.getId());
    artifact.delete(parent);
    child1.pullArtifact(artifact);
    assertFalse(artifact2.isAlive());
  }

  @Test
  public void testPullPropertyAlive() {
    Artifact artifact = parent.createArtifact();
    child1.pullArtifact(artifact);
    Property property = artifact.createProperty(parent, PROPERTY);
    property.setValue(parent, PROPERTY);
    Artifact artifact2 = child1.getArtifact(artifact.getId());
    child1.pullProperty(property);
    assertTrue(artifact2.hasProperty(PROPERTY));
    assertEquals(PROPERTY, artifact2.getPropertyValue(PROPERTY));
    property.delete(parent);
    child1.pullProperty(property);
    assertFalse(artifact2.isPropertyAlive(PROPERTY));
  }

  @Test
  public void testPullAll() {
    Artifact artifact1 = parent.createArtifact();
    artifact1.setPropertyValue(parent, PROPERTY, PROPERTY);
    Artifact artifact2 = parent.createArtifact();
    artifact2.setPropertyValue(parent, PROPERTY, PROPERTY);
    child1.pullAll();
    Artifact pArtifact1 = child1.getArtifact(artifact1.getId());
    Artifact pArtifact2 = child1.getArtifact(artifact2.getId());
    assertTrue(pArtifact1.hasProperty(PROPERTY));
    assertTrue(pArtifact2.hasProperty(PROPERTY));
  }
  
  @Test
  public void testPullAllProperty() {
    Artifact artifact1 = parent.getArtifact(DataStorage.TRACE_LINK);
    artifact1.setPropertyValue(child1, PROPERTY, PROPERTY);
    child1.pushAll();
    Artifact pArtifact1 = child1.getArtifact(DataStorage.TRACE_LINK);
    assertTrue(pArtifact1.hasProperty(PROPERTY));
    assertEquals(PROPERTY, pArtifact1.getPropertyValue(PROPERTY));
  }


  @Test
  public void testResolvePushConflict() {
    Artifact artifact1 = child1.createArtifact();
    artifact1.setPropertyValue(child1, PROPERTY, null);
    child1.pushAll();
    Artifact pArtifact1 = parent.getArtifact(artifact1.getId());
    Property prop = pArtifact1.getProperty(PROPERTY);
    pArtifact1.setPropertyValue(parent, PROPERTY, PROPERTY);
    artifact1.setPropertyValue(child1, PROPERTY, 1);
    Map<Artifact, Set<Property>> diffs = child1.pullAll();
    assertEquals(1, diffs.size());
    Iterator<Entry<Artifact, Set<Property>>> iterator = diffs.entrySet().iterator();
    Entry<Artifact, Set<Property>> next = iterator.next();
    assertEquals(pArtifact1, next.getKey());
    assertEquals(1, next.getValue().size());
    assertEquals(prop, next.getValue().iterator().next());
    assertEquals(PROPERTY, artifact1.getPropertyValue(PROPERTY));
  }
}
