package at.jku.sea.cloud.listeners.events.artifact;

import java.util.List;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.PublicVersion;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class ArtifactCommited extends ArtifactChange {
  public final PublicVersion version;
  public final List<Change> changes;
  public final long oldBaseVersion;

  public ArtifactCommited(final long origin, final Artifact artifact, final PublicVersion version, final List<Change> changes, final long oldBaseVersion) {
    super(origin, artifact, ChangeType.ArtifactCommited);
    this.version = version;
    this.changes = changes;
    this.oldBaseVersion = oldBaseVersion;
  }
}
