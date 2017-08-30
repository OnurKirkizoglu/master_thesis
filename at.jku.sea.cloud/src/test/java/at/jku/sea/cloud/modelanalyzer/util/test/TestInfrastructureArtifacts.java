package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.implementation.CloudFactory;

public class TestInfrastructureArtifacts {
  private static final Cloud cloud = CloudFactory.getInstance();

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testCreateOwner() {
    final Owner owner = TestInfrastructureArtifacts.cloud.createOwner();
    assertEquals(TestInfrastructureArtifacts.cloud.getHeadVersionNumber(), owner.getVersionNumber());
  }

  @Test
  public void testCreateTool() {
    final Tool tool = TestInfrastructureArtifacts.cloud.createTool();
    assertEquals(TestInfrastructureArtifacts.cloud.getHeadVersionNumber(), tool.getVersionNumber());
  }

}
