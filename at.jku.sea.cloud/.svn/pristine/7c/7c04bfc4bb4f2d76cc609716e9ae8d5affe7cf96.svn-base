package at.jku.sea.cloud.stream.sink;

import java.util.NoSuchElementException;

import at.jku.sea.cloud.stream.Context;
import at.jku.sea.cloud.stream.predicate.Predicate;

public class FindSink<T> extends ShortCircuitSink<T>implements TerminalSink<T, T> {
  private T found;
  
  public FindSink(String context, Predicate<T> predicate) {
    super(context, predicate);
  }
  
  @Override
  public void apply(Context c, T t) {
    if (predicate.test(c.put(context, t))) {
      found = t;
      isCancelled = true;
    }
  }
  
  @Override
  public T get() {
    if (!isCancelled) {
      throw new NoSuchElementException();
    }
    return found;
  }
}