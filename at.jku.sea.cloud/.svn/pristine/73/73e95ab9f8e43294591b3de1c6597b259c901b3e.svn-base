package at.jku.sea.cloud.stream.sink;

import at.jku.sea.cloud.stream.Context;
import at.jku.sea.cloud.stream.predicate.Predicate;

public class ExistSink<T> extends ShortCircuitSink<T>implements TerminalSink<T, Boolean> {
  private boolean result = false;
  
  public ExistSink(String context, Predicate<T> predicate) {
    super(context, predicate);
  }
  
  @Override
  public void apply(Context c, T t) {
    if (predicate.test(c.put(context, t))) {
      result = true;
      isCancelled = true;
    }
  }
  
  @Override
  public Boolean get() {
    return result;
  }
}