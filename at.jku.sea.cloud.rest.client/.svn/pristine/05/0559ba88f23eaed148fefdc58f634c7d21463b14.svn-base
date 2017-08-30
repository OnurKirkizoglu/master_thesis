package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateOr;

public class PredicateOr<T> extends RestPredicate<T> {
  private RestPredicate<T> predicate1;
  private RestPredicate<T> predicate2;

  public PredicateOr(RestPredicate<T> predicate1, RestPredicate<T> predicate2) {
    this.predicate1 = predicate1;
    this.predicate2 = predicate2;
  }

  public RestPredicate<T> getPredicate1() {
    return predicate1;
  }

  public RestPredicate<T> getPredicate2() {
    return predicate2;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateOr(predicate1.map(pojoFactory), predicate2.map(pojoFactory));
  }

}
