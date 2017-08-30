package at.jku.sea.cloud.listeners;

import java.rmi.Remote;
import java.rmi.RemoteException;
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

public interface DataStorageListener extends Remote {
  public void artifactEvent(final Collection<ArtifactEvent> events) throws RemoteException;

  public void collectionAddedElement(final Collection<CollectionAddedElementEvent> events) throws RemoteException;
  
  public void collectionAddedElements(final Collection<CollectionAddedElementsEvent> events) throws RemoteException;

  public void collectionRemovedElement(final Collection<CollectionRemovedElementEvent> events) throws RemoteException;
  
  public void mapCleared(final Collection<MapClearedEvent> events) throws RemoteException;
  
  public void mapPut(final Collection<MapPutEvent> events) throws RemoteException;
  
  public void mapRemovedElement(final Collection<MapRemovedElementEvent> events) throws RemoteException;

  public void artifactCommited(final ArtifactCommitedEvent event) throws RemoteException;

  public void propertyAliveSet(final Collection<PropertyAliveSetEvent> events) throws RemoteException;

  public void propertyReferenceSet(final Collection<PropertyReferenceSetEvent> events) throws RemoteException;

  public void propertyValueSet(final Collection<PropertyValueSetEvent> events) throws RemoteException;

  public void propertyMapsSet(final Collection<PropertyMapSetEvent> events)
      throws RemoteException;

  public void propertyCommited(final PropertyCommitedEvent event) throws RemoteException;

  public void propertyDeleted(final Collection<PropertyDeletedEvent> events) throws RemoteException;

  public void versionCommited(final VersionCommitedEvent event) throws RemoteException;

  public void versionDeleted(final VersionDeletedEvent event) throws RemoteException;

  public void privateVersionDeleted(final PrivateVersionDeletedEvent event) throws RemoteException;

  public void privateVersionAdded(final PrivateVersionAddedEvent event) throws RemoteException;

  public void privateVersionRebased(final PrivateVersionRebasedEvent event) throws RemoteException;

  public void privateVersionParentSet(final PrivateVersionParentSetEvent event) throws RemoteException;
}
