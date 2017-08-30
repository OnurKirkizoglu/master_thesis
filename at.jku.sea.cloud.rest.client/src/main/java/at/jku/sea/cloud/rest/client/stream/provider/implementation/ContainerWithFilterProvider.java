package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.Container.Filter;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoContainerWithFilterProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class ContainerWithFilterProvider extends RestProvider<Artifact> {

  private final Container container;
  private final Filter filter;

  public ContainerWithFilterProvider(Container container, Filter filter) {
    this.container = container;
    this.filter = filter;
  }

  public Container getContainer() {
    return container;
  }

  public Filter getFilter() {
    return filter;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoContainerWithFilterProvider(pojoFactory.createPojo(container), pojoFactory.convertFilter(filter));
  }

}
