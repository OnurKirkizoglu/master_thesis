package at.jku.sea.cloud.rest.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.server.handler.PropertyHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@Controller
@RequestMapping("/designspace/artifacts/id={id}&v={version}/properties")
public class PropertyController {

  @Autowired
  PropertyHandler handler;

  @RequestMapping(value = "/name={name}/owner", method = RequestMethod.GET)
  public ResponseEntity<PojoOwner> getOwner(@PathVariable long id, @PathVariable long version, @PathVariable String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      VersionDoesNotExistException {
    PojoOwner result = handler.getOwner(id, version, name);
    return new ResponseEntity<PojoOwner>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/name={name}/tool", method = RequestMethod.GET)
  public ResponseEntity<PojoTool> getTool(@PathVariable long id, @PathVariable long version, @PathVariable String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      VersionDoesNotExistException {
    PojoTool result = handler.getTool(id, version, name);
    return new ResponseEntity<PojoTool>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/name={name}/alive", method = RequestMethod.GET)
  public ResponseEntity<Boolean> isAlive(@PathVariable long id, @PathVariable long version, @PathVariable String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      VersionDoesNotExistException {
    Boolean result = handler.isAlive(id, version, name);
    return new ResponseEntity<Boolean>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/name={name}/reference", method = RequestMethod.GET)
  public ResponseEntity<Boolean> isReference(@PathVariable long id, @PathVariable long version, @PathVariable String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      VersionDoesNotExistException {
    Boolean result = handler.isReference(id, version, name);
    return new ResponseEntity<Boolean>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/name={name}/value", method = RequestMethod.GET)
  public ResponseEntity<PojoObject> getValue(@PathVariable long id, @PathVariable long version, @PathVariable String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      VersionDoesNotExistException {
    PojoObject result = handler.getValue(id, version, name);
    return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/name={name}/valueornull", method = RequestMethod.GET)
  public ResponseEntity<PojoObject> getValueOrNull(@PathVariable long id, @PathVariable long version, @PathVariable String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      VersionDoesNotExistException {
    PojoObject result = handler.getValueOrNull(id, version, name);
    return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
  }


  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<PojoProperty[]> getProperties(@PathVariable long id, @PathVariable long version, @RequestParam(value = "onlyalive", required = false, defaultValue = "false") Boolean onlyAlive)
      throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    PojoProperty[] result = handler.getProperties(id, version, onlyAlive);
    return new ResponseEntity<PojoProperty[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/func/names", method = RequestMethod.GET)
  public ResponseEntity<String[]> getPropertyNames(@PathVariable long id, @PathVariable long version, @RequestParam(value = "onlyalive", required = false, defaultValue = "false") Boolean onlyAlive)
      throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    String[] result = handler.getPropertyNames(id, version, onlyAlive);
    return new ResponseEntity<String[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/name={name}/func/exists", method = RequestMethod.GET)
  public ResponseEntity<Boolean> exists(@PathVariable long id, @PathVariable long version, @PathVariable String name) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    Boolean result = handler.exists(id, version, name);
    return new ResponseEntity<Boolean>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/name={name}", method = RequestMethod.GET)
  public ResponseEntity<PojoProperty> getProperty(@PathVariable long id, @PathVariable long version, @PathVariable String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      VersionDoesNotExistException {
    PojoProperty result = handler.getProperty(id, version, name);
    return new ResponseEntity<PojoProperty>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/name={name}/representation", method = RequestMethod.GET)
  public ResponseEntity<PojoObject[]> getPropertyRepresentation(@PathVariable long id, @PathVariable long version, @PathVariable String name) {
    PojoObject[] obj = handler.getPropertyRepresentation(id, version, name);
    return new ResponseEntity<PojoObject[]>(obj, HttpStatus.OK);
  }

  @RequestMapping(value = "/mappings", method = RequestMethod.GET)
  public ResponseEntity<PojoPropertyFilter[]> getPropertyMappings(@PathVariable long id, @PathVariable long version,
      @RequestParam(value = "alive", required = false, defaultValue = "true") Boolean alive) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    PojoPropertyFilter[] result = handler.getPropertyMappings(id, version, alive);
    return new ResponseEntity<PojoPropertyFilter[]>(result, HttpStatus.OK);
  }

  // ------------------------------------
  // Exception handling
  // ------------------------------------

  @ExceptionHandler(ArtifactDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleArtifactDoesNotExistException() {
    return ArtifactDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(PropertyDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handlePropertyDoesNotExistException() {
    return PropertyDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(VersionDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleVersionDoesNotExistException() {
    return VersionDoesNotExistException.class.getSimpleName();
  }
}
