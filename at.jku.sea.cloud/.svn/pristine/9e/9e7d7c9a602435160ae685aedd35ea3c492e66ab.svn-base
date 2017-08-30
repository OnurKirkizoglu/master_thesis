package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public class PrivateVersionAddedEvent implements Serializable {
  public final long privateVersion;

  PrivateVersionAddedEvent(long privateVersion) {
    this.privateVersion = privateVersion;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    PrivateVersionAddedEvent other = (PrivateVersionAddedEvent) obj;
    if (privateVersion != other.privateVersion)
      return false;
    return true;
  }

}
