package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public abstract class CollectionEvent implements Serializable {
  public final long origin;
  public final long privateVersion;
  public final long owner;
  public final long tool;
  public final long collectionId;
  public final Object elem;
  
  

  CollectionEvent(final long origin, final long privateVersion, final long owner, final long tool, final long collectionId, final Object elem) {
    this.origin = origin;
    this.privateVersion = privateVersion;
    this.owner = owner;
    this.tool = tool;
    this.collectionId = collectionId;
    this.elem = elem;
  }
}
