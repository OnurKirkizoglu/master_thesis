package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.CloudFactory;
import at.jku.sea.cloud.listeners.WorkspaceAdapter;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactAliveSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCreated;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactContainerSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactTypeSet;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElement;
import at.jku.sea.cloud.listeners.events.artifact.CollectionRemovedElement;
import at.jku.sea.cloud.listeners.events.property.PropertyAliveSet;
import at.jku.sea.cloud.listeners.events.property.PropertyValueSet;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRebased;

public class TestEmitChangesAfterRebase {
  private Owner owner;
  private Tool tool;
  private final Cloud cloud = CloudFactory.getInstance();
  private final TestListener listener = new TestListener();
  private Workspace ws1, ws2;
  private long id1, id2, caId;

  @Before
  public void setUp() throws Exception {
    this.owner = this.cloud.getOwner(DataStorage.ADMIN);
    this.tool = this.cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    final Workspace ws = this.cloud.createWorkspace(owner, tool, "baseWS");
    final Artifact a1 = ws.createArtifact();
    a1.setPropertyValue(ws, "n", "someValue");
    final Artifact a2 = ws.createArtifact();
    final CollectionArtifact ca1 = ws.createCollection(true);
    ca1.addElement(ws, a1);
    ca1.addElement(ws, a2);
    this.caId = ca1.getId();
    ws.commitAll("");
    ws.close();
    this.id1 = a1.getId();
    this.id2 = a2.getId();
    this.ws1 = this.cloud.createWorkspace(owner, tool, "ws1");
    this.ws2 = this.cloud.createWorkspace(owner, tool, "ws2");
    this.ws2.addListener(this.listener);
    this.listener.emittedChanges.clear();
  }

  @After
  public void tearDown() throws Exception {
    this.listener.emittedChanges.clear();
    this.ws1.rollbackAll();
    this.ws2.rollbackAll();
    this.ws1.close();
    this.ws2.close();
  }

  @Test
  public void testArtifactCreatedTest() {
    final Artifact artifact = this.ws1.createArtifact();
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final ArtifactCreated change = (ArtifactCreated) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.ArtifactCreated, change.getType());
    assertEquals(artifact.getId(), change.artifact.getId());
  }

  @Test
  public void testArtifactAliveSetTest() {
    final Artifact artifact = this.ws1.getArtifact(this.id1);
    artifact.delete(this.ws1);
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final ArtifactAliveSet change = (ArtifactAliveSet) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.ArtifactAliveSet, change.getType());
    assertEquals(artifact.getId(), change.artifact.getId());
    assertEquals(false, change.artifact.isAlive());
  }

  @Test
  public void testArtifactTypeSetTest() {
    final Artifact artifact = this.ws1.getArtifact(this.id1);
    final Artifact type = this.ws1.getArtifact(this.id2);
    artifact.setType(this.ws1, type);
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final ArtifactTypeSet change = (ArtifactTypeSet) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.ArtifactTypeSet, change.getType());
    assertEquals(artifact.getId(), change.artifact.getId());
    assertEquals(type.getId(), change.artifact.getType().getId());
  }

  @Test
  public void testArtifactTypeSetMultipleCommitsTest() {
    final Artifact artifact = this.ws1.getArtifact(this.id1);
    artifact.setType(this.ws1, this.ws1.getArtifact(DataStorage.TRACE_LINK));
    this.ws1.commitAll("");
    this.ws1.getArtifact(this.id2);
    artifact.setType(this.ws1, this.ws1.getArtifact(DataStorage.DATA_TYPE));
    this.ws1.commitAll("");
    final Artifact type = this.ws1.getArtifact(this.id2);
    artifact.setType(this.ws1, type);
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final ArtifactTypeSet change = (ArtifactTypeSet) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.ArtifactTypeSet, change.getType());
    assertEquals(artifact.getId(), change.artifact.getId());
    assertEquals(type.getId(), change.artifact.getType().getId());
  }

  @Test
  public void testArtifactTypeSetMultipleCommitsTest_CRInRange() {
    final Artifact artifact = this.ws1.createArtifact();
    artifact.setType(this.ws1, this.ws1.getArtifact(DataStorage.TRACE_LINK));
    this.ws1.commitAll("");
    this.ws1.getArtifact(this.id2);
    artifact.setType(this.ws1, this.ws1.getArtifact(DataStorage.DATA_TYPE));
    this.ws1.commitAll("");
    final Artifact type = this.ws1.getArtifact(this.id2);
    artifact.setType(this.ws1, type);
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final ArtifactCreated change = (ArtifactCreated) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.ArtifactCreated, change.getType());
    assertEquals(artifact.getId(), change.artifact.getId());
    assertEquals(type.getId(), change.artifact.getType().getId());
  }

  @Test
  public void testArtifactPackageSetTest() {
    final Artifact artifact = this.ws1.getArtifact(this.id1);
    final Package pckg = this.ws1.createPackage();
    artifact.setPackage(this.ws1, pckg);
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(2, this.listener.emittedChanges.size());
    // Pckg created
    final ArtifactCreated change = (ArtifactCreated) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.ArtifactCreated, change.getType());
    assertEquals(pckg.getId(), change.artifact.getId());
    // PckgSet
    final ArtifactContainerSet changePckg = (ArtifactContainerSet) this.listener.emittedChanges.get(1);
    assertEquals(ChangeType.ArtifactContainerSet, changePckg.getType());
    assertEquals(artifact.getId(), changePckg.artifact.getId());
    assertEquals(pckg.getId(), changePckg.artifact.getPackage().getId());
  }

  @Test
  public void testPropertyAliveSetTest() {
    final Artifact artifact = this.ws1.getArtifact(this.id1);
    artifact.deleteProperty(this.ws1, "n");
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final PropertyAliveSet change = (PropertyAliveSet) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.PropertyAliveSet, change.getType());
    assertEquals(artifact.getId(), change.property.getId());
    assertEquals("n", change.property.getName());
    assertEquals(artifact.isPropertyAlive("n"), change.property.isAlive());
  }

  @Test
  public void testPropertyValueSetTest() {
    final Artifact artifact = this.ws1.getArtifact(this.id2);
    double random = Math.random();
    artifact.setPropertyValue(this.ws1, "name", random);
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final PropertyValueSet change = (PropertyValueSet) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.PropertyValueSet, change.getType());
    assertEquals(artifact.getId(), change.property.getId());
    assertEquals("name", change.property.getName());
    assertEquals(artifact.getPropertyValue("name"), change.property.getValue());

    assertEquals(null, change.oldValue);
    assertEquals(random, change.value);
  }

  @Test
  public void testPropertyValueSetArtifactTest() {
    final Artifact artifact = this.ws1.getArtifact(this.id2);
    artifact.setPropertyValue(this.ws1, "name", this.ws1.getArtifact(this.id1));
    long newHeadVersion = this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final PropertyValueSet change = (PropertyValueSet) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.PropertyValueSet, change.getType());
    assertEquals(artifact.getId(), change.property.getId());
    assertEquals("name", change.property.getName());
    assertEquals(((Artifact) artifact.getPropertyValue("name")).getId(), ((Artifact) change.property.getValue()).getId());
  }

  @Test
  public void testPropertyValueSetNullTest() {
    final Artifact artifact = this.ws1.getArtifact(this.id2);
    artifact.setPropertyValue(this.ws1, "name", null);
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final PropertyValueSet change = (PropertyValueSet) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.PropertyValueSet, change.getType());
    assertEquals(artifact.getId(), change.property.getId());
    assertEquals("name", change.property.getName());
    assertEquals(artifact.getPropertyValue("name"), change.property.getValue());
  }

  @Test
  public void testCollectionAddedElementTest() {
    final CollectionArtifact ca = this.ws1.getCollectionArtifact(this.caId);
    final Artifact artifact = this.ws1.getArtifact(this.id1);
    ca.addElement(this.ws1, artifact);
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final CollectionAddedElement change = (CollectionAddedElement) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.CollectionAddedElement, change.getType());
    assertEquals(ca.getId(), change.artifact.getId());
    final Artifact element = (Artifact) change.value;
    assertEquals(artifact.getId(), element.getId());
  }

  @Test
  public void testCollectionRemovedElementTest() {
    final CollectionArtifact ca = this.ws1.getCollectionArtifact(this.caId);
    final Artifact artifact = this.ws1.getArtifact(this.id1);
    ca.removeElement(this.ws1, artifact);
    this.ws1.commitAll("");
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final CollectionRemovedElement change = (CollectionRemovedElement) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.CollectionRemovedElement, change.getType());
    assertEquals(ca.getId(), change.artifact.getId());
    final Artifact element = (Artifact) change.value;
    assertEquals(artifact.getId(), element.getId());
  }
  
  
  @Test
  public void testCollectionAddedElementWorkspaceTest() {
    final CollectionArtifact ca = this.ws1.getCollectionArtifact(this.caId);
    final Artifact artifact = this.ws1.getArtifact(this.id1);
    ca.addElement(this.ws1, artifact);
    this.ws1.commitAll("");
    
    final CollectionArtifact ca2 = this.ws2.getCollectionArtifact(this.caId);
    final Artifact artifact2 = this.ws2.getArtifact(this.id1);
    ca2.addElement(this.ws1, artifact2);
    
    
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final CollectionAddedElement change = (CollectionAddedElement) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.CollectionAddedElement, change.getType());
    assertEquals(ca.getId(), change.artifact.getId());
    final Artifact element = (Artifact) change.value;
    assertEquals(artifact.getId(), element.getId());
  }

  @Test
  public void testCollectionRemovedElementWorkspaceTest() {
    final CollectionArtifact ca = this.ws1.getCollectionArtifact(this.caId);
    final Artifact artifact = this.ws1.getArtifact(this.id1);
    ca.addElement(this.ws1, artifact);
    this.ws1.commitAll("");
    
    final CollectionArtifact ca2 = this.ws2.getCollectionArtifact(this.caId);
    final Artifact artifact2 = this.ws2.getArtifact(this.id1);
    ca2.addElement(this.ws1, artifact2);
    
    this.ws2.rebaseToHeadVersion();
    assertEquals(1, this.listener.emittedChanges.size());
    final CollectionAddedElement change = (CollectionAddedElement) this.listener.emittedChanges.get(0);
    assertEquals(ChangeType.CollectionAddedElement, change.getType());
    assertEquals(ca.getId(), change.artifact.getId());
    final Artifact element = (Artifact) change.value;
    assertEquals(artifact.getId(), element.getId());
  }

  private class TestListener extends WorkspaceAdapter {

    public List<Change> emittedChanges = new ArrayList<Change>();

    @Override
    public void workspaceRebased(final WorkspaceRebased event) throws RemoteException {
      this.emittedChanges.addAll(event.changes);
    }

  }

}
