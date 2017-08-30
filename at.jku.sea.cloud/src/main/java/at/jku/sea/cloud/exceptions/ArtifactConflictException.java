package at.jku.sea.cloud.exceptions;

public class ArtifactConflictException extends AbstractConflictException {

  private static final long serialVersionUID = 1L;

  public ArtifactConflictException(final long version) {
    super("artifact commit of version (version=" + version + ") conflicts with trunk", version);
  }

}
