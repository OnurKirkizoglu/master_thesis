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
 * DefaultCloud.java created on 14.03.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.implementation;

import java.io.Serializable;
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
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
import at.jku.sea.cloud.PublicVersion;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.CredentialsException;
import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MetaModelDoesNotExistException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.exceptions.PackageDoesNotExistException;
import at.jku.sea.cloud.exceptions.ProjectDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.ResourceDoesNotExistException;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
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
import at.jku.sea.cloud.listeners.CloudListener;
import at.jku.sea.cloud.listeners.DataStorageListener;
import at.jku.sea.cloud.listeners.RemoteDataStorageListenerWrapper;
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
import at.jku.sea.cloud.stream.QueryFactory;
import at.jku.sea.cloud.stream.QueryFactoryImpl;

/**
 * @author alexander noehrer
 * @author mriedl
 */
public class DefaultCloud implements Cloud, DataStorageListener, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger logger = LoggerFactory.getLogger(DefaultCloud.class);

  private final DataStorage dataStorage;
  private final List<CloudListener> listeners;
  private final WeakHashMap<Long, Workspace> workspaces;

  public DefaultCloud(final DataStorage dataStorage) {
    super();
    // System.out.println(new File("./data/log4j.properties").getAbsolutePath());
    this.dataStorage = dataStorage;
    this.listeners = new CopyOnWriteArrayList<>();// new ArrayList<CloudListener>(3);
    final String className = this.dataStorage.getClass().toString();
    if (className.startsWith("class $") || className.startsWith("class com.sun.proxy")) {
      try {
        this.dataStorage.addListener(new RemoteDataStorageListenerWrapper(this));
      } catch (final RemoteException e) {
        logger.error("", e);
      }
    } else {
      this.dataStorage.addListener(this);
    }
    this.workspaces = new WeakHashMap<Long, Workspace>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#createOwner()
   */
  @Override
  public Owner createOwner() {
    final long ownerId = this.dataStorage.createOwner(DataStorage.ADMIN, DataStorage.ROOT_TOOL_ID);
    logger.debug("created Owner(id={})", ownerId);
    return new DefaultOwner(this.dataStorage, ownerId, this.getHeadVersionNumber());
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getOwner(long, java.lang.String)
   */
  @Override
  public Owner getOwner(final long id) throws OwnerDoesNotExistException {
    logger.debug("TODO use password to authenticate");
    try {
      final Artifact artifact = this.getArtifact(this.getHeadVersionNumber(), id);
      if (artifact instanceof Owner) {
        logger.debug("returning Owner(id={})", id);
        return (Owner) artifact;
      }
      throw new OwnerDoesNotExistException(id);
    } catch (final ArtifactDoesNotExistException e) {
      throw new OwnerDoesNotExistException(id);
    }
  }

  @Override
  public Tool getTool(final long id) throws ToolDoesNotExistException {
    try {
      final Artifact artifact = this.getArtifact(this.getHeadVersionNumber(), id);
      if (artifact instanceof Tool) {
        logger.debug("returning Tool({})", id);
        return (Tool) artifact;
      }
      throw new ToolDoesNotExistException(id);
    } catch (final ArtifactDoesNotExistException e) {
      throw new ToolDoesNotExistException(id);
    }
  }

  /**
   * creates a new tool without name and tool version
   */
  @Override
  public Tool createTool() {
    final long toolId = this.dataStorage.createTool(DataStorage.ADMIN, DataStorage.ROOT_TOOL_ID);
    logger.debug("created Tool(id={})", toolId);
    
    return new DefaultTool(this.dataStorage, toolId, this.getHeadVersionNumber());
  }
  
  @Override
  public Tool createTool(String name, String toolVersion) {
    logger.debug("createTool(name={},toolVersion={})", name, toolVersion);
    
    return dataStorage.createTool(name, toolVersion);
    
    //return new DefaultTool(this.dataStorage, toolId, this.getHeadVersionNumber());
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getWorkspace(at.jku.sea.cloud.Owner, java.lang.String)
   */
  @Override
  public Workspace getWorkspace(final Owner owner, final Tool tool, final String identifier) {
    return this.getWS(owner, tool, identifier, 0, false);
  }

  @Override
  public Workspace getWorkspace(final Owner owner, final Tool tool, final String identifier, final long baseVersion) throws WorkspaceExpiredException {
    return this.getWS(owner, tool, identifier, baseVersion, true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getWorkspace(long)
   */
  @Override
  public Workspace getWorkspace(final long privateVersionNumber) throws WorkspaceExpiredException {
    if (privateVersionNumber > 0) {
      throw new IllegalArgumentException("version is not private");
    }
    Workspace workspace = this.workspaces.get(privateVersionNumber);
    if (workspace == null) {
      final Object[] representation = this.dataStorage.getWorkspaceRepresentation(privateVersionNumber);
      final Owner owner = this.getOwner((Long) representation[2]);
      final Tool tool = this.getTool((Long) representation[3]);
      workspace = new DefaultWorkspace(this, this.dataStorage, owner, tool, (String) representation[4], (Long) representation[1], privateVersionNumber);
      this.workspaces.put(privateVersionNumber, workspace);
    }
    return workspace;
  }

  private Workspace getWS(final Owner owner, final Tool tool, final String identifier, final long baseVersion, boolean sanityCheck) {
    if ((baseVersion > this.getHeadVersionNumber()) && (this.getWorkspace(baseVersion) == null)) {
      throw new IllegalArgumentException("no correct baseVersion specified");
    }
    Long privateVersionNumber = this.dataStorage.getWorkspaceVersionNumber(owner.getId(), tool.getId(), identifier);
    if (privateVersionNumber != null) {
      final long baseVersionNumber = this.dataStorage.getWorkspaceBaseVersionNumber(privateVersionNumber);
      if (sanityCheck && baseVersionNumber != baseVersion) {
        return null;
      }
      final Workspace workspace = new DefaultWorkspace(this, this.dataStorage, owner, tool, identifier, baseVersionNumber, privateVersionNumber);
      return workspace;
    }
    logger.debug("returning Workspace(id={})", privateVersionNumber);
    return null;
  }

  public Workspace createWorkspace(final Owner owner, final Tool tool, final String identifier) throws WorkspaceExpiredException {
    return this.createWS(owner, tool, identifier, this.getHeadVersionNumber(), null, PropagationType.triggered, PropagationType.triggered);
  }

  public Workspace createWorkspace(final Owner owner, final Tool tool, final String identifier, final PropagationType push, final PropagationType pull) throws WorkspaceExpiredException {
    return this.createWS(owner, tool, identifier, this.getHeadVersionNumber(), null, push, pull);
  }

  @Override
  public Workspace createWorkspace(final Owner owner, final Tool tool, final String identifier, final long baseVersion, final PropagationType push, final PropagationType pull)
      throws WorkspaceExpiredException {
    return this.createWS(owner, tool, identifier, baseVersion, null, push, pull);
  }

  @Override
  public Workspace createWorkspace(final Owner owner, final Tool tool, final String identifier, final Workspace parent, final PropagationType push, final PropagationType pull)
      throws WorkspaceExpiredException {
    return this.createWS(owner, tool, identifier, parent == null ? this.getHeadVersionNumber() : parent.getBaseVersion().getVersionNumber(), parent, push, pull);
  }

  private Workspace createWS(final Owner owner, final Tool tool, final String identifier, final long baseVersion, final Workspace parent, final PropagationType push, final PropagationType pull) {
    Long privateVersionNumber = this.dataStorage.getWorkspaceVersionNumber(owner.getId(), tool.getId(), identifier);
    if (privateVersionNumber == null) {
      Long parentId = parent != null ? parent.getId() : null;
      privateVersionNumber = this.dataStorage.createWorkspace(owner.getId(), tool.getId(), identifier, baseVersion, parentId, push, pull);
      final Workspace workspace = new DefaultWorkspace(this, this.dataStorage, owner, tool, identifier, baseVersion, privateVersionNumber);
      this.workspaces.put(privateVersionNumber, workspace);
      return workspace;
    }
    return this.getWorkspace(privateVersionNumber);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getWorkspaces()
   */
  @Override
  public List<Workspace> getWorkspaces() {
    final List<Long> privateVersionNumbers = this.dataStorage.getWorkspaces();
    final List<Workspace> result = new ArrayList<Workspace>(privateVersionNumbers.size());
    for (final Long privateVersionNumber : privateVersionNumbers) {
      result.add(this.getWorkspace(privateVersionNumber));
    }
    return result;
  }

  public Collection<Workspace> getWorkspaceChildren(Long privateVersion) throws WorkspaceExpiredException {
    Collection<Object[]> objs = dataStorage.getWorkspaceChildren(privateVersion);
    Collection<Workspace> wss = new ArrayList<>(objs.size());

    for (Object[] obj : objs) {
      Workspace ws = getWorkspace((long) obj[Columns.WORKSPACE_VERSION.getIndex()]);
      wss.add(ws);
    }

    return wss;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getPackages(long)
   */
  @Override
  public Collection<Package> getPackages(final long version) throws VersionDoesNotExistException {
    return this.getArtifacts(version, DataStorage.PACKAGE_TYPE_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getCurrentPackages()
   */
  @Override
  public Collection<Package> getHeadPackages() {
    return this.getPackages(this.getHeadVersionNumber());
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getArtifacts(long)
   */
  @Override
  public Collection<Artifact> getArtifacts(final long version) throws VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    final Collection<Object[]> ids = this.dataStorage.getAliveArtifacts(version);
    final Collection<Artifact> result = new ArrayList<Artifact>(ids.size());
    for (final Object[] id : ids) {
      result.add(ArtifactFactory.getArtifact(this.dataStorage, version, id));
    }
    return result;
  }

  @Override
  public Collection<Artifact> getArtifacts(long version, Set<Long> ids) throws VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    Collection<Artifact> artifacts = new ArrayList<>();
    for (Long id : ids) {
      artifacts.add(ArtifactFactory.getArtifact(this.dataStorage, version, id));
    }
    return artifacts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getCurrentArtifacts()
   */
  @Override
  public Collection<Artifact> getHeadArtifacts() {
    return this.getArtifacts(this.getHeadVersionNumber());
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getPackage(long, long)
   */
  @Override
  public Package getPackage(final long version, final long id) throws PackageDoesNotExistException, VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    try {
      final Artifact artifact = ArtifactFactory.getArtifact(this.dataStorage, version, id);
      if (artifact instanceof Package) {
        return (Package) artifact;
      }
    } catch (final ArtifactDoesNotExistException e) {
      // ignore
    }
    throw new PackageDoesNotExistException(version, id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getProject(long, long)
   */
  @Override
  public Project getProject(final long version, final long id) throws ProjectDoesNotExistException, VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    try {
      final Artifact artifact = ArtifactFactory.getArtifact(this.dataStorage, version, id);
      if (artifact instanceof Project) {
        return (Project) artifact;
      }
    } catch (final ArtifactDoesNotExistException e) {
      // ignore
    }
    throw new ProjectDoesNotExistException(version, id);
  }

  @Override
  public MetaModel getMetaModel(final long version, final long id) throws MetaModelDoesNotExistException, VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    try {
      final Artifact artifact = ArtifactFactory.getArtifact(this.dataStorage, version, id);
      if (artifact instanceof MetaModel) {
        return (MetaModel) artifact;
      }
    } catch (final ArtifactDoesNotExistException e) {
      // ignore
    }
    throw new MetaModelDoesNotExistException(version, id);
  }

  @Override
  public CollectionArtifact getCollectionArtifact(final long version, final long id) throws CollectionArtifactDoesNotExistException, VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    try {
      final Artifact artifact = ArtifactFactory.getArtifact(this.dataStorage, version, id);
      if (artifact instanceof CollectionArtifact) {
        return (CollectionArtifact) artifact;
      }
    } catch (final ArtifactDoesNotExistException e) {
      // ignore
    }
    throw new CollectionArtifactDoesNotExistException(version, id);
  }

  @Override
  public MapArtifact getMapArtifact(final long version, final long id) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    try {
      final Artifact artifact = ArtifactFactory.getArtifact(this.dataStorage, version, id);
      if (artifact instanceof MapArtifact) {
        return (MapArtifact) artifact;
      }
    } catch (final ArtifactDoesNotExistException e) {
    }
    throw new MapArtifactDoesNotExistException(version, id);
  }

  @Override
  public Collection<MapArtifact> getMapArtifacts(final long version) throws VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }

    final Collection<Object[]> ids = this.dataStorage.getArtifactsByType(version, DataStorage.MAP_TYPE_ID);
    final Collection<MapArtifact> result = new ArrayList<MapArtifact>(ids.size());
    for (final Object[] id : ids) {
      result.add((MapArtifact) ArtifactFactory.getArtifact(this.dataStorage, version, id));
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getHeadVersionNumber()
   */
  @Override
  public long getHeadVersionNumber() {
    return this.dataStorage.getHeadVersionNumber();
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getArtifactHistoryVersionNumbers(at.jku.sea.cloud.Artifact)
   */
  @Override
  public List<Version> getArtifactHistoryVersionNumbers(final Artifact artifact) throws ArtifactDoesNotExistException {
    final List<Long> versionNumbers = this.dataStorage.getArtifactHistoryVersionNumbers(artifact.getId());
    final List<Version> result = new ArrayList<Version>(versionNumbers.size());
    for (final Long versionNumber : versionNumbers) {
      result.add(new DefaultPublicVersion(this.dataStorage, versionNumber));
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getPropertyHistoryVersionNumbers(at.jku.sea.cloud.Artifact, java.lang.String)
   */
  @Override
  public List<Version> getPropertyHistoryVersionNumbers(final Artifact artifact, final String property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    final List<Long> versionNumbers = this.dataStorage.getPropertyHistoryVersionNumbers(artifact.getId(), property);
    final List<Version> result = new ArrayList<Version>(versionNumbers.size());
    for (final Long versionNumber : versionNumbers) {
      result.add(new DefaultPublicVersion(this.dataStorage, versionNumber));
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getAllVersions()
   */
  @Override
  public List<Version> getAllVersions() {
    final long headVersion = this.dataStorage.getHeadVersionNumber();
    final List<Version> privateVersions = this.getPrivateVersions();
    final List<Version> result = new ArrayList<Version>(Long.valueOf(headVersion).intValue() + privateVersions.size());
    for (long i = DataStorage.FIRST_VERSION; i <= headVersion; i++) {
      result.add(new DefaultPublicVersion(this.dataStorage, i));
    }
    result.addAll(privateVersions);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Cloud#getVersion(long)
   */
  @Override
  public Version getVersion(final long version) throws VersionDoesNotExistException {
    if ((version > this.getHeadVersionNumber()) && (this.workspaces.get(version) == null)) {
      throw new VersionDoesNotExistException(version);
    }
    if (version < 0) {
      return this.workspaces.get(version);
    }
    return new DefaultPublicVersion(this.dataStorage, version);
  }

  @Override
  public void addListener(final CloudListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeListener(final CloudListener listener) {
    this.listeners.remove(listener);
  }

  @Override
  public Artifact getArtifact(final long version, final long id) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    return ArtifactFactory.getArtifact(this.dataStorage, version, id);
  }

  @Override
  public List<Version> getPrivateVersions() {
    final List<Long> privateVersionNumbers = this.dataStorage.getWorkspaces();
    final List<Version> result = new ArrayList<Version>(privateVersionNumbers.size());
    for (final Long privateVersionNumber : privateVersionNumbers) {
      result.add(this.getWorkspace(privateVersionNumber));
    }
    return result;
  }

  @Override
  public Collection<Project> getProjects(final long version) throws VersionDoesNotExistException {
    return this.getArtifacts(version, DataStorage.PROJECT_TYPE_ID);
  }

  @Override
  public Collection<Project> getHeadProjects() {
    return this.getProjects(this.getHeadVersionNumber());
  }

  @Override
  public Collection<MetaModel> getMetaModels(final long version) throws VersionDoesNotExistException {
    return this.getArtifacts(version, DataStorage.META_MODEL_TYPE_ID);
  }

  @Override
  public Collection<CollectionArtifact> getCollectionArtifacts(final long version) throws VersionDoesNotExistException {
    return this.getArtifacts(version, DataStorage.COLLECTION_TYPE_ID);
  }
  
  private <A extends Artifact> List<A> getArtifacts(long version, long type) {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    return this.dataStorage.getArtifactsByType(version, type).stream()
        .map(id -> ArtifactFactory.<A>getArtifact(this.dataStorage, version, id))
        .collect(Collectors.toList());
  }

  @Override
  public Collection<MetaModel> getHeadMetaModels() {
    return this.getMetaModels(this.getHeadVersionNumber());
  }

  @Override
  public Collection<CollectionArtifact> getHeadCollectionArtifacts() {
    return this.getCollectionArtifacts(this.getHeadVersionNumber());
  }

  @Override
  public Collection<Artifact> getArtifacts(final long version, final Artifact... filters) throws VersionDoesNotExistException {
    return this.getArtifactsWithProperty(version, null, null, filters);
  }

  @Override
  public Collection<Artifact> getHeadArtifacts(final Artifact... filters) {
    return this.getArtifacts(this.getHeadVersionNumber(), filters);
  }

  @Override
  public Collection<Artifact> getArtifactsWithProperty(final long version, final String propertyName, final Object propertyValue, final Artifact... filters) throws VersionDoesNotExistException {
    return this.getArtifactsWithProperty(version, propertyName, propertyValue, true, filters);
  }

  @Override
  public Collection<Artifact> getArtifactsWithProperty(final long version, final String propertyName, final Object propertyValue, final boolean alive, final Artifact... filters)
      throws VersionDoesNotExistException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    return GetArtifactsWithFilterUtils.getArtifactsWithProperty(dataStorage, version, propertyName, propertyValue, alive, filters);
  }

  @Override
  public Collection<Artifact> getArtifactsWithReference(long version, Artifact artifact, Artifact... filters) throws WorkspaceExpiredException {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    return GetArtifactsWithFilterUtils.getArtifactsWithReference(dataStorage, version, artifact, filters);
  }

  @Override
  public Collection<Artifact> getArtifactsWithProperty(final long version, final Map<String, Object> propertyToValue, final Artifact... filters) throws VersionDoesNotExistException {
    return this.getArtifactsWithProperty(version, propertyToValue, true, filters);
  }

  @Override
  public Collection<Artifact> getArtifactsWithProperty(final long version, final Map<String, Object> propertyToValue, final boolean alive, final Artifact... filters)
      throws VersionDoesNotExistException {
    return GetArtifactsWithFilterUtils.getArtifactsWithProperty(dataStorage, version, propertyToValue, alive, filters);
  }

  @Override
  public Collection<Artifact> getHeadArtifactsWithProperty(final String propertyName, final Object propertyValue, final Artifact... filters) {
    return this.getArtifactsWithProperty(this.getHeadVersionNumber(), propertyName, propertyValue, true, filters);
  }

  @Override
  public Collection<Artifact> getHeadArtifactsWithProperty(final String propertyName, final Object propertyValue, final boolean alive, final Artifact... filters) {
    return this.getArtifactsWithProperty(this.getHeadVersionNumber(), propertyName, propertyValue, alive, filters);
  }

  @Override
  public Collection<Artifact> getHeadArtifactsWithProperty(final Map<String, Object> propertyToValue, final Artifact... filters) throws VersionDoesNotExistException {
    return this.getArtifactsWithProperty(this.getHeadVersionNumber(), propertyToValue, true, filters);
  }

  @Override
  public Collection<Artifact> getHeadArtifactsWithProperty(final Map<String, Object> propertyToObject, final boolean alive, final Artifact... filters) throws VersionDoesNotExistException {
    return this.getArtifactsWithProperty(this.getHeadVersionNumber(), propertyToObject, alive, filters);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long version, Artifact... filters) throws WorkspaceExpiredException {
    return GetArtifactsWithFilterUtils.getArtifactsAndPropertyMap(dataStorage, version, null, null, true, filters);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long version, String propertyName, Object propertyValue, boolean alive, Artifact... filters) throws WorkspaceExpiredException {
    return GetArtifactsWithFilterUtils.getArtifactsAndPropertyMap(dataStorage, version, propertyName, propertyValue, alive, filters);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long version, Map<String, Object> propertyToValue, boolean alive, Artifact... filters) throws WorkspaceExpiredException {
    return GetArtifactsWithFilterUtils.getArtifactsAndPropertyMap(dataStorage, version, propertyToValue, alive, filters);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(long version, Set<Artifact> artifacts, Set<String> properties) throws WorkspaceExpiredException {
    Map<Artifact, Map<String, Object>> map = new HashMap<>();
    for (Artifact artifact : artifacts) {
      map.put(artifact, new HashMap<String, Object>());
      for (String property : properties) {
        Object[] rep = this.dataStorage.getPropertyRepresentationOrNull(version, artifact.getId(), property);
        Object value = null;
        if (rep != null) {
          Long reference = (Long) rep[DataStorage.Columns.PROPERTY_REFERENCE.getIndex()];
          if (reference != null) {
            value = ArtifactFactory.getArtifact(dataStorage, version, reference);
          } else {
            value = rep[DataStorage.Columns.PROPERTY_VALUE.getIndex()];
          }
        }
        map.get(artifact).put(property, value);
      }
    }
    return map;
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(long version, Set<Artifact> artifacts) throws WorkspaceExpiredException {
    return GetArtifactsWithFilterUtils.createArtifactAndStringObjectMap(dataStorage, version, artifacts);
  }

  @Override
  public Collection<Property> getPropertiesByReference(final long version, final Artifact artifact) {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    final Map<Long, Set<String>> idToPropName = this.dataStorage.getPropertiesByReference(version, artifact.getId());
    final Collection<Property> properties = new HashSet<Property>();
    final Iterator<Entry<Long, Set<String>>> iterator = idToPropName.entrySet().iterator();
    while (iterator.hasNext()) {
      final Entry<Long, Set<String>> next = iterator.next();
      final Long id = next.getKey();
      final Set<String> props = next.getValue();
      for (final String propName : props) {
        properties.add(new DefaultProperty(this.dataStorage, id, version, propName));
      }
    }
    return properties;
  }

  @Override
  public void artifactEvent(Collection<ArtifactEvent> aEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<ArtifactCreated> createdEvents = new ArrayList<>();
      Collection<ArtifactContainerSet> packageEvents = new ArrayList<>();
      Collection<ArtifactTypeSet> typeEvents = new ArrayList<>();
      Collection<ArtifactAliveSet> aliveSetEvents = new ArrayList<>();
      Collection<ArtifactRollBack> rollbackEvents = new ArrayList<>();
      for (ArtifactEvent aEvent : aEvents) {
        Artifact artifact = ArtifactFactory.getArtifact(this.dataStorage, aEvent.privateVersion, aEvent.id, aEvent.type);
        switch (aEvent.eventType) {
          case CREATE:
            createdEvents.add(new ArtifactCreated(aEvent.origin, artifact));
            break;
          case CONTAINER_SET:
            final Container container;
            if (aEvent.packageId == null) {
              container = null;
            } else {
              container = (Container) ArtifactFactory.getArtifact(this.dataStorage, aEvent.privateVersion, aEvent.packageId, DataStorage.PACKAGE_TYPE_ID);
            }
            packageEvents.add(new ArtifactContainerSet(aEvent.origin, artifact, container));
            break;
          case TYPE_SET:
            final Artifact typeArtifact;
            if (aEvent.type == null) {
              typeArtifact = null;
            } else {
              typeArtifact = this.getArtifact(aEvent.privateVersion, aEvent.type);
            }
            typeEvents.add(new ArtifactTypeSet(aEvent.origin, artifact, typeArtifact));
            break;
          case ALIVE_SET:
            aliveSetEvents.add(new ArtifactAliveSet(aEvent.origin, artifact, aEvent.alive));
            break;
          case DELETE:
            rollbackEvents.add(new ArtifactRollBack(aEvent.origin, artifact, aEvent.isDeceased));
            break;
          default:
            throw new IllegalArgumentException(aEvent.eventType.name() + " not handled");
        }
      }

      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (ArtifactCreated event : createdEvents) {
            listener.artifactCreated(event);
          }
          for (ArtifactContainerSet event : packageEvents) {
            listener.artifactContainerSet(event);
          }
          for (ArtifactTypeSet event : typeEvents) {
            listener.artifactTypeSet(event);
          }
          for (ArtifactRollBack event : rollbackEvents) {
            listener.artifactRollback(event);
          }
          for (ArtifactAliveSet event : aliveSetEvents) {
            listener.artifactAliveSet(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void collectionAddedElement(Collection<CollectionAddedElementEvent> caeEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<CollectionAddedElement> events = new ArrayList<>();
      for (CollectionAddedElementEvent caeEvent : caeEvents) {
        final CollectionArtifact collArtifact = (CollectionArtifact) ArtifactFactory.getArtifact(this.dataStorage, caeEvent.privateVersion, caeEvent.collectionId);
        Object element = caeEvent.elem;
        if (collArtifact.isContainingOnlyArtifacts()) {
          final Long aid = (Long) caeEvent.elem;
          element = ArtifactFactory.getArtifact(this.dataStorage, caeEvent.privateVersion, aid);
        }
        events.add(new CollectionAddedElement(caeEvent.origin, collArtifact, element));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (CollectionAddedElement event : events) {
            listener.collectionAddedElement(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void collectionAddedElements(Collection<CollectionAddedElementsEvent> caeEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<CollectionAddedElements> events = new ArrayList<>();
      for (CollectionAddedElementsEvent caeEvent : caeEvents) {
        final CollectionArtifact collArtifact = (CollectionArtifact) ArtifactFactory.getArtifact(this.dataStorage, caeEvent.privateVersion, caeEvent.collectionId);
        @SuppressWarnings("unchecked")
        Collection<Object> elements = (Collection<Object>) caeEvent.elem;
        Collection<Object> result = new ArrayList<>();
        if (collArtifact.isContainingOnlyArtifacts()) {
          for (Object element : elements) {
            final Long aid = (Long) element;
            result.add(ArtifactFactory.getArtifact(this.dataStorage, caeEvent.privateVersion, aid));
          }
        } else {
          result.addAll(elements);
        }
        events.add(new CollectionAddedElements(caeEvent.origin, collArtifact, result));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (CollectionAddedElements event : events) {
            listener.collectionAddedElements(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void collectionRemovedElement(Collection<CollectionRemovedElementEvent> creEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<CollectionRemovedElement> events = new ArrayList<>();
      for (CollectionRemovedElementEvent creEvent : creEvents) {
        final CollectionArtifact collArtifact = (CollectionArtifact) ArtifactFactory.getArtifact(this.dataStorage, creEvent.privateVersion, creEvent.collectionId);
        Object element = creEvent.elem;
        if (collArtifact.isContainingOnlyArtifacts()) {
          final Long aid = (Long) creEvent.elem;
          element = ArtifactFactory.getArtifact(this.dataStorage, creEvent.privateVersion, aid);
        }
        events.add(new CollectionRemovedElement(creEvent.origin, collArtifact, element));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (CollectionRemovedElement event : events) {
            listener.collectionRemovedElement(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void mapCleared(Collection<MapClearedEvent> mcEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<MapCleared> events = new ArrayList<>();
      for (MapClearedEvent mcEvent : mcEvents) {
        final MapArtifact mapArtifact = (MapArtifact) ArtifactFactory.getArtifact(this.dataStorage, mcEvent.privateVersion, mcEvent.mapId);
        events.add(new MapCleared(mcEvent.origin, mapArtifact));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (MapCleared event : events) {
            listener.mapCleared(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void mapPut(Collection<MapPutEvent> mpEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<MapPut> events = new ArrayList<>();
      for (MapPutEvent mpEvent : mpEvents) {
        final MapArtifact mapArtifact = (MapArtifact) ArtifactFactory.getArtifact(this.dataStorage, mpEvent.privateVersion, mpEvent.mapId);
        Object key = mpEvent.isKeyReference ? ArtifactFactory.getArtifact(this.dataStorage, mpEvent.privateVersion, (long) mpEvent.key) : mpEvent.key;
        Object oldValue = null;
        if (!mpEvent.isAdded) {
          oldValue = mpEvent.isOldValueReference ? ArtifactFactory.getArtifact(this.dataStorage, mpEvent.privateVersion, (long) mpEvent.oldValue) : mpEvent.oldValue;
        }
        Object newValue = mpEvent.isNewValueReference ? ArtifactFactory.getArtifact(this.dataStorage, mpEvent.privateVersion, (long) mpEvent.newValue) : mpEvent.newValue;
        events.add(new MapPut(mpEvent.origin, mapArtifact, key, oldValue, newValue, mpEvent.isAdded));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (MapPut event : events) {
            listener.putMap(event);
          }
        } catch (RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void mapRemovedElement(Collection<MapRemovedElementEvent> mrEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<MapElementRemoved> events = new ArrayList<>();
      for (MapRemovedElementEvent mrEvent : mrEvents) {
        final MapArtifact mapArtifact = (MapArtifact) ArtifactFactory.getArtifact(this.dataStorage, mrEvent.privateVersion, mrEvent.mapId);
        Object key = mrEvent.isKeyReference ? ArtifactFactory.getArtifact(this.dataStorage, mrEvent.privateVersion, (long) mrEvent.key) : mrEvent.key;
        Object value = mrEvent.isValueReference ? ArtifactFactory.getArtifact(this.dataStorage, mrEvent.privateVersion, (long) mrEvent.value) : mrEvent.value;
        events.add(new MapElementRemoved(mrEvent.origin, mapArtifact, key, value));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (MapElementRemoved event : events) {
            listener.mapElementRemoved(event);
          }
        } catch (RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#propertyAliveSet(long, long, long, long, java.lang.String, boolean)
   */
  @Override
  public void propertyAliveSet(Collection<PropertyAliveSetEvent> pasEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<PropertyAliveSet> events = new ArrayList<>();
      for (PropertyAliveSetEvent pasEvent : pasEvents) {
        final Property p = new DefaultProperty(this.dataStorage, pasEvent.artifactId, pasEvent.version, pasEvent.propertyName);
        events.add(new PropertyAliveSet(pasEvent.origin, p, pasEvent.alive));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (PropertyAliveSet event : events) {
            listener.propertyAliveSet(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#propertyReferenceSet(long, long, long, long, java.lang.String, long)
   */
  @Override
  public void propertyReferenceSet(Collection<PropertyReferenceSetEvent> prsEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<PropertyValueSet> events = new ArrayList<>();
      for (PropertyReferenceSetEvent prsEvent : prsEvents) {
        final Property p = new DefaultProperty(this.dataStorage, prsEvent.artifactId, prsEvent.version, prsEvent.propertyName);
        Artifact oldArtifact = null;
        if ((prsEvent.oldReferenceId != null) && (prsEvent.oldReferenceId != -1)) {
          oldArtifact = this.getArtifact(prsEvent.version, prsEvent.oldReferenceId);
        }
        final Artifact artifact = this.getArtifact(prsEvent.version, prsEvent.newReferenceId);
        events.add(new PropertyValueSet(prsEvent.origin, p, artifact, oldArtifact, true));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (PropertyValueSet event : events) {
            listener.propertyValueSet(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#propertyValueSet(long, long, long, long, java.lang.String, java.lang.Object)
   */
  @Override
  public void propertyValueSet(Collection<PropertyValueSetEvent> pvsEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<PropertyValueSet> events = new ArrayList<>();
      for (PropertyValueSetEvent pvsEvent : pvsEvents) {
        final Property p = new DefaultProperty(this.dataStorage, pvsEvent.artifactId, pvsEvent.version, pvsEvent.propertyName);
        events.add(new PropertyValueSet(pvsEvent.origin, p, pvsEvent.value, pvsEvent.oldValue, pvsEvent.wasReference));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (PropertyValueSet event : events) {
            listener.propertyValueSet(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void propertyMapsSet(Collection<PropertyMapSetEvent> pmsEvents) {
    if (!this.listeners.isEmpty()) {
      Collection<PropertyMapSet> events = new ArrayList<>();
      for (PropertyMapSetEvent pmsEvent : pmsEvents) {
        final Collection<PropertyValueSet> properties = new ArrayList<>();
        long origin = -1;
        for (PropertyValueSetEvent pvsEvent : pmsEvent.valueSet) {
          final Property p = new DefaultProperty(this.dataStorage, pvsEvent.artifactId, pvsEvent.version, pvsEvent.propertyName);
          properties.add(new PropertyValueSet(pvsEvent.origin, p, pvsEvent.value, pvsEvent.oldValue, pvsEvent.wasReference));
          origin = pvsEvent.origin;
        }

        for (PropertyReferenceSetEvent prsEvent : pmsEvent.referenceSet) {
          final Property p = new DefaultProperty(this.dataStorage, prsEvent.artifactId, prsEvent.version, prsEvent.propertyName);
          Artifact old = prsEvent.oldReferenceId != null ? this.getArtifact(prsEvent.version, prsEvent.oldReferenceId) : null;
          properties.add(new PropertyValueSet(prsEvent.origin, p, this.getArtifact(prsEvent.version, prsEvent.newReferenceId), old, true));
          origin = prsEvent.origin;
        }
        events.add(new PropertyMapSet(origin, properties));
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (PropertyMapSet event : events) {
            listener.propertyMapSet(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public void artifactCommited(ArtifactCommitedEvent acEvent) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      final Artifact artifact = ArtifactFactory.getArtifact(this.dataStorage, acEvent.privateVersion, acEvent.id, acEvent.type);
      final PublicVersion v = new DefaultPublicVersion(this.dataStorage, acEvent.version);
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      final ArtifactCommited event = new ArtifactCommited(acEvent.version, artifact, v, Collections.emptyList(), acEvent.oldBaseVersion);
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          listener.artifactCommited(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#propertyCommited(long, long, java.lang.String, java.lang.String, long)
   */
  @Override
  public void propertyCommited(PropertyCommitedEvent pcEvent) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      final Property p = new DefaultProperty(this.dataStorage, pcEvent.id, pcEvent.privateVersion, pcEvent.property);
      final PublicVersion v = new DefaultPublicVersion(this.dataStorage, pcEvent.version);
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      final PropertyCommited event = new PropertyCommited(pcEvent.version, p, v, Collections.emptyList(), pcEvent.oldBaseVersion);
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          listener.propertyCommited(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#versionCommited(long, java.lang.String, long)
   */
  @Override
  public void versionCommited(VersionCommitedEvent vcEvent) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      final Workspace pv = this.getWorkspace(vcEvent.privateVersion);
      final PublicVersion v = new DefaultPublicVersion(this.dataStorage, vcEvent.version);
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      final VersionCommited event = new VersionCommited(pv, v, Collections.emptyList(), vcEvent.oldBaseVersion);
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          listener.versionCommited(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#propertyDeleted(long, long, java.lang.String)
   */
  @Override
  public void propertyDeleted(Collection<PropertyDeletedEvent> pdEvents) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      Collection<PropertyRollBack> events = new ArrayList<>();
      for (PropertyDeletedEvent pdEvent : pdEvents) {
        final Property p = new DefaultProperty(this.dataStorage, pdEvent.artifactId, pdEvent.version, pdEvent.propertyName);
        final PropertyRollBack event = new PropertyRollBack(pdEvent.origin, p, pdEvent.isDeceased);
        events.add(event);
      }
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          for (PropertyRollBack event : events) {
            listener.propertyRollback(event);
          }
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#versionDeleted(long)
   */
  @Override
  public void versionDeleted(VersionDeletedEvent vdEvent) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      final Workspace pv = this.getWorkspace(vdEvent.privateVersion);
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      final WorkspaceRollBack event = new WorkspaceRollBack(pv);
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          listener.workspaceRollback(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#privateVersionDeleted(long)
   */
  @Override
  public void privateVersionDeleted(PrivateVersionDeletedEvent pvdEvent) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      final Workspace pv = this.getWorkspace(pvdEvent.privateVersion);
      this.workspaces.remove(pvdEvent.privateVersion);
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      final WorkspaceClosed event = new WorkspaceClosed(pv);
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          listener.workspaceClosed(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
      this.listeners.remove(pv);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#privateVersionAdded(long)
   */
  @Override
  public void privateVersionAdded(PrivateVersionAddedEvent pvaAvent) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      final Workspace pv = this.getWorkspace(pvaAvent.privateVersion);
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      final WorkspaceAdded event = new WorkspaceAdded(pv);
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          listener.workspaceAdded(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.listeners.DataStorageListener#privateVersionAdded(long)
   */
  @Override
  public void privateVersionRebased(PrivateVersionRebasedEvent pvrEvent) throws RemoteException {
    if (!this.listeners.isEmpty()) {
      final Workspace ws = this.getWorkspace(pvrEvent.privateVersion);
      final Version origin = this.getVersion(pvrEvent.origin);
      final PublicVersion pv = ((PublicVersion) this.getVersion(pvrEvent.oldBaseVersion));
      final Iterator<CloudListener> iterator = this.listeners.iterator();
      final WorkspaceRebased event = new WorkspaceRebased(origin, ws, pv, Collections.emptyList());
      while (iterator.hasNext()) {
        final CloudListener listener = iterator.next();
        try {
          listener.workspaceRebased(event);
        } catch (final RemoteException e) {
          iterator.remove();
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public Set<Artifact> getArtifactDiffs(final long version, final long previousVersion) {
    final Set<Long> diffs = this.dataStorage.getArtifactDiffs(version, previousVersion);
    final Set<Artifact> artifacts = new HashSet<Artifact>();
    for (final Long aid : diffs) {
      final long maxVersion = this.dataStorage.getArtifactMaxVersionNumber(aid, version, previousVersion);
      artifacts.add(this.getArtifact(maxVersion, aid));
    }
    return artifacts;
  }

  @Override
  public Map<Artifact, Set<Property>> getDiffs(final long version) {
    final Map<Long, Set<String>> diffs = this.dataStorage.getArtifactPropertyDiffs(version);
    final Map<Artifact, Set<Property>> apDiffs = new HashMap<Artifact, Set<Property>>();
    final Iterator<Entry<Long, Set<String>>> iterator = diffs.entrySet().iterator();
    while (iterator.hasNext()) {
      final Entry<Long, Set<String>> entry = iterator.next();
      final Artifact artifact = this.getArtifact(version, entry.getKey());
      final Set<Property> props = new HashSet<Property>();
      for (final String prop : entry.getValue()) {
        props.add(artifact.getProperty(prop));
      }
      apDiffs.put(artifact, props);

    }
    return apDiffs;
  }

  @Override
  public Map<Artifact, Set<Property>> getDiffs(final long version, final long previousVersion) {
    final Map<Artifact, Set<Property>> diffs = new HashMap<Artifact, Set<Property>>();
    for (long v = previousVersion + 1; v <= version; v++) {
      final Map<Artifact, Set<Property>> diffV = this.getDiffs(v);
      final Iterator<Entry<Artifact, Set<Property>>> it = diffV.entrySet().iterator();
      while (it.hasNext()) {
        final Entry<Artifact, Set<Property>> entry = it.next();
        if (!diffs.containsKey(entry.getKey())) {
          diffs.put(entry.getKey(), new HashSet<Property>());
        }
        diffs.get(entry.getKey()).addAll(entry.getValue());
      }
    }
    return diffs;
  }

  @Override
  public boolean isWorkspaceEmpty(final long versionNumber) {
    return this.dataStorage.isWorkspaceEmpty(versionNumber);
  }

  @Override
  public Object[] getArtifactRepresentation(final long version, final long id) {
    return this.dataStorage.getArtifactRepresentation(version, id);
  }

  @Override
  public void privateVersionParentSet(final PrivateVersionParentSetEvent versionEvent) throws RemoteException {
    if (!listeners.isEmpty()) {
      Workspace parent = null;

      if (versionEvent.newParent != null)
        parent = getWorkspace(versionEvent.newParent);

      Workspace ws = getWorkspace(versionEvent.privateVersion);
      WorkspaceParentSet event = new WorkspaceParentSet(parent, ws);

      for (CloudListener listener : listeners) {
        try {
          listener.workspaceParentSet(event);
        } catch (RemoteException e) {
          listeners.remove(listener);
          logger.warn("listener " + listener + " removed because of: ", e);
        }
      }
    }
  }

  @Override
  public Resource getResource(long version, long id) {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    try {
      final Artifact artifact = ArtifactFactory.getArtifact(this.dataStorage, version, id);
      if (artifact instanceof MetaModel) {
        return (Resource) artifact;
      }
    } catch (final ArtifactDoesNotExistException e) {
      // ignore
    }
    throw new ResourceDoesNotExistException(version, id);
  }

  @Override
  public Collection<Resource> getResources(long version) {
    return this.getArtifacts(version, DataStorage.RESOURCE_TYPE_ID);
  }

  @Override
  public Collection<Resource> getResources(long version, String fullQualifiedName) {
    if (version > this.getHeadVersionNumber()) {
      throw new VersionDoesNotExistException(version);
    }
    Collection<Resource> artifactsWithProperty = GetArtifactsWithFilterUtils.getArtifactsWithProperty(dataStorage, Resource.class, version, DataStorage.PROPERTY_FULL_QUALIFIED_NAME,
        fullQualifiedName, true, new Artifact[] {});
    return artifactsWithProperty;
  }
  
  @Override
  public Collection<User> getUsers() {
    return dataStorage.getUsers();
  }
  
  @Override
  public User getUser(long id) {
   return dataStorage.getUser(id);
  }
  
  @Override
  public User getUserByCredentials(String login, String password) throws CredentialsException {
    return dataStorage.getUserByCredentials(login, password);
  }
  
  @Override
  public User getUserByOwnerId(long ownerId) throws OwnerDoesNotExistException {
    return dataStorage.getUserByOwnerId(ownerId);
  }
  
  @Override
  public User createUser(String name, String login, String password) {
    return dataStorage.createUser(name, login, password);
  }
  
  @Override
  public User updateUser(User user) {
    return dataStorage.updateUser(user);
  }
  
  @Override
  public void deleteUser(long user) {
    dataStorage.deleteUser(user);
  }
  
  @Override
  public void deleteTool(long id) {
    dataStorage.deleteTool(id);
  }
  
  @Override
  public Collection<Tool> getTools() {
    Package pkg = getPackage(getHeadVersionNumber(), DataStorage.TOOL_PACKAGE_ID);
    Collection<Artifact> atools = pkg.getArtifacts();
    
    return atools.stream().filter(Tool.class::isInstance).map(Tool.class::cast).collect(Collectors.toList());
  }
  
  @Override
  public Project createProject(String name) {
    long id = dataStorage.createProject(getHeadVersionNumber(), DataStorage.ADMIN, DataStorage.ROOT_TOOL_ID, null);
    dataStorage.setPropertyValue(getHeadVersionNumber(), DataStorage.ADMIN, DataStorage.ROOT_TOOL_ID, id, DataStorage.PROPERTY_NAME, name);
    return new DefaultProject(dataStorage, id, getHeadVersionNumber());
  }
  
  
  @Override
  public QueryFactory queryFactory() {
    return QueryFactoryImpl.getInstance();
  }
}
