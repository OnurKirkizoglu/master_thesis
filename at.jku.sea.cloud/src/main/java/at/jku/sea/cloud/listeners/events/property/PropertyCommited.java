package at.jku.sea.cloud.listeners.events.property;

import java.util.List;

import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.PublicVersion;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;

public final class PropertyCommited extends PropertyChange {
  public final PublicVersion version;
  public final List<Change> changes;
  public final long oldBaseVersion;

  public PropertyCommited(final long origin, final Property property, final PublicVersion version, final List<Change> changes, final long oldBaseVersion) {
    super(origin, property, ChangeType.PropertyCommited);
    this.version = version;
    this.changes = changes;
    this.oldBaseVersion = oldBaseVersion;
  }
}
