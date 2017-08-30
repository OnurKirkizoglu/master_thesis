package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateInv;
import at.jku.sea.cloud.stream.predicate.Predicate;

public class PredicateInv<T> extends RestPredicate<T> {
  private RestPredicate<T> p;

  public PredicateInv(Predicate<T> p) {
    if (p instanceof RestPredicate) {
      this.p = (RestPredicate<T>) p;
    }
  }

  public Predicate<T> getPredicate() {
    return p;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateInv(p.map(pojoFactory));
  }

}
