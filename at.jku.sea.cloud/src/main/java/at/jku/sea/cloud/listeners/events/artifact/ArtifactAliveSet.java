package at.jku.sea.cloud.listeners.events.artifact;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class ArtifactAliveSet extends ArtifactChange {
  public boolean alive;

  public ArtifactAliveSet(final long origin, final Artifact artifact, final boolean alive) {
    super(origin, artifact, ChangeType.ArtifactAliveSet);
    this.alive = alive;
  }
}
