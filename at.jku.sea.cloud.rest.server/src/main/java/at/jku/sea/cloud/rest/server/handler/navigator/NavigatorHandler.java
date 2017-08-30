package at.jku.sea.cloud.rest.server.handler.navigator;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.navigator.PojoNavigator;
import at.jku.sea.cloud.rest.server.factory.NavigatorFactory;

public class NavigatorHandler {

  @Autowired
  protected Cloud cloud;
  @Autowired
  protected PojoFactory pojoFactory;
  @Autowired
  protected NavigatorFactory navigatorFactory;

  public PojoObject get(PojoNavigator navigator) {
    @SuppressWarnings("rawtypes")
    TerminalOperation nav = navigatorFactory.map(cloud, navigator);
    return pojoFactory.createPojo(nav.get());
  }
}
