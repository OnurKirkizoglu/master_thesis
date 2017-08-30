package at.jku.sea.cloud.rest.client;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.rest.client.handler.ContainerHandler;

public class RestContainer extends RestArtifact implements Container {

	private static final long serialVersionUID = 1L;

	private ContainerHandler handler;

	protected RestContainer(long id, long version) {
		super(id, version);
		handler = ContainerHandler.getInstance();
	}

	@Override
	public boolean containsArtifact(Artifact artifact) {
		return handler.containsArtifact(id, version, artifact);
	}

	@Override
	public void addArtifact(Workspace workspace, Artifact artifact) throws ArtifactDeadException {
		handler.addArtifact(workspace.getId(), id, version, artifact);
	}

	@Override
	public void addArtifacts(Workspace workspace, Collection<Artifact> artifacts) throws ArtifactDeadException {
		handler.addArtifacts(workspace.getId(), id, version, artifacts);
	}

	@Override
	public void removeArtifact(Workspace workspace, Artifact artifact) throws ArtifactDeadException {
		handler.removeArtifact(workspace.getId(), id, version, artifact);
	}

	@Override
	public Collection<Artifact> getArtifacts() {
		return handler.getArtifacts(id, version);
	}

	@Override
	public Collection<Artifact> getArtifacts(Filter filter) {
		return handler.getArtifacts(id, version, filter);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(String propertyName, Object propertyValue, boolean alive,
			Filter filter) {
		return handler.getArtifactsWithProperty(id, version, propertyName, propertyValue, alive, filter);
	}

	@Override
	public Collection<Artifact> getArtifactsWithProperty(Map<String, Object> propertyToValue, boolean alive,
			Filter filter) {
		return handler.getArtifactsWithProperty(id, version, propertyToValue, alive, filter);
	}

	@Override
	public Collection<Artifact> getArtifactsWithReference(Artifact artifact, Filter filter) {
		return handler.getArtifactsWithReference(id, version, artifact, filter);
	}

	@Override
	public Map<Artifact, Set<Property>> getArtifactAndProperties() {
		return handler.getArtifactAndProperties(id, version);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactPropertyMap() {
		return handler.getArtifactPropertyMap(id, version);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(Filter filter) {
		return handler.getArtifactsAndPropertyMap(id, version, filter);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(String propertyName, Object propertyValue,
			boolean alive, Filter filter) {
		return handler.getArtifactsAndPropertyMap(id, version, propertyName, propertyValue, alive, filter);
	}

	@Override
	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(Map<String, Object> propertyToValue,
			boolean alive, Filter filter) {
		return handler.getArtifactsAndPropertyMap(id, version, propertyToValue, alive, filter);
	}

	@Override
	public Collection<Object[]> getArtifactRepresentations() {
		return handler.getArtifactRepresentations(id, version);
	}

	@Override
	public Map<Object[], Set<Object[]>> getArtifactPropertyMapRepresentations() {
		return handler.getArtifactPropertyMapRepresentations(id, version);
	}

}
