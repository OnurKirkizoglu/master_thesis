package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public class VersionCommitedEvent implements Serializable {

  public final long privateVersion;
  public final String message;
  public final long version;
  public final long oldBaseVersion;

  VersionCommitedEvent(final long privateVersion, final String message, final long version, final long oldBaseVersion) {
    this.privateVersion = privateVersion;
    this.message = message;
    this.version = version;
    this.oldBaseVersion = oldBaseVersion;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (privateVersion ^ (privateVersion >>> 32));
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
    VersionCommitedEvent other = (VersionCommitedEvent) obj;
    if (privateVersion != other.privateVersion)
      return false;
    if (version != other.version)
      return false;
    return true;
  }
}
