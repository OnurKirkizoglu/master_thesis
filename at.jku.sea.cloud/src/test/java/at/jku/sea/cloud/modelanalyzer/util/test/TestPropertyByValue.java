package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.CloudFactory;

public class TestPropertyByValue {

  private static Cloud cloud;
  private Tool tool;
  private Owner owner;

  @Before
  public void before() {
    cloud = CloudFactory.getInstance();
    this.owner = cloud.getOwner(DataStorage.ADMIN);
    this.tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
  }

  @After
  public void after() {
  }

  @Test
  public void testPropByValue() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact();
    artifact.setPropertyValue(ws1, "name", artifact);
    final Collection<Property> prop = cloud.getPropertiesByReference(ws1.getVersionNumber(), artifact);
    assertEquals(1, prop.size());
    final Property p = prop.iterator().next();
    assertEquals(artifact.getId(), p.getId());
    assertEquals(artifact.getVersionNumber(), p.getVersionNumber());
    assertEquals("name", p.getName());
    assertEquals(artifact, p.getValue());
    ws1.rollbackAll();
    ws1.close();
  }

  @Test
  public void testGetArtifactsWithProperty() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact(ws1.getArtifact(DataStorage.CONSTRAINT));
    artifact.setPropertyValue(ws1, "name", "someValue");
    final Collection<Artifact> artifacts = ws1.getArtifactsWithProperty("name", "someValue", ws1.getArtifact(DataStorage.CONSTRAINT));
    assertEquals(1, artifacts.size());
    assertEquals(artifact, artifacts.iterator().next());
    ws1.rollbackAll();
    ws1.close();
  }

  @Test
  public void testAlphaNumericOneProperty() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact(ws1.getArtifact(DataStorage.CONSTRAINT));
    artifact.setPropertyValue(ws1, "name.name", "someValue");
    Collection<Property> properties = artifact.getAllProperties();
    assertEquals(1, properties.size());
    Property name = properties.iterator().next();
    assertEquals("name.name", name.getName());
    assertEquals("someValue", artifact.getPropertyValue("name.name"));
    ws1.rollbackAll();
    ws1.close();
  }
  
  @Test
  public void testAlphaNumericTwoProperty() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact(ws1.getArtifact(DataStorage.CONSTRAINT));
    artifact.setPropertyValue(ws1, "name#name", "someValue");
    Collection<Property> properties = artifact.getAllProperties();
    assertEquals(1, properties.size());
    Property name = properties.iterator().next();
    assertEquals("name#name", name.getName());
    assertEquals("someValue", artifact.getPropertyValue("name#name"));
    ws1.rollbackAll();
    ws1.close();
  }
}
