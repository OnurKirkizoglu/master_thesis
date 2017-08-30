package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasType;

public class PredicateHasType<T> extends RestPredicate<T> {

  private final String context;
  private final Artifact type;

  public PredicateHasType(String context, Artifact type) {
    this.context = context;
    this.type = type;
  }

  public String getContext() {
    return context;
  }

  public Artifact getType() {
    return type;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateHasType(context, pojoFactory.createPojo(type));
  }

}
