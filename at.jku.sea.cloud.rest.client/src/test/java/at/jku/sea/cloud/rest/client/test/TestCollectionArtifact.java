package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;

public class TestCollectionArtifact {

  private static CollectionArtifact COLLECTIONARTIFACT_ONLY_ARTIFACT;
  private static CollectionArtifact COLLECTIONARTIFACT_NO_ARTIFACT;
  private static long COLLECTIONARTIFACT_SIZE = 0;

  private static long INDEX = 0;
  private static long INDEX_OUTOFBOUND = 1;
  private static final boolean CONTAINS_ONLY_ARTIFACTS = true;
  private static final boolean CONTAINS_NO_ARTIFACTS = false;

  private static Object OBJECT = "test";
  private static Artifact ARTIFACT;

  @BeforeClass
  public static void setUpBeforeClass() {
    ARTIFACT = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
  }

  @Before
  public void setUp() {
    COLLECTIONARTIFACT_ONLY_ARTIFACT = WS.createCollection(CONTAINS_ONLY_ARTIFACTS);
    COLLECTIONARTIFACT_NO_ARTIFACT = WS.createCollection(CONTAINS_NO_ARTIFACTS);
  }

  @After
  public void tearDown() {
    WS.rollbackAll();
  }

  @Test
  public void testContainsOnlyArtifacts() {
    assertEquals(CONTAINS_ONLY_ARTIFACTS, COLLECTIONARTIFACT_ONLY_ARTIFACT.isContainingOnlyArtifacts());
  }

  @Test
  public void testContainsNoArtifacts() {
    assertEquals(CONTAINS_NO_ARTIFACTS, COLLECTIONARTIFACT_NO_ARTIFACT.isContainingOnlyArtifacts());
  }

  @Test
  public void testAddElementWithObject() {
    int old_size = COLLECTIONARTIFACT_NO_ARTIFACT.getElements().size();
    COLLECTIONARTIFACT_NO_ARTIFACT.addElement(WS, OBJECT);
    int new_size = COLLECTIONARTIFACT_NO_ARTIFACT.getElements().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test
  public void testAddElementWithArtifact() {
    int old_size = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElements().size();
    COLLECTIONARTIFACT_ONLY_ARTIFACT.addElement(WS, ARTIFACT);
    int new_size = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElements().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testAddElementArtifactDead() {
    COLLECTIONARTIFACT_NO_ARTIFACT.delete(WS);
    COLLECTIONARTIFACT_NO_ARTIFACT.addElement(WS, OBJECT);
  }

  @Test
  public void testRemoveElementWithObject() {
    COLLECTIONARTIFACT_NO_ARTIFACT.addElement(WS, OBJECT);
    int old_size = COLLECTIONARTIFACT_NO_ARTIFACT.getElements().size();
    COLLECTIONARTIFACT_NO_ARTIFACT.removeElement(WS, OBJECT);
    int new_size = COLLECTIONARTIFACT_NO_ARTIFACT.getElements().size();
    assertEquals(old_size - 1, new_size);
  }

  @Test
  public void testRemoveElementWithArtifact() {
    COLLECTIONARTIFACT_ONLY_ARTIFACT.addElement(WS, ARTIFACT);
    int old_size = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElements().size();
    COLLECTIONARTIFACT_ONLY_ARTIFACT.removeElement(WS, ARTIFACT);
    int new_size = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElements().size();
    assertEquals(old_size - 1, new_size);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testRemoveElementArtifactDead() {
    COLLECTIONARTIFACT_NO_ARTIFACT.delete(WS);
    COLLECTIONARTIFACT_NO_ARTIFACT.removeElement(WS, OBJECT);
  }

  @Test
  public void testInsertElementAtWithObject() {
    long old_size = COLLECTIONARTIFACT_NO_ARTIFACT.getElements().size();
    COLLECTIONARTIFACT_NO_ARTIFACT.addElement(WS, OBJECT);
    long new_size = COLLECTIONARTIFACT_NO_ARTIFACT.getElements().size();
    assertEquals(old_size + 1, new_size);
    COLLECTIONARTIFACT_NO_ARTIFACT.insertElementAt(WS, OBJECT, INDEX);
    new_size = COLLECTIONARTIFACT_NO_ARTIFACT.getElements().size();
    assertEquals(old_size + 2, new_size);
    Object object = COLLECTIONARTIFACT_NO_ARTIFACT.getElementAt(INDEX);
    assertEquals(OBJECT.toString(), object.toString());
  }

  @Test
  public void testInsertElementAtWithArtifact() {
    long old_size = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElements().size();
    COLLECTIONARTIFACT_ONLY_ARTIFACT.addElement(WS, ARTIFACT);
    long new_size = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElements().size();
    assertEquals(old_size + 1, new_size);
    COLLECTIONARTIFACT_ONLY_ARTIFACT.insertElementAt(WS, ARTIFACT, INDEX);
    new_size = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElements().size();
    assertEquals(old_size + 2, new_size);
    Object object = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElementAt(INDEX);
    assertTrue(object instanceof Artifact);
    assertEquals(ARTIFACT.getId(), ((Artifact) object).getId());
    assertEquals(ARTIFACT.getVersionNumber(), ((Artifact) object).getVersionNumber());
  }

  @Test(expected = ArtifactDeadException.class)
  public void testInsertElementAtArtifactDead() {
    COLLECTIONARTIFACT_NO_ARTIFACT.delete(WS);
    COLLECTIONARTIFACT_NO_ARTIFACT.insertElementAt(WS, OBJECT, INDEX);
  }

  @Test
  public void testRemoveElementAt() {
    COLLECTIONARTIFACT_NO_ARTIFACT.addElement(WS, OBJECT);
    COLLECTIONARTIFACT_NO_ARTIFACT.addElement(WS, OBJECT);
    int old_size = COLLECTIONARTIFACT_NO_ARTIFACT.getElements().size();
    assertEquals(2, old_size);
    COLLECTIONARTIFACT_NO_ARTIFACT.removeElementAt(WS, INDEX);
    int new_size = COLLECTIONARTIFACT_NO_ARTIFACT.getElements().size();
    assertEquals(old_size - 1, new_size);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testRemoveElementAtArtifactDead() {
    COLLECTIONARTIFACT_ONLY_ARTIFACT.delete(WS);
    COLLECTIONARTIFACT_ONLY_ARTIFACT.removeElementAt(WS, INDEX);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveElementAtIndexOutOfBounds() {
    COLLECTIONARTIFACT_ONLY_ARTIFACT.removeElementAt(WS, INDEX_OUTOFBOUND);
  }

  @Test
  public void testGetElementAtWithObject() {
    COLLECTIONARTIFACT_NO_ARTIFACT.addElement(WS, OBJECT);
    Object object = COLLECTIONARTIFACT_NO_ARTIFACT.getElementAt(INDEX);
    assertEquals(OBJECT.toString(), object.toString());
  }

  @Test
  public void testGetElementAtWithArtifact() {
    COLLECTIONARTIFACT_ONLY_ARTIFACT.addElement(WS, ARTIFACT);
    Object object = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElementAt(INDEX);
    assertTrue(object instanceof Artifact);
    assertEquals(ARTIFACT.getId(), ((Artifact) object).getId());
    assertEquals(ARTIFACT.getVersionNumber(), ((Artifact) object).getVersionNumber());
  }

  @Test
  public void testGetElements() {
    int size = COLLECTIONARTIFACT_ONLY_ARTIFACT.getElements().size();
    assertEquals(COLLECTIONARTIFACT_SIZE, size);
  }

  @Test
  public void testExistsElementWithObject() {
    assertFalse(COLLECTIONARTIFACT_NO_ARTIFACT.existsElement(OBJECT));
    COLLECTIONARTIFACT_NO_ARTIFACT.addElement(WS, OBJECT);
    assertTrue(COLLECTIONARTIFACT_NO_ARTIFACT.existsElement(OBJECT));
  }

  @Test
  public void testExistsElementWithArtifact() {
    assertFalse(COLLECTIONARTIFACT_ONLY_ARTIFACT.existsElement(ARTIFACT));
    COLLECTIONARTIFACT_ONLY_ARTIFACT.addElement(WS, ARTIFACT);
    assertTrue(COLLECTIONARTIFACT_ONLY_ARTIFACT.existsElement(ARTIFACT));
  }

  @Test
  public void testSize() {
    long actualSize = COLLECTIONARTIFACT_NO_ARTIFACT.size();
    assertEquals(COLLECTIONARTIFACT_SIZE, actualSize);
    COLLECTIONARTIFACT_NO_ARTIFACT.addElement(WS, OBJECT);
    actualSize = COLLECTIONARTIFACT_NO_ARTIFACT.size();
    assertEquals(COLLECTIONARTIFACT_SIZE + 1, actualSize);
  }

  @Test
  public void testGetArtifactReps() {
    final Artifact a1 = WS.createArtifact();
    final Artifact a2 = WS.createArtifact();
    final MetaModel collection = WS.createMetaModel();
    collection.addArtifact(WS, a1);
    collection.addArtifact(WS, a2);
    collection.setPropertyValue(WS, "someValue", WS.createArtifact());

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
    final Artifact a1 = WS.createArtifact();
    a1.setPropertyValue(WS, "name", 2);
    final Artifact a2 = WS.createArtifact();
    final MetaModel collection = WS.createMetaModel();
    collection.addArtifact(WS, a1);
    collection.addArtifact(WS, a2);
    collection.setPropertyValue(WS, "someValue", WS.createArtifact());

    Map<Artifact, Set<Property>> artifactPropertyMap = collection.getArtifactAndProperties();
    assertEquals(2, artifactPropertyMap.keySet().size());

    assertTrue(artifactPropertyMap.containsKey(a1));
    assertTrue(artifactPropertyMap.containsKey(a2));
    assertEquals(1, artifactPropertyMap.get(a1).size());
    assertEquals("name", artifactPropertyMap.get(a1).iterator().next().getName());
  }

  @Test
  public void testGetArtifactAndPropertyReps() {
    final Artifact a1 = WS.createArtifact();
    a1.setPropertyValue(WS, "name", 2);
    final Artifact a2 = WS.createArtifact();
    final MetaModel collection = WS.createMetaModel();
    collection.addArtifact(WS, a1);
    collection.addArtifact(WS, a2);
    collection.setPropertyValue(WS, "someValue", WS.createArtifact());

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
  public void testAddElementsArtifact() {
    CollectionArtifact collection = WS.createCollection(true);
    Collection<Object> artifacts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      artifacts.add(WS.createArtifact());
    }
    collection.addElements(WS, artifacts);
    assertEquals(10, (long) collection.size());
  }

  @Test
  public void testAddElementsObjects() {
    CollectionArtifact collection = WS.createCollection(false);
    Collection<Object> artifacts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      artifacts.add(i);
    }
    collection.addElements(WS, artifacts);
    assertEquals(10, (long) collection.size());
  }

  @Test
  public void testCollectionArtifactConstructor() {
    Collection<Object> artifacts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      artifacts.add(WS.createArtifact());
    }
    Map<String, Object> properties = new HashMap<>();
    properties.put(DataStorage.PROPERTY_NAME, "testCollection");
    CollectionArtifact collection = WS.createCollection(true, null, artifacts, properties);
    assertEquals(10, (long) collection.size());
    assertEquals("testCollection", collection.getPropertyValue(DataStorage.PROPERTY_NAME));
  }
}
