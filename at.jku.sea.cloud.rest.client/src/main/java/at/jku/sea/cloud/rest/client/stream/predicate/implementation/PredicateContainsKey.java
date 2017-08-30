package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateContainsKey;

public class PredicateContainsKey<T> extends RestPredicate<T> {

  private final String context;
  private final Object key;

  public PredicateContainsKey(String context, Object key) {
    this.context = context;
    this.key = key;
  }

  public String getContext() {
    return context;
  }

  public Object getKey() {
    return key;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateContainsKey(context, pojoFactory.createPojo(key));
  }

}
