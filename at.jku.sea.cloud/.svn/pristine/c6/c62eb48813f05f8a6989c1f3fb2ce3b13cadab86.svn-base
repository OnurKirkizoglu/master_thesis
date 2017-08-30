package at.jku.sea.cloud.listeners.events;

import java.io.Serializable;

public enum ChangeType implements Serializable {
  ArtifactCreated(1), ArtifactAliveSet(2), ArtifactTypeSet(3), ArtifactContainerSet(3), PropertyValueSet(4), PropertyMapSet(4), PropertyAliveSet(5), CollectionAddedElement(4), CollectionRemovedElement(
      4),MapCleared(4), MapPut(4), MapRemovedElement(4), ArtifactCommited(5), PropertyCommited(5), VersionCommited(5), ArtifactRollback(6), PropertyRollBack(6), WorkspaceRollBack(6), WorkspaceRebased(7), WorkspaceClosed(8), WorkspaceAdded(9), WorkspaceParentSet(10);

  private final int priority;

  ChangeType(final int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return this.priority;
  }
}
