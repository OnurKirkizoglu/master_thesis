package at.jku.sea.cloud.rest.client.stream.provider;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;
import at.jku.sea.cloud.stream.provider.Provider;

public abstract class RestProvider<T> implements Provider<T> {

  @Override
  public Iterable<T> get() {
    throw new UnsupportedOperationException("Can only used in a Stream.");
  }

  public abstract PojoProvider map(PojoFactory pojoFactory);
}
