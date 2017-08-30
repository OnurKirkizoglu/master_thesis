package at.jku.sea.cloud.exceptions;

public class ArtifactNotPushOrPullableException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public ArtifactNotPushOrPullableException(final long aid) {
    super("artifact (id=" + aid + ") references (type, package, project) existing only in WS");
  }
}