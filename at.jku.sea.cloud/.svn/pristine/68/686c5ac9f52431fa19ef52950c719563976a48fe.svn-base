package at.jku.sea.cloud.exceptions;

public class ArtifactNotCommitableException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public ArtifactNotCommitableException(final long version, final long id) {
    super("artifact (id=" + id + ", version=" + version + ") cannot be commited, its properties or type, package reference an artifact only existing in the workspace");
  }
}
