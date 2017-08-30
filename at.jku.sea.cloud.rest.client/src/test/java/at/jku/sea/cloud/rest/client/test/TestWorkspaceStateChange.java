package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.DATASTORAGE;
import static at.jku.sea.cloud.rest.client.test.TestAll.EXPIRED_WS;
import static at.jku.sea.cloud.rest.client.test.TestAll.EXPIRED_WS_IDENTIFIER;
import static at.jku.sea.cloud.rest.client.test.TestAll.OWNER;
import static at.jku.sea.cloud.rest.client.test.TestAll.TOOL;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS_IDENTIFIER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.PublicVersion;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactConflictException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyConflictException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyNotCommitableException;
import at.jku.sea.cloud.exceptions.VersionConflictException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.rest.client.RestCloud;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class TestWorkspaceStateChange {

  private static final String DEAD_PROPERTY = "deadProperty";

  private static String TEMP_WS_IDENTIFIER = "temp";

  private static long VERSION = 2;

  private static final String MESSAGE = "Test commit message";
  private static final String NEW_PROPERTY_NAME = "new_property";
  private static final String NEW_PROPERTY_VALUE = "ws";
  private static final String NEW_PROPERTY_VALUE2 = "temp";

  @After
  public void tearDown() {
    DATASTORAGE.truncateAll();
    DATASTORAGE.init();

    OWNER = RestCloud.getInstance().getOwner(DataStorage.ADMIN);
    TOOL = RestCloud.getInstance().getTool(DataStorage.ECLIPSE_TOOL_ID);

    WS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, WS_IDENTIFIER);

    EXPIRED_WS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, EXPIRED_WS_IDENTIFIER);
    EXPIRED_WS.close();
  }

  @Test
  public void testRebase() {
    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    tempWS.createArtifact();
    tempWS.commitAll("");
    tempWS.createArtifact();
    tempWS.commitAll("");
    assertEquals(1, WS.getBaseVersion().getVersionNumber());
    assertEquals(3, tempWS.getBaseVersion().getVersionNumber());
    Version version = RestCloud.getInstance().getVersion(VERSION);
    WS.rebase(version);
    assertEquals(VERSION, WS.getBaseVersion().getVersionNumber());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testRebaseWorkspaceExpired() {
    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    tempWS.createArtifact();
    tempWS.commitAll(MESSAGE);
    Version version = RestCloud.getInstance().getVersion(VERSION);
    EXPIRED_WS.rebase(version);
  }

  @Test
  public void testRebaseToHead() {
    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    tempWS.createArtifact();
    tempWS.commitAll(MESSAGE);
    tempWS.createArtifact();
    tempWS.commitAll(MESSAGE);
    assertEquals(1, WS.getBaseVersion().getVersionNumber());
    assertEquals(3, tempWS.getBaseVersion().getVersionNumber());
    WS.rebaseToHeadVersion();
    assertEquals(3, WS.getBaseVersion().getVersionNumber());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testRebaseToHeadWorkspaceExpired() {
    EXPIRED_WS.rebaseToHeadVersion();
  }

  @Test
  public void testCommitArtifact() {
    assertEquals(DataStorage.FIRST_VERSION, WS.getBaseVersion().getVersionNumber());
    int old_size = WS.getArtifacts().size();
    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    Artifact artifact = tempWS.createArtifact();
    tempWS.commitArtifact(artifact, MESSAGE);
    WS.rebaseToHeadVersion();
    assertEquals(VERSION, WS.getBaseVersion().getVersionNumber());
    assertEquals(old_size + 1, WS.getArtifacts().size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCommitArtifactWorkspaceExpired() {
    Artifact artifact = WS.createArtifact();
    EXPIRED_WS.commitArtifact(artifact, MESSAGE);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testCommitArtifactArtifactDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    WS.rollbackAll();
    WS.commitArtifact(artifact, MESSAGE);
  }

  @Test(expected = ArtifactConflictException.class)
  public void testCommitArtifactArtifactConflict() {
    Artifact artifact = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    artifact.setType(WS, WS.getPackage(DataStorage.EPLAN_PCKG));// WS.createProject());

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    Artifact temp = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    temp.setType(tempWS, WS.getPackage(DataStorage.MMM_PACKAGE_ID));// tempWS.createPackage());

    tempWS.commitArtifact(temp, MESSAGE);
    WS.commitArtifact(artifact, MESSAGE);
  }

  @Test
  public void testCommitArtifactWithMessage() {
    assertEquals(1, WS.getBaseVersion().getVersionNumber());
    int old_size = WS.getArtifacts().size();
    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    Artifact artifact = tempWS.createArtifact();
    tempWS.commitArtifact(artifact, MESSAGE);
    WS.rebaseToHeadVersion();
    assertEquals(VERSION, WS.getBaseVersion().getVersionNumber());
    assertEquals(old_size + 1, WS.getArtifacts().size());
    assertTrue(tempWS.getBaseVersion() instanceof PublicVersion);
    PublicVersion publicVersion = (PublicVersion) tempWS.getBaseVersion();
    assertEquals(MESSAGE, publicVersion.getCommitMessage());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCommitArtifactWithMessageWorkspaceExpired() {
    Artifact artifact = WS.createArtifact();
    EXPIRED_WS.commitArtifact(artifact, MESSAGE);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testCommitArtifactWithMessageArtifactDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    WS.rollbackAll();
    WS.commitArtifact(artifact, MESSAGE);
  }

  @Test(expected = ArtifactConflictException.class)
  public void testCommitArtifactWithMessageArtifactConflict() {
    Artifact artifact = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    artifact.setType(WS, WS.getPackage(DataStorage.EPLAN_PCKG));// WS.createProject());

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    Artifact temp = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    temp.setType(tempWS, WS.getPackage(DataStorage.DESIGNSPACE_PACKAGE_ID));// tempWS.createPackage());

    tempWS.commitArtifact(temp, MESSAGE);
    WS.commitArtifact(artifact, MESSAGE);
  }

  @Test
  public void testCommitProperty() {
    assertEquals(DataStorage.FIRST_VERSION, WS.getBaseVersion().getVersionNumber());
    Property property = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(WS, NEW_PROPERTY_VALUE);

    WS.commitProperty(property, MESSAGE);

    assertEquals(VERSION, WS.getBaseVersion().getVersionNumber());
    assertEquals(NEW_PROPERTY_VALUE, WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getPropertyValue(DataStorage.PROPERTY_NAME).toString());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCommitPropertyWorkspaceExpired() {
    Property property = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    EXPIRED_WS.commitProperty(property, MESSAGE);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testCommitPropertyArtifactDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, NEW_PROPERTY_NAME);
    WS.rollbackArtifact(artifact);
    WS.commitProperty(property, MESSAGE);
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testCommitPropertyPropertyDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, NEW_PROPERTY_NAME);
    WS.rollbackProperty(property);
    WS.commitProperty(property, MESSAGE);
  }

  @Test(expected = PropertyConflictException.class)
  public void testCommitPropertyPropertyConflict() {
    Property property = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(WS, "ws");

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    Property temp = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    temp.setValue(tempWS, "temp");

    tempWS.commitProperty(temp, MESSAGE);
    WS.commitProperty(property, MESSAGE);
  }

  @Test(expected = PropertyNotCommitableException.class)
  public void testCommitPropertyPropertyNotCommitable() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, NEW_PROPERTY_NAME);
    WS.commitProperty(property, MESSAGE);
  }

  @Test
  public void testCommitPropertyWithMessage() {
    assertEquals(DataStorage.FIRST_VERSION, WS.getBaseVersion().getVersionNumber());
    Property property = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(WS, NEW_PROPERTY_VALUE);

    WS.commitProperty(property, MESSAGE);

    assertEquals(VERSION, WS.getBaseVersion().getVersionNumber());
    assertEquals(NEW_PROPERTY_VALUE, WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getPropertyValue(DataStorage.PROPERTY_NAME).toString());
    assertTrue(WS.getBaseVersion() instanceof PublicVersion);
    PublicVersion publicVersion = (PublicVersion) WS.getBaseVersion();
    assertEquals(MESSAGE, publicVersion.getCommitMessage());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCommitPropertyWithMessageWorkspaceExpired() {
    Property property = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    EXPIRED_WS.commitProperty(property, MESSAGE);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testCommitPropertyWithMessageArtifactDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, NEW_PROPERTY_NAME);
    WS.rollbackArtifact(artifact);
    WS.commitProperty(property, MESSAGE);
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testCommitPropertyWithMessagePropertyDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, NEW_PROPERTY_NAME);
    WS.rollbackProperty(property);
    WS.commitProperty(property, MESSAGE);
  }

  @Test(expected = PropertyConflictException.class)
  public void testCommitPropertyWithMessagePropertyConflict() {
    Property property = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(WS, "ws");

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    Property temp = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    temp.setValue(tempWS, "temp");

    tempWS.commitProperty(temp, MESSAGE);
    WS.commitProperty(property, MESSAGE);
  }

  @Test(expected = PropertyNotCommitableException.class)
  public void testCommitPropertyWithMessagePropertyNotCommitable() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, NEW_PROPERTY_NAME);

    WS.commitProperty(property, MESSAGE);
  }

  public void testCommitAll() {
    assertEquals(5, WS.getBaseVersion().getVersionNumber());
    int old_size = WS.getArtifacts().size();
    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);

    tempWS.createArtifact();
    Property property = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(tempWS, NEW_PROPERTY_VALUE);
    tempWS.commitAll(MESSAGE);

    WS.rebaseToHeadVersion();
    assertEquals(VERSION, WS.getBaseVersion().getVersionNumber());
    assertEquals(old_size + 1, WS.getArtifacts().size());
    assertEquals(NEW_PROPERTY_VALUE, WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getPropertyValue(DataStorage.PROPERTY_NAME).toString());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCommitAllWorkspaceExpired() {
    EXPIRED_WS.commitAll(MESSAGE);
  }

  @Test(expected = VersionConflictException.class)
  public void testCommitAllVersionConflict() {
    assertEquals(DataStorage.FIRST_VERSION, WS.getBaseVersion().getVersionNumber());

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);

    Property property = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(tempWS, NEW_PROPERTY_VALUE);
    tempWS.commitAll(MESSAGE);
    assertEquals(VERSION, tempWS.getBaseVersion().getVersionNumber());

    property = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(WS, NEW_PROPERTY_VALUE);
    WS.commitAll(MESSAGE);
  }

  @Test
  public void testCommitAllWithMessage() {
    assertEquals(DataStorage.FIRST_VERSION, WS.getBaseVersion().getVersionNumber());
    int old_size = WS.getArtifacts().size();
    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);

    tempWS.createArtifact();
    Property property = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(tempWS, NEW_PROPERTY_VALUE);
    tempWS.commitAll(MESSAGE);

    WS.rebaseToHeadVersion();
    assertEquals(VERSION, WS.getBaseVersion().getVersionNumber());
    // assertEquals(old_size + 1, WS.getArtifacts().size());
    assertEquals(NEW_PROPERTY_VALUE, WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getPropertyValue(DataStorage.PROPERTY_NAME).toString());

    assertTrue(WS.getBaseVersion() instanceof PublicVersion);
    PublicVersion publicVersion = (PublicVersion) WS.getBaseVersion();
    assertEquals(MESSAGE, publicVersion.getCommitMessage());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCommitAllWithMessageWorkspaceExpired() {
    EXPIRED_WS.commitAll(MESSAGE);
  }

  @Test(expected = VersionConflictException.class)
  public void testCommitAllWithMessageVersionConflict() {
    assertEquals(1, WS.getBaseVersion().getVersionNumber());

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);

    Property property = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(tempWS, NEW_PROPERTY_VALUE);
    tempWS.commitAll(MESSAGE);
    assertEquals(VERSION, tempWS.getBaseVersion().getVersionNumber());

    property = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(WS, NEW_PROPERTY_VALUE);
    WS.commitAll(MESSAGE);

  }

  @Test
  public void testGetArtifactConflicts() {
    Artifact artifact = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    artifact.setType(WS, WS.getPackage(DataStorage.PROE_PACKAGE));// WS.createProject());

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    Artifact temp = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    temp.setType(tempWS, WS.getPackage(DataStorage.EPLAN_PCKG));// tempWS.createPackage());

    tempWS.commitArtifact(temp, MESSAGE);

    Set<Artifact> conflicts = WS.getArtifactConflicts();
    assertEquals(1, conflicts.size());
    assertEquals(artifact.getId(), conflicts.toArray(new Artifact[conflicts.size()])[0].getId());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetArtifactConflictsWorkspaceExpiredException() {
    EXPIRED_WS.getArtifactConflicts();
  }

  @Test
  public void testGetArtifactConflictsWithVersion() {
    Artifact artifact = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    artifact.setType(WS, WS.getPackage(DataStorage.EPLAN_PCKG));// WS.createProject());

    Artifact artifact1 = WS.getArtifact(DataStorage.AT_PACKAGE_ID);
    artifact1.setType(WS, WS.getPackage(DataStorage.EPLAN_PCKG));// WS.createProject());

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);

    Artifact temp = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    temp.setType(tempWS, WS.getPackage(DataStorage.PROE_PACKAGE));// tempWS.createPackage());

    Artifact temp1 = tempWS.getArtifact(DataStorage.AT_PACKAGE_ID);
    temp1.setType(tempWS, WS.getPackage(DataStorage.PROE_PACKAGE));// tempWS.createPackage());

    tempWS.commitArtifact(temp, MESSAGE);
    tempWS.commitArtifact(temp1, MESSAGE);

    Set<Artifact> conflicts = WS.getArtifactConflicts(VERSION);
    assertEquals(1, conflicts.size());
    assertEquals(temp.getId(), conflicts.toArray(new Artifact[conflicts.size()])[0].getId());

    conflicts = WS.getArtifactConflicts(VERSION + 1);
    assertEquals(2, conflicts.size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetArtifactConflictsWithVersionWorkspaceExpiredException() {
    EXPIRED_WS.getArtifactConflicts(VERSION);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetArtifactConflictsWithVersionIllegalArgument() {
    WS.getArtifactConflicts(VERSION);
  }

  @Test
  public void testGetPropertyConflicts() {
    Property property = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property.setValue(WS, NEW_PROPERTY_VALUE);

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);
    Property temp = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    temp.setValue(tempWS, NEW_PROPERTY_VALUE2);

    tempWS.commitProperty(temp, MESSAGE);

    Set<Property> conflicts = WS.getPropertyConflicts();
    assertEquals(1, conflicts.size());
    assertEquals(NEW_PROPERTY_VALUE, conflicts.toArray(new Property[conflicts.size()])[0].getValue());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetPropertyConflictsWorkspaceExpiredException() {
    EXPIRED_WS.getPropertyConflicts();
  }

  @Test
  public void testGetPropertyConflictsWithVersion() {
    Property property1 = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property1.setValue(WS, NEW_PROPERTY_VALUE);

    Property property2 = WS.getArtifact(DataStorage.MMM_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    property2.setValue(WS, NEW_PROPERTY_VALUE);

    Workspace tempWS = RestCloud.getInstance().createWorkspace(OWNER, TOOL, TEMP_WS_IDENTIFIER);

    Property temp1 = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    temp1.setValue(tempWS, NEW_PROPERTY_VALUE2);

    Property temp2 = tempWS.getArtifact(DataStorage.MMM_META_MODEL).getProperty(DataStorage.PROPERTY_NAME);
    temp2.setValue(tempWS, NEW_PROPERTY_VALUE2);

    tempWS.commitProperty(temp1, MESSAGE);
    tempWS.commitProperty(temp2, MESSAGE);

    Set<Property> conflicts = WS.getPropertyConflicts(VERSION);
    assertEquals(1, conflicts.size());
    assertEquals(NEW_PROPERTY_VALUE, conflicts.toArray(new Property[conflicts.size()])[0].getValue());

    conflicts = WS.getPropertyConflicts(VERSION + 1);
    assertEquals(2, conflicts.size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetPropertyConflictsWithVersionWorkspaceExpiredException() {
    EXPIRED_WS.getPropertyConflicts(VERSION);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPropertyConflictsWithVersionIllegalArgument() {
    WS.getPropertyConflicts(VERSION);
  }

  @Test
  public void testArtifactsAndPropertyMapWithFilters() {
    Artifact type = WS.createArtifact();
    Artifact artifact = WS.createArtifact(type);
    artifact.setPropertyValue(WS, DataStorage.PROPERTY_NAME, DataStorage.PROPERTY_NAME);
    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap = WS.getArtifactsAndPropertyMap(new Artifact[] { type });
    assertTrue(artifactsAndPropertyMap.containsKey(artifact));
    Map<String, Object> map = artifactsAndPropertyMap.get(artifact);
    assertTrue(map.containsKey(DataStorage.PROPERTY_NAME));
    assertEquals(DataStorage.PROPERTY_NAME, map.get(DataStorage.PROPERTY_NAME));
  }

  @Test
  public void testArtifactsAndPropertyMapWithProperty() {
    Artifact type = WS.createArtifact();
    Artifact artifact = WS.createArtifact(type);
    artifact.setPropertyValue(WS, DataStorage.PROPERTY_NAME, DataStorage.PROPERTY_NAME);
    Property deadProperty = artifact.createProperty(WS, DEAD_PROPERTY);
    deadProperty.delete(WS);
    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap = WS.getArtifactsAndPropertyMap(DataStorage.PROPERTY_NAME, DataStorage.PROPERTY_NAME, true, new Artifact[] { type });
    assertTrue(artifactsAndPropertyMap.containsKey(artifact));
    Map<String, Object> map = artifactsAndPropertyMap.get(artifact);
    assertTrue(map.containsKey(DataStorage.PROPERTY_NAME));
    assertEquals(DataStorage.PROPERTY_NAME, map.get(DataStorage.PROPERTY_NAME));
  }

  @Test
  public void testArtifactsAndPropertyMapWithPropertyNotFound() {
    Artifact type = WS.createArtifact();
    Artifact artifact = WS.createArtifact(type);
    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap = WS.getArtifactsAndPropertyMap(DataStorage.PROPERTY_NAME, DataStorage.PROPERTY_NAME, true, new Artifact[] { type });
    assertTrue(artifactsAndPropertyMap.isEmpty());
  }

  @Test
  public void testArtifactsAndPropertyMapWithMapOfProperties() {
    Artifact type = WS.createArtifact();
    Artifact artifact = WS.createArtifact(type);
    artifact.setPropertyValue(WS, DataStorage.PROPERTY_NAME, DataStorage.PROPERTY_NAME);
    Property deadProperty = artifact.createProperty(WS, DEAD_PROPERTY);
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put(DataStorage.PROPERTY_NAME, DataStorage.PROPERTY_NAME);
    properties.put(DEAD_PROPERTY, null);
    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap = WS.getArtifactsAndPropertyMap(properties, true, new Artifact[] { type });
    assertTrue(artifactsAndPropertyMap.containsKey(artifact));
    Map<String, Object> map = artifactsAndPropertyMap.get(artifact);
    assertEquals(2, map.size());
    assertTrue(map.containsKey(DataStorage.PROPERTY_NAME));
    assertEquals(DataStorage.PROPERTY_NAME, map.get(DataStorage.PROPERTY_NAME));
    assertTrue(map.containsKey(DEAD_PROPERTY));
    assertEquals(null, map.get(DEAD_PROPERTY));
  }
}
