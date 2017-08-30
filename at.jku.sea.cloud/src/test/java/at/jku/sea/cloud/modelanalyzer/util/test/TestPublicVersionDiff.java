package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;
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
import at.jku.sea.cloud.implementation.CloudFactory;

public class TestPublicVersionDiff {

  private static Cloud cloud;
  private Tool tool;
  private Owner owner;

  @Before
  public void before() {
    cloud = CloudFactory.getInstance();
    this.owner = cloud.getOwner(DataStorage.ADMIN);
    this.tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
  }

  @After
  public void after() {
  }

  @Test
  public void testArtifactSimpleDiff() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact();
    final long before = cloud.getHeadVersionNumber();
    ws1.commitAll("");
    final long after = cloud.getHeadVersionNumber();
    final Set<Artifact> diffs = cloud.getArtifactDiffs(after, before);
    assertTrue(diffs.contains(cloud.getArtifact(after, artifact.getId())));
    assertEquals(diffs.size(), 1);
    ws1.rollbackAll();
    ws1.close();
  }

  @Test
  public void testArtifactSimpleDiffOverMultipleVersions() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact();
    final long before = cloud.getHeadVersionNumber();
    final long a0 = ws1.commitAll("");
    final Artifact artifact1 = ws1.createArtifact();
    final long a1 = ws1.commitAll("");
    final Artifact artifact2 = ws1.createArtifact();
    final long a2 = ws1.commitAll("");
    final Set<Artifact> diffs = cloud.getArtifactDiffs(a2, before);
    assertTrue(diffs.contains(cloud.getArtifact(a0, artifact.getId())));
    assertTrue(diffs.contains(cloud.getArtifact(a1, artifact1.getId())));
    assertTrue(diffs.contains(cloud.getArtifact(a2, artifact2.getId())));
    assertEquals(diffs.size(), 3);
    ws1.rollbackAll();
    ws1.close();
  }

  @Test
  public void testArtifactPropertySimpleDiff() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact();
    final Artifact artifact1 = ws1.createArtifact();
    artifact1.setPropertyValue(ws1, "name", "blub");
    final long before = cloud.getHeadVersionNumber();
    ws1.commitAll("");
    final long after = cloud.getHeadVersionNumber();
    final Map<Artifact, Set<Property>> diffs = cloud.getDiffs(after, before);
    assertTrue(diffs.containsKey(cloud.getArtifact(after, artifact.getId())));
    assertTrue(diffs.containsKey(cloud.getArtifact(after, artifact1.getId())));
    assertEquals(diffs.size(), 2);
    assertTrue(diffs.get(cloud.getArtifact(after, artifact.getId())).isEmpty());
    assertTrue(!diffs.get(cloud.getArtifact(after, artifact1.getId())).isEmpty());
    assertEquals(diffs.get(cloud.getArtifact(after, artifact1.getId())).iterator().next().getName(), "name");
    ws1.rollbackAll();
    ws1.close();
  }

  @Test
  public void testArtifactPropertySimpleDiffObj() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact();
    final Artifact artifact1 = ws1.createArtifact();
    artifact1.setPropertyValue(ws1, "name", "blub");
    final Property prop = artifact1.getProperty("name");
    final long before = cloud.getHeadVersionNumber();
    ws1.commitAll("");
    final long after = cloud.getHeadVersionNumber();
    final Map<Artifact, Set<Property>> diffs = cloud.getDiffs(after, before);
    assertEquals(diffs.size(), 2);

    final Artifact artifactVAfter = cloud.getArtifact(after, artifact.getId());
    final Artifact artifact1VAfter = cloud.getArtifact(after, artifact1.getId());
    final Property propVAfter = artifact1VAfter.getProperty("name");
    assertTrue(diffs.containsKey(artifactVAfter));
    assertTrue(diffs.containsKey(artifact1VAfter));
    assertTrue(diffs.get(artifactVAfter).isEmpty());
    assertTrue(!diffs.get(artifact1VAfter).isEmpty());
    assertTrue(diffs.get(artifact1VAfter).contains(propVAfter));
    ws1.rollbackAll();
    ws1.close();
  }

  @Test
  public void testArtifactPropertyDiffOverMultipleVersions() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact();
    final Artifact artifact1 = ws1.createArtifact();
    artifact1.setPropertyValue(ws1, "name", "blub");
    final long before = cloud.getHeadVersionNumber();
    final long a0 = ws1.commitAll("");
    final Artifact artifact2 = ws1.createArtifact();
    artifact1.setPropertyValue(ws1, "asdf", "asdf");
    artifact2.setPropertyValue(ws1, "name", 123);
    final long a1 = ws1.commitAll("");
    final Artifact artifact3 = ws1.createArtifact();
    final long a2 = ws1.commitAll("");
    final long after = cloud.getHeadVersionNumber();
    final Map<Artifact, Set<Property>> diffs = cloud.getDiffs(after, before);

    assertTrue(diffs.containsKey(cloud.getArtifact(a0, artifact.getId())));
    assertTrue(diffs.containsKey(cloud.getArtifact(a0, artifact1.getId())));
    assertTrue(diffs.containsKey(cloud.getArtifact(a1, artifact2.getId())));
    assertTrue(diffs.containsKey(cloud.getArtifact(a2, artifact3.getId())));

    assertEquals(diffs.size(), 5);

    assertTrue(diffs.get(cloud.getArtifact(a0, artifact.getId())).isEmpty());
    assertTrue(!diffs.get(cloud.getArtifact(a0, artifact1.getId())).isEmpty());
    assertTrue(!diffs.get(cloud.getArtifact(a1, artifact2.getId())).isEmpty());
    assertTrue(diffs.get(cloud.getArtifact(a2, artifact3.getId())).isEmpty());

    assertEquals(diffs.get(cloud.getArtifact(a0, artifact1.getId())).iterator().next().getName(), "name");
    assertEquals(diffs.get(cloud.getArtifact(a1, artifact1.getId())).iterator().next().getName(), "asdf");
    assertEquals(diffs.get(cloud.getArtifact(a1, artifact2.getId())).iterator().next().getName(), "name");
    ws1.rollbackAll();
    ws1.close();
  }

  @Test
  public void testArtifactPropertyDiffOverMultipleVersionsObj() {
    final Workspace ws1 = cloud.createWorkspace(owner, tool, "WS1");
    final Artifact artifact = ws1.createArtifact();
    final Artifact artifact1 = ws1.createArtifact();
    artifact1.setPropertyValue(ws1, "name", "blub");
    final long before = cloud.getHeadVersionNumber();
    ws1.commitAll("");
    final long v0 = cloud.getHeadVersionNumber();
    final Artifact artifact2 = ws1.createArtifact();
    artifact1.setPropertyValue(ws1, "asdf", "asdf");
    artifact2.setPropertyValue(ws1, "name", 123);
    ws1.commitAll("");
    final long v1 = cloud.getHeadVersionNumber();
    final Artifact artifact3 = ws1.createArtifact();
    ws1.commitAll("");
    final long v2 = cloud.getHeadVersionNumber();
    final Artifact artifact4 = ws1.createArtifact();
    artifact1.setPropertyValue(ws1, "name", "asdf");
    artifact4.setPropertyValue(ws1, "asdfasf", 39402);
    ws1.commitAll("");
    final long after = cloud.getHeadVersionNumber();
    final Map<Artifact, Set<Property>> diffs = cloud.getDiffs(after, before);

    final Artifact a1 = cloud.getArtifact(v0, artifact.getId());
    final Artifact a2 = cloud.getArtifact(v0, artifact1.getId());
    final Artifact a3 = cloud.getArtifact(v1, artifact1.getId());
    final Artifact a7 = cloud.getArtifact(v1, artifact2.getId());
    final Artifact a4 = cloud.getArtifact(v2, artifact3.getId());
    final Artifact a5 = cloud.getArtifact(after, artifact1.getId());
    final Artifact a6 = cloud.getArtifact(after, artifact4.getId());

    assertTrue(diffs.containsKey(a1));
    assertTrue(diffs.containsKey(a2));
    assertTrue(diffs.containsKey(a3));
    assertTrue(diffs.containsKey(a4));
    assertTrue(diffs.containsKey(a5));
    assertTrue(diffs.containsKey(a6));
    assertTrue(diffs.containsKey(a7));

    assertEquals(diffs.size(), 7);

    assertTrue(diffs.get(a1).isEmpty());
    assertTrue(!diffs.get(a2).isEmpty());

    assertTrue(!diffs.get(a3).isEmpty());
    assertTrue(!diffs.get(a7).isEmpty());

    assertTrue(diffs.get(a4).isEmpty());

    assertTrue(!diffs.get(a5).isEmpty());
    assertTrue(!diffs.get(a6).isEmpty());

    final Property p2 = a2.getProperty("name");
    final Property p3 = a3.getProperty("asdf");
    final Property p4 = a7.getProperty("name");

    final Property p5 = a5.getProperty("name");

    final Property p6 = a6.getProperty("asdfasf");

    assertTrue(diffs.get(a2).contains(p2));
    assertTrue(diffs.get(a3).contains(p3));
    assertTrue(diffs.get(a7).contains(p4));
    assertTrue(diffs.get(a5).contains(p5));
    assertTrue(diffs.get(a6).contains(p6));
    ws1.rollbackAll();
    ws1.close();
  }

  @Test
  public void testGetArtifactConflictsWithVersion() {
    final Workspace WS = cloud.createWorkspace(owner, tool, "WS1");
    Artifact artifact = WS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    artifact.setType(WS, WS.getPackage(DataStorage.EPLAN_PCKG));// WS.createProject());

    Artifact artifact1 = WS.getArtifact(DataStorage.AT_PACKAGE_ID);
    artifact1.setType(WS, WS.getPackage(DataStorage.EPLAN_PCKG));// WS.createProject());

    Workspace tempWS = cloud.createWorkspace(owner, tool, "WS1tmp");

    Artifact temp = tempWS.getArtifact(DataStorage.DESIGNSPACE_META_MODEL);
    temp.setType(tempWS, WS.getPackage(DataStorage.PROE_PACKAGE));// tempWS.createPackage());

    Artifact temp1 = tempWS.getArtifact(DataStorage.AT_PACKAGE_ID);
    temp1.setType(tempWS, WS.getPackage(DataStorage.PROE_PACKAGE));// tempWS.createPackage());

    long v1 = tempWS.commitArtifact(temp, "");
    long v2 = tempWS.commitArtifact(temp1, "");

    Set<Artifact> conflicts = WS.getArtifactConflicts(v1);
    assertEquals(1, conflicts.size());
    assertEquals(temp.getId(), conflicts.toArray(new Artifact[conflicts.size()])[0].getId());

    conflicts = WS.getArtifactConflicts(v2);
    assertEquals(2, conflicts.size());
    WS.rollbackAll();
    WS.close();
    tempWS.rollbackAll();
    tempWS.close();
  }
}
