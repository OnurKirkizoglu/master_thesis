package at.jku.sea.cloud.rest.client.navigator.actions;

import at.jku.sea.cloud.rest.pojo.navigator.PojoAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoToolAccess;

public class ToolAccess extends Access {

  @Override
  public PojoAccess map() {
    return new PojoToolAccess();
  }
}
