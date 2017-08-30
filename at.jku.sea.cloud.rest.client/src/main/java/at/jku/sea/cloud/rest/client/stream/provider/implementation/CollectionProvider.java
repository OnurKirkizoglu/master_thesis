package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import java.util.Collection;

import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoCollectionProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class CollectionProvider<T> extends RestProvider<T> {

  private final Collection<T> collection;

  public CollectionProvider(Collection<T> collection) {
    this.collection = collection;
  }

  public Collection<T> getCollection() {
    return collection;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoCollectionProvider(pojoFactory.createPojoArray(collection.toArray()));
  }

}
