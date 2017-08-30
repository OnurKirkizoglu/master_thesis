package at.jku.sea.cloud.rest.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.server.handler.ArtifactHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@Controller
@RequestMapping("/designspace/artifacts")
public class ArtifactController {

  @Autowired
  ArtifactHandler handler;

  @RequestMapping(value = "/id={id}&v={version}/owner", method = RequestMethod.GET)
  public ResponseEntity<PojoOwner> getOwner(@PathVariable long id, @PathVariable long version) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    PojoOwner result = handler.getOwner(id, version);
    return new ResponseEntity<PojoOwner>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={id}&v={version}/tool", method = RequestMethod.GET)
  public ResponseEntity<PojoTool> getTool(@PathVariable long id, @PathVariable long version) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    PojoTool result = handler.getTool(id, version);
    return new ResponseEntity<PojoTool>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={id}&v={version}/alive", method = RequestMethod.GET)
  public ResponseEntity<Boolean> isAlive(@PathVariable long id, @PathVariable long version) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    Boolean result = handler.isAlive(id, version);
    return new ResponseEntity<Boolean>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={id}&v={version}/type", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifact> getType(@PathVariable long id, @PathVariable long version) throws ArtifactDoesNotExistException, VersionDoesNotExistException, NullPointerException {
    PojoArtifact result = handler.getType(id, version);
    return new ResponseEntity<PojoArtifact>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={id}&v={version}/package", method = RequestMethod.GET)
  public ResponseEntity<PojoPackage> getPackage(@PathVariable long id, @PathVariable long version) throws ArtifactDoesNotExistException, VersionDoesNotExistException, NullPointerException {
    PojoPackage result = handler.getPackage(id, version);
    return new ResponseEntity<PojoPackage>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={id}&v={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifact> getArtifact(@PathVariable long id, @PathVariable long version) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    PojoArtifact result = handler.getArtifact(id, version);
    return new ResponseEntity<PojoArtifact>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={id}&v={version}/representation", method = RequestMethod.GET)
  public ResponseEntity<PojoObject[]> getArtifactRepresentation(@PathVariable long id, @PathVariable long version) {
    PojoObject[] obj = handler.getArtifactRepresentation(version, id);
    return new ResponseEntity<PojoObject[]>(obj, HttpStatus.OK);
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

  @ExceptionHandler(VersionDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleVersionDoesNotExistException() {
    return VersionDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleNullPointerException() {
    return NullPointerException.class.getSimpleName();
  }

}
