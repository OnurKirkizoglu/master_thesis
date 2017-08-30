package at.jku.sea.cloud.exceptions;

public class WorkspaceConflictException extends AbstractConflictException {

  private final long parentId;

  public WorkspaceConflictException(final long childId, final long parentId) {
    super("Workspaces (child: " + childId + "; desired parent: " + parentId
        + ") are in conflict, they contain conflicting assignments to artifacts or properties", childId);
    this.parentId = parentId;
  }

  public long getParentVersionNumber() {
    return parentId;
  }
}
