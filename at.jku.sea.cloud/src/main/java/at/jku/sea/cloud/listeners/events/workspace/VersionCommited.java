package at.jku.sea.cloud.listeners.events.workspace;

import java.util.List;

import at.jku.sea.cloud.PublicVersion;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class VersionCommited extends WorkspaceChange {
  public final PublicVersion version;
  public final List<Change> changes;
  public final long oldBaseVersion;

  public VersionCommited(final Workspace workspace, final PublicVersion version, final List<Change> changes, final long oldBaseVersion) {
    super(workspace, ChangeType.VersionCommited);
    this.version = version;
    this.changes = changes;
    this.oldBaseVersion = oldBaseVersion;
  }

}
