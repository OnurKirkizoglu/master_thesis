package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.implementation.CloudFactory;

public class TestInitialization {
  private static final Cloud cloud = CloudFactory.getInstance();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  /*
   * If something went wrong here, some serious shit has happened
   */
  @Test
  public void testMMM() {
    MetaModel mmm = cloud.getMetaModel(DataStorage.FIRST_VERSION, DataStorage.MMM_META_MODEL);
    Collection<Artifact> artifacts = mmm.getArtifacts();
    assertEquals(14, artifacts.size());
    assertEquals(14, (long) ((CollectionArtifact) mmm).size());
  }

}
