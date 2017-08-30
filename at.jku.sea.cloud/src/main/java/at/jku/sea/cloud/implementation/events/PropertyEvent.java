package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public abstract class PropertyEvent implements Serializable {
  public final Long origin;
  public final Long version;
  public final long owner, tool, artifactId;
  public final String propertyName;

  PropertyEvent(final Long origin, final Long version, final long owner, final long tool, final long id, final String property) {
    this.origin = origin;
    this.version = version;
    this.owner = owner;
    this.tool = tool;
    this.artifactId = id;
    this.propertyName = property;
  }
}
