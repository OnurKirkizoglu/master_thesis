package at.jku.sea.cloud.stream.sink;

import at.jku.sea.cloud.stream.Context;

public interface Sink<T> {
  void apply(Context c, T t);

  boolean isCancelled();
}