package at.jku.sea.cloud.exceptions;


public class VersionConflictException extends AbstractConflictException {

  private static final long serialVersionUID = 1L;

  public VersionConflictException(final long version) {
    super("commiting version=" + version + " failed", version);
  }

}
