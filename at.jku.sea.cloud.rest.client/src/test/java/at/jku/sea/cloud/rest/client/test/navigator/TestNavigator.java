package at.jku.sea.cloud.rest.client.test.navigator;

import static at.jku.sea.cloud.rest.client.test.TestAll.CLOUD;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.navigator.NavigatorProvider;
import at.jku.sea.cloud.stream.Contexts;

/**
 * @author Florian Weger
 */
public class TestNavigator {

  private static Artifact ARTIFACT;
  private static CollectionArtifact COLLECTION;
  private static Container CONTAINER;
  private static Project PROJECT;
  private static MapArtifact MAP;
  private static MetaModel META_MODEL;

  private static String LONG_KEY = "L";
  private static Long LONG = 1L;

  private static String STRING_KEY = "S";
  private static String STRING = "STRING";

  private static String CHAR_KEY = "C";
  private static Character CHARACTER = 'C';

  private static String NULL_KEY = "N";
  private static Object NULL = null;

  private static String COLLECTION_PATH = "COLLECTION";
  private static String CONTAINER_PATH = "CONTAINER";
  private static String PROJECT_PATH = "PROJECT";
  private static String MAP_PATH = "MAP";
  private static String META_MODEL_PATH = "METAMODEL";
  private static NavigatorProvider navigators;

  @BeforeClass
  public static void setUpBeforeClass() {
    navigators = CLOUD.queryFactory().navigatorProvider();
    ARTIFACT = WS.createArtifact();
    COLLECTION = WS.createCollection(true);
    CONTAINER = WS.createPackage();
    PROJECT = WS.createProject();
    MAP = WS.createMap();
    META_MODEL = WS.createMetaModel();

    ARTIFACT.setPropertyValue(WS, LONG_KEY, LONG);
    ARTIFACT.setPropertyValue(WS, STRING_KEY, STRING);
    ARTIFACT.setPropertyValue(WS, CHAR_KEY, CHARACTER);
    ARTIFACT.setPropertyValue(WS, NULL_KEY, NULL);

    ARTIFACT.setPropertyValue(WS, COLLECTION_PATH, COLLECTION);
    COLLECTION.setPropertyValue(WS, CONTAINER_PATH, CONTAINER);
    CONTAINER.setPropertyValue(WS, PROJECT_PATH, PROJECT);
    PROJECT.setPropertyValue(WS, MAP_PATH, MAP);
    MAP.setPropertyValue(WS, META_MODEL_PATH, META_MODEL);
  }

  @AfterClass
  public static void tearDown() {
    WS.rollbackAll();
  }

  @Test
  public void testTo() {
    Artifact result = navigators.from(ARTIFACT).to(COLLECTION_PATH).get();
    assertEquals(COLLECTION, result);
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testToException() {
    navigators.from(ARTIFACT).to(PROJECT_PATH).get();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testToException2() {
    navigators.from(Contexts.of("e")).get(Contexts.empty().put("e", new Object()));
  }

  @Test
  public void testToCollection() {
    CollectionArtifact result = navigators.from(ARTIFACT).toCollection(COLLECTION_PATH).get();
    assertEquals(COLLECTION, result);
  }

  @Test
  public void testToMap() {
    MapArtifact result = navigators.from(PROJECT).toMap(MAP_PATH).get();
    assertEquals(MAP, result);
  }

  @Test
  public void testToMetaModel() {
    MetaModel result = navigators.from(MAP).toMetaModel(META_MODEL_PATH).get();
    assertEquals(META_MODEL, result);
  }

  @Test
  public void testToProject() {
    Project result = navigators.from(CONTAINER).toProject(PROJECT_PATH).get();
    assertEquals(PROJECT, result);
  }

  @Test
  public void testToContainer() {
    Container result = navigators.from(COLLECTION).toContainer(CONTAINER_PATH).get();
    assertEquals(CONTAINER, result);
  }

  @Test
  public void testToOwner() {
    Owner result = navigators.from(ARTIFACT).toOwner().get();
    assertEquals(ARTIFACT.getOwner(), result);
  }

  @Test
  public void testToTool() {
    Tool result = navigators.from(ARTIFACT).toTool().get();
    assertEquals(ARTIFACT.getTool(), result);
  }

  @Test
  public void testToNumber() {
    Number result = navigators.from(ARTIFACT).toNumber(LONG_KEY).get();
    assertEquals(LONG, result);
  }

  @Test
  public void testToCharacter() {
    Character result = navigators.from(ARTIFACT).toCharacter(CHAR_KEY).get();
    assertEquals(CHARACTER, result);
  }

  @Test
  public void testToString() {
    String result = navigators.from(ARTIFACT).toString(STRING_KEY).get();
    assertEquals(STRING, result);
  }
}
