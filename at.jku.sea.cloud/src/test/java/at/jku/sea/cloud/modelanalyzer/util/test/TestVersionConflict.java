package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactConflictException;
import at.jku.sea.cloud.exceptions.PropertyConflictException;
import at.jku.sea.cloud.exceptions.VersionConflictException;
import at.jku.sea.cloud.implementation.CloudFactory;

public class TestVersionConflict {

  private static Cloud cloud;
  private Tool tool;
  private Owner owner;

  @Before
  public void before() {
    cloud = CloudFactory.getInstance();

    // final TestListener listener = new TestListener();
    // cloud.addDatastorageListener(listener);

    this.owner = cloud.getOwner(DataStorage.ADMIN);
    this.tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
  }

  @After
  public void after() {
  }

  @Test
  public void testArtifactConflictSadlauer() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    final Artifact a1 = ws1.createArtifact();
    a1.setPropertyValue(ws1, "n", 1);
    ws1.commitAll("");
    final Artifact a2 = ws2.createArtifact();
    a2.setPropertyValue(ws2, "n", 1);
    ws2.commitAll("");
    a1.setPropertyValue(ws1, "n", 2);
    final Set<Artifact> conflictingArtifacts = ws1.getArtifactConflicts();
    assertTrue(conflictingArtifacts.isEmpty());
    ws1.rollbackAll();
    ws2.rollbackAll();
    ws1.close();
    ws2.close();
  }

  @Test
  public void testArtifactConflict() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    ws1.getArtifact(DataStorage.TRACE_LINK).setPackage(ws1, ws1.createPackage());
    ws1.commitAll("");
    ws2.getArtifact(DataStorage.TRACE_LINK).setPackage(ws2, ws2.createPackage());
    final Set<Artifact> conflictingArtifacts = ws2.getArtifactConflicts();
    assertFalse(conflictingArtifacts.isEmpty());
    assertTrue(conflictingArtifacts.size() == 1);
    assertEquals(conflictingArtifacts.iterator().next().getId(), DataStorage.TRACE_LINK);
    ws1.rollbackAll();
    ws2.rollbackAll();
    ws1.close();
    ws2.close();
  }

  @Test
  public void testArtifactNoConflict() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    ws1.getArtifact(DataStorage.TRACE_LINK).setPackage(ws1, ws1.createPackage());
    ws1.commitAll("");
    ws2.createArtifact();
    final Set<Artifact> conflictingArtifacts = ws2.getArtifactConflicts();
    assertTrue(conflictingArtifacts.isEmpty());
    ws1.rollbackAll();
    ws2.rollbackAll();
    ws1.close();
    ws2.close();
  }

  @Test(expected = VersionConflictException.class)
  public void testCommitException() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    ws1.getArtifact(DataStorage.TRACE_LINK).setPackage(ws1, ws1.createPackage());
    ws1.commitAll("");
    ws2.getArtifact(DataStorage.TRACE_LINK).setPackage(ws2, ws2.createPackage());
    try {
      ws2.commitAll("");
    } catch (final RuntimeException e) {
      final Set<Artifact> conflictingArtifacts = ws2.getArtifactConflicts();
      ws1.rollbackAll();
      ws2.rollbackAll();
      ws1.close();
      ws2.close();
      assertFalse(conflictingArtifacts.isEmpty());
      assertTrue(conflictingArtifacts.size() == 1);
      assertEquals(conflictingArtifacts.iterator().next().getId(), DataStorage.TRACE_LINK);
      throw e;
    }
  }

  @Test(expected = ArtifactConflictException.class)
  public void testCommitArtifactException() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    ws1.getArtifact(DataStorage.TRACE_LINK).setPackage(ws1, ws1.createPackage());
    ws1.commitAll("");
    ws2.getArtifact(DataStorage.TRACE_LINK).setPackage(ws2, ws2.createPackage());
    try {
      ws2.commitArtifact(ws2.getArtifact(DataStorage.TRACE_LINK), "");
    } catch (final RuntimeException e) {
      assertEquals(ws2.getArtifactConflicts().size(), 1);
      assertEquals(ws2.getArtifactConflicts().iterator().next().getId(), DataStorage.TRACE_LINK);
      ws1.rollbackAll();
      ws2.rollbackAll();
      ws1.close();
      ws2.close();
      throw e;
    }
  }

  @Test(expected = PropertyConflictException.class)
  public void testCommitPropertyException() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    ws1.getArtifact(DataStorage.TRACE_LINK).setPropertyValue(ws1, "n", "asdf");
    ws1.commitAll("");
    ws2.getArtifact(DataStorage.TRACE_LINK).setPropertyValue(ws2, "n", "blub");
    try {
      final Property property = ws2.getArtifact(DataStorage.TRACE_LINK).getProperty("n");
      ws2.commitProperty(property, "");
    } catch (final RuntimeException e) {
      final PropertyConflictException pe = (PropertyConflictException) e;
      final Set<Property> conflictingProperties = ws2.getPropertyConflicts();
      assertEquals(conflictingProperties.size(), 1);
      final Artifact a = ws2.getArtifact(DataStorage.TRACE_LINK);
      assertEquals("n", conflictingProperties.iterator().next().getName());
      ws1.rollbackAll();
      ws2.rollbackAll();
      ws1.close();
      ws2.close();

      throw e;
    }
  }

  @Test
  public void testPropertyConflict() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    ws1.getArtifact(DataStorage.TRACE_LINK).setPropertyValue(ws1, "n", "asdf");
    ws1.commitAll("");
    ws2.getArtifact(DataStorage.TRACE_LINK).setPropertyValue(ws2, "n", "blub");
    final Set<Property> conflictingProperties = ws2.getPropertyConflicts();
    assertFalse(conflictingProperties.isEmpty());
    assertTrue(conflictingProperties.size() == 1);
    assertEquals(conflictingProperties.iterator().next().getArtifact().getId(), DataStorage.TRACE_LINK);
    assertEquals(conflictingProperties.iterator().next().getName(), "n");
    ws1.rollbackAll();
    ws2.rollbackAll();
    ws1.close();
    ws2.close();
  }

  @Test
  public void testPropertyNoConflict() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    ws1.getArtifact(DataStorage.TRACE_LINK).setPropertyValue(ws1, "n", "asdf");
    ws1.commitAll("");
    ws2.getArtifact(DataStorage.TRACE_LINK).setPropertyValue(ws2, "m", "blub");
    final Set<Property> conflictingProperties = ws2.getPropertyConflicts();
    assertTrue(conflictingProperties.isEmpty());
    ws1.rollbackAll();
    ws2.rollbackAll();
    ws1.close();
    ws2.close();
  }

  @Test
  public void testSuccessfullRebase() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    final Artifact a1 = ws1.createArtifact();
    ws1.commitAll("");
    assertEquals(ws2.getBaseVersion().getVersionNumber(), cloud.getHeadVersionNumber() - 1);
    final Map<Artifact, Set<Property>> conflicts = ws2.rebaseToHeadVersion();
    final Artifact a2 = ws2.getArtifact(a1.getId());
    assertEquals(a1.getId(), a2.getId());
    assertEquals(ws2.getBaseVersion().getVersionNumber(), cloud.getHeadVersionNumber());
    assertEquals(ws1.getBaseVersion().getVersionNumber(), ws2.getBaseVersion().getVersionNumber());
    ws1.close();
    ws2.close();
    assertTrue(conflicts.isEmpty());
  }

  public void testRebaseWithConflicts() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Workspace ws2 = cloud.createWorkspace(owner, tool, "WS2");
    final Artifact a1 = ws1.getArtifact(DataStorage.COMPLEX_TYPE);
    final Artifact a2 = ws2.getArtifact(DataStorage.COMPLEX_TYPE);
    a1.setPropertyValue(ws1, "asdf", 1);
    a2.setPropertyValue(ws2, "asdf", 3);
    ws1.commitAll("");
    final Map<Artifact, Set<Property>> conflicts = ws2.rebaseToHeadVersion();
    ws1.close();
    ws2.close();
    assertTrue(!conflicts.isEmpty());
  }

}
