/*
 * (C) Johannes Kepler University Linz, Austria, 2005-2013
 * Institute for Systems Engineering and Automation (SEA)
 *
 * The software may only be used for academic purposes (teaching, scientific
 * research). Any redistribution or commercialization of the software program
 * and documentation (or any part thereof) requires prior written permission of
 * the JKU. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * This software program and documentation are copyrighted by Johannes Kepler
 * University Linz, Austria (the JKU). The software program and documentation
 * are supplied AS IS, without any accompanying services from the JKU. The JKU
 * does not warrant that the operation of the program will be uninterrupted or
 * error-free. The end-user understands that the program was developed for
 * research purposes and is advised not to rely exclusively on the program for
 * any reason.
 *
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
 * SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
 * ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE
 * AUTHOR HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. THE AUTHOR
 * SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE AUTHOR HAS
 * NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 */

/*
 * DefaultWorkspace.java created on 14.03.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.implementation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.DataStorage.Columns;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactConflictException;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MetaModelDoesNotExistException;
import at.jku.sea.cloud.exceptions.PackageDoesNotExistException;
import at.jku.sea.cloud.exceptions.ProjectDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyConflictException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyNotCommitableException;
import at.jku.sea.cloud.exceptions.PropertyNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.VersionConflictException;
import at.jku.sea.cloud.exceptions.WorkspaceConflictException;
import at.jku.sea.cloud.exceptions.WorkspaceCycleException;
import at.jku.sea.cloud.exceptions.WorkspaceEmptyException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.exceptions.WorkspaceNotEmptyException;
import at.jku.sea.cloud.listeners.CloudListener;
import at.jku.sea.cloud.listeners.WorkspaceListener;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.ChangeType;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactAliveSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCommited;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactContainerSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCreated;
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
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import at.jku.sea.cloud.utils.ArtifactUtils;

/**
 * @author alexander noehrer
 */
class DefaultWorkspace implements Workspace, CloudListener, Serializable {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(DefaultWorkspace.class);

  private transient Cloud cloud;
  private transient DataStorage dataStorage;
  private final long privateVersionId;
  private long baseVersion;
  private boolean workspaceExpired = false;
  private final Owner owner;
  private final Tool tool;
  private final String identifier;
  private final List<WorkspaceListener> listeners;

  private final Pattern collElement = Pattern.compile(DataStorage.COLLECTION_ELEMENT_PATTERN);

  // WorkspaceExpired Exception
  /**
   * @param cloud
   * @param owner
   * @param identifier
   */
  public DefaultWorkspace(final DefaultCloud cloud, final DataStorage dataStorage, final Owner owner, final Tool tool, final String identifier, final long baseVersion, final long privateVersionId) {
    super();
    if (cloud == null) {
      throw new IllegalArgumentException("cloud == null");
    }
    if (owner == null) {
      throw new IllegalArgumentException("owner == null");
    }
    if (tool == null) {
      throw new IllegalArgumentException("tool == null");
    }
    this.cloud = cloud;
    this.dataStorage = dataStorage;
    this.owner = owner;
    this.tool = tool;
    this.identifier = identifier;
    this.baseVersion = baseVersion;
    this.privateVersionId = privateVersionId;
    this.listeners = new CopyOnWriteArrayList<>();// new ArrayList<WorkspaceListener>(3);
    if (this.cloud.getClass().getName().startsWith("com.sun.proxy")) {
      logger.error("FIXME: no remote listener available yet (similar to DefaultCloud constructor)");
      // FIXME no remote listener available yet (similar to DefaultCloud constructor)
      // try {
      // this.cloud.addListener(new RemoteDataStorageListenerWrapper(this));
      // } catch (final RemoteException e) {
      // logger.error("", e);
      // }
    } else {
      this.cloud.addListener(this);
    }
  }

  // protected Object readResolve() throws ObjectStreamException {
  // this.dataStorage = CloudFactory.getInstance().getDataStorage();
  // this.cloud = CloudFactory.getInstance();
  // return this;
  // }

  @Override
  public Version getBaseVersion() {
    if (this.baseVersion < 0) {
      return this.cloud.getWorkspace(this.baseVersion);
    }
    return new DefaultPublicVersion(this.dataStorage, this.baseVersion);
  }

  private void rebaseToVersion(final long version) throws WorkspaceExpiredException, VersionConflictException, IllegalArgumentException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    if (this.baseVersion > version) {
      throw new IllegalArgumentException("New BaseVersion can only be set to newer Version");
    }

    this.dataStorage.setWorkspaceBaseVersionNumber(this.privateVersionId, version);

    this.baseVersion = version;
  }

  @Override
  public Map<Artifact, Set<Property>> rebase(final Version version) throws WorkspaceExpiredException {
    final Map<Artifact, Set<Property>> conflicts = new HashMap<Artifact, Set<Property>>();
    final Set<Long> artifactConflicts = this.dataStorage.getArtifactConflicts(this.privateVersionId);
    for (final Long aid : artifactConflicts) {
      conflicts.put(ArtifactFactory.getArtifact(this.dataStorage, version.getVersionNumber(), aid), new HashSet<Property>());
    }
    final Map<Long, Set<String>> propertyConflicts = this.dataStorage.getPropertyConflicts(this.privateVersionId);
    final Iterator<Entry<Long, Set<String>>> iterator = propertyConflicts.entrySet().iterator();
    while (iterator.hasNext()) {
      final Entry<Long, Set<String>> next = iterator.next();
      final Artifact artifact = ArtifactFactory.getArtifact(this.dataStorage, version.getVersionNumber(), next.getKey());
      final Set<Property> properties = new HashSet<Property>();
      for (final String prop : next.getValue()) {
        properties.add(new DefaultProperty(this.dataStorage, next.getKey(), version.getVersionNumber(), prop));
      }
      if (artifactConflicts.contains(next.getKey())) {
        conflicts.get(artifact).addAll(properties);
      } else {
        conflicts.put(artifact, properties);
      }
    }
    this.rebaseToVersion(version.getVersionNumber());
    return conflicts;
  }

  @Override
  public Map<Artifact, Set<Property>> rebaseToHeadVersion() throws WorkspaceExpiredException {
    return this.rebase(this.cloud.getVersion(this.cloud.getHeadVersionNumber()));
  }

  /*
   * this methods emits the previous mentioned change notifications; general rule: only the last state change of the artifacts/properties with regard to the base version are
   * emitted
   */
  private List<Change> determineChangeNotifications(final long oldBaseVersion, final long newBaseVersion, final long ignoreVersion) throws RemoteException, ArtifactDoesNotExistException {
    final Map<Long, Set<String>> diffs = this.dataStorage.getArtifactPropertyDiffs(newBaseVersion, oldBaseVersion);
    final Map<Long, Set<String>> versiontoBeIgnoredDiff = this.dataStorage.getArtifactPropertyDiffs(ignoreVersion);// this.privateVersionId);
    final Iterator<Entry<Long, Set<String>>> iterator = diffs.entrySet().iterator();
    final List<Change> changesToEmit = new ArrayList<Change>();
    while (iterator.hasNext()) {
      final Entry<Long, Set<String>> entry = iterator.next();
      final Long aid = entry.getKey();
      final List<Long> versionNumbers = this.dataStorage.getArtifactHistoryVersionNumbers(aid);
      final boolean createdInRange = this.createdInRange(oldBaseVersion, versionNumbers);
      final boolean isAlive = this.dataStorage.isArtifactAlive(newBaseVersion, aid);

      if (isAlive && createdInRange) { // artifactCreated
        changesToEmit.add(new ArtifactCreated(newBaseVersion, this.createArtifact(aid)));
      } else if (!versiontoBeIgnoredDiff.containsKey(aid)) {
        final Object[] artifactCurr = this.dataStorage.getArtifactRepresentation(newBaseVersion, aid);
        final Object[] artifactPrev = this.dataStorage.getArtifactRepresentation(oldBaseVersion, aid);

        // artifactAliveSet
        final Boolean aliveValue = (Boolean) artifactCurr[DataStorage.Columns.ALIVE.getIndex()];
        if (!aliveValue.equals(artifactPrev[DataStorage.Columns.ALIVE.getIndex()])) {
          changesToEmit.add(new ArtifactAliveSet(newBaseVersion, this.createArtifact(aid), aliveValue));
        }

        // artifactTypeSet
        final Long type = (Long) artifactCurr[DataStorage.Columns.ARTIFACT_TYPE.getIndex()];
        if (!type.equals(artifactPrev[DataStorage.Columns.ARTIFACT_TYPE.getIndex()])) {
          changesToEmit.add(new ArtifactTypeSet(newBaseVersion, this.createArtifact(aid), this.createArtifact(type)));
        }

        // artifactPackageSet
        final Long pckg = (Long) artifactCurr[DataStorage.Columns.ARTIFACT_CONTAINER.getIndex()];
        if ((pckg != null) && !pckg.equals(artifactPrev[DataStorage.Columns.ARTIFACT_CONTAINER.getIndex()])) {
          changesToEmit.add(new ArtifactContainerSet(newBaseVersion, this.createArtifact(aid), (Package) this.createArtifact(pckg)));
        }
      }

      if (isAlive && createdInRange) {
        continue;
      }

      final Set<String> properties = entry.getValue();
      for (final String property : properties) {
        final boolean isCollectionProp = this.collElement.matcher(property).matches();
        if (isCollectionProp) {
          continue;
        }

        final boolean inWS = versiontoBeIgnoredDiff.containsKey(aid) && versiontoBeIgnoredDiff.get(aid).contains(property);
        if (inWS) {
          continue;
        }

        final Object[] propertyCurr = this.dataStorage.getPropertyRepresentation(newBaseVersion, aid, property);
        if (((String) propertyCurr[DataStorage.Columns.PROPERTY_NAME.getIndex()]).compareTo(DataStorage.PROPERTY_COLLECTION_NEXT_IDX) == 0) {
          continue;
        }

        final boolean isReference = this.dataStorage.isPropertyReference(newBaseVersion, aid, property);
        Object value = isReference ? propertyCurr[DataStorage.Columns.PROPERTY_REFERENCE.getIndex()] : propertyCurr[DataStorage.Columns.PROPERTY_VALUE.getIndex()];
        final boolean isPropAlive = (boolean) propertyCurr[DataStorage.Columns.ALIVE.getIndex()];
        final List<Long> propVersionNumbers = this.dataStorage.getPropertyHistoryVersionNumbers(aid, property);

        final boolean propCreatedInRange = this.createdInRange(oldBaseVersion, propVersionNumbers);

        final long prevProp = propCreatedInRange ? propVersionNumbers.get(0) : oldBaseVersion;
        final boolean wasReference = this.dataStorage.isPropertyReference(prevProp, aid, property);
        final Object[] propertyPrev = this.dataStorage.getPropertyRepresentation(prevProp, aid, property);
        Object oldValue = wasReference ? propertyPrev[DataStorage.Columns.PROPERTY_REFERENCE.getIndex()] : propertyPrev[DataStorage.Columns.PROPERTY_VALUE.getIndex()];
        final boolean wasAlive = (boolean) propertyPrev[DataStorage.Columns.ALIVE.getIndex()];
        final boolean aliveHasChanged = isPropAlive != wasAlive;
        final boolean valueHasChanged = (!Objects.equals(oldValue, value) || propCreatedInRange);

        if (isReference) {
          value = this.createArtifact((Long) value);
        }

        if (wasReference) {
          oldValue = ArtifactFactory.getArtifact(this.dataStorage, prevProp, (long) oldValue);
        }

        if (propVersionNumbers.size() == 1 && propVersionNumbers.contains(newBaseVersion)) {
          oldValue = null;
        }

        final Property p = new DefaultProperty(this.dataStorage, aid, this.privateVersionId, property);
        if (isPropAlive && valueHasChanged) { // propertyValueSet
          changesToEmit.add(new PropertyValueSet(newBaseVersion, p, value, oldValue, (oldValue instanceof Artifact) && !(value instanceof Artifact)));
        }

        if (aliveHasChanged) { // propertyAliveSet
          changesToEmit.add(new PropertyAliveSet(newBaseVersion, p, isPropAlive));
        }
      }

      if (this.dataStorage.isCollection(newBaseVersion, aid)) {
        final boolean isReferenceCollection = this.dataStorage.isReferenceCollection(newBaseVersion, aid);
        final CollectionArtifact collArtifact = (CollectionArtifact) this.createArtifact(aid);
        final Collection<?> newElements = collArtifact.getElements();
        if (createdInRange) {
          for (final Object newElem : newElements) {
            changesToEmit.add(new CollectionAddedElement(newBaseVersion, collArtifact, newElem));
          }
        } else {
          final CollectionArtifact prevCollArtifact = this.cloud.getCollectionArtifact(oldBaseVersion, aid);
          final Collection<?> oldElements = prevCollArtifact.getElements();
          final Collection removedElements = this.removeElements(newElements, oldElements, isReferenceCollection);
          for (final Object elem : newElements) {
            changesToEmit.add(new CollectionAddedElement(newBaseVersion, collArtifact, elem));
          }
          this.removeElements(oldElements, removedElements, isReferenceCollection);
          for (final Object elem : oldElements) {
            changesToEmit.add(new CollectionRemovedElement(newBaseVersion, collArtifact, elem));
          }
        }
      }
    }

    Map<Artifact, Set<PropertyValueSet>> chngPerArtifact = new HashMap<>();
    for (Change chng : changesToEmit) {
      if (chng.getType() == ChangeType.PropertyValueSet) {
        PropertyValueSet propertyChng = (PropertyValueSet) chng;
        Artifact artifact = propertyChng.property.getArtifact();
        if (!chngPerArtifact.containsKey(artifact)) {
          chngPerArtifact.put(artifact, new HashSet<PropertyValueSet>());
        }
        chngPerArtifact.get(artifact).add(propertyChng);
      }
    }

    Iterator<Entry<Artifact, Set<PropertyValueSet>>> chngIterator = chngPerArtifact.entrySet().iterator();
    while (chngIterator.hasNext()) {
      Entry<Artifact, Set<PropertyValueSet>> next = chngIterator.next();
      Set<PropertyValueSet> value = next.getValue();
      if (value.size() > 1) {
        changesToEmit.removeAll(value);
        PropertyMapSet mapSet = new PropertyMapSet(newBaseVersion, value);
        changesToEmit.add(mapSet);
      }
    }

    Collections.sort(changesToEmit);
    return changesToEmit;
  }

  private Collection removeElements(final Collection<?> removeFrom, final Collection<?> toRemove, final boolean isReferenceCollection) {
    final Iterator<?> itToRemove = toRemove.iterator();
    final Collection removedElements = new ArrayList();
    while (itToRemove.hasNext()) {
      final Object toRem = itToRemove.next();
      final Object removed = this.removeFirstOccurence(removeFrom, toRem, isReferenceCollection);
      if (removed != null) {
        removedElements.add(removed);
      }
    }
    return removedElements;
  }

  private Object removeFirstOccurence(final Collection<?> removeFrom, final Object toRemove, final boolean isReferenceCollection) {
    final Iterator<?> itRemoveFrom = removeFrom.iterator();
    while (itRemoveFrom.hasNext()) {
      final Object next = itRemoveFrom.next();
      if (isReferenceCollection && (((Artifact) next).getId() == ((Artifact) toRemove).getId())) {
        itRemoveFrom.remove();
        return toRemove;
      } else if (removeFrom.equals(toRemove)) {
        itRemoveFrom.remove();
        return toRemove;
      }
    }
    return null;
  }

  private Artifact createArtifact(final Long aid) {
    return ArtifactFactory.getArtifact(this.dataStorage, this.privateVersionId, aid);
  }

  private boolean createdInRange(final long v, final List<Long> versionNumbers) {
    return !(v >= versionNumbers.get(0));
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getId()
   */
  @Override
  public long getVersionNumber() {
    return this.privateVersionId;
  }

  @Override
  public long getId() {
    return this.privateVersionId;
  }

  @Override
  public boolean isClosed() {
    return this.workspaceExpired;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getOwner()
   */
  @Override
  public Owner getOwner() {
    return this.owner;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getTool()
   */
  @Override
  public Tool getTool() {
    return this.tool;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#createNamespace()
   */
  @Override
  public Package createPackage() throws WorkspaceExpiredException {
    return this.createPackage(null, null);
  }

  @Override
  public Package createPackage(String name) {
    return this.createPackage(null, name);
  }
  
  @Override
  public Package createPackage(final Package pckg) {
    return this.createPackage(pckg, null);
  }
  
  @Override
  public Package createPackage(final Package pckg, String name) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final Long pckgId = pckg != null ? pckg.getId() : null;
    final long id = this.dataStorage.createPackage(this.privateVersionId, this.owner.getId(), this.tool.getId(), name, pckgId);
    
    return new DefaultPackage(this.dataStorage, id, this.privateVersionId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#createArtifact()
   */
  @Override
  public Artifact createArtifact() throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final long id = this.dataStorage.createArtifact(this.privateVersionId, this.owner.getId(), this.tool.getId(), DataStorage.ROOT_TYPE_ID, null);
    return new DefaultArtifact(this.dataStorage, id, this.privateVersionId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#createArtifact(at.jku.sea.cloud.Artifact)
   */
  @Override
  public Artifact createArtifact(final Artifact type) throws WorkspaceExpiredException {
    return this.createArtifact(type, null);
  }

  @Override
  public Artifact createArtifact(final Package pckg) throws WorkspaceExpiredException {
    return this.createArtifact(null, pckg);
  }

  @Override
  public Artifact createArtifact(final Artifact type, final Package pckg) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    Long typeId = null;
    final Long pckgId = pckg != null ? pckg.getId() : null;
    if (type != null) {
      typeId = type.getId();
      if (typeId == DataStorage.PROJECT_TYPE_ID) {
        return this.createProject(pckg);
      } else if (typeId == DataStorage.META_MODEL_TYPE_ID) {
        return this.createMetaModel(pckg);
      } else if (typeId == DataStorage.COLLECTION_TYPE_ID) {
        // TODO think about default values
        return this.createCollection(true, pckg);
      } else if (typeId == DataStorage.MAP_TYPE_ID) {
        return this.createMap(pckg);
      }
    }
    final long id = this.dataStorage.createArtifact(this.privateVersionId, this.owner.getId(), this.tool.getId(), typeId, pckgId);
    return ArtifactFactory.getArtifact(this.dataStorage, this.privateVersionId, id);
  }

  @Override
  public Artifact createArtifact(final Artifact type, final Container container, final MetaModel metamodel, final Project project, final Map<String, Object> properties) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final Map<String, Long> referenceMap = new HashMap<>();
    final Map<String, Object> valueMap = new HashMap<>();
    if (properties != null) {
      for (final Entry<String, Object> property : properties.entrySet()) {
        if (property.getValue() instanceof Artifact) {
          referenceMap.put(property.getKey(), ((Artifact) property.getValue()).getId());
        } else {
          valueMap.put(property.getKey(), property.getValue());
        }
      }
    }

    final long id = this.dataStorage.createArtifact(this.privateVersionId, this.owner.getId(), this.tool.getId(), type == null ? DataStorage.ROOT_TYPE_ID : type.getId(),
        container == null ? null : container.getId(), metamodel == null ? null : metamodel.getId(), project == null ? null : project.getId(), valueMap, referenceMap);
    return ArtifactFactory.getArtifact(this.dataStorage, this.privateVersionId, id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#commitArtifact(at.jku.sea.cloud.Artifact, java.lang.String)
   */
  @Override
  public long commitArtifact(final Artifact artifact, final String message) throws ArtifactDoesNotExistException, WorkspaceExpiredException, ArtifactConflictException, WorkspaceEmptyException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final long version = this.dataStorage.commitArtifact(this.privateVersionId, artifact.getId(), message);
    return version;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#rollbackArtifact(at.jku.sea.cloud.Artifact)
   */
  @Override
  public void rollbackArtifact(final Artifact artifact) throws ArtifactDoesNotExistException, WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    this.dataStorage.rollbackArtifact(this.privateVersionId, artifact.getId());
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#commitProperty(at.jku.sea.cloud.Artifact, java.lang.String, java.lang.String)
   */
  @Override
  public long commitProperty(final Property property, final String message) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, WorkspaceExpiredException, PropertyConflictException,
      PropertyNotCommitableException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final long version = this.dataStorage.commitProperty(this.privateVersionId, property.getId(), property.getName(), message);
    return version;
  }

  /*
   * at.jku.sea.cloud.Package (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#rollbackProperty(at.jku.sea.cloud.Artifact, java.lang.String)
   */
  @Override
  public void rollbackProperty(final Property property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    this.dataStorage.rollbackProperty(this.privateVersionId, property.getId(), property.getName());
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#commitAll(java.lang.String)
   */
  @Override
  public long commitAll(final String message) throws WorkspaceExpiredException, VersionConflictException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final long version = this.dataStorage.commitVersion(this.privateVersionId, message);
    return version;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#rollbackAll()
   */
  @Override
  public void rollbackAll() throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    this.dataStorage.rollbackVersion(this.privateVersionId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getArtifacts()
   */
  @Override
  public Collection<Artifact> getArtifacts() throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifacts(this.privateVersionId);
  }

  @Override
  public Collection<Artifact> getArtifacts(Set<Long> ids) throws WorkspaceExpiredException {
    return this.cloud.getArtifacts(privateVersionId, ids);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getArtifacts(at.jku.sea.cloud.Artifact[])
   */
  @Override
  public Collection<Artifact> getArtifacts(final Artifact... filters) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifacts(this.privateVersionId, filters);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getArtifactsWithProperty(java.lang.String, java.lang.Object, at.jku.sea.cloud.Artifact[])
   */
  @Override
  public Collection<Artifact> getArtifactsWithProperty(final String propertyName, final Object propertyValue, final Artifact... filters) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsWithProperty(this.privateVersionId, propertyName, propertyValue, filters);
  }

  @Override
  public Collection<Artifact> getArtifactsWithProperty(final String propertyName, final Object propertyValue, final boolean alive, final Artifact... filters) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsWithProperty(this.privateVersionId, propertyName, propertyValue, alive, filters);
  }

  @Override
  public Collection<Artifact> getArtifactsWithReference(Artifact artifact, Artifact... filters) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsWithReference(this.privateVersionId, artifact, filters);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getArtifactsWithProperty(java.lang.String, java.lang.Object, at.jku.sea.cloud.Artifact[])
   */
  @Override
  public Collection<Artifact> getArtifactsWithProperty(final Map<String, Object> propertyToValue, final Artifact... filters) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsWithProperty(this.privateVersionId, propertyToValue, filters);
  }

  @Override
  public Collection<Artifact> getArtifactsWithProperty(final Map<String, Object> propertyToValue, final boolean alive, final Artifact... filters) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsWithProperty(this.privateVersionId, propertyToValue, alive, filters);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(Artifact... filters) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsAndPropertyMap(privateVersionId, filters);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(String propertyName, Object propertyValue, boolean alive, Artifact... filters) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsAndPropertyMap(this.privateVersionId, propertyName, propertyValue, alive, filters);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(Set<Artifact> artifacts, Set<String> properties) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsPropertyMap(privateVersionId, artifacts, properties);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(Set<Artifact> artifacts) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsPropertyMap(privateVersionId, artifacts);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(Map<String, Object> propertyToValue, boolean alive, Artifact... filters) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifactsAndPropertyMap(this.privateVersionId, propertyToValue, alive, filters);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getProjects()
   */
  @Override
  public Collection<Project> getProjects() throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getProjects(this.privateVersionId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getMetaModels()
   */
  @Override
  public Collection<MetaModel> getMetaModels() throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getMetaModels(this.privateVersionId);
  }

  @Override
  public MetaModel getMetaMetaModel(Artifact artifact) throws ArtifactDeadException {
    Long type = this.dataStorage.getArtifactType(this.privateVersionId, artifact.getId());
    Object[] propertyRep = this.dataStorage.getPropertyRepresentationOrNull(this.privateVersionId, type, MMMTypeProperties.METAMODEL);
    if (propertyRep != null) {
      return (MetaModel) ArtifactFactory.getArtifact(dataStorage, this.privateVersionId, (long) propertyRep[DataStorage.Columns.PROPERTY_REFERENCE.getIndex()]);
    }
    Long typeOfType = this.dataStorage.getArtifactType(this.privateVersionId, type);

    Set<Object[]> artifacts = this.dataStorage.getArtifacts(privateVersionId, true, null, null, DataStorage.META_MODEL_TYPE_ID, null);
    for (Object[] artifactRep : artifacts) {
      Long mmId = (Long) artifactRep[DataStorage.Columns.ARTIFACT_ID.getIndex()];
      if (this.dataStorage.existsElementInCollection(this.privateVersionId, mmId, typeOfType)) {
        return (MetaModel) ArtifactFactory.getArtifact(dataStorage, this.privateVersionId, artifactRep);
      }
    }

    return null;
  }

  @Override
  public Collection<CollectionArtifact> getCollectionArtifacts() throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getCollectionArtifacts(this.privateVersionId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getPackage(long)
   */
  @Override
  public Package getPackage(final long id) throws WorkspaceExpiredException, PackageDoesNotExistException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getPackage(this.privateVersionId, id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getPackage(long)
   */
  @Override
  public Project getProject(final long id) throws WorkspaceExpiredException, ProjectDoesNotExistException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getProject(this.privateVersionId, id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Workspace#getPackage(long)
   */
  @Override
  public Resource getResource(final long id) throws WorkspaceExpiredException, ProjectDoesNotExistException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getResource(this.privateVersionId, id);
  }

  @Override
  public MetaModel getMetaModel(final long id) throws WorkspaceExpiredException, MetaModelDoesNotExistException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getMetaModel(this.privateVersionId, id);
  }

  @Override
  public Collection<Resource> getResources() {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getResources(this.privateVersionId);
  }

  @Override
  public Collection<Resource> getResources(String fullQualifiedName) {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getResources(privateVersionId, fullQualifiedName);
  }

  @Override
  public CollectionArtifact getCollectionArtifact(final long id) throws WorkspaceExpiredException, CollectionArtifactDoesNotExistException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getCollectionArtifact(this.privateVersionId, id);
  }

  @Override
  public CollectionArtifact createCollection(final boolean containsOnlyArtifacts) throws WorkspaceExpiredException {
    return this.createCollection(containsOnlyArtifacts, null);
  }

  @Override
  public CollectionArtifact createCollection(final boolean containsOnlyArtifacts, final Package pckg) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final Long pckgId = pckg != null ? pckg.getId() : null;
    final long collectionId = this.dataStorage.createCollection(this.privateVersionId, this.owner.getId(), this.tool.getId(), pckgId, containsOnlyArtifacts);
    return new DefaultCollectionArtifact(this.dataStorage, collectionId, this.privateVersionId);
  }

  @Override
  public CollectionArtifact createCollection(boolean containsOnlyArtifacts, Package pckg, Collection<?> elements, Map<String, Object> properties) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final Map<String, Long> referenceMap = new HashMap<>();
    final Map<String, Object> valueMap = new HashMap<>();
    if (properties != null) {
      for (final Entry<String, Object> property : properties.entrySet()) {
        if (property.getValue() instanceof Artifact) {
          referenceMap.put(property.getKey(), ((Artifact) property.getValue()).getId());
        } else {
          valueMap.put(property.getKey(), property.getValue());
        }
      }
    }
    if (containsOnlyArtifacts) {
      Collection<Long> newElements = new ArrayList<>();
      for (Object element : elements) {
        if (!(element instanceof Artifact)) {
          throw new IllegalArgumentException(DefaultCollectionArtifact.FAIL_ADD);
        }
        newElements.add(((Artifact) element).getId());
      }
      elements = newElements;
    } else {
      for (Object element : elements) {
        if (element instanceof Artifact) {
          throw new IllegalArgumentException(DefaultCollectionArtifact.FAIL_ADD);
        }
      }
    }

    long collectionId = this.dataStorage.createCollection(this.privateVersionId, this.owner.getId(), this.tool.getId(), pckg != null ? pckg.getId() : null, containsOnlyArtifacts, elements, valueMap,
        referenceMap);
    return (CollectionArtifact) ArtifactFactory.getArtifact(dataStorage, this.privateVersionId, collectionId);
  }

  @Override
  public void close() throws WorkspaceExpiredException, WorkspaceNotEmptyException {
    if (!this.workspaceExpired) {
      Workspace parent = this.getParent();
      Collection<Object[]> children = this.dataStorage.getWorkspaceChildren(this.getId());
      if (parent != null) {
        for (Object[] child : children) {
          Long wsId = (Long) child[Columns.WORKSPACE_VERSION.getIndex()];
          PropagationType push = (PropagationType) child[Columns.WORKSPACE_PUSH.getIndex()];
          PropagationType pull = (PropagationType) child[Columns.WORKSPACE_PULL.getIndex()];
          Map<Long, Set<String>> diffs = this.dataStorage.getWorkspaceDiffs(parent.getId(), wsId);
          if (!diffs.isEmpty() && (pull == PropagationType.instant || push == PropagationType.instant)) {
            throw new WorkspaceConflictException(wsId, parent.getId());
          }
        }
      }
      for (Object[] child : children) {
        Long wsId = (Long) child[Columns.WORKSPACE_VERSION.getIndex()];
        this.dataStorage.setWorkspaceParent(wsId, parent == null ? null : parent.getId());
      }
      this.dataStorage.deleteWorkspace(this.privateVersionId);

    }
  }

  @Override
  public MapArtifact createMap() {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final Long mapId = this.dataStorage.createMap(this.privateVersionId, this.owner.getId(), this.tool.getId(), null);
    return new DefaultMapArtifact(this.dataStorage, mapId, this.privateVersionId);
  }

  @Override
  public MapArtifact createMap(final Package pckg) {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final long mapId = this.dataStorage.createMap(this.privateVersionId, this.owner.getId(), this.tool.getId(), pckg.getId());
    return new DefaultMapArtifact(this.dataStorage, mapId, this.privateVersionId);
  }

  @Override
  public Artifact getArtifact(final long id) throws WorkspaceExpiredException, ArtifactDoesNotExistException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getArtifact(this.privateVersionId, id);
  }

  @Override
  public Project createProject() throws WorkspaceExpiredException {
    return this.createProject((String) null);
  }
  
  @Override
  public Project createProject(Package pckg) {
    throw new  UnsupportedOperationException();
  }

  @Override
  public Project createProject(final String name) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    
    final long id = this.dataStorage.createProject(this.privateVersionId, this.owner.getId(), this.tool.getId(), name);
    return new DefaultProject(this.dataStorage, id, this.privateVersionId);
  }

  @Override
  public MetaModel createMetaModel() throws WorkspaceExpiredException {
    return this.createMetaModel(null);
  }

  @Override
  public MetaModel createMetaModel(final Package pckg) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    final Long pckgId = pckg != null ? pckg.getId() : null;
    final long id = this.dataStorage.createMetaModel(this.privateVersionId, this.owner.getId(), this.tool.getId(), pckgId);
    return new DefaultMetaModel(this.dataStorage, id, this.privateVersionId);
  }

  @Override
  public Resource createResource(String fullQualifiedName) {
    return createResource(fullQualifiedName, null, null, new ArrayList<Artifact>());
  }

  @Override
  public Resource createResource(String fullQualifiedName, Package pckg, Project project, Collection<Artifact> artifacts) {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    Long pckgId = pckg == null ? null : pckg.getId();
    Long projectId = pckg == null ? null : pckg.getId();
    Map<String, Object> valueMap = new HashMap<>();
    valueMap.put(DataStorage.PROPERTY_FULL_QUALIFIED_NAME, fullQualifiedName);
    Set<Long> artifactIds = new HashSet<>();
    for (Artifact artifact : artifacts) {
      artifactIds.add(artifact.getId());
    }
    final long id = this.dataStorage.createResource(this.privateVersionId, owner.getId(), tool.getId(), pckgId, projectId, valueMap, new HashMap<String, Long>(), artifactIds);

    return new DefaultResource(this.dataStorage, id, this.privateVersionId);
  }

  @Override
  public String getIdentifier() throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.identifier;
  }

  @Override
  public Collection<Package> getPackages() {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    return this.cloud.getPackages(this.privateVersionId);
  }

  @Override
  public Set<Artifact> getArtifactConflicts() throws WorkspaceExpiredException {
    return this.getArtifactConflicts(this.cloud.getHeadVersionNumber());
  }

  @Override
  public Set<Artifact> getArtifactConflicts(final long publicVersion) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    if (this.cloud.getHeadVersionNumber() < publicVersion) {
      throw new IllegalArgumentException();
    }
    return ArtifactUtils.getArtifactsInWs(this, this.dataStorage.getArtifactConflicts(this.privateVersionId, publicVersion));
  }

  @Override
  public Set<Property> getPropertyConflicts() throws WorkspaceExpiredException {
    return this.getPropertyConflicts(this.cloud.getHeadVersionNumber());
  }

  @Override
  public Workspace getParent() {
    Long workspaceParent = this.dataStorage.getWorkspaceParent(this.privateVersionId);
    if (workspaceParent != null) {
      return this.cloud.getWorkspace(workspaceParent);
    }
    return null;
  }

  @Override
  public Collection<Workspace> getChildren() {
    Collection<Object[]> children = this.dataStorage.getWorkspaceChildren(this.getId());
    Collection<Workspace> childWS = new ArrayList<>();
    for (Object[] child : children) {
      Long wsId = (Long) child[Columns.WORKSPACE_VERSION.getIndex()];
      childWS.add(this.cloud.getWorkspace(wsId));
    }
    return childWS;
  }

  @Override
  public PropagationType getPush() {
    return this.dataStorage.getWorkspacePush(this.privateVersionId);
  }

  @Override
  public void setPush(PropagationType type) throws WorkspaceConflictException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    Workspace parent = this.getParent();
    if (parent != null && this.getPush() == PropagationType.triggered && type == PropagationType.instant) {
      Map<Long, Set<String>> diffs = this.dataStorage.getWorkspaceDiffs(privateVersionId, parent.getId());
      if (!diffs.isEmpty()) {
        throw new WorkspaceConflictException(this.privateVersionId, parent.getId());
      }
    }
    this.dataStorage.setWorkspacePush(this.privateVersionId, type);
  }

  @Override
  public PropagationType getPull() {
    return this.dataStorage.getWorkspacePull(this.privateVersionId);
  }

  @Override
  public void setPull(PropagationType type) {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    this.dataStorage.setWorkspacePull(this.privateVersionId, type);
    if (type == PropagationType.instant) {
      Long parentId = this.dataStorage.getWorkspaceParent(this.privateVersionId);
      if (parentId != null) {
        this.dataStorage.pullAll(privateVersionId, parentId);
      }
    }

  }

  @Override
  public void pushArtifact(Artifact artifact) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    Workspace parent = this.getParent();
    if (parent != null) {
      if (parent.isClosed()) {
        throw new WorkspaceExpiredException(parent.getId());
      }
      this.dataStorage.pushArtifact(privateVersionId, parent.getId(), artifact.getId());
    }
  }

  @Override
  public void pushProperty(Property property) throws WorkspaceExpiredException, ArtifactDoesNotExistException, PropertyDoesNotExistException, PropertyNotPushOrPullableException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    Workspace parent = this.getParent();
    if (parent != null) {
      if (parent.isClosed()) {
        throw new WorkspaceExpiredException(parent.getId());
      }
      this.dataStorage.pushProperty(privateVersionId, parent.getId(), property.getId(), property.getName());
    }
  }

  @Override
  public void pushAll() throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    Workspace parent = this.getParent();
    if (parent != null) {
      if (parent.isClosed()) {
        throw new WorkspaceExpiredException(parent.getId());
      }
      this.dataStorage.pushAll(privateVersionId, parent.getId());
    }
  }

  @Override
  public void pullArtifact(Artifact artifact) throws WorkspaceExpiredException, ArtifactDoesNotExistException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    Workspace parent = this.getParent();
    if (parent != null) {
      if (parent.isClosed()) {
        throw new WorkspaceExpiredException(parent.getId());
      }
      this.dataStorage.pullArtifact(privateVersionId, parent.getId(), artifact.getId());
    }
  }

  @Override
  public void pullProperty(Property property) throws WorkspaceExpiredException, ArtifactDoesNotExistException, PropertyDoesNotExistException, PropertyNotPushOrPullableException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    Workspace parent = this.getParent();
    if (parent != null) {
      if (parent.isClosed()) {
        throw new WorkspaceExpiredException(parent.getId());
      }
      this.dataStorage.pullProperty(privateVersionId, parent.getId(), property.getId(), property.getName());
    }
  }

  @Override
  public Map<Artifact, Set<Property>> pullAll() throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    Workspace parent = this.getParent();
    if (parent != null) {
      if (parent.isClosed()) {
        throw new WorkspaceExpiredException(parent.getId());
      }
      Map<Artifact, Set<Property>> diff = getWSDiff(parent.getId());
      this.dataStorage.pullAll(privateVersionId, parent.getId());
      return diff;
    } else {
      return this.rebaseToHeadVersion();
    }
  }

  private Map<Artifact, Set<Property>> getWSDiff(final long pvId) {
    Map<Artifact, Set<Property>> diffs = new HashMap<>();
    Map<Long, Set<String>> workspaceDiffs = this.dataStorage.getWorkspaceDiffs(this.privateVersionId, pvId);
    for (Iterator<Entry<Long, Set<String>>> iterator = workspaceDiffs.entrySet().iterator(); iterator.hasNext();) {
      Entry<Long, Set<String>> next = iterator.next();
      Artifact artifact = ArtifactFactory.getArtifact(dataStorage, pvId, next.getKey());
      Set<Property> props = new HashSet<>();
      for (String name : next.getValue()) {
        props.add(new DefaultProperty(dataStorage, next.getKey(), pvId, name));
      }
      diffs.put(artifact, props);
    }
    return diffs;

  }

  @Override
  public Set<Property> getPropertyConflicts(final long publicVersion) throws WorkspaceExpiredException {
    if (this.workspaceExpired) {
      throw new WorkspaceExpiredException(this.privateVersionId);
    }
    if (this.cloud.getHeadVersionNumber() < publicVersion) {
      throw new IllegalArgumentException();
    }
    final Map<Long, Set<String>> conflicts = this.dataStorage.getPropertyConflicts(this.privateVersionId, publicVersion);
    final Set<Property> conflictingProperties = new HashSet<Property>();
    for (final Long aid : conflicts.keySet()) {
      for (final String prop : conflicts.get(aid)) {
        conflictingProperties.add(new DefaultProperty(dataStorage, aid, this.privateVersionId, prop));
      }
    }
    return conflictingProperties;
  }

  @Override
  public void setParent(Workspace parent) throws IllegalArgumentException {
    if (parent != null && parent.getBaseVersion().getVersionNumber() != this.baseVersion) {
      throw new IllegalArgumentException("parent has not the same base version");
    }
    if (parent != null && parent.getId() == this.privateVersionId) {
      throw new IllegalArgumentException("cannot set parent to itself");
    }

    if (parent != null) {
      Set<Long> hierarchyIds = ObjectArrayRepresentationUtils.getWorkspaceIds(this.dataStorage.getWorkspaceParents(parent.getId()));
      if (hierarchyIds.contains(this.privateVersionId)) {
        throw new WorkspaceCycleException(this.privateVersionId, parent.getId());
      }
    }

    if (parent != null) {
      Map<Long, Set<String>> diffs = this.dataStorage.getWorkspaceDiffs(privateVersionId, parent.getId());
      if (!diffs.isEmpty() && (this.getPull() == PropagationType.instant || this.getPush() == PropagationType.instant)) {
        throw new WorkspaceConflictException(this.privateVersionId, parent.getId());
      }
    }

    this.dataStorage.setWorkspaceParent(this.privateVersionId, parent == null ? null : parent.getId());

    if (this.getPull() == PropagationType.instant) {
      this.pullAll();
    }
    if (this.getPush() == PropagationType.instant) {
      this.pushAll();
    }
  }

  @Override
  public void artifactCreated(final ArtifactCreated event) throws RemoteException {

    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.artifactCreated(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void artifactAliveSet(final ArtifactAliveSet event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.artifactAliveSet(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void artifactTypeSet(final ArtifactTypeSet event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.artifactTypeSet(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void artifactContainerSet(final ArtifactContainerSet event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.artifactContainerSet(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void propertyAliveSet(final PropertyAliveSet event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.property.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.propertyAliveSet(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void propertyValueSet(final PropertyValueSet event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.property.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.propertyValueSet(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void propertyMapSet(final PropertyMapSet event) throws RemoteException {
    if (!this.listeners.isEmpty() && (!event.changes.isEmpty() && (event.changes.iterator().next().property.getVersionNumber() == this.privateVersionId))) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.propertyMapSet(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void collectionAddedElement(final CollectionAddedElement event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.collectionAddedElement(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void collectionAddedElements(CollectionAddedElements event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.collectionAddedElements(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void collectionRemovedElement(final CollectionRemovedElement event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.collectionRemovedElement(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void mapCleared(MapCleared event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.mapCleared(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void mapElementRemoved(MapElementRemoved event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.mapElementRemoved(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void putMap(MapPut event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.putMap(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void artifactCommited(final ArtifactCommited preEvent) throws RemoteException {
    if (preEvent.artifact.getVersionNumber() == this.privateVersionId) {
      this.baseVersion = preEvent.version.getVersionNumber();
      if (!this.listeners.isEmpty()) {
        final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
        List<Change> changes = new ArrayList<>();
        try {
          changes.addAll(this.determineChangeNotifications(preEvent.oldBaseVersion, preEvent.version.getVersionNumber() - 1, preEvent.version.getVersionNumber()));
        } catch (final ArtifactDoesNotExistException e) {
          // something went terribly, terribly wrong
          // reaching this point indicates a Bug in the ArtifactFactory
          logger.error("Artifact queried from the DataStorage could not be created -> Bug ArtifactFactory");
        }
        ArtifactCommited event = new ArtifactCommited(preEvent.origin, preEvent.artifact, preEvent.version, changes, preEvent.oldBaseVersion);
        while (iterator.hasNext()) {
          final WorkspaceListener listener = iterator.next();
          try {
            listener.artifactCommited(event);
          } catch (final RemoteException e) {
            iterator.remove();
            logger.warn("listener " + listener + " removed because of: ", e);
          }
        }
      }
    }
  }

  @Override
  public void propertyCommited(final PropertyCommited preEvent) throws RemoteException {
    if (preEvent.property.getVersionNumber() == this.privateVersionId) {
      this.baseVersion = preEvent.version.getVersionNumber();
      if (!this.listeners.isEmpty()) {
        final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
        List<Change> changes = new ArrayList<>();
        try {
          changes.addAll(this.determineChangeNotifications(preEvent.oldBaseVersion, preEvent.version.getVersionNumber() - 1, preEvent.version.getVersionNumber()));

        } catch (final ArtifactDoesNotExistException e) {
          // something went terribly, terribly wrong
          // reaching this point indicates a Bug in the ArtifactFactory
          logger.error("Artifact queried from the DataStorage could not be created -> Bug ArtifactFactory");
        }
        PropertyCommited event = new PropertyCommited(preEvent.origin, preEvent.property, preEvent.version, changes, preEvent.oldBaseVersion);
        while (iterator.hasNext()) {
          final WorkspaceListener listener = iterator.next();
          try {
            listener.propertyCommited(event);
          } catch (final RemoteException e) {
            iterator.remove();
            logger.warn("listener " + listener + " removed because of: ", e);
          }
        }
      }
    }
  }

  @Override
  public void versionCommited(final VersionCommited preEvent) throws RemoteException {
    if (preEvent.workspace.getVersionNumber() == this.privateVersionId) {
      this.baseVersion = preEvent.version.getVersionNumber();
      if (!this.listeners.isEmpty()) {
        List<Change> changes = new ArrayList<>();
        try {
          changes.addAll(this.determineChangeNotifications(preEvent.oldBaseVersion, preEvent.version.getVersionNumber() - 1, preEvent.version.getVersionNumber()));
        } catch (final ArtifactDoesNotExistException e) {
          // something went terribly, terribly wrong
          // reaching this point indicates a Bug in the ArtifactFactory
          logger.error("Artifact queried from the DataStorage could not be created -> Bug ArtifactFactory");
        }
        final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
        VersionCommited event = new VersionCommited(preEvent.workspace, preEvent.version, changes, preEvent.oldBaseVersion);
        while (iterator.hasNext()) {
          final WorkspaceListener listener = iterator.next();
          try {
            listener.versionCommited(event);
          } catch (final RemoteException e) {
            iterator.remove();
            logger.warn("listener " + listener + " removed because of: ", e);
          }
        }
      }
    }
  }

  @Override
  public void artifactRollback(final ArtifactRollBack event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.artifact.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.artifactRollback(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void propertyRollback(final PropertyRollBack event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.property.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.propertyRollback(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void workspaceRollback(final WorkspaceRollBack event) throws RemoteException {
    if (!this.listeners.isEmpty() && (event.workspace.getVersionNumber() == this.privateVersionId)) {
      final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final WorkspaceListener listener = iterator.next();
        try {
          listener.workspaceRollback(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void workspaceClosed(final WorkspaceClosed event) throws RemoteException {
    if (event.workspace.getVersionNumber() == this.privateVersionId) {
      this.workspaceExpired = true;
      if (!this.listeners.isEmpty()) {
        final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
          final WorkspaceListener listener = iterator.next();
          try {
            listener.workspaceClosed(event);
          } catch (final RemoteException e) {
            iterator.remove();
            logger.warn("listener " + listener + " removed because of: ", e);
          }
        }
      }
    }
  }

  @Override
  public void workspaceRebased(final WorkspaceRebased event) throws RemoteException {
    if (event.workspace.getVersionNumber() == this.privateVersionId) {
      this.baseVersion = this.dataStorage.getWorkspaceBaseVersionNumber(this.privateVersionId);
    }
    if (!this.listeners.isEmpty() && (event.workspace.getVersionNumber() == this.privateVersionId)) {
      List<Change> changes = new ArrayList<>();
      try {
        final long oldBaseVersion = event.oldBaseVersion.getVersionNumber();
        changes.addAll(this.determineChangeNotifications(oldBaseVersion, this.baseVersion, this.privateVersionId));

      } catch (final ArtifactDoesNotExistException e) {
        // something went terribly, terribly wrong
        // reaching this point indicates a Bug in the ArtifactFactory
        logger.error("Artifact queried from the DataStorage could not be created -> Bug ArtifactFactory");
      }
      final WorkspaceRebased event1 = new WorkspaceRebased(event.origin, event.workspace, event.oldBaseVersion, changes);
      if (!this.listeners.isEmpty()) {
        final Iterator<WorkspaceListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
          final WorkspaceListener listener = iterator.next();
          try {
            listener.workspaceRebased(event1);
          } catch (final RemoteException e) {
            iterator.remove();
            logger.warn("listener " + listener + " removed because of: ", e);
          }
        }
      }
    }
  }

  @Override
  public void workspaceAdded(final WorkspaceAdded event) throws RemoteException {
    // ignore because not relevant for this workspace
  }

  @Override
  public void workspaceParentSet(WorkspaceParentSet event) throws RemoteException {
    if (event.workspace.getVersionNumber() == this.privateVersionId) {
      for (WorkspaceListener listener : listeners) {
        listener.workspaceParentSet(event);
      }
    }
  }

  @Override
  public void addListener(final WorkspaceListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeListener(final WorkspaceListener listener) {
    this.listeners.remove(listener);
  }

  @Override
  public Object[] getArtifactRepresentation(long id) {
    return this.dataStorage.getArtifactRepresentation(this.privateVersionId, id);
  }

}
