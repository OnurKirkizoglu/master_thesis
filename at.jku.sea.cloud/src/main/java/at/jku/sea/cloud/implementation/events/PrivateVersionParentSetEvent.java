package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;
import java.util.Objects;

public class PrivateVersionParentSetEvent implements Serializable {
  private static final long serialVersionUID = -5888264341514383828L;
  public final long privateVersion;
  public final Long newParent;

  public PrivateVersionParentSetEvent(long privateVersion, Long newParent) {
    this.privateVersion = privateVersion;
    this.newParent = newParent;
  }

  @Override
  public int hashCode() {
    return Objects.hash(privateVersion, newParent);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PrivateVersionParentSetEvent other = (PrivateVersionParentSetEvent) obj;
    if (privateVersion != other.privateVersion)
      return false;
    
    if(newParent != other.newParent)
      return false;
    
    return true;
  }
}
