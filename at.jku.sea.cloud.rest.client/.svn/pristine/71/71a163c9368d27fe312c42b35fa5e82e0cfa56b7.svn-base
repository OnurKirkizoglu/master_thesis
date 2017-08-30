package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MapArtifact.Entry;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.client.RestMapArtifact.RestEntry;

public class TestMapArtifact {

  private static MapArtifact MAP;
  private static MapArtifact MAPWITHPACKAGE;
  private static Byte BYTE = 0x0;
  private static Short SHORT = 1;
  private static Integer INT = 2;
  private static Long LONG = 3L;
  private static Float FLOAT = 4.0f;
  private static Double DOUBLE = 5.0d;
  private static Character CHAR = '6';
  private static String STRING = "key";
  private static Boolean BOOLEAN = Boolean.TRUE;
  private static Artifact ARTIFACT;

  private static final long EMPTYSIZE = 0;

  @Before
  public void setUp() {
    ARTIFACT = WS.createArtifact();
    Package pkg = WS.createPackage();
    MAP = WS.createMap();
    MAPWITHPACKAGE = WS.createMap(pkg);
  }

  @After
  public void tearDown() {
    WS.rollbackAll();
  }

  @Test
  public void testSize() {
    assertEquals(EMPTYSIZE, MAP.size());
  }

  @Test
  public void testSizeWithPackage() {
    assertEquals(EMPTYSIZE, MAPWITHPACKAGE.size());
  }

  @Test
  public void testInputRangeAndContains() {
    MAP.put(WS, BYTE, BYTE);
    assertTrue(MAP.containsKey(BYTE));
    assertTrue(MAP.containsValue(BYTE));
    MAP.put(WS, SHORT, SHORT);
    assertTrue(MAP.containsKey(SHORT));
    assertTrue(MAP.containsValue(SHORT));
    MAP.put(WS, INT, INT);
    assertTrue(MAP.containsKey(INT));
    assertTrue(MAP.containsValue(INT));
    MAP.put(WS, LONG, LONG);
    assertTrue(MAP.containsKey(LONG));
    assertTrue(MAP.containsValue(LONG));
    MAP.put(WS, FLOAT, FLOAT);
    assertTrue(MAP.containsKey(FLOAT));
    assertTrue(MAP.containsValue(FLOAT));
    MAP.put(WS, DOUBLE, DOUBLE);
    assertTrue(MAP.containsKey(DOUBLE));
    assertTrue(MAP.containsValue(DOUBLE));
    MAP.put(WS, CHAR, CHAR);
    assertTrue(MAP.containsKey(CHAR));
    assertTrue(MAP.containsValue(CHAR));
    MAP.put(WS, STRING, STRING);
    assertTrue(MAP.containsKey(STRING));
    assertTrue(MAP.containsValue(STRING));
    MAP.put(WS, BOOLEAN, BOOLEAN);
    assertTrue(MAP.containsKey(BOOLEAN));
    assertTrue(MAP.containsValue(BOOLEAN));
    MAP.put(WS, ARTIFACT, ARTIFACT);
    assertTrue(MAP.containsKey(ARTIFACT));
    assertTrue(MAP.containsValue(ARTIFACT));
  }

  @Test
  public void testGet() {
    MAP.put(WS, BOOLEAN, ARTIFACT);
    Object obj = MAP.get(BOOLEAN);
    assertEquals(ARTIFACT, obj);
  }

  @Test
  public void testPut() {
    Object first = MAP.put(WS, SHORT, STRING);
    assertNull(first);
    Object second = MAP.put(WS, SHORT, CHAR);
    assertEquals(STRING, second);
  }

  @Test
  public void testRemove() {
    MAP.put(WS, DOUBLE, MAPWITHPACKAGE);
    assertTrue(MAP.containsKey(DOUBLE));
    assertTrue(MAP.containsValue(MAPWITHPACKAGE));
    assertEquals(EMPTYSIZE + 1, MAP.size());
    Object obj = MAP.remove(WS, DOUBLE);
    assertEquals(MAPWITHPACKAGE, obj);
    assertEquals(EMPTYSIZE, MAP.size());
    obj = MAP.remove(WS, ARTIFACT);
    assertNull(obj);
  }

  @Test
  public void testClear() {
    MAP.put(WS, BOOLEAN, ARTIFACT);
    MAP.put(WS, BYTE, LONG);
    assertEquals(EMPTYSIZE + 2, MAP.size());
    MAP.clear(WS);
    assertFalse(MAP.containsKey(BOOLEAN));
    assertFalse(MAP.containsValue(ARTIFACT));
    assertFalse(MAP.containsKey(BYTE));
    assertFalse(MAP.containsValue(LONG));
    assertEquals(EMPTYSIZE, MAP.size());
  }

  @Test
  public void testKeySet() {
    MAP.put(WS, LONG, null);
    MAP.put(WS, INT, null);
    MAP.put(WS, null, null);
    Set<Object> keys = MAP.keySet();
    Set<Object> expected = new HashSet<Object>(Arrays.asList(LONG, INT, null));
    assertEquals(expected, keys);
  }

  @Test
  public void testValues() {
    MAP.put(WS, SHORT, LONG);
    MAP.put(WS, LONG, LONG);
    MAP.put(WS, INT, INT);
    MAP.put(WS, null, null);
    Collection<Object> values = MAP.values();
    Collection<Object> expected = Arrays.<Object> asList(LONG, LONG, INT, null);
    assertEquals(expected.size(), values.size());
    assertTrue(expected.containsAll(values));
  }

  @Test
  public void testEntrySet() {
    MAP.put(WS, SHORT, LONG);
    MAP.put(WS, LONG, LONG);
    MAP.put(WS, INT, INT);
    MAP.put(WS, null, null);
    Set<MapArtifact.Entry> entries = MAP.entrySet();
    for (MapArtifact.Entry entry : entries) {
      assertTrue(MAP.containsKey(entry.getKey()));
      assertTrue(MAP.containsValue(entry.getValue()));
    }
  }

  @Test
  public void testEntryGetKeyVALUE() {
    MAP.put(WS, FLOAT, DOUBLE);
    Entry entry = MAP.entrySet().toArray(new RestEntry[Long.valueOf(MAP.size()).intValue()])[0];
    assertEquals(FLOAT, entry.getKey());
    assertEquals(DOUBLE, entry.getValue());
  }

  @Test(expected = PropertyDeadException.class)
  public void testPutPropertyDeadException() {
    MAP.put(WS, SHORT, LONG);
    MAP.put(WS, LONG, LONG);
    MAP.put(WS, INT, INT);
    MAP.put(WS, null, null);
    Set<Entry> entries = MAP.entrySet();
    RestEntry entry = (RestEntry) entries.toArray()[0];
    Class<?> entryClass = entry.getClass();
    try {
      Field idField = entryClass.getDeclaredField("entryId");
      idField.setAccessible(true);
      long id = idField.getLong(entry);
      idField.setAccessible(false);
      Artifact artifact = WS.getArtifact(id);
      artifact.deleteProperty(WS, "value");
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
    MAP.put(WS, entry.getKey(), ARTIFACT);
  }

  @Test(expected = TypeNotSupportedException.class)
  public void testPutTypeNotSupportedException() {
    MAP.put(WS, BOOLEAN, new ArrayList<Object>());
  }

  @Test(expected = PropertyDeadException.class)
  public void testSetEntryPropertyDeadException() {
    MAP.put(WS, SHORT, LONG);
    MAP.put(WS, LONG, LONG);
    MAP.put(WS, INT, INT);
    MAP.put(WS, null, null);
    Set<Entry> entries = MAP.entrySet();
    RestEntry entry = (RestEntry) entries.toArray()[1];
    Class<?> entryClass = entry.getClass();
    try {
      Field idField = entryClass.getDeclaredField("entryId");
      idField.setAccessible(true);
      long id = idField.getLong(entry);
      idField.setAccessible(false);
      Artifact artifact = WS.getArtifact(id);
      artifact.deleteProperty(WS, "value");
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
    entry.setValue(WS, null);
  }

  @Test(expected = TypeNotSupportedException.class)
  public void testSetEntryTypeNotSupportedException() {
    MAP.put(WS, SHORT, LONG);
    Set<Entry> entries = MAP.entrySet();
    RestEntry entry = (RestEntry) entries.toArray()[0];
    entry.setValue(WS, new ArrayList<Object>());
  }
}
