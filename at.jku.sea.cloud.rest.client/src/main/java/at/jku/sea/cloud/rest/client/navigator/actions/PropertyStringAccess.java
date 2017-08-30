package at.jku.sea.cloud.rest.client.navigator.actions;

import at.jku.sea.cloud.rest.pojo.navigator.PojoAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoPropertyStringAccess;

public class PropertyStringAccess extends Access {
  public final String path;

  public PropertyStringAccess(String path) {
    this.path = path;
  }

  @Override
  public PojoAccess map() {
    return new PojoPropertyStringAccess(path);
  }
}
