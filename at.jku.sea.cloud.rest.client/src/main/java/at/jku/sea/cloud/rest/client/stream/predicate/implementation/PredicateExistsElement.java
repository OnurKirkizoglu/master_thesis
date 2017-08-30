package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateExistsElement;

public class PredicateExistsElement<T> extends RestPredicate<T> {

  private final String context;
  private final Object o;

  public PredicateExistsElement(String context, Object o) {
    this.context = context;
    this.o = o;
  }

  public String getContext() {
    return context;
  }

  public Object getO() {
    return o;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateExistsElement(context, pojoFactory.createPojo(o));
  }

}
