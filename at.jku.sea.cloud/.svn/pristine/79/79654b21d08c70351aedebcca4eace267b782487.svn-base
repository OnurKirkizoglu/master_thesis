package at.jku.sea.cloud.implementation.events;

import java.util.Collection;
import java.util.Set;

import at.jku.sea.cloud.listeners.DataStorageEventType;

public class EventFactory {

  public static CollectionAddedElementEvent createCollectionAddedElementEvent(final long origin, final long privateVersion, final long owner, final long tool, final long collectionId,
      final Object elem) {
    return new CollectionAddedElementEvent(origin, privateVersion, owner, tool, collectionId, elem);
  }
  
  public static CollectionAddedElementsEvent createCollectionAddedElementsEvent(long origin, Long privateVersion, long owner, long tool, long id, Collection<?> elements) {
    return new CollectionAddedElementsEvent(origin, privateVersion, owner, tool, id, elements);
  }

  public static CollectionRemovedElementEvent createCollectionRemovedElementEvent(final long origin, final long privateVersion, final long owner, final long tool, final long collectionId,
      final Object elem) {
    return new CollectionRemovedElementEvent(origin, privateVersion, owner, tool, collectionId, elem);
  }

  public static MapClearedEvent createMapClearedEvent(final long origin, final long privateVersion, final long owner, final long tool, final long mapId) {
    return new MapClearedEvent(origin, privateVersion, owner, tool, mapId);
  }

  public static MapPutEvent createMapPutEvent(long origin, long privateVersion, long owner, long tool, long mapId, Object key, boolean isKeyReference, Object oldValue, boolean isOldValueReference,
      Object newValue, boolean isNewValueReference, boolean isAdded) {
    return new MapPutEvent(origin, privateVersion, owner, tool, mapId, key, isKeyReference, oldValue, isOldValueReference, newValue, isNewValueReference, isAdded);
  }

  public static MapRemovedElementEvent createMapRemovedElementEvent(long origin, long privateVersion, long owner, long tool, long mapId, Object key, boolean isKeyReference, Object value,
      boolean isValueReference) {
    return new MapRemovedElementEvent(origin, privateVersion, owner, tool, mapId, key, isKeyReference, value, isValueReference);
  }

  public static ArtifactEvent createArtifactEvent(final DataStorageEventType eventType, final long origin, final long privateVersion, final long owner, final long tool, final long id,
      final Long type, final Long packageId, final boolean alive, final boolean isDeceased) {
    return new ArtifactEvent(eventType, origin, privateVersion, owner, tool, id, type, packageId, alive, isDeceased);
  }

  public static ArtifactCommitedEvent createArtifactCommitedEvent(final long privateVersion, final long id, final Long type, final String message, final long version, final long oldBaseVersion) {
    return new ArtifactCommitedEvent(privateVersion, id, type, message, version, oldBaseVersion);
  }

  public static PropertyReferenceSetEvent createPropertyReferenceSetEvent(final Long origin, final Long version, final long owner, final long tool, final long id, final String property,
      final Long oldReferenceId, final Long newReferenceId) {
    return new PropertyReferenceSetEvent(origin, version, owner, tool, id, property, oldReferenceId, newReferenceId);
  }

  public static PropertyValueSetEvent createPropertyValueSetEvent(final Long origin, final Long version, final long owner, final long tool, final long id, final String property,
      final Object oldValue, final Object newValue, final boolean wasReference) {
    return new PropertyValueSetEvent(origin, version, owner, tool, id, property, oldValue, newValue, wasReference);
  }

  public static PropertyAliveSetEvent createPropertyAliveSetEvent(final long origin, final long privateVersion, final long owner, final long tool, final long artifactId, final String property,
      final boolean alive) {
    return new PropertyAliveSetEvent(origin, privateVersion, owner, tool, artifactId, property, alive);
  }

  public static PropertyDeletedEvent createPropertyDeletedEvent(final long origin, final long privateVersion, final long owner, final long tool, final long id, final String property,
      final boolean isDeceased) {
    return new PropertyDeletedEvent(origin, privateVersion, owner, tool, id, property, isDeceased);
  }

  public static PropertyMapSetEvent createPropertyMapSetEvent(final Set<PropertyValueSetEvent> valueSet, final Set<PropertyReferenceSetEvent> referenceSet) {
    return new PropertyMapSetEvent(valueSet, referenceSet);
  }

  public static PropertyCommitedEvent createPropertyCommitedEvent(final long privateVersion, final long id, final String property, final String message, final long version, final long oldBaseVersion) {
    return new PropertyCommitedEvent(privateVersion, id, property, message, version, oldBaseVersion);
  }

  public static VersionDeletedEvent createVersionDeletedEvent(final long privateVersion) {
    return new VersionDeletedEvent(privateVersion);
  }

  public static VersionCommitedEvent createVersionCommitedEvent(final long privateVersion, final String message, final long version, final long oldBaseVersion) {
    return new VersionCommitedEvent(privateVersion, message, version, oldBaseVersion);
  }

  public static PrivateVersionAddedEvent createPrivateVersionAddedEvent(long privateVersion) {
    return new PrivateVersionAddedEvent(privateVersion);
  }

  public static PrivateVersionDeletedEvent createPrivateVersionDeletedEvent(long privateVersion) {
    return new PrivateVersionDeletedEvent(privateVersion);
  }

  public static PrivateVersionRebasedEvent createPrivateVersionRebasedEvent(long origin, long privateVersion, long oldBaseVersion, long newBaseVersion) {
    return new PrivateVersionRebasedEvent(origin, privateVersion, oldBaseVersion, newBaseVersion);
  }

}
