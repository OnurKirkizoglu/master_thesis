package at.jku.sea.cloud.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;

public class WorkspaceUtils {

  /**
   * 
   * @param workspace
   * @param propertyValues
   *          {@link Map} mapping property names to desired values. Must not be <code>null</code>
   * @param filters
   *          as in {@link Workspace#getArtifactsWithProperty(String, Object, Artifact...)}
   * @return
   * @throws WorkspaceExpiredException
   * @throws IllegalArgumentException
   *           if propertyValues is empty
   */
  public Set<Artifact> getArtifactsWithProperties(final Workspace workspace, final Map<String, Object> propertyValues, final Artifact... filters) throws WorkspaceExpiredException,
      IllegalArgumentException {
    final Iterator<Entry<String, Object>> iterator = propertyValues.entrySet().iterator();
    if (iterator.hasNext()) {
      throw new IllegalArgumentException("no propertties given");
    }
    Entry<String, Object> entry = iterator.next();
    final Set<Artifact> artifacts = new HashSet<>(workspace.getArtifactsWithProperty(entry.getKey(), entry.getValue(), filters));
    while (iterator.hasNext()) {
      entry = iterator.next();
      artifacts.retainAll(workspace.getArtifactsWithProperty(entry.getKey(), entry.getValue(), filters));
    }
    return artifacts;
  }
}
