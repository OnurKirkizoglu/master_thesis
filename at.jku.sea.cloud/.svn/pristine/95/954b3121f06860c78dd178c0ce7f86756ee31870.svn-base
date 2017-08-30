package at.jku.sea.cloud.listeners;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class DataStorageAdapter implements DataStorageListener, Serializable {
  private static final long serialVersionUID = 1L;
  private static Logger logger = LoggerFactory.getLogger(DataStorageAdapter.class);

  private final boolean log;

  public DataStorageAdapter() {
    this(false);
  }

  public DataStorageAdapter(final boolean log) {
    super();
    this.log = log;
  }

  @Override
  public void artifactEvent(Collection<ArtifactEvent> events) throws RemoteException {
    if (this.log) {
      for (ArtifactEvent event : events) {
        logger.debug("DataStorageAdapter: artifactEvent(eventType={}, version={}, owner={}, tool={}, id={}, type={}, packageId={}, alive={}, isDeceased={})", new Object[] { event.eventType,
            event.privateVersion, event.owner, event.tool, event.id, event.type, event.packageId, event.alive, event.isDeceased });
      }
    }
  }

  @Override
  public void collectionAddedElement(Collection<CollectionAddedElementEvent> events) throws RemoteException {
    if (this.log) {
      for (CollectionAddedElementEvent event : events) {
        logger.debug("DataStorageAdapter: collectionAddedElement(version={}, owner={}, tool={}, collectionId={}, elem={})", new Object[] { event.privateVersion, event.owner, event.tool,
            event.collectionId, event.elem });
      }
    }
  }

  @Override
  public void collectionAddedElements(Collection<CollectionAddedElementsEvent> events) throws RemoteException {
    if (this.log) {
      for (CollectionAddedElementsEvent event : events) {
        logger.debug("DataStorageAdapter: collectionAddedElement(version={}, owner={}, tool={}, collectionId={}, elem={})", new Object[] { event.privateVersion, event.owner, event.tool,
            event.collectionId, event.elem });
      }
    }
  }
  
  @Override
  public void collectionRemovedElement(Collection<CollectionRemovedElementEvent> events) throws RemoteException {
    if (this.log) {
      for (CollectionRemovedElementEvent event : events) {
        logger.debug("DataStorageAdapter: collectionRemovedElement(version={}, owner={}, tool={}, collectionId={}, elem={})", new Object[] { event.privateVersion, event.owner, event.tool,
            event.collectionId, event.elem });
      }
    }
  }

  @Override
  public void mapCleared(Collection<MapClearedEvent> events) throws RemoteException {
    if (this.log) {
      for (MapClearedEvent event : events) {
        logger.debug("DataStorageAdapter: mapCleared(version={}, owner={}, tool={}, mapId={})", new Object[] { event.privateVersion, event.owner, event.tool, event.mapId });
      }
    }
  }

  @Override
  public void mapPut(Collection<MapPutEvent> events) throws RemoteException {
    if (this.log) {
      for (MapPutEvent event : events) {
        logger.debug("DataStorageAdapter: mapPut(version={}, owner={}, tool={}, mapId={}, key={}, keyReference={}, oldValue={}, oldValueReference={}, newValue={}, newValueReference={}, isAdded={})",
            new Object[] { event.privateVersion, event.owner, event.tool, event.mapId, event.key, event.isKeyReference, event.oldValue, event.isOldValueReference, event.newValue,
                event.isNewValueReference, event.isAdded });
      }
    }
  }

  @Override
  public void mapRemovedElement(Collection<MapRemovedElementEvent> events) throws RemoteException {
    if (this.log) {
      for (MapRemovedElementEvent event : events) {
        logger.debug("DataStorageAdapter: mapRemovedElement(version={}, owner={}, tool={}, mapId={}, key={}, keyReference={}, value={}, valueReference={})", new Object[] { event.privateVersion,
            event.owner, event.tool, event.mapId, event.key, event.isKeyReference, event.value, event.isValueReference });
      }
    }
  }

  @Override
  public void artifactCommited(ArtifactCommitedEvent event) throws RemoteException {
    if (this.log) {
      logger.debug("DataStorageAdapter: artifactCommited(privateVersion={}, id={}, type={}, message={}, version={})", new Object[] { event.privateVersion, event.id, event.type, event.message,
          event.version });
    }
  }

  @Override
  public void propertyAliveSet(Collection<PropertyAliveSetEvent> events) {
    if (this.log) {
      for (PropertyAliveSetEvent event : events) {
        logger.debug("DataStorageAdapter: propertyAliveSet(version={}, owner={}, tool={}, artifactId={}, property={}, alive={})", new Object[] { event.version, event.owner, event.tool,
            event.artifactId, event.propertyName, event.alive });
      }
    }
  }

  @Override
  public void propertyReferenceSet(Collection<PropertyReferenceSetEvent> events) {
    if (this.log) {
      for (PropertyReferenceSetEvent event : events) {
        logger.debug("DataStorageAdapter: propertyReferenceSet(version={}, owner={}, tool={}, id={}, property={}, oldReferenceId={}, newReferenceId={})", new Object[] { event.version, event.owner,
            event.tool, event.artifactId, event.propertyName, event.oldReferenceId, event.newReferenceId });

      }
    }
  }

  @Override
  public void propertyValueSet(Collection<PropertyValueSetEvent> events) {
    if (this.log) {
      for (PropertyValueSetEvent event : events) {
        logger.debug("DataStorageAdapter: propertyValueSet(version={}, owner={}, tool={}, id={}, property={}, oldValue={}, newValue={}, wasReference={})", new Object[] { event.origin, event.owner,
            event.tool, event.artifactId, event.propertyName, event.oldValue, event.value });
      }
    }
  }

  @Override
  public void propertyMapsSet(Collection<PropertyMapSetEvent> events) throws RemoteException {
    if (this.log) {
      for (PropertyMapSetEvent event : events) {
        logger.debug("DataStorageAdapter: propertyMapsSet(valueMap={}, referenceMap={}", new Object[] { event.valueSet, event.referenceSet });
      }
    }
  }

  @Override
  public void propertyCommited(PropertyCommitedEvent event) {
    if (this.log) {
      logger.debug("DataStorageAdapter: propertyCommited(privateVersion={}, id={}, property={}, message={}, version={})", new Object[] { event.privateVersion, event.id, event.property, event.message,
          event.version });
    }
  }

  @Override
  public void versionCommited(VersionCommitedEvent event) {
    if (this.log) {
      logger.debug("DataStorageAdapter: versionCommited(privateVersion={}, message={}, version={})", new Object[] { event.privateVersion, event.message, event.version });
    }
  }

  @Override
  public void propertyDeleted(Collection<PropertyDeletedEvent> events) {
    if (this.log) {
      for (PropertyDeletedEvent event : events) {
        logger.debug("DataStorageAdapter: propertyDeleted(version={}, id={}, property={})", new Object[] { event.version, event.artifactId, event.propertyName });
      }
    }
  }

  @Override
  public void versionDeleted(VersionDeletedEvent event) {
    if (this.log) {
      logger.debug("DataStorageAdapter: versionDeleted(version={})", new Object[] { event.privateVersion });
    }
  }

  @Override
  public void privateVersionDeleted(PrivateVersionDeletedEvent event) {
    if (this.log) {
      logger.debug("DataStorageAdapter: privateVersionDeleted(privateVersion={})", new Object[] { event.privateVersion });
    }
  }

  @Override
  public void privateVersionAdded(PrivateVersionAddedEvent event) {
    if (this.log) {
      logger.debug("DataStorageAdapter: privateVersionAdded(privateVersion={})", new Object[] { event.privateVersion });
    }
  }

  @Override
  public void privateVersionRebased(PrivateVersionRebasedEvent event) throws RemoteException {
    if (this.log) {
      logger.debug("DataStorageAdapter: privateVersionRebased(privateVersion={}, oldBaseVersion={}, newBaseVersion={})", new Object[] { event.privateVersion, event.oldBaseVersion,
          event.newBaseVersion });
    }
  }

  @Override
  public void privateVersionParentSet(PrivateVersionParentSetEvent event) throws RemoteException {
    if (this.log) {
      logger.debug("DataStorageAdapter: privateVersionSetParent(privateVersion={})", new Object[] { event.privateVersion, event.newParent });
    }
  }

}
