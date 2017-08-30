package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.PropagationType;

public class TestInstantPushPull extends WorkspaceHierarchy {
  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.instant, PropagationType.instant);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.instant, PropagationType.instant);
  }

  @Test
  public void testArtifactPropagation() {
    Artifact artifact = child1.createArtifact();
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    assertEquals(artifact.getId(), artifact2.getId());
    assertEquals(parent.getVersionNumber(), artifact2.getVersionNumber());
    Artifact artifact3 = child2.getArtifact(artifact.getId());
    assertEquals(artifact.getId(), artifact3.getId());
    assertEquals(child2.getVersionNumber(), artifact3.getVersionNumber());
  }

  @Test
  public void testPushPropertyUpdate() {
    Artifact artifact = child1.createArtifact();
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    Artifact artifact3 = child2.getArtifact(artifact.getId());
    artifact.setPropertyValue(child1, PROPERTY, PROPERTY);
    artifact2.hasProperty(PROPERTY);
    artifact3.hasProperty(PROPERTY);
    assertEquals(PROPERTY, artifact2.getPropertyValue(PROPERTY));
    assertEquals(PROPERTY, artifact3.getPropertyValue(PROPERTY));
  }
  
  @Test
  public void testPushProperty() {
    Artifact artifact = child1.getArtifact(DataStorage.TRACE_LINK);
    artifact.setPropertyValue(child1, PROPERTY, PROPERTY);
    assertEquals(PROPERTY, parent.getArtifact(DataStorage.TRACE_LINK).getPropertyValue(PROPERTY));
    assertEquals(PROPERTY, parent.getArtifact(DataStorage.TRACE_LINK).getPropertyValue(PROPERTY));
  }
  
  
  @Test
  public void testCollectionArtifact() {
    CollectionArtifact collection = child1.createCollection(true);
    Artifact artifact = child1.createArtifact();
    collection.addElement(child1, artifact);
    CollectionArtifact parentCollection = parent.getCollectionArtifact(collection.getId());
    CollectionArtifact child2Collection = child2.getCollectionArtifact(collection.getId());
    Artifact parentArtifact = parent.getArtifact(artifact.getId());
    Artifact child2Artifact = child2.getArtifact(artifact.getId());
    assertTrue(parentCollection.existsElement(parentArtifact));
    assertTrue(child2Collection.existsElement(child2Artifact));
  }
  
}
