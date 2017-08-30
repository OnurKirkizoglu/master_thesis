package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateContainsValue;

public class PredicateContainsValue<T> extends RestPredicate<T> {

  private final String context;
  private final Object value;

  public PredicateContainsValue(String context, Object value) {
    this.context = context;
    this.value = value;
  }

  public String getContext() {
    return context;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateContainsValue(context, pojoFactory.createPojo(value));
  }

}
