package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateEq;

public class PredicateEq<T> extends RestPredicate<T> {
  private final String context;
  private final T t;

  public PredicateEq(String context, T t) {
    this.context = context;
    this.t = t;
  }

  public String getContext() {
    return context;
  }

  public T getT() {
    return t;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateEq(context, pojoFactory.createPojo(t));
  }
}
