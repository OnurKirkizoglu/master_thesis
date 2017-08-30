package at.jku.sea.cloud.listeners.events.workspace;

import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.listeners.events.ChangeType;

public class WorkspaceParentSet extends WorkspaceChange {
  private static final long serialVersionUID = 7001427915555417594L;
  public Workspace parent;

  public WorkspaceParentSet(Workspace parent, Workspace workspace) {
    super(workspace, ChangeType.WorkspaceParentSet);
    this.parent = parent;
  }
}
