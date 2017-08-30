package at.jku.sea.cloud.navigator.implementation;

import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.stream.Context;

public class StreamStartNavigator<T extends Artifact> extends AbstractNavigator<T> {
  private Context.Path path;
  
  public StreamStartNavigator(Context.Path path) {
    Objects.requireNonNull(path);
    this.path = path;
  }
  
  @Override
  public T get() {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public T get(Context context) {
    return context.get(path.get());
  }
}