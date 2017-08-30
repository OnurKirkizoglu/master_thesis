package at.jku.sea.cloud.implementation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.DataStorage.Columns;
import at.jku.sea.cloud.PropagationType;

public class WorkspaceHierarchyUtils {

  protected static Set<Object[]> collectAllWorkspacesInInstantPath(AbstractDataStorage storage, final long privateVersion) {
    Set<Object[]> workspaces = new HashSet<>();
    if (privateVersion < DataStorage.VOID_VERSION) {
      Object[] workspace = storage.getWorkspaceRepresentation(privateVersion);
      collectWorkspacesInInstantPath(storage, workspace, new HashSet<Long>(), workspaces);
    }
    return workspaces;
  }

  protected static Set<Long> collectAllWorkspaceIdsInInstantPath(AbstractDataStorage storage, final long privateVersion) {
    Set<Object[]> workspaces = collectAllWorkspacesInInstantPath(storage, privateVersion);
    Set<Long> wsIds = new HashSet<>();
    for (Object[] workspace : workspaces) {
      wsIds.add((Long) workspace[Columns.WORKSPACE_VERSION.getIndex()]);
    }
    return wsIds;
  }

  protected static Set<Long> collectAllChildrenWorkspaceIdsInInstantPath(AbstractDataStorage storage, final long privateVersion) {
    Set<Object[]> workspaces = new HashSet<>();
    Object[] privateWS = storage.getWorkspaceRepresentation(privateVersion);
    Set<Long> visited = new HashSet<>();
    visited.add((Long) privateWS[Columns.WORKSPACE_PARENT.getIndex()]);
    collectWorkspacesInInstantPath(storage, privateWS, visited, workspaces);
    Set<Long> wsIds = new HashSet<>();
    for (Object[] workspace : workspaces) {
      wsIds.add((Long) workspace[Columns.WORKSPACE_VERSION.getIndex()]);
    }
    return wsIds;
  }

  protected static Set<Long> collectAllParentWorkspaceIdsInInstantPath(AbstractDataStorage storage, final long privateVersion, final long childId) {
    Set<Object[]> workspaces = new HashSet<>();
    Object[] privateWS = storage.getWorkspaceRepresentation(privateVersion);
    Set<Long> visited = new HashSet<>();
    visited.add(childId);
    collectWorkspacesInInstantPath(storage, privateWS, visited, workspaces);
    Set<Long> wsIds = new HashSet<>();
    for (Object[] workspace : workspaces) {
      wsIds.add((Long) workspace[Columns.WORKSPACE_VERSION.getIndex()]);
    }
    return wsIds;
  }

  private static void collectWorkspacesInInstantPath(AbstractDataStorage storage, Object[] workspace, Set<Long> visited, Set<Object[]> toUpdate) {
    Long wsId = (Long) workspace[Columns.WORKSPACE_VERSION.getIndex()];
    Long parentId = (Long) workspace[Columns.WORKSPACE_PARENT.getIndex()];

    PropagationType push = (PropagationType) workspace[Columns.WORKSPACE_PUSH.getIndex()];
    Object[] parent = parentId != null ? storage.getWorkspaceRepresentation(parentId) : null;
    Collection<Object[]> children = storage.getWorkspaceChildren(wsId);
    visited.add(wsId);
    if (!ObjectArrayRepresentationUtils.containsWorkspace(toUpdate, wsId)) {
      toUpdate.add(workspace);
    }
    if (push == PropagationType.instant && parentId != null) {
      if (!visited.contains(parentId)) {
        collectWorkspacesInInstantPath(storage, parent, visited, toUpdate);
      }
    }
    for (Object[] child : children) {
      PropagationType pull = (PropagationType) child[Columns.WORKSPACE_PULL.getIndex()];
      Long childId = (Long) child[Columns.WORKSPACE_VERSION.getIndex()];
      if (pull == PropagationType.instant && !visited.contains(childId)) {
        // if (!ObjectArrayRepresentationUtils.containsWorkspace(toUpdate, childId)) {
        // toUpdate.add(child);
        // }
        collectWorkspacesInInstantPath(storage, child, visited, toUpdate);
      }
    }
  }

  protected static Set<Object[]> collectCompleteWorkspaceHierarchy(AbstractDataStorage storage, final long privateVersion) {
    Object[] workspace = storage.getWorkspaceRepresentation(privateVersion);
    Set<Object[]> hierarchy = new HashSet<>();
    collectWorkspaces(storage, workspace, new HashSet<Long>(), hierarchy);
    return hierarchy;
  }

  private static void collectWorkspaces(AbstractDataStorage storage, Object[] workspace, Set<Long> visited, Set<Object[]> hierarchy) {
    Long wsId = (Long) workspace[Columns.WORKSPACE_VERSION.getIndex()];
    Long parentId = (Long) workspace[Columns.WORKSPACE_PARENT.getIndex()];

    visited.add(wsId);
    if (!ObjectArrayRepresentationUtils.containsWorkspace(hierarchy, wsId)) {
      hierarchy.add(workspace);
    }
    if (parentId != null && !visited.contains(parentId)) {
      collectWorkspaces(storage, storage.getWorkspaceRepresentation(parentId), visited, hierarchy);
    }

    Collection<Object[]> children = storage.getWorkspaceChildren(wsId);
    for (Object[] child : children) {
      Long childId = (Long) child[Columns.WORKSPACE_VERSION.getIndex()];
      if (!visited.contains(childId)) {
        collectWorkspaces(storage, child, visited, hierarchy);
      }
    }
  }
}
