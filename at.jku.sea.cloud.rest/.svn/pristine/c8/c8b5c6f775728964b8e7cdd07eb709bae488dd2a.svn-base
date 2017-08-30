package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoPublicVersion;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "ArtifactCommited")
public class PojoArtifactCommited extends PojoArtifactChange {

  private PojoPublicVersion version;
  private long oldBaseVersion;
  private PojoChange[] changes;

  public PojoArtifactCommited() {
  }

  public PojoArtifactCommited(long origin, PojoArtifact artifact, PojoPublicVersion version, PojoChange[] changes, long oldBaseVersion) {
    super(origin, artifact);
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
