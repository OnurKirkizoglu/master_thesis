package at.jku.sea.cloud.rest.client.navigator;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.navigator.PojoArtifactNavigator;
import at.jku.sea.cloud.rest.pojo.navigator.PojoNavigator;

public class RestStartArtifact<T extends Artifact> extends RestStartNavigator<T> {
  public final Artifact artifact;

  public RestStartArtifact(Artifact artifact) {
    this.artifact = artifact;
  }

  @Override
  public PojoNavigator map(PojoFactory pojoFactory) {
    PojoArtifactNavigator result = new PojoArtifactNavigator();
    result.setStartElement(pojoFactory.createPojo(artifact));
    return result;
  }
}
