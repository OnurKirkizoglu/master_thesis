package at.jku.sea.cloud.rest.server.controller.navigator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.navigator.PojoNavigator;
import at.jku.sea.cloud.rest.server.handler.navigator.NavigatorHandler;

@Controller
@RequestMapping("/designspace/navigators")
public class NavigatorController {

  @Autowired
  private NavigatorHandler handler;

  @RequestMapping(value = "/get", method = RequestMethod.POST)
  public ResponseEntity<PojoObject> get(@RequestBody(required = true) PojoNavigator navigator)
      throws PropertyDoesNotExistException {
    PojoObject result = handler.get(navigator);
    return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
  }

  // ------------------------------------
  // Exception handling
  // ------------------------------------

  @ExceptionHandler(PropertyDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handlePropertyDoesNotExistException() {
    return PropertyDoesNotExistException.class.getSimpleName();
  }
}
