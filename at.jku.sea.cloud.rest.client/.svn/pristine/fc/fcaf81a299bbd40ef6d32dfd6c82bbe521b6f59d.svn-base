package at.jku.sea.cloud.rest.client.test;

/*
 * Before starting the tests:
 *   - delete data folder
 *   - execute start_hsqldb.bat
 *   - run at.jku.sea.cloud.rest.server.Server
 *   - run at.jku.sea.cloud.rest.test.BeforeTesting
 */

import static at.jku.sea.cloud.rest.client.test.TestAll.ADMIN_PASSWORD;
import static at.jku.sea.cloud.rest.client.test.TestAll.EXPIRED_WS;
import static at.jku.sea.cloud.rest.client.test.TestAll.OWNER;
import static at.jku.sea.cloud.rest.client.test.TestAll.PROPERTY_DOESNOTEXIST;
import static at.jku.sea.cloud.rest.client.test.TestAll.PROPERTY_VALUE_SETTER;
import static at.jku.sea.cloud.rest.client.test.TestAll.TOOL;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static at.jku.sea.cloud.rest.client.test.TestAll.WSID;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS_IDENTIFIER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MetaModelDoesNotExistException;
import at.jku.sea.cloud.exceptions.PackageDoesNotExistException;
import at.jku.sea.cloud.exceptions.ProjectDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.exceptions.WorkspaceNotEmptyException;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import at.jku.sea.cloud.rest.client.RestCloud;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class TestWorkspace {

  private static String CLOSING_WS_IDENTIFIER = "closing";

  private static long COLLECTIONARTIFACT_ID;

  private static int ARTIFACTS_SIZE = 218;
  private static int PACKAGES_SIZE = 42;
  private static int PROJECTS_SIZE = 1;
  private static int METAMODELS_SIZE = 5;
  private static int COLLECTIONARTIFACTS_SIZE = 77;

  private static long ARTIFACT_DOESNOTEXIST = 12345;
  private static long PACKAGE_DOESNOTEXIST = 12345;
  private static long PROJECT_DOESNOTEXIST = 12345;
  private static long METAMODEL_DOESNOTEXIST = 12345;
  private static long COLLECTIONARTIFACT_DOESNOTEXIST = 12345;

  private static boolean CONTAINSONLYARTIFACTS = true;

  private static Artifact[] ARTIFACT_FILTERS;
  private static int ARTIFACTS_FILTERED_SIZE = 8;

  private static Map<String, Object> PROPERTY_FILTERS;
  private static int ARTIFACTS_FILTERED2_SIZE = 1;

  private static String ARTIFACT_PROPERTY_NAME = "artifact";
  private static String OBJECT_PROPERTY_NAME = "object";

  private static String OBJECT_PROPERTY = "test";

  @BeforeClass
  public static void setUpBeforeClass() {
    ARTIFACT_FILTERS = new Artifact[1];
    ARTIFACT_FILTERS[0] = WS.getMetaModel(DataStorage.DESIGNSPACE_META_MODEL);

    PROPERTY_FILTERS = new HashMap<String, Object>();
    PROPERTY_FILTERS.put(DataStorage.PROPERTY_NAME, "Root-Type");
  }

  @Before
  public void startUp() {
    COLLECTIONARTIFACT_ID = WS.createCollection(true).getId();
  }

  @After
  public void tearDown() {
    WS.rollbackAll();
  }

  @Test
  public void testGetId() {
    assertEquals(WSID, WS.getId());
  }

  @Test
  public void testGetVersion() {
    assertEquals(WSID, WS.getVersionNumber());
  }

  @Test
  public void testGetIdentifier() {
    assertEquals(WS_IDENTIFIER, WS.getIdentifier());
  }

  @Test
  public void testGetOwner() {
    Owner owner = WS.getOwner();
    assertEquals(DataStorage.ADMIN, owner.getId());
  }

  @Test
  public void testGetTool() {
    Tool tool = WS.getTool();
    assertEquals(DataStorage.ECLIPSE_TOOL_ID, tool.getId());
  }

  @Test
  public void testGetBaseVersion() {
    Version version = WS.getBaseVersion();
    assertEquals(DataStorage.FIRST_VERSION, version.getVersionNumber());
    assertEquals(null, version.getIdentifier());
  }

  @Test
  public void testCreatePackage() {
    int old_size = WS.getPackages().size();
    WS.createPackage();
    int new_size = WS.getPackages().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreatePackageWorkspaceExpired() {
    EXPIRED_WS.createPackage();
  }

  @Test
  public void testCreatePackageInPackage() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    int old_size = pkg.getArtifacts().size();
    WS.createPackage(pkg);
    int new_size = pkg.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreatePackageInPackageWorkspaceExpired() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    EXPIRED_WS.createPackage(pkg);
  }

  @Test
  public void testCreateProject() {
    int old_size = WS.getProjects().size();
    WS.createProject();
    int new_size = WS.getProjects().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateProjectWorkspaceExpired() {
    EXPIRED_WS.createProject();
  }

  @Test
  public void testCreateProjectInPackage() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    int old_size = pkg.getArtifacts().size();
    WS.createProject(pkg);
    int new_size = pkg.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateProjectInPackageWorkspaceExpired() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    EXPIRED_WS.createProject(pkg);
  }

  @Test
  public void testCreateMetamodel() {
    int old_size = WS.getMetaModels().size();
    WS.createMetaModel();
    int new_size = WS.getMetaModels().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateMetamodelWorkspaceExpired() {
    EXPIRED_WS.createMetaModel();
  }

  @Test
  public void testCreateMetamodelInPackage() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    int old_size = pkg.getArtifacts().size();
    WS.createMetaModel(pkg);
    int new_size = pkg.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateMetamodelInPackageWorkspaceExpired() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    EXPIRED_WS.createMetaModel(pkg);
  }

  @Test
  public void testCreateArtifact() {
    int old_size = WS.getArtifacts().size();
    WS.createArtifact();
    int new_size = WS.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateArtifactWorkspaceExpired() {
    EXPIRED_WS.createMetaModel();
  }

  @Test
  public void testCreateArtifactInPackage() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    int old_size = pkg.getArtifacts().size();
    WS.createArtifact(pkg);
    int new_size = pkg.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateArtifactInPackageWorkspaceExpired() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    EXPIRED_WS.createArtifact(pkg);
  }

  @Test
  public void testCreateArtifactWithType() {
    Artifact type = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    int old_size = WS.getArtifacts().size();
    Artifact artifact = WS.createArtifact(type);
    int new_size = WS.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
    assertEquals(DataStorage.PROJECT_TYPE_ID, artifact.getType().getId());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateArtifactWithTypeWorkspaceExpired() {
    Artifact type = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    EXPIRED_WS.createArtifact(type);
  }

  @Test
  public void testCreateArtifactWithTypeInPackage() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    Artifact type = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    int old_size = pkg.getArtifacts().size();
    Artifact artifact = WS.createArtifact(type, pkg);
    int new_size = pkg.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
    assertEquals(DataStorage.PROJECT_TYPE_ID, artifact.getType().getId());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateArtifactWithTypeInPackageWorkspaceExpired() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    Artifact type = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    EXPIRED_WS.createArtifact(type, pkg);
  }

  @Test
  public void testCreateArtifactAllParameters() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    Artifact type = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    MetaModel mm = WS.getMetaModel(DataStorage.DESIGNSPACE_META_MODEL);
    Project project = WS.getProject(DataStorage.TEST_PROJECT_ID);
    Map<String, Object> testProperties = new HashMap<>();
    Artifact propertyArtifact = WS.getArtifact(DataStorage.OWNER_TYPE_ID);
    testProperties.put(ARTIFACT_PROPERTY_NAME, propertyArtifact);
    testProperties.put(OBJECT_PROPERTY_NAME, OBJECT_PROPERTY);
    Artifact artifact = WS.createArtifact(type, pkg, mm, project, testProperties);
    assertEquals(pkg, artifact.getPackage());
    assertEquals(type, artifact.getType());
    assertTrue(mm.getArtifacts().contains(artifact));
    assertTrue(project.getArtifacts().contains(artifact));
    assertTrue(artifact.hasProperty(ARTIFACT_PROPERTY_NAME));
    assertTrue(artifact.hasProperty(OBJECT_PROPERTY_NAME));
    assertEquals((Artifact) artifact.getPropertyValue(ARTIFACT_PROPERTY_NAME), propertyArtifact);
    assertEquals((String) artifact.getPropertyValue(OBJECT_PROPERTY_NAME), OBJECT_PROPERTY);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateArtifactAllParametersWorkspaceExpired() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    Artifact type = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    MetaModel mm = WS.getMetaModel(DataStorage.DESIGNSPACE_META_MODEL);
    Project project = WS.getProject(DataStorage.TEST_PROJECT_ID);
    Map<String, Object> testProperties = new HashMap<>();
    Artifact propertyArtifact = WS.getArtifact(DataStorage.OWNER_TYPE_ID);
    testProperties.put(ARTIFACT_PROPERTY_NAME, propertyArtifact);
    testProperties.put(OBJECT_PROPERTY_NAME, OBJECT_PROPERTY);
    EXPIRED_WS.createArtifact(type, pkg, mm, project, testProperties);
  }

  @Test
  public void testCreateCollection() {
    int old_size = WS.getArtifacts().size();
    WS.createCollection(CONTAINSONLYARTIFACTS);
    int new_size = WS.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateCollectionWorkspaceExpired() {
    EXPIRED_WS.createCollection(CONTAINSONLYARTIFACTS);
  }

  @Test
  public void testCreateCollectionInPackage() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    int old_size = pkg.getArtifacts().size();
    WS.createCollection(CONTAINSONLYARTIFACTS, pkg);
    int new_size = pkg.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testCreateCollectionInPackageWorkspaceExpired() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    EXPIRED_WS.createCollection(CONTAINSONLYARTIFACTS, pkg);
  }

  @Test
  public void testGetArtifacts() {
    Collection<Artifact> artifacts = WS.getArtifacts();
    Assert.assertEquals(ARTIFACTS_SIZE, artifacts.size());
  }

  @Test
  public void testGetArtifactsWithIds() {
    Set<Long> ids = new HashSet<>();
    ids.add(DataStorage.MA_TOOL_ID);
    ids.add(DataStorage.COMPLEX_TYPE);
    Collection<Artifact> artifacts = WS.getArtifacts(ids);
    Assert.assertEquals(2, artifacts.size());

  }

  @Test
  public void testGetArtifactsWithPropertyMap() {
    Artifact artifact = WS.getArtifact(DataStorage.COMPLEX_TYPE);
    Set<String> properties = new HashSet<>();
    properties.add(MMMTypeProperties.NAME);
    Map<Artifact, Map<String, Object>> artifactsPropertyMap = WS.getArtifactsPropertyMap(new HashSet<>(Arrays.asList(new Artifact[] { artifact })), properties);
    Assert.assertEquals(1, artifactsPropertyMap.keySet().size());
    Assert.assertEquals(1, artifactsPropertyMap.get(artifact).size());
    Assert.assertEquals(MMMTypeProperties.COMPLEXTYPE_NAME, artifactsPropertyMap.get(artifact).get(MMMTypeProperties.NAME));
  }
  
  @Test
  public void testGetArtifactsPropertyMap() {
    Artifact artifact = WS.getArtifact(DataStorage.COMPLEX_TYPE);
    Map<Artifact, Map<String, Object>> artifactsPropertyMap = WS.getArtifactsPropertyMap(new HashSet<>(Arrays.asList(new Artifact[] { artifact })));
    Assert.assertEquals(1, artifactsPropertyMap.keySet().size());
    Assert.assertNotEquals(1, artifactsPropertyMap.get(artifact).size());
    Assert.assertEquals(MMMTypeProperties.COMPLEXTYPE_NAME, artifactsPropertyMap.get(artifact).get(MMMTypeProperties.NAME));
  }


  @Test(expected = WorkspaceExpiredException.class)
  public void testGetArtifactsWorkspaceExpired() {
    EXPIRED_WS.getArtifacts();
  }

  @Test
  public void testGetArtifactsWithFilter() {
    Collection<Artifact> artifacts = WS.getArtifacts(ARTIFACT_FILTERS);
    assertEquals(ARTIFACTS_FILTERED_SIZE, artifacts.size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetArtifactsWithFilterWorkspaceExpired() {
    EXPIRED_WS.getArtifacts(ARTIFACT_FILTERS);
  }

  @Test
  public void testGetArtifactsWithFilter2() {
    String key = PROPERTY_FILTERS.keySet().toArray(new String[0])[0];
    Collection<Artifact> artifacts = WS.getArtifactsWithProperty(key, PROPERTY_FILTERS.get(key), ARTIFACT_FILTERS);
    assertEquals(ARTIFACTS_FILTERED2_SIZE, artifacts.size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetArtifactsWithFilter2WorkspaceExpired() {
    String key = PROPERTY_FILTERS.keySet().toArray(new String[0])[0];
    EXPIRED_WS.getArtifactsWithProperty(key, PROPERTY_FILTERS.get(key), ARTIFACT_FILTERS);
  }

  @Test
  public void testGetArtifactsWithFilter3() {
    Collection<Artifact> artifacts = WS.getArtifactsWithProperty(PROPERTY_FILTERS, ARTIFACT_FILTERS);
    assertEquals(ARTIFACTS_FILTERED2_SIZE, artifacts.size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetArtifactsWithFilter3WorkspaceExpired() {
    EXPIRED_WS.getArtifactsWithProperty(PROPERTY_FILTERS, ARTIFACT_FILTERS);
  }

  @Test
  public void testGetArtifactsWithReference() {
    Collection<Artifact> artifacts = WS.getArtifactsWithReference(WS.getArtifact(DataStorage.COMPLEX_TYPE), WS.getArtifact(DataStorage.META_MODEL_TYPE_ID));
    Assert.assertEquals(1, artifacts.size());
    Assert.assertEquals(WS.getArtifact(DataStorage.MMM_META_MODEL), artifacts.iterator().next());
  }

  @Test
  public void testGetArtifact() {
    Artifact artifact = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    Assert.assertEquals(DataStorage.PROJECT_TYPE_ID, artifact.getId());
    Assert.assertEquals(WSID, artifact.getVersionNumber());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetArtifactWorkspaceExpired() {
    EXPIRED_WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testGetArtifactDoesNotExist() {
    WS.getArtifact(ARTIFACT_DOESNOTEXIST);
  }

  @Test
  public void testGetPackages() {
    Collection<Package> packages = WS.getPackages();
    Assert.assertEquals(PACKAGES_SIZE, packages.size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetPackagesWorkspaceExpired() {
    EXPIRED_WS.getPackages();
  }

  @Test
  public void testGetPackage() {
    Package pkg = WS.getPackage(DataStorage.AT_PACKAGE_ID);
    Assert.assertEquals(DataStorage.AT_PACKAGE_ID, pkg.getId());
    Assert.assertEquals(WSID, pkg.getVersionNumber());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetPackageWorkspaceExpired() {
    EXPIRED_WS.getPackage(DataStorage.AT_PACKAGE_ID);
  }

  @Test(expected = PackageDoesNotExistException.class)
  public void testGetPackageDoesNotExist() {
    WS.getPackage(PACKAGE_DOESNOTEXIST);
  }

  @Test
  public void testGetProjects() {
    Collection<Project> projects = WS.getProjects();
    Assert.assertEquals(PROJECTS_SIZE, projects.size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetProjectsWorkspaceExpired() {
    EXPIRED_WS.getProjects();
  }

  @Test
  public void testGetProject() {
    Project project = WS.getProject(DataStorage.TEST_PROJECT_ID);
    Assert.assertEquals(DataStorage.TEST_PROJECT_ID, project.getId());
    Assert.assertEquals(WSID, project.getVersionNumber());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetProjectWorkspaceExpired() {
    EXPIRED_WS.getProject(DataStorage.TEST_PROJECT_ID);
  }

  @Test(expected = ProjectDoesNotExistException.class)
  public void testGetProjectDoesNotExist() {
    WS.getProject(PROJECT_DOESNOTEXIST);
  }

  @Test
  public void testGetMetaModels() {
    Collection<MetaModel> metamodels = WS.getMetaModels();
    Assert.assertEquals(METAMODELS_SIZE, metamodels.size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetMetaModelsWorkspaceExpired() {
    EXPIRED_WS.getMetaModels();
  }

  @Test
  public void testGetMetaModel() {
    MetaModel metamodel = WS.getMetaModel(DataStorage.DESIGNSPACE_META_MODEL);
    Assert.assertEquals(DataStorage.DESIGNSPACE_META_MODEL, metamodel.getId());
    Assert.assertEquals(WSID, metamodel.getVersionNumber());
  }

  @Test
  public void testGetMetaMetaModel() {
    MetaModel mm = WS.getMetaModel(DataStorage.MMM_META_MODEL);
    assertEquals(mm, WS.getMetaMetaModel(WS.getArtifact(DataStorage.COMPLEX_TYPE)));
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetMetaModelWorkspaceExpired() {
    EXPIRED_WS.getMetaModel(DataStorage.DESIGNSPACE_META_MODEL);
  }

  @Test(expected = MetaModelDoesNotExistException.class)
  public void testGetMetaModelDoesNotExist() {
    WS.getMetaModel(METAMODEL_DOESNOTEXIST);
  }

  @Test
  public void testGetCollectionArtifacts() {
    Collection<CollectionArtifact> cas = WS.getCollectionArtifacts();
    Assert.assertEquals(COLLECTIONARTIFACTS_SIZE, cas.size());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetCollectionArtifactsWorkspaceExpired() {
    EXPIRED_WS.getCollectionArtifacts();
  }

  @Test
  public void testGetCollectionArtifact() {
    CollectionArtifact ca = WS.getCollectionArtifact(COLLECTIONARTIFACT_ID);
    Assert.assertEquals(COLLECTIONARTIFACT_ID, ca.getId());
    Assert.assertEquals(WSID, ca.getVersionNumber());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetCollectionArtifactWorkspaceExpired() {
    EXPIRED_WS.getCollectionArtifact(COLLECTIONARTIFACT_ID);
  }

  @Test(expected = CollectionArtifactDoesNotExistException.class)
  public void testGetCollectionArtifactDoesNotExist() {
    WS.getCollectionArtifact(COLLECTIONARTIFACT_DOESNOTEXIST);
  }

  @Test
  public void testRollbackArtifact() {
    Artifact artifact = WS.createArtifact();
    int old_size = WS.getArtifacts().size();
    WS.rollbackArtifact(artifact);
    int new_size = WS.getArtifacts().size();
    Assert.assertEquals(old_size - 1, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testRollbackArtifactWorkspaceExpired() {
    Artifact artifact = WS.getArtifact(DataStorage.PACKAGE_TYPE_ID);
    EXPIRED_WS.rollbackArtifact(artifact);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testRollbackArtifactDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    WS.rollbackArtifact(artifact);
    WS.rollbackArtifact(artifact);
  }

  @Test
  public void testRollbackProperty() {
    Artifact artifact = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    Property property = artifact.getProperty(DataStorage.PROPERTY_NAME);
    String oldValue = property.getValue().toString();
    property.setValue(WS, PROPERTY_VALUE_SETTER);
    Assert.assertEquals(PROPERTY_VALUE_SETTER, property.getValue().toString());
    WS.rollbackProperty(property);
    Assert.assertEquals(oldValue, property.getValue().toString());
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testRollbackPropertyWorkspaceExpired() {
    Artifact artifact = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    Property property = artifact.getProperty(DataStorage.PROPERTY_NAME);
    EXPIRED_WS.rollbackProperty(property);
  }

  @Test(expected = ArtifactDoesNotExistException.class)
  public void testRollbackPropertyArtifactDoesNotExist() {
    Artifact artifact = WS.createArtifact();
    Property property = artifact.createProperty(WS, DataStorage.PROPERTY_NAME);
    WS.rollbackArtifact(artifact);
    WS.rollbackProperty(property);
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testRollbackPropertyDoesNotExist() {
    Artifact artifact = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    Property property = artifact.getProperty(PROPERTY_DOESNOTEXIST);
    WS.rollbackProperty(property);
  }

  @Test
  public void testRollbackAll() {
    int i = WS.getArtifacts().size();
    WS.createArtifact();
    WS.createPackage();
    WS.createProject();
    WS.createMetaModel();
    WS.createCollection(CONTAINSONLYARTIFACTS);
    int j = WS.getArtifacts().size();
    assertEquals(i + 5, j);
    int old_size = WS.getArtifacts().size();
    WS.rollbackAll();
    int new_size = WS.getArtifacts().size();
    Assert.assertEquals(old_size - (5 + 1) /*
                                            * +1 for the collectionartifact created @Before
                                            */, new_size);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testRollbackAllWorkspaceExpired() {
    EXPIRED_WS.rollbackAll();
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testClose() {
    Workspace ws = RestCloud.getInstance().createWorkspace(OWNER, TOOL, CLOSING_WS_IDENTIFIER);
    try {
      ws.getArtifact(DataStorage.PROJECT_TYPE_ID);
    } catch (Exception e) {
      throw new UnsupportedOperationException();
    }
    ws.close();
    ws.getArtifact(DataStorage.PROJECT_TYPE_ID);
  }

  @Test(expected = WorkspaceNotEmptyException.class)
  public void testClose2() {
    WS.createArtifact();
    WS.close();
  }

  // @Test
  // public void testAddListener() {
  // throw new UnsupportedOperationException();
  // }
  //
  // @Test
  // public void testRemoveListener() {
  // throw new UnsupportedOperationException();
  // }

}
