package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasPropertyValue;

public class PredicateHasPropertyValue<T> extends RestPredicate<T> {

  private final String context;
  private final String name;
  private final Object value;

  public PredicateHasPropertyValue(String context, String name, Object value) {
    this.context = context;
    this.name = name;
    this.value = value;
  }

  public String getContext() {
    return context;
  }

  public String getName() {
    return name;
  }

  public Object value() {
    return value;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateHasPropertyValue(context, name, pojoFactory.createPojo(value));
  }

}
