package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.CloudFactory;

public class TestCollectionArtifact {
  private Owner owner;
  private Tool tool;
  private static final Cloud cloud = CloudFactory.getInstance();
  private Workspace workspace;

  @Before
  public void setUp() throws Exception {
    this.owner = TestCollectionArtifact.cloud.getOwner(DataStorage.ADMIN);
    if (this.owner == null) {
      this.owner = TestCollectionArtifact.cloud.createOwner();
    }
    this.tool = TestCollectionArtifact.cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    if (this.tool == null) {
      this.tool = TestCollectionArtifact.cloud.createTool();
    }

    this.workspace = cloud.createWorkspace(owner, tool, "");
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testInsert() {
    final Artifact a1 = this.workspace.createArtifact();
    final Artifact a2 = this.workspace.createArtifact();
    final CollectionArtifact collection = this.workspace.createCollection(true);
    collection.addElement(this.workspace, a1);
    collection.addElement(this.workspace, a2);

    assertEquals(2, collection.getElements().size());
    assertTrue(collection.getElements().contains(a1));
    assertTrue(collection.getElements().contains(a2));
  }

  @Test
  public void testGetElements() {
    final Artifact a1 = this.workspace.createArtifact();
    final Artifact a2 = this.workspace.createArtifact();
    final CollectionArtifact collection = this.workspace.createCollection(true);
    collection.addElement(this.workspace, a1);
    collection.addElement(this.workspace, a2);

    assertEquals(2, collection.getElements().size());
    assertEquals(a1, collection.getElementAt(0));
    assertEquals(a2, collection.getElementAt(1));
  }

  @Test
  public void testGetElementsOrder() {
    final Artifact a1 = this.workspace.createArtifact();
    final Artifact a2 = this.workspace.createArtifact();
    final CollectionArtifact collection = this.workspace.createCollection(true);
    collection.addElement(this.workspace, a1);
    collection.addElement(this.workspace, a2);

    Collection<?> elements = collection.getElements();
    assertEquals(2, elements.size());
    Iterator<?> iterator = elements.iterator();
    assertEquals(a1, iterator.next());
    assertEquals(a2, iterator.next());
  }

  @Test
  public void testDelete() {
    final Artifact a1 = this.workspace.createArtifact();
    final Artifact a2 = this.workspace.createArtifact();
    final CollectionArtifact collection = this.workspace.createCollection(true);
    long start = System.nanoTime();
    collection.addElement(this.workspace, a1);
    System.out.println("addElement [ms]: " + (System.nanoTime() - start) / Math.pow(10, 6));
    collection.addElement(this.workspace, a2);
    collection.removeElement(this.workspace, a1);
    assertEquals(1, collection.getElements().size());
    assertTrue(collection.getElements().contains(a2));
  }

  @Test
  public void testGetArtifactReps() {
    final Artifact a1 = this.workspace.createArtifact();
    final Artifact a2 = this.workspace.createArtifact();
    final MetaModel collection = this.workspace.createMetaModel();
    collection.addArtifact(workspace, a1);
    collection.addArtifact(workspace, a2);
    collection.setPropertyValue(workspace, "someValue", this.workspace.createArtifact());

    Collection<Object[]> representations = collection.getArtifactRepresentations();
    assertEquals(2, representations.size());
    Iterator<Object[]> iterator = representations.iterator();
    Object[] a1Rep = iterator.next();
    Object[] a2Rep = iterator.next();
    assertEquals(a1.getId(), a1Rep[DataStorage.Columns.ARTIFACT_ID.getIndex()]);
    assertEquals(a2.getId(), a2Rep[DataStorage.Columns.ARTIFACT_ID.getIndex()]);
  }

  @Test
  public void testGetArtifactAndPropertyMap() {
    final Artifact a1 = this.workspace.createArtifact();
    a1.setPropertyValue(workspace, "name", 2);
    final Artifact a2 = this.workspace.createArtifact();
    final MetaModel collection = this.workspace.createMetaModel();
    collection.addArtifact(workspace, a1);
    collection.addArtifact(workspace, a2);
    collection.setPropertyValue(workspace, "someValue", this.workspace.createArtifact());

    Map<Artifact, Set<Property>> artifactPropertyMap = collection.getArtifactAndProperties();
    assertEquals(2, artifactPropertyMap.keySet().size());

    assertTrue(artifactPropertyMap.containsKey(a1));
    assertTrue(artifactPropertyMap.containsKey(a2));
    assertEquals(1, artifactPropertyMap.get(a1).size());
    assertEquals("name", artifactPropertyMap.get(a1).iterator().next().getName());
  }

  @Test
  public void testGetArtifactAndPropertyReps() {
    final Artifact a1 = this.workspace.createArtifact();
    a1.setPropertyValue(workspace, "name", 2);
    final Artifact a2 = this.workspace.createArtifact();
    final MetaModel collection = this.workspace.createMetaModel();
    collection.addArtifact(workspace, a1);
    collection.addArtifact(workspace, a2);
    collection.setPropertyValue(workspace, "someValue", this.workspace.createArtifact());

    Map<Object[], Set<Object[]>> map = collection.getArtifactPropertyMapRepresentations();
    assertEquals(2, map.keySet().size());
    Iterator<Entry<Object[], Set<Object[]>>> iterator = map.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<Object[], Set<Object[]>> next = iterator.next();
      Object[] key = next.getKey();
      Set<Object[]> value = next.getValue();
      Long artifactId = (Long) key[DataStorage.Columns.ARTIFACT_ID.getIndex()];
      if (artifactId != a1.getId() && artifactId != a2.getId()) {
        assertTrue(false);
      }
      assertTrue(value.size() == 1 || value.size() == 0);
      if (value.size() == 1) {
        assertEquals("name", value.iterator().next()[DataStorage.Columns.PROPERTY_NAME.getIndex()]);
      }
    }
  }

  @Test
  public void testInsertElementAt() {
    final Artifact a1 = this.workspace.createArtifact();
    final CollectionArtifact collection = this.workspace.createCollection(true);
    collection.insertElementAt(workspace, a1, 0);
    assertTrue(collection.existsElement(a1));
    assertEquals(a1, collection.getElementAt(0));
  }

  @Test
  public void testMetaModelCastToCollectionArtifact() {
    MetaModel metaModel = this.workspace.createMetaModel();
    CollectionArtifact ca = (CollectionArtifact) metaModel;
    assertEquals(0, (long) ca.size());
  }

  @Test
  public void testAddElementsArtifact() {
    CollectionArtifact collection = this.workspace.createCollection(true);
    Collection<Object> artifacts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      artifacts.add(this.workspace.createArtifact());
    }
    long start = System.nanoTime();
    collection.addElements(workspace, artifacts);
    System.out.println("addElements [ms]: " + (System.nanoTime() - start) / Math.pow(10, 6));
    assertEquals(10, (long) collection.size());
  }

  @Test
  public void testAddElementsObjects() {
    CollectionArtifact collection = this.workspace.createCollection(false);
    Collection<Object> artifacts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      artifacts.add(i);
    }
    collection.addElements(workspace, artifacts);
    assertEquals(10, (long) collection.size());
  }
}
