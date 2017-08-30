package at.jku.sea.cloud.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.DataStorage.Columns;
import at.jku.sea.cloud.exceptions.ArtifactNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.PropertyNotPushOrPullableException;
import at.jku.sea.cloud.implementation.events.ArtifactEvent;
import at.jku.sea.cloud.implementation.events.CollectionEvent;
import at.jku.sea.cloud.implementation.events.EventFactory;
import at.jku.sea.cloud.implementation.events.PropertyEvent;
import at.jku.sea.cloud.listeners.DataStorageEventType;

public class DuplicationUtils {
  protected static void duplicateArtifactWithProperties(AbstractDataStorage storage, final long to, final long from, final Object[] artifact, final boolean fireEvent) {
    Long artifactId = (Long) artifact[Columns.ARTIFACT_ID.getIndex()];
    Long owner = (Long) artifact[Columns.OWNER.getIndex()];
    Long tool = (Long) artifact[Columns.TOOL.getIndex()];
    Long type = (Long) artifact[Columns.ARTIFACT_TYPE.getIndex()];
    Long pckg = (Long) artifact[Columns.ARTIFACT_CONTAINER.getIndex()];

    Set<Object[]> properties = storage.getVersionPropertiesOfArtifact(from, artifactId);
    for (Object[] property : properties) {
      Long reference = (Long) property[Columns.PROPERTY_REFERENCE.getIndex()];
      final boolean refOnlyInPrivateVersion = reference != null ? storage.existsArtifact(to, reference) : false;
      if (refOnlyInPrivateVersion) {
        throw new ArtifactNotPushOrPullableException(artifactId);
      }
    }
    boolean isCollection = storage.isCollection(from, artifactId);
    boolean isNewlyCreated = !storage.existsArtifactInVersion(to, artifactId);
    DuplicationUtils.duplicateArtifact(storage, to, from, artifact, isCollection, isNewlyCreated, fireEvent && !isNewlyCreated, true);

    Pattern collElement = Pattern.compile(DataStorage.COLLECTION_ELEMENT_PATTERN);
    Collection<PropertyEvent> propEvents = new ArrayList<>();
    for (Object[] property : properties) {
      String name = (String) property[Columns.PROPERTY_NAME.getIndex()];
      if (!collElement.matcher(name).matches() && !name.equals(DataStorage.PROPERTY_COLLECTION_NEXT_IDX)) {
        propEvents.add(DuplicationUtils.duplicateProperty(storage, to, from, property));
      }
    }
    if (isNewlyCreated) {
      Collection<ArtifactEvent> events = new ArrayList<>();
      events.add(EventFactory.createArtifactEvent(DataStorageEventType.CREATE, from, to, owner, tool, artifactId, type, pckg, true, false));
      storage.fireArtifactEvent(events);
    } else {
      storage.firePropertyEvents(propEvents);
    }
    if (isCollection) {
      storage.fireCollectionEvents(DuplicationUtils.collectionChanged(storage, to, from, artifactId, owner, tool));
    }

  }

  protected static void duplicateArtifact(AbstractDataStorage storage, final long to, final long from, final Object[] artifact, boolean isCollection, boolean createNew, final boolean fireEvent,
      final boolean sanityCheck) {
    Long artifactId = (Long) artifact[Columns.ARTIFACT_ID.getIndex()];
    Long owner = (Long) artifact[Columns.OWNER.getIndex()];
    Long tool = (Long) artifact[Columns.TOOL.getIndex()];
    Long type = (Long) artifact[Columns.ARTIFACT_TYPE.getIndex()];
    Long pckg = (Long) artifact[Columns.ARTIFACT_CONTAINER.getIndex()];
    boolean alive = (boolean) artifact[Columns.ALIVE.getIndex()];
    if (sanityCheck && ((!storage.existsArtifact(to, type)) || (pckg != null && !storage.existsArtifact(to, pckg)))) {
      throw new ArtifactNotPushOrPullableException(artifactId);
    }

    if (createNew) {
      storage.storeArtifact(to, owner, tool, artifactId, type, pckg, alive);
      if (isCollection) {
        storage.storeProperty(to, owner, tool, artifactId, DataStorage.PROPERTY_COLLECTION_NEXT_IDX, 0l, false, true);
        storage.storeProperty(to, owner, tool, artifactId, DataStorage.PROPERTY_COLLECTION_REFERENCES, storage.isReferenceCollection(from, artifactId), false, true);
      }
      if (fireEvent) {
        Collection<ArtifactEvent> events = new ArrayList<>();
        events.add(EventFactory.createArtifactEvent(DataStorageEventType.CREATE, from, to, owner, tool, artifactId, type, pckg, true, false));
        storage.fireArtifactEvent(events);
      }
    } else {
      Object[] pArtifact = storage.getArtifactRepresentation(to, artifactId);
      storage.storeArtifact(to, owner, tool, artifactId, type, pckg, alive);
      Collection<ArtifactEvent> events = new ArrayList<>();
      if (type != (Long) pArtifact[Columns.ARTIFACT_TYPE.getIndex()]) {
        events.add(EventFactory.createArtifactEvent(DataStorageEventType.TYPE_SET, from, to, owner, tool, artifactId, type, pckg, alive, false));
      }
      if (pckg != (Long) pArtifact[Columns.ARTIFACT_CONTAINER.getIndex()]) {
        events.add(EventFactory.createArtifactEvent(DataStorageEventType.CONTAINER_SET, from, to, owner, tool, artifactId, type, pckg, alive, false));
      }
      if (alive != (boolean) pArtifact[Columns.ALIVE.getIndex()]) {
        events.add(EventFactory.createArtifactEvent(DataStorageEventType.ALIVE_SET, from, to, owner, tool, artifactId, type, pckg, alive, false));
      }
      storage.fireArtifactEvent(events);
    }

  }

  protected static Collection<CollectionEvent> collectionChanged(AbstractDataStorage storage, final long to, final long from, Long artifactId, Long owner, Long tool) {
    Collection<Object> toCollection = storage.getElementsFromCollection(to, artifactId);
    Collection<Object> fromCollection = storage.getElementsFromCollection(from, artifactId);
    Collection<Object> intersect = new ArrayList<>(toCollection);
    intersect.retainAll(fromCollection);
    toCollection.removeAll(intersect);
    fromCollection.removeAll(intersect);
    Collection<CollectionEvent> events = new ArrayList<>();
    for (Object element : toCollection) {
      // this.removeElemFromColl(from, versions, artifactId, owner, tool, element);
      final Long index = storage.getIndexOfElementInCollection(to, artifactId, element);
      if (index == -1) {
        continue;
      }
      Object deleted = storage.removeElementAt(to, artifactId, owner, tool, index);
      events.add(EventFactory.createCollectionRemovedElementEvent(from, to, owner, tool, artifactId, deleted));
    }
    for (Object element : fromCollection) {
      // this.addElemToColl(from, versions, artifactId, owner, tool, element);
      storage.addElement(to, artifactId, owner, tool, element);
      events.add(EventFactory.createCollectionAddedElementEvent(from, to, owner, tool, artifactId, element));
    }
    return events;
  }

  protected static PropertyEvent duplicateProperty(AbstractDataStorage storage, final long to, final long from, final Object[] property) throws PropertyNotPushOrPullableException {
    Long artifactId = (Long) property[Columns.ARTIFACT_ID.getIndex()];
    Long owner = (Long) property[Columns.OWNER.getIndex()];
    Long tool = (Long) property[Columns.TOOL.getIndex()];
    String propName = (String) property[Columns.PROPERTY_NAME.getIndex()];
    Long reference = (Long) property[Columns.PROPERTY_REFERENCE.getIndex()];
    boolean isReference = reference != null;
    Object value = isReference ? reference : property[Columns.PROPERTY_VALUE.getIndex()];
    boolean alive = (boolean) property[Columns.ALIVE.getIndex()];
    PropertyEvent event = null;
    if (!storage.existsArtifact(to, artifactId) || (reference != null && !storage.existsArtifact(to, reference))) {
      throw new PropertyNotPushOrPullableException(artifactId, propName);
    }
    if (storage.existsPropertyInVersion(to, artifactId, propName) && (alive != (boolean) storage.getPropertyRepresentation(to, artifactId, propName)[Columns.ALIVE.getIndex()])) {
      // this.setPropertyAlive(to, owner, tool, artifactId, propName, alive);
      event = EventFactory.createPropertyAliveSetEvent(from, to, owner, tool, artifactId, propName, alive);
    } else {
      Object[] oldProp = null;
      if (storage.hasProperty(to, artifactId, propName)) {
        oldProp = storage.getPropertyRepresentation(to, artifactId, propName);
      }
      boolean wasReference = oldProp != null ? oldProp[Columns.PROPERTY_REFERENCE.getIndex()] != null : false;
      Long oldReferenceId = (Long) (wasReference ? oldProp[Columns.PROPERTY_REFERENCE.getIndex()] : null);
      Object oldValue = (oldProp != null ? oldProp[Columns.PROPERTY_VALUE.getIndex()] : null);
      if (isReference) {
        event = EventFactory.createPropertyReferenceSetEvent(from, to, owner, tool, artifactId, propName, oldReferenceId, reference);
      } else {
        event = EventFactory.createPropertyValueSetEvent(from, to, owner, tool, artifactId, propName, oldValue, value, wasReference);
      }
    }
    storage.storeProperty(to, owner, tool, artifactId, propName, value, isReference, alive);
    return event;
  }

  protected static boolean contains(Set<Object[]> artifacts, Long artifactId) {
    for (Object[] itArtifact : artifacts) {
      Long itArtifactId = (Long) itArtifact[Columns.ARTIFACT_ID.getIndex()];
      if (Objects.equals(artifactId, itArtifactId)) {
        return true;
      }
    }
    return false;
  }

  protected static void duplicateAll(AbstractDataStorage storage, final long to, final long from) {
    Set<Object[]> wsArtifacts = storage.getVersionArtifacts(from);
    Set<Object[]> wsProperties = storage.getVersionProperties(from);
    Map<Long, Set<Object[]>> workspaceProperties = new HashMap<>();
    for (Object[] artifact : wsArtifacts) {
      Long artifactId = (Long) artifact[Columns.ARTIFACT_ID.getIndex()];
      workspaceProperties.put(artifactId, new HashSet<Object[]>());
    }

    Pattern collElement = Pattern.compile(DataStorage.COLLECTION_ELEMENT_PATTERN);
    Set<Long> created = new HashSet<>();
    for (Object[] artifact : wsArtifacts) {
      Long artifactId = (Long) artifact[Columns.ARTIFACT_ID.getIndex()];
      boolean isArtifactCreated = !storage.existsArtifactInVersion(to, artifactId);
      boolean isCollection = storage.isCollection(from, artifactId);
      if (isArtifactCreated) {
        created.add(artifactId);
      }
      DuplicationUtils.duplicateArtifact(storage, to, from, artifact, isCollection, isArtifactCreated, !isArtifactCreated, false);
    }
    for (Object[] property : wsProperties) {
      Long artifactId = (Long) property[Columns.ARTIFACT_ID.getIndex()];
      if (!contains(wsArtifacts, artifactId)) {
        wsArtifacts.add(storage.getArtifactRepresentation(from, artifactId));
      }
      if (!workspaceProperties.containsKey(artifactId)) {
        workspaceProperties.put(artifactId, new HashSet<Object[]>());
      }
      workspaceProperties.get(artifactId).add(property);
    }

    for (Object[] artifact : wsArtifacts) {
      Long artifactId = (Long) artifact[Columns.ARTIFACT_ID.getIndex()];
      Long type = (Long) artifact[Columns.ARTIFACT_TYPE.getIndex()];
      Long owner = (Long) artifact[Columns.OWNER.getIndex()];
      Long tool = (Long) artifact[Columns.TOOL.getIndex()];
      Long pckg = (Long) artifact[Columns.ARTIFACT_CONTAINER.getIndex()];
      boolean isCollection = storage.isCollection(from, artifactId);
      Set<Object[]> properties = workspaceProperties.get(artifactId);
      Collection<PropertyEvent> events = new ArrayList<>();

      for (Object[] property : properties) {
        String propName = (String) property[Columns.PROPERTY_NAME.getIndex()];
        if (!collElement.matcher(propName).matches() && !propName.equals(DataStorage.PROPERTY_COLLECTION_NEXT_IDX) && !propName.equals(DataStorage.PROPERTY_COLLECTION_REFERENCES)) {
          events.add(DuplicationUtils.duplicateProperty(storage, to, from, property));
        }
      }
      if (created.contains(artifactId)) {
        ArtifactEvent event = EventFactory.createArtifactEvent(DataStorageEventType.CREATE, from, to, owner, tool, artifactId, type, pckg, true, false);
        storage.fireArtifactEvent(Arrays.asList(new ArtifactEvent[] { event }));
      } else {
        storage.firePropertyEvents(events);
      }

      if (isCollection) {
        storage.fireCollectionEvents(DuplicationUtils.collectionChanged(storage, to, from, artifactId, owner, tool));
      }
    }
  }
}
