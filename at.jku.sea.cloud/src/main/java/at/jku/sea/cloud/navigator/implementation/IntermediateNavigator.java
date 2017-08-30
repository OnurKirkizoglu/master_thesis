package at.jku.sea.cloud.navigator.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.stream.Context;

class IntermediateNavigator<T extends Artifact> extends AbstractNavigator<T> {
  
  private TerminalOperation<? extends Artifact> upstream;
  private String path;
  
  public IntermediateNavigator(TerminalOperation<? extends Artifact> upstream, String path) {
    this.upstream = upstream;
    this.path = path;
  }
  
  @Override
  public T get() {
    @SuppressWarnings("unchecked")
    T val = (T) upstream.get().getPropertyValue(path);
    return val;
  }
  
  @Override
  public T get(Context context) {
    @SuppressWarnings("unchecked")
    T val = (T) upstream.get(context).getPropertyValue(path);
    return val;
  }
  
}