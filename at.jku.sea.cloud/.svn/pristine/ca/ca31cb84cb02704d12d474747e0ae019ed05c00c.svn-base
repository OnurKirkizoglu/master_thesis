package at.jku.sea.cloud.modelanalyzer.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.CloudFactory;

public class TestResource {

  private static Cloud cloud;
  private Tool tool;
  private Owner owner;

  private Workspace ws;

  @Before
  public void before() {
    cloud = CloudFactory.getInstance();
    this.owner = cloud.getOwner(DataStorage.ADMIN);
    this.tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    ws = cloud.createWorkspace(owner, tool, "");
  }

  @After
  public void after() {
    ws.rollbackAll();
  }

  @Test
  public void testCreateResource() {
    Resource resource = ws.createResource("at.jku.isse.testResource");
    assertTrue(resource instanceof Resource);
  }

  @Test
  public void testGetResource() {
    Resource resource = ws.createResource("at.jku.isse.testResource");
    assertEquals(resource, ws.getResources("at.jku.isse.testResource").iterator().next());
  }

}
