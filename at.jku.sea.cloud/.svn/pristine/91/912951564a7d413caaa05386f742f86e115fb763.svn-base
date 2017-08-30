package at.jku.sea.cloud.listeners.events.artifact;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public class MapPut extends ArtifactChange {

  public final Object key, oldValue, newValue;
  public final boolean isAdded;
  
  public MapPut(long origin, Artifact artifact, Object key, Object oldValue, Object newValue, boolean isAdded) {
    super(origin, artifact, ChangeType.MapPut);
    this.key = key;
    this.oldValue = oldValue;
    this.newValue = newValue;
    this.isAdded = isAdded;
  }
}
