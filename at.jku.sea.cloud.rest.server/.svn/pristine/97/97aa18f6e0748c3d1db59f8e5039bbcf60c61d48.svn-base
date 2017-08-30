package at.jku.sea.cloud.rest.server.listeners;

import java.rmi.RemoteException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.jku.sea.cloud.listeners.WorkspaceListener;
import at.jku.sea.cloud.listeners.events.Change;
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
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceClosed;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceParentSet;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRebased;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRollBack;

public class WorkspaceBufferListener implements WorkspaceListener {

  Queue<Change> eventBuffer = new ConcurrentLinkedQueue<Change>();

  public Queue<Change> getEvents() {
    Queue<Change> result = eventBuffer;
    eventBuffer = new ConcurrentLinkedQueue<Change>();
    return result;
  }

  @Override
  public void artifactCreated(ArtifactCreated event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void artifactAliveSet(ArtifactAliveSet event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void artifactTypeSet(ArtifactTypeSet event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void artifactContainerSet(ArtifactContainerSet event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void propertyAliveSet(PropertyAliveSet event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void propertyValueSet(PropertyValueSet event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void propertyMapSet(PropertyMapSet event) throws RemoteException {
    eventBuffer.offer(event);

  }

  @Override
  public void collectionAddedElement(CollectionAddedElement event) throws RemoteException {
    eventBuffer.offer(event);
  }
  
  @Override
  public void collectionAddedElements(CollectionAddedElements event) throws RemoteException {
    eventBuffer.offer(event);
  }


  @Override
  public void collectionRemovedElement(CollectionRemovedElement event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void mapCleared(MapCleared event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void mapElementRemoved(MapElementRemoved event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void putMap(MapPut event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void artifactCommited(ArtifactCommited event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void propertyCommited(PropertyCommited event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void versionCommited(VersionCommited event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void artifactRollback(ArtifactRollBack event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void propertyRollback(PropertyRollBack event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void workspaceRollback(WorkspaceRollBack event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void workspaceRebased(WorkspaceRebased event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void workspaceClosed(WorkspaceClosed event) throws RemoteException {
    eventBuffer.offer(event);
  }

  @Override
  public void workspaceParentSet(WorkspaceParentSet event) throws RemoteException {
    eventBuffer.offer(event);
  }
}
