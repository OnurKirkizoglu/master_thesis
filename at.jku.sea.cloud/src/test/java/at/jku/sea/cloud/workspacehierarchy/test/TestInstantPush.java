package at.jku.sea.cloud.workspacehierarchy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;

public class TestInstantPush extends WorkspaceHierarchy {
  @Before
  public void before() {
    super.before();
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.instant, PropagationType.triggered);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.instant, PropagationType.triggered);
  }

  @Test
  public void testPushArtifact() {
    Artifact artifact = child1.createArtifact();
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    assertEquals(artifact.getId(), artifact2.getId());
    assertEquals(parent.getVersionNumber(), artifact2.getVersionNumber());
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testArtifactNotPropagatedToFar() {
    Artifact artifact = child1.createArtifact();
    child2.getArtifact(artifact.getId());
  }

  @Test
  public void testPushPropertyNew() {
    Artifact artifact = child1.createArtifact();
    artifact.createProperty(child1, PROPERTY);
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    assertTrue(artifact2.hasProperty(PROPERTY));
  }

  @Test
  public void testPushPropertyUpdate() {
    Artifact artifact = child1.createArtifact();
    Artifact artifact2 = parent.getArtifact(artifact.getId());
    artifact2.createProperty(parent, PROPERTY);
    assertEquals(null, artifact2.getPropertyValue(PROPERTY));
    Property property = artifact.createProperty(child1, PROPERTY);
    property.setValue(child1, PROPERTY);
    assertEquals(PROPERTY, artifact2.getPropertyValue(PROPERTY));
  }
  
  @Test(expected = PropertyDoesNotExistException.class)
  public void testPropertyNotPropagatedToFar() {
    Artifact artifact = child1.createArtifact();
    child2.pullArtifact(artifact);
    artifact.setPropertyValue(child1, PROPERTY, PROPERTY);
    child2.getArtifact(artifact.getId()).getPropertyValue(PROPERTY);
  }
  
  @Test
  public void testCollectionArtifact() {
    CollectionArtifact collection = child1.createCollection(true);
    Artifact artifact = child1.createArtifact();
    collection.addElement(child1, artifact);
    CollectionArtifact parentCollection = parent.getCollectionArtifact(collection.getId());
    Artifact parentArtifact = child1.getArtifact(artifact.getId());
    assertTrue(parentCollection.existsElement(parentArtifact));
  }

}
