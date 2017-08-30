package at.jku.sea.cloud.implementation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Workspace;

public class TestGetArtifactsPerformance {

  private Cloud cloud = CloudFactory.getInstance();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    Workspace ws = cloud.createWorkspace(cloud.createOwner(), cloud.createTool(), "benchmark");
    Artifact artifact = ws.createArtifact();
    for (int i = 0; i < 100; i++) {
      artifact.setPropertyValue(ws, "" + i, i);
    }
    artifact.setPropertyValue(ws, "name", "name");

    long start = System.nanoTime();
    artifact.hasProperty("name");
    artifact.getPropertyValue("name");
    System.out.println("[ms]: " + (System.nanoTime() - start) / Math.pow(10, 6));
    start = System.nanoTime();
    artifact.getAlivePropertiesMap().get("name");
    System.out.println("[ms]: " + (System.nanoTime() - start) / Math.pow(10, 6));
  }

}
