package at.jku.sea.cloud.rest.client.handler.util;

import java.util.List;
import java.util.Objects;

import at.jku.sea.cloud.rest.client.navigator.RestNavigator;
import at.jku.sea.cloud.rest.client.navigator.RestStartNavigator;
import at.jku.sea.cloud.rest.client.navigator.actions.Access;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.navigator.PojoAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoNavigator;

public class Navigator2PojoFactory {

  public static <T> PojoNavigator map(PojoFactory pojoFactory, RestNavigator<T> navigator) {
    List<Access> paths = navigator.getPaths();
    RestStartNavigator<T> start = navigator.getStart();

    Objects.requireNonNull(paths);
    Objects.requireNonNull(start);

    PojoAccess[] access = new PojoAccess[paths.size()];
    for (int i = 0; i < access.length; i++) {
      access[i] = paths.get(i).map();
    }
    PojoNavigator pojoNavigator = start.map(pojoFactory);
    pojoNavigator.setAccess(access);
    return pojoNavigator;
  }
}
