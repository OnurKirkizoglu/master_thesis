package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateInContainer;

public class PredicateInContainer<T> extends RestPredicate<T> {

  private final String context;
  private final Container p;

  public PredicateInContainer(String context, Container p) {
    this.context = context;
    this.p = p;
  }

  public String getContext() {
    return context;
  }

  public Container getPackage() {
    return p;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateInContainer(context, pojoFactory.createPojo(p));
  }

}
