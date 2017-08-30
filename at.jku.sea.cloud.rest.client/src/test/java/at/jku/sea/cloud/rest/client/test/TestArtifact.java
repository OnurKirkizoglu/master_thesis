package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.ADMIN_PASSWORD;
import static at.jku.sea.cloud.rest.client.test.TestAll.PROPERTY_DOESNOTEXIST;
import static at.jku.sea.cloud.rest.client.test.TestAll.PROPERTY_VALUE;
import static at.jku.sea.cloud.rest.client.test.TestAll.PROPERTY_VALUE_SETTER;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static at.jku.sea.cloud.rest.client.test.TestAll.WSID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class TestArtifact {

  private static Artifact ARTIFACT;
  private static Project PROJECT;

  private static String ARTIFACT_NAME = "artifact";
  private static String OBJECT_NAME = "object";

  private static Map<String, Long> MAP_OBJECT = new HashMap<>();
  private static Object OBJECT = "test";
  private static String ID_NAME = "id";
  private static long ID_OBJECT = 10L;

  private static int PROPERTIES_SIZE = 3;

  @BeforeClass
  public static void setUpBeforeClass() {
    ARTIFACT = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    PROJECT = WS.getProject(DataStorage.TEST_PROJECT_ID);
    MAP_OBJECT.put(ID_NAME, ID_OBJECT);
  }

  @After
  public void tearDown() {
    WS.rollbackAll();
  }

  @Test
  public void testGetId() {
    assertEquals(DataStorage.DESIGNSPACE_META_MODEL, ARTIFACT.getId());
  }

  @Test
  public void testGetVersion() {
    assertEquals(WSID, ARTIFACT.getVersionNumber());
  }

  @Test
  public void testGetRepresentation() {
    Object[] representation = ARTIFACT.getRepresentation();
    assertFalse(representation == null);
    assertEquals(ARTIFACT.getId(), representation[DataStorage.Columns.ARTIFACT_ID.getIndex()]);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testGetRepresentationException() {
    WS.getArtifactRepresentation(Long.MAX_VALUE);
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testGetPropertyRepresentationException() {
    ARTIFACT.getPropertyRepresentation("asdf");
  }

  @Test
  public void testGetOwner() {
    Owner owner = ARTIFACT.getOwner();
    assertEquals(DataStorage.ADMIN, owner.getId());
  }

  @Test
  public void testGetTool() {
    Tool tool = ARTIFACT.getTool();
    assertEquals(DataStorage.ROOT_TOOL_ID, tool.getId());
  }

  @Test
  public void testGetType() {
    Artifact type = ARTIFACT.getType();
    assertEquals(DataStorage.META_MODEL_TYPE_ID, type.getId());
    assertEquals(WS.getVersionNumber(), type.getVersionNumber());
  }

  @Test
  public void testGetPackage() {
    Package pkg = ARTIFACT.getPackage();
    assertEquals(DataStorage.DESIGNSPACE_PACKAGE_ID, pkg.getId());
    assertEquals(WSID, pkg.getVersionNumber());
  }

  @Test
  public void testGetPackageIsNull() {
    Artifact temp = WS.getArtifact(DataStorage.AT_PACKAGE_ID);
    assertNull(temp.getPackage());
  }

  @Test
  public void testSetType() {
    Artifact type = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    ARTIFACT.setType(WS, type);
    assertEquals(type.getId(), ARTIFACT.getType().getId());
    assertEquals(type.getVersionNumber(), ARTIFACT.getType().getVersionNumber());
  }

  @Test(expected = ArtifactDeadException.class)
  public void testSetTypeArtifactDead() {
    Artifact type = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    ARTIFACT.delete(WS);
    ARTIFACT.setType(WS, type);
  }

  @Test
  public void testIsAlive() {
    assertTrue(ARTIFACT.isAlive());
    ARTIFACT.delete(WS);
    assertFalse(ARTIFACT.isAlive());
  }

  @Test
  public void testHasProperty() {
    assertTrue(ARTIFACT.hasProperty(DataStorage.PROPERTY_NAME));
    assertFalse(ARTIFACT.hasProperty(PROPERTY_DOESNOTEXIST));
  }

  @Test
  public void testIsPropertyAlive() {
    assertTrue(ARTIFACT.isPropertyAlive(DataStorage.PROPERTY_NAME));
    ARTIFACT.deleteProperty(WS, DataStorage.PROPERTY_NAME);
    assertFalse(ARTIFACT.isPropertyAlive(DataStorage.PROPERTY_NAME));
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testIsPropertyAliveDoesNotExist() {
    ARTIFACT.isPropertyAlive(PROPERTY_DOESNOTEXIST);
  }

  @Test
  public void testGetPropertyValue() {
    assertEquals(PROPERTY_VALUE, ARTIFACT.getPropertyValue(DataStorage.PROPERTY_NAME).toString());
  }

  @Test
  public void testGetPropertyValueOrNull() {
    assertEquals(PROPERTY_VALUE, ARTIFACT.getPropertyValueOrNull(DataStorage.PROPERTY_NAME).toString());
  }

  @Test
  public void testGetPropertyValueOrNull_Null() {
    assertEquals(null, ARTIFACT.getPropertyValueOrNull("asdf"));
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testGetPropertyValueDoesNotExist() {
    ARTIFACT.getPropertyValue(PROPERTY_DOESNOTEXIST);
  }

  @Test
  public void testGetAlivePropertyNames() {
    Collection<String> propertyNames = ARTIFACT.getAlivePropertyNames();
    assertEquals(PROPERTIES_SIZE, propertyNames.size());
    assertTrue(propertyNames.contains(DataStorage.PROPERTY_COLLECTION_REFERENCES));
    assertTrue(propertyNames.contains(DataStorage.PROPERTY_NAME));
    ARTIFACT.deleteProperty(WS, DataStorage.PROPERTY_NAME);
    assertEquals(propertyNames.size() - 1, ARTIFACT.getAlivePropertyNames().size());
  }

  @Test
  public void testGetAllPropertyNames() {
    Collection<String> propertyNames = ARTIFACT.getAllPropertyNames();
    assertEquals(PROPERTIES_SIZE, propertyNames.size());
    assertTrue(propertyNames.contains(DataStorage.PROPERTY_COLLECTION_REFERENCES));
    assertTrue(propertyNames.contains(DataStorage.PROPERTY_NAME));
    ARTIFACT.deleteProperty(WS, DataStorage.PROPERTY_NAME);
    assertEquals(propertyNames.size(), ARTIFACT.getAllPropertyNames().size());
  }

  @Test
  public void testDelete() {
    assertTrue(ARTIFACT.isAlive());
    ARTIFACT.delete(WS);
    assertFalse(ARTIFACT.isAlive());
  }

  @Test
  public void testUndelete() {
    ARTIFACT.delete(WS);
    assertFalse(ARTIFACT.isAlive());
    ARTIFACT.undelete(WS);
    assertTrue(ARTIFACT.isAlive());
  }

  @Test
  public void testSetPropertyValue() {
    ARTIFACT.getProperty(DataStorage.PROPERTY_NAME);
    ARTIFACT.setPropertyValue(WS, DataStorage.PROPERTY_NAME, PROPERTY_VALUE_SETTER);
    assertEquals(PROPERTY_VALUE_SETTER, ARTIFACT.getPropertyValue(DataStorage.PROPERTY_NAME));
  }

  @Test
  public void testSetPropertyValues() {
    Artifact artifact = WS.getArtifact(DataStorage.PACKAGE_TYPE_ID);
    Map<String, Object> properties = new HashMap<>();
    properties.put(ARTIFACT_NAME, artifact);
    properties.put(OBJECT_NAME, OBJECT);
    ARTIFACT.setPropertyValues(WS, properties);
    assertTrue(ARTIFACT.hasProperty(ARTIFACT_NAME));
    assertTrue(ARTIFACT.hasProperty(OBJECT_NAME));
    assertEquals((Artifact) ARTIFACT.getPropertyValue(ARTIFACT_NAME), artifact);
    assertEquals(ARTIFACT.getPropertyValue(OBJECT_NAME), OBJECT);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testSetPropertyValuesArtifactDoesNotExists() {
    Artifact artifact = WS.createArtifact();
    artifact.createProperty(WS, PROPERTY_DOESNOTEXIST);
    Map<String, Object> properties = new HashMap<>();
    properties.put(OBJECT_NAME, OBJECT);
    WS.rollbackAll();
    artifact.setPropertyValues(WS, properties);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testSetPropertyValuesArtifactDead() {
    ARTIFACT.delete(WS);
    Map<String, Object> properties = new HashMap<>();
    properties.put(OBJECT_NAME, OBJECT);
    ARTIFACT.setPropertyValues(WS, properties);
  }

  @Test(expected = PropertyDeadException.class)
  public void testSetPropertyValuesPropertyDead() {
    ARTIFACT.createProperty(WS, OBJECT_NAME);
    ARTIFACT.deleteProperty(WS, OBJECT_NAME);
    Map<String, Object> properties = new HashMap<>();
    properties.put(OBJECT_NAME, OBJECT);
    ARTIFACT.setPropertyValues(WS, properties);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testSetPropertyValueArtifactDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    artifact.createProperty(WS, PROPERTY_DOESNOTEXIST);
    WS.rollbackAll();
    artifact.setPropertyValue(WS, PROPERTY_DOESNOTEXIST, PROPERTY_VALUE);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testSetPropertyValueArtifactDead() {
    ARTIFACT.delete(WS);
    ARTIFACT.setPropertyValue(WS, DataStorage.PROPERTY_NAME, PROPERTY_VALUE_SETTER);
  }

  @Test(expected = PropertyDeadException.class)
  public void testSetPropertyValuePropertyDead() {
    ARTIFACT.deleteProperty(WS, DataStorage.PROPERTY_NAME);
    ARTIFACT.setPropertyValue(WS, DataStorage.PROPERTY_NAME, PROPERTY_VALUE_SETTER);
  }

  @Test
  public void testDeleteProperty() {
    assertTrue(ARTIFACT.isPropertyAlive(DataStorage.PROPERTY_NAME));
    ARTIFACT.deleteProperty(WS, DataStorage.PROPERTY_NAME);
    assertFalse(ARTIFACT.isPropertyAlive(DataStorage.PROPERTY_NAME));
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testDeletePropertyDoesNotExist() {
    ARTIFACT.deleteProperty(WS, PROPERTY_DOESNOTEXIST);
  }

  @Test
  public void testUndeleteProperty() {
    ARTIFACT.deleteProperty(WS, DataStorage.PROPERTY_NAME);
    assertFalse(ARTIFACT.isPropertyAlive(DataStorage.PROPERTY_NAME));
    ARTIFACT.undeleteProperty(WS, DataStorage.PROPERTY_NAME);
    assertTrue(ARTIFACT.isPropertyAlive(DataStorage.PROPERTY_NAME));
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testUndeletePropertyDoesNotExist() {
    ARTIFACT.undeleteProperty(WS, PROPERTY_DOESNOTEXIST);
  }

  @Test
  public void testSetPackage() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    ARTIFACT.setPackage(WS, pkg);
    assertEquals(pkg.getId(), ARTIFACT.getPackage().getId());
    assertEquals(pkg.getVersionNumber(), ARTIFACT.getVersionNumber());
  }

  @Test(expected = ArtifactDeadException.class)
  public void testSetPackageArtifactDead() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    ARTIFACT.delete(WS);
    ARTIFACT.setPackage(WS, pkg);
  }

  @Test
  public void testCreateProperty() {
    ARTIFACT.createProperty(WS, PROPERTY_DOESNOTEXIST);
    assertTrue(ARTIFACT.hasProperty(PROPERTY_DOESNOTEXIST));
  }

  @Test(expected = ArtifactDeadException.class)
  public void testCreatePropertyArtifactDead() {
    ARTIFACT.delete(WS);
    ARTIFACT.createProperty(WS, PROPERTY_DOESNOTEXIST);
  }

  @Test
  public void testGetProperty() {
    Property property = ARTIFACT.getProperty(DataStorage.PROPERTY_NAME);
    assertEquals(DataStorage.PROPERTY_NAME, property.getName());
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testGetPropertyDoesNotExist() {
    ARTIFACT.getProperty(PROPERTY_DOESNOTEXIST);
  }

  @Test
  public void testGetAliveProperties() {
    Collection<Property> properties = ARTIFACT.getAliveProperties();
    assertEquals(PROPERTIES_SIZE, properties.size());
    ARTIFACT.deleteProperty(WS, DataStorage.PROPERTY_NAME);
    assertEquals(properties.size() - 1, ARTIFACT.getAliveProperties().size());
  }

  @Test
  public void testGetAllProperties() {
    Collection<Property> properties = ARTIFACT.getAllProperties();
    assertEquals(PROPERTIES_SIZE, properties.size());
    ARTIFACT.deleteProperty(WS, DataStorage.PROPERTY_NAME);
    assertEquals(properties.size(), ARTIFACT.getAllProperties().size());
  }

  @Test
  public void testAddToProject() {
    int old_size = PROJECT.getArtifacts().size();
    ARTIFACT.addToProject(WS, PROJECT);
    int new_size = PROJECT.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testAddToProjectArtifactDead() {
    PROJECT.delete(WS);
    ARTIFACT.addToProject(WS, PROJECT);
  }

  @Test
  public void testRemoveFromProject() {
    ARTIFACT.addToProject(WS, PROJECT);
    int old_size = PROJECT.getArtifacts().size();
    ARTIFACT.removeFromProject(WS, PROJECT);
    int new_size = PROJECT.getArtifacts().size();
    assertEquals(old_size - 1, new_size);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testRemoveFromProjectArtifactDead() {
    ARTIFACT.addToProject(WS, PROJECT);
    PROJECT.delete(WS);
    ARTIFACT.removeFromProject(WS, PROJECT);
  }

  @Test
  public void testArtifactMappings() {
    Artifact art = WS.createArtifact();
    art.createProperty(WS, DataStorage.PROPERTY_NAME);
    Map<String, Object> map = art.getAlivePropertiesMap();
    assertEquals(1, map.size());
    assertTrue(map.containsKey(DataStorage.PROPERTY_NAME));
    assertEquals(null, map.get(DataStorage.PROPERTY_NAME));
  }

  @Test
  public void testDeadArtifactMappings() {
    Artifact art = WS.createArtifact();
    Property property = art.createProperty(WS, DataStorage.PROPERTY_NAME);
    property.delete(WS);
    Map<String, Object> map = art.getDeadPropertiesMap();
    assertEquals(1, map.size());
    assertTrue(map.containsKey(DataStorage.PROPERTY_NAME));
    assertEquals(null, map.get(DataStorage.PROPERTY_NAME));
  }
}
