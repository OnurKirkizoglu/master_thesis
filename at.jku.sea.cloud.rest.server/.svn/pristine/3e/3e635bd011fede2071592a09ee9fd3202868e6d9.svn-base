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

import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.server.handler.VersionHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@Controller
@RequestMapping("/designspace/versions")
public class VersionController {

	@Autowired VersionHandler handler;

	@RequestMapping(value = "/v={version}/identifier", method = RequestMethod.GET)
	public ResponseEntity<String> getIdentifier(
			@PathVariable long version)
			throws VersionDoesNotExistException {
		String result = handler.getIdentifier(version);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/v={version}/owner", method = RequestMethod.GET)
	public ResponseEntity<PojoOwner> getOwner(
			@PathVariable long version)
			throws VersionDoesNotExistException {
		PojoOwner result = handler.getOwner(version);
		return new ResponseEntity<PojoOwner>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/v={version}/tool", method = RequestMethod.GET)
	public ResponseEntity<PojoTool> getTool(
			@PathVariable long version)
			throws VersionDoesNotExistException {
		PojoTool result = handler.getTool(version);
		return new ResponseEntity<PojoTool>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/v={version}", method = RequestMethod.GET)
	public ResponseEntity<PojoVersion> getVersion(
			@PathVariable long version)
			throws VersionDoesNotExistException {
		PojoVersion result = handler.getVersion(version);
		return new ResponseEntity<PojoVersion>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/head", method = RequestMethod.GET)
	public ResponseEntity<Long> getHeadVersion()
			throws VersionDoesNotExistException {
		Long result = handler.getHeadVersion();
		return new ResponseEntity<Long>(result, HttpStatus.OK);
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
}
