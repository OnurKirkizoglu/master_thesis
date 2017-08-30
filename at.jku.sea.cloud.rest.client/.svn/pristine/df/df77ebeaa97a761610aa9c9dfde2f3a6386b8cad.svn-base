package at.jku.sea.cloud.rest.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Property;
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
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.listeners.CloudListener;
import at.jku.sea.cloud.rest.client.handler.CloudHandler;
import at.jku.sea.cloud.rest.client.handler.ToolHandler;
import at.jku.sea.cloud.rest.client.handler.UserHandler;
import at.jku.sea.cloud.rest.client.stream.RestQueryFactory;
import at.jku.sea.cloud.stream.QueryFactory;
/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestCloud implements Cloud {

	private static RestCloud instance;

	public static RestCloud getInstance() {
		if (instance == null) {
			instance = new RestCloud();
		}
		return instance;
	}

	private CloudHandler handler;
	private UserHandler userHandler;
	private ToolHandler toolHandler;

	private RestCloud() {
		handler = CloudHandler.getInstance();
		userHandler = UserHandler.getInstance();
		toolHandler = ToolHandler.getInstance();
	}

	@Override
	public Owner getOwner(long id) throws OwnerDoesNotExistException {
		return handler.getOwner(id);
	}

	@Override
	public Tool getTool(long id) throws ToolDoesNotExistException {
		return handler.getTool(id);
	}

	@Override
	public Workspace getWorkspace(Owner owner, Tool tool, String identifier) throws WorkspaceExpiredException {
		return handler.getWorkspace(owner, tool, identifier);
	}

	@Override
	public Workspace getWorkspace(Owner owner, Tool tool, String identifier, long baseVersion)
			throws WorkspaceExpiredException {
		return handler.getWorkspace(owner, tool, identifier, baseVersion);
	}

	@Override
	public Workspace getWorkspace(long privateVersionNumber) throws WorkspaceExpiredException {
		return handler.getWorkspace(privateVersionNumber);
	}

	@Override
	public Workspace createWorkspace(Owner owner, Tool tool, String identifier) throws WorkspaceExpiredException {
		return handler.createWorkspace(owner, tool, identifier);
	}

	@Override
	public Workspace createWorkspace(Owner owner, Tool tool, String identifier, PropagationType push,
			PropagationType pull) throws WorkspaceExpiredException {
		return handler.createWorkspace(owner, tool, identifier, push, pull);
	}

	@Override
	public Workspace createWorkspace(Owner owner, Tool tool, String identifier, long baseVersion, PropagationType push,
			PropagationType pull) throws WorkspaceExpiredException {
		return handler.createWorkspace(owner, tool, identifier, baseVersion, push, pull);
	}

	@Override
	public Workspace createWorkspace(Owner owner, Tool tool, String identifier, Workspace parent, PropagationType push,
			PropagationType pull) throws WorkspaceExpiredException {
		return handler.createWorkspace(owner, tool, identifier, parent, push, pull);
	}

	@Override
	public Version getVersion(long version) throws VersionDoesNotExistException {
		return handler.getVersion(version);
	}

	@Override
	public long getHeadVersionNumber() {
		return handler.getHeadVersionNumber();
	}

	@Override
	public Artifact getArtifact(long version, long id) {
		return handler.getArtifact(version, id);
	}

	@Override
	public void addListener(CloudListener listener) {
		handler.addListener(listener);
	}

	@Override
	public void removeListener(CloudListener listener) {
		handler.removeListener(listener);
	}

	@Override
	public List<Version> getAllVersions() {
		return handler.getAllVersions();
	}

	@Override
	public Collection<MetaModel> getMetaModels(long versionNumber) {
		return handler.getMetaModels(versionNumber);
	}

	@Override
	public Collection<Package> getPackages(long versionNumber) {
		return handler.getPackages(versionNumber);
	}

	@Override
	public Collection<Project> getProjects(long versionNumber) {
		return handler.getProjects(versionNumber);
	}

	@Override
	public List<Workspace> getWorkspaces() {
		return handler.getWorkspaces();
	}

	@Override
	public Collection<Artifact> getArtifacts(long versionNumber, Artifact... filter) {
		return handler.getArtifacts(versionNumber, filter);
	}

	@Override
	public Collection<Artifact> getArtifacts(long versionNumber) {
		return handler.getArtifacts(versionNumber);
	}

	@Override
	public Collection<Artifact> getArtifacts(long version, Set<Long> ids) throws VersionDoesNotExistException {
		return handler.getArtifacts(version, ids);
	}

	@Override
	public Owner createOwner() {
		return handler.createOwner();
	}

	@Override
	public Tool createTool(String name, String toolVersion) {
		return toolHandler.createTool(name, toolVersion);
	}

	// @Override
	// public Workspace getWorkspace(long privateVersionNumber)
	// throws WorkspaceExpiredException {
	// return handler.getWorkspace(privateVersionNumber);
	// }

	@Override
	public Collection<Artifact> getHeadArtifacts() {
		return handler.getHeadArtifacts();
	}

	@Override
	public Collection<Artifact> getHeadArtifacts(Artifact... filters) {
		return handler.getHeadArtifacts(filters);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(long version, String propertyName, Object propertyValue,
			Artifact... filters) throws VersionDoesNotExistException {
		return handler.getArtifactsWithProperty(version, propertyName, propertyValue, filters);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(long version, Map<String, Object> propertyToValue,
			Artifact... filters) throws VersionDoesNotExistException {
		return handler.getArtifactWithProperty(version, propertyToValue, filters);
	}

	@Override
	public Collection<Artifact> getHeadArtifactsWithProperty(String propertyName, Object propertyValue,
			Artifact... filters) {
		return handler.getHeadArtifactsWithProperty(propertyName, propertyValue, filters);
	}

	@Override
	public Collection<Artifact> getHeadArtifactsWithProperty(Map<String, Object> propertyToObject, Artifact... filters)
			throws VersionDoesNotExistException {
		return handler.getHeadArtifactsWithProperty(propertyToObject, filters);
	}

	@Override
	public Collection<Property> getPropertiesByReference(long version, Artifact artifact)
			throws VersionDoesNotExistException {
		return handler.getPropertiesByReference(version, artifact);
	}

	@Override
	public Package getPackage(long version, long id) throws PackageDoesNotExistException, VersionDoesNotExistException {
		return handler.getPackage(version, id);
	}

	@Override
	public Collection<Package> getHeadPackages() {
		return handler.getHeadPackages();
	}

	@Override
	public Project getProject(long version, long id) throws ProjectDoesNotExistException, VersionDoesNotExistException {
		return handler.getProject(version, id);
	}

	@Override
	public Collection<Project> getHeadProjects() {
		return handler.getHeadProjects();
	}

	@Override
	public MetaModel getMetaModel(long version, long id)
			throws MetaModelDoesNotExistException, VersionDoesNotExistException {
		return handler.getMetaModel(version, id);
	}

	@Override
	public Collection<MetaModel> getHeadMetaModels() {
		return handler.getHeadMetaModels();
	}

	@Override
	public CollectionArtifact getCollectionArtifact(long version, long id)
			throws CollectionArtifactDoesNotExistException, VersionDoesNotExistException {
		return handler.getCollectionArtifact(version, id);
	}

	@Override
	public Collection<CollectionArtifact> getCollectionArtifacts(long version) throws VersionDoesNotExistException {
		return handler.getCollectionArtifacts(version);
	}

	@Override
	public MapArtifact getMapArtifact(long version, long id) throws MapArtifactDoesNotExistException {
		return handler.getMapArtifact(version, id);
	}

	@Override
	public Collection<MapArtifact> getMapArtifacts(long version) throws VersionDoesNotExistException {
		return handler.getMapArtifacts(version);
	}

	@Override
	public Collection<CollectionArtifact> getHeadCollectionArtifacts() {
		return handler.getHeadCollectionArtifacts();
	}

	@Override
	public List<Version> getPrivateVersions() {
		return handler.getPrivateVersions();
	}

	@Override
	public List<Version> getArtifactHistoryVersionNumbers(Artifact artifact) throws ArtifactDoesNotExistException {
		return handler.getArtifactHistoryVersionNumbers(artifact);
	}

	@Override
	public List<Version> getPropertyHistoryVersionNumbers(Artifact artifact, String property)
			throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
		return handler.getPropertyHistoryVersionNumbers(artifact, property);
	}

	@Override
	public Set<Artifact> getArtifactDiffs(long version, long previousVersion) throws VersionDoesNotExistException {
		return handler.getArtifactDiffs(version, previousVersion);
	}

	@Override
	public Map<Artifact, Set<Property>> getDiffs(long version) throws VersionDoesNotExistException {
		return handler.getDiffs(version);
	}

	@Override
	public Map<Artifact, Set<Property>> getDiffs(long version, long previousVersion)
			throws VersionDoesNotExistException {
		return handler.getDiffs(version, previousVersion);
	}

	@Override
	public boolean isWorkspaceEmpty(long versionNumber) {
		return handler.isWorkspaceEmpty(versionNumber);
	}

	@Override
	public Object[] getArtifactRepresentation(long version, long id) {
		return handler.getArtifactRepresentation(version, id);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(long version, String propertyName, Object propertyValue,
			boolean alive, Artifact... filters) throws VersionDoesNotExistException {
		return handler.getArtifactsWithProperty(version, propertyName, propertyValue, alive, filters);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(long version, Map<String, Object> propertyToValue,
			boolean alive, Artifact... filters) throws VersionDoesNotExistException {
		return handler.getArtifactsWithProperty(version, propertyToValue, alive, filters);
	}

	@Override
	public Collection<Artifact> getHeadArtifactsWithProperty(String propertyName, Object propertyValue, boolean alive,
			Artifact... filters) {
		return handler.getHeadArtifactsWithProperty(propertyName, propertyValue, alive, filters);
	}

	@Override
	public Collection<Artifact> getHeadArtifactsWithProperty(Map<String, Object> propertyToObject, boolean alive,
			Artifact... filters) throws VersionDoesNotExistException {
		return handler.getHeadArtifactsWithProperty(propertyToObject, alive, filters);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long version, Artifact... filters)
			throws WorkspaceExpiredException {
		return handler.getArtifactsAndPropertyMap(version, filters);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long version, String propertyName,
			Object propertyValue, boolean alive, Artifact... filters) throws WorkspaceExpiredException {
		return handler.getArtifactsAndPropertyMap(version, propertyName, propertyValue, alive, filters);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long version,
			Map<String, Object> propertyToValue, boolean alive, Artifact... filters) throws WorkspaceExpiredException {
		return handler.getArtifactsAndPropertyMap(version, propertyToValue, alive, filters);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(long version, Set<Artifact> artifacts,
			Set<String> properties) throws WorkspaceExpiredException {
		return handler.getArtifactPropertyMap(version, artifacts, properties);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(long version, Set<Artifact> artifacts)
			throws WorkspaceExpiredException {
		return handler.getArtifactPropertyMap(version, artifacts, null);
	}

	@Override
	public Collection<Artifact> getArtifactsWithReference(long version, Artifact artifact, Artifact... filters)
			throws WorkspaceExpiredException {
		return handler.getArtifactsWithReference(version, artifact, filters);
	}

	@Override
	public Collection<Workspace> getWorkspaceChildren(Long privateVersion) throws WorkspaceExpiredException {
		return handler.getWorkspaceChildren(privateVersion);
	}

	@Override
	public Resource getResource(long version, long id) {
		return handler.getResource(version, id);
	}

	@Override
	public Collection<Resource> getResources(long version) {
		return handler.getResources(version);
	}

	@Override
	public Collection<Resource> getResources(long version, String fullQualifiedName) {
		return handler.getResource(version, fullQualifiedName);
	}
	
	@Override
	public Collection<User> getUsers() {
		return userHandler.getUsers();
	}
	
	@Override
	public User getUser(long id) {
		return userHandler.getUser(id);
	}
	
	@Override
	public User createUser(String name, String login, String password) {
		return userHandler.insertUser(name, login, password);
	}
	
	@Override
	public User updateUser(User user) {
		return userHandler.updateUser(user);
	}
	
	@Override
	public void deleteUser(long userId) {
		userHandler.deleteUser(userId);
	}

	@Override
	public User getUserByOwnerId(long ownerId) {
	  return userHandler.getUserByOwnerId(ownerId);
	}

	@Override
	public User getUserByCredentials(String login, String password) throws CredentialsException {
		return userHandler.getUserByCredentials(login,password);
	}
	
	@Override
	public void deleteTool(long id) throws ToolDoesNotExistException {
		toolHandler.deleteTool(id);
	}
	
	@Override
	public Collection<Tool> getTools() {
		return toolHandler.getTools();
	}

	public Tool createTool() {
		return createTool("", null);
	}
	
	@Override
	public Project createProject(String name) {
	  return handler.createProject(name);
	}
	
  @Override
  public QueryFactory queryFactory() {
    return RestQueryFactory.getInstance();
  }
}
