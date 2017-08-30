package at.jku.sea.cloud.listeners.events.artifact;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class CollectionRemovedElement extends ArtifactChange {
  public final Object value;

  public CollectionRemovedElement(final long origin, final Artifact artifact, final Object value) {
    super(origin, artifact, ChangeType.CollectionRemovedElement);
    this.value = value;
  }
}
