package at.jku.sea.cloud.rest.client;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.rest.client.handler.MetaModelHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestMetaModel extends RestCollectionArtifact implements MetaModel {

  private static final long serialVersionUID = 1L;

  private MetaModelHandler handler;

  protected RestMetaModel(long id, long version) {
    super(id, version);
    handler = MetaModelHandler.getInstance();
  }

  @Override
  public void addArtifact(Workspace workspace, Artifact artifact) throws ArtifactDeadException {
    handler.addArtifact(workspace.getId(), id, version, artifact.getId(), artifact.getVersionNumber());
  }

  @Override
  public void addArtifacts(Workspace workspace, Collection<Artifact> artifacts) throws ArtifactDeadException {
    handler.addArtifacts(workspace.getId(), id, version, artifacts);
  }

  @Override
  public void removeArtifact(Workspace workspace, Artifact artifact) throws ArtifactDeadException {
    handler.removeArtifact(workspace.getId(), id, version, artifact.getId(), artifact.getVersionNumber());
  }

  @Override
  public Collection<Artifact> getArtifacts() {
    return handler.getArtifacts(id, version);
  }

  @Override
  public MetaModel getMetaModel() throws ArtifactDeadException {
    return handler.getMetaModel(this.id, this.version);
  }

  @Override
  public Map<Artifact, Set<Property>> getArtifactAndProperties() {
    return handler.getArtifactAndProperties(id, version);
  }

  @Override
  public Collection<Object[]> getArtifactRepresentations() {
    return handler.getArtifactRepresentations(id, version);
  }

  @Override
  public Map<Object[], Set<Object[]>> getArtifactPropertyMapRepresentations() {
    return handler.getArtifactPropertyMapRepresentations(id, version);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactPropertyMap() {
    return handler.getArtifactPropertyMap(id, version);
  }

}
