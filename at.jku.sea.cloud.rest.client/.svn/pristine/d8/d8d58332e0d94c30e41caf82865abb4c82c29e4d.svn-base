package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.client.stream.RestStream;

public abstract class PredicateStreamPredicate<T> extends RestPredicate<T> {

  private final RestStream<T> stream;
  private final String context;
  private final RestPredicate<T> predicate;

  public PredicateStreamPredicate(RestStream<T> stream, String context, RestPredicate<T> predicate) {
    this.stream = stream;
    this.context = context;
    this.predicate = predicate;
  }

  public RestStream<T> getStream() {
    return stream;
  }

  public String getContext() {
    return context;
  }

  public RestPredicate<T> getPredicate() {
    return predicate;
  }
}
