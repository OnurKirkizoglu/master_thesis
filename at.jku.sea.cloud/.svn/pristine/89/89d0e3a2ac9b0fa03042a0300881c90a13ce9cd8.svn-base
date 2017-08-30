package at.jku.sea.cloud.listeners.events.artifact;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class ArtifactContainerSet extends ArtifactChange {
  public final Container container;

  public ArtifactContainerSet(final long origin, final Artifact artifact, final Container container) {
    super(origin, artifact, ChangeType.ArtifactContainerSet);
    this.container = container;
  }
}
