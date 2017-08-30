package at.jku.sea.cloud.rest.server.controller.stream;

import java.util.NoSuchElementException;

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
import at.jku.sea.cloud.rest.pojo.stream.PojoMatchStream;
import at.jku.sea.cloud.rest.pojo.stream.PojoStream;
import at.jku.sea.cloud.rest.server.handler.stream.StreamHandler;

@Controller
@RequestMapping("/designspace/streams")
public class StreamController {
  @Autowired
  StreamHandler handler;
  
  @RequestMapping(value = "/find", method = RequestMethod.POST)
  public ResponseEntity<PojoObject> find(@RequestBody(required = true) PojoMatchStream pojoStream)
      throws NoSuchElementException, PropertyDoesNotExistException, IllegalArgumentException {
    PojoObject result = handler.find(pojoStream);
    return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/anymatch", method = RequestMethod.POST)
  public ResponseEntity<Boolean> anyMatch(@RequestBody(required = true) PojoMatchStream pojoStream)
      throws PropertyDoesNotExistException, IllegalArgumentException {
    Boolean anyMatch = handler.anyMatch(pojoStream);
    return new ResponseEntity<Boolean>(anyMatch, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/allmatch", method = RequestMethod.POST)
  public ResponseEntity<Boolean> allMatch(@RequestBody(required = true) PojoMatchStream pojoStream)
      throws PropertyDoesNotExistException, IllegalArgumentException {
    Boolean allMatch = handler.allMatch(pojoStream);
    return new ResponseEntity<Boolean>(allMatch, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/nonematch", method = RequestMethod.POST)
  public ResponseEntity<Boolean> noneMatch(@RequestBody(required = true) PojoMatchStream pojoStream)
      throws PropertyDoesNotExistException, IllegalArgumentException {
    Boolean noneMatch = handler.noneMatch(pojoStream);
    return new ResponseEntity<Boolean>(noneMatch, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/tolist", method = RequestMethod.POST)
  public ResponseEntity<PojoObject[]> toList(@RequestBody(required = true) PojoStream pojoStream)
      throws PropertyDoesNotExistException, IllegalArgumentException {
    PojoObject[] list = handler.toList(pojoStream);
    return new ResponseEntity<PojoObject[]>(list, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/count", method = RequestMethod.POST)
  public ResponseEntity<Long> count(@RequestBody(required = true) PojoStream pojoStream)
      throws PropertyDoesNotExistException, IllegalArgumentException {
    Long count = handler.count(pojoStream);
    return new ResponseEntity<Long>(count, HttpStatus.OK);
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
  
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleNoSuchElementException() {
    return NoSuchElementException.class.getSimpleName();
  }
  
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleIllegalArgumentException() {
    return IllegalArgumentException.class.getSimpleName();
  }
  
}
