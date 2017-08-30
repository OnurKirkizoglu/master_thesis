package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public class PropertyCommitedEvent implements Serializable {
  public final long privateVersion;
  public final long id;
  public final String property;
  public final String message;
  public final long version;
  public final long oldBaseVersion;

  PropertyCommitedEvent(final long privateVersion, final long id, final String property, final String message, final long version, final long oldBaseVersion) {
    this.privateVersion = privateVersion;
    this.id = id;
    this.property = property;
    this.message = message;
    this.version = version;
    this.oldBaseVersion = oldBaseVersion;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + (int) (privateVersion ^ (privateVersion >>> 32));
    result = prime * result + ((property == null) ? 0 : property.hashCode());
    result = prime * result + (int) (version ^ (version >>> 32));
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
    PropertyCommitedEvent other = (PropertyCommitedEvent) obj;
    if (id != other.id)
      return false;
    if (privateVersion != other.privateVersion)
      return false;
    if (property == null) {
      if (other.property != null)
        return false;
    } else if (!property.equals(other.property))
      return false;
    if (version != other.version)
      return false;
    return true;
  }

}
