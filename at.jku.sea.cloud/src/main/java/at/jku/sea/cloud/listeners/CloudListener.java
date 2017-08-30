package at.jku.sea.cloud.listeners;

import java.rmi.Remote;
import java.rmi.RemoteException;

import at.jku.sea.cloud.listeners.events.artifact.ArtifactAliveSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCommited;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCreated;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactContainerSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactRollBack;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactTypeSet;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElement;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElements;
import at.jku.sea.cloud.listeners.events.artifact.CollectionRemovedElement;
import at.jku.sea.cloud.listeners.events.artifact.MapCleared;
import at.jku.sea.cloud.listeners.events.artifact.MapElementRemoved;
import at.jku.sea.cloud.listeners.events.artifact.MapPut;
import at.jku.sea.cloud.listeners.events.property.PropertyAliveSet;
import at.jku.sea.cloud.listeners.events.property.PropertyCommited;
import at.jku.sea.cloud.listeners.events.property.PropertyMapSet;
import at.jku.sea.cloud.listeners.events.property.PropertyRollBack;
import at.jku.sea.cloud.listeners.events.property.PropertyValueSet;
import at.jku.sea.cloud.listeners.events.workspace.VersionCommited;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceAdded;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceClosed;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceParentSet;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRebased;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRollBack;

public interface CloudListener extends Remote {
  public void artifactCreated(final ArtifactCreated event) throws RemoteException;

  public void artifactAliveSet(final ArtifactAliveSet event) throws RemoteException;

  public void artifactTypeSet(final ArtifactTypeSet event) throws RemoteException;

  public void artifactContainerSet(final ArtifactContainerSet event) throws RemoteException;

  public void propertyAliveSet(final PropertyAliveSet event) throws RemoteException;

  public void propertyValueSet(final PropertyValueSet event) throws RemoteException;

  public void propertyMapSet(final PropertyMapSet event) throws RemoteException;

  public void collectionAddedElement(final CollectionAddedElement event) throws RemoteException;
  
  public void collectionAddedElements(final CollectionAddedElements event) throws RemoteException;

  public void collectionRemovedElement(final CollectionRemovedElement event) throws RemoteException;
  //TODO: MAP | put, clear, remove
  public void mapCleared(final MapCleared event) throws RemoteException;
  
  public void putMap(final MapPut event) throws RemoteException;
  
  public void mapElementRemoved(final MapElementRemoved event) throws RemoteException;

  public void artifactCommited(final ArtifactCommited event) throws RemoteException;

  public void propertyCommited(final PropertyCommited event) throws RemoteException;

  public void versionCommited(final VersionCommited event) throws RemoteException;

  public void artifactRollback(final ArtifactRollBack event) throws RemoteException;

  public void propertyRollback(final PropertyRollBack event) throws RemoteException;

  public void workspaceRollback(final WorkspaceRollBack event) throws RemoteException;

  public void workspaceRebased(final WorkspaceRebased event) throws RemoteException;

  public void workspaceAdded(final WorkspaceAdded event) throws RemoteException;

  public void workspaceClosed(final WorkspaceClosed event) throws RemoteException;

  public void workspaceParentSet(final WorkspaceParentSet event) throws RemoteException;
}
