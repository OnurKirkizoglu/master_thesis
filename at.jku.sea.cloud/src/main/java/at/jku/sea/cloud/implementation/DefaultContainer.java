package at.jku.sea.cloud.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;

abstract class DefaultContainer extends DefaultArtifact implements Container {

  public DefaultContainer(DataStorage dataStorage, long id, long version) {
    super(dataStorage, id, version);
  }

  @Override
  public boolean containsArtifact(Artifact artifact) { // possible extend to work recursively
    return Objects.equals(artifact.getPackage(), this);
  }

  @Override
  public void addArtifact(Workspace workspace, Artifact artifact) throws ArtifactDeadException {
    this.dataStorage.setArtifactContainer(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), artifact.getId(), this.id);
  }

  @Override
  public void addArtifacts(Workspace workspace, Collection<Artifact> artifacts) throws ArtifactDeadException {
    long ownerId = workspace.getOwner().getId();
    long toolId = workspace.getTool().getId();
    long workspaceId = workspace.getId();
    for (Artifact artifact : artifacts) {
      this.dataStorage.setArtifactContainer(workspaceId, ownerId, toolId, artifact.getId(), this.id);
    }
  }

  @Override
  public void removeArtifact(Workspace workspace, Artifact artifact) throws ArtifactDeadException {
    this.dataStorage.setArtifactContainer(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), artifact.getId(), null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Project#getArtifacts()
   */
  @Override
  public Collection<Artifact> getArtifacts() {
    final Collection<Object[]> ids = this.dataStorage.getArtifactsByContainer(this.version, this.id);
    final Collection<Artifact> result = new ArrayList<Artifact>(ids.size());
    for (final Object[] id : ids) {
      result.add(ArtifactFactory.getArtifact(this.dataStorage, version, id));
    }
    return result;
  }

  @Override
  public Collection<Artifact> getArtifacts(Filter filter) {
    return GetArtifactsWithFilterUtils.getArtifactsWithProperty(this.dataStorage, version, null, null, true, createFilterArray(filter));
  }

  @Override
  public Collection<Artifact> getArtifactsWithProperty(String propertyName, Object propertyValue, boolean alive, Filter filter) {
    return GetArtifactsWithFilterUtils.getArtifactsWithProperty(this.dataStorage, version, propertyName, propertyValue, alive, createFilterArray(filter));
  }

  @Override
  public Collection<Artifact> getArtifactsWithProperty(Map<String, Object> propertyToValue, boolean alive, Filter filter) {
    return GetArtifactsWithFilterUtils.getArtifactsWithProperty(dataStorage, version, propertyToValue, alive, createFilterArray(filter));
  }

  @Override
  public Collection<Artifact> getArtifactsWithReference(Artifact artifact, Filter filter) {
    return GetArtifactsWithFilterUtils.getArtifactsWithReference(dataStorage, version, artifact, createFilterArray(filter));
  }

  private Artifact[] createFilterArray(Filter filter) {
    return new Artifact[] { this, filter.getArtifact(), filter.getCollection(), filter.getOwner(), filter.getTool(), filter.getProject() };
  }

  @Override
  public Map<Artifact, Set<Property>> getArtifactAndProperties() {
    Collection<Artifact> artifacts = this.getArtifacts();
    return GetArtifactsWithFilterUtils.createArtifactAndPropertyMap(dataStorage, this.version, artifacts);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactPropertyMap() {
    Collection<Artifact> artifacts = this.getArtifacts();
    return GetArtifactsWithFilterUtils.createArtifactAndStringObjectMap(dataStorage, this.version, artifacts);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(Filter filter) {
    return GetArtifactsWithFilterUtils.getArtifactsAndPropertyMap(dataStorage, version, null, null, true, createFilterArray(filter));
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(String propertyName, Object propertyValue, boolean alive, Filter filter) {
    return GetArtifactsWithFilterUtils.getArtifactsAndPropertyMap(dataStorage, version, propertyName, propertyValue, alive, createFilterArray(filter));
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(Map<String, Object> propertyToValue, boolean alive, Filter filter) {
    return GetArtifactsWithFilterUtils.getArtifactsAndPropertyMap(dataStorage, version, propertyToValue, alive, createFilterArray(filter));
  }

  @Override
  public Collection<Object[]> getArtifactRepresentations() {
    return this.dataStorage.getArtifactsByContainer(version, id);
  }

  @Override
  public Map<Object[], Set<Object[]>> getArtifactPropertyMapRepresentations() {
    Collection<Object[]> artifacts = this.dataStorage.getArtifactsByContainer(version, id);
    return GetArtifactsWithFilterUtils.createArtifactAndPropertyObjectMap(dataStorage, version, artifacts);
  }

}
