package at.jku.sea.cloud.listeners.events.property;

import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.listeners.events.AbstractChange;
import at.jku.sea.cloud.listeners.events.ChangeType;

public abstract class PropertyChange extends AbstractChange {
  public Property property;
  public final long origin;

  public PropertyChange(final long origin, final Property property, final ChangeType type) {
    this.origin = origin;
    this.property = property;
    this.type = type;
  }
}
