package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasTool;

public class PredicateHasTool<T> extends RestPredicate<T> {

  private final String context;
  private final Tool t;

  public PredicateHasTool(String context, Tool t) {
    this.context = context;
    this.t = t;
  }

  public String getContext() {
    return context;
  }

  public Tool getT() {
    return t;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateHasTool(context, pojoFactory.createPojo(t));
  }

}
