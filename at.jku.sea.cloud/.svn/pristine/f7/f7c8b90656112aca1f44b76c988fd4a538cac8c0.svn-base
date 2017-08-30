package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

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

public class TestArtifactAndPropertyMap {
  private Owner owner;
  private Tool tool;
  private static final Cloud cloud = CloudFactory.getInstance();
  private Workspace workspace;
  private static final String PROPERTY = "PROPERTY";
  private static final String DEAD_PROPERTY = "DEAD_PROPERTY";

  @Before
  public void setUp() throws Exception {
    this.owner = cloud.getOwner(DataStorage.ADMIN);
    if (this.owner == null) {
      this.owner = cloud.createOwner();
    }
    this.tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    if (this.tool == null) {
      this.tool = cloud.createTool();
    }

    this.workspace = cloud.createWorkspace(owner, tool, "");
  }

  @After
  public void tearDown() throws Exception {
    this.workspace.rollbackAll();
  }

  @Test
  public void testAlivePropertyMap() {
    Artifact type = workspace.createArtifact();
    type.setPropertyValue(workspace, PROPERTY, PROPERTY);
    Property deadProperty = type.createProperty(workspace, DEAD_PROPERTY);
    deadProperty.delete(workspace);
    Map<String, Object> alivePropertyMap = type.getAlivePropertiesMap();
    assertEquals(1, alivePropertyMap.size());
    assertTrue(alivePropertyMap.containsKey(PROPERTY));
    assertEquals(PROPERTY, alivePropertyMap.get(PROPERTY));
  }

  @Test
  public void testDeadPropertyMap() {
    Artifact type = workspace.createArtifact();
    type.setPropertyValue(workspace, PROPERTY, PROPERTY);
    Property deadProperty = type.createProperty(workspace, DEAD_PROPERTY);
    deadProperty.delete(workspace);
    Map<String, Object> deadPropertiesMap = type.getDeadPropertiesMap();
    assertEquals(1, deadPropertiesMap.size());
    assertTrue(deadPropertiesMap.containsKey(DEAD_PROPERTY));
    assertEquals(null, deadPropertiesMap.get(DEAD_PROPERTY));
  }

  @Test
  public void testArtifactsAndPropertyMapWithFilters() {
    Artifact type = workspace.createArtifact();
    Artifact artifact = workspace.createArtifact(type);
    artifact.setPropertyValue(workspace, PROPERTY, PROPERTY);
    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap = this.workspace.getArtifactsAndPropertyMap(new Artifact[] { type });
    assertTrue(artifactsAndPropertyMap.containsKey(artifact));
    Map<String, Object> map = artifactsAndPropertyMap.get(artifact);
    assertTrue(map.containsKey(PROPERTY));
    assertEquals(PROPERTY, map.get(PROPERTY));
  }

  @Test
  public void testArtifactsAndPropertyMapWithProperty() {
    Artifact type = workspace.createArtifact();
    Artifact artifact = workspace.createArtifact(type);
    artifact.setPropertyValue(workspace, PROPERTY, PROPERTY);
    Property deadProperty = artifact.createProperty(workspace, DEAD_PROPERTY);
    deadProperty.delete(workspace);
    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap = this.workspace.getArtifactsAndPropertyMap(PROPERTY, PROPERTY, true, new Artifact[] { type });
    assertTrue(artifactsAndPropertyMap.containsKey(artifact));
    Map<String, Object> map = artifactsAndPropertyMap.get(artifact);
    assertTrue(map.containsKey(PROPERTY));
    assertEquals(PROPERTY, map.get(PROPERTY));
  }

  @Test
  public void testArtifactsAndPropertyMapWithPropertyNotFound() {
    Artifact type = workspace.createArtifact();
    Artifact artifact = workspace.createArtifact(type);
    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap = this.workspace.getArtifactsAndPropertyMap(PROPERTY, PROPERTY, true, new Artifact[] { type });
    assertTrue(artifactsAndPropertyMap.isEmpty());
  }
  
  @Test
  public void testArtifactsAndPropertyMapWithMapOfProperties() {
    Artifact type = workspace.createArtifact();
    Artifact artifact = workspace.createArtifact(type);
    artifact.setPropertyValue(workspace, PROPERTY, PROPERTY);
    Property deadProperty = artifact.createProperty(workspace, DEAD_PROPERTY);
    Map<String, Object> properties = new HashMap<String,Object>();
    properties.put(PROPERTY, PROPERTY);
    properties.put(DEAD_PROPERTY, null);
    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap = this.workspace.getArtifactsAndPropertyMap(properties, true, new Artifact[] { type });
    assertTrue(artifactsAndPropertyMap.containsKey(artifact));
    Map<String, Object> map = artifactsAndPropertyMap.get(artifact);
    assertEquals(2, map.size());
    assertTrue(map.containsKey(PROPERTY));
    assertEquals(PROPERTY, map.get(PROPERTY));
    assertTrue(map.containsKey(DEAD_PROPERTY));
    assertEquals(null, map.get(DEAD_PROPERTY));
  }
}
