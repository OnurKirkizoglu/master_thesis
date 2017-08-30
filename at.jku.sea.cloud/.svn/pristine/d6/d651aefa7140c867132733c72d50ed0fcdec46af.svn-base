package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public class PropertyValueSetEvent extends PropertyEvent implements Serializable {
  public final Object oldValue, value;
  public final boolean wasReference;

  PropertyValueSetEvent(final Long origin, final Long version, final long owner, final long tool, final long id, final String property, final Object oldValue, final Object newValue,
      final boolean wasReference) {
    super(origin, version, owner, tool, id, property);
    this.oldValue = oldValue;
    this.value = newValue;
    this.wasReference = wasReference;
  }

}
