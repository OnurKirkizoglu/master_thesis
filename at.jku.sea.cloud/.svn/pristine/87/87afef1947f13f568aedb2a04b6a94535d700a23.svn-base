package at.jku.sea.cloud.listeners.events.property;

import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class PropertyValueSet extends PropertyChange {
  public final Object value;
  public final Object oldValue;
  public final boolean wasReference;

  public PropertyValueSet(final long origin, final Property property, final Object value, final Object oldValue, final boolean wasReference) {
    super(origin, property, ChangeType.PropertyValueSet);
    this.value = value;
    this.oldValue = oldValue;
    this.wasReference = wasReference;
  }
}
