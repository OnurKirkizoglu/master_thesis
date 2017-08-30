package at.jku.sea.cloud.rest.client.navigator.actions;

import at.jku.sea.cloud.rest.pojo.navigator.PojoAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoPropertyNumberAccess;

public class PropertyNumberAccess extends Access {
  public final String path;

  public PropertyNumberAccess(String path) {
    this.path = path;
  }

  @Override
  public PojoAccess map() {
    return new PojoPropertyNumberAccess(path);
  }
}
