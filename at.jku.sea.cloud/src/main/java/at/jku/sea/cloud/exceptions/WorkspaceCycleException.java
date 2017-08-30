package at.jku.sea.cloud.exceptions;

public class WorkspaceCycleException extends AbstractConflictException {

  private final long parentId;

  public WorkspaceCycleException(final long childId, final long parentId) {
    super("Setting the specified parent (parentId=" + parentId + ") of workspace (childId=" + childId + ") would close a cycle", childId);
    this.parentId = parentId;
  }

  public long getParentVersionNumber() {
    return parentId;
  }
}
