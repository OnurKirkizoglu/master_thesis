package at.jku.sea.cloud.exceptions;

public class ArtifactNotDeleteableException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public ArtifactNotDeleteableException(final long privateVersion, final long aid) {
    super("artifact (version=" + privateVersion + "id=" + aid + ") is not deletable, it only exists in this WS and is referenced (type, package, property) by another artifact");
  }
}
