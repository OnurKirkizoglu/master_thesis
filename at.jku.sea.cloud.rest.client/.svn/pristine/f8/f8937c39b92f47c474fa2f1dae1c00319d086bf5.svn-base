package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasId;

public class PredicateHasId<T> extends RestPredicate<T> {

  private final String context;
  private final Long id;

  public PredicateHasId(String context, long id) {
    this.context = context;
    this.id = id;
  }

  public String getContext() {
    return context;
  }

  public Long getId() {
    return id;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateHasId(context, id);
  }
}