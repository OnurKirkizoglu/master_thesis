package at.jku.sea.cloud.tests;

import static org.junit.Assert.assertEquals;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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

public class CloudTestCases {
  // @Rule
  // public ExpectedException exception = ExpectedException.none();

  private static Workspace workspace;
  private static Cloud cloud;

  @BeforeClass
  public static void setUp() {

  }

  @Before
  public void before() {
    cloud = CloudFactory.getInstance();

    // final TestListener listener = new TestListener();
    // cloud.addDatastorageListener(listener);

    final Owner owner = cloud.getOwner(DataStorage.ADMIN);
    final Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    workspace = cloud.createWorkspace(owner, tool, "");
  }

  @Test
  // (expected = PropertyDoesNotExistException.class)
  public void testCollectionAddExists() {

    final CollectionArtifact coll = CloudTestCases.workspace.createCollection(false);
    coll.addElement(workspace, "bla");
    coll.addElement(workspace, "test1");
    assertEquals(true, coll.existsElement("test1"));
    assertEquals(false, coll.existsElement("test2"));
  }

  @Test
  public void testCollectionGet() {
    final CollectionArtifact coll = CloudTestCases.workspace.createCollection(false);
    coll.addElement(workspace, "bla");
    coll.addElement(workspace, "test1");
    final Collection<?> collection = coll.getElements();
    assertEquals(2, collection.size());
    assertEquals("bla", collection.toArray()[0]);
    assertEquals("test1", collection.toArray()[1]);

  }

  @Test
  public void testCollectionRemove() {
    final CollectionArtifact coll = CloudTestCases.workspace.createCollection(false);
    coll.addElement(workspace, "bla");
    assertEquals(true, coll.existsElement("bla"));
    coll.removeElement(workspace, "bla");
    assertEquals(false, coll.existsElement("bla"));
  }

  @Test(expected = InvalidParameterException.class)
  public void testCollectionUnordered() {
    final CollectionArtifact coll = CloudTestCases.workspace.createCollection(false);
    coll.addElement(workspace, "bla");

    coll.getElementAt(0);
    coll.removeElementAt(workspace, 0);
    coll.insertElementAt(workspace, "test", 2);
  }

  @Test
  public void testOrderedCollectionAddRemove() {
    final CollectionArtifact coll = CloudTestCases.workspace.createCollection(false);
    System.err.println("testOrderedCollectionAddRemove");
    coll.addElement(workspace, "test");
    coll.addElement(workspace, "test1");
    coll.addElement(workspace, "test3");
    coll.removeElementAt(workspace, 1);
    coll.removeElementAt(workspace, 2);
    coll.addElement(workspace, "test4");
    final Collection<?> returnCol = coll.getElements();
    assertEquals(2, returnCol.size());
    assertEquals("test", coll.getElementAt(0));
    assertEquals("test4", coll.getElementAt(1));
    System.err.println(coll.getElements().toArray()[1]);
    for (final Object object : returnCol) {
      System.err.println(object);
    }
  }

  @Test
  public void testOrderedCollectionExists() {
    final CollectionArtifact coll = CloudTestCases.workspace.createCollection(false);
    coll.addElement(workspace, "test");
    coll.addElement(workspace, "test1");
    coll.addElement(workspace, "test3");
    coll.removeElementAt(workspace, 1);
    coll.removeElementAt(workspace, 2);
    coll.addElement(workspace, "test4");
  }

  @Test(expected = InvalidParameterException.class)
  public void testUnorderedException1() {
    final CollectionArtifact coll = CloudTestCases.workspace.createCollection(false);
    coll.addElement(workspace, "test");
    coll.addElement(workspace, "test");
    coll.getElementAt(1);

  }

  @Test(expected = InvalidParameterException.class)
  public void testUnorderedException2() {
    final CollectionArtifact coll = CloudTestCases.workspace.createCollection(false);
    coll.addElement(workspace, "test");
    coll.addElement(workspace, "test");
    coll.removeElementAt(workspace, 0);

  }

  @Test
  public void testArtifact() {
    final Artifact type = workspace.createArtifact();
    final Collection<Artifact> artifacts = workspace.getArtifacts();
    int found = 0;
    for (final Artifact artifact : artifacts) {
      if (artifact.getId() == (type.getId())) {
        found++;
      }
    }
    assertEquals(1, found);

    Package namespaceMetaMetaModel;
    namespaceMetaMetaModel = workspace.createPackage();
    namespaceMetaMetaModel.setPropertyValue(workspace, "name", "http://sea.jku.at/test/mmm");
    type.setPackage(workspace, namespaceMetaMetaModel);

    type.setPropertyValue(workspace, "name", "Type");
    type.setPropertyValue(workspace, "type", type);
    type.setPropertyValue(workspace, "superType", type);
    type.setPropertyValue(workspace, "genericTypes", Collection.class);
    type.setPropertyValue(workspace, "boundTypes", Map.class);
    final Collection<String> allProperties = type.getAllPropertyNames();
    for (final String string : allProperties) {
      System.err.println("\"" + string + "\"");
    }
    assertEquals("Type", type.getPropertyValue("name"));
    assertEquals(type.getId(), ((Artifact) type.getPropertyValue("type")).getId());

    assertEquals(Collection.class, type.getPropertyValue("genericTypes"));

    assertEquals(Map.class, type.getPropertyValue("boundTypes"));

    assertEquals(namespaceMetaMetaModel.getId(), type.getPackage().getId());

    assertEquals(true, allProperties.contains("name"));
    assertEquals(true, allProperties.contains("type"));
    assertEquals(true, allProperties.contains("superType"));
    assertEquals(true, allProperties.contains("genericTypes"));
    assertEquals(true, allProperties.contains("boundTypes"));

    final int size = workspace.getArtifacts().size();
    workspace.rollbackArtifact(type);
    assertEquals(size - 1, workspace.getArtifacts().size());

    // long commitArtifact = workspace.commitArtifact(type);

  }

  @Test
  public void testBig() {
    Package namespaceMetaMetaModel;
    namespaceMetaMetaModel = workspace.createPackage();

    namespaceMetaMetaModel.setPropertyValue(workspace, "name", "http://sea.jku.at/test/mmm");
    final Artifact type = workspace.createArtifact();
    type.setPackage(workspace, namespaceMetaMetaModel);
    type.setPropertyValue(workspace, "name", "Type");
    type.setPropertyValue(workspace, "type", type);
    type.setPropertyValue(workspace, "superType", type);
    type.setPropertyValue(workspace, "genericTypes", Collection.class);
    type.setPropertyValue(workspace, "boundTypes", Map.class);
    final Artifact reference = workspace.createArtifact();
    reference.setPackage(workspace, namespaceMetaMetaModel);
    reference.setPropertyValue(workspace, "name", "Reference");
    reference.setPropertyValue(workspace, "owner", type);
    reference.setPropertyValue(workspace, "type", type);
    reference.setPropertyValue(workspace, "lowerBound", int.class);
    reference.setPropertyValue(workspace, "upperBound", int.class);
    final Artifact attribute = workspace.createArtifact();
    attribute.setPackage(workspace, namespaceMetaMetaModel);
    attribute.setPropertyValue(workspace, "name", "Attribute");
    attribute.setPropertyValue(workspace, "owner", type);
    attribute.setPropertyValue(workspace, "attributeType", Class.class);
    final Artifact operation = workspace.createArtifact();
    operation.setPackage(workspace, namespaceMetaMetaModel);
    operation.setPropertyValue(workspace, "name", "Operation");
    operation.setPropertyValue(workspace, "owner", type);
    operation.setPropertyValue(workspace, "identifier", String.class);
    operation.setPropertyValue(workspace, "parameters", List.class);
    operation.setPropertyValue(workspace, "returnType", Class.class);
    final Artifact constraint = workspace.createArtifact();
    constraint.setPackage(workspace, namespaceMetaMetaModel);
    constraint.setPropertyValue(workspace, "name", "Constraint");
    constraint.setPropertyValue(workspace, "type", type);
    constraint.setPropertyValue(workspace, "context", type);
    constraint.setPropertyValue(workspace, "rule", String.class);
    constraint.setPropertyValue(workspace, "enabled", boolean.class);
    constraint.setPropertyValue(workspace, "repairable", boolean.class);
    final Artifact typeConstraint = workspace.createArtifact();
    typeConstraint.setPackage(workspace, namespaceMetaMetaModel);
    typeConstraint.setPropertyValue(workspace, "name", "TypeConstraint");
    typeConstraint.setPropertyValue(workspace, "type", constraint);
    typeConstraint.setPropertyValue(workspace, "context", type);
    typeConstraint.setPropertyValue(workspace, "rule", "self.type <> null");
    typeConstraint.setPropertyValue(workspace, "enabled", true);
    typeConstraint.setPropertyValue(workspace, "repairable", false);

    final Package namespaceMetaModel = workspace.createPackage();
    namespaceMetaModel.setPropertyValue(workspace, "name", "http://sea.jku.at/test/mm");
    namespaceMetaModel.setPackage(workspace, namespaceMetaMetaModel);
    final Artifact data = workspace.createArtifact();
    data.setPackage(workspace, namespaceMetaModel);
    data.setPropertyValue(workspace, "name", "Data");
    data.setPropertyValue(workspace, "type", type);
    data.setPropertyValue(workspace, "genericTypes", Arrays.asList("ValueType"));
    final Artifact dataValue = workspace.createArtifact();
    dataValue.setPackage(workspace, namespaceMetaModel);
    dataValue.setPropertyValue(workspace, "name", "value");
    dataValue.setPropertyValue(workspace, "type", attribute);
    dataValue.setPropertyValue(workspace, "owner", data);
    dataValue.setPropertyValue(workspace, "attributeType", "ValueType");
    final Artifact quantifiedData = workspace.createArtifact();
    quantifiedData.setPackage(workspace, namespaceMetaModel);
    quantifiedData.setPropertyValue(workspace, "name", "QuantifiedData");
    quantifiedData.setPropertyValue(workspace, "type", type);
    quantifiedData.setPropertyValue(workspace, "superType", data);
    final Map<String, Class<?>> boundTypes = new HashMap<String, Class<?>>();
    boundTypes.put("ValueType", Double.class);
    quantifiedData.setPropertyValue(workspace, "boundTypes", boundTypes);
    final Artifact quantifiedDataNominalValueOperation = workspace.createArtifact();
    quantifiedDataNominalValueOperation.setPackage(workspace, namespaceMetaModel);
    quantifiedDataNominalValueOperation.setPropertyValue(workspace, "type", operation);
    quantifiedDataNominalValueOperation.setPropertyValue(workspace, "owner", data);
    quantifiedDataNominalValueOperation.setPropertyValue(workspace, "identifier", "nominalValue");
    quantifiedDataNominalValueOperation.setPropertyValue(workspace, "parameters", Collections.EMPTY_LIST);
    quantifiedDataNominalValueOperation.setPropertyValue(workspace, "returnType", int.class);

    final Package namespaceModel = workspace.createPackage();
    namespaceModel.setPropertyValue(workspace, "name", "http://sea.jku.at/test/m");
    namespaceModel.setPackage(workspace, namespaceMetaModel);
  }

  @Test
  public void testCommitedArtifact() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    if (owner == null) {
      owner = cloud.createOwner();
    }
    // final Workspace workspace = cloud.getWorkspace(owner, "CloudTest");
    final Collection<Artifact> artifacts = cloud.getArtifacts(cloud.getHeadVersionNumber());
    // Collection<Artifact> artifacts =workspace.getArtifacts();
    for (final Artifact artifact : artifacts) {
      if ((artifact.getVersionNumber() == 8) && (artifact.getId() == 16)) {
        artifact.getPropertyValue("name");
      }
      if (artifact.isAlive()) {
        System.out.println(artifact.getId());
      }
    }
  }

  @Test
  public void testProps() {
    // TEST: java.lang.Integer cannot be cast to [Ljava.lang.Object;

    final Artifact arti = cloud.getArtifact(DataStorage.FIRST_VERSION, DataStorage.ADMIN);
    for (final String prop : arti.getAlivePropertyNames()) {
      final Object propertyValue = arti.getPropertyValue(prop);
      System.out.println(propertyValue);
    }

  }

  @After
  public void after() {
    System.out.println("rollback workspace");
    CloudTestCases.workspace.rollbackAll();
    CloudTestCases.workspace.close();
  }
}
