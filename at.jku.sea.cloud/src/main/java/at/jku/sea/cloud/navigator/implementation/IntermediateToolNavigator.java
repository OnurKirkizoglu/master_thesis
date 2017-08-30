package at.jku.sea.cloud.navigator.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.stream.Context;

public class IntermediateToolNavigator extends AbstractNavigator<Tool> {
  
  private TerminalOperation<? extends Artifact> upstream;
  
  public IntermediateToolNavigator(TerminalOperation<? extends Artifact> upstream) {
    this.upstream = upstream;
  }
  
  @Override
  public Tool get() {
    return upstream.get().getTool();
  }
  
  @Override
  public Tool get(Context context) {
    return upstream.get(context).getTool();
  }
}
