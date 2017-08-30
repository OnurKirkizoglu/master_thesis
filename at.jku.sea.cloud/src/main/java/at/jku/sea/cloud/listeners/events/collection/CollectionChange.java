package at.jku.sea.cloud.listeners.events.collection;

import java.util.Collection;

import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.listeners.events.AbstractChange;
import at.jku.sea.cloud.listeners.events.ChangeType;

public abstract class CollectionChange<T> extends AbstractChange {
  public Collection<T> changes;
  public final long origin;

  public CollectionChange(final long origin, final Collection<T> changes, final ChangeType type) {
    this.origin = origin;
    this.type = type;
    this.changes = changes;
  }
}
