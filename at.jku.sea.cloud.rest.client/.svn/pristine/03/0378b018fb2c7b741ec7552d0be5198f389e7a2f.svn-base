package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoResourceProvider;

public class ResourceProvider extends RestProvider<Artifact> {
  private final Resource resource;

  public ResourceProvider(Resource resource) {
    this.resource = resource;
  }

  public Resource getResource() {
    return resource;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoResourceProvider(pojoFactory.createPojo(resource));
  }

}
