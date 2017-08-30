package at.jku.sea.cloud.rest.client.handler.navigator;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.rest.client.handler.AbstractHandler;
import at.jku.sea.cloud.rest.client.handler.util.Navigator2PojoFactory;
import at.jku.sea.cloud.rest.client.navigator.RestNavigator;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.navigator.PojoNavigator;

public class NavigatorHandler extends AbstractHandler {

  public <T> T get(RestNavigator<T> navigator) {
    PojoNavigator pojoNavigator = Navigator2PojoFactory.map(pojoFactory, navigator);
    PojoObject entity = null;
    try {
      entity = template.postForEntity(NAVIGATOR_ADDRESS + "/get", pojoNavigator, PojoObject.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
        throw new PropertyDoesNotExistException();
      }
      throw e;
    }
    @SuppressWarnings("unchecked")
    T result = (T) restFactory.createRest(entity);
    return result;
  }

}
