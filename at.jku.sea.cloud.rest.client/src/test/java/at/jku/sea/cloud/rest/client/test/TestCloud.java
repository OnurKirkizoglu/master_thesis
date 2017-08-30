package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.ADMIN_PASSWORD;
import static at.jku.sea.cloud.rest.client.test.TestAll.DATASTORAGE;
import static at.jku.sea.cloud.rest.client.test.TestAll.EXPIRED_WS;
import static at.jku.sea.cloud.rest.client.test.TestAll.EXPIRED_WSID;
import static at.jku.sea.cloud.rest.client.test.TestAll.EXPIRED_WS_IDENTIFIER;
import static at.jku.sea.cloud.rest.client.test.TestAll.OWNER;
import static at.jku.sea.cloud.rest.client.test.TestAll.TOOL;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS_IDENTIFIER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
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
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import at.jku.sea.cloud.rest.client.RestCloud;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 * @author Roland Kretschmer
 */
public class TestCloud {

  public static long OWNERID_DOESNOTEXIST = 0;
  public static long TOOLID_DOESNOTEXIST = 0;

  public static long BASEVERSION_ILLEGALARGUMENT = 100;

  public static long HEADVERSION = 1;

  private RestCloud cloud;

  @Before
  public void setUp() {
    cloud = RestCloud.getInstance();
  }

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
  public void testGetOwner() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    assertEquals(DataStorage.ADMIN, owner.getId());
  }

  @Test(expected = OwnerDoesNotExistException.class)
  public void testGetOwnerDoesNotExist() {
    cloud.getOwner(OWNERID_DOESNOTEXIST);
  }

  @Test
  public void testGetTool() {
    Tool tool = cloud.getTool(DataStorage.ECLIPSE_TOOL_ID);
    assertEquals(DataStorage.ECLIPSE_TOOL_ID, tool.getId());
  }

  @Test(expected = ToolDoesNotExistException.class)
  public void testGetToolDoesNotExist() {
    cloud.getTool(TOOLID_DOESNOTEXIST);
  }

  @Test
  public void testGetWorkspace() {
    Workspace workspace = cloud.getWorkspace(OWNER, TOOL, WS_IDENTIFIER);
    assertTrue(workspace.getIdentifier().equals(WS_IDENTIFIER));
  }

  public void testGetWorkspaceExpired() {
    Workspace workspace = cloud.getWorkspace(OWNER, TOOL, EXPIRED_WS_IDENTIFIER);
    assertEquals(null, workspace);
  }

  @Test(expected = WorkspaceExpiredException.class)
  public void testGetWorkspaceExpiredWSID() {
    cloud.getWorkspace(EXPIRED_WSID);
  }

  @Test
  public void testGetWorkspaceWithBaseVersion() {
    Workspace workspace = cloud.getWorkspace(OWNER, TOOL, WS_IDENTIFIER, DataStorage.FIRST_VERSION);
    assertTrue(workspace.getIdentifier().equals(WS_IDENTIFIER));
    assertEquals(DataStorage.FIRST_VERSION, workspace.getBaseVersion().getVersionNumber());
  }

  public void testGetWorkspaceWithBaseVersionExpired() {
    Workspace workspace = cloud.getWorkspace(OWNER, TOOL, EXPIRED_WS_IDENTIFIER, DataStorage.FIRST_VERSION);
    assertEquals(null, workspace);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWorkspaceWithIllegalBaseVersion() {
    cloud.getWorkspace(OWNER, TOOL, WS_IDENTIFIER, BASEVERSION_ILLEGALARGUMENT);
  }

  @Test
  public void testGetVersion() {
    Version version = cloud.getVersion(DataStorage.FIRST_VERSION);
    assertEquals(DataStorage.FIRST_VERSION, version.getVersionNumber());
  }

  @Test(expected = VersionDoesNotExistException.class)
  public void testGetVersionDoesNotExist() {
    cloud.getVersion(BASEVERSION_ILLEGALARGUMENT);
  }

  @Test
  public void testGetHeadVersion() {
    long head = cloud.getHeadVersionNumber();
    assertEquals(HEADVERSION, head);
  }

  @Test
  public void testCreateOwner() {
    cloud.createOwner();
  }

  @Test
  public void testCreateTool() {
    cloud.createTool();
  }

  @Test
  public void testGetHeadArtifacts() {
    Collection<Artifact> a = cloud.getHeadArtifacts();
    assertEquals(217, a.size());
  }

  @Test
  public void testGetAllVersions() {
    Collection<Version> versions = cloud.getAllVersions();
    assertEquals(2, versions.size());
  }

  @Test
  public void testGetMetaModels() {
    Collection<MetaModel> models = cloud.getMetaModels(1);
    assertEquals(5, models.size());
  }

  @Test
  public void testGetPackages() {
    Collection<Package> pkgs = cloud.getPackages(1);
    assertEquals(42, pkgs.size());
  }

  @Test
  public void testGetProjects() {
    Collection<Project> prj = cloud.getProjects(1);
    assertEquals(1, prj.size());
  }

  @Test
  public void testGetWorkspaces() {
    List<Workspace> ws = cloud.getWorkspaces();
    assertEquals(1, ws.size());
  }

  @Test
  public void testGetArtifactsWithFilter() {
    MetaModel mm = cloud.getMetaModel(1, 49);

    Collection<Artifact> arts = cloud.getArtifacts(1, mm);
    assertEquals(14, arts.size());
  }

  @Test
  public void testGetArtifactsWithVersion() {
    Collection<Artifact> arts = cloud.getArtifacts(1);
    assertEquals(217, arts.size());
  }

  @Test
  public void testGetArtifact() {
    Artifact a = cloud.getArtifact(DataStorage.PACKAGE_TYPE_ID, 1);
    assertEquals(DataStorage.PACKAGE_TYPE_ID, a.getId());
    assertEquals(1, a.getVersionNumber());
  }

  @Test
  public void testGetHeadArtifactsWithFilter() {
    MetaModel mm = cloud.getMetaModel(1, 49);
    Collection<Artifact> arts = cloud.getHeadArtifacts(mm);
    assertEquals(14, arts.size());
  }

  @Test
  public void testGetPackage() {
    Package pkg = cloud.getPackage(1, DataStorage.AT_PACKAGE_ID);
    assertEquals(DataStorage.AT_PACKAGE_ID, pkg.getId());
    assertEquals(1, pkg.getVersionNumber());
  }

  @Test
  public void testGetHeadPackages() {
    Collection<Package> pkgs = cloud.getHeadPackages();
    assertEquals(42, pkgs.size());
  }

  @Test
  public void testGetProject() {
    Project prj = cloud.getProject(1, DataStorage.TEST_PROJECT_ID);
    assertEquals(DataStorage.TEST_PROJECT_ID, prj.getId());
    assertEquals(1, prj.getVersionNumber());
  }

  @Test
  public void testGetHeadProjects() {
    Collection<Project> prj = cloud.getHeadProjects();
    assertEquals(1, prj.size());
  }

  @Test
  public void testGetMetaModel() {
    MetaModel mm = cloud.getMetaModel(1, DataStorage.LINK_META_MODEL_ID);
    assertEquals(DataStorage.LINK_META_MODEL_ID, mm.getId());
    assertEquals(1, mm.getVersionNumber());
  }

  @Test
  public void testGetHeadMetaModel() {
    Collection<MetaModel> mm = cloud.getHeadMetaModels();
    assertEquals(5, mm.size());
  }

  @Test
  public void testGetCollectionArtifact() {
    Artifact link = cloud.getArtifact(1, DataStorage.TRACE_LINK);
    CollectionArtifact ca1 = (CollectionArtifact) link.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES);
    CollectionArtifact ca = cloud.getCollectionArtifact(1, ca1.getId());

    assertEquals(ca1.getId(), ca.getId());
    assertEquals(1, ca.getVersionNumber());
  }

  @Test
  public void testGetCollectionArtifacts() {
    Collection<CollectionArtifact> ca = cloud.getCollectionArtifacts(1);
    assertEquals(76, ca.size());
  }

  @Test
  public void testGetHeadCollectionArtifacts() {
    Collection<CollectionArtifact> ca = cloud.getHeadCollectionArtifacts();
    assertEquals(76, ca.size());
  }

  @Test
  public void testGetPrivateVersions() {
    List<Version> v = cloud.getPrivateVersions();
    assertEquals(1, v.size());
  }

  @Test
  public void testGetArtifactHistoryVersionNumbers() {
    Artifact a = cloud.getArtifact(1, DataStorage.ROOT_TYPE_ID);
    List<Version> v = cloud.getArtifactHistoryVersionNumbers(a);
    assertEquals(1, v.size());
  }

  @Test
  public void testGetPropertyHistoryVersionNumbers() {
    Artifact a = cloud.getArtifact(1, DataStorage.ROOT_TYPE_ID);
    List<Version> v = cloud.getPropertyHistoryVersionNumbers(a, DataStorage.PROPERTY_NAME);
    assertEquals(1, v.size());
  }

  @Test
  public void testGetArtifactDiffs() {
    Set<Artifact> a = cloud.getArtifactDiffs(1, 1);
    assertEquals(0, a.size());
  }

  @Test
  public void testGetArtifactRepresentation() {
    Object[] rep = cloud.getArtifactRepresentation(1, 1);
    assertEquals(7, rep.length);
  }

  @Test
  public void testGetDiff() {
    Map<Artifact, Set<Property>> ap = cloud.getDiffs(1);
    assertEquals(217, ap.size());
  }

  @Test
  public void testGetDiff2() {
    Map<Artifact, Set<Property>> ap = cloud.getDiffs(1, 1);
    assertEquals(0, ap.size());
  }

  @Test
  public void testGetWorkspaceChildren1() {
	  cloud.getWorkspaceChildren(1l);
  }
  
  @Test
  public void testGetWorkspaceChildren2() {
	  Collection<Workspace> wss = cloud.getWorkspaceChildren(null);
	  assertEquals(1, wss.size());
  }
}
