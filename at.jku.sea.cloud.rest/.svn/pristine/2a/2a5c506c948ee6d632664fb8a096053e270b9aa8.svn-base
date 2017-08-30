package at.jku.sea.cloud.rest.pojo.listener.property;

import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPublicVersion;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "PropertyCommited")
public class PojoPropertyCommited extends PojoPropertyChange {

  private PojoPublicVersion version;
  private long oldBaseVersion;
  private PojoChange[] changes;

  public PojoPropertyCommited() {
  }

  public PojoPropertyCommited(long origin, PojoProperty property, PojoPublicVersion version, PojoChange[] changes, long oldBaseVersion) {
    super(origin, property);
    this.version = version;
    this.setChanges(changes);
    this.setOldBaseVersion(oldBaseVersion);
  }

  public PojoPublicVersion getVersion() {
    return version;
  }

  public void setVersion(PojoPublicVersion version) {
    this.version = version;
  }

  public long getOldBaseVersion() {
    return oldBaseVersion;
  }

  public void setOldBaseVersion(long oldBaseVersion) {
    this.oldBaseVersion = oldBaseVersion;
  }

  public PojoChange[] getChanges() {
    return changes;
  }

  public void setChanges(PojoChange[] changes) {
    this.changes = changes;
  }
}
