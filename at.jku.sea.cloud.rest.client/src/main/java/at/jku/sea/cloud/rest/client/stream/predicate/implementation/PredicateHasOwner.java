package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasOwner;

public class PredicateHasOwner<T> extends RestPredicate<T> {

  private final Owner o;
  private final String context;

  public PredicateHasOwner(String context, Owner o) {
    this.context = context;
    this.o = o;
  }

  public Owner getOwner() {
    return o;
  }

  public String getContext() {
    return context;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateHasOwner(context, pojoFactory.createPojo(o));
  }

}
