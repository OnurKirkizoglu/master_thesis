package at.jku.sea.cloud.exceptions;

public class TypeNotSupportedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public TypeNotSupportedException() {
    super("Only Boolean, Character, String, Number and Artifacts are allowed.");
  }

  public TypeNotSupportedException(final Class<?> type) {
    super("Only Boolean, Character, String, Number and Artifacts are allowed. But was: " + type);
  }
}
