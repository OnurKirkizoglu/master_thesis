package at.jku.sea.cloud.exceptions;

public class PushConflictException extends AbstractConflictException {

  private static final long serialVersionUID = 1L;
  private final long parentId;

  public PushConflictException(final long childId, final long parentId) {
    super("pushing child (childId=" + childId + ") to parent (parentId=" + parentId + ") failed, ", childId);
    this.parentId = parentId;
  }

  public long getParentId() {
    return parentId;
  }

}
