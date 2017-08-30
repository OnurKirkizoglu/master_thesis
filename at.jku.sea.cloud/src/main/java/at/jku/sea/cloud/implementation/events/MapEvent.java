package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public abstract class MapEvent implements Serializable {
  public final long origin;
  public final long privateVersion;
  public final long owner;
  public final long tool;
  public final long mapId;

  MapEvent(final long origin, final long privateVersion, final long owner, final long tool, final long mapId) {
    this.origin = origin;
    this.privateVersion = privateVersion;
    this.owner = owner;
    this.tool = tool;
    this.mapId = mapId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (mapId ^ (mapId >>> 32));
    result = prime * result + (int) (origin ^ (origin >>> 32));
    result = prime * result + (int) (owner ^ (owner >>> 32));
    result = prime * result + (int) (privateVersion ^ (privateVersion >>> 32));
    result = prime * result + (int) (tool ^ (tool >>> 32));
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
    MapEvent other = (MapEvent) obj;
    if (mapId != other.mapId)
      return false;
    if (origin != other.origin)
      return false;
    if (owner != other.owner)
      return false;
    if (privateVersion != other.privateVersion)
      return false;
    if (tool != other.tool)
      return false;
    return true;
  }
}
