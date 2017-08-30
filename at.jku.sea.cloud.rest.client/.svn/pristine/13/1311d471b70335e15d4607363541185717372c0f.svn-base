package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoOwnerProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class OwnerProvider extends RestProvider<Artifact> {
  private final Owner owner;

  public OwnerProvider(Owner owner) {
    this.owner = owner;
  }

  public Owner getOwner() {
    return owner;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoOwnerProvider(pojoFactory.createPojo(owner));
  }

}
