package at.jku.sea.cloud.rest.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.server.handler.CollectionArtifactHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@Controller
@RequestMapping("/designspace/collectionartifacts")
public class CollectionArtifactController {

	@Autowired CollectionArtifactHandler handler;

	@RequestMapping(value = "/id={id}&v={version}/containsonlyartifacts", method = RequestMethod.GET)
	public ResponseEntity<Boolean> containsOnlyArtifacts(
			@PathVariable long id,
			@PathVariable long version)
			throws CollectionArtifactDoesNotExistException {
		Boolean result = handler.containsOnlyArtifacts(id, version);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/elements", method = RequestMethod.GET)
	public ResponseEntity<PojoObject[]> getElements(
			@PathVariable long id,
			@PathVariable long version)
			throws CollectionArtifactDoesNotExistException {
		PojoObject[] result = handler.getElements(id, version);
		return new ResponseEntity<PojoObject[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/elements/pos={index}", method = RequestMethod.GET)
	public ResponseEntity<PojoObject> getElementAt(
			@PathVariable long id,
			@PathVariable long version,
			@PathVariable long index)
			throws CollectionArtifactDoesNotExistException {
		PojoObject result = handler.getElementAt(id, version, index);
		return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/elements/func/exists", method = RequestMethod.POST)
	public ResponseEntity<Boolean> existsElement(
			@PathVariable long id,
			@PathVariable long version,
			@RequestBody(required = true) PojoObject element)
			throws CollectionArtifactDoesNotExistException {
		Boolean result = handler.existsElement(id, version, element);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/size", method = RequestMethod.GET)
	public ResponseEntity<Long> getSize(
			@PathVariable long id,
			@PathVariable long version)
			throws CollectionArtifactDoesNotExistException {
		Long result = handler.size(id, version);
		return new ResponseEntity<Long>(result, HttpStatus.OK);
	}

	// ------------------------------------
	// Exception handling
	// ------------------------------------

	@ExceptionHandler(CollectionArtifactDoesNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleCollectionArtifactDoesNotExistException() {
		return CollectionArtifactDoesNotExistException.class.getSimpleName();
	}
}
