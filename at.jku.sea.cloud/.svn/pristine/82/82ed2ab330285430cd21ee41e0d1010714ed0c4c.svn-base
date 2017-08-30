package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public class PropertyDeletedEvent extends PropertyEvent implements Serializable {

  public final boolean isDeceased;
  

  PropertyDeletedEvent(final long origin, final long privateVersion, final long owner, final long tool, final long artifactId, final String property, final boolean isDeceased) {
    super(origin, privateVersion, owner, tool, artifactId, property);
    this.isDeceased = isDeceased;
  }

}
