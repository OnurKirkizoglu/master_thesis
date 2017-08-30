package at.jku.sea.cloud.rest.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndFilters;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndProperties;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoArtifactRepAndPropertiesRep;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.server.handler.ContainerHandler;

@Controller
@RequestMapping("/designspace/containers")
public class ContainerController {
	@Autowired
	ContainerHandler handler;

	@RequestMapping(value = "/id={id}&v={version}/func/contains", method = RequestMethod.POST)
	public ResponseEntity<Boolean> containsArtifact(@PathVariable long id, @PathVariable long version,
			@RequestBody(required = true) PojoArtifact artifact) {
		Boolean art = handler.containsArtifact(id, version, artifact);
		return new ResponseEntity<Boolean>(art, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/artifacts", method = RequestMethod.GET)
	public ResponseEntity<PojoArtifact[]> getArtifacts(@PathVariable long id, @PathVariable long version) {
		PojoArtifact[] art = handler.getArtifacts(id, version);
		return new ResponseEntity<PojoArtifact[]>(art, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/artifacts", method = RequestMethod.POST)
	public ResponseEntity<PojoArtifact[]> getArtifacts(@PathVariable long id, @PathVariable long version,
			@RequestBody(required = true) PojoArtifact[] filters) {
		PojoArtifact[] art = handler.getArtifact(id, version, filters);
		return new ResponseEntity<PojoArtifact[]>(art, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/artifacts/alive={alive}/func/filteredbyproperties", method = RequestMethod.POST)
	public ResponseEntity<PojoArtifact[]> getArtifactsWithProperty(@PathVariable long id, @PathVariable long version,
			@PathVariable boolean alive, @RequestBody(required = true) PojoArtifactAndPropertyFilter filter) {
		PojoArtifact[] response = handler.getArtifactsWithProperty(id, version, alive, filter);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/artifacts/func/filteredbyreference", method = RequestMethod.POST)
	public ResponseEntity<PojoArtifact[]> getArtifactsWithReference(@PathVariable long id, @PathVariable long version,
			@RequestBody(required = true) PojoArtifactAndFilters artifactAndFilter) {
		PojoArtifact[] response = handler.getArtifactsWithReference(id, version, artifactAndFilter);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/artifactandproperties", method = RequestMethod.GET)
	public ResponseEntity<PojoArtifactAndProperties[]> getArtifactAndProperties(@PathVariable long id,
			@PathVariable long version) {
		PojoArtifactAndProperties[] result = handler.getArtifactAndProperties(id, version);
		return new ResponseEntity<PojoArtifactAndProperties[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/artifacts/func/mappings", method = RequestMethod.GET)
	public ResponseEntity<PojoArtifactAndProperties[]> getArtifactsAndPropertyMap(@PathVariable long id,
			@PathVariable long version) {
		PojoArtifactAndProperties[] response = handler.getArtifactsAndPropertyMap(id, version);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/artifacts/alive={alive}/func/mappings", method = RequestMethod.POST)
	public ResponseEntity<PojoArtifactAndProperties[]> getArtifactsAndPropertyMap(@PathVariable long id,
			@PathVariable long version, @PathVariable boolean alive,
			@RequestBody(required = true) PojoArtifactAndPropertyFilter filter) {
		PojoArtifactAndProperties[] response = handler.getArtifactsAndPropertyMap(id, version, alive, filter);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/artifacts/func/representation", method = RequestMethod.GET)
	public ResponseEntity<PojoObject[]> getArtifactRepresentation(@PathVariable long id, @PathVariable long version) {
		PojoObject[] obj = handler.getArtifactRepresentation(id, version);
		return new ResponseEntity<PojoObject[]>(obj, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={id}&v={version}/artifactsandproperties/func/representation", method = RequestMethod.GET)
	public ResponseEntity<PojoArtifactRepAndPropertiesRep[]> getArtifactPropertyMapRepresentations(
			@PathVariable long id, @PathVariable long version) {
		PojoArtifactRepAndPropertiesRep[] result = handler.getArtifactPropertyMapRepresentations(id, version);
		return new ResponseEntity<PojoArtifactRepAndPropertiesRep[]>(result, HttpStatus.OK);
	}

	// ------------------------------------
	// Exception handling
	// ------------------------------------

//	@ExceptionHandler(ArtifactDeadException.class)
//	@ResponseStatus(value = HttpStatus.NOT_FOUND)
//	@ResponseBody
//	public String handleArtifactDeadException() {
//		return ArtifactDeadException.class.getSimpleName();
//	}
}
