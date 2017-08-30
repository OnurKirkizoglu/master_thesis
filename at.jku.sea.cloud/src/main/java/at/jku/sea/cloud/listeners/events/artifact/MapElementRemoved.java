package at.jku.sea.cloud.listeners.events.artifact;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public class MapElementRemoved extends ArtifactChange {
  
  public final Object key, value;

  public MapElementRemoved(long origin, Artifact artifact, Object key, Object value) {
    super(origin, artifact, ChangeType.MapRemovedElement);
    this.key = key;
    this.value = value;
  }

}
