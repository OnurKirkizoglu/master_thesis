package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoCollectionArtifactOnlyArtifactProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class CollectionArtifactOnlyArtifactProvider extends RestProvider<Artifact> {
  private CollectionArtifact collectionArtifact;

  public CollectionArtifactOnlyArtifactProvider(CollectionArtifact collectionArtifact) {
    this.collectionArtifact = collectionArtifact;
  }

  public CollectionArtifact getCollectionArtifact() {
    return collectionArtifact;
  }

  public void setCollectionArtifact(CollectionArtifact collectionArtifact) {
    this.collectionArtifact = collectionArtifact;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoCollectionArtifactOnlyArtifactProvider(pojoFactory.createPojo(collectionArtifact));
  }

}
