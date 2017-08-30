package at.jku.sea.cloud.listeners;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import at.jku.sea.cloud.implementation.events.ArtifactCommitedEvent;
import at.jku.sea.cloud.implementation.events.ArtifactEvent;
import at.jku.sea.cloud.implementation.events.CollectionAddedElementEvent;
import at.jku.sea.cloud.implementation.events.CollectionAddedElementsEvent;
import at.jku.sea.cloud.implementation.events.CollectionRemovedElementEvent;
import at.jku.sea.cloud.implementation.events.MapClearedEvent;
import at.jku.sea.cloud.implementation.events.MapPutEvent;
import at.jku.sea.cloud.implementation.events.MapRemovedElementEvent;
import at.jku.sea.cloud.implementation.events.PrivateVersionAddedEvent;
import at.jku.sea.cloud.implementation.events.PrivateVersionDeletedEvent;
import at.jku.sea.cloud.implementation.events.PrivateVersionParentSetEvent;
import at.jku.sea.cloud.implementation.events.PrivateVersionRebasedEvent;
import at.jku.sea.cloud.implementation.events.PropertyAliveSetEvent;
import at.jku.sea.cloud.implementation.events.PropertyCommitedEvent;
import at.jku.sea.cloud.implementation.events.PropertyDeletedEvent;
import at.jku.sea.cloud.implementation.events.PropertyMapSetEvent;
import at.jku.sea.cloud.implementation.events.PropertyReferenceSetEvent;
import at.jku.sea.cloud.implementation.events.PropertyValueSetEvent;
import at.jku.sea.cloud.implementation.events.VersionCommitedEvent;
import at.jku.sea.cloud.implementation.events.VersionDeletedEvent;

public class RemoteDataStorageListenerWrapper extends UnicastRemoteObject implements DataStorageListener {

  private final DataStorageListener listener;

  public RemoteDataStorageListenerWrapper(final DataStorageListener listener) throws RemoteException {
    super();
    this.listener = listener;
  }

  private static final long serialVersionUID = 1L;

  @Override
  public void artifactEvent(Collection<ArtifactEvent> event) throws RemoteException {
    this.listener.artifactEvent(event);
  }

  @Override
  public void collectionAddedElement(Collection<CollectionAddedElementEvent> event) throws RemoteException {
    this.listener.collectionAddedElement(event);
  }
  
  @Override
  public void collectionAddedElements(Collection<CollectionAddedElementsEvent> events) throws RemoteException {
    this.listener.collectionAddedElements(events);
  }

  @Override
  public void collectionRemovedElement(Collection<CollectionRemovedElementEvent> event) throws RemoteException {
    this.listener.collectionRemovedElement(event);
  }
  
  @Override
  public void mapCleared(Collection<MapClearedEvent> event) throws RemoteException {
    this.listener.mapCleared(event);
  }
  
  @Override
  public void mapPut(Collection<MapPutEvent> event) throws RemoteException {
    this.listener.mapPut(event);
  }
  
  @Override
  public void mapRemovedElement(Collection<MapRemovedElementEvent> event) throws RemoteException {
    this.listener.mapRemovedElement(event);
  }

  @Override
  public void artifactCommited(ArtifactCommitedEvent event) throws RemoteException {
    this.listener.artifactCommited(event);
  }

  @Override
  public void propertyAliveSet(Collection<PropertyAliveSetEvent> event) throws RemoteException {
    this.listener.propertyAliveSet(event);
  }

  @Override
  public void propertyReferenceSet(Collection<PropertyReferenceSetEvent> event) throws RemoteException {
    this.listener.propertyReferenceSet(event);
  }

  @Override
  public void propertyValueSet(Collection<PropertyValueSetEvent> event) throws RemoteException {
    this.listener.propertyValueSet(event);
  }

  @Override
  public void propertyMapsSet(Collection<PropertyMapSetEvent> event) throws RemoteException {
    this.listener.propertyMapsSet(event);

  }

  @Override
  public void propertyCommited(PropertyCommitedEvent event) throws RemoteException {
    this.listener.propertyCommited(event);
  }

  @Override
  public void versionCommited(VersionCommitedEvent event) throws RemoteException {
    this.listener.versionCommited(event);
  }

  @Override
  public void propertyDeleted(Collection<PropertyDeletedEvent> event) throws RemoteException {
    this.listener.propertyDeleted(event);

  }

  @Override
  public void versionDeleted(VersionDeletedEvent event) throws RemoteException {
    this.listener.versionDeleted(event);
  }

  @Override
  public void privateVersionDeleted(PrivateVersionDeletedEvent event) throws RemoteException {
    this.listener.privateVersionDeleted(event);
  }

  @Override
  public void privateVersionAdded(PrivateVersionAddedEvent event) throws RemoteException {
    this.listener.privateVersionAdded(event);
  }

  @Override
  public void privateVersionRebased(PrivateVersionRebasedEvent event) throws RemoteException {
    this.listener.privateVersionRebased(event);
  }

  @Override
  public void privateVersionParentSet(PrivateVersionParentSetEvent event) throws RemoteException {
    listener.privateVersionParentSet(event);
  }

}
