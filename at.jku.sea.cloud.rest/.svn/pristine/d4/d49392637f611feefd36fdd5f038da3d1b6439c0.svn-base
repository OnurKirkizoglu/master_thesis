package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoContainer;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "ArtifactContainerSet")
public class PojoArtifactContainerSet extends PojoArtifactChange {

  private PojoContainer _container;

  public PojoArtifactContainerSet() {
  }

  public PojoArtifactContainerSet(long origin, PojoArtifact artifact, PojoContainer _container) {
    super(origin, artifact);
    this._container = _container;
  }

  public PojoContainer getContainer() {
    return _container;
  }

  public void setContainer(PojoContainer _container) {
    this._container = _container;
  }
}
