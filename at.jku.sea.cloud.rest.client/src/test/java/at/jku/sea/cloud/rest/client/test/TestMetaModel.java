package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class TestMetaModel {

  private static int ARTIFACTS_SIZE = 8;
  private static MetaModel METAMODEL;

  @BeforeClass
  public static void setUpBeforeClass() {
    METAMODEL = WS.getMetaModel(DataStorage.DESIGNSPACE_META_MODEL);
  }

  @After
  public void tearDown() {
    WS.rollbackAll();
  }

  @Test
  public void testAddArtifact() {
    Artifact artifact = WS.createArtifact();
    int old_size = METAMODEL.getArtifacts().size();
    METAMODEL.addArtifact(WS, artifact);
    int new_size = METAMODEL.getArtifacts().size();
    assertEquals(old_size + 1, new_size);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testAddArtifactArtifactDead() {
    Artifact artifact = WS.createArtifact();
    // artifact.delete(WS);
    METAMODEL.delete(WS);
    METAMODEL.addArtifact(WS, artifact);
  }

  @Test
  public void testRemoveArtifact() {
    Artifact artifact = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    int old_size = METAMODEL.getArtifacts().size();
    METAMODEL.removeArtifact(WS, artifact);
    int new_size = METAMODEL.getArtifacts().size();
    assertEquals(old_size - 1, new_size);
  }

  @Test(expected = ArtifactDeadException.class)
  public void testRemoveArtifactArtifactDead() {
    Artifact artifact = WS.getArtifact(DataStorage.PROJECT_TYPE_ID);
    // artifact.delete(WS);
    METAMODEL.delete(WS);
    METAMODEL.removeArtifact(WS, artifact);
  }

  @Test
  public void testGetArtifacts() {
    Collection<Artifact> artifacts = METAMODEL.getArtifacts();
    assertEquals(ARTIFACTS_SIZE, artifacts.size());
  }

  @Test
  public void testGetMetaModel() {
    MetaModel mm = WS.getMetaModel(DataStorage.MMM_META_MODEL);
    assertEquals(mm, mm.getMetaModel());
  }

  @Test
  public void testGetMetaModelNull() {
    MetaModel mm = WS.createMetaModel();
    assertEquals(null, mm.getMetaModel());
  }

  @Test
  public void testAddArtifacts() {
    MetaModel mm = WS.createMetaModel();
    Collection<Artifact> artifacts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      artifacts.add(WS.createArtifact());
    }
    mm.addArtifacts(WS, artifacts);
    assertEquals(10, mm.getArtifacts().size());
  }

  @Test
  public void testGetArtifactPropertyMap() {
    MetaModel mm = WS.createMetaModel();
    Collection<Artifact> artifacts = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Artifact artifact = WS.createArtifact();
      artifacts.add(artifact);
      for (int m = 0; m < 10; m++) {
        artifact.setPropertyValue(WS, "" + m, m);
      }
    }
    mm.addArtifacts(WS, artifacts);
    Map<Artifact, Map<String, Object>> artifactPropertyMap = mm.getArtifactPropertyMap();
    assertEquals(10, artifactPropertyMap.keySet().size());
    Iterator<Entry<Artifact, Map<String, Object>>> iterator = artifactPropertyMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<Artifact, Map<String, Object>> next = iterator.next();
      Artifact key = next.getKey();
      Map<String, Object> value = next.getValue();
      assertEquals(10, value.keySet().size());
      for (int m = 0; m < 10; m++) {
        assertEquals(m, value.get("" + m));
      }
    }
  }
}
