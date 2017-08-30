package at.jku.sea.cloud.modelanalyzer.util.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.implementation.CloudFactory;
import at.jku.sea.cloud.mmm.MMMTypeProperties;

public class TestWorkspace {

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

  @Test(expected = WorkspaceExpiredException.class)
  public void testWorkspaceClose() {
    Workspace ws = cloud.createWorkspace(owner, tool, "");
    ws.close();
    cloud.getWorkspace(ws.getId());
  }

  @Test
  public void testGetArtifactsWithPropertyMap() {
    Workspace ws = cloud.createWorkspace(owner, tool, "");
    Artifact artifact = ws.getArtifact(DataStorage.COMPLEX_TYPE);
    Set<String> properties = new HashSet<>();
    properties.add(MMMTypeProperties.NAME);
    Map<Artifact, Map<String, Object>> artifactsPropertyMap = ws.getArtifactsPropertyMap(new HashSet<>(Arrays.asList(new Artifact[] { artifact })), properties);
    Assert.assertEquals(1, artifactsPropertyMap.keySet().size());
    Assert.assertEquals(1, artifactsPropertyMap.get(artifact).size());
    Assert.assertEquals(MMMTypeProperties.COMPLEXTYPE_NAME, artifactsPropertyMap.get(artifact).get(MMMTypeProperties.NAME));

  }
}
