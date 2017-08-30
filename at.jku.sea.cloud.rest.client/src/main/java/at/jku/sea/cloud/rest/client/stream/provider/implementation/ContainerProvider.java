package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoContainerProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class ContainerProvider extends RestProvider<Artifact> {

  private final Container container;

  public ContainerProvider(Container container) {
    this.container = container;
  }

  public Container getContainer() {
    return container;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoContainerProvider(pojoFactory.createPojo(container));
  }

}
