package at.jku.sea.cloud.navigator.implementation;

import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.stream.Context;

class StartNavigator<T extends Artifact> extends AbstractNavigator<T> {
  private T element;
  
  public StartNavigator(T element) {
    Objects.requireNonNull(element);
    this.element = element;
  }
  
  public StartNavigator() {
  }
  
  @Override
  public T get() {
    Objects.requireNonNull(element);
    return element;
  }
  
  @Override
  public T get(Context context) {
    throw new UnsupportedOperationException();
  }
}