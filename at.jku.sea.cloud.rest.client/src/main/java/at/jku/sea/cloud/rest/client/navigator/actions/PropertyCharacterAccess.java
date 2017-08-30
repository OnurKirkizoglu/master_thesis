package at.jku.sea.cloud.rest.client.navigator.actions;

import at.jku.sea.cloud.rest.pojo.navigator.PojoAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoPropertyCharacterAccess;

public class PropertyCharacterAccess extends Access {
  public final String path;

  public PropertyCharacterAccess(String path) {
    this.path = path;
  }

  @Override
  public PojoAccess map() {
    return new PojoPropertyCharacterAccess(path);
  }
}
