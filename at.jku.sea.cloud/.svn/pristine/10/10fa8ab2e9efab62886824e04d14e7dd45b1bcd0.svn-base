package at.jku.sea.cloud.implementation;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static at.jku.sea.cloud.DataStorage.*;
import static org.junit.Assert.*;
import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.sql.SQLDataStorage;

public class TestPerformance {
  private DataStorage dataStorage;
  private Cloud cloud;

  @Before
  public void before() {
    dataStorage = new SQLDataStorage();
    cloud = new DefaultCloud(dataStorage);
  }

  @After
  public void after() {
    dataStorage.truncateAll();
    cloud = null;
  }

  @Test
  public void testArtifactFactory() {
    long start = System.nanoTime();
    ArtifactFactory.getArtifact(dataStorage, FIRST_VERSION, COMPLEX_TYPE);
    System.out.println("ArtifactFactory.getArtifact " + (System.nanoTime() - start) / Math.pow(10, 6));
    start = System.nanoTime();
    ArtifactFactory.getArtifact(dataStorage, FIRST_VERSION, COMPLEX_TYPE);
    System.out.println("ArtifactFactory.getArtifact " + (System.nanoTime() - start) / Math.pow(10, 6));
  }

  private SecureRandom random = new SecureRandom();

  public String randomString() {
    return new BigInteger(130, random).toString(32);
  }

  @Test
  public void testGetArtifacts() {
    long start = System.nanoTime();
    cloud.getArtifacts(FIRST_VERSION);
    System.out.println("getArtifacts: " + (System.nanoTime() - start) / Math.pow(10, 6));

  }

  @Test
  public void testCompleteWorkspaceHiearchy() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    Workspace p = cloud.createWorkspace(owner, tool, "");
    WorkspaceHierarchyUtils workspaceUtils = new WorkspaceHierarchyUtils();
    Workspace p1 = p;
    for (int i = 0; i < 50; i++) {
      p = cloud.createWorkspace(owner, tool, "" + i, p, PropagationType.instant, PropagationType.instant);
    }
    long start = System.nanoTime();
    Set<Object[]> hierarchy = workspaceUtils.collectCompleteWorkspaceHierarchy((AbstractDataStorage) dataStorage, p.getId());
    System.out.println("hierarchy bottom : " + (System.nanoTime() - start) / Math.pow(10, 6));
    assertEquals(51, hierarchy.size());
    start = System.nanoTime();
    hierarchy = workspaceUtils.collectCompleteWorkspaceHierarchy((AbstractDataStorage) dataStorage, p1.getId());
    System.out.println("hierarchy top : " + (System.nanoTime() - start) / Math.pow(10, 6));
    assertEquals(51, hierarchy.size());

  }

  @Test
  public void testWriteMultipleValues() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    Workspace p = cloud.createWorkspace(owner, tool, "");
    Artifact artifact = p.createArtifact();
    Collection<String> properties = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      String property = randomString();
      properties.add(property);
    }
    long start = System.nanoTime();
    for (String property : properties) {
      artifact.setPropertyValue(p, property, 1);
    }
    System.out.println("insert: " + (System.nanoTime() - start) / Math.pow(10, 6));
    start = System.nanoTime();
    for (String property : properties) {
      artifact.setPropertyValue(p, property, 2);
    }
    System.out.println("update: " + (System.nanoTime() - start) / Math.pow(10, 6));

  }

  @Test
  public void testWriteValue() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    Workspace p = cloud.createWorkspace(owner, tool, "");
    Artifact artifact = p.createArtifact();
    String property = randomString();
    long start = System.nanoTime();
    artifact.setPropertyValue(p, property, 1);
    System.out.println("write single value: " + (System.nanoTime() - start) / Math.pow(10, 6));
  }

  @Test
  public void testWriteValueInWSHierarchy() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    Workspace p = cloud.createWorkspace(owner, tool, "WS1");
    Workspace c1 = cloud.createWorkspace(owner, tool, "WS2", p, PropagationType.instant, PropagationType.instant);
    Workspace c2 = cloud.createWorkspace(owner, tool, "WS3", p, PropagationType.instant, PropagationType.instant);
    Artifact artifact = c1.createArtifact();
    long duration = 0l;
    for (int i = 0; i < 10; i++) {
      String property = randomString();
      long start = System.nanoTime();
      artifact.setPropertyValue(c1, property, 1);
      duration += (System.nanoTime() - start);
    }
    System.out.println("write value in hierarchy: " + (duration) / Math.pow(10, 6));
  }

  @Test
  public void testWriteSingleValueInWSHierarchy() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    Workspace p = cloud.createWorkspace(owner, tool, "WS1");
    Workspace c1 = cloud.createWorkspace(owner, tool, "WS2", p, PropagationType.instant, PropagationType.instant);
    Workspace c2 = cloud.createWorkspace(owner, tool, "WS3", p, PropagationType.instant, PropagationType.instant);
    Artifact artifact = c1.createArtifact();
    String property = randomString();
    long start = System.nanoTime();
    artifact.setPropertyValue(c1, property, 1);
    System.out.println("write single value in hierarchy: " + (System.nanoTime() - start) / Math.pow(10, 6));
  }

  @Test
  public void testWriteValueMapInWSHierarchy() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    Workspace p = cloud.createWorkspace(owner, tool, "WS1");
    Workspace c1 = cloud.createWorkspace(owner, tool, "WS2", p, PropagationType.instant, PropagationType.instant);
    Workspace c2 = cloud.createWorkspace(owner, tool, "WS3", p, PropagationType.instant, PropagationType.instant);
    Artifact artifact = c1.createArtifact();
    Map<String, Object> properties = new HashMap<>();
    for (int i = 0; i < 10; i++) {
      String property = randomString();
      properties.put(property, 1);
    }
    long start = System.nanoTime();
    artifact.setPropertyValues(c1, properties);
    System.out.println("write valueMap in hierarchy: " + (System.nanoTime() - start) / Math.pow(10, 6));
  }

  @Test
  public void testWriteValueMap() {
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    Workspace p = cloud.createWorkspace(owner, tool, "WS1");
    Artifact artifact = p.createArtifact();
    Map<String, Object> properties = new HashMap<>();
    for (int i = 0; i < 10; i++) {
      String property = randomString();
      properties.put(property, 1);
    }
    long start = System.nanoTime();
    artifact.setPropertyValues(p, properties);
    System.out.println("write valueMap: " + (System.nanoTime() - start) / Math.pow(10, 6));
  }

  private void asdf() {
    PropagationType[] types = new PropagationType[] { PropagationType.instant, PropagationType.triggered };
    Random pushpull = new Random(10);
    Owner owner = cloud.getOwner(DataStorage.ADMIN);
    Tool tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    /** create workspaces **/
    List<Workspace> workspaces = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      PropagationType push = types[pushpull.nextBoolean() ? 1 : 0];
      PropagationType pull = types[pushpull.nextBoolean() ? 1 : 0];
      workspaces.add(cloud.createWorkspace(owner, tool, "WS" + i, push, pull));
    }

    /** create binary tree **/
    for (int i = 0; i < 49; i++) {
      Workspace parent = workspaces.get(i);
      Workspace child1 = workspaces.get(i * 2 + 1);
      Workspace child2 = workspaces.get(i * 2 + 2);
      child1.setParent(parent);
      child2.setParent(parent);
    }

    /** shuffle tree **/
    Random parent = new Random();
    for (int m = 0; m < 50; m++) {
      int ws = parent.nextInt(100);
      int newParent = parent.nextInt(100);
      try {
        workspaces.get(ws).setParent(workspaces.get(newParent));
      } catch (Exception e) {

      }
    }
  }

  private int treeWidth(DataStorage storage) {
    Collection<Object[]> roots = storage.getWorkspaceChildren(null);
    assert 1 == roots.size();
    return visit(0, roots, storage);
  }

  private int visit(int level, Collection<Object[]> workspaces, DataStorage storage) {
    Collection<Object[]> children = new ArrayList<>();
    int max = workspaces.size();
    for (Object[] ws : workspaces) {
      children.addAll(storage.getWorkspaceChildren((long) ws[Columns.WORKSPACE_VERSION.getIndex()]));
    }

    int width = visit(level++, children, storage);
    max = width > max ? width : max;
    return max;
  }

  private int treeHeight(DataStorage storage) {
    Collection<Object[]> roots = storage.getWorkspaceChildren(null);
    assert 1 == roots.size();
    Object[] root = roots.iterator().next();
    return visit(0, root, storage);
  }

  private int visit(int level, Object[] workspace, DataStorage storage) {
    long wsId = (long) workspace[Columns.WORKSPACE_VERSION.getIndex()];
    Collection<Object[]> children = storage.getWorkspaceChildren(wsId);
    int max = level;
    for (Object[] child : children) {
      int height = visit(level++, child, storage);
      if (height > max) {
        max = height;
      }
    }
    return max;
  }
}
