package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.stream.Context;
import at.jku.sea.cloud.stream.predicate.Predicate;

public abstract class RestPredicate<T> implements Predicate<T> {

  @Override
  public boolean test(Context c) {
    throw new UnsupportedOperationException("Can only used in a Stream.");
  }

  @Override
  public Predicate<T> and(Predicate<T> p) {
    if (!(p instanceof RestPredicate)) {
      throw new IllegalArgumentException("Can only be a RestPredicate");
    }
    return new PredicateAnd<>(this, (RestPredicate<T>) p);
  }

  @Override
  public Predicate<T> or(Predicate<T> p) {
    if (!(p instanceof RestPredicate)) {
      throw new IllegalArgumentException("Can only be a RestPredicate");
    }
    return new PredicateOr<>(this, (RestPredicate<T>) p);
  }

  public abstract PojoPredicate map(PojoFactory pojoFactory);
}
