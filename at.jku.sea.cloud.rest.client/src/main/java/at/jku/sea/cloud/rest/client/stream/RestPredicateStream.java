package at.jku.sea.cloud.rest.client.stream;

import at.jku.sea.cloud.rest.client.stream.predicate.implementation.RestPredicate;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateAllMatch;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateAnyMatch;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateNoneMatch;
import at.jku.sea.cloud.stream.predicate.Predicate;
import at.jku.sea.cloud.stream.predicate.PredicateStream;

public class RestPredicateStream<T> implements PredicateStream<T> {

  private final RestStream<T> stream;

  public RestPredicateStream(RestStream<T> restStream) {
    this.stream = restStream;
  }

  @Override
  public Predicate<T> anyMatch(String context, Predicate<T> p) {
    if (p instanceof RestPredicate) {
      return new PredicateAnyMatch<T>(stream, context, (RestPredicate<T>) p);
    } else {
      throw new IllegalArgumentException("Predicate must be implemented in the REST service.");
    }
  }

  @Override
  public Predicate<T> allMatch(String context, Predicate<T> p) {
    if (p instanceof RestPredicate) {
      return new PredicateAllMatch<T>(stream, context, (RestPredicate<T>) p);
    } else {
      throw new IllegalArgumentException("Predicate must be implemented in the REST service.");
    }
  }

  @Override
  public Predicate<T> noneMatch(String context, Predicate<T> p) {
    if (p instanceof RestPredicate) {
      return new PredicateNoneMatch<T>(stream, context, (RestPredicate<T>) p);
    } else {
      throw new IllegalArgumentException("Predicate must be implemented in the REST service.");
    }
  }
}
