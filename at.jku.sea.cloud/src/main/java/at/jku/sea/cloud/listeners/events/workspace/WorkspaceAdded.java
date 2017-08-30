package at.jku.sea.cloud.listeners.events.workspace;

import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.listeners.events.ChangeType;

public  final class WorkspaceAdded extends WorkspaceChange {

  public WorkspaceAdded(final Workspace workspace) {
    super(workspace, ChangeType.WorkspaceAdded);
  }
}
