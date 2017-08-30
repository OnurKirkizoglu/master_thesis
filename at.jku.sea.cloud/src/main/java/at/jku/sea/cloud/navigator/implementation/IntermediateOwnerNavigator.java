package at.jku.sea.cloud.navigator.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.stream.Context;

public class IntermediateOwnerNavigator extends AbstractNavigator<Owner> {
  
  private TerminalOperation<? extends Artifact> upstream;
  
  public IntermediateOwnerNavigator(TerminalOperation<? extends Artifact> upstream) {
    this.upstream = upstream;
  }
  
  @Override
  public Owner get() {
    return upstream.get().getOwner();
  }
  
  @Override
  public Owner get(Context context) {
    return upstream.get(context).getOwner();
  }
}
