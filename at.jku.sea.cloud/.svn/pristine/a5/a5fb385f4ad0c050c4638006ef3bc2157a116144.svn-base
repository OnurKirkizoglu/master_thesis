package at.jku.sea.cloud.navigator.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.implementation.CloudFactory;
import at.jku.sea.cloud.navigator.NavigatorProvider;
import at.jku.sea.cloud.navigator.TerminalManeuverable;
import at.jku.sea.cloud.stream.Context;
import at.jku.sea.cloud.stream.QueryFactory;

public class NavigatorsTest {

  private static final int NESTED_DEPTH = 10;

  private static final String WRONG_PATH = "WRONG_PATH";

  private static final String PATH = "path";
  private static final String MAP_PATH = "mappath";
  private static final String METAMODEL_PATH = "mmpath";
  private static final String PACKAGE_PATH = "pckgpath";
  private static final String PROJECT_PATH = "projectpath";
  private static final String NUMBER_PATH = "numberpath";
  private static final String CHARACTER_PATH = "charpath";
  private static final String STRING_PATH = "strpath";

  private static final Long TEST_NUMBER = 100L;
  private static final Character TEST_CHARACTER = 'C';
  private static final String TEST_STRING = "TEST";

  private static Workspace WS;
  private static Artifact root;
  private static Artifact[] nested;
  private static MetaModel mm;
  private static Package pckg;
  private static Project project;
  private static MapArtifact map;

  private static Tool tool;
  private static Owner owner;
  private static QueryFactory queryFactory;
  private static NavigatorProvider navigators;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    Cloud c = CloudFactory.getInstance();
    queryFactory = c.queryFactory();
    navigators = queryFactory.navigatorProvider();
    WS = c.createWorkspace(owner = c.createOwner(), tool = c.createTool(), "NavigatorUnitTest");

    root = WS.createArtifact();
    nested = new Artifact[NESTED_DEPTH];
    Artifact cur = root;
    for (int i = 0; i < nested.length; i++) {
      Artifact a = WS.createArtifact();
      cur.setPropertyValue(WS, PATH, a);
      cur = a;
      nested[i] = cur;
    }
    root.setPropertyValue(WS, MAP_PATH, map = WS.createMap());
    root.setPropertyValue(WS, METAMODEL_PATH, mm = WS.createMetaModel());
    root.setPropertyValue(WS, PACKAGE_PATH, pckg = WS.createPackage());
    root.setPropertyValue(WS, PROJECT_PATH, project = WS.createProject());
    root.setPropertyValue(WS, NUMBER_PATH, TEST_NUMBER);
    root.setPropertyValue(WS, CHARACTER_PATH, TEST_CHARACTER);
    root.setPropertyValue(WS, STRING_PATH, TEST_STRING);

  }

  @Test
  public void testGetRoot() {
    assertEquals(root, navigators.from(root).get());
  }

  @Test(expected = NullPointerException.class)
  public void testGetNull() {
    navigators.from((Context.Path) null).get();
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testWrongPath() {
    navigators.from(root).to(WRONG_PATH).get();
  }

  @Test
  public void testPathInNested() {
    TerminalManeuverable<Artifact> cur = navigators.from(root);
    for (int i = 0; i < NESTED_DEPTH; i++) {
      cur = cur.to(PATH);
      assertEquals(nested[i], cur.get());
    }
  }

  @Test
  public void testGetOwner() {
    assertEquals(owner.getId(), navigators.from(root).toOwner().get().getId());
  }

  @Test
  public void testGetTool() {
    assertEquals(tool.getId(), navigators.from(root).toTool().get().getId());
  }

  @Test
  public void testGetMap() {
    assertEquals(map, navigators.from(root).toMap(MAP_PATH).get());
  }

  @Test
  public void testGetMetaModel() {
    assertEquals(mm, navigators.from(root).toMetaModel(METAMODEL_PATH).get());
  }

  @Test
  public void testGetPackage() {
    assertEquals(pckg, navigators.from(root).toContainer(PACKAGE_PATH).get());
  }

  @Test
  public void testGetProject() {
    assertEquals(project, navigators.from(root).toProject(PROJECT_PATH).get());
  }

}
