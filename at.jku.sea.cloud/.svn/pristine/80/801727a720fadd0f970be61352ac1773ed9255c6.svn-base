package at.jku.sea.cloud.stream.sink;

import java.util.ArrayList;
import java.util.List;

import at.jku.sea.cloud.stream.Context;

public class ListSink<T> implements TerminalSink<T, List<T>> {

  private List<T> result = new ArrayList<>();

  @Override
  public void apply(Context c, T t) {
    result.add(t);
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public List<T> get() {
    return result;
  }
}