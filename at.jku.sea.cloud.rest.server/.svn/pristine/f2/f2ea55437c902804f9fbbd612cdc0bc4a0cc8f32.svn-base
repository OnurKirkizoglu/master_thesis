package at.jku.sea.cloud.rest.server.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MetaModelDoesNotExistException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.exceptions.PackageDoesNotExistException;
import at.jku.sea.cloud.exceptions.ProjectDoesNotExistException;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndFilters;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndProperties;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndString;
import at.jku.sea.cloud.rest.pojo.PojoCollectionArtifact;
import at.jku.sea.cloud.rest.pojo.PojoMapArtifact;
import at.jku.sea.cloud.rest.pojo.PojoMetaModel;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoOwnerToolStringLong;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.PojoProject;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoResource;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.PojoWorkspace;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;
import at.jku.sea.cloud.rest.server.handler.CloudHandler;

@Controller
@RequestMapping("/designspace")
public class CloudController {

  @Autowired
  CloudHandler handler;

  @RequestMapping(value = "/listener/add", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<Long> addListener() {
    long id = handler.addListener();
    return new ResponseEntity<Long>(id, HttpStatus.OK);
  }

  @RequestMapping(value = "/listener/id={lid}/remove", method = RequestMethod.DELETE)
  @ResponseBody
  public void removeListener(@PathVariable long lid) {
    handler.removeListener(lid);
  }

  @RequestMapping(value = "/artifacts/version={version},id={id}", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifact> getArtifact(@PathVariable long version, @PathVariable long id) {
    PojoArtifact result = handler.getArtifact(version, id);
    return new ResponseEntity<PojoArtifact>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/versions", method = RequestMethod.GET)
  public ResponseEntity<PojoVersion[]> getAllVersions() {
    PojoVersion[] versions = handler.getAllVersions();
    return new ResponseEntity<PojoVersion[]>(versions, HttpStatus.OK);
  }

  @RequestMapping(value = "/func/listener/id={lid}/events", method = RequestMethod.GET)
  public ResponseEntity<PojoChange[]> getListenerEvents(@PathVariable long lid) {
    PojoChange[] result = handler.getListenerEvents(lid);
    return new ResponseEntity<PojoChange[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/metamodels/version={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoMetaModel[]> getMetaModels(@PathVariable long version) {
    PojoMetaModel[] models = handler.getMetaModels(version);
    return new ResponseEntity<PojoMetaModel[]>(models, HttpStatus.OK);
  }

  @RequestMapping(value = "/packages/version={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoPackage[]> getPackages(@PathVariable long version) {
    PojoPackage[] pkg = handler.getPackages(version);
    return new ResponseEntity<PojoPackage[]>(pkg, HttpStatus.OK);
  }

  @RequestMapping(value = "/projects/version={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoProject[]> getProjects(@PathVariable long version) {
    PojoProject[] prj = handler.getProjects(version);
    return new ResponseEntity<PojoProject[]>(prj, HttpStatus.OK);
  }

  @RequestMapping(value = "/workspaces", method = RequestMethod.GET)
  public ResponseEntity<PojoWorkspace[]> getWorkspaces() {
    PojoWorkspace[] ws = handler.getWorkspaces();
    return new ResponseEntity<PojoWorkspace[]>(ws, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/version={version}", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifact[]> getArtifacts(@PathVariable long version, @RequestBody(required = true) PojoArtifact[] filters) {
    PojoArtifact[] art = handler.getArtifact(version, filters);
    return new ResponseEntity<PojoArtifact[]>(art, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/version={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifact[]> getArtifacts(@PathVariable long version) {
    PojoArtifact[] art = handler.getArtifacts(version);
    return new ResponseEntity<PojoArtifact[]>(art, HttpStatus.OK);
  }

  @RequestMapping(value = "/owners/func/create", method = RequestMethod.POST)
  public ResponseEntity<PojoOwner> createOwner() {
    PojoOwner owner = handler.createOwner();
    return new ResponseEntity<PojoOwner>(owner, HttpStatus.CREATED);
  }

//  @RequestMapping(value = "/tools/func/create", method = RequestMethod.POST)
//  public ResponseEntity<PojoTool> createTool() {
//    PojoTool tool = handler.createTool();
//    return new ResponseEntity<PojoTool>(tool, HttpStatus.CREATED);
//  }

  @RequestMapping(value = "/workspaces/id={privateVersionNumber}")
  public ResponseEntity<PojoWorkspace> getWorkspace(@PathVariable long privateVersionNumber) throws WorkspaceExpiredException {
    PojoWorkspace ws = handler.getWorkspace(privateVersionNumber);
    return new ResponseEntity<PojoWorkspace>(ws, HttpStatus.OK);
  }

  @RequestMapping(value = "/workspaces/func/get", method = RequestMethod.POST)
  public ResponseEntity<PojoWorkspace> getWorkspace(@RequestBody(required = true) PojoOwnerToolStringLong pojo) throws WorkspaceExpiredException {
    PojoWorkspace ws = handler.getWorkspace(pojo);
    return new ResponseEntity<PojoWorkspace>(ws, HttpStatus.OK);
  }

  @RequestMapping(value = "/headartifacts", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifact[]> getHeadArtifacts() {
    PojoArtifact[] art = handler.getHeadArtifacts();
    return new ResponseEntity<PojoArtifact[]>(art, HttpStatus.OK);
  }

  @RequestMapping(value = "/headartifacts", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifact[]> getHeadArtifacts(@RequestBody(required = true) PojoArtifact[] filters) {

    PojoArtifact[] art = handler.getHeadArtifacts(filters);
    return new ResponseEntity<PojoArtifact[]>(art, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/property/version={version}", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifact[]> getArtifactsWithProperty(@PathVariable long version, @RequestBody(required = true) PojoArtifactAndPropertyFilter filter) {
    PojoArtifact[] result = handler.getArtifacts(version, filter);
    return new ResponseEntity<PojoArtifact[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/headartifacts/version={version}", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifact[]> getHeadArtifactsWithProperty(@RequestBody(required = true) PojoArtifactAndPropertyFilter filter) {
    PojoArtifact[] result = handler.getHeadArtifactsWithProperty(filter);
    return new ResponseEntity<PojoArtifact[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/properties/version={version}", method = RequestMethod.POST)
  public ResponseEntity<PojoProperty[]> getPropertiesByReference(@PathVariable long version, @RequestBody(required = true) PojoArtifact artifact) {
    PojoProperty[] result = handler.getPropertiesByReference(version, artifact);
    return new ResponseEntity<PojoProperty[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/version={version}/func/getwithreference", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifact[]> getArtifactsWithReference(@PathVariable long version, @RequestBody(required = true) PojoArtifactAndFilters artifactAndFilter) {
    PojoArtifact[] response = handler.getArtifactsWithReference(version, artifactAndFilter);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/version={wid}/artifacts/func/getwithfilters", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifact[]> getArtifacts(@PathVariable long wid, @RequestBody(required = true) PojoArtifactAndPropertyFilter options) throws WorkspaceExpiredException,
      ArtifactDoesNotExistException, CollectionArtifactDoesNotExistException, MetaModelDoesNotExistException, PackageDoesNotExistException, ProjectDoesNotExistException, OwnerDoesNotExistException,
      ToolDoesNotExistException {

    PojoArtifact[] result = handler.getArtifacts(wid, options);
    return new ResponseEntity<PojoArtifact[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/packages/version={version}/id={id}", method = RequestMethod.GET)
  public ResponseEntity<PojoPackage> getPackage(@PathVariable long version, @PathVariable long id) {
    PojoPackage pkg = handler.getPackage(version, id);
    return new ResponseEntity<PojoPackage>(pkg, HttpStatus.OK);
  }

  @RequestMapping(value = "/headpackages", method = RequestMethod.GET)
  public ResponseEntity<PojoPackage[]> getHeadPackages() {
    PojoPackage[] pkgs = handler.getHeadPackages();
    return new ResponseEntity<PojoPackage[]>(pkgs, HttpStatus.OK);
  }

  @RequestMapping(value = "/projects/version={version}/id={id}", method = RequestMethod.GET)
  public ResponseEntity<PojoProject> getProject(@PathVariable long version, @PathVariable long id) {
    PojoProject prj = handler.getProject(version, id);
    return new ResponseEntity<PojoProject>(prj, HttpStatus.OK);
  }

  @RequestMapping(value = "/metamodels/version={version}/id={id}", method = RequestMethod.GET)
  public ResponseEntity<PojoMetaModel> getMetaModel(@PathVariable long version, @PathVariable long id) {
    PojoMetaModel mm = handler.getMetaModel(version, id);
    return new ResponseEntity<PojoMetaModel>(mm, HttpStatus.OK);
  }

  @RequestMapping(value = "/headmetamodels", method = RequestMethod.GET)
  public ResponseEntity<PojoMetaModel[]> getHeadMetaModels() {
    PojoMetaModel[] mm = handler.getHeadMetaModels();
    return new ResponseEntity<PojoMetaModel[]>(mm, HttpStatus.OK);
  }

  @RequestMapping(value = "/collectionartifacts/version={version}/id={id}", method = RequestMethod.GET)
  public ResponseEntity<PojoCollectionArtifact> getCollectionArtifact(@PathVariable long version, @PathVariable long id) {
    PojoCollectionArtifact cart = handler.getCollectionArtifact(version, id);
    return new ResponseEntity<PojoCollectionArtifact>(cart, HttpStatus.OK);
  }

  @RequestMapping(value = "/collectionartifacts/version={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoCollectionArtifact[]> getCollectionArtifacts(@PathVariable long version) {
    PojoCollectionArtifact[] carts = handler.getCollectionArtifacts(version);
    return new ResponseEntity<PojoCollectionArtifact[]>(carts, HttpStatus.OK);
  }

  @RequestMapping(value = "/headcollectionartifacts", method = RequestMethod.GET)
  public ResponseEntity<PojoCollectionArtifact[]> getHeadCollectionArtifacts() {
    PojoCollectionArtifact[] carts = handler.getHeadCollectionArtifacts();
    return new ResponseEntity<PojoCollectionArtifact[]>(carts, HttpStatus.OK);
  }

  @RequestMapping(value = "/privateversions", method = RequestMethod.GET)
  public ResponseEntity<PojoVersion[]> getPrivateVersions() {
    PojoVersion[] versions = handler.getPrivateVersions();
    return new ResponseEntity<PojoVersion[]>(versions, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/history", method = RequestMethod.POST)
  public ResponseEntity<PojoVersion[]> getArtifactHistoryVersionNumbers(@RequestBody(required = true) PojoArtifact artifact) {
    PojoVersion[] versions = handler.getArtifactHistoryVersionNumbers(artifact);
    return new ResponseEntity<PojoVersion[]>(versions, HttpStatus.OK);
  }

  @RequestMapping(value = "/properties/history/name={name}", method = RequestMethod.POST)
  public ResponseEntity<PojoVersion[]> getPropertyHistoryVersionNumbers(@PathVariable String name, @RequestBody(required = true) PojoArtifact artifact) {
    PojoVersion[] versions = handler.getPropertyHistoryVersionNumbers(artifact, name);
    return new ResponseEntity<PojoVersion[]>(versions, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/diff/version={version}/previousVersion={previousVersion}", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifact[]> getArtifactDiffs(@PathVariable long version, @PathVariable long previousVersion) {
    PojoArtifact[] art = handler.getArtifactDiffs(version, previousVersion);
    return new ResponseEntity<PojoArtifact[]>(art, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/diff/version={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifactAndProperties[]> getDiffs(@PathVariable long version) {
    PojoArtifactAndProperties[] result = handler.getDiffs(version);
    return new ResponseEntity<PojoArtifactAndProperties[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/diff/version={version}/previousVersion={previousVersion}", method = RequestMethod.GET)
  public ResponseEntity<PojoArtifactAndProperties[]> getDiffs(@PathVariable long version, @PathVariable long previousVersion) {
    PojoArtifactAndProperties[] result = handler.getDiffs(version, previousVersion);
    return new ResponseEntity<PojoArtifactAndProperties[]>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/headprojects", method = RequestMethod.GET)
  public ResponseEntity<PojoProject[]> getHeadProjects() {
    PojoProject[] prj = handler.getHeadProjects();
    return new ResponseEntity<PojoProject[]>(prj, HttpStatus.OK);
  }

  // @RequestMapping(value = "/workspaces/version={versionNumber}/isempty",
  // method = RequestMethod.GET)
  // public ResponseEntity<Boolean> isWorkspaceEmpty(long versionNumber) {
  // boolean isEmpty = handler.isWorkspaceEmpty(versionNumber);
  // return new ResponseEntity<Boolean>(isEmpty, HttpStatus.OK);
  // }
  //
  @RequestMapping(value = "/artifacts/version={version}/id={id}/representation", method = RequestMethod.GET)
  public ResponseEntity<PojoObject[]> getArtifactRepresentation(@PathVariable long version, @PathVariable long id) {
    PojoObject[] obj = handler.getArtifactRepresentation(version, id);
    return new ResponseEntity<PojoObject[]>(obj, HttpStatus.OK);
  }

  @RequestMapping(value = "/mapartifacts/id={id}&v={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoMapArtifact> getMapArtifact(@PathVariable long id, @PathVariable long version) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
    PojoMapArtifact response = handler.getMapArtifact(id, version);
    return new ResponseEntity<PojoMapArtifact>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/mapartifacts/v={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoMapArtifact[]> getMapArtifact(@PathVariable long version) throws VersionDoesNotExistException {
    PojoMapArtifact[] response = handler.getMapArtifacts(version);
    return new ResponseEntity<PojoMapArtifact[]>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/property/version={version}/alive={alive}", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifact[]> getArtifactsWithProperty(@PathVariable long version, @RequestBody(required = true) PojoArtifactAndPropertyFilter filter, @PathVariable boolean alive) {
    PojoArtifact[] response = handler.getArtifactsWithProperty(version, filter, alive);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/property/version={version}/alive={alive}/mappings", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifactAndProperties[]> getArtifactsAndPropertyMap(@PathVariable long version, @RequestBody(required = true) PojoArtifactAndPropertyFilter filter,
      @PathVariable boolean alive) {
    PojoArtifactAndProperties[] response = handler.getArtifactsAndPropertyMap(version, filter, alive);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/property/version={version}/artifactpropertymap", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifactAndProperties[]> getArtifactsPropertyMap(@PathVariable long version, @RequestBody(required = true) PojoArtifactAndString artifactsAndProps) {
    PojoArtifactAndProperties[] response = handler.getArtifactsPropertyMap(version, artifactsAndProps);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/headartifacts/property/alive={alive}", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifact[]> getHeadArtifactsWithProperty(@RequestBody(required = true) PojoArtifactAndPropertyFilter filter, @PathVariable boolean alive) {
    PojoArtifact[] response = handler.getHeadArtifactsWithProperty(filter, alive);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/workspaces/children", method = RequestMethod.GET)
  public ResponseEntity<PojoWorkspace[]> getWorkspaceChildren(@RequestParam(value = "privateVersion", required = false) Long privateVersion) throws WorkspaceExpiredException {
    PojoWorkspace[] response = handler.getWorkspaceChildren(privateVersion);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/artifacts/version={version}/func/getwithids", method = RequestMethod.POST)
  public ResponseEntity<PojoArtifact[]> getArtifacts(@RequestBody(required = true) Set<Long> ids, @PathVariable long version) {
    PojoArtifact[] response = handler.getArtifacts(version, ids);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/resources/version={version},id={id}", method = RequestMethod.GET)
  public ResponseEntity<PojoResource> getResource(@PathVariable long version, @PathVariable long id) {
	PojoResource result = handler.getResource(version, id);
    return new ResponseEntity<PojoResource>(result, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/resources/version={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoResource[]> getResources(@PathVariable long version) {
	PojoResource[] art = handler.getResources(version);
    return new ResponseEntity<PojoResource[]>(art, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/resources/version={version}", method = RequestMethod.POST)
  public ResponseEntity<PojoResource[]> getResources(@PathVariable long version, @RequestBody(required = true) String fullQualifiedName) {
	PojoResource[] art = handler.getResources(version, fullQualifiedName);
    return new ResponseEntity<PojoResource[]>(art, HttpStatus.OK);
  }

  // ------------------------------------
  // Exception handling
  // ------------------------------------

  @ExceptionHandler(WorkspaceExpiredException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleWorkspaceExpiredException() {
    return WorkspaceExpiredException.class.getSimpleName();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleIllegalArgumentException() {
    return IllegalArgumentException.class.getSimpleName();
  }

  @ExceptionHandler(IndexOutOfBoundsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleIndexOutOfBoundsException() {
    return IndexOutOfBoundsException.class.getSimpleName();
  }

  @ExceptionHandler(OwnerDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleOwnerDoesNotExistException() {
    return OwnerDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(MapArtifactDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleMapArtifactDoesNotExistException() {
    return MapArtifactDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(ToolDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleToolDoesNotExistException() {
    return ToolDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(ArtifactDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleArtifactDoesNotExistException() {
    return ArtifactDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(PackageDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handlePackageDoesNotExistException() {
    return PackageDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(ProjectDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleProjectDoesNotExistException() {
    return ProjectDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(MetaModelDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleMetaModelDoesNotExistException() {
    return MetaModelDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(CollectionArtifactDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleCollectionArtifactDoesNotExistException() {
    return CollectionArtifactDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(VersionDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleVersionDoesNotExistException() {
    return VersionDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(ArtifactDeadException.class)
  @ResponseStatus(value = HttpStatus.GONE)
  @ResponseBody
  public String handleArtifactDeadException() {
    return ArtifactDeadException.class.getSimpleName();
  }
}
