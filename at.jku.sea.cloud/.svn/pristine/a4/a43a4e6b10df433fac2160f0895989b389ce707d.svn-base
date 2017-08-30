package at.jku.sea.cloud.implementation.sql;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.DataStorage.Columns;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.implementation.DefaultCloud;

public class TestGetChildWorkspaces {

  protected Workspace parent;

  private static SQLDataStorage storage = new SQLDataStorage();
  protected static Cloud cloud = new DefaultCloud(storage);
  protected Tool tool;
  protected Owner owner;

  @Before
  public void before() {
    this.owner = cloud.getOwner(DataStorage.ADMIN);
    this.tool = cloud.getTool(DataStorage.JUNIT_TOOL_ID);
    parent = cloud.createWorkspace(owner, tool, "parent", null, PropagationType.triggered, PropagationType.triggered);
  }

  @Test
  public void testGetWorkspacesWithNullParent() {
    Collection<Object[]> workspaces = storage.getWorkspaceChildren(null);
    assertEquals(1, workspaces.size());
    Object[] wsParent = workspaces.iterator().next();
    assertEquals(parent.getId(), wsParent[Columns.WORKSPACE_VERSION.getIndex()]);
  }
}
