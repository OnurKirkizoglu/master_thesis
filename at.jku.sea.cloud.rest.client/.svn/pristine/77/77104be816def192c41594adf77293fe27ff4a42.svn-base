package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoCollectionArtifactProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class CollectionArtifactProvider extends RestProvider<Object> {
  private CollectionArtifact collectionArtifact;

  public CollectionArtifactProvider(CollectionArtifact collectionArtifact) {
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
    return new PojoCollectionArtifactProvider(pojoFactory.createPojo(collectionArtifact));
  }

}
