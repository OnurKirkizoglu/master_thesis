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

import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.server.handler.MapArtifactHandler;

@Controller
@RequestMapping("/designspace/mapartifacts")
public class MapArtifactController {
	
	@Autowired MapArtifactHandler handler;
	
	@RequestMapping(value = "/id={id}&v={version}/size", method = RequestMethod.GET)
	public ResponseEntity<Long> getSize(
			@PathVariable long id,
			@PathVariable long version)
			throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		Long result = handler.size(id, version);
		return new ResponseEntity<Long>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={id}&v={version}/func/containskey", method = RequestMethod.POST)
	public ResponseEntity<Boolean> containsKey(
			@PathVariable long id,
			@PathVariable long version,
			@RequestBody PojoObject key) 
			throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		Boolean result = handler.containsKey(id, version, key);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={id}&v={version}/func/containsvalue", method = RequestMethod.POST)
	public ResponseEntity<Boolean> containsValue(
			@PathVariable long id,
			@PathVariable long version,
			@RequestBody PojoObject value) 
			throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		Boolean result = handler.containsValue(id, version, value);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={id}&v={version}/func/get", method = RequestMethod.POST)
	public ResponseEntity<PojoObject> get(
			@PathVariable long id,
			@PathVariable long version,
			@RequestBody PojoObject key) 
			throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		PojoObject result = handler.get(id, version, key);
		return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={id}&v={version}/keyset", method = RequestMethod.GET)
	public ResponseEntity<PojoObject[]> keySet(
			@PathVariable long id,
			@PathVariable long version) 
			throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		PojoObject[] result = handler.keySet(id, version);
		return new ResponseEntity<PojoObject[]>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={id}&v={version}/values", method = RequestMethod.GET)
	public ResponseEntity<PojoObject[]> values(
			@PathVariable long id,
			@PathVariable long version) 
			throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		PojoObject[] result = handler.values(id, version);
		return new ResponseEntity<PojoObject[]>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={id}&v={version}/entries", method = RequestMethod.GET)
	public ResponseEntity<PojoArtifact[]> entrySet(
			@PathVariable long id,
			@PathVariable long version) 
			throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		PojoArtifact[] result = handler.entrySet(id, version);
		return new ResponseEntity<PojoArtifact[]>(result, HttpStatus.OK);
	}
	
	// ------------------------------------
	// Exception handling
	// ------------------------------------

	@ExceptionHandler(VersionDoesNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleVersionDoesNotExistException() {
		return VersionDoesNotExistException.class.getSimpleName();
	}
	
	@ExceptionHandler(MapArtifactDoesNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleMapArtifactDoesNotExistException() {
		return MapArtifactDoesNotExistException.class.getSimpleName();
	}
	
	@ExceptionHandler(PropertyDeadException.class)
	@ResponseStatus(value = HttpStatus.GONE)
	@ResponseBody
	public String handlePropertyDeadException() {
		return PropertyDeadException.class.getSimpleName();
	}
}
