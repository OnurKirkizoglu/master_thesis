package at.jku.sea.cloud.stream.sink;

import at.jku.sea.cloud.stream.Context;

public class CountSink<T> implements TerminalSink<T, Long> {
  private long count = 0;

  @Override
  public void apply(Context c, T t) {
    count++;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public Long get() {
    return count;
  }
}