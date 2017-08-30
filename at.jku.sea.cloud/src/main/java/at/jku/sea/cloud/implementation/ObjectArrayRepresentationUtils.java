package at.jku.sea.cloud.implementation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import at.jku.sea.cloud.DataStorage.Columns;

public class ObjectArrayRepresentationUtils {
 
  public static void retainAllArtifacts(Set<Object[]> intersect, Set<Object[]> artifacts) {
    Iterator<Object[]> itIntersect = intersect.iterator();
    while (itIntersect.hasNext()) {
      Object[] aIntersect = itIntersect.next();
      Long aIntId = (Long) aIntersect[Columns.ARTIFACT_ID.getIndex()];
      Iterator<Object[]> it = artifacts.iterator();
      boolean found = false;
      while (it.hasNext()) {
        Object[] next = it.next();
        Long nextId = (Long) next[Columns.ARTIFACT_ID.getIndex()];
        if (nextId.equals(aIntId)) {
          found = true;
          break;
        }
      }
      if (!found) {
        itIntersect.remove();
      }
    }
  }

  public static void retainAll(Set<Object[]> artifacts, Set<Long> ids) {
    Iterator<Object[]> it = artifacts.iterator();
    while (it.hasNext()) {
      Object[] next = it.next();
      Long artifactId = (Long) next[Columns.ARTIFACT_ID.getIndex()];
      if (!ids.contains(artifactId)) {
        it.remove();
      }
    }
  }
  
  
  public static Set<Long> getArtifactIds(Set<Object[]> artifacts) {
    Set<Long> ids = new HashSet<>();
    for (Object[] artifact : artifacts) {
      ids.add((Long) artifact[Columns.ARTIFACT_ID.getIndex()]);
    }
    return ids;
  }

  public static boolean containsWorkspace(Set<Object[]> hierarchy, Long toFindWs) {
    for (Object[] ws : hierarchy) {
      Long wsId = (Long) ws[Columns.WORKSPACE_VERSION.getIndex()];
      if (wsId.equals(toFindWs)) {
        return true;
      }
    }
    return false;
  }

  public static Set<Long> getWorkspaceIds(Collection<Object[]> hierarchy) {
    Set<Long> wsIds = new HashSet<>();
    for (Object[] ws : hierarchy) {
      wsIds.add((Long) ws[Columns.WORKSPACE_VERSION.getIndex()]);
    }
    return wsIds;
  }

}
