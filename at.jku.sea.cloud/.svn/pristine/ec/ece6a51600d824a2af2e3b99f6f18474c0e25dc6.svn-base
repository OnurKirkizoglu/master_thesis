package at.jku.sea.cloud.stream.sink;

import at.jku.sea.cloud.stream.Context;
import at.jku.sea.cloud.stream.predicate.Predicate;

public class PredicateSink<T> extends ChainedSink<T> {
  private Predicate<T> predicate;
  private final String context;
  
  public PredicateSink(String context, Predicate<T> predicate) {
    this.context = context;
    this.predicate = predicate;
  }
  
  @Override
  public void apply(Context c, T t) {
    if (predicate.test(c.put(context, t))) {
      super.apply(c, t);
    }
  }
}