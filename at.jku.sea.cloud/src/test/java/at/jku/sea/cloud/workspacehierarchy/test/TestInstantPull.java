package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Property;

public class TestInstantPull extends WorkspaceHierarchy {
  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.triggered, PropagationType.instant);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.triggered, PropagationType.instant);
  }

  @Test
  public void testPullArtifact() {
    Artifact artifact = parent.createArtifact();
    Artifact artifact2 = child1.getArtifact(artifact.getId());
    assertEquals(artifact.getId(), artifact2.getId());
    assertEquals(child1.getVersionNumber(), artifact2.getVersionNumber());
  }

  @Test
  public void testPullPropertyNew() {
    Artifact artifact = parent.createArtifact();
    Artifact artifact2 = child1.getArtifact(artifact.getId());
    assertFalse(artifact2.hasProperty(PROPERTY));
    artifact.createProperty(parent, PROPERTY);
    assertTrue(artifact2.hasProperty(PROPERTY));
  }

  @Test
  public void testPullPropertyUpdate() {
    Artifact artifact = parent.createArtifact();
    Artifact artifact2 = child1.getArtifact(artifact.getId());
    Property property = artifact.createProperty(parent, PROPERTY);
    assertTrue(artifact2.hasProperty(PROPERTY));
    assertEquals(null, artifact2.getPropertyValue(PROPERTY));
    property.setValue(parent, PROPERTY);
    assertTrue(artifact2.hasProperty(PROPERTY));
    assertEquals(PROPERTY, artifact2.getPropertyValue(PROPERTY));
  }

  @Test
  public void testPullCollectionArtifact() {
    CollectionArtifact collection = parent.createCollection(true);
    Artifact artifact = parent.createArtifact();
    collection.addElement(parent, artifact);
    CollectionArtifact child1Collection = child1.getCollectionArtifact(collection.getId());
    CollectionArtifact child2Collection = child2.getCollectionArtifact(collection.getId());
    Artifact child1Artifact = child1.getArtifact(artifact.getId());
    Artifact child2Artifact = child2.getArtifact(artifact.getId());
    assertTrue(child1Collection.existsElement(child1Artifact));
    assertTrue(child2Collection.existsElement(child2Artifact));
  }
}
