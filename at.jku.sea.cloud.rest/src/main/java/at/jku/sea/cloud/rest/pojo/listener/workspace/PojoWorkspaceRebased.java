package at.jku.sea.cloud.rest.pojo.listener.workspace;

import at.jku.sea.cloud.rest.pojo.PojoPublicVersion;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.PojoWorkspace;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "WorkspaceRebased")
public class PojoWorkspaceRebased extends PojoWorkspaceChange {

  private PojoVersion origin;
  private PojoPublicVersion version;
  private PojoChange[] changes;

  public PojoWorkspaceRebased() {
  }

  public PojoWorkspaceRebased(PojoVersion origin, PojoWorkspace workspace, PojoPublicVersion version, PojoChange[] changes) {
    super(workspace);
    this.version = version;
    this.changes = changes;
    this.origin = origin;
  }

  public PojoPublicVersion getVersion() {
    return version;
  }

  public void setVersion(PojoPublicVersion version) {
    this.version = version;
  }

  public PojoChange[] getChanges() {
    return changes;
  }

  public void setChanges(PojoChange[] changes) {
    this.changes = changes;
  }

  public PojoVersion getOrigin() {
    return origin;
  }

  public void setOrigin(PojoVersion origin) {
    this.origin = origin;
  }
}
