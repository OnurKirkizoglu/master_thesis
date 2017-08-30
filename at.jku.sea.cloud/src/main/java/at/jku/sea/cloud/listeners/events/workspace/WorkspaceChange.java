package at.jku.sea.cloud.listeners.events.workspace;

import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.listeners.events.AbstractChange;
import at.jku.sea.cloud.listeners.events.ChangeType;

public abstract class WorkspaceChange extends AbstractChange {
  public Workspace workspace;

  public WorkspaceChange(final Workspace workspace, final ChangeType type) {
    this.workspace = workspace;
    this.type = type;
  }
}
