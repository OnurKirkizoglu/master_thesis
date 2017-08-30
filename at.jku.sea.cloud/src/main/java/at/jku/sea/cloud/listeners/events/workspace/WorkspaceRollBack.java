package at.jku.sea.cloud.listeners.events.workspace;

import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class WorkspaceRollBack extends WorkspaceChange {

  public WorkspaceRollBack(final Workspace workspace) {
    super(workspace, ChangeType.WorkspaceRollBack);
  }
}
