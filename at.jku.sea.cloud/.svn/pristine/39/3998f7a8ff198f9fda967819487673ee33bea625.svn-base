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
 * Workspace.java created on 13.03.2013
 *
 * (c) alexander noehrer
 * (c) andreas demuth
 */

package at.jku.sea.cloud;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.exceptions.ArtifactConflictException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.ArtifactNotPushOrPullableException;
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
import at.jku.sea.cloud.listeners.EventSource;
import at.jku.sea.cloud.listeners.WorkspaceListener;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElement;

/**
 * A workspace is a private working area and the only way to create and adapt {@link Artifact}s and {@link Property}s. A workspace is defined through a unique negative identifier (id), a developer obtaining the workspace (owner), the tool the developer is working with (Tool) and the obtained
 * revision (base). Owner and tool record necessary traceability information in revision control, to answer the question: who did what, and when?
 *
 * Changes contained in the workspace can be committed, in order to publicize those, an update (i.e., a setting a new base version) is as well supported.
 *
 * A workspace only stores the new states of {@link Artifact}s and {@link Property}s with regard to the obtained base version.<br>
 * Consider the public accessible {@link Property} <br>
 * (aid=100,version=5,isAlive=true,isReference=false,name="name",value="Class")<br>
 * Changing this {@link Property} in a workspace (id=-1), would result in the entry:<br>
 * (aid=100,version=-1,isAlive=true,isReference=false,name="name",value="Class1") <br>
 * Committing the change: (aid=100,version=6,isAlive=true,isReference=false,name="name",value="Class1")
 *
 * A base version of a workspace, can as well be another existing workspace. The workspace which's base version is another workspace then see's all performed adaptations of the other workspace.
 *
 * Implementation Note:<br>
 * Implementation wise, there is actually no difference between a public available version, and a workspace, both are stored in the same table. The only difference being is that workspace have negative version numbers.
 *
 * @author alexander noehrer
 * @author mriedl
 */
public interface Workspace extends Version, EventSource<WorkspaceListener> {

  /**
   * Returns the negative id of the workspace.
   *
   * @return the negative id of the workspace.
   */
  public long getId();

  /**
   * Returns {@literal true} if the workspace was closed
   * 
   * @return {@literal true} if the workspace was closed
   */
  public boolean isClosed();

  /**
   * Returns the base {@link Version} of the workspace---either {@link Workspace} or {@link PublicVersion}.
   *
   * @return the base {@link Version} of the workspace.
   */
  public Version getBaseVersion();

  /**
   * Returns the specified parent {@link Workspace} of this workspace.
   * 
   * @return the specified parent {@link Workspace} of this workspace, can be {@literal null} if no parent is specified
   */
  public Workspace getParent();

  /**
   * Sets the specified {@link Workspace} as the new parent of this workspace. Necessary push/pull operations are called automatically.
   * 
   * @param parent
   *          the specified parent {@link Workspace} to be set as new parent, can be {@literal null}.
   * 
   * @throws IllegalArgumentException
   *           if the parent workspace has a different baseVersion
   */
  public void setParent(Workspace parent) throws IllegalArgumentException, WorkspaceExpiredException, WorkspaceConflictException, WorkspaceCycleException;

  /**
   * Returns the collection of workspaces that are children to this {@link Workspace}.
   * 
   * @return the collection of workspaces that are children to this {@link Workspace}.
   */
  public Collection<Workspace> getChildren();

  /**
   * Returns the currently employed push {@link PropagtionType} strategy of the workspace
   * 
   * @return the currently employed push {@link PropagtionType} strategy
   */
  public PropagationType getPush();

  /**
   * Sets the to be employed push strategy of the workspace If set to instant all contents of the workspace are pushed
   * 
   * @param type
   *          the to be set {@link PropagtionType}
   * @throws WorkspaceConflictException
   *           if push is set from triggered to instant and the parent workspace and this workspace contain conflicting changes
   */
  public void setPush(PropagationType type) throws WorkspaceConflictException;

  /**
   * Returns the currently employed pull {@link PropagtionType} strategy of the workspace
   * 
   * @return the currently employed pull {@link PropagtionType} strategy
   */
  public PropagationType getPull();

  /***
   * Sets the to be employed pull strategy of the workspace. If set to instant all contents of the workspace are pulled.
   * 
   * @param type
   *          the to be set {@link PropagtionType}
   */
  public void setPull(PropagationType type);

  // create new artifacts
  /**
   * Returns the newly created {@link Package}.
   *
   * @return the newly created {@link Package}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed
   */
  public Package createPackage() throws WorkspaceExpiredException;
  
  /**
   * Returns the newly created {@link Package} with a name.
   *
   * @return the newly created {@link Package}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed
   */
  public Package createPackage(String name) throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link Package} that itself is contained within the specified {@link Package}.
   *
   * @return a newly created {@link Package} that itself is contained within the specified {@link Package}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed
   */
  public Package createPackage(final Package parent) throws WorkspaceExpiredException;
  
  /**
   * Returns a newly created {@link Package} with a name that itself is contained within the specified {@link Package}.
   *
   * @return a newly created {@link Package} with a name that itself is contained within the specified {@link Package}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed
   */
  public Package createPackage(final Package parent, String name) throws WorkspaceExpiredException;

  /**
   * Returns the newly created {@link Project}.
   *
   * @return the newly created {@link Project}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Project createProject() throws WorkspaceExpiredException;

  /**
   * Returns the newly created {@link Project}.
   *
   * @return the newly created {@link Project}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Project createProject(String name) throws WorkspaceExpiredException;
  
  /**
   * @deprecated
   * 
   * Returns a newly created {@link Project} that is contained within the specified {@link Package}.
   *
   * @return a newly created {@link Project} that is contained within the specified {@link Package}.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  @Deprecated
  public Project createProject(final Package pckg) throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link MetaModel}.
   *
   * @return a newly created {@link MetaModel}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public MetaModel createMetaModel() throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link MetaModel} that is contained within the specified {@link Package}.
   *
   * @return a newly created {@link MetaModel} that is contained within the specified {@link Package}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public MetaModel createMetaModel(final Package pckg) throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link Artifact}.
   *
   * @return a newly created {@link Artifact}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Artifact createArtifact() throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link Artifact} of the specified type.
   *
   * @return a newly created {@link Artifact} of the specified type.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Artifact createArtifact(final Artifact type) throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link Artifact} that is contained within the specified {@link Package}.
   *
   * @return a newly created {@link Artifact} that is contained within the specified {@link Package}.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Artifact createArtifact(final Artifact type, final Container container, final MetaModel metamodel, final Project project, final Map<String, Object> properties) throws WorkspaceExpiredException;

  public Artifact createArtifact(final Package pckg) throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link Artifact} of the specified type and contained within the specified {@link Package}.
   *
   * @return a newly created {@link Artifact} of the specified type and contained within the specified {@link Package}.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Artifact createArtifact(final Artifact type, final Package pckg) throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link CollectionArtifact}.
   *
   * @param containsOnlyArtifacts
   *          if the {@link CollectionAddedElement} should <tt>only</tt> contain objects of type {@link Artifact}.
   * @return the newly created {@link CollectionArtifact}.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed
   */
  public CollectionArtifact createCollection(final boolean containsOnlyArtifacts) throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link CollectionArtifact}.
   *
   * @param containsOnlyArtifacts
   *          if the {@link CollectionAddedElement} should <tt>only</tt> contain objects of type {@link Artifact}.
   * @return the newly created {@link CollectionArtifact}.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed
   */
  public CollectionArtifact createCollection(final boolean containsOnlyArtifacts, final Package pckg) throws WorkspaceExpiredException;

  public CollectionArtifact createCollection(final boolean containsOnlyArtifacts, final Package pckg, Collection<?> elements, Map<String, Object> properties) throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link MapArtifact}.
   *
   * @return the newly created {@link MapArtifact}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed
   */
  public MapArtifact createMap() throws WorkspaceExpiredException;

  /**
   * Returns a newly created {@link MapArtifact} that is contained within the specified {@link Package}.
   *
   * @param pckg
   *          which contains the newly created {@link MapArtifact}
   * @return the newly created {@link MapArtifact}
   * @throws WorkspaceExpiredException
   *           if the workspace is closed
   */
  public MapArtifact createMap(Package pckg) throws WorkspaceExpiredException;

  public Resource createResource(String fullQualifiedName);

  public Resource createResource(String fullQualifiedName, Package pckg, Project project, Collection<Artifact> artifacts);

  public Resource getResource(long id);

  public Collection<Resource> getResources();

  public Collection<Resource> getResources(String fullQualifiedName);

  // get artifacts
  /**
   * Returns the {@link Artifact} with the specified id, its version will be the id of the workspace.
   *
   * @param id
   *          the specified artifact id.
   * @return the {@link Artifact} with the specified id.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed
   * @throws ArtifactDoesNotExistException
   *           if an {@link Artifact} with the specified id does not exist.
   */
  public Artifact getArtifact(final long id) throws WorkspaceExpiredException, ArtifactDoesNotExistException;

  /**
   * Returns a collection of all currently available {@link Artifact}s.<br>
   * Speaking with sets: (BaseVersion U WorkspaceAdded) / WorkspaceRemoved
   *
   * @return the collection of currently available {@link Artifact}s.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Collection<Artifact> getArtifacts() throws WorkspaceExpiredException;

  public Collection<Artifact> getArtifacts(Set<Long> ids) throws WorkspaceExpiredException;

  public Object[] getArtifactRepresentation(long id);

  public Collection<Artifact> getArtifacts(final Artifact... filters) throws WorkspaceExpiredException;

  public Collection<Artifact> getArtifactsWithProperty(final String propertyName, final Object propertyValue, final Artifact... filters) throws WorkspaceExpiredException;

  public Collection<Artifact> getArtifactsWithProperty(final String propertyName, final Object propertyValue, boolean alive, final Artifact... filters) throws WorkspaceExpiredException;

  public Collection<Artifact> getArtifactsWithProperty(final Map<String, Object> propertyToValue, final Artifact... filters) throws WorkspaceExpiredException;

  public Collection<Artifact> getArtifactsWithProperty(final Map<String, Object> propertyToValue, boolean alive, final Artifact... filters) throws WorkspaceExpiredException;

  public Collection<Artifact> getArtifactsWithReference(Artifact artifact, final Artifact... filters) throws WorkspaceExpiredException;

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(final Artifact... filters) throws WorkspaceExpiredException;

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(final String propertyName, final Object propertyValue, boolean alive, final Artifact... filters)
      throws WorkspaceExpiredException;

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(final Map<String, Object> propertyToValue, boolean alive, final Artifact... filters) throws WorkspaceExpiredException;

  public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(final Set<Artifact> artifacts, final Set<String> properties) throws WorkspaceExpiredException;

  public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(final Set<Artifact> artifacts) throws WorkspaceExpiredException;

  public Package getPackage(final long id) throws WorkspaceExpiredException, PackageDoesNotExistException;

  public Collection<Package> getPackages() throws WorkspaceExpiredException;

  public Project getProject(final long id) throws WorkspaceExpiredException, ProjectDoesNotExistException;

  public Collection<Project> getProjects() throws WorkspaceExpiredException;

  public MetaModel getMetaModel(final long id) throws WorkspaceExpiredException, MetaModelDoesNotExistException;

  public Collection<MetaModel> getMetaModels() throws WorkspaceExpiredException;

  public MetaModel getMetaMetaModel(final Artifact artifact) throws WorkspaceExpiredException;

  public CollectionArtifact getCollectionArtifact(final long id) throws WorkspaceExpiredException, CollectionArtifactDoesNotExistException;

  public Collection<CollectionArtifact> getCollectionArtifacts() throws WorkspaceExpiredException;

  // commit and rollback artifacts
  /**
   * Rebases the workspace to the specified {@link Version} (i.e., sets a new base version). This operation only returns the set of {@link Artifact}s and {@link Property}s that are in conflict. Meaning, that private adaptations overwrite the public adaptations.
   *
   * @param version
   *          the version it should be rebased to.
   * @return the set of {@link Artifact}s and {@link Property}s that are in conflict with the applied private adaptations.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Map<Artifact, Set<Property>> rebase(Version version) throws WorkspaceExpiredException;

  /**
   * Rebases the workspace to the current existing head {@link Version} (i.e., sets a new base version). This operation only returns the set of {@link Artifact}s and {@link Property}s that are in conflict. Meaning, that private adaptations overwrite the public adaptations.
   *
   * @return the set of {@link Artifact}s and {@link Property}s that are in conflict with the applied private adaptations.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Map<Artifact, Set<Property>> rebaseToHeadVersion() throws WorkspaceExpiredException;

  /**
   * Commits (i.e., publishes) the specified artifact ({@link Artifact}), creates a new public version, and implicitly sets the base version of the workspace to the newly introduced version
   * 
   * @param artifact
   *          the specified artifact ({@link Artifact}) to commit
   * @param message
   *          the commit message, can be set to null.
   * @return the new head revision number
   * @throws ArtifactDoesNotExistException
   *           if the {@link Artifact} does no longer exist.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   * @throws ArtifactConflictException
   *           if newer versions than the base version as well contain changes to this {@link Artifact}
   * @throws WorkspaceEmptyException
   *           if there are no changes for this workspace
   */
  public long commitArtifact(final Artifact artifact, final String message) throws ArtifactDoesNotExistException, WorkspaceExpiredException, ArtifactConflictException, WorkspaceEmptyException;

  /***
   * Deletes all stored changes for the specified artifact ({@link Artifact}) in the workspace ({@link Workspace}). If the artifact is only contained in the workspace (i.e., has no previous public version) the artifact ceases to exist. In this case, all of its properties are as well deleted.
   * 
   * @param artifact
   *          the specified artifact for which changes should be deleted.
   * @throws ArtifactDoesNotExistException
   *           if the artifact does not exist.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public void rollbackArtifact(final Artifact artifact) throws ArtifactDoesNotExistException, WorkspaceExpiredException;

  /***
   * Commits (i.e., publishes) the specified property ({@link Property}), creates a new public version, and implicitly sets the base version of the workspace to the newly introduced version
   * 
   * @param property
   *          the specified property to commit.
   * @param message
   *          the commit message, can be set to null.
   * @return the new head version number
   * @throws ArtifactDoesNotExistException
   *           if the artifact ({@link Artifact}) to which the specified property belongs does no longer exist
   * @throws PropertyDoesNotExistException
   *           if the property does not exist.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   * @throws PropertyConflictException
   *           if the newer versions than the base version as well contain changes to the specified property.
   * @throws PropertyNotCommitableException
   *           if the artifact the property belongs to only exists in the workspace.
   */
  public long commitProperty(final Property property, final String message) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, WorkspaceExpiredException, PropertyConflictException,
      PropertyNotCommitableException;

  /***
   * Deletes all stored changes for the specified property ({@link Property}) in the workspace ({@link Workspace}).
   * 
   * @param property
   *          the specified property for which changes should be deleted.
   * @throws ArtifactDoesNotExistException
   *           if the artifact ({@link Artifact}) to which the property belongs does not exist.
   * @throws PropertyDoesNotExistException
   *           if the property does not exist.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public void rollbackProperty(final Property property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, WorkspaceExpiredException;

  /***
   * Commits (i.e., publishes) the contents of the workspace, creates a new public version, and implicitly sets the base version of the workspace to the newly introduced version
   * 
   * @param message
   *          the commit message, can be set to null.
   * @return the new head version number.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   * @throws VersionConflictException
   *           if the newer versions than the base version contains similar changes.
   */
  public long commitAll(final String message) throws WorkspaceExpiredException, VersionConflictException;

  /***
   * Deletes all stored changes in the {@link Workspace}.
   * 
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public void rollbackAll() throws WorkspaceExpiredException;

  /***
   * Pushes the specified artifact ({@link Artifact}) to the parent of this workspace. If no parent is specified, implicitly {@link #commitArtifact(Artifact, String)} is called with an empty string.
   * 
   * @param artifact
   *          the specified artifact to be pushed.
   * @throws WorkspaceExpiredException
   *           if either the workspace, or its parent is closed.
   */
  public void pushArtifact(final Artifact artifact) throws WorkspaceExpiredException, ArtifactDoesNotExistException, ArtifactNotPushOrPullableException;

  /***
   * Pushes the specified property ({@link Property}) including properties) to the parent of this workspace. If no parent is specified, implicitly {@link #commitProperty(Artifact, String)} is called with an empty commit message string.
   * 
   * @param property
   *          the specified property to be pushed.
   * @throws WorkspaceExpiredException
   *           if either the workspace, or its parent is closed.
   * @throws ArtifactDoesNotExistException
   *           if the artifact ({@link Artifact}) to which the property belongs does not exist.
   * @throws PropertyDoesNotExistException
   *           if the property does not exist.
   * @throws PropertyNotPushOrPullableException
   *           if the property cannot be pushed as it references or belongs to an artifact only available in the parent workspace.
   */
  public void pushProperty(final Property property) throws WorkspaceExpiredException, ArtifactDoesNotExistException, PropertyDoesNotExistException, PropertyNotPushOrPullableException;

  /**
   * Pushes all changes of the workspace ({@link Workspace}) to the parent of this workspace. If no parent is specified, implicitly {@link #commitAll(String)} is called with an empty string.
   * 
   * @throws WorkspaceExpiredException
   * 
   */
  public void pushAll() throws WorkspaceExpiredException;

  /***
   * Pulls changes of the specified artifact ({@link Artifact} including properties) from the parent workspace.
   * 
   * @param artifact
   *          the specified artifact for which changes should be pulled.
   * @throws WorkspaceExpiredException
   *           if either the workspace, or its parent is closed.
   * @throws ArtifactDoesNotExistException
   *           if the artifact does not exist.
   */
  public void pullArtifact(final Artifact artifact) throws WorkspaceExpiredException, ArtifactDoesNotExistException, ArtifactNotPushOrPullableException;

  /**
   * Pulls changes of the specified property ({@link Property}) from the parent workspace.
   * 
   * @param property
   *          the specified property for which changes should be pulled.
   * @throws WorkspaceExpiredException
   *           if either the workspace, or its parent is closed.
   * @throws ArtifactDoesNotExistException
   *           if the artifact ({@link Artifact}) to which the property belongs does not exist.
   * @throws PropertyDoesNotExistException
   *           if the property does not exist.
   * @throws PropertyNotPushOrPullableException
   *           if the property cannot be pulled as it references or belongs to an artifact only available in the parent workspace.
   */
  public void pullProperty(final Property property) throws WorkspaceExpiredException, ArtifactDoesNotExistException, PropertyDoesNotExistException, PropertyNotPushOrPullableException;

  /**
   * Pulls all changes from the parent workspace.
   * 
   * @throws WorkspaceExpiredException
   *           if either the workspace, or its parent is closed.
   */
  public Map<Artifact, Set<Property>> pullAll() throws WorkspaceExpiredException;

  /**
   * Returns the set of artifacts ({@link Artifact}), for which changes exist in the workspace and in the version history from the workspace's baseVersion to the current head version.
   * 
   * @return the set of artifacts that are in conflict with the version history.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Set<Artifact> getArtifactConflicts() throws WorkspaceExpiredException;

  /**
   * Returns the set of artifacts ({@link Artifact}), for which changes exist in the workspace and in the version history from the workspace's baseVersion to the specified publicVersion.
   * 
   * @param publicVersion
   *          the specified version up to which conflicts should be checked.
   * @return the set of artifacts that are in conflict with the version history.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   * @throws IllegalArgumentException
   *           if publicVersion > headVersion || publicVersion < baseVersion.
   */
  public Set<Artifact> getArtifactConflicts(final long publicVersion) throws WorkspaceExpiredException, IllegalArgumentException;

  /**
   * Returns the set of artifacts ({@link Artifact}), for which changes exist in the workspace and in the version history from the workspace's baseVersion to the current head version.
   * 
   * @return the set of properties that are in conflict with the version history.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   */
  public Set<Property> getPropertyConflicts() throws WorkspaceExpiredException;

  /**
   * Returns the set of properties ({@link Property}), for which changes exist in the workspace and in the version history from the workspace's baseVersion to the specified publicVersion.
   * 
   * @param publicVersion
   *          the specified version up to which conflicts should be checked.
   * @return the set of properties that are in conflict with the version history.
   * @throws WorkspaceExpiredException
   *           if the workspace is closed.
   * @throws IllegalArgumentException
   *           if publicVersion > headVersion || publicVersion < baseVersion.
   */
  public Set<Property> getPropertyConflicts(final long publicVersion) throws WorkspaceExpiredException, IllegalArgumentException;

  /**
   * closes the workspace, so it ceases to exist
   */
  public void close() throws WorkspaceExpiredException, WorkspaceNotEmptyException;
}
