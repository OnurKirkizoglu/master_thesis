package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;
import java.util.Collection;

public class CollectionAddedElementsEvent extends CollectionEvent implements Serializable {
  CollectionAddedElementsEvent(final long origin, final long privateVersion, final long owner, final long tool, final long collectionId, final Collection<?> elem) {
    super(origin, privateVersion, owner, tool, collectionId, elem);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (collectionId ^ (collectionId >>> 32));
    result = prime * result + (int) (origin ^ (origin >>> 32));
    result = prime * result + (int) (privateVersion ^ (privateVersion >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CollectionAddedElementsEvent other = (CollectionAddedElementsEvent) obj;
    if (collectionId != other.collectionId)
      return false;
    if (origin != other.origin)
      return false;
    if (privateVersion != other.privateVersion)
      return false;
    return true;
  }
}
