package at.jku.sea.cloud.exceptions;

public class PropertyNotPushOrPullableException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public PropertyNotPushOrPullableException(final long aid, final String property) {
    super("property (id=" + aid + ", property=" + property + ") either belongs or references an artifact existing only in WS");
  }

}
