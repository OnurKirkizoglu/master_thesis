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
import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.server.handler.MapEntryHandler;

@Controller
@RequestMapping("/designspace/mapartifacts")
public class MapEntryController {

	@Autowired MapEntryHandler handler;

	@RequestMapping(value = "/id={id}&v={version}/entries/id={eid}&v={eversion}/key", method = RequestMethod.GET)
	public ResponseEntity<PojoObject> getKey(
			@PathVariable long id,
			@PathVariable long version,
			@PathVariable long eid,
			@PathVariable long eversion)
			throws ArtifactDoesNotExistException, VersionDoesNotExistException, MapArtifactDoesNotExistException, PropertyDoesNotExistException {
		PojoObject result = handler.getKey(id, version, eid, eversion);
		return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/entries/id={eid}&v={eversion}/value", method = RequestMethod.GET)
	public ResponseEntity<PojoObject> getValue(
			@PathVariable long id,
			@PathVariable long version,
			@PathVariable long eid,
			@PathVariable long eversion)
			throws ArtifactDoesNotExistException, VersionDoesNotExistException, MapArtifactDoesNotExistException, PropertyDoesNotExistException {
		PojoObject result = handler.getValue(id, version, eid, eversion);
		return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
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
	
	@ExceptionHandler(ArtifactDoesNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleArtifactDoesNotExistException() {
		return ArtifactDoesNotExistException.class.getSimpleName();
	}
	
	@ExceptionHandler(MapArtifactDoesNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleMapArtifactDoesNotExistException() {
		return MapArtifactDoesNotExistException.class.getSimpleName();
	}
	
	@ExceptionHandler(PropertyDoesNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handlePropertyDoesNotExistException() {
		return PropertyDoesNotExistException.class.getSimpleName();
	}
}
