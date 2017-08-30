package at.jku.sea.cloud.listeners.events.property;

import java.util.Collection;

import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.ChangeType;
import at.jku.sea.cloud.listeners.events.collection.CollectionChange;

public final class PropertyMapSet extends CollectionChange<PropertyValueSet> {

  public PropertyMapSet(final long origin, final Collection<PropertyValueSet> changes) {
    super(origin, changes, ChangeType.PropertyMapSet);
  }
}
