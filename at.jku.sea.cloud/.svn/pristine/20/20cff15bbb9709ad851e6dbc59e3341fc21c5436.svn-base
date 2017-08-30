package at.jku.sea.cloud.stream.sink;

import at.jku.sea.cloud.stream.predicate.Predicate;

public abstract class ShortCircuitSink<T> implements Sink<T> {
  protected boolean isCancelled = false;
  protected Predicate<T> predicate;
  protected final String context;

  public ShortCircuitSink(String context, Predicate<T> predicate) {
    this.context = context;
    this.predicate = predicate;
  }

  @Override
  public boolean isCancelled() {
    return isCancelled;
  }
}