package at.jku.sea.cloud.stream.sink;

import at.jku.sea.cloud.stream.Context;

public class ChainedSink<T> implements Sink<T> {
  private Sink<T> downStream;

  @Override
  public void apply(Context c, T t) {
    downStream.apply(c, t);
  }

  public void setDownStreamSink(Sink<T> downStream) {
    this.downStream = downStream;
  }

  @Override
  public boolean isCancelled() {
    return downStream.isCancelled();
  }
}