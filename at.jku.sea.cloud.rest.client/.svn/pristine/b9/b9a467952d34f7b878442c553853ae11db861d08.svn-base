package at.jku.sea.cloud.rest.client.stream.actions;

import at.jku.sea.cloud.rest.client.handler.util.Navigator2PojoFactory;
import at.jku.sea.cloud.rest.client.navigator.RestNavigator;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoMapAction;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoStreamAction;

public class MapAction<T, U> extends StreamAction {

  private final RestNavigator<U> navigator;

  public MapAction(String context, RestNavigator<U> navigator) {
    super(context);
    this.navigator = navigator;
  }

  public RestNavigator<U> getNavigator() {
    return navigator;
  }

  @Override
  public PojoStreamAction map(PojoFactory pojoFactory) {
    return new PojoMapAction(context, Navigator2PojoFactory.map(pojoFactory, navigator));
  }

}
