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

import at.jku.sea.cloud.exceptions.MetaModelDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndProperties;
import at.jku.sea.cloud.rest.pojo.PojoArtifactRepAndPropertiesRep;
import at.jku.sea.cloud.rest.pojo.PojoMetaModel;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.server.handler.MetaModelHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@Controller
@RequestMapping("/designspace/metamodels")
public class MetaModelController {

  @Autowired
  MetaModelHandler handler;

  @RequestMapping(value = "/id={id}&v={version}/artifacts", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifact[]> getArtifacts(@PathVariable long id, @PathVariable long version) throws MetaModelDoesNotExistException {
    PojoArtifact[] result = handler.getArtifacts(id, version);
    return new ResponseEntity<PojoArtifact[]>(result, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/id={id}&v={version}/metamodel", method = RequestMethod.GET)
  public ResponseEntity<PojoMetaModel> getMetaModel(@PathVariable long id, @PathVariable long version) throws MetaModelDoesNotExistException {
    PojoMetaModel result = handler.getMetaModel(id,version);
    return new ResponseEntity<PojoMetaModel>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={id}&v={version}/artifactandproperties", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifactAndProperties[]> getArtifactAndProperties(@PathVariable long id, @PathVariable long version) throws MetaModelDoesNotExistException {
    PojoArtifactAndProperties[] result = handler.getArtifactAndProperties(id, version);
    return new ResponseEntity<PojoArtifactAndProperties[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={id}&v={version}/artifactreps", method = RequestMethod.GET)
  public ResponseEntity<PojoObject[][]> getArtifactRepresentations(@PathVariable long id, @PathVariable long version) throws MetaModelDoesNotExistException {
    PojoObject[][] result = handler.getArtifactRepresentations(id, version);
    return new ResponseEntity<PojoObject[][]>(result, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/id={id}&v={version}/artifactpropertymap", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifactAndProperties[]> getArtifactPropertyMap(@PathVariable long id, @PathVariable long version) throws MetaModelDoesNotExistException {
    PojoArtifactAndProperties[] result = handler.getArtifactPropertyMap(id, version);
    return new ResponseEntity<PojoArtifactAndProperties[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={id}&v={version}/artifactandpropertymapreps", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifactRepAndPropertiesRep[]> getArtifactPropertyMapRepresentations(@PathVariable long id, @PathVariable long version) throws MetaModelDoesNotExistException {
    PojoArtifactRepAndPropertiesRep[] result = handler.getArtifactPropertyMapRepresentations(id, version);
    return new ResponseEntity<PojoArtifactRepAndPropertiesRep[]>(result, HttpStatus.OK);
  }

  // ------------------------------------
  // Exception handling
  // ------------------------------------

  @ExceptionHandler(MetaModelDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleMetaModelDoesNotExistException() {
    return MetaModelDoesNotExistException.class.getSimpleName();
  }
}
