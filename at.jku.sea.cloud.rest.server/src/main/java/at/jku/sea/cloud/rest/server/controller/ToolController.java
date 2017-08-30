package at.jku.sea.cloud.rest.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.server.handler.ToolHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@Controller
@RequestMapping("/designspace/tools")
public class ToolController {

	@Autowired ToolHandler handler;

	@RequestMapping(value = "/id={id}/artifacts", method = RequestMethod.GET)
	public ResponseEntity<PojoArtifact[]> getArtifacts(
			@PathVariable long id)
			throws ToolDoesNotExistException {
		PojoArtifact[] result = handler.getArtifacts(id);
		return new ResponseEntity<PojoArtifact[]>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<PojoTool[]> getTools() {
		PojoTool[] result = handler.getTools();
		return new ResponseEntity<PojoTool[]>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = {"", "/func/create"}, method = RequestMethod.POST)
	public ResponseEntity<PojoTool> createTool(@RequestBody PojoTool pojo) {
		PojoTool tool = handler.createTool(pojo);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Location", "./id=" + tool.getId());
		
		return new ResponseEntity<>(tool, headers, HttpStatus.CREATED);
	}
	
//	@RequestMapping(value = "/id={id}", method = RequestMethod.PUT)
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void updateTool(@PathVariable long id,
//			@RequestBody PojoTool tool) 
//			throws ToolDoesNotExistException {
//		handler.updateTool(tool);
//	}
	
	@RequestMapping(value = "/id={id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteTool(@PathVariable long id) 
			throws ToolDoesNotExistException {
		handler.deleteTool(id);
	}

	@RequestMapping(value = "/id={id}", method = RequestMethod.GET)
	public ResponseEntity<PojoTool> getTool(
			@PathVariable long id)
			throws ToolDoesNotExistException {
		PojoTool result = handler.getTool(id);
		return new ResponseEntity<PojoTool>(result, HttpStatus.OK);
	}

	// ------------------------------------
	// Exception handling
	// ------------------------------------

	@ExceptionHandler(ToolDoesNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleToolDoesNotExistException() {
		return ToolDoesNotExistException.class.getSimpleName();
	}
}
