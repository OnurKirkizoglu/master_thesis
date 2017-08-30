package at.jku.sea.cloud.listeners.events.property;

import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public  final class PropertyRollBack extends PropertyChange {
  public final boolean isDeceased;

  public PropertyRollBack(final long origin, final Property property, final boolean isDeceased) {
    super(origin, property, ChangeType.PropertyRollBack);
    this.isDeceased = isDeceased;
  }
}
