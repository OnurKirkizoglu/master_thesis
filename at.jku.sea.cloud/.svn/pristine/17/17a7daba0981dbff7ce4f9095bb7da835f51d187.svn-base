package at.jku.sea.cloud.listeners.events.workspace;

import java.util.List;

import at.jku.sea.cloud.PublicVersion;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class WorkspaceRebased extends WorkspaceChange {
  public final Version origin;
  public final PublicVersion oldBaseVersion;
  public final List<Change> changes;

  public WorkspaceRebased(Version origin, final Workspace workspace, final PublicVersion oldBaseVersion, final List<Change> changes) {
    super(workspace, ChangeType.WorkspaceRebased);
    this.origin = origin;
    this.oldBaseVersion = oldBaseVersion;
    this.changes = changes;
  }
}
