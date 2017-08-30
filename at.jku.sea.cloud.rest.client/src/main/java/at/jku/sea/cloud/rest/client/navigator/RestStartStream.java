package at.jku.sea.cloud.rest.client.navigator;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.navigator.PojoNavigator;
import at.jku.sea.cloud.rest.pojo.navigator.PojoStreamNavigator;

public class RestStartStream extends RestStartNavigator<Artifact> {
  public final String context;

  public RestStartStream(String context) {
    this.context = context;
  }

  @Override
  public PojoNavigator map(PojoFactory pojoFactory) {
    PojoStreamNavigator result = new PojoStreamNavigator();
    result.setContext(context);
    return result;
  }

}
