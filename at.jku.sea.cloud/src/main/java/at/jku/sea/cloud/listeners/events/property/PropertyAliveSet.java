package at.jku.sea.cloud.listeners.events.property;

import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class PropertyAliveSet extends PropertyChange {
  public final boolean alive;

  public PropertyAliveSet(final long origin, final Property property, final boolean alive) {
    super(origin, property, ChangeType.PropertyAliveSet);
    this.alive = alive;
  }
}
