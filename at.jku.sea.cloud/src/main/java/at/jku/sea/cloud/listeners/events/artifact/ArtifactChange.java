package at.jku.sea.cloud.listeners.events.artifact;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.AbstractChange;
import at.jku.sea.cloud.listeners.events.ChangeType;

public abstract class ArtifactChange extends AbstractChange {
  public final Artifact artifact;
//  public final Version origin;
  public final long origin;

  public ArtifactChange(final long origin, final Artifact artifact, final ChangeType type) {
    this.origin = origin;
    this.artifact = artifact;
    this.type = type;
  }
}
