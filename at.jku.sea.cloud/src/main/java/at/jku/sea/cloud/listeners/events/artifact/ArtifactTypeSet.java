package at.jku.sea.cloud.listeners.events.artifact;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public  final class ArtifactTypeSet extends ArtifactChange {
  public final Artifact artifactType;

  public ArtifactTypeSet(final long origin, final Artifact artifact, final Artifact artifactType) {
    super(origin, artifact, ChangeType.ArtifactTypeSet);
    this.artifactType = artifactType;
  }
}
