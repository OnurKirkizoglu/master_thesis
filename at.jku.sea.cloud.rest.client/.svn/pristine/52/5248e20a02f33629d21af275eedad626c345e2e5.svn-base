package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoMetaModelProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class MetaModelProvider extends RestProvider<Artifact> {
  private final MetaModel metaModel;

  public MetaModelProvider(MetaModel metaModel) {
    this.metaModel = metaModel;
  }

  public MetaModel getMetaModel() {
    return metaModel;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoMetaModelProvider(pojoFactory.createPojo(metaModel));
  }

}
