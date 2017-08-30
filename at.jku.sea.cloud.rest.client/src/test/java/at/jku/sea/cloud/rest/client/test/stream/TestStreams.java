package at.jku.sea.cloud.rest.client.test.stream;

import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.implementation.CloudFactory;
import at.jku.sea.cloud.navigator.NavigatorProvider;
import at.jku.sea.cloud.stream.Context;
import at.jku.sea.cloud.stream.Contexts;
import at.jku.sea.cloud.stream.QueryFactory;
import at.jku.sea.cloud.stream.StreamProvider;
import at.jku.sea.cloud.stream.predicate.PredicateProvider;

public class TestStreams {

  private static final String CONTEXT_PATH = "PATH";
  private static final String FAIL_CONTEXT_PATH = "FAILPATH";
  private static final int COLLECTION_LENGTH = 100;
  private static final Long DUMMY_ELEMENT = 1L;
  private static final long SINGLE_ELEMENT = 1L;

  private static Artifact dummyArtifact;
  private static List<Long> longList;
  private static String[] stringArray;
  private static CollectionArtifact onlyArtifact;
  private static CollectionArtifact objectCollection;
  private static Container pckg;
  private static Container resource;
  private static MapArtifact mapArtifact;
  private static MetaModel metaModel;
  private static Project project;

  private static UUID uuid;

  private static StreamProvider streams;
  private static NavigatorProvider navigators;
  private static PredicateProvider predicates;

  @BeforeClass
  public static void beforeClass() {
    Cloud c = CloudFactory.getInstance();
    QueryFactory queryFactory = c.queryFactory();
    streams = queryFactory.streamProvider();
    navigators = queryFactory.navigatorProvider();
    predicates = queryFactory.predicateProvider();
    WS = c.createWorkspace(c.createOwner(), c.createTool(), "StreamUnitTest");

    dummyArtifact = WS.createArtifact();

    // CollectionArtifact
    objectCollection = WS.createCollection(false);
    onlyArtifact = WS.createCollection(true);

    onlyArtifact.addElement(WS, dummyArtifact);
    objectCollection.addElement(WS, DUMMY_ELEMENT);

    // Container
    pckg = WS.createPackage();
    uuid = UUID.randomUUID();
    resource = WS.createResource(uuid.toString());
    resource.addArtifact(WS, dummyArtifact);

    // MapArtifact
    mapArtifact = WS.createMap();
    mapArtifact.put(WS, DUMMY_ELEMENT, dummyArtifact);

    // MetaModel
    metaModel = WS.createMetaModel();
    metaModel.addArtifact(WS, dummyArtifact);

    // Project
    project = WS.createProject();
    project.addArtifact(WS, dummyArtifact);

    // Collection + array
    Random random = new Random();
    longList = new ArrayList<>(COLLECTION_LENGTH);
    stringArray = new String[COLLECTION_LENGTH];
    for (int i = 0; i < COLLECTION_LENGTH; i++) {
      Long l = random.nextLong();
      longList.add(l);
      stringArray[i] = l.toString();
      Artifact a = WS.createArtifact();
      a.setPropertyValue(WS, CONTEXT_PATH, l);
      pckg.addArtifact(WS, a);
    }
  }

  // Collection + array tests

  @Test
  public void testArrayWrapperAndList() {
    List<String> strings = streams.of(stringArray).toList();
    assertEquals(strings.size(), stringArray.length);
    assertArrayEquals(stringArray, strings.toArray(new String[strings.size()]));
  }

  @Test
  public void testCollectionWrapperAndList() {
    List<Long> longs = streams.of(longList).toList();
    assertEquals(longs.size(), longList.size());
    assertArrayEquals(longList.toArray(new Long[longList.size()]), longs.toArray(new Long[longs.size()]));
  }

  @Test(expected = TypeNotSupportedException.class)
  public void testCollectionWithNotSupportedType() {
    List<List<Integer>> listOfList = new ArrayList<>(Arrays.asList(Arrays.asList(1)));
    streams.of(listOfList).toList();
  }

  @Test
  public void testFilter() {
    Long element = longList.get(longList.size() >> 1);
    List<Long> elements = streams.of(longList).filter("i", predicates.eq(Contexts.of("i"), element)).toList();
    assertEquals(SINGLE_ELEMENT, elements.size());
    assertEquals(element, elements.get(0));
  }

  @Test
  public void testFind() {
    Long element = longList.get(longList.size() >> 1);
    Long found = streams.of(longList).find("i", predicates.eq(Contexts.of("i"), element));
    assertEquals(element, found);
  }

  @Test(expected = NoSuchElementException.class)
  public void testFindError() {
    streams.of(stringArray).find("i", predicates.eq(Contexts.of("i"), "TEST"));
  }

  @Test
  public void testCount() {
    long count = streams.of(stringArray).count();
    assertEquals(COLLECTION_LENGTH, count);
  }

  @Test
  public void testMapping() {
    List<Number> list = streams.of(pckg).map("c", navigators.from(Contexts.of("c")).toNumber(CONTEXT_PATH)).toList();
    assertEquals(new HashSet<>(longList), new HashSet<>(list));
  }

  @Test(expected = PropertyDoesNotExistException.class)
  public void testFailMapping() {
    streams.of(pckg).map("c", navigators.from(Contexts.of("c")).toNumber(FAIL_CONTEXT_PATH)).toList();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testContextFail() {
    streams.of(pckg).map("c", navigators.from(Contexts.of("a")).toNumber(CONTEXT_PATH)).toList();
  }

  @Test
  public void testContextPath() {
    Context.Path path = Contexts.of(CONTEXT_PATH);
    assertEquals(path.get(), CONTEXT_PATH);
  }

  @Test
  public void testOnlyArtifact() {
    long resultSize = streams.onlyArtifacts(onlyArtifact).count();
    List<Artifact> arts = streams.onlyArtifacts(onlyArtifact).toList();
    assertEquals(SINGLE_ELEMENT, resultSize);
    assertEquals(Long.valueOf(arts.size()), onlyArtifact.size());
    assertEquals(dummyArtifact, arts.get(0));
  }

  @Test
  public void testObjectArtifact() {
    long resultSize = streams.onlyArtifacts(objectCollection).count();
    List<Artifact> ojbs = streams.onlyArtifacts(objectCollection).toList();
    assertEquals(SINGLE_ELEMENT, resultSize);
    assertEquals(Long.valueOf(ojbs.size()), onlyArtifact.size());
    assertEquals(DUMMY_ELEMENT, ojbs.get(0));
  }

  @Test
  public void testContainer() {
    long count = streams.of(resource).count();
    List<Artifact> list = streams.of(resource).toList();
    assertEquals(SINGLE_ELEMENT, count);
    assertEquals(list.size(), resource.getArtifacts().size());
    assertEquals(dummyArtifact, list.get(0));
  }

  @Test
  public void testMapArtifactKey() {
    List<Object> list = streams.keys(mapArtifact).toList();
    assertEquals(SINGLE_ELEMENT, list.size());
    assertEquals(DUMMY_ELEMENT, list.get(0));
  }

  @Test
  public void testMapArtifactValue() {
    List<Object> list = streams.values(mapArtifact).toList();
    assertEquals(SINGLE_ELEMENT, list.size());
    assertEquals(dummyArtifact, list.get(0));
  }

  @Test
  public void testMetaModel() {
    List<Artifact> list = streams.of(metaModel).toList();
    assertEquals(SINGLE_ELEMENT, list.size());
    assertEquals(dummyArtifact, metaModel.getArtifacts().iterator().next());
  }

  @Test
  public void testProject() {
    List<Artifact> list = streams.of(project).toList();
    assertEquals(SINGLE_ELEMENT, list.size());
    assertEquals(dummyArtifact, project.getArtifacts().iterator().next());
  }

  @Test(expected = NullPointerException.class)
  public void testSupplierNull() {
    streams.of((CollectionArtifact) null);
  }

  @Test(expected = NullPointerException.class)
  public void testContextNull() {
    streams.of(project).filter(null, predicates.hasProperty(Contexts.of("e"), CONTEXT_PATH));
  }

  @Test(expected = NullPointerException.class)
  public void testPredicateNull() {
    streams.of(project).filter("e", null);
  }

}
