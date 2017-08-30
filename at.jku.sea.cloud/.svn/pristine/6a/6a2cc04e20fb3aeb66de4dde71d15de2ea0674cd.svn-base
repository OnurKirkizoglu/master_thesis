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
 * Cloud.java created on 11.03.2013
 *
 * (c) alexander noehrer
 * (c) andreas demuth
 */

package at.jku.sea.cloud;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.CredentialsException;
import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MetaModelDoesNotExistException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.exceptions.PackageDoesNotExistException;
import at.jku.sea.cloud.exceptions.ProjectDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.listeners.CloudListener;
import at.jku.sea.cloud.listeners.EventSource;
import at.jku.sea.cloud.stream.QueryFactory;

public interface Cloud extends EventSource<CloudListener> {

  // cloud properties
  public long getHeadVersionNumber();

  // create infrastructure artifacts
  /***
   * @deprecated Owners should not be created manually anymore, use the User API ({@link #createUser(String, String, String) instead.
   * 
   * Creates an owner, this operation implicitly creates a new {@link Version}, containing only the new owner.
   *
   * @return the owner {@link Artifact}
   */
  public Owner createOwner();

  /***
   * @deprecated Tools should be created by using the {@link #createTool(String, String)} method to set the tool's name and tool version.
   * 
   * Creates a tool, this operation implicitly creates a new {@link Version}, containing only the new tool.
   *
   * @return the tool {@link Artifact}
   */
  public Tool createTool();

  // get infrastructure artifacts
  /**
   * Returns the {@link Owner} for the specified id
   *
   * @param id
   *          the id of the owner
   * @return the Owner object
   */
  public Owner getOwner(final long id) throws OwnerDoesNotExistException;

  public Tool getTool(final long id) throws ToolDoesNotExistException;

  // gets the the workspace for the given owner and identifier (in case tool was closed and reopened before commit)
  public Workspace getWorkspace(final Owner owner, final Tool tool, final String identifier) throws WorkspaceExpiredException;

  public Workspace getWorkspace(final Owner owner, final Tool tool, final String identifier, final long baseVersion) throws WorkspaceExpiredException;

  public Workspace getWorkspace(final long privateVersionNumber) throws WorkspaceExpiredException;

  public List<Workspace> getWorkspaces();

  public Collection<Workspace> getWorkspaceChildren(Long privateVersion) throws WorkspaceExpiredException;

  public Workspace createWorkspace(final Owner owner, final Tool tool, final String identifier) throws WorkspaceExpiredException;

  public Workspace createWorkspace(final Owner owner, final Tool tool, final String identifier, final PropagationType push, final PropagationType pull) throws WorkspaceExpiredException;

  public Workspace createWorkspace(final Owner owner, final Tool tool, final String identifier, final long baseVersion, final PropagationType push, final PropagationType pull)
      throws WorkspaceExpiredException;

  public Workspace createWorkspace(final Owner owner, final Tool tool, final String identifier, final Workspace parent, final PropagationType push, final PropagationType pull)
      throws WorkspaceExpiredException;

  // get artifacts methods
  public Artifact getArtifact(final long version, final long id) throws ArtifactDoesNotExistException, VersionDoesNotExistException;

  public Collection<Artifact> getArtifacts(final long version) throws VersionDoesNotExistException;

  public Collection<Artifact> getArtifacts(final long version, final Set<Long> ids) throws VersionDoesNotExistException;

  public Collection<Artifact> getHeadArtifacts();

  public Collection<Artifact> getArtifacts(final long version, final Artifact... filters) throws VersionDoesNotExistException;

  public Collection<Artifact> getHeadArtifacts(final Artifact... filters);

  /**
   * @param version
   *          the version to look in
   * @param propertyName
   *          the name of the property the returned artifacts should have
   * @param propertyValue
   *          the value the property should have
   * @param filters
   *          the additional filters the returned artifacts should<br>
   *          if an assigned <b>Artifact</b> is a <b>Project</b> only artifacts belonging to this project are returned
   *          <br>
   *          if an assigned <b>Artifact</b> is a <b>Package</b> only artifacts belonging to this package are returned
   *          <br>
   *          if an assigned <b>Artifact</b> is a <b>Owner</b> only artifacts modified last by this owner are returned
   *          <br>
   *          if an assigned <b>Artifact</b> is a <b>Tool</b> only artifacts modified last by this tool are returned<br>
   *          if an assigned <b>Artifact</b> is a <b>MetaModel</b> only artifacts belonging to this meta-model are
   *          returned<br>
   * @return a <b>Collection</b> of <b>Artifact</b>
   */
  public Collection<Artifact> getArtifactsWithProperty(final long version, final String propertyName, final Object propertyValue, final Artifact... filters) throws VersionDoesNotExistException;

  public Collection<Artifact> getArtifactsWithProperty(final long version, final String propertyName, final Object propertyValue, final boolean alive, final Artifact... filters)
      throws VersionDoesNotExistException;

  public Collection<Artifact> getArtifactsWithProperty(final long version, final Map<String, Object> propertyToValue, final Artifact... filters) throws VersionDoesNotExistException;

  public Collection<Artifact> getArtifactsWithProperty(final long version, final Map<String, Object> propertyToValue, final boolean alive, final Artifact... filters)
      throws VersionDoesNotExistException;

  public Collection<Artifact> getArtifactsWithReference(final long version, Artifact artifact, final Artifact... filters) throws WorkspaceExpiredException;

  public Collection<Artifact> getHeadArtifactsWithProperty(final String propertyName, final Object propertyValue, final Artifact... filters);

  public Collection<Artifact> getHeadArtifactsWithProperty(final String propertyName, final Object propertyValue, final boolean alive, final Artifact... filters);

  public Collection<Artifact> getHeadArtifactsWithProperty(final Map<String, Object> propertyToObject, final Artifact... filters) throws VersionDoesNotExistException;

  public Collection<Artifact> getHeadArtifactsWithProperty(final Map<String, Object> propertyToObject, final boolean alive, final Artifact... filters) throws VersionDoesNotExistException;

  public Collection<Property> getPropertiesByReference(final long version, final Artifact artifact) throws VersionDoesNotExistException;

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(final long version, final Artifact... filters) throws WorkspaceExpiredException;

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(final long version, final String propertyName, final Object propertyValue, boolean alive, final Artifact... filters)
      throws WorkspaceExpiredException;

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(final long version, final Map<String, Object> propertyToValue, boolean alive, final Artifact... filters)
      throws WorkspaceExpiredException;

  public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(final long version, final Set<Artifact> artifacts, final Set<String> properties) throws WorkspaceExpiredException;

  public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(final long version, final Set<Artifact> artifacts) throws WorkspaceExpiredException;

  public Package getPackage(final long version, final long id) throws PackageDoesNotExistException, VersionDoesNotExistException;

  public Collection<Package> getPackages(final long version) throws VersionDoesNotExistException, VersionDoesNotExistException;

  public Collection<Package> getHeadPackages();

  public Project getProject(final long version, final long id) throws ProjectDoesNotExistException, VersionDoesNotExistException;

  public Collection<Project> getProjects(final long version) throws VersionDoesNotExistException;

  public Collection<Project> getHeadProjects();

  public MetaModel getMetaModel(final long version, final long id) throws MetaModelDoesNotExistException, VersionDoesNotExistException;

  public Collection<MetaModel> getMetaModels(final long version) throws VersionDoesNotExistException, VersionDoesNotExistException;

  public Collection<MetaModel> getHeadMetaModels();

  public Resource getResource(final long version, final long id);

  public Collection<Resource> getResources(final long version);

  public Collection<Resource> getResources(final long version, final String fullQualifiedName);

  public CollectionArtifact getCollectionArtifact(final long version, final long id) throws CollectionArtifactDoesNotExistException, VersionDoesNotExistException;

  public Collection<CollectionArtifact> getCollectionArtifacts(final long version) throws VersionDoesNotExistException;

  public MapArtifact getMapArtifact(final long version, final long id) throws MapArtifactDoesNotExistException, VersionDoesNotExistException;

  public Collection<MapArtifact> getMapArtifacts(final long version) throws VersionDoesNotExistException;

  public Collection<CollectionArtifact> getHeadCollectionArtifacts();

  // version browsing
  public Version getVersion(final long version) throws VersionDoesNotExistException;

  public List<Version> getAllVersions();

  public List<Version> getPrivateVersions();

  public List<Version> getArtifactHistoryVersionNumbers(final Artifact artifact) throws ArtifactDoesNotExistException;

  public List<Version> getPropertyHistoryVersionNumbers(final Artifact artifact, final String property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  // differing artifact/properties of two public versions
  public Set<Artifact> getArtifactDiffs(final long version, final long previousVersion) throws VersionDoesNotExistException;

  public Map<Artifact, Set<Property>> getDiffs(final long version) throws VersionDoesNotExistException;

  public Map<Artifact, Set<Property>> getDiffs(final long version, final long previousVersion) throws VersionDoesNotExistException;

  public boolean isWorkspaceEmpty(long versionNumber);

  public Object[] getArtifactRepresentation(long version, long id);
  
  // user management
  
  /**
   * Gets a list of all users.
   * @return
   */
  public Collection<User> getUsers();
  
  /**
   * Gets one user by id.
   * @param id
   * @return
   * @throws OwnerDoesNotExistException
   */
  public User getUser(long id) throws OwnerDoesNotExistException;
  
  /**
   * Get one user by login and password.<p>
   * 
   * The presence of a {@link User} instance alone does NOT mean that the user has logged in,
   * as the {@link #getUsers()}, {@link #getUser(long)} and {@link #getUserByOwnerId(long)} methods
   * will also provide the user with an instance.<p>
   * 
   * There is, as of now, no real login process.<br>
   * 
   * For that, another layer would be needed that might use this function to check if the supplied credentials are genuine,
   * and then provides the user with something like a Session object.<p>
   * 
   * @param login
   * @param password
   * @return
   * @throws CredentialsException if no matching user exists (login doesn't exist or password is incorrect).
   */
  public User getUserByCredentials(String login, String password) throws CredentialsException;
  
  /**
   * Get the user for a Owner instance, if one exists.
   * 
   * @param ownerId
   * @return
   */
  public User getUserByOwnerId(long ownerId) throws OwnerDoesNotExistException;
  
  /**
   * Creates a new user with the given data.
   * Implicitly also creates a Owner artifact for the user and thus a new head version.
   * @param name
   * @param login
   * @param password
   * @return a new user instance
   * @throws CredentialsException when there is already a user with this login name
   */
  public User createUser(String name, String login, String password) throws CredentialsException;
  
  /**
   * Updates a user. May not actually commit every changed property.
   * 
   * In the current implementation, password is changed if it is set to a value other than null,
   * login is changed if there is no other user with the same login,
   * Owner ID cannot be changed as it is a 1:1 relationship.
   * The updated fields name and login are also changed in the corresponding Owner artefact.
   * 
   * @param user
   * @return
   * @throws OwnerDoesNotExistException if there is no user with this ID
   * @throws CredentialsException       if the login was supposed to be changed to a login already in use
   */
  public User updateUser(User user) throws OwnerDoesNotExistException, CredentialsException;
  
  /**
   * Deletes a user by id.
   * @param userId
   * @throws OwnerDoesNotExistException
   */
  public void deleteUser(long userId) throws OwnerDoesNotExistException;
  

  /**
   * Creates a new Tool artifact with pre-filled properties name and toolVersion.
   * @param name
   * @param toolVersion
   * @return
   */
  public Tool createTool(String name, String toolVersion);
  
  /**
   * Deletes a Tool artifact by id.
   * @param id
   * @throws ToolDoesNotExistException
   */
  public void deleteTool(long id) throws ToolDoesNotExistException;

  /**
   * Gets a list of all tools.
   * @return
   */
  public Collection<Tool> getTools();
  
  /**
   * Creates a project .
   */
  public Project createProject(String name);
  
   /**
   * Creates the {@code QueryFactory} depending on the implementation of this {@code Cloud}.
   * 
   * @return instance of a {@code QueryFactory}
   */
  QueryFactory queryFactory();
}
