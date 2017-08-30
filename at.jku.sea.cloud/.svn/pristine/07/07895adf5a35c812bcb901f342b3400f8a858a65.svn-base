package at.jku.sea.cloud.workspacehierarchy.test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import at.jku.sea.cloud.listeners.WorkspaceAdapter;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactAliveSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCreated;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactContainerSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactTypeSet;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElement;
import at.jku.sea.cloud.listeners.events.artifact.CollectionRemovedElement;
import at.jku.sea.cloud.listeners.events.property.PropertyAliveSet;
import at.jku.sea.cloud.listeners.events.property.PropertyMapSet;
import at.jku.sea.cloud.listeners.events.property.PropertyValueSet;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceParentSet;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRebased;

public class TestListener extends WorkspaceAdapter {

  public List<Change> emittedChanges = new ArrayList<Change>();

  public void artifactCreated(final ArtifactCreated event) throws RemoteException {
    this.emittedChanges.add(event);
  }

  public void artifactAliveSet(final ArtifactAliveSet event) throws RemoteException {
    this.emittedChanges.add(event);
  }

  public void artifactTypeSet(final ArtifactTypeSet event) throws RemoteException {
    this.emittedChanges.add(event);
  }

  public void artifactContainerSet(final ArtifactContainerSet event) throws RemoteException {
    this.emittedChanges.add(event);
  }

  public void propertyAliveSet(final PropertyAliveSet event) throws RemoteException {
    this.emittedChanges.add(event);
  }

  public void propertyValueSet(final PropertyValueSet event) throws RemoteException {
    this.emittedChanges.add(event);
  }

  public void propertyMapSet(final PropertyMapSet event) throws RemoteException {
    this.emittedChanges.add(event);
  }

  public void collectionAddedElement(final CollectionAddedElement event) throws RemoteException {
    this.emittedChanges.add(event);
  }

  public void collectionRemovedElement(final CollectionRemovedElement event) throws RemoteException {
    this.emittedChanges.add(event);
  }

  @Override
  public void workspaceRebased(final WorkspaceRebased event) throws RemoteException {
    this.emittedChanges.add(event);
  }
  
  @Override
  public void workspaceParentSet(WorkspaceParentSet event) throws RemoteException {
    emittedChanges.add(event);
  }
}
