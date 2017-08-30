package at.jku.sea.cloud.listeners.events.artifact;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class CollectionAddedElement extends ArtifactChange {
  public final Object value;

  public CollectionAddedElement(final long origin, final Artifact artifact, final Object value) {
    super(origin, artifact, ChangeType.CollectionAddedElement);
    this.value = value;
  }
}
