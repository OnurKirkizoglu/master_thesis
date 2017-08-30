package at.jku.sea.cloud.rest.client.stream.actions;

import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoStreamAction;

public abstract class StreamAction {
  protected String context;

  public StreamAction(String context) {
    this.context = context;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public abstract PojoStreamAction map(PojoFactory pojoFactory);
}
