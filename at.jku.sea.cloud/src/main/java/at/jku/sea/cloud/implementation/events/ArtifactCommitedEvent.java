package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public class ArtifactCommitedEvent implements Serializable {
  public final long privateVersion;
  public final long id;
  public final Long type;
  public final String message;
  public final long version;
  public final long oldBaseVersion;

  ArtifactCommitedEvent(final long privateVersion, final long id, final Long type, final String message, final long version, final long oldBaseVersion) {
    this.privateVersion = privateVersion;
    this.id = id;
    this.type = type;
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
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    ArtifactCommitedEvent other = (ArtifactCommitedEvent) obj;
    if (id != other.id)
      return false;
    if (privateVersion != other.privateVersion)
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    if (version != other.version)
      return false;
    return true;
  }
}
