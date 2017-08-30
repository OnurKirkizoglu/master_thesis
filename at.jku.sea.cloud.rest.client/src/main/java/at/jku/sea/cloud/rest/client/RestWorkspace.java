package at.jku.sea.cloud.rest.client;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
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
import at.jku.sea.cloud.exceptions.WorkspaceEmptyException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.listeners.WorkspaceListener;
import at.jku.sea.cloud.rest.client.handler.WorkspaceHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestWorkspace extends RestVersion implements Workspace {

	private long id;

	private WorkspaceHandler handler;

	protected RestWorkspace(long id, long version) {
		super(version);
		this.id = id;
		handler = WorkspaceHandler.getInstance();
	}

	@Override
	public long getVersionNumber() {
		return id;
	}

	@Override
	public Owner getOwner() {
		return handler.getOwner(id);
	}

	@Override
	public Tool getTool() {
		return handler.getTool(id);
	}

	@Override
	public String getIdentifier() {
		return handler.getIdentifier(id);
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public Version getBaseVersion() {
		return handler.getBaseVersion(id);
	}

	@Override
	public Map<Artifact, Set<Property>> rebase(Version version) throws WorkspaceExpiredException {
		return handler.rebase(id, version);
	}

	@Override
	public Map<Artifact, Set<Property>> rebaseToHeadVersion() throws WorkspaceExpiredException {
		return handler.rebaseToHead(id);
	}

	@Override
	public Package createPackage() throws WorkspaceExpiredException {
		return handler.createPackage(id);
	}
	
	@Override
	public Package createPackage(String name) throws WorkspaceExpiredException {
	  return handler.createPackage(id, name);
	}

	@Override
	public Package createPackage(Package pckg) throws WorkspaceExpiredException {
		return handler.createPackage(id, pckg.getId(), pckg.getVersionNumber());
	}
	
	@Override
	public Package createPackage(Package pkg, String name) throws WorkspaceExpiredException {
	  return handler.createPackage(id, pkg.getId(), pkg.getVersionNumber(), name);
	}

	@Override
	public Project createProject() throws WorkspaceExpiredException {
		return handler.createProject(id);
	}

	@Override
	public Project createProject(String name) throws WorkspaceExpiredException {
	  return handler.createProject(id, name);
	}
	
	@Override
	public Project createProject(Package pkg) throws WorkspaceExpiredException {
		return handler.createProject(id, pkg.getId(), pkg.getVersionNumber());
	}

	@Override
	public MetaModel createMetaModel() throws WorkspaceExpiredException {
		return handler.createMetaModel(id);
	}

	@Override
	public MetaModel createMetaModel(Package pkg) throws WorkspaceExpiredException {
		return handler.createMetaModel(id, pkg.getId(), pkg.getVersionNumber());
	}

	@Override
	public Artifact createArtifact() throws WorkspaceExpiredException {
		return handler.createArtifact(id);
	}

	@Override
	public Artifact createArtifact(Artifact type) throws WorkspaceExpiredException {
		return handler.createArtifact(id, type);
	}

	@Override
	public Artifact createArtifact(Package pkg) throws WorkspaceExpiredException {
		return handler.createArtifact(id, pkg.getId(), pkg.getVersionNumber());
	}

	@Override
	public Artifact createArtifact(Artifact type, Package pkg) throws WorkspaceExpiredException {
		return handler.createArtifact(id, pkg.getId(), pkg.getVersionNumber(), type);
	}

	@Override
	public Artifact createArtifact(Artifact type, Container container, MetaModel metamodel, Project project,
			Map<String, Object> properties) throws WorkspaceExpiredException {
		return handler.createArtifact(id, type, container, metamodel, project, properties);
	}

	@Override
	public CollectionArtifact createCollection(boolean containsOnlyArtifacts) throws WorkspaceExpiredException {
		return handler.createCollection(id, containsOnlyArtifacts);
	}

	@Override
	public CollectionArtifact createCollection(boolean containsOnlyArtifacts, Package pkg)
			throws WorkspaceExpiredException {
		return handler.createCollection(id, pkg.getId(), pkg.getVersionNumber(), containsOnlyArtifacts);
	}

	@Override
	public CollectionArtifact createCollection(boolean containsOnlyArtifacts, Package pckg, Collection<?> elements,
			Map<String, Object> properties) throws WorkspaceExpiredException {
		return handler.createCollection(id, pckg, containsOnlyArtifacts, elements, properties);
	}

	@Override
	public MapArtifact createMap() throws WorkspaceExpiredException {
		return handler.createMap(id);
	}

	@Override
	public MapArtifact createMap(Package pkg) throws WorkspaceExpiredException {
		return handler.createMap(id, pkg.getId(), pkg.getVersionNumber());
	}

	@Override
	public Artifact getArtifact(long id) throws WorkspaceExpiredException, ArtifactDoesNotExistException {
		return handler.getArtifact(this.id, id);
	}

	@Override
	public Object[] getArtifactRepresentation(long id) {
		return handler.getArtifactRepresentation(this.id, id);
	}

	@Override
	public Collection<Artifact> getArtifacts() throws WorkspaceExpiredException {
		return handler.getArtifacts(id);
	}

	@Override
	public Collection<Artifact> getArtifacts(Set<Long> ids) throws WorkspaceExpiredException {
		return handler.getArtifacts(id, ids);
	}

	@Override
	public Collection<Artifact> getArtifacts(Artifact... filters) throws WorkspaceExpiredException {
		return handler.getArtifacts(id, filters);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(String propertyName, Object propertyValue, Artifact... filters)
			throws WorkspaceExpiredException {
		return handler.getArtifacts(id, filters, propertyName, propertyValue);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(Map<String, Object> propertyToValue, Artifact... filters)
			throws WorkspaceExpiredException {
		return handler.getArtifacts(id, filters, propertyToValue);
	}

	@Override
	public Package getPackage(long id) throws WorkspaceExpiredException, PackageDoesNotExistException {
		return handler.getPackage(this.id, id);
	}

	@Override
	public Collection<Package> getPackages() throws WorkspaceExpiredException {
		return handler.getPackages(id);
	}

	@Override
	public Project getProject(long id) throws WorkspaceExpiredException, ProjectDoesNotExistException {
		return handler.getProject(this.id, id);
	}

	@Override
	public Collection<Project> getProjects() throws WorkspaceExpiredException {
		return handler.getProjects(id);
	}

	@Override
	public MetaModel getMetaModel(long id) throws WorkspaceExpiredException, MetaModelDoesNotExistException {
		return handler.getMetaModel(this.id, id);
	}

	@Override
	public Collection<MetaModel> getMetaModels() throws WorkspaceExpiredException {
		return handler.getMetaModels(id);
	}

	@Override
	public MetaModel getMetaMetaModel(Artifact artifact) throws WorkspaceExpiredException {
		return handler.getMetaMetaModel(this.id, artifact);
	}

	@Override
	public CollectionArtifact getCollectionArtifact(long id)
			throws WorkspaceExpiredException, CollectionArtifactDoesNotExistException {
		return handler.getCollectionArtifact(this.id, id);
	}

	@Override
	public Collection<CollectionArtifact> getCollectionArtifacts() throws WorkspaceExpiredException {
		return handler.getCollectionArtifacts(this.id);
	}

	@Override
	public long commitArtifact(Artifact artifact, String message)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, ArtifactConflictException, WorkspaceEmptyException {
		return handler.commitArtifact(id, artifact.getId(), message);
	}

	@Override
	public void rollbackArtifact(Artifact artifact) throws WorkspaceExpiredException, ArtifactDoesNotExistException {
		handler.rollbackArtifact(id, artifact.getId());
	}

	@Override
	public long commitProperty(Property property, String message)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, PropertyDoesNotExistException,
			PropertyConflictException, PropertyNotCommitableException {
		return handler.commitProperty(id, property.getId(), property.getName(), message);
	}

	@Override
	public void rollbackProperty(Property property)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, PropertyDoesNotExistException {
		handler.rollbackProperty(id, property.getId(), property.getName());
	}

	@Override
	public long commitAll(String message) throws WorkspaceExpiredException, VersionConflictException {
		return handler.commitAll(id, message);
	}

	@Override
	public void rollbackAll() throws WorkspaceExpiredException {
		handler.rollbackAll(id);
	}

	@Override
	public Set<Artifact> getArtifactConflicts() throws WorkspaceExpiredException {
		return handler.getArtifactConflicts(id);
	}

	@Override
	public Set<Artifact> getArtifactConflicts(long version) throws WorkspaceExpiredException, IllegalArgumentException {
		return handler.getArtifactConflicts(id, version);
	}

	@Override
	public Set<Property> getPropertyConflicts() throws WorkspaceExpiredException {
		return handler.getPropertyConflicts(id);
	}

	@Override
	public Set<Property> getPropertyConflicts(long version) throws WorkspaceExpiredException, IllegalArgumentException {
		return handler.getPropertyConflicts(id, version);
	}

	@Override
	public void close() {
		handler.close(id);
	}

	@Override
	public void addListener(WorkspaceListener listener) {
		handler.addListener(id, listener);
	}

	@Override
	public void removeListener(WorkspaceListener listener) {
		handler.removeListener(id, listener);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(String propertyName, Object propertyValue, boolean alive,
			Artifact... filters) throws WorkspaceExpiredException {
		return handler.getArtifactsWithProperty(id, propertyName, propertyValue, alive, filters);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(Map<String, Object> propertyToValue, boolean alive,
			Artifact... filters) throws WorkspaceExpiredException {
		return handler.getArtifactsWithProperty(id, propertyToValue, alive, filters);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(Artifact... filters)
			throws WorkspaceExpiredException {
		return handler.getArtifactsAndPropertyMap(id, filters);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(String propertyName, Object propertyValue,
			boolean alive, Artifact... filters) throws WorkspaceExpiredException {
		return handler.getArtifactsAndPropertyMap(id, propertyName, propertyValue, alive, filters);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(Map<String, Object> propertyToValue,
			boolean alive, Artifact... filters) throws WorkspaceExpiredException {
		return handler.getArtifactsAndPropertyMap(id, propertyToValue, alive, filters);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(Set<Artifact> artifacts, Set<String> properties)
			throws WorkspaceExpiredException {
		return handler.getArtifactsPropertyMap(id, artifacts, properties);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(Set<Artifact> artifacts)
			throws WorkspaceExpiredException {
		return handler.getArtifactsPropertyMap(id, artifacts, null);
	}

	@Override
	public Collection<Artifact> getArtifactsWithReference(Artifact artifact, Artifact... filters)
			throws WorkspaceExpiredException {
		return handler.getArtifactsWithReference(this.id, artifact, filters);
	}

	@Override
	public boolean isClosed() {
		return handler.isClosed(id);
	}

	@Override
	public Workspace getParent() throws WorkspaceExpiredException {
		return handler.getParent(id);
	}

	@Override
	public Collection<Workspace> getChildren() {
		return handler.getChildren(id);
	}

	@Override
	public PropagationType getPush() {
		return handler.getPush(id);
	}

	@Override
	public void setPush(PropagationType type) throws WorkspaceConflictException {
		handler.setPush(id, type);
	}

	@Override
	public PropagationType getPull() {
		return handler.getPull(id);
	}

	@Override
	public void setPull(PropagationType type) {
		handler.setPull(id, type);
	}

	@Override
	public void pushArtifact(Artifact artifact)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, ArtifactNotPushOrPullableException {
		handler.pushArtifact(id, artifact);
	}

	@Override
	public void pushProperty(Property property) throws WorkspaceExpiredException, ArtifactDoesNotExistException,
			PropertyDoesNotExistException, PropertyNotPushOrPullableException {
		handler.pushProperty(id, property);
	}

	@Override
	public void pushAll() throws WorkspaceExpiredException {
		handler.pushAll(id);
	}

	@Override
	public void pullArtifact(Artifact artifact)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, ArtifactNotPushOrPullableException {
		handler.pullArtifact(id, artifact);
	}

	@Override
	public void pullProperty(Property property) throws WorkspaceExpiredException, ArtifactDoesNotExistException,
			PropertyDoesNotExistException, PropertyNotPushOrPullableException {
		handler.pullProperty(id, property);
	}

	@Override
	public Map<Artifact, Set<Property>> pullAll() throws WorkspaceExpiredException {
		return handler.pullAll(id);
	}

	@Override
	public void setParent(Workspace parent) throws IllegalArgumentException {
		handler.setParent(this.getId(), parent == null ? null : parent.getId());
	}

	@Override
	public Resource createResource(String fullQualifiedName) {
		return handler.createResource(id, fullQualifiedName);
	}

	@Override
	public Resource createResource(String fullQualifiedName, Package pckg, Project project,
			Collection<Artifact> artifacts) {
		return handler.createResource(id, fullQualifiedName, pckg, project, artifacts);
	}
	
	@Override
	public Resource getResource(long id) {
		return handler.getResource(this.id, id);
	}

	@Override
	public Collection<Resource> getResources() {
		return handler.getResources(id);
	}

	@Override
	public Collection<Resource> getResources(String fullQualifiedName) {
		return handler.getResources(id, fullQualifiedName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestWorkspace other = (RestWorkspace) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
