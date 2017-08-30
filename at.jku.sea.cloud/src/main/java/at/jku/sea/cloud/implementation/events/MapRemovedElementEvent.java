package at.jku.sea.cloud.implementation.events;

public class MapRemovedElementEvent extends MapEvent {

  public boolean isKeyReference, isValueReference;
  public Object key, value;
  
  MapRemovedElementEvent(long origin, long privateVersion, long owner, long tool, long mapId, Object key, boolean isKeyReference, Object value, boolean isValueReference) {
    super(origin, privateVersion, owner, tool, mapId);
    this.key = key;
    this.isKeyReference = isKeyReference;
    this.value = value;
    this.isValueReference = isValueReference;
  }

}
