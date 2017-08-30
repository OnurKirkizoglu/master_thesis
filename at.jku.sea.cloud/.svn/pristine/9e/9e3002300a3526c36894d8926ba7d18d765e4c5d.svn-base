package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import at.jku.sea.cloud.listeners.WorkspaceAdapter;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCommited;
import at.jku.sea.cloud.listeners.events.property.PropertyCommited;
import at.jku.sea.cloud.listeners.events.property.PropertyValueSet;
import at.jku.sea.cloud.listeners.events.workspace.VersionCommited;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRebased;

public class TestWorkspaceCommitEvents {
  private Owner owner;
  private Tool tool;
  private final Cloud cloud = CloudFactory.getInstance();
  private final TestListener ws1Listener = new TestListener();
  private final TestListener ws2Listener = new TestListener();
  private Workspace ws1, ws2;
  private SecureRandom random = new SecureRandom();

  @Before
  public void setUp() throws Exception {
    this.owner = this.cloud.getOwner(DataStorage.ADMIN);
    this.tool = this.cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    this.ws1 = this.cloud.createWorkspace(owner, tool, "ws1");
    this.ws2 = this.cloud.createWorkspace(owner, tool, "ws2");
    this.ws1.addListener(this.ws1Listener);
    this.ws2.addListener(this.ws2Listener);
    this.ws1Listener.emittedChanges.clear();
    this.ws2Listener.emittedChanges.clear();
  }

  @After
  public void tearDown() throws Exception {
    this.ws1Listener.emittedChanges.clear();
    this.ws2Listener.emittedChanges.clear();
    this.ws1.rollbackAll();
    this.ws2.rollbackAll();
    this.ws1.close();
    this.ws2.close();
  }

  public String randomString() {
    return new BigInteger(130, random).toString(32);
  }

  @Test
  public void testVersionCommitEmptyChanges() {
    ws1.createArtifact();
    ws1.commitAll("");
    assertEquals(1, ws1Listener.emittedChanges.size());
    Change chng = ws1Listener.emittedChanges.iterator().next();
    assertEquals(ChangeType.VersionCommited, chng.getType());
    VersionCommited vrsnCommited = (VersionCommited) chng;
    assertEquals(0, vrsnCommited.changes.size());
  }

  @Test
  public void testArtifactCommitEmptyChanges() {
    Artifact artifact = ws1.createArtifact();
    ws1.commitArtifact(artifact, "");
    assertEquals(1, ws1Listener.emittedChanges.size());
    Change chng = ws1Listener.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactCommited, chng.getType());
    ArtifactCommited artifactCommited = (ArtifactCommited) chng;
    assertEquals(0, artifactCommited.changes.size());
  }

  @Test
  public void testPropertyCommitEmptyChanges() {
    Artifact test = ws1.getArtifact(DataStorage.TEST_PROJECT_ID);
    String propertyName = randomString();
    Property property = test.createProperty(ws1, propertyName);
    property.setValue(ws1, "r1");
    ws1.commitProperty(property, "");
    assertEquals(1, ws1Listener.emittedChanges.size());
    Change chng = ws1Listener.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyCommited, chng.getType());
    PropertyCommited propertyCommited = (PropertyCommited) chng;
    assertEquals(0, propertyCommited.changes.size());
  }

  @Test
  public void testArtifactCommitChange() {
    Artifact test = ws2.getArtifact(DataStorage.TEST_PROJECT_ID);
    String propertyName = randomString();
    Property property = test.createProperty(ws2, propertyName);
    ws2.commitProperty(property, "");
    Artifact a = ws1.createArtifact();
    ws1.commitArtifact(a, "");

    assertEquals(1, ws1Listener.emittedChanges.size());
    Change chng = ws1Listener.emittedChanges.iterator().next();
    assertEquals(ChangeType.ArtifactCommited, chng.getType());
    ArtifactCommited vrsnCommited = (ArtifactCommited) chng;
    assertEquals(1, vrsnCommited.changes.size());
    Change versionChng = vrsnCommited.changes.iterator().next();
    assertEquals(ChangeType.PropertyValueSet, versionChng.getType());
    PropertyValueSet propertyValueSet = (PropertyValueSet) versionChng;
    assertEquals(propertyName, propertyValueSet.property.getName());
    assertEquals(null, propertyValueSet.value);
  }

  @Test
  public void testPropertyCommitChange() {
    Artifact test = ws2.getArtifact(DataStorage.TEST_PROJECT_ID);
    String propertyName = randomString();
    Property property = test.createProperty(ws2, propertyName);
    ws2.commitProperty(property, "");
    Artifact a = ws1.getArtifact(DataStorage.TEST_PROJECT_ID);
    String otherPropertyName = randomString();
    Property property1 = a.createProperty(ws1, otherPropertyName);
    ws1.commitProperty(property1, "");

    assertEquals(1, ws1Listener.emittedChanges.size());
    Change chng = ws1Listener.emittedChanges.iterator().next();
    assertEquals(ChangeType.PropertyCommited, chng.getType());
    PropertyCommited vrsnCommited = (PropertyCommited) chng;
    assertEquals(1, vrsnCommited.changes.size());
    Change versionChng = vrsnCommited.changes.iterator().next();
    assertEquals(ChangeType.PropertyValueSet, versionChng.getType());
    PropertyValueSet propertyValueSet = (PropertyValueSet) versionChng;
    assertEquals(propertyName, propertyValueSet.property.getName());
    assertEquals(null, propertyValueSet.value);
  }

  @Test
  public void testVersionCommitChange() {
    Artifact test = ws2.getArtifact(DataStorage.TEST_PROJECT_ID);
    String propertyName = randomString();
    Property property = test.createProperty(ws2, propertyName);
    ws2.commitProperty(property, "");
    ws1.createArtifact();
    ws1.commitAll("");

    assertEquals(1, ws1Listener.emittedChanges.size());
    Change chng = ws1Listener.emittedChanges.iterator().next();
    assertEquals(ChangeType.VersionCommited, chng.getType());
    VersionCommited vrsnCommited = (VersionCommited) chng;
    assertEquals(1, vrsnCommited.changes.size());
    Change versionChng = vrsnCommited.changes.iterator().next();
    assertEquals(ChangeType.PropertyValueSet, versionChng.getType());
    PropertyValueSet propertyValueSet = (PropertyValueSet) versionChng;
    assertEquals(propertyName, propertyValueSet.property.getName());
    assertEquals(null, propertyValueSet.value);
  }

  @Test
  public void testPropertyWorkspace() {
    Artifact test2 = ws2.getArtifact(DataStorage.TEST_PROJECT_ID);
    Artifact test1 = ws1.getArtifact(DataStorage.TEST_PROJECT_ID);
    String propertyName = randomString();
    Property property2 = test2.createProperty(ws2, propertyName);
    Property property1 = test1.createProperty(ws1, propertyName);
    ws2.commitProperty(property2, "");
    ws1.rebaseToHeadVersion();

    assertEquals(1, ws1Listener.emittedChanges.size());
    Change chng = ws1Listener.emittedChanges.iterator().next();
    assertEquals(ChangeType.WorkspaceRebased, chng.getType());
    WorkspaceRebased vrsnCommited = (WorkspaceRebased) chng;
    assertEquals(0, vrsnCommited.changes.size());
  }

  @Test
  public void testCommitMissingRebaseEvent() {
    ws1.createArtifact();
    ws1.commitAll("");
    ws2.rebaseToHeadVersion();
    ws1.createArtifact();
    ws1.commitAll("");
    ws2.rebaseToHeadVersion();

    assertEquals(2, ws1Listener.emittedChanges.size());
    assertEquals(2, ws2Listener.emittedChanges.size());
    Iterator<Change> iteratorWSone = ws1Listener.emittedChanges.iterator();
    Change chng = iteratorWSone.next();
    assertEquals(ChangeType.VersionCommited, chng.getType());
    chng = iteratorWSone.next();
    assertEquals(ChangeType.VersionCommited, chng.getType());

    Iterator<Change> iteratorWStwo = ws2Listener.emittedChanges.iterator();
    chng = iteratorWStwo.next();
    assertEquals(ChangeType.WorkspaceRebased, chng.getType());
    chng = iteratorWStwo.next();
    assertEquals(ChangeType.WorkspaceRebased, chng.getType());
  }

  private class TestListener extends WorkspaceAdapter {

    public List<Change> emittedChanges = new ArrayList<Change>();

    @Override
    public void workspaceRebased(final WorkspaceRebased event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void artifactCommited(final ArtifactCommited event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void propertyCommited(final PropertyCommited event) throws RemoteException {
      this.emittedChanges.add(event);
    }

    public void versionCommited(final VersionCommited event) throws RemoteException {
      this.emittedChanges.add(event);
    }
  }
}
