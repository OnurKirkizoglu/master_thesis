package at.jku.sea.cloud.exceptions;

public abstract class AbstractConflictException extends RuntimeException implements ConflictException {

  private static final long serialVersionUID = 1L;
  private final long version;

  public AbstractConflictException(final String message, final long version) {
    super(message);
    this.version = version;
  }

  @Override
  public long getPrivateVersion() {
    return this.version;
  }

}
