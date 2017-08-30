package at.jku.sea.cloud.listeners.events.artifact;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public  final class ArtifactRollBack extends ArtifactChange {
  public final boolean isDeceased;

  public ArtifactRollBack(final long origin, final Artifact artifact, final boolean isDeceased) {
    super(origin, artifact, ChangeType.ArtifactRollback);
    this.isDeceased = isDeceased;
  }
}
