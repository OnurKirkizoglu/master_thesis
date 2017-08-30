package at.jku.sea.cloud.implementation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Workspace;

public class TestPropertyPerformance {

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
    Package pckg = ws.createPackage();
    for (int i = 0; i < 100000; i++) {
      Artifact artifact = ws.createArtifact(pckg);
      artifact.setPropertyValue(ws, "" + i, i);
    }

    long start = System.nanoTime();
    ws.getArtifacts(pckg);
    System.out.println("[ms]: " + (System.nanoTime() - start) / Math.pow(10, 6));
    
    start = System.nanoTime();
    ws.getArtifactsWithProperty("300", 300, pckg);
    System.out.println("[ms]: " + (System.nanoTime() - start) / Math.pow(10, 6));
  }

}
