package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.DATASTORAGE;
import static at.jku.sea.cloud.rest.client.test.TestAll.OWNER;
import static at.jku.sea.cloud.rest.client.test.TestAll.TOOL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.PropertyNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.WorkspaceConflictException;
import at.jku.sea.cloud.rest.client.RestCloud;

public class TestWorkspacePushPull {

  private static final String PROPERTY = "property";
  protected Workspace parent;
  protected Workspace child1;
  protected Workspace child2;

  protected static Cloud cloud = RestCloud.getInstance();

  @Before
  public void before() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
    child1 = cloud.createWorkspace(owner, tool, "child1", parent, PropagationType.triggered, PropagationType.triggered);
    child2 = cloud.createWorkspace(owner, tool, "child2", parent, PropagationType.triggered, PropagationType.triggered);
  }

  @After
  public void tearDown() {
    DATASTORAGE.truncateAll();
    DATASTORAGE.init();
  }

  @Test
  public void testPushArtifact() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
  }

  @Test
  public void testInitialization() {
    RestCloud cloud = RestCloud.getInstance();
    Workspace parent = cloud.createWorkspace(OWNER, TOOL, "parent", null, PropagationType.triggered, PropagationType.triggered);
    Workspace child1 = cloud.createWorkspace(OWNER, TOOL, "child1", parent, PropagationType.triggered, PropagationType.instant);
    Workspace child2 = cloud.createWorkspace(OWNER, TOOL, "child2", parent, PropagationType.instant, PropagationType.triggered);
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

  @Test
  public void testGetWorkspaceChildren() {
    RestCloud cloud = RestCloud.getInstance();
    Workspace parent = cloud.createWorkspace(OWNER, TOOL, "parent", null, PropagationType.triggered, PropagationType.triggered);
    Workspace child1 = cloud.createWorkspace(OWNER, TOOL, "child1", parent, PropagationType.triggered, PropagationType.instant);
    Workspace child2 = cloud.createWorkspace(OWNER, TOOL, "child2", parent, PropagationType.instant, PropagationType.triggered);
    Collection<Workspace> children = parent.getChildren();
    assertEquals(2, children.size());
    assertTrue(children.contains(child1));
    assertTrue(children.contains(child2));
  }

  @Test
  public void testGetWorkspaceChildrenEmpty() {
    RestCloud cloud = RestCloud.getInstance();
    Workspace parent = cloud.createWorkspace(OWNER, TOOL, "parent", null, PropagationType.triggered, PropagationType.triggered);
    Workspace child1 = cloud.createWorkspace(OWNER, TOOL, "child1", parent, PropagationType.triggered, PropagationType.instant);
    Workspace child2 = cloud.createWorkspace(OWNER, TOOL, "child2", parent, PropagationType.instant, PropagationType.triggered);
    Collection<Workspace> children = child1.getChildren();
    assertEquals(0, children.size());
  }

  @Test
  public void testPushPropertyNew() {
    Artifact artifact = child1.createArtifact();
    child1.pushArtifact(artifact);
    Property property = artifact.createProperty(child1, PROPERTY);
    child1.pushProperty(property);
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
  }

  @Test
  public void testPullArtifact() {
    Artifact artifact = parent.createArtifact();
    child1.pullArtifact(artifact);
  }

  @Test(expected = PropertyNotPushOrPullableException.class)
  public void testPullPropertyFailure() {
    Artifact artifact = parent.createArtifact();
    Property property = artifact.createProperty(parent, PROPERTY);
    child1.pullProperty(property);
  }

  @Test
  public void testPullAll() {
    Artifact artifact1 = parent.createArtifact();
    artifact1.setPropertyValue(parent, PROPERTY, PROPERTY);
    Artifact artifact2 = parent.createArtifact();
    artifact2.setPropertyValue(parent, PROPERTY, PROPERTY);
    child1.pullAll();
  }

  @Test
  public void testSetParent() {
    child1.setParent(child2);
  }
  
  @Test
  public void testSetPush() {
    RestCloud cloud = RestCloud.getInstance();
    Workspace parent = cloud.createWorkspace(OWNER, TOOL, "parent1", null, PropagationType.triggered, PropagationType.triggered);
    Workspace child1 = cloud.createWorkspace(OWNER, TOOL, "child11", parent, PropagationType.triggered, PropagationType.instant);
    child1.setPush(PropagationType.instant);
    assertEquals(PropagationType.instant, child1.getPush());
  }

  @Test
  public void testSetPull() {
    RestCloud cloud = RestCloud.getInstance();
    Workspace parent = cloud.createWorkspace(OWNER, TOOL, "parent2", null, PropagationType.triggered, PropagationType.triggered);
    Workspace child1 = cloud.createWorkspace(OWNER, TOOL, "child12", parent, PropagationType.triggered, PropagationType.instant);
    child1.setPull(PropagationType.instant);
    assertEquals(PropagationType.instant, child1.getPull());
  }

  @Test(expected = WorkspaceConflictException.class)
  public void testSetPushWithConflict() {
    RestCloud cloud = RestCloud.getInstance();
    Workspace parent = cloud.createWorkspace(OWNER, TOOL, "parent3", null, PropagationType.triggered, PropagationType.triggered);
    Workspace child1 = cloud.createWorkspace(OWNER, TOOL, "child13", parent, PropagationType.triggered, PropagationType.instant);
    Artifact artifact = parent.getArtifact(DataStorage.TRACE_LINK);
    artifact.setPropertyValue(parent, PROPERTY, 1);
    artifact.setPropertyValue(child1, PROPERTY, 2);
    child1.setPush(PropagationType.instant);
  }

}
