package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateIsPropertyAlive;

public class PredicateIsPropertyAlive<T> extends RestPredicate<T> {

  private final String context;
  private final String property;

  public PredicateIsPropertyAlive(String context, String property) {
    this.context = context;
    this.property = property;
  }

  public String getContext() {
    return context;
  }

  public String getProperty() {
    return property;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateIsPropertyAlive(context, property);
  }

}
