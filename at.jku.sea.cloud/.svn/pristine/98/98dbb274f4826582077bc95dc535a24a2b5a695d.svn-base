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
 * AbstractDataStorage.java created on 13.03.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.implementation;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.exceptions.ArtifactConflictException;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.ArtifactIsNotACollectionException;
import at.jku.sea.cloud.exceptions.ArtifactNotDeleteableException;
import at.jku.sea.cloud.exceptions.ArtifactNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.CredentialsException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyConflictException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyNotCommitableException;
import at.jku.sea.cloud.exceptions.PropertyNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.PushConflictException;
import at.jku.sea.cloud.exceptions.VersionConflictException;
import at.jku.sea.cloud.exceptions.WorkspaceEmptyException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.exceptions.WorkspaceNotEmptyException;
import at.jku.sea.cloud.implementation.events.ArtifactCommitedEvent;
import at.jku.sea.cloud.implementation.events.ArtifactEvent;
import at.jku.sea.cloud.implementation.events.CollectionAddedElementEvent;
import at.jku.sea.cloud.implementation.events.CollectionAddedElementsEvent;
import at.jku.sea.cloud.implementation.events.CollectionEvent;
import at.jku.sea.cloud.implementation.events.CollectionRemovedElementEvent;
import at.jku.sea.cloud.implementation.events.EventFactory;
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
import at.jku.sea.cloud.implementation.events.PropertyEvent;
import at.jku.sea.cloud.implementation.events.PropertyMapSetEvent;
import at.jku.sea.cloud.implementation.events.PropertyReferenceSetEvent;
import at.jku.sea.cloud.implementation.events.PropertyValueSetEvent;
import at.jku.sea.cloud.implementation.events.VersionCommitedEvent;
import at.jku.sea.cloud.implementation.events.VersionDeletedEvent;
import at.jku.sea.cloud.listeners.DataStorageEventType;
import at.jku.sea.cloud.listeners.DataStorageListener;
import at.jku.sea.cloud.utils.StringUtils;

/**
 * @author alexander noehrer
 */
public abstract class AbstractDataStorage implements DataStorage {
  private static Logger logger = LoggerFactory.getLogger(AbstractDataStorage.class);
  protected static Pattern COLLECTION_ELEMENT = Pattern.compile(DataStorage.COLLECTION_ELEMENT_PATTERN);

  public static void bindDataStorageInstance(final DataStorage dataStorage) throws Exception {
    System.setProperty("java.rmi.server.hostname", "127.0.0.1");
    try {
      final int port = 1099;
      try {
        logger.info("creating RMI registry...");
        LocateRegistry.createRegistry(port);
      } catch (final RemoteException ex) {
        logger.debug("error creating RMI registry", ex);
        logger.info("RMI registry already exists");
      }
      System.setProperty("java.security.policy", "policy.all");
      System.setProperty("java.rmi.server.hostname", "127.0.0.1");
      if (System.getSecurityManager() == null) {
        System.setSecurityManager(new RMISecurityManager());
        logger.info("Security manager installed.");
      }
      logger.info("binding DataStorage [//localhost:{}/DataStorage]...", port);
      trmi.Naming.bind("//localhost:" + port + "/DataStorage", dataStorage, new Class[] { DataStorage.class });
      logger.info("binding was successful.");
    } catch (final Exception e) {
      logger.error("error binding RMI object", e);
      throw e;
    }
  }

  protected AtomicLong idGenerator;
  protected AtomicLong versionGenerator;
  protected AtomicLong privateVersionNumberGenerator;

  /**
   * generator for User ids
   * 
   * @author jMayer
   */
  protected AtomicLong userGenerator;

  /**
   * returns the max user id, used for initializing the userGenerator from the DB.
   * 
   * @return highest user id in the system
   */
  protected abstract long getMaximumUserId();

  /**
   * User creation with pre-existing Owner artifact.
   * 
   * Used internally for the users created with {@link InfrastructureInitializer} and by
   * {@link SQLDataStorage#createUser(String, String, String)}.
   * 
   * @see #createUser(String, String, String)
   * @param name
   * @param login
   * @param password
   * @param ownerId
   * @return
   * @throws CredentialsException
   */
  protected abstract User createUser(String name, String login, String password, long ownerId) throws CredentialsException;

  /**
   * deletes a metadata owner artifact. If user creation fails, the already created uninitialized owner must be removed.
   * Also if a user is deleted, the owner must be deleted as well.
   * 
   * @param id
   * @throws OwnerDoesNotExistException
   */
  protected abstract void deleteOwner(long id) throws OwnerDoesNotExistException;

  private final List<DataStorageListener> listeners;

  protected AbstractDataStorage() {
    this.idGenerator = null;
    this.versionGenerator = null;
    this.privateVersionNumberGenerator = null;
    this.listeners = new CopyOnWriteArrayList<>();// new Vector<>();
  }

  @Override
  public void init() {
    if (this.idGenerator == null) {
      this.idGenerator = new AtomicLong(Math.max(this.getMaximumArtifactId(), RESERVERD_IDS));
      this.versionGenerator = new AtomicLong(Math.max(this.getHeadVersionNumber(), FIRST_VERSION));
      this.privateVersionNumberGenerator = new AtomicLong(Math.min(this.getPrivateHeadVersionNumber(), 0));

      this.userGenerator = new AtomicLong(this.getMaximumUserId());

      if (!this.existsArtifact(FIRST_VERSION, ROOT_TYPE_ID)) {
        logger.info("first time init");
        new InfrastructureInitializer(this).init();
      }

      // this.idGenerator = new AtomicLong(Math.max(this.getMaximumArtifactId(), RESERVERD_IDS));
      // this.versionGenerator = new AtomicLong(Math.max(this.getHeadVersionNumber(), FIRST_VERSION));
      // this.privateVersionNumberGenerator = new AtomicLong(Math.min(this.getPrivateHeadVersionNumber(), 0));
    }
  }

  // To implement methods in subclasses
  protected abstract void storeArtifact(final long version, final long owner, final long tool, final long id, final Long type, final Long pkg, final boolean alive) throws ArtifactDeadException;

  protected abstract void storeProperty(final long version, final long owner, final long tool, final long artifactId, final String name, final Object value, final boolean reference,
      final boolean alive) throws PropertyDeadException;

  protected abstract void storeArtifact(final Set<Long> versions, final long owner, final long tool, final long id, final Long type, final Long pkg, final boolean alive) throws ArtifactDeadException;

  protected abstract void storeProperty(final Set<Long> versions, final long owner, final long tool, final long artifactId, final String name, final Object value, final boolean reference,
      final boolean alive) throws PropertyDeadException;

  protected abstract void storePropertyValues(final Set<Long> versions, final long owner, final long tool, final long artifactId, Map<String, Object> properties, final boolean alive)
      throws PropertyDeadException;

  protected abstract void storePropertyReferences(final Set<Long> versions, final long owner, final long tool, final long artifactId, Map<String, Long> properties, final boolean alive)
      throws PropertyDeadException;

  protected abstract void storePrivateWorkspace(final long privateVersion, final long owner, final long tool, final String identifier, final long baseVersion, final Long parent,
      final PropagationType push, final PropagationType pull);

  protected abstract long getRealArtifactVersionNumber(final long version, final long id) throws ArtifactDoesNotExistException;

  protected abstract long getRealPropertyVersionNumber(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  protected abstract Set<String> getPropertyNames(final long version, final Long minVersion, final long id) throws ArtifactDoesNotExistException;

  protected abstract Set<String> getPropertyNames(final long version, final Long minVersion, final long id, final boolean alive) throws ArtifactDoesNotExistException;

  protected abstract Collection<Object[]> getProperties(final long version, final Long minVersion, final long id, final boolean alive) throws ArtifactDoesNotExistException;

  protected abstract Map<Long, Set<String>> getPropertiesByValue(final long version, final Object value, final boolean isReference);

  protected abstract boolean existsArtifactInVersion(final long version, final long id);

  protected abstract boolean existsPropertyInVersion(final long version, final long artifactId, final String propertyName);

  protected abstract Set<Object[]> getArtifactReps(final long version, final Long minVersion, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId);

  protected abstract Set<Object[]> getArtifactReps(final long version, final Long minVersion, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId,
      final String propertyName, final Object propertyValue, final Boolean propertyReference);

  protected abstract Map<String, Object[]> getReferencedArtifactsByName(final long version, final Long collection);

  protected abstract Set<Object[]> getVersionArtifacts(final long privateVersion);

  protected abstract Set<Object[]> getVersionProperties(final long privateVersion);

  protected abstract Set<Object[]> getVersionPropertiesOfArtifact(final long privateVersion, final long id);

  protected abstract List<Long> getArtifactCompleteHistoryVersionNumbers(final long id);

  protected abstract List<Long> getPropertyCompleteHistoryVersionNumbers(final long id, final String property);

  protected abstract long publishArtifact(final long privateVersion, final long id, final String message) throws ArtifactDoesNotExistException, ArtifactConflictException, WorkspaceEmptyException;

  protected abstract long publishProperty(final long privateVersion, final long id, final String property, final String message)
      throws ArtifactDoesNotExistException, PropertyDoesNotExistException, PropertyConflictException, WorkspaceEmptyException, PropertyNotCommitableException;

  protected abstract long publishVersion(final long privateVersion, final String message) throws VersionConflictException, WorkspaceEmptyException;

  protected abstract void deleteArtifact(final long privateVersion, final long id) throws ArtifactDoesNotExistException, ArtifactNotDeleteableException;

  protected abstract void deleteProperty(final long privateVersion, final long id, final String property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  protected abstract void deleteVersion(final long privateVersion);

  protected abstract void closeWorkspace(final long privateVersion) throws WorkspaceExpiredException, WorkspaceNotEmptyException;

  protected abstract void updateWorkspaceBaseVersionNumber(final long privateVersion, final long newBaseVersion) throws WorkspaceExpiredException;

  protected abstract void setPrivateVersionParent(final long privateVersion, final Long newParent);

  protected abstract void setPrivateVersionPull(final long privateVersion, final PropagationType type);

  protected abstract void setPrivateVersionPush(final long privateVersion, final PropagationType type);

  @Override
  public final long createArtifact(final long version, final long owner, final long tool, final Long type, final Long container) {
    logger.debug("createArtifact(version={}, owner={}, tool={}, type={}, container={})", version, owner, tool, type, container);
    Collection<ArtifactEvent> events = this.createArtifact(version, new HashSet<Long>(), owner, tool, type, container);
    this.fireArtifactEvent(events);
    return events.iterator().next().id;
  }

  @Override
  public final long createArtifact(final long version, final long owner, final long tool, final Long type, final Long container, final Long metamodel, final Long project,
      final Map<String, Object> valueMap, final Map<String, Long> referenceMap) {
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createArtifact(version, versions, owner, tool, type, container);
    long id = events.iterator().next().id;
    if (metamodel != null) {
      this.addElemToColl(version, versions, metamodel, owner, tool, id);
    }
    if (project != null) {
      this.addElemToColl(version, versions, project, owner, tool, id);
    }
    this.setPropertyValues(version, versions, owner, tool, id, valueMap, referenceMap);
    this.fireArtifactEvent(events);
    return id;
  }

  @Override
  public final long createOwner(final long owner, final long tool) {
    logger.debug("createOwner(owner={}, tool={})", owner, tool);
    final long version = this.versionGenerator.incrementAndGet();
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createArtifact(version, versions, owner, tool, DataStorage.OWNER_TYPE_ID, DataStorage.OWNER_PACKAGE_ID);
    fireArtifactEvent(events);
    return events.iterator().next().id;
  }

  @Override
  public final long createTool(final long owner, final long tool) {
    logger.debug("createTool(owner={}, tool={})", owner, tool);
    final long version = this.versionGenerator.incrementAndGet();
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createArtifact(version, versions, owner, tool, DataStorage.TOOL_TYPE_ID, DataStorage.TOOL_PACKAGE_ID);
    fireArtifactEvent(events);
    return events.iterator().next().id;
  }

  protected Collection<ArtifactEvent> createArtifact(final long version, final Set<Long> versions, final long owner, final long tool, Long type, final Long container) {
    final long id = this.idGenerator.incrementAndGet();
    versions.clear();
    if (version < VOID_VERSION && versions.size() == 0) {
      versions.addAll(WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version));
    } else {
      versions.add(version);
    }
    if (type == null) {
      type = DataStorage.ROOT_TYPE_ID;
    }
    // logger.debug("createArtifact(version={}, owner={}, tool={}, type={}, pckg={})", pId, owner, tool,
    // type, pckg);
    this.storeArtifact(versions, owner, tool, id, type, container, true);
    Collection<ArtifactEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createArtifactEvent(DataStorageEventType.CREATE, version, v, owner, tool, id, type, container, true, false));
    }
    return events;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#isArtifactAlive(long, long)
   */
  @Override
  public final boolean isArtifactAlive(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("isArtifactAlive(version={}, id={})", version, id);
    final boolean result = (Boolean) this.getArtifactRepresentation(version, id)[Columns.ALIVE.getIndex()];
    logger.debug("isArtifactAlive(version={}, id={}) => {}", version, id, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#setArtifactAlive(long, long, long, boolean)
   */
  @Override
  public final void setArtifactAlive(final long version, final long owner, final long tool, final long id, final boolean alive) throws ArtifactDoesNotExistException {
    logger.debug("setArtifactAlive(version={},owner={}, tool={}, id={}, alive={})", version, owner, tool, id, alive);
    final Object[] representation = this.getArtifactRepresentation(version, id);
    final Long pkg = (Long) representation[Columns.ARTIFACT_CONTAINER.getIndex()];
    final Long type = (Long) representation[Columns.ARTIFACT_TYPE.getIndex()];
    Set<Long> versions = new HashSet<>();
    this.updateArtifact(version, versions, id, owner, tool, type, pkg, alive);
    Collection<ArtifactEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createArtifactEvent(DataStorageEventType.ALIVE_SET, version, v, owner, tool, id, type, pkg, alive, false));
    }
    this.fireArtifactEvent(events);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#setArtifactType(long, long, long, long)
   */
  @Override
  public final void setArtifactType(final long version, final long owner, final long tool, final long id, final Long type) throws ArtifactDoesNotExistException, ArtifactDeadException {
    logger.debug("setArtifactType(version={}, owner={}, tool={}, id={}, type={})", version, owner, tool, id, type);
    final Object[] representation = this.getArtifactRepresentation(version, id);
    final Long pkg = (Long) representation[Columns.ARTIFACT_CONTAINER.getIndex()];
    final boolean alive = (Boolean) representation[Columns.ALIVE.getIndex()];
    if (!alive) {
      throw new ArtifactDeadException(version, id);
    }
    Set<Long> versions = new HashSet<>();
    this.updateArtifact(version, versions, id, owner, tool, type, pkg, true);
    Collection<ArtifactEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createArtifactEvent(DataStorageEventType.TYPE_SET, version, v, owner, tool, id, type, pkg, alive, false));
    }
    this.fireArtifactEvent(events);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#setArtifactContainer(long, long, long, long)
   */
  @Override
  public final void setArtifactContainer(final long version, final long owner, final long tool, final long id, final Long containerId) throws ArtifactDoesNotExistException, ArtifactDeadException {
    logger.debug("setArtifactContainer(version={}, owner={}, tool={}, id={}, containerId={})", version, owner, tool, id, containerId);
    final Object[] representation = this.getArtifactRepresentation(version, id);
    final Long type = (Long) representation[Columns.ARTIFACT_TYPE.getIndex()];
    final boolean alive = (Boolean) representation[Columns.ALIVE.getIndex()];
    if (!alive) {
      throw new ArtifactDeadException(version, id);
    }
    Set<Long> versions = new HashSet<>();
    this.updateArtifact(version, versions, id, owner, tool, type, containerId, true);
    Collection<ArtifactEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createArtifactEvent(DataStorageEventType.CONTAINER_SET, version, v, owner, tool, id, type, containerId, alive, false));
    }
    this.fireArtifactEvent(events);
  }

  private long updateArtifact(final long version, final Set<Long> versions, final long id, final long owner, final long tool, final Long type, final Long container, boolean alive) {
    versions.clear();
    if (version < VOID_VERSION && versions.size() == 0) {
      versions.addAll(WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version));
    } else if (version > VOID_VERSION) {
      versions.add(version);
    }
    this.storeArtifact(versions, owner, tool, id, type, container, alive);
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getArtifactType(long, long)
   */
  @Override
  public final Long getArtifactType(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getArtifactType(version={}, id={})", version, id);
    final Long result = (Long) this.getArtifactRepresentation(version, id)[Columns.ARTIFACT_TYPE.getIndex()];
    logger.debug("getArtifactType(version={}, id={}) => {}", version, id, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getArtifactContainer(long, long)
   */
  @Override
  public final Long getArtifactContainer(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getArtifactContainer(version={}, id={})", version, id);
    return (Long) this.getArtifactRepresentation(version, id)[Columns.ARTIFACT_CONTAINER.getIndex()];
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#isPropertyAlive(long, long, java.lang.String)
   */
  @Override
  public final boolean isPropertyAlive(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    logger.debug("isPropertyAlive(version={}, artifactId={}, propertyName={})", version, artifactId, propertyName);
    final Boolean result = (Boolean) this.getPropertyRepresentation(version, artifactId, propertyName)[Columns.ALIVE.getIndex()];
    logger.debug("isPropertyAlive(version={}, artifactId={}, propertyName={}) => {}", version, artifactId, propertyName, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#setPropertyAlive(long, long, long, java.lang.String, boolean)
   */
  @Override
  public final void setPropertyAlive(final long version, final long owner, final long tool, final long artifactId, final String propertyName, final boolean alive)
      throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    logger.debug("setPropertyAlive(version={}, owner={}, tool={}, artifactId={}, propertyName={}, alive={})", version, owner, tool, artifactId, propertyName, alive);
    final Object[] representation = this.getPropertyRepresentation(version, artifactId, propertyName);
    final Boolean reference = representation[Columns.PROPERTY_REFERENCE.getIndex()] != null;
    final Object value = reference ? representation[Columns.PROPERTY_REFERENCE.getIndex()] : representation[Columns.PROPERTY_VALUE.getIndex()];
    Set<Long> versions = new HashSet<>();
    if (version < VOID_VERSION && versions.size() == 0) {
      versions.addAll(WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version));
    } else if (version > VOID_VERSION) {
      versions.add(version);
    }
    this.storeProperty(versions, owner, tool, artifactId, propertyName, value, reference, alive);
    Collection<PropertyAliveSetEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createPropertyAliveSetEvent(version, v, owner, tool, artifactId, propertyName, alive));
    }
    this.fireSetPropertyAlive(events);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#existsArtifact(long, long)
   */
  @Override
  public final boolean existsArtifact(final long version, final long id) {
    logger.debug("existsArtifact(version={}, id={})", version, id);
    return this.getRealArtifactVersionNumber(version, id) != VOID_VERSION;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#hasProperty(long, long, java.lang.String)
   */
  @Override
  public final boolean hasProperty(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException {
    logger.debug("hasProperty(version={}, artifactId={}, propertyName={})", version, artifactId, propertyName);
    final boolean result = this.getRealPropertyVersionNumber(version, artifactId, propertyName) != VOID_VERSION;
    logger.debug("hasProperty(version={}, artifactId={}, propertyName={}) => {}", version, artifactId, propertyName, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#isPropertyReference(long, long, java.lang.String)
   */
  @Override
  public final boolean isPropertyReference(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    logger.debug("isPropertyReference(version={}, artifactId={}, propertyName={})", version, artifactId, propertyName);
    final boolean result = this.getPropertyRepresentation(version, artifactId, propertyName)[Columns.PROPERTY_REFERENCE.getIndex()] != null;
    logger.debug("isPropertyReference(version={}, artifactId={}, propertyName={}) => {}", version, artifactId, propertyName, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getPropertyValue(long, long, java.lang.String)
   */
  @Override
  public final Object getPropertyValue(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    logger.debug("getPropertyValue(version={}, artifactId={}, propertyName={})", version, artifactId, propertyName);
    final Object result = this.getPropertyRepresentation(version, artifactId, propertyName)[Columns.PROPERTY_VALUE.getIndex()];
    logger.debug("getPropertyValue(version={}, artifactId={}, propertyName={}) => {}", version, artifactId, propertyName, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getPropertyReference(long, long, java.lang.String)
   */
  @Override
  public final Long getPropertyReference(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    logger.debug("getPropertyReference(version={}, artifactId={}, propertyName={})", version, artifactId, propertyName);
    final Long result = (Long) this.getPropertyRepresentation(version, artifactId, propertyName)[Columns.PROPERTY_REFERENCE.getIndex()];
    logger.debug("getPropertyReference(version={}, artifactId={}, propertyName={}) => {}", version, artifactId, propertyName, result);
    return result;
  }

  @Override
  public final void setPropertyValues(final long version, final long owner, final long tool, final long artifactId, final Map<String, Object> valueMap, final Map<String, Long> referenceMap)
      throws ArtifactDoesNotExistException, PropertyDeadException {
    if ((referenceMap.size() + valueMap.size()) > 0) {
      Collection<PropertyMapSetEvent> events = this.setPropertyValues(version, new HashSet<Long>(), owner, tool, artifactId, valueMap, referenceMap);
      this.fireSetProperties(events);
    }
  }

  private Collection<PropertyMapSetEvent> setPropertyValues(final long version, Set<Long> versions, final long owner, final long tool, final long artifactId, final Map<String, Object> valueMap,
      final Map<String, Long> referenceMap) throws ArtifactDoesNotExistException, PropertyDeadException {
    Set<PropertyValueSetEvent> valueSet = new HashSet<>();
    Set<PropertyReferenceSetEvent> referenceSet = new HashSet<>();
    Collection<PropertyMapSetEvent> events = new ArrayList<>();
    if (!this.isArtifactAlive(version, artifactId)) {
      throw new ArtifactDeadException(version, artifactId);
    }
    Set<String> properties = new HashSet<>();
    if (valueMap != null) {
      properties.addAll(valueMap.keySet());
    }
    if (referenceMap != null) {
      properties.addAll(referenceMap.keySet());
    }
    for (String property : properties) {
      // boolean hasProperty = this.hasProperty(version, artifactId, property);
      Object[] prop = this.getPropertyRepresentationOrNull(version, artifactId, property);
      if (prop != null) {
        boolean isAlive = (Boolean) prop[DataStorage.Columns.ALIVE.getIndex()];
        if (!isAlive) {
          throw new PropertyDeadException(version, artifactId, property);
        }
      }
    }
    if (versions.size() == 0) {
      versions.addAll(WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version));
    }
    if (valueMap != null) {
      for (final Entry<String, Object> entry : valueMap.entrySet()) {
        valueSet.addAll(createPropertyValueSetEvents(version, versions, owner, tool, artifactId, entry.getKey(), entry.getValue()));
      }
      this.storePropertyValues(versions, owner, tool, artifactId, valueMap, true);
    }
    if (referenceMap != null) {
      for (final Entry<String, Long> entry : referenceMap.entrySet()) {
        referenceSet.addAll(createPropertyReferenceSetEvents(version, versions, owner, tool, artifactId, entry.getKey(), entry.getValue()));
      }
      this.storePropertyReferences(versions, owner, tool, artifactId, referenceMap, true);
    }
    for (Long v : versions) {
      Set<PropertyValueSetEvent> valueSetEventsOfVersion = new HashSet<>();
      for (PropertyValueSetEvent event : valueSet) {
        if (Objects.equals(v, event.version)) {
          valueSetEventsOfVersion.add(event);
        }
      }
      Set<PropertyReferenceSetEvent> referenceSetEventsOfVersion = new HashSet<>();
      for (PropertyReferenceSetEvent event : referenceSet) {
        if (Objects.equals(v, event.version)) {
          referenceSetEventsOfVersion.add(event);
        }
      }
      events.add(EventFactory.createPropertyMapSetEvent(valueSetEventsOfVersion, referenceSetEventsOfVersion));
    }
    return events;
  }

  @Override
  public final void setPropertyValue(final long version, final long owner, final long tool, final long artifactId, final String propertyName, final Object value)
      throws ArtifactDoesNotExistException, ArtifactDeadException, PropertyDeadException {
    this.fireSetPropertyValue(this.setPropertyValue(version, new HashSet<Long>(), owner, tool, artifactId, propertyName, value));
  }

  private Collection<PropertyValueSetEvent> setPropertyValue(final long version, final Set<Long> versions, final long owner, final long tool, final long artifactId, final String propertyName,
      final Object value) throws ArtifactDoesNotExistException, ArtifactDeadException, PropertyDeadException {
    logger.debug("setPropertyValue(version={}, owner={}, tool={}, artifactId={}, propertyName={}, value={})", version, owner, tool, artifactId, propertyName, value);

    if (!this.isArtifactAlive(version, artifactId)) {
      throw new ArtifactDeadException(version, artifactId);
    }
    Object[] property = this.getPropertyRepresentationOrNull(version, artifactId, propertyName);
    if (property != null && !(Boolean) property[Columns.ALIVE.getIndex()]) {
      throw new PropertyDeadException(version, artifactId, propertyName);
    }
    if (version < VOID_VERSION && versions.size() == 0) {
      versions.addAll(WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version));
    } else if (version > VOID_VERSION) {
      versions.add(version);
    }
    Collection<PropertyValueSetEvent> events = createPropertyValueSetEvents(version, versions, owner, tool, artifactId, propertyName, value);
    this.storeProperty(versions, owner, tool, artifactId, propertyName, value, false, true);
    return events;
  }

  private Collection<PropertyValueSetEvent> createPropertyValueSetEvents(final long version, final Set<Long> versions, final long owner, final long tool, final long artifactId,
      final String propertyName, final Object value) {
    Collection<PropertyValueSetEvent> events = new ArrayList<>();
    for (Long v : versions) {
      Object oldValue = null;
      boolean wasReference = false;
      Object[] vProperty = this.getPropertyRepresentationOrNull(v, artifactId, propertyName);
      if (vProperty != null) {
        oldValue = this.getPropertyValue(vProperty);
        wasReference = vProperty[Columns.PROPERTY_REFERENCE.getIndex()] == null;
      }
      events.add(EventFactory.createPropertyValueSetEvent(version, v, owner, tool, artifactId, propertyName, oldValue, value, wasReference));
    }
    return events;
  }

  private Object getPropertyValue(Object[] property) {
    return property[Columns.PROPERTY_REFERENCE.getIndex()] == null ? property[Columns.PROPERTY_VALUE.getIndex()] : property[Columns.PROPERTY_REFERENCE.getIndex()];
  }

  @Override
  public final void setPropertyReference(final long version, final long owner, final long tool, final long artifactId, final String propertyName, final long referenceId)
      throws ArtifactDoesNotExistException, ArtifactDeadException {
    this.fireSetPropertyReference(this.setPropertyReference(version, new HashSet<Long>(), owner, tool, artifactId, propertyName, referenceId));
  }

  private Collection<PropertyReferenceSetEvent> setPropertyReference(final long version, final Set<Long> versions, final long owner, final long tool, final long artifactId, final String propertyName,
      final long referenceId) throws ArtifactDoesNotExistException, ArtifactDeadException {
    logger.debug("setPropertyReference(version={}, owner={}, tool={}, artifactId={}, propertyName={}, referenceId={})", version, owner, tool, artifactId, propertyName, referenceId);
    if (!this.isArtifactAlive(version, artifactId)) {
      throw new ArtifactDeadException(version, artifactId);
    }
    Object[] property = this.getPropertyRepresentationOrNull(version, artifactId, propertyName);
    if (property != null && !(Boolean) property[Columns.ALIVE.getIndex()]) {
      throw new PropertyDeadException(version, artifactId, propertyName);
    }
    if (version < VOID_VERSION && versions.size() == 0) {
      versions.addAll(WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version));
    } else if (version > VOID_VERSION) {
      versions.add(version);
    }
    Collection<PropertyReferenceSetEvent> events = createPropertyReferenceSetEvents(version, versions, owner, tool, artifactId, propertyName, referenceId);
    this.storeProperty(versions, owner, tool, artifactId, propertyName, referenceId, true, true);
    return events;
  }

  private Collection<PropertyReferenceSetEvent> createPropertyReferenceSetEvents(final long version, final Set<Long> versions, final long owner, final long tool, final long artifactId,
      final String propertyName, final long referenceId) {
    Collection<PropertyReferenceSetEvent> events = new ArrayList<>();
    for (Long v : versions) {
      // hasProperty = this.hasProperty(v, artifactId, propertyName);
      Object[] vProperty = this.getPropertyRepresentationOrNull(version, artifactId, propertyName);
      Long oldReferenceId = vProperty != null ? (Long) vProperty[DataStorage.Columns.PROPERTY_REFERENCE.getIndex()] : null;
      events.add(EventFactory.createPropertyReferenceSetEvent(version, v, owner, tool, artifactId, propertyName, oldReferenceId, referenceId));
    }
    return events;
  }

  @Override
  public final Set<String> getAlivePropertyNames(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getAlivePropertyNames(version={}, id={})", version, id);
    final Set<String> result = this.getPropertyNames(version, null, id, true);
    logger.debug("getAlivePropertyNames(version={}, id={}) => {}", version, id, result);
    return result;
  }

  @Override
  public final Set<String> getAlivePropertyNames(final long version, final long minVersion, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getAlivePropertyNames(version={}, id={}, minVersion={})", version, id, minVersion);
    final Set<String> result = this.getPropertyNames(version, minVersion, id, true);
    logger.debug("getAlivePropertyNames(version={}, id={}, minVersion={}) => {}", version, id, minVersion, result);
    return result;
  }

  @Override
  public final Set<String> getAllPropertyNames(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getAllProperties(version={}, id={})", version, id);
    // final Set<String> all = this.getPropertyNames(version, null, id, true);
    // final Set<String> deadPropertyNames = this.getPropertyNames(version, null, id, false);
    // if (deadPropertyNames != null) {
    // all.addAll(deadPropertyNames);
    // }
    final Set<String> all = this.getPropertyNames(version, null, id);
    logger.debug("getAllProperties(version={}, id={}) => {}", version, id, all);
    return all;
  }

  @Override
  public final Set<String> getAllPropertyNames(final long version, final long minVersion, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getAllProperties(version={}, id={}, minVersion={})", version, id, minVersion);
    // final Set<String> all = this.getPropertyNames(version, minVersion, id, true);
    // all.addAll(this.getPropertyNames(version, minVersion, id, false));
    final Set<String> all = this.getPropertyNames(version, minVersion, id);
    logger.debug("getAllProperties(version={}, id={}, minVersion={}) => {}", version, id, minVersion, all);
    return all;
  }

  @Override
  public final Collection<Object[]> getAllPropertyRepresentations(final long version, final Long minVersion, final long artifactId, boolean alive)
      throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    return this.getProperties(version, minVersion, artifactId, alive);
  }

  @Override
  public final synchronized long createWorkspace(final long owner, final long tool, final String identifier, final long baseVersion, final Long parent, final PropagationType push,
      final PropagationType pull) {
    logger.debug("createPrivateWorkspace(owner={}, tool={}, identifier={}, baseVersion={})", owner, tool, identifier, baseVersion);
    final long privateVersion = this.privateVersionNumberGenerator.decrementAndGet();
    this.storePrivateWorkspace(privateVersion, owner, tool, identifier, baseVersion, parent, push, pull);
    this.fireAddedPrivateVersion(EventFactory.createPrivateVersionAddedEvent(privateVersion));
    return privateVersion;
  }

  @Override
  public final long createCollection(final long version, final long owner, final long tool, final Long container, final boolean containsOnlyReferences) {
    logger.debug("createCollection(version={}, owner={}, tool={}, ordered={}, container={}, containsOnlyReferences={}, container={})",
        version, owner, tool, container, containsOnlyReferences, container);
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createCollection(version, versions, owner, tool, COLLECTION_TYPE_ID, container, containsOnlyReferences);
    fireArtifactEvent(events);
    return events.iterator().next().id;
  }

  private Collection<ArtifactEvent> createCollection(final long version, final Set<Long> versions, final long owner, final long tool, final long type, final Long container,
      final boolean containsOnlyReferences) {
    logger.debug("createCollection(version={}, owner={}, tool={}, ordered={}, type={}, container={}, containsOnlyReferences={})",
        version, owner, tool, type, container, containsOnlyReferences);
    Collection<ArtifactEvent> events = this.createArtifact(version, versions, owner, tool, type, container);
    long collectionArtifactId = events.iterator().next().id;
    this.setPropertyValue(version, versions, owner, tool, collectionArtifactId, PROPERTY_COLLECTION_NEXT_IDX, 0l);
    this.setPropertyValue(version, versions, owner, tool, collectionArtifactId, PROPERTY_COLLECTION_REFERENCES, containsOnlyReferences);
    return events;
  }

  @Override
  public long createCollection(final long version, final long owner, final long tool, final Long container, final boolean containsOnlyReferences, Collection<?> elements, Map<String, Object> valueMap,
      Map<String, Long> referenceMap) {
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createCollection(version, versions, owner, tool, COLLECTION_TYPE_ID, container, containsOnlyReferences);
    long collectionId = events.iterator().next().id;
    this.setPropertyValues(version, versions, owner, tool, collectionId, valueMap, referenceMap);
    this.addElemsToCollection(version, versions, collectionId, owner, tool, elements);
    fireArtifactEvent(events);
    return collectionId;
  }

  @Override
  public long createResource(final long version, final long owner, final long tool, final Long container, final Long projectId, Map<String, Object> valueMap, Map<String, Long> referenceMap,
      Collection<Long> artifacts) {
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createArtifact(version, versions, owner, tool, RESOURCE_TYPE_ID, container);
    long resourceId = events.iterator().next().id;
    this.setPropertyValues(version, versions, owner, tool, resourceId, valueMap, referenceMap);
    for (Long artifact : artifacts) {
      final Object[] representation = this.getArtifactRepresentation(version, artifact);
      final Long type = (Long) representation[Columns.ARTIFACT_TYPE.getIndex()];
      this.updateArtifact(version, versions, artifact, owner, tool, type, resourceId, true);
    }
    fireArtifactEvent(events);
    return resourceId;
  }

  @Override
  public final long createMap(final long version, final long owner, final long tool, final Long container) {
    logger.debug("createMap(version={}, owner={}, tool={}, container={})", version, owner, tool, container);
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createCollection(version, versions, owner, tool, COLLECTION_TYPE_ID, null, true);
    final Long collection = events.iterator().next().id;
    Collection<ArtifactEvent> mapEvents = this.createArtifact(version, versions, owner, tool, MAP_TYPE_ID, container);
    final Long map = mapEvents.iterator().next().id;
    this.setPropertyReference(version, versions, owner, tool, map, PROPERTY_MAP_COLLECTION, collection);
    this.fireArtifactEvent(mapEvents);
    return map;
  }

  @Override
  public final long createProject(final long version, final long owner, final long tool, final String name) {
    logger.debug("createProject(version={}, owner={}, tool={}", version, owner, tool);
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createCollection(version, versions, owner, tool, PROJECT_TYPE_ID, null, true);
    fireArtifactEvent(events);
    final long id = events.iterator().next().id;
    
    if(name != null)
      this.storeProperty(version, owner, tool, id, PROPERTY_NAME, name, false, true);
    return id;
  }
  
  @Override
  public final long createPackage(final long version, final long owner, final long tool, final String name, final Long parent) {
    logger.debug("createProject(version={}, owner={}, tool={}", version, owner, tool);
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createCollection(version, versions, owner, tool, PACKAGE_TYPE_ID, parent, true);
    fireArtifactEvent(events);
    final long id = events.iterator().next().id;
    
    if(name != null)
      this.storeProperty(version, owner, tool, id, PROPERTY_NAME, name, false, true);
    
    return id;
  }

  @Override
  public final long createMetaModel(final long version, final long owner, final long tool, final Long container) {
    logger.debug("createProject(version={}, owner={}, tool={),  container={}", version, owner, tool, container);
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createCollection(version, versions, owner, tool, META_MODEL_TYPE_ID, container, true);
    fireArtifactEvent(events);
    return events.iterator().next().id;
  }

  @Override
  public final boolean isCollection(final long version, final long id) throws ArtifactDoesNotExistException {
    final Long artifactType = this.getArtifactType(version, id);
    final boolean result = ((artifactType != null) && ((artifactType == COLLECTION_TYPE_ID) || (artifactType == PROJECT_TYPE_ID) || (artifactType == META_MODEL_TYPE_ID)));
    logger.debug("isCollection(version={}, id={}) => {}", version, id, result);
    return result;
  }

  @Override
  public final boolean isReferenceCollection(final long version, final long id) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    if (!this.isCollection(version, id)) {
      throw new ArtifactIsNotACollectionException(version, id);
    }
    final boolean propertyValue = (boolean) this.getPropertyValue(version, id, PROPERTY_COLLECTION_REFERENCES);
    logger.debug("isReferenceCollection(version={}, id={}) => {}", version, id, propertyValue);
    return propertyValue;
  }

  protected Long getNextCollectionIndex(final long version, final long id) {
    return (Long) this.getPropertyValue(version, id, DataStorage.PROPERTY_COLLECTION_NEXT_IDX);
  }

  @Override
  public final void addElementToCollection(final long version, final long id, final long owner, final long tool, final Object element) throws ArtifactDeadException, ArtifactDoesNotExistException {
    logger.debug("addElementToCollection(version={}, id={}, owner={}, tool={}, element={})", version, id, owner, tool, element);
    this.fireCollectionAddedElementToCollection(this.addElemToColl(version, new HashSet<Long>(), id, owner, tool, element));
  }

  @Override
  public final void addElementsToCollection(final long version, final long id, final long owner, long tool, final Collection<?> elements)
      throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException, ArtifactDeadException {
    Set<Long> versions = WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version);
    this.fireCollectionAddedElementsToCollection(addElemsToCollection(version, versions, id, owner, tool, elements));
  }

  private Collection<CollectionAddedElementsEvent> addElemsToCollection(final long version, Set<Long> versions, final long id, final long owner, long tool, final Collection<?> elements) {
    if (!this.isArtifactAlive(version, id)) {
      throw new ArtifactDeadException(version, id);
    }
    if (!this.isCollection(version, id)) {
      throw new ArtifactIsNotACollectionException(version, id);
    }
    if (version < VOID_VERSION && versions.size() == 0) {
      versions.addAll(WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version));
    } else {
      versions.add(version);
    }
    for (Long wsId : versions) {
      final boolean reference = this.isReferenceCollection(wsId, id);
      if (reference) {
        addArtifactsToCollection(version, id, owner, tool, (Collection<Long>) elements);
      } else {
        addObjectsToCollection(version, id, owner, tool, elements);
      }
    }
    Collection<CollectionAddedElementsEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createCollectionAddedElementsEvent(version, v, owner, tool, id, elements));
    }
    return events;
  }

  private void addArtifactsToCollection(final Long version, final long id, final long owner, final long tool, Collection<Long> elements) {
    Long index = this.getNextCollectionIndex(version, id);
    Map<String, Long> propertyReferences = new HashMap<>();
    for (Object element : elements) {
      propertyReferences.put(StringUtils.printf("%050i", index), (Long) element);
      index++;
    }

    this.storePropertyReferences(new HashSet<Long>(Arrays.asList(new Long[] { version })), owner, tool, id, propertyReferences, true);
    this.storeProperty(version, owner, tool, id, PROPERTY_COLLECTION_NEXT_IDX, index, false, true);
  }

  private void addObjectsToCollection(final Long version, final long id, final long owner, final long tool, Collection<?> elements) {
    Long index = this.getNextCollectionIndex(version, id);
    Map<String, Object> propertyValues = new HashMap<>();
    for (Object element : elements) {
      propertyValues.put(StringUtils.printf("%050i", index), element);
      index++;
    }
    propertyValues.put(PROPERTY_COLLECTION_NEXT_IDX, index);
    this.storePropertyValues(new HashSet<Long>(Arrays.asList(new Long[] { version })), owner, tool, id, propertyValues, true);
  }

  private Collection<CollectionAddedElementEvent> addElemToColl(final long version, final Set<Long> versions, final long id, final long owner, final long tool, final Object element)
      throws ArtifactDeadException, ArtifactDoesNotExistException {
    if (version < VOID_VERSION && versions.size() == 0) {
      versions.addAll(WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version));
    } else if (version > VOID_VERSION) {
      versions.add(version);
    }
    if (!this.isArtifactAlive(version, id)) {
      throw new ArtifactDeadException(version, id);
    }
    if (!this.isCollection(version, id)) {
      throw new ArtifactIsNotACollectionException(version, id);
    }
    for (Long wId : versions) {
      addElement(wId, id, owner, tool, element);
    }
    Collection<CollectionAddedElementEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createCollectionAddedElementEvent(version, v, owner, tool, id, element));
    }
    return events;
  }

  protected void addElement(final long version, final long id, final long owner, final long tool, final Object element) {
    final boolean reference = this.isReferenceCollection(version, id);
    final Long index = this.getNextCollectionIndex(version, id);
    final String propertyName = StringUtils.printf("%050i", index);

    this.storeProperty(version, owner, tool, id, propertyName, element, reference, true);
    this.storeProperty(version, owner, tool, id, PROPERTY_COLLECTION_NEXT_IDX, index + 1, false, true);
  }

  @Override
  public final void insertElementInCollectionAt(final long version, final long id, final long owner, final long tool, final Object element, final long index)
      throws ArtifactDeadException, ArtifactDoesNotExistException {
    Set<Long> versions = WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version);
    for (Long wsId : versions) {
      addElementAtPosition(wsId, id, owner, tool, element, index);
    }
    Collection<CollectionAddedElementEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createCollectionAddedElementEvent(version, v, owner, tool, id, element));
    }
    this.fireCollectionAddedElementToCollection(events);
  }

  private void addElementAtPosition(final long version, final long id, final long owner, final long tool, final Object element, final long index) {
    if (!this.isArtifactAlive(version, id)) {
      throw new ArtifactDeadException(version, id);
    }
    final boolean reference = this.isReferenceCollection(version, id);
    final Long maxIdx = this.getNextCollectionIndex(version, id);
    if ((index > maxIdx) || (index < 0)) {
      throw new IndexOutOfBoundsException();
    }
    for (Long it = maxIdx; it > index; it--) {
      final String prev = StringUtils.printf("%050i", it - 1);
      final String curr = StringUtils.printf("%050i", it);
      final Object value = reference ? this.getPropertyReference(version, id, prev) : this.getPropertyValue(version, id, prev);
      this.storeProperty(version, owner, tool, id, curr, value, reference, true);
    }
    final String curr = StringUtils.printf("%050i", index);
    this.storeProperty(version, owner, tool, id, curr, element, reference, true);
    this.storeProperty(version, owner, tool, id, DataStorage.PROPERTY_COLLECTION_NEXT_IDX, maxIdx + 1, false, true);
  }

  @Override
  public final boolean existsElementInCollection(final long version, final long id, final Object element) throws ArtifactDoesNotExistException {
    final boolean result = this.getIndexOfElementInCollection(version, id, element) != -1l;
    logger.debug("existsElementInCollection(version={}, id={}, element={}) => {}", version, id, element, result);
    return result;
  }

  @Override
  public final Collection<Object> getElementsFromCollection(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getElementsFromCollection(version={}, id={})", version, id);
    if (!this.isCollection(version, id)) {
      throw new ArtifactIsNotACollectionException(version, id);
    }
    final Collection<Object> result = new ArrayList<Object>();
    final boolean reference = this.isReferenceCollection(version, id);
    // for (Long it = 0l; it < this.getNextCollectionIndex(version, id); it++) {
    // final String idxProp = StringUtils.printf("%050i", it);
    // final Object idxElem = reference ? this.getPropertyReference(version, id, idxProp) :
    // this.getPropertyValue(version, id, idxProp);
    // result.add(idxElem);
    // }
    //
    List<Object[]> properties = new ArrayList<>(this.getAllPropertyRepresentations(version, null, id, true));
    Iterator<Object[]> iterator = properties.iterator();
    while (iterator.hasNext()) {
      Object[] property = iterator.next();
      String name = (String) property[Columns.PROPERTY_NAME.getIndex()];
      if (!COLLECTION_ELEMENT.matcher(name).matches()) {
        iterator.remove();
      }
    }
    Collections.sort(properties, new PropertyNameComparator());

    iterator = properties.iterator();
    while (iterator.hasNext()) {
      if (reference) {
        result.add(iterator.next()[Columns.PROPERTY_REFERENCE.getIndex()]);
      } else {
        result.add(iterator.next()[Columns.PROPERTY_VALUE.getIndex()]);
      }
    }
    return result;
  }

  @Override
  public Collection<Object[]> getArtifactsFromCollection(long version, long id) throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException {
    if (!this.isCollection(version, id)) {
      throw new ArtifactIsNotACollectionException(version, id);
    }

    Map<String, Object[]> map = this.getReferencedArtifactsByName(version, id);
    Collection<Object[]> result = new ArrayList<>();
    List<String> propertyNames = new ArrayList<>(map.keySet());
    Iterator<String> iterator = propertyNames.iterator();
    while (iterator.hasNext()) {
      String name = iterator.next();
      if (!COLLECTION_ELEMENT.matcher(name).matches()) {
        iterator.remove();
      }
    }
    Collections.sort(propertyNames);

    for (String name : propertyNames) {
      result.add(map.get(name));
    }
    return result;
  }

  private class PropertyNameComparator implements Comparator<Object[]> {

    @Override
    public int compare(Object[] p1, Object[] p2) {
      String p1Name = (String) p1[Columns.PROPERTY_NAME.getIndex()];
      String p2Name = (String) p2[Columns.PROPERTY_NAME.getIndex()];
      return p1Name.compareTo(p2Name);
    }

  }

  @Override
  public final void removeElementAtFromCollection(final long version, final long id, final long owner, final long tool, final long index) throws ArtifactDeadException, ArtifactDoesNotExistException {
    logger.debug("removeElementFromCollectionAt(version={}, id={}, owner={}, tool={}, index={})", version, id, owner, tool, index);
    final Object deleted = removeElementAt(version, id, owner, tool, index);
    Set<Long> versions = WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version);
    for (Long wsId : versions) {
      if (wsId != version) {
        this.removeElementFromCollection(wsId, id, owner, tool, deleted);
      }
    }
    Collection<CollectionRemovedElementEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createCollectionRemovedElementEvent(version, v, owner, tool, id, deleted));
    }
    this.fireCollectionRemovedElementFromCollection(events);
  }

  protected Object removeElementAt(final long version, final long id, final long owner, final long tool, final long index) {
    if (!this.isArtifactAlive(version, id)) {
      throw new ArtifactDeadException(version, id);
    }
    final boolean reference = this.isReferenceCollection(version, id);
    final Long maxIdx = this.getNextCollectionIndex(version, id);
    if ((index >= maxIdx) || (index < 0)) {
      throw new IndexOutOfBoundsException();
    }
    final String delProp = StringUtils.printf("%050i", index);
    final Object deleted = reference ? this.getPropertyReference(version, id, delProp) : this.getPropertyValue(version, id, delProp);
    for (Long it = index; it < (maxIdx - 1); it++) {
      final String next = StringUtils.printf("%050i", it + 1);
      final String curr = StringUtils.printf("%050i", it);
      final Object value = reference ? this.getPropertyReference(version, id, next) : this.getPropertyValue(version, id, next);
      this.storeProperty(version, owner, tool, id, curr, value, reference, true);
    }
    final String curr = StringUtils.printf("%050i", maxIdx - 1);
    // this.setPropertyAlive(version, owner, tool, id, curr, false);
    final Object value = reference ? this.getPropertyReference(version, id, curr) : this.getPropertyValue(version, id, curr);
    this.storeProperty(version, owner, tool, id, curr, value, reference, false);
    this.storeProperty(version, owner, tool, id, DataStorage.PROPERTY_COLLECTION_NEXT_IDX, maxIdx - 1, false, true);
    return deleted;
  }

  protected Long getIndexOfElementInCollection(final long version, final long id, final Object element) throws ArtifactIsNotACollectionException {
    if (!this.isCollection(version, id)) {
      throw new ArtifactIsNotACollectionException(version, id);
    }
    final boolean reference = this.isReferenceCollection(version, id);
    final Long maxIdx = this.getNextCollectionIndex(version, id);
    for (Long it = 0l; it < maxIdx; it++) {
      final String idxProp = StringUtils.printf("%050i", it);
      final Object idxElem = reference ? this.getPropertyReference(version, id, idxProp) : this.getPropertyValue(version, id, idxProp);
      if (Objects.equals(idxElem, element)) {
        return it;
      }
    }
    // Collection<Object> collection = this.getElementsFromCollection(version, id);
    // Iterator<Object> iterator = collection.iterator();
    // Long it = 0l;
    // while (iterator.hasNext()) {
    // if (Objects.equals(iterator.next(), element)) {
    // return it;
    // }
    // it++;
    // }
    return -1l;
  }

  @Override
  public final void removeElementFromCollection(final long version, final long id, final long owner, final long tool, final Object element)
      throws ArtifactDeadException, ArtifactDoesNotExistException, ArtifactIsNotACollectionException {
    logger.debug("removeElementFromCollection(version={}, id={}, owner={}, tool={}, element={})", version, id, owner, tool, element);
    this.fireCollectionRemovedElementFromCollection(this.removeElemFromColl(version, new HashSet<Long>(), id, owner, tool, element));
  }

  private Collection<CollectionRemovedElementEvent> removeElemFromColl(final long version, final Set<Long> versions, final long id, final long owner, final long tool, final Object element)
      throws ArtifactDeadException, ArtifactDoesNotExistException, ArtifactIsNotACollectionException {
    if (!this.isArtifactAlive(version, id)) {
      throw new ArtifactDeadException(version, id);
    }
    if (!this.isCollection(version, id)) {
      throw new ArtifactIsNotACollectionException(version, id);
    }
    Object deleted = null;
    if (version < VOID_VERSION && versions.size() == 0) {
      versions.addAll(WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version));
    } else if (version > VOID_VERSION) {
      versions.add(version);
    }
    for (Long wId : versions) {
      final Long index = this.getIndexOfElementInCollection(wId, id, element);
      if (index == -1) {
        continue;
      }
      deleted = this.removeElementAt(wId, id, owner, tool, index);
    }
    Collection<CollectionRemovedElementEvent> events = new ArrayList<>();
    for (Long v : versions) {
      events.add(EventFactory.createCollectionRemovedElementEvent(version, v, owner, tool, id, deleted));
    }
    return events;
  }

  @Override
  public final Object getElementAtFromCollection(final long version, final long id, final long index)
      throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException, IndexOutOfBoundsException {
    logger.debug("getElementAtFromCollection(version={}, id={}, index={})", version, id, index);
    if (index >= this.getNextCollectionIndex(version, id)) {
      throw new IndexOutOfBoundsException();
    }
    final boolean reference = this.isReferenceCollection(version, id);
    final String idxProp = StringUtils.printf("%050i", index);
    final Object value = reference ? this.getPropertyReference(version, id, idxProp) : this.getPropertyValue(version, id, idxProp);
    logger.debug("getElementAtFromCollection(version={}, id={}, index={}) => {}", version, id, index, value);
    return value;
  }

  // MapArtifact

  @Override
  public void clearMap(long version, long mapId, long owner, long tool) throws ArtifactDeadException, ArtifactDoesNotExistException, PropertyDoesNotExistException {
    Collection<MapClearedEvent> events = clearMapArtifact(version, mapId, owner, tool);
    fireMapCleared(events);
  }

  protected Collection<MapClearedEvent> clearMapArtifact(long version, long mapId, long owner, long tool) throws ArtifactDeadException, ArtifactDoesNotExistException, PropertyDoesNotExistException {
    Set<Long> versions = WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version);
    Collection<MapClearedEvent> events = new ArrayList<>();
    for (Long v : versions) {
      long collectionArtifactId = getPropertyReference(v, mapId, DataStorage.PROPERTY_MAP_COLLECTION);
      long collectionArtifactSize = (long) getPropertyValue(v, collectionArtifactId, DataStorage.PROPERTY_COLLECTION_NEXT_IDX);
      for (long i = collectionArtifactSize - 1; i >= 0; i--) {
        removeElementAt(v, collectionArtifactId, owner, tool, i);
      }
      events.add(EventFactory.createMapClearedEvent(version, v, owner, tool, mapId));
    }
    return events;
  }

  @Override
  public void putInMap(long version, long mapId, long owner, long tool, String keyName, Object key, boolean isKeyReference, String valueName, Object value, boolean isValueReference) {
    Collection<MapPutEvent> events = putInMapArtifact(version, mapId, owner, tool, keyName, key, isKeyReference, valueName, value, isValueReference);
    fireMapPut(events);
  }

  protected Collection<MapPutEvent> putInMapArtifact(long version, long mapId, long owner, long tool, String keyName, Object key, boolean isKeyReference, String valueName, Object value,
      boolean isValueReference) {
    Set<Long> versions = WorkspaceHierarchyUtils.collectAllWorkspaceIdsInInstantPath(this, version);
    Collection<MapPutEvent> events = new ArrayList<>();
    for (Long v : versions) {
      long collectionArtifactId = getPropertyReference(v, mapId, DataStorage.PROPERTY_MAP_COLLECTION);
      Long artifactId = getElementFromCollectionWithPropertyValue(v, collectionArtifactId, keyName, key, isKeyReference);
      if (artifactId != null) {
        boolean isOldValueReference = isPropertyReference(v, artifactId, valueName);
        Object oldValue = isOldValueReference ? getPropertyReference(v, artifactId, valueName) : getPropertyValue(v, artifactId, valueName);
        if (isValueReference) {
          setPropertyReference(v, versions, owner, tool, artifactId, valueName, (long) value);
        } else {
          setPropertyValue(v, versions, owner, tool, artifactId, valueName, value);
        }
        // return Collections.singleton);
        events.add(EventFactory.createMapPutEvent(version, v, owner, tool, mapId, key, isKeyReference, oldValue, isOldValueReference, value, isValueReference, false));
        break;
      }

      artifactId = createArtifact(v, owner, tool, DataStorage.ROOT_TYPE_ID, null);
      // this.addElementToCollection(v, collectionArtifactId, owner, tool, artifactId);
      this.addElemToColl(v, new HashSet<Long>(), collectionArtifactId, owner, tool, artifactId);
      if (isKeyReference) {
        setPropertyReference(v, versions, owner, tool, artifactId, keyName, (long) key);
      } else {
        setPropertyValue(v, versions, owner, tool, artifactId, keyName, key);
      }

      if (isValueReference) {
        setPropertyReference(v, versions, owner, tool, artifactId, valueName, (long) value);
      } else {
        setPropertyValue(v, versions, owner, tool, artifactId, valueName, value);
      }
      events.add(EventFactory.createMapPutEvent(version, v, owner, tool, mapId, key, isKeyReference, null, false, value, isValueReference, true));
    }
    return events;
  }

  private Long getElementFromCollectionWithPropertyValue(long version, long collectionArtifactId, String keyName, Object key, boolean isKeyReference) {
    Collection<Object> elements = getElementsFromCollection(version, collectionArtifactId);
    for (Object o : elements) {
      if (o instanceof Long) {
        long artifactId = (Long) o;
        boolean isStoredKeyReference = isPropertyReference(version, artifactId, keyName);
        if (isStoredKeyReference == isKeyReference) {
          Object storedKey = isStoredKeyReference ? getPropertyReference(version, artifactId, keyName) : getPropertyValue(version, artifactId, keyName);
          if (key == null ? key == storedKey : key.equals(storedKey)) {
            return artifactId;
          }
        }
      }
    }
    return null;
  }

  @Override
  public void removeFromMap(long version, long mapId, long owner, long tool, String keyName, Object key, boolean isKeyReference, String valueName) {
    Collection<MapRemovedElementEvent> events = removeFromMapArtifact(version, mapId, owner, tool, keyName, key, isKeyReference, valueName);
    fireMapRemovedElement(events);
  }

  protected Collection<MapRemovedElementEvent> removeFromMapArtifact(long version, long mapId, long owner, long tool, String keyName, Object key, boolean isKeyReference, String valueName) {
    Set<Long> versions = WorkspaceHierarchyUtils.collectAllChildrenWorkspaceIdsInInstantPath(this, version);
    Collection<MapRemovedElementEvent> events = new ArrayList<>();
    for (Long v : versions) {
      long collectionArtifactId = getPropertyReference(v, mapId, DataStorage.PROPERTY_MAP_COLLECTION);
      Long artifactId = getElementFromCollectionWithPropertyValue(v, collectionArtifactId, keyName, key, isKeyReference);
      if (artifactId != null) {
        boolean isValueReference = isPropertyReference(v, artifactId, valueName);
        Object value = isValueReference ? getPropertyReference(v, artifactId, valueName) : getPropertyValue(v, artifactId, valueName);
        // removeElementFromCollection(v, collectionArtifactId, owner, tool, artifactId);
        this.removeElemFromColl(v, new HashSet<Long>(), collectionArtifactId, owner, tool, artifactId);
        events.add(EventFactory.createMapRemovedElementEvent(version, v, owner, tool, mapId, key, isKeyReference, value, isValueReference));
      }
    }
    return events;
  }

  @Override
  public final Set<Object[]> getAliveArtifacts(final long version) {
    logger.debug("getAliveArtifacts(version={})", version);
    return this.getArtifacts(version, true);
  }

  @Override
  public final Set<Object[]> getAliveArtifacts(final long version, final long minVersion) {
    logger.debug("getAliveArtifacts(version={}, minVersion={})", version, minVersion);
    return this.getArtifacts(version, minVersion, true);
  }

  @Override
  public final Set<Object[]> getAllArtifacts(final long version) {
    logger.debug("getAllArtifacts(version={})", version);
    final Set<Object[]> all = this.getArtifacts(version, false);
    all.addAll(this.getArtifacts(version, true));
    return all;
  }

  @Override
  public final Set<Object[]> getAllArtifacts(final long version, final long minVersion) {
    logger.debug("getAllArtifacts(version={}, minVersion={})", version, minVersion);
    final Set<Object[]> all = this.getArtifacts(version, minVersion, false);
    all.addAll(this.getArtifacts(version, minVersion, true));
    return all;
  }

  private Set<Object[]> getArtifacts(final long version, final boolean alive) {
    return this.getArtifactReps(version, null, alive, null, null, null, null);
  }

  private Set<Object[]> getArtifacts(final long version, final long minVersion, final boolean alive) {
    return this.getArtifactReps(version, minVersion, alive, null, null, null, null);
  }

  @Override
  public final Set<Object[]> getArtifactsByType(final long version, final long id) {
    logger.debug("getArtifactsByType(version={}, typeId={})", version, id);
    return this.getArtifactReps(version, null, true, null, null, id, null);
  }

  @Override
  public final Set<Object[]> getArtifactsByType(final long version, final long minVersion, final long id) {
    logger.debug("getArtifactsByType(version={}, minVersion={}, typeId={})", version, minVersion, id);
    return this.getArtifactReps(version, minVersion, true, null, null, id, null);
  }

  @Override
  public final Set<Object[]> getArtifactsByContainer(final long version, final long id) {
    logger.debug("getArtifactsByContainer(version={}, containerId={})", version, id);
    return this.getArtifactReps(version, null, true, null, null, null, id);
  }

  @Override
  public final Set<Object[]> getArtifactsByContainer(final long version, final long minVersion, final long id) {
    logger.debug("getArtifactsByContainer(version={}, minVersion={}, containerId={})", version, minVersion, id);
    return this.getArtifactReps(version, minVersion, true, null, null, null, id);
  }

  @Override
  public final Set<Object[]> getArtifacts(final long version, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId) {
    return this.getArtifactReps(version, null, alive, toolId, ownerId, typeId, containerId);
  }

  @Override
  public final Set<Object[]> getArtifacts(final long version, final long minVersion, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId) {
    return this.getArtifactReps(version, minVersion, alive, toolId, ownerId, typeId, containerId);
  }

  @Override
  public final Set<Object[]> getArtifacts(long version, boolean alive, Long toolId, Long ownerId, Long typeId, Long containerId, String propertyName, Object propertyValue, Boolean propertyReference) {
    return this.getArtifactReps(version, null, alive, toolId, ownerId, typeId, containerId, propertyName, propertyValue, propertyReference);
  }

  @Override
  public final Set<Object[]> getArtifacts(final long version, final long minVersion, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId,
      final String propertyName, final Object propertyValue, final Boolean propertyReference) {
    return this.getArtifactReps(version, minVersion, alive, toolId, ownerId, typeId, containerId, propertyName, propertyValue, propertyReference);
  }

  @Override
  public final Set<Long> getArtifactConflicts(final long privateVersion) {
    return this.getArtifactConflicts(privateVersion, this.getHeadVersionNumber());
  }

  @Override
  public final Set<Long> getArtifactConflicts(final long privateVersion, final long publicVersion) {
    logger.debug("getConflictingArtifacts(privateVersion={}, publicVersion={})", privateVersion);
    long baseVersion = this.getWorkspaceBaseVersionNumber(privateVersion);
    if ((baseVersion == this.getHeadVersionNumber()) || (baseVersion >= publicVersion)) {
      return Collections.emptySet();
    }
    final Set<Long> changedInPublicVersion = ObjectArrayRepresentationUtils.getArtifactIds(this.getAllArtifacts(publicVersion, baseVersion + 1));
    final Set<Long> changedInPrivateVersion = ObjectArrayRepresentationUtils.getArtifactIds(this.getVersionArtifacts(privateVersion));
    changedInPublicVersion.retainAll(changedInPrivateVersion);
    logger.debug("getConflictingArtifacts(privateVersion={}) => conflictingArtifacts={}", privateVersion, changedInPublicVersion);
    return changedInPublicVersion;
  }

  @Override
  public final Map<Long, Set<String>> getPropertyConflicts(final long privateVersion) {
    return this.getPropertyConflicts(privateVersion, this.getHeadVersionNumber());
  }

  @Override
  public final Map<Long, Set<String>> getPropertyConflicts(final long privateVersion, final long publicVersion) {
    logger.debug("getConflictingProperties(privateVersion={}, publicVersion={})", privateVersion);
    final Map<Long, Set<String>> conflictingProperties = new HashMap<Long, Set<String>>();
    long baseVersion = this.getWorkspaceBaseVersionNumber(privateVersion);
    if ((baseVersion == this.getHeadVersionNumber()) || (baseVersion >= publicVersion)) {
      return conflictingProperties;
    }

    final Map<Long, Set<String>> changedInPublicVersion = this.getArtifactPropertyDiffs(publicVersion, baseVersion);
    final Map<Long, Set<String>> changedInPrivateVersion = new HashMap<Long, Set<String>>();
    Set<Object[]> properties = this.getVersionProperties(privateVersion);
    for (Object[] property : properties) {
      Long artifactId = (Long) property[Columns.ARTIFACT_ID.getIndex()];
      if (!changedInPrivateVersion.containsKey(artifactId)) {
        Set<String> names = new HashSet<>();
        changedInPrivateVersion.put(artifactId, names);
      }
      changedInPrivateVersion.get(artifactId).add((String) property[Columns.PROPERTY_NAME.getIndex()]);
    }

    changedInPublicVersion.keySet().retainAll(changedInPrivateVersion.keySet());
    final Iterator<Entry<Long, Set<String>>> iterator = changedInPublicVersion.entrySet().iterator();
    while (iterator.hasNext()) {
      final Entry<Long, Set<String>> next = iterator.next();
      final Set<String> propertiesPrivate = changedInPrivateVersion.get(next.getKey());
      propertiesPrivate.retainAll(next.getValue());
      if (!propertiesPrivate.isEmpty()) {
        conflictingProperties.put(next.getKey(), propertiesPrivate);
      }
    }

    logger.debug("getConflictingProperties(privateVersion={}, publicVersion={}) => conflictingProperties={}", privateVersion, conflictingProperties);
    return conflictingProperties;
  }

  @Override
  public final Map<Long, Set<String>> getArtifactPropertyDiffs(final long version) {
    final Map<Long, Set<String>> diffs = new HashMap<Long, Set<String>>();
    Set<Object[]> artifacts = this.getVersionArtifacts(version);
    Set<Object[]> properties = this.getVersionProperties(version);
    for (Object[] artifact : artifacts) {
      Long aid = (Long) artifact[Columns.ARTIFACT_ID.getIndex()];
      diffs.put(aid, new HashSet<String>());
    }
    for (Object[] property : properties) {
      Long aid = (Long) property[Columns.ARTIFACT_ID.getIndex()];
      if (!diffs.containsKey(aid)) {
        diffs.put(aid, new HashSet<String>());
      }
      String name = (String) property[Columns.PROPERTY_NAME.getIndex()];
      diffs.get(aid).add(name);
    }
    return diffs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getWorkspaceTool(long)
   */
  @Override
  public final long getWorkspaceTool(final long privateVersion) throws WorkspaceExpiredException {
    logger.debug("getWorkspaceTool(privateVersion={})", privateVersion);
    final Long result = (Long) this.getWorkspaceRepresentation(privateVersion)[Columns.TOOL.getIndex()];
    logger.debug("getWorkspaceTool(privateVersion={}) => {}", privateVersion, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getWorkspaceOwner(long)
   */
  @Override
  public final long getWorkspaceOwner(final long privateVersion) throws WorkspaceExpiredException {
    logger.debug("getWorkspaceOwner(privateVersion={})", privateVersion);
    final Long result = (Long) this.getWorkspaceRepresentation(privateVersion)[Columns.OWNER.getIndex()];
    logger.debug("getWorkspaceOwner(privateVersion={}) => {}", privateVersion, result);
    return result;
  }

  @Override
  public final Long getWorkspaceParent(final long privateVersion) {
    return (Long) this.getWorkspaceRepresentation(privateVersion)[DataStorage.Columns.WORKSPACE_PARENT.getIndex()];
  }

  @Override
  public final PropagationType getWorkspacePush(final long privateVersion) {
    return (PropagationType) this.getWorkspaceRepresentation(privateVersion)[DataStorage.Columns.WORKSPACE_PUSH.getIndex()];
  }

  @Override
  public final PropagationType getWorkspacePull(final long privateVersion) {
    return (PropagationType) this.getWorkspaceRepresentation(privateVersion)[DataStorage.Columns.WORKSPACE_PULL.getIndex()];
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getWorkspaceIdentifier(long)
   */
  @Override
  public final String getWorkspaceIdentifier(final long privateVersion) throws WorkspaceExpiredException {
    logger.debug("getWorkspaceIdentifier(privateVersion={})", privateVersion);
    final String result = (String) this.getWorkspaceRepresentation(privateVersion)[Columns.WORKSPACE_IDENTIFIER.getIndex()];
    logger.debug("getWorkspaceIdentifier(privateVersion={}) => {}", privateVersion, result);
    return result;
  }

  @Override
  public final Map<Long, Set<String>> getPropertiesByReference(final long version, final Long id) {
    return this.getPropertiesByValue(version, id, true);
  }

  @Override
  public final Map<Long, Set<String>> getPropertiesByValue(final long version, final Object value) {
    return this.getPropertiesByValue(version, value, false);
  }

  @Override
  public final Set<Object[]> getArtifactsByPropertyValue(final long version, final String property, final Object value) {
    logger.debug("getArtifactsByPropertyValue(version={}, property={}, value={})", version, property, value);
    return this.getArtifactsByProperty(version, null, true, property, value, false);
  }

  @Override
  public final Set<Object[]> getArtifactsByPropertyValue(final long version, final long minVersion, final String property, final Object value) {
    logger.debug("getArtifactsByPropertyValue(version={}, minVersion={}, property={}, value={})", version, minVersion, property, value);
    return this.getArtifactsByProperty(version, minVersion, true, property, value, false);
  }

  @Override
  public final Set<Object[]> getArtifactsByPropertyReference(final long version, final String property, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getArtifactsByPropertyReference(version={}, property={}, id={})", version, property, id);
    return this.getArtifactsByProperty(version, null, true, property, id, true);
  }

  @Override
  public final Set<Object[]> getArtifactsByPropertyReference(final long version, final long minVersion, final String property, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getArtifactsByPropertyReference(version={}, minVersion={} property={}, id={})", version, minVersion, property, id);
    return this.getArtifactsByProperty(version, minVersion, true, property, id, true);
  }

  private Set<Object[]> getArtifactsByProperty(final long version, final Long minVersion, boolean alive, final String property, final Object value, final Boolean reference) {
    return this.getArtifactReps(version, minVersion, alive, null, null, null, null, property, value, reference);
  }

  @Override
  public final Set<Object[]> getArtifactsWithProperty(final long version, final String property) {
    logger.debug("getArtifactsWithProperty(version={}, property={})", version, property);
    return this.getArtifactsByProperty(version, null, true, property, null, null);
  }

  @Override
  public final Set<Object[]> getArtifactsWithProperty(final long version, final boolean alive, final String property) {
    logger.debug("getArtifactsWithProperty(version={}, alive={}, property={})", version, alive, property);
    return this.getArtifactsByProperty(version, null, alive, property, null, null);
  }

  @Override
  public final Set<Object[]> getArtifactsWithProperty(final long version, final long minVersion, final String property) {
    logger.debug("getArtifactsWithProperty(version={}, property={})", version, property);
    return this.getArtifactsByProperty(version, minVersion, true, property, null, null);
  }

  @Override
  public final Set<Object[]> getArtifactsWithProperty(final long version, final long minVersion, final boolean alive, final String property) {
    logger.debug("getArtifactsWithProperty(version={}, alive={}, property={})", version, alive, property);
    return this.getArtifactsByProperty(version, minVersion, alive, property, null, null);
  }

  @Override
  public final Long getArtifactOwner(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getArtifactOwner(version={}, id={})", version, id);
    final Long result = (Long) this.getArtifactRepresentation(version, id)[Columns.OWNER.getIndex()];
    logger.debug("getArtifactOwner(version={}, id={}) => {}", version, id, result);
    return result;
  }

  @Override
  public final Long getPropertyOwner(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDeadException, PropertyDoesNotExistException {
    logger.debug("getPropertyOwner(version={}, artifactId={}, propertyName={})", version, artifactId, propertyName);
    final Long result = (Long) this.getPropertyRepresentation(version, artifactId, propertyName)[Columns.OWNER.getIndex()];
    logger.debug("getPropertyOwner(version={}, artifactId={}, propertyName={}) => {}", version, artifactId, propertyName, result);
    return result;
  }

  @Override
  public final Long getArtifactTool(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getArtifactTool(version={}, id={})", version, id);
    final Long result = (Long) this.getArtifactRepresentation(version, id)[Columns.TOOL.getIndex()];
    logger.debug("getArtifactTool(version={}, id={}) => {}", version, id, result);
    return result;
  }

  @Override
  public final Long getPropertyTool(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDeadException, PropertyDoesNotExistException {
    logger.debug("getPropertyTool(version={}, artifactId={}, propertyName={})", version, artifactId, propertyName);
    final Long result = (Long) this.getPropertyRepresentation(version, artifactId, propertyName)[Columns.TOOL.getIndex()];
    logger.debug("getPropertyTool(version={}, artifactId={}, propertyName={}) => {}", version, artifactId, propertyName, result);
    return result;
  }

  @Override
  public final void setWorkspaceBaseVersionNumber(final long privateVersion, final long newBaseVersion) throws WorkspaceExpiredException {
    Set<Object[]> hierarchy = WorkspaceHierarchyUtils.collectCompleteWorkspaceHierarchy(this, privateVersion);
    Long oldBaseVersion = (Long) hierarchy.iterator().next()[Columns.WORKSPACE_BASE_VERSION.getIndex()];
    rebasePrivateVersion(privateVersion, newBaseVersion, hierarchy, oldBaseVersion, new ArrayList<Long>());
  }

  private void rebasePrivateVersion(final long privateVersion, final long newBaseVersion, Set<Object[]> hierarchy, Long oldBaseVersion, Collection<Long> filter) {
    for (Object[] ws : hierarchy) {
      Long wsId = (Long) ws[Columns.WORKSPACE_VERSION.getIndex()];
      this.updateWorkspaceBaseVersionNumber(wsId, newBaseVersion);
    }
    Collection<PrivateVersionRebasedEvent> events = new ArrayList<>();
    for (Long v : ObjectArrayRepresentationUtils.getWorkspaceIds(hierarchy)) {
      if (!filter.contains(v)) {
        events.add(EventFactory.createPrivateVersionRebasedEvent(privateVersion, v, oldBaseVersion, newBaseVersion));
      }
    }
    this.fireRebasedPrivateVersion(events);
  }

  @Override
  public final long commitArtifact(final long privateVersion, final long id, final String message) throws ArtifactDoesNotExistException, ArtifactConflictException, WorkspaceEmptyException {
    long oldBaseVersion = this.getWorkspaceBaseVersionNumber(privateVersion);
    long newVersion = publishArtifact(privateVersion, id, message);
    Set<Object[]> properties = this.getVersionPropertiesOfArtifact(newVersion, id);
    if (newVersion != VOID_VERSION) {
      Set<Object[]> hierarchy = WorkspaceHierarchyUtils.collectCompleteWorkspaceHierarchy(this, privateVersion);
      this.removeArtifactFromHierarchy(privateVersion, this.getArtifactRepresentation(newVersion, id), hierarchy);
      for (Object[] property : properties) {
        this.removePropertyFromHierarchy(privateVersion, property, hierarchy);
      }
      ArtifactCommitedEvent event = EventFactory.createArtifactCommitedEvent(privateVersion, id, this.getArtifactType(privateVersion, id), message, newVersion, oldBaseVersion);
      this.fireCommitArtifact(event);
      this.rebasePrivateVersion(privateVersion, newVersion, hierarchy, oldBaseVersion, Arrays.asList(new Long[] { privateVersion }));
    }
    return newVersion;
  }

  @Override
  public final long commitProperty(final long privateVersion, final long id, final String property, final String message)
      throws ArtifactDoesNotExistException, PropertyDoesNotExistException, PropertyConflictException, WorkspaceEmptyException, PropertyNotCommitableException {
    long oldBaseVersion = this.getWorkspaceBaseVersionNumber(privateVersion);
    long newVersion = this.publishProperty(privateVersion, id, property, message);
    if (newVersion != VOID_VERSION) {
      Set<Object[]> hierarchy = WorkspaceHierarchyUtils.collectCompleteWorkspaceHierarchy(this, privateVersion);
      removePropertyFromHierarchy(privateVersion, this.getPropertyRepresentation(newVersion, id, property), hierarchy);
      PropertyCommitedEvent event = EventFactory.createPropertyCommitedEvent(privateVersion, id, property, message, newVersion, oldBaseVersion);
      this.fireCommitProperty(event);
      this.rebasePrivateVersion(privateVersion, newVersion, hierarchy, oldBaseVersion, Arrays.asList(new Long[] { privateVersion }));
    }
    return newVersion;
  }

  @Override
  public final long commitVersion(final long privateVersion, final String message) throws VersionConflictException, WorkspaceEmptyException {
    final long oldBaseVersion = this.getWorkspaceBaseVersionNumber(privateVersion);
    long newVersion = this.publishVersion(privateVersion, message);
    Set<Object[]> artifacts = this.getVersionArtifacts(newVersion);
    Set<Object[]> properties = this.getVersionProperties(newVersion);
    if (newVersion != VOID_VERSION) {
      Set<Object[]> hierarchy = WorkspaceHierarchyUtils.collectCompleteWorkspaceHierarchy(this, privateVersion);
      this.removeWorkspaceContentsFromHierarchy(privateVersion, artifacts, properties, hierarchy);
      VersionCommitedEvent event = EventFactory.createVersionCommitedEvent(privateVersion, message, newVersion, oldBaseVersion);
      this.fireCommitVersion(event);
      this.rebasePrivateVersion(privateVersion, newVersion, hierarchy, oldBaseVersion, Arrays.asList(new Long[] { privateVersion }));
    }
    return newVersion;
  }

  private void removeArtifactFromHierarchy(final long privateVersion, Object[] artifact, Set<Object[]> hierarchy) {
    long id = (long) artifact[Columns.ARTIFACT_ID.getIndex()];
    for (Object[] workspace : hierarchy) {
      Long wsId = (Long) workspace[Columns.WORKSPACE_VERSION.getIndex()];
      if (wsId != privateVersion) {
        if (this.existsArtifactInVersion(wsId, id)) {
          Object[] wsArtifact = this.getArtifactRepresentation(wsId, id);
          if (!Objects.equals(wsArtifact[Columns.ARTIFACT_TYPE.getIndex()], artifact[Columns.ARTIFACT_TYPE.getIndex()])) {
            continue;
          }

          if (!Objects.equals(wsArtifact[Columns.ARTIFACT_CONTAINER.getIndex()], artifact[Columns.ARTIFACT_CONTAINER.getIndex()])) {
            continue;
          }
          this.deleteArtifact(wsId, id);
        }
      }
    }
  }

  private void removePropertyFromHierarchy(final long privateVersion, Object[] property, Set<Object[]> hierarchy) {
    Long id = (Long) property[Columns.ARTIFACT_ID.getIndex()];
    String propertyName = (String) property[Columns.PROPERTY_NAME.getIndex()];
    for (Object[] workspace : hierarchy) {
      Long wsId = (Long) workspace[Columns.WORKSPACE_VERSION.getIndex()];
      if (wsId != privateVersion) {
        if (this.existsPropertyInVersion(wsId, id, propertyName)) {
          Object[] wsProperty = this.getPropertyRepresentation(wsId, id, propertyName);
          if (!Objects.equals(wsProperty[Columns.PROPERTY_VALUE.getIndex()], property[Columns.PROPERTY_VALUE.getIndex()])) {
            continue;
          }

          if (!Objects.equals(wsProperty[Columns.PROPERTY_REFERENCE.getIndex()], property[Columns.PROPERTY_REFERENCE.getIndex()])) {
            continue;
          }

          if (!Objects.equals(wsProperty[Columns.ALIVE.getIndex()], property[Columns.ALIVE.getIndex()])) {
            continue;
          }
          this.deleteProperty(wsId, id, propertyName);
        }
      }
    }
  }

  private void removeWorkspaceContentsFromHierarchy(long privateVersion, Set<Object[]> artifacts, Set<Object[]> properties, Set<Object[]> hierarchy) {
    for (Object[] property : properties) {
      this.removePropertyFromHierarchy(privateVersion, property, hierarchy);
    }
    for (Object[] artifact : artifacts) {
      this.removeArtifactFromHierarchy(privateVersion, artifact, hierarchy);
    }
  }

  protected boolean existsOnlyInPrivateVersion(final long privateVersion, final long id) {
    final List<Long> versionNumbers = this.getArtifactCompleteHistoryVersionNumbers(id);
    return (versionNumbers.size() == 1) && versionNumbers.get(0).equals(privateVersion);
  }

  @Override
  public final void rollbackArtifact(final long privateVersion, final long id) throws ArtifactDoesNotExistException, ArtifactNotDeleteableException {
    Set<Object[]> properties = this.getVersionPropertiesOfArtifact(privateVersion, id);
    Object[] artifact = this.getArtifactRepresentation(privateVersion, id);
    final boolean onlyInPrivateVersion = this.existsOnlyInPrivateVersion(privateVersion, id);
    if (onlyInPrivateVersion) {
      final Map<Long, Set<String>> propsRefArtifact = this.getPropertiesByReference(privateVersion, id);
      final Set<Long> container = ObjectArrayRepresentationUtils.getArtifactIds(this.getArtifactsByContainer(privateVersion, id));
      final Set<Long> types = ObjectArrayRepresentationUtils.getArtifactIds(this.getArtifactsByType(privateVersion, id));
      if (!propsRefArtifact.isEmpty() || !container.isEmpty() || !types.isEmpty()) {
        throw new ArtifactNotDeleteableException(privateVersion, id);
      }
    }
    this.deleteArtifact(privateVersion, id);

    Set<Object[]> hierarchy = WorkspaceHierarchyUtils.collectAllWorkspacesInInstantPath(this, privateVersion);
    this.removeArtifactFromHierarchy(privateVersion, artifact, hierarchy);
    for (Object[] property : properties) {
      this.removePropertyFromHierarchy(privateVersion, property, hierarchy);
    }
    Long owner = (Long) artifact[Columns.OWNER.getIndex()];
    Long tool = (Long) artifact[Columns.TOOL.getIndex()];
    Long type = (Long) artifact[Columns.ARTIFACT_TYPE.getIndex()];
    Long container = (Long) artifact[Columns.ARTIFACT_CONTAINER.getIndex()];
    Boolean alive = (Boolean) artifact[Columns.ALIVE.getIndex()];
    Collection<ArtifactEvent> events = new ArrayList<>();
    for (Long v : ObjectArrayRepresentationUtils.getWorkspaceIds(hierarchy)) {
      events.add(EventFactory.createArtifactEvent(DataStorageEventType.DELETE, privateVersion, v, owner, tool, id, type, container, alive, onlyInPrivateVersion));
    }
    this.fireArtifactEvent(events);
  }

  @Override
  public final void rollbackProperty(final long privateVersion, final long id, final String property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    Set<Object[]> hierarchy = WorkspaceHierarchyUtils.collectAllWorkspacesInInstantPath(this, privateVersion);
    Object[] propertyRep = this.getPropertyRepresentation(privateVersion, id, property);
    final boolean onlyInPrivateVersion = existsPropertyOnlyInPrivateVersion(privateVersion, id, property);
    this.deleteProperty(privateVersion, id, property);
    removePropertyFromHierarchy(privateVersion, propertyRep, hierarchy);
    Collection<PropertyDeletedEvent> events = new ArrayList<>();
    for (Long v : ObjectArrayRepresentationUtils.getWorkspaceIds(hierarchy)) {
      events.add(
          EventFactory.createPropertyDeletedEvent(privateVersion, v, (Long) propertyRep[Columns.OWNER.getIndex()], (Long) propertyRep[Columns.TOOL.getIndex()], id, property, onlyInPrivateVersion));
    }
    this.fireDeleteProperty(events);
  }

  protected boolean existsPropertyOnlyInPrivateVersion(final long privateVersion, final long id, final String property) {
    final List<Long> versionNumbers = this.getPropertyCompleteHistoryVersionNumbers(id, property);
    final boolean onlyInPrivateVersion = (versionNumbers.size() == 1) && versionNumbers.get(0).equals(privateVersion);
    return onlyInPrivateVersion;
  }

  @Override
  public final void rollbackVersion(final long privateVersion) {
    Set<Object[]> artifacts = this.getVersionArtifacts(privateVersion);
    Set<Object[]> properties = this.getVersionProperties(privateVersion);
    this.deleteVersion(privateVersion);
    Set<Object[]> hierarchy = WorkspaceHierarchyUtils.collectAllWorkspacesInInstantPath(this, privateVersion);
    this.removeWorkspaceContentsFromHierarchy(privateVersion, artifacts, properties, hierarchy);
    VersionDeletedEvent event = EventFactory.createVersionDeletedEvent(privateVersion);
    this.fireDeleteVersion(event);
  }

  @Override
  public final Set<Long> getArtifactDiffs(final long version, final long previousVersion) {
    logger.debug("getArtifactDiffs(version={}, previousVersion={})", version, previousVersion);
    if ((version > this.getHeadVersionNumber()) || (version < previousVersion)) {
      throw new IllegalArgumentException();
    }
    final Set<Long> artifacts = new HashSet<>();
    for (long v = previousVersion + 1; v <= version; v++) {
      artifacts.addAll(ObjectArrayRepresentationUtils.getArtifactIds(this.getArtifacts(v, v, true)));
      artifacts.addAll(ObjectArrayRepresentationUtils.getArtifactIds(this.getArtifacts(v, v, false)));
    }
    logger.debug("getArtifactDiffs(version={}, previousVersion={}) => artifactDiffs={}", version, previousVersion, artifacts);
    return artifacts;
  }

  @Override
  public final Map<Long, Set<String>> getArtifactPropertyDiffs(final long version, final long previousVersion) {
    logger.debug("getArtifactPropertyDiffs(version={}, previousVersion={})", version, previousVersion);
    if ((version > this.getHeadVersionNumber()) || (version < previousVersion)) {
      throw new IllegalArgumentException();
    }
    final Map<Long, Set<String>> diffs = new HashMap<Long, Set<String>>();
    for (long v = previousVersion + 1; v <= version; v++) {
      final Map<Long, Set<String>> vDiff = this.getArtifactPropertyDiffs(v);
      final Iterator<Entry<Long, Set<String>>> it = vDiff.entrySet().iterator();
      while (it.hasNext()) {
        final Entry<Long, Set<String>> entry = it.next();
        if (!diffs.containsKey(entry.getKey())) {
          diffs.put(entry.getKey(), new HashSet<String>());
        }
        diffs.get(entry.getKey()).addAll(entry.getValue());
      }
    }

    logger.debug("getArtifactPropertyDiffs(version={}, previousVersion={}) => artifactPropertyDiffs={}", version, previousVersion, diffs);
    return diffs;
  }

  @Override
  public Map<Long, Set<String>> getWorkspaceDiffs(final long privateVersionOne, final long privateVersionTwo) {
    Set<Object[]> wsOneArtifacts = this.getVersionArtifacts(privateVersionOne);
    Set<Object[]> wsOneProperties = this.getVersionProperties(privateVersionOne);
    Set<Object[]> wsTwoArtifacts = this.getVersionArtifacts(privateVersionTwo);
    Set<Object[]> wsTwoProperties = this.getVersionProperties(privateVersionTwo);

    Map<Long, Set<String>> diffs = new HashMap<>();

    for (Object[] artifactOne : wsOneArtifacts) {
      Long artifactIdOne = (Long) artifactOne[Columns.ARTIFACT_ID.getIndex()];
      for (Object[] artifactTwo : wsTwoArtifacts) {
        Long artifactIdTwo = (Long) artifactOne[Columns.ARTIFACT_ID.getIndex()];
        if (Objects.equals(artifactIdOne, artifactIdTwo)) {
          if (!Objects.equals(artifactOne[Columns.ARTIFACT_TYPE.getIndex()], artifactTwo[Columns.ARTIFACT_TYPE.getIndex()])
              || !Objects.equals(artifactOne[Columns.ARTIFACT_CONTAINER.getIndex()], artifactTwo[Columns.ARTIFACT_CONTAINER.getIndex()])) {
            diffs.put(artifactIdOne, new HashSet<String>());
          }
        }
      }
    }

    for (Object[] propertyOne : wsOneProperties) {
      Long artifactIdOne = (Long) propertyOne[Columns.ARTIFACT_ID.getIndex()];
      String propertyNameOne = (String) propertyOne[Columns.PROPERTY_NAME.getIndex()];
      for (Object[] propertyTwo : wsTwoProperties) {
        Long artifactIdTwo = (Long) propertyOne[Columns.ARTIFACT_ID.getIndex()];
        String propertyNameTwo = (String) propertyOne[Columns.PROPERTY_NAME.getIndex()];
        if (Objects.equals(artifactIdOne, artifactIdTwo) && Objects.equals(propertyNameOne, propertyNameTwo)) {
          if (!Objects.equals(propertyOne[Columns.PROPERTY_VALUE.getIndex()], propertyTwo[Columns.PROPERTY_VALUE.getIndex()])
              || !Objects.equals(propertyOne[Columns.PROPERTY_REFERENCE.getIndex()], propertyTwo[Columns.PROPERTY_REFERENCE.getIndex()])
              || !Objects.equals(propertyOne[Columns.ALIVE.getIndex()], propertyTwo[Columns.ALIVE.getIndex()])) {
            if (!diffs.containsKey(artifactIdOne)) {
              diffs.put(artifactIdOne, new HashSet<String>());
            }
            diffs.get(artifactIdOne).add(propertyNameOne);
          }
        }
      }
    }

    return diffs;
  }

  @Override
  public final void pushArtifact(final long childId, final long parentId, final long artifactId) throws ArtifactDoesNotExistException {
    if (childId >= 0 || parentId >= 0) {
      throw new IllegalArgumentException("child or parent not a workspace");
    }
    Object[] artifact = this.getArtifactRepresentation(childId, artifactId);
    Set<Long> toUpdate = WorkspaceHierarchyUtils.collectAllParentWorkspaceIdsInInstantPath(this, parentId, childId);
    for (Long to : toUpdate) {
      DuplicationUtils.duplicateArtifactWithProperties(this, to, childId, artifact, true);
    }
  }

  @Override
  public final void pushProperty(final long childId, final long parentId, final long artifactId, final String propName)
      throws ArtifactDoesNotExistException, PropertyDoesNotExistException, PropertyNotPushOrPullableException {
    if (childId >= 0 || parentId >= 0) {
      throw new IllegalArgumentException("child or parent not a workspace");
    }
    Object[] property = this.getPropertyRepresentation(childId, artifactId, propName);
    Set<Long> toUpdate = WorkspaceHierarchyUtils.collectAllParentWorkspaceIdsInInstantPath(this, parentId, childId);
    Collection<PropertyEvent> coll = new ArrayList<>();
    for (Long to : toUpdate) {
      coll.add(DuplicationUtils.duplicateProperty(this, to, childId, property));
    }
    this.firePropertyEvents(coll);
  }

  @Override
  public void setWorkspaceParent(final long privateVersion, final Long newParent) {
    setPrivateVersionParent(privateVersion, newParent);
    PrivateVersionParentSetEvent event = new PrivateVersionParentSetEvent(privateVersion, newParent);
    fireWorkspaceParentSet(event);
  }

  @Override
  public final void pushAll(final long childId, final long parentId) {
    if (childId >= 0 || parentId >= 0) {
      throw new IllegalArgumentException("child or parent not a workspace");
    }
    if (!getWorkspaceDiffs(childId, parentId).isEmpty()) {
      throw new PushConflictException(childId, parentId);
    }
    Set<Long> toUpdate = WorkspaceHierarchyUtils.collectAllParentWorkspaceIdsInInstantPath(this, parentId, childId);
    for (Long to : toUpdate) {
      DuplicationUtils.duplicateAll(this, to, childId);
    }
  }

  @Override
  public final void pullArtifact(final long childId, final long parentId, final long artifactId) throws ArtifactDoesNotExistException, ArtifactNotPushOrPullableException {
    if (childId >= 0 || parentId >= 0) {
      throw new IllegalArgumentException("child or parent not a workspace");
    }
    Object[] artifact = this.getArtifactRepresentation(parentId, artifactId);
    Set<Long> toUpdate = WorkspaceHierarchyUtils.collectAllChildrenWorkspaceIdsInInstantPath(this, childId);
    for (Long to : toUpdate) {
      DuplicationUtils.duplicateArtifactWithProperties(this, to, parentId, artifact, true);
    }
  }

  @Override
  public final void pullProperty(final long childId, final long parentId, final long artifactId, final String propName)
      throws ArtifactDoesNotExistException, PropertyDoesNotExistException, PropertyNotPushOrPullableException {
    if (childId >= 0 || parentId >= 0) {
      throw new IllegalArgumentException("child or parent not a workspace");
    }
    Object[] property = this.getPropertyRepresentation(parentId, artifactId, propName);
    Set<Long> toUpdate = WorkspaceHierarchyUtils.collectAllChildrenWorkspaceIdsInInstantPath(this, childId);
    Collection<PropertyEvent> coll = new ArrayList<>();
    for (Long to : toUpdate) {
      coll.add(DuplicationUtils.duplicateProperty(this, to, parentId, property));
    }
    this.firePropertyEvents(coll);
  }

  @Override
  public final void pullAll(final long childId, final long parentId) {
    if (childId >= 0 || parentId >= 0) {
      throw new IllegalArgumentException("child or parent not a workspace");
    }
    Set<Long> toUpdate = WorkspaceHierarchyUtils.collectAllChildrenWorkspaceIdsInInstantPath(this, childId);
    for (Long to : toUpdate) {
      DuplicationUtils.duplicateAll(this, to, parentId);
    }
  }

  @Override
  public final List<Object[]> getWorkspaceParents(final long privateVersion) {
    List<Object[]> parents = new ArrayList<>();
    Object[] workspace = this.getWorkspaceRepresentation(privateVersion);
    Long parent = (Long) workspace[Columns.WORKSPACE_PARENT.getIndex()];
    while (parent != null) {
      workspace = this.getWorkspaceRepresentation(parent);
      parents.add(workspace);
      parent = (Long) workspace[Columns.WORKSPACE_PARENT.getIndex()];
    }
    return parents;
  }

  @Override
  public void setWorkspacePull(final long privateVersion, final PropagationType type) {
    this.setPrivateVersionPull(privateVersion, type);
    // TODO events
  }

  @Override
  public void setWorkspacePush(final long privateVersion, final PropagationType type) {
    this.setPrivateVersionPush(privateVersion, type);
    // TODO events
  }

  @Override
  public void deleteWorkspace(long privateVersion) throws WorkspaceExpiredException, WorkspaceNotEmptyException {
    this.closeWorkspace(privateVersion);
    PrivateVersionDeletedEvent event = EventFactory.createPrivateVersionDeletedEvent(privateVersion);
    this.fireDeletePrivateVersion(event);
  }

  @Override
  public final void addListener(final DataStorageListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public final void removeListener(final DataStorageListener listener) {
    this.listeners.remove(listener);
  }

  protected void fireArtifactEvent(Collection<ArtifactEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.artifactEvent(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireCollectionAddedElementToCollection(Collection<CollectionAddedElementEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.collectionAddedElement(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  private void fireCollectionAddedElementsToCollection(Collection<CollectionAddedElementsEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.collectionAddedElements(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireCollectionRemovedElementFromCollection(Collection<CollectionRemovedElementEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.collectionRemovedElement(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireMapCleared(Collection<MapClearedEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.mapCleared(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireMapPut(Collection<MapPutEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.mapPut(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireMapRemovedElement(Collection<MapRemovedElementEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.mapRemovedElement(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireSetPropertyAlive(Collection<PropertyAliveSetEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.propertyAliveSet(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireSetPropertyReference(Collection<PropertyReferenceSetEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.propertyReferenceSet(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireSetProperties(Collection<PropertyMapSetEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.propertyMapsSet(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireSetPropertyValue(Collection<PropertyValueSetEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.propertyValueSet(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireCommitArtifact(ArtifactCommitedEvent event) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.artifactCommited(event);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireCommitProperty(PropertyCommitedEvent event) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.propertyCommited(event);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireCommitVersion(VersionCommitedEvent event) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.versionCommited(event);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireDeleteProperty(Collection<PropertyDeletedEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.propertyDeleted(events);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireDeleteVersion(VersionDeletedEvent event) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.versionDeleted(event);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireDeletePrivateVersion(PrivateVersionDeletedEvent event) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.privateVersionDeleted(event);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireAddedPrivateVersion(PrivateVersionAddedEvent event) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        listener.privateVersionAdded(event);
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void fireRebasedPrivateVersion(Collection<PrivateVersionRebasedEvent> events) {
    final Iterator<DataStorageListener> iterator = this.listeners.iterator();
    while (iterator.hasNext()) {
      final DataStorageListener listener = iterator.next();
      try {
        for (PrivateVersionRebasedEvent event : events) {
          listener.privateVersionRebased(event);
        }
      } catch (final RemoteException e) {
        iterator.remove();
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void firePropertyEvents(Collection<PropertyEvent> events) {
    Set<PropertyReferenceSetEvent> referenceEvents = new HashSet<>();
    Set<PropertyValueSetEvent> valueEvents = new HashSet<>();
    Collection<PropertyAliveSetEvent> aliveEvents = new ArrayList<>();
    for (PropertyEvent event : events) {
      if (event instanceof PropertyReferenceSetEvent) {
        referenceEvents.add((PropertyReferenceSetEvent) event);
      } else if (event instanceof PropertyValueSetEvent) {
        valueEvents.add((PropertyValueSetEvent) event);
      } else if (event instanceof PropertyAliveSetEvent) {
        aliveEvents.add((PropertyAliveSetEvent) event);
      } else {
        throw new RuntimeException("something was not accounted for");
      }
    }
    if (referenceEvents.size() + valueEvents.size() > 1) {
      PropertyMapSetEvent mapSetEvent = EventFactory.createPropertyMapSetEvent(valueEvents, referenceEvents);
      this.fireSetProperties(Arrays.asList(new PropertyMapSetEvent[] { mapSetEvent }));
    } else {
      this.fireSetPropertyReference(referenceEvents);
      this.fireSetPropertyValue(valueEvents);
    }
    this.fireSetPropertyAlive(aliveEvents);
  }

  protected void fireCollectionEvents(Collection<CollectionEvent> events) {
    Collection<CollectionAddedElementEvent> addedEvents = new ArrayList<>();
    Collection<CollectionRemovedElementEvent> removedEvents = new ArrayList<>();
    for (CollectionEvent event : events) {
      if (event instanceof CollectionRemovedElementEvent) {
        removedEvents.add((CollectionRemovedElementEvent) event);
      } else if (event instanceof CollectionAddedElementEvent) {
        addedEvents.add((CollectionAddedElementEvent) event);
      } else {
        throw new RuntimeException("something was not accounted for");
      }
    }
    this.fireCollectionRemovedElementFromCollection(removedEvents);
    this.fireCollectionAddedElementToCollection(addedEvents);
  }

  protected void fireWorkspaceParentSet(PrivateVersionParentSetEvent event) {
    for (DataStorageListener listener : listeners) {
      try {
        listener.privateVersionParentSet(event);
      } catch (RemoteException e) {
        listeners.remove(listener);
        logger.warn("listener " + listener + " removed because of: ", e);
      }
    }
  }

  protected void resetGenerators() {
    this.idGenerator = null;
    this.versionGenerator = null;
    this.privateVersionNumberGenerator = null;
    this.userGenerator = null;
  }

}
