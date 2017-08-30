package at.jku.sea.cloud.listeners.events.artifact;

import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.listeners.events.ChangeType;

public class CollectionAddedElements extends ArtifactChange {
  public final Collection<Object> values;

  public CollectionAddedElements(final long origin, final Artifact artifact, final Collection<Object> values) {
    super(origin, artifact, ChangeType.CollectionAddedElement);
    this.values = values;
  }
}
