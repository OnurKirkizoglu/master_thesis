package at.jku.sea.cloud.javaobjectstorage.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.CloudFactory;
import at.jku.sea.cloud.javaobjectstorage.ObjectLoader;
import at.jku.sea.cloud.javaobjectstorage.ObjectSaver;
import at.jku.sea.cloud.javaobjectstorage.test.data.Child;
import at.jku.sea.cloud.javaobjectstorage.test.data.ComplexObject;
import at.jku.sea.cloud.javaobjectstorage.test.data.Node;

public class TestJavaObjectStorage {
  private static final Cloud CLOUD = CloudFactory.getInstance();
  private static Owner OWNER;
  private static Tool TOOL;
  private static Workspace WORKSPACE;

  private ObjectSaver saver;
  private ObjectLoader loader;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    OWNER = CLOUD.getOwner(DataStorage.ADMIN); //$NON-NLS-1$
    if (OWNER == null) {
      OWNER = CLOUD.createOwner();
    }
    TOOL = CLOUD.getTool(DataStorage.JUNIT_TOOL_ID);
    if (TOOL == null) {
      TOOL = CLOUD.createTool();
    }
    WORKSPACE = CLOUD.createWorkspace(OWNER, TOOL, "testWS");
  }

  @Before
  public void setUp() throws Exception {
    this.saver = new ObjectSaver();
    this.loader = new ObjectLoader();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    WORKSPACE.rollbackAll();
  }

  @Test
  public void testNull() throws ClassNotFoundException {
    final Object expected = null;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Object actual = this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testPrimitiveBoolean() throws ClassNotFoundException {
    final boolean expected = true;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final boolean actual = (boolean) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testObjectBoolean() throws ClassNotFoundException {
    final Boolean expected = true;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Boolean actual = (Boolean) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testPrimitiveByte() throws ClassNotFoundException {
    final byte expected = 5;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final byte actual = (byte) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testObjectByte() throws ClassNotFoundException {
    final Byte expected = 5;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Byte actual = (Byte) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testPrimitiveShort() throws ClassNotFoundException {
    final short expected = 5;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final short actual = (short) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testObjectShort() throws ClassNotFoundException {
    final Short expected = 5;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Short actual = (Short) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testPrimitiveInteger() throws ClassNotFoundException {
    final int expected = 5;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final int actual = (int) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testObjectInteger() throws ClassNotFoundException {
    final Integer expected = 5;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Integer actual = (Integer) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testPrimitiveLong() throws ClassNotFoundException {
    final long expected = 5;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final long actual = (long) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testObjectLong() throws ClassNotFoundException {
    final Long expected = 5L;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Long actual = (Long) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testPrimitiveCharacter() throws ClassNotFoundException {
    final char expected = 'f';
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final char actual = (char) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testObjectCharacter() throws ClassNotFoundException {
    final Character expected = 'f';
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Character actual = (Character) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testPrimitiveFloat() throws ClassNotFoundException {
    final float expected = 1.25F;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final float actual = (float) this.loader.loadObject(artifact);
    assertEquals(expected, actual, 0);
  }

  @Test
  public void testObjectFloat() throws ClassNotFoundException {
    final Float expected = 1.25F;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Float actual = (Float) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testPrimitiveDouble() throws ClassNotFoundException {
    final double expected = 1.25;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final double actual = (double) this.loader.loadObject(artifact);
    assertEquals(expected, actual, 0);
  }

  @Test
  public void testObjectDouble() throws ClassNotFoundException {
    final Double expected = 1.25;
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Double actual = (Double) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testString() throws ClassNotFoundException {
    final String expected = "asdf"; //$NON-NLS-1$
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final String actual = (String) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testComplexObject() throws ClassNotFoundException {
    final ComplexObject expected = new ComplexObject(1, 2);
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final ComplexObject actual = (ComplexObject) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testPrimitiveArray() throws ClassNotFoundException {
    final int[] expecteds = { 1, 2, 3, 4, 5 };
    final Artifact artifact = this.saver.saveObject(expecteds, TestJavaObjectStorage.WORKSPACE);
    final int[] actuals = (int[]) this.loader.loadObject(artifact);
    assertArrayEquals(expecteds, actuals);
  }

  @Test
  public void testPrimitiveObjectArray() throws ClassNotFoundException {
    final Integer[] expecteds = { 1, 2, 3, 4, 5 };
    final Artifact artifact = this.saver.saveObject(expecteds, TestJavaObjectStorage.WORKSPACE);
    final Integer[] actuals = (Integer[]) this.loader.loadObject(artifact);
    assertArrayEquals(expecteds, actuals);
  }

  @Test
  public void testStringArray() throws ClassNotFoundException {
    final String[] expecteds = { "qwerty", "asdf", "x", "y", "z" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    final Artifact artifact = this.saver.saveObject(expecteds, TestJavaObjectStorage.WORKSPACE);
    final String[] actuals = (String[]) this.loader.loadObject(artifact);
    assertArrayEquals(expecteds, actuals);
  }

  @Test
  public void testComplexObjectArray() throws ClassNotFoundException {
    final ComplexObject[] expecteds = { new ComplexObject(1, 2), new ComplexObject(3, 4), new ComplexObject(5, 6) };
    final Artifact artifact = this.saver.saveObject(expecteds, TestJavaObjectStorage.WORKSPACE);
    final ComplexObject[] actuals = (ComplexObject[]) this.loader.loadObject(artifact);
    assertArrayEquals(expecteds, actuals);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPrimitiveObjectCollection() throws ClassNotFoundException {
    final Collection<Integer> expected = Arrays.asList(1, 2, 3);
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Collection<Integer> actual = (Collection<Integer>) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testComplexObjectCollection() throws ClassNotFoundException {
    final Collection<ComplexObject> expected = Arrays.asList(new ComplexObject(1, 2), new ComplexObject(3, 4), new ComplexObject(5, 6));
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Collection<ComplexObject> actual = (Collection<ComplexObject>) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testPrimitiveObjectMap() throws ClassNotFoundException {
    final Map<Integer, Character> expected = new HashMap<Integer, Character>();
    expected.put(1, 'a');
    expected.put(2, 'b');
    expected.put(3, 'c');
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Map<Integer, Character> actual = (Map<Integer, Character>) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testComplexObjectMap() throws ClassNotFoundException {
    final Map<Integer, ComplexObject> expected = new HashMap<Integer, ComplexObject>();
    expected.put(1, new ComplexObject(1, 1));
    expected.put(2, new ComplexObject(2, 2));
    expected.put(3, new ComplexObject(3, 3));
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Map<Integer, ComplexObject> actual = (Map<Integer, ComplexObject>) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testCyclicData() throws ClassNotFoundException {
    final Node child1 = new Node(2, null);
    final Node child2 = new Node(3, null);
    final Node expected = new Node(1, null, child1, child2);
    child1.setParent(expected);
    child2.setParent(expected);
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Node actual = (Node) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }

  @Test
  public void testOverriddenFields() throws ClassNotFoundException {
    final Child expected = new Child(1.5F, "test"); //$NON-NLS-1$
    final Artifact artifact = this.saver.saveObject(expected, TestJavaObjectStorage.WORKSPACE);
    final Child actual = (Child) this.loader.loadObject(artifact);
    assertEquals(expected, actual);
  }
}
