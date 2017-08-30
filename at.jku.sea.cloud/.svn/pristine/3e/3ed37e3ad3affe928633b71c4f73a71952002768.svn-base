package at.jku.sea.cloud.workspacehierarchy.test;

import org.junit.After;
import org.junit.Before;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.DefaultCloud;
import at.jku.sea.cloud.implementation.sql.SQLDataStorage;

public abstract class WorkspaceHierarchy {
  protected Workspace parent;
  protected Workspace child1;
  protected Workspace child2;

  protected static final String PROPERTY = "property";

  protected static Cloud cloud = null;
  protected Tool tool;
  protected Owner owner;
  protected static SQLDataStorage dataStorage = new SQLDataStorage();

  @Before
  public void before() {
    dataStorage.truncateAll();
    dataStorage.init();
    cloud = new DefaultCloud(dataStorage);
    this.owner = cloud.getOwner(DataStorage.ADMIN);
    this.tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
  }

  @After
  public void after() {
    parent.rollbackAll();
    child1.rollbackAll();
    child2.rollbackAll();
    parent.close();
    child1.close();
    child2.close();
  }

}
