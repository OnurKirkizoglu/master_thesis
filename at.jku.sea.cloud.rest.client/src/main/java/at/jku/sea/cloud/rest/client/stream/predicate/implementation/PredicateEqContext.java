package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateEqContext;

public class PredicateEqContext<T> extends RestPredicate<T> {

  private final String context1;
  private final String context2;

  public PredicateEqContext(String context1, String context2) {
    this.context1 = context1;
    this.context2 = context2;
  }

  public String getContext1() {
    return context1;
  }

  public String getContext2() {
    return context2;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateEqContext(context1, context2);
  }

}
