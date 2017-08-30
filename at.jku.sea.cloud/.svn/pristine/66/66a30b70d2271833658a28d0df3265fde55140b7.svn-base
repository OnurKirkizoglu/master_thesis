package at.jku.sea.cloud.implementation.events;

public class MapPutEvent extends MapEvent {

  public Object oldValue, newValue, key;
  public boolean isOldValueReference, isNewValueReference, isKeyReference, isAdded;
  
  MapPutEvent(long origin, long privateVersion, long owner, long tool, long mapId, Object key, boolean isKeyReference, Object oldValue, boolean isOldValueReference, Object newValue, boolean isNewValueReference, boolean isAdded) {
    super(origin, privateVersion, owner, tool, mapId);
    this.key = key;
    this.isKeyReference = isKeyReference;
    this.oldValue = oldValue;
    this.isOldValueReference = isOldValueReference;
    this.newValue = newValue;
    this.isNewValueReference = isNewValueReference;
    this.isAdded = isAdded;
  }
}
