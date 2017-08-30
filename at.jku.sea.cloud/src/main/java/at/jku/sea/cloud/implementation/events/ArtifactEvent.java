package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

import at.jku.sea.cloud.listeners.DataStorageEventType;

public class ArtifactEvent implements Serializable {
  public final DataStorageEventType eventType;
  public final long origin;
  public final long privateVersion;
  public final long owner;
  public final long tool;
  public final long id;
  public final Long type;
  public final Long packageId;
  public final boolean alive;
  public final boolean isDeceased;

  ArtifactEvent(final DataStorageEventType eventType, final long origin, final long privateVersion, final long owner, final long tool, final long id, final Long type, final Long packageId,
      final boolean alive, final boolean isDeceased) {
    this.eventType = eventType;
    this.isDeceased = isDeceased;
    this.alive = alive;
    this.packageId = packageId;
    this.type = type;
    this.id = id;
    this.tool = tool;
    this.owner = owner;
    this.privateVersion = privateVersion;
    this.origin = origin;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
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
    ArtifactEvent other = (ArtifactEvent) obj;
    if (eventType != other.eventType)
      return false;
    if (id != other.id)
      return false;
    if (origin != other.origin)
      return false;
    if (privateVersion != other.privateVersion)
      return false;
    return true;
  }
}
