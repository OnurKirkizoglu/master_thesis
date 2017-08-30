package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasVersion;

public class PredicateHasVersion<T> extends RestPredicate<T> {
  private final String context;
  private final Long version;

  public PredicateHasVersion(String context, long version) {
    this.context = context;
    this.version = version;
  }

  public String getContext() {
    return context;
  }

  public Long getVersion() {
    return version;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateHasVersion(context, version);
  }

}
