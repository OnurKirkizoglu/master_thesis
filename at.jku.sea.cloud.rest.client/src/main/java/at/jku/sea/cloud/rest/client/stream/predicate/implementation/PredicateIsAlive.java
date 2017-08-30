package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateIsAlive;

public class PredicateIsAlive<T> extends RestPredicate<T> {

  private final String context;

  public PredicateIsAlive(String context) {
    this.context = context;
  }

  public String getContext() {
    return context;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateIsAlive(context);
  }

}
