package at.jku.sea.cloud.rest.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.jku.sea.cloud.rest.server.handler.ResourceHandler;

@Controller
@RequestMapping("/designspace/resources")
public class ResourceController {
	@Autowired
	ResourceHandler handler;

	@RequestMapping(value = "/id={id}&v={version}/uri", method = RequestMethod.GET)
	public ResponseEntity<String> getFullQualifiedName(@PathVariable long id, @PathVariable long version) {
		String result = handler.getFullQualifiedName(id, version);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}
