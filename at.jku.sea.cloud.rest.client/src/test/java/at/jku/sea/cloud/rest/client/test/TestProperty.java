package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.ADMIN_PASSWORD;
import static at.jku.sea.cloud.rest.client.test.TestAll.PROPERTY_DOESNOTEXIST;
import static at.jku.sea.cloud.rest.client.test.TestAll.PROPERTY_VALUE;
import static at.jku.sea.cloud.rest.client.test.TestAll.PROPERTY_VALUE_SETTER;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static at.jku.sea.cloud.rest.client.test.TestAll.WSID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.mmm.MMMTypeProperties;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class TestProperty {

  private static Property PROPERTY;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    PROPERTY = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
  }

  @After
  public void tearDown() throws Exception {
    WS.rollbackAll();
  }

  @Test
  public void testGetId() {
    assertEquals(DataStorage.DESIGNSPACE_META_MODEL, PROPERTY.getId());
  }

  @Test
  public void testGetVersion() {
    assertEquals(WSID, PROPERTY.getVersionNumber());
  }

  @Test
  public void testGetOwner() {
    Owner owner = PROPERTY.getOwner();
    assertEquals(DataStorage.ADMIN, owner.getId());
  }

  @Test
  public void testGetTool() {
    Tool tool = PROPERTY.getTool();
    assertEquals(DataStorage.ROOT_TOOL_ID, tool.getId());
  }

  @Test
  public void testIsAlive() {
    assertTrue(PROPERTY.isAlive());
    PROPERTY.delete(WS);
    assertFalse(PROPERTY.isAlive());
  }

  @Test
  public void testIsReference() {
    assertFalse(PROPERTY.isReference());
    assertTrue(WS.getArtifact(DataStorage.TRACE_LINK).getProperty(MMMTypeProperties.ALLSUPER_TYPES).isReference());
  }

  @Test
  public void testGetName() {
    assertEquals(DataStorage.PROPERTY_NAME, PROPERTY.getName());
  }

  @Test
  public void testGetValue() {
    assertEquals(PROPERTY_VALUE, PROPERTY.getValue());
  }
  
  @Test
  public void testGetRepresentation() {
    Object[] representation = PROPERTY.getRepresentation();
    assertFalse(representation == null);
    assertEquals(PROPERTY.getId(), representation[DataStorage.Columns.ARTIFACT_ID.getIndex()]);
    assertEquals(PROPERTY_VALUE, representation[DataStorage.Columns.PROPERTY_VALUE.getIndex()]);
  }
  
  @Test(expected = PropertyDoesNotExistException.class)
  public void testGetRepresentationException() {
    WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getPropertyRepresentation("asdf");
  }

  @Test
  public void testSetValue() {
    PROPERTY.setValue(WS, PROPERTY_VALUE_SETTER);
    assertEquals(PROPERTY_VALUE_SETTER, PROPERTY.getValue().toString());
  }

  @Test
  public void testSetValueWithArtifact() {
    Artifact artifact = WS.createArtifact();
    PROPERTY.setValue(WS, artifact);
    Object o = PROPERTY.getValue();
    assertTrue(o instanceof Artifact);
    Artifact value = (Artifact) o;
    assertEquals(artifact.getId(), value.getId());
    assertEquals(artifact.getVersionNumber(), value.getVersionNumber());
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testSetValueArtifactDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, PROPERTY_DOESNOTEXIST);
    WS.rollbackAll();
    property.setValue(WS, PROPERTY_VALUE_SETTER);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testSetValueArtifactDead() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, PROPERTY_DOESNOTEXIST);
    artifact.delete(WS);
    property.setValue(WS, PROPERTY_VALUE_SETTER);
  }

  @Test(expected = PropertyDeadException.class)
  public void testSetValuePropertyDead() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, PROPERTY_DOESNOTEXIST);
    property.delete(WS);
    property.setValue(WS, PROPERTY_VALUE_SETTER);
  }

  @Test
  public void testDelete() {
    assertTrue(PROPERTY.isAlive());
    PROPERTY.delete(WS);
    assertFalse(PROPERTY.isAlive());
  }

  @Test
  public void testUndelete() {
    PROPERTY.delete(WS);
    assertFalse(PROPERTY.isAlive());
    PROPERTY.undelete(WS);
    assertTrue(PROPERTY.isAlive());
  }

  @Test
  public void testGetArtifact() {
    Artifact artifact = PROPERTY.getArtifact();
    assertEquals(DataStorage.DESIGNSPACE_META_MODEL, artifact.getId());
    assertEquals(WSID, artifact.getVersionNumber());
  }
  
}
