package at.jku.sea.cloud.stream.sink;

import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.stream.Context;

public class NavigatorSink<T, U> implements Sink<T> {
  private Sink<U> downStream;
  private TerminalOperation<U> navigator;
  private final String context;
  
  public NavigatorSink(String context, TerminalOperation<U> navigator) {
    this.context = context;
    this.navigator = navigator;
  }
  
  @Override
  public void apply(Context c, T t) {
    downStream.apply(c, navigator.get(c.put(context, t)));
  }
  
  @Override
  public boolean isCancelled() {
    return false;
  }
  
  public void setDownStreamSink(Sink<U> downStream) {
    this.downStream = downStream;
  }
}