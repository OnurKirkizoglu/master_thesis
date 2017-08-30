package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public class PrivateVersionRebasedEvent implements Serializable {
  public final long origin;
  public final long privateVersion;
  public final long oldBaseVersion;
  public final long newBaseVersion;

  PrivateVersionRebasedEvent(long origin, long privateVersion, long oldBaseVersion, long newBaseVersion) {
    this.origin = origin;
    this.privateVersion = privateVersion;
    this.oldBaseVersion = oldBaseVersion;
    this.newBaseVersion = newBaseVersion;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (newBaseVersion ^ (newBaseVersion >>> 32));
    result = prime * result + (int) (oldBaseVersion ^ (oldBaseVersion >>> 32));
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
    PrivateVersionRebasedEvent other = (PrivateVersionRebasedEvent) obj;
    if (newBaseVersion != other.newBaseVersion)
      return false;
    if (oldBaseVersion != other.oldBaseVersion)
      return false;
    if (privateVersion != other.privateVersion)
      return false;
    return true;
  }

}
