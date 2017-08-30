package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public class PropertyAliveSetEvent extends PropertyEvent implements Serializable {
  public final boolean alive;

   PropertyAliveSetEvent(final long origin, final long version, final long owner, final long tool, final long artifactId, final String property, final boolean alive) {
    super(origin, version, owner, tool, artifactId, property);
    this.alive = alive;
  }

}
