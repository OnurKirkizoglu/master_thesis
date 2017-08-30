package at.jku.sea.cloud.rest.client.navigator.actions;

import at.jku.sea.cloud.rest.pojo.navigator.PojoAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoPropertyArtifactAccess;

public class PropertyArtifactAccess extends Access {
  public final String path;

  public PropertyArtifactAccess(String path) {
    this.path = path;
  }

  @Override
  public PojoAccess map() {
    return new PojoPropertyArtifactAccess(path);
  }
}
