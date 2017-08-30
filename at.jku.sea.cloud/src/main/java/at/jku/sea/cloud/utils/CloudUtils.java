package at.jku.sea.cloud.utils;

import at.jku.sea.cloud.Artifact;

public class CloudUtils {
  /**
   * Checks if the provided {@code obj} is allowed in the cloud.
   * <p>
   * Following types are allowed: <code>null</code>, {@link Number}, {@link Character}, {@link String}, {@link Boolean},
   * {@link Artifact}
   * </p>
   * 
   * @param obj
   *          the {@code Object} to be checked
   * @return {@code true} if type of {@code obj} is allowed else {@code false}
   */
  public static boolean isSupportedType(final Object obj) {
    return obj == null || obj instanceof Number || obj instanceof Character || obj instanceof String || obj instanceof Boolean || obj instanceof Artifact;
  }
}
