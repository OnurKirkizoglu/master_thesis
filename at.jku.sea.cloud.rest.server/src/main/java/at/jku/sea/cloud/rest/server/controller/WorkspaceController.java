package at.jku.sea.cloud.rest.server.controller;

import java.util.Collection;

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

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.exceptions.ArtifactConflictException;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.ArtifactNotCommitableException;
import at.jku.sea.cloud.exceptions.ArtifactNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MetaModelDoesNotExistException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.exceptions.PackageDoesNotExistException;
import at.jku.sea.cloud.exceptions.ProjectDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyConflictException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyNotCommitableException;
import at.jku.sea.cloud.exceptions.PropertyNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.exceptions.VersionConflictException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceConflictException;
import at.jku.sea.cloud.exceptions.WorkspaceCycleException;
import at.jku.sea.cloud.exceptions.WorkspaceEmptyException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.exceptions.WorkspaceNotEmptyException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndProperties;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoArtifactWithParameters;
import at.jku.sea.cloud.rest.pojo.PojoCollectionArtifact;
import at.jku.sea.cloud.rest.pojo.PojoCollectionArtifactWithParameters;
import at.jku.sea.cloud.rest.pojo.PojoKeyValue;
import at.jku.sea.cloud.rest.pojo.PojoMapArtifact;
import at.jku.sea.cloud.rest.pojo.PojoMetaModel;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoOwnerToolStringLongParentPushPull;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.PojoProject;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoResource;
import at.jku.sea.cloud.rest.pojo.PojoStringPackageProjectArtifacts;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.PojoWorkspace;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;
import at.jku.sea.cloud.rest.server.handler.WorkspaceHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@Controller
@RequestMapping("/designspace/workspaces")
public class WorkspaceController {

	@Autowired
	WorkspaceHandler handler;

	@RequestMapping(value = "/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoWorkspace> getOrCreateWorkspace(
			@RequestBody(required = true) PojoOwnerToolStringLongParentPushPull options)
					throws WorkspaceExpiredException, OwnerDoesNotExistException, ToolDoesNotExistException {
		PojoWorkspace result = handler.getOrCreateWorkspace(options);
		return new ResponseEntity<PojoWorkspace>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/identifier", method = RequestMethod.GET)
	public ResponseEntity<String> getIdentifier(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		String result = handler.getIdentifier(wsid);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/owner", method = RequestMethod.GET)
	public ResponseEntity<PojoOwner> getOwner(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoOwner result = handler.getOwner(wsid);
		return new ResponseEntity<PojoOwner>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/tool", method = RequestMethod.GET)
	public ResponseEntity<PojoTool> getTool(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoTool result = handler.getTool(wsid);
		return new ResponseEntity<PojoTool>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/baseversion", method = RequestMethod.GET)
	public ResponseEntity<PojoVersion> getBaseVersion(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoVersion result = handler.getBaseVersion(wsid);
		return new ResponseEntity<PojoVersion>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/func/rebase", method = RequestMethod.POST)
	public ResponseEntity<PojoArtifactAndProperties[]> rebase(@PathVariable long wsid,
			@RequestParam(value = "v", required = false) Long version)
					throws WorkspaceExpiredException, IllegalArgumentException, VersionDoesNotExistException {
		PojoArtifactAndProperties[] result = handler.rebase(wsid, version);
		return new ResponseEntity<PojoArtifactAndProperties[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts", method = RequestMethod.GET)
	public ResponseEntity<PojoArtifact[]> getArtifacts(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoArtifact[] result = handler.getArtifacts(wsid);
		return new ResponseEntity<PojoArtifact[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/func/getwithfilters", method = RequestMethod.POST)
	public ResponseEntity<PojoArtifact[]> getArtifacts(@PathVariable long wsid,
			@RequestBody(required = true) PojoArtifactAndPropertyFilter options) throws WorkspaceExpiredException,
					ArtifactDoesNotExistException, CollectionArtifactDoesNotExistException,
					MetaModelDoesNotExistException, PackageDoesNotExistException, ProjectDoesNotExistException,
					OwnerDoesNotExistException, ToolDoesNotExistException {
		PojoArtifact[] result = handler.getArtifacts(wsid, options);
		return new ResponseEntity<PojoArtifact[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}", method = RequestMethod.GET)
	public ResponseEntity<PojoArtifact> getArtifact(@PathVariable long wsid, @PathVariable long id)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException {
		PojoArtifact result = handler.getArtifact(wsid, id);
		return new ResponseEntity<PojoArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoArtifact> createArtifact(@PathVariable long wsid,
			@RequestBody(required = false) PojoArtifact type) throws WorkspaceExpiredException,
					IllegalArgumentException, ArtifactDoesNotExistException, VersionDoesNotExistException {
		PojoArtifact result = handler.createArtifact(wsid, type);
		return new ResponseEntity<PojoArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/func/createwithparams", method = RequestMethod.POST)
	public ResponseEntity<PojoArtifact> createArtifact(@PathVariable long wsid,
			@RequestBody PojoArtifactWithParameters params) throws WorkspaceExpiredException {
		PojoArtifact result = handler.createArtifact(wsid, params);
		return new ResponseEntity<PojoArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/packages/id={pkgId}&v={pkgVersion}/artifacts/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoArtifact> createArtifact(@PathVariable long wsid, @PathVariable long pkgId,
			@PathVariable long pkgVersion, @RequestBody(required = false) PojoArtifact type)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					PackageDoesNotExistException, VersionDoesNotExistException {
		PojoArtifact result = handler.createArtifact(wsid, pkgId, pkgVersion, type);
		return new ResponseEntity<PojoArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}/func/commit", method = RequestMethod.POST)
	public ResponseEntity<Long> commitArtifact(@PathVariable long wsid, @PathVariable long id,
			@RequestBody(required = false) String message) throws WorkspaceExpiredException, IllegalArgumentException,
					ArtifactDoesNotExistException, ArtifactConflictException {
		Long result = handler.commitArtifact(wsid, id, message);
		return new ResponseEntity<Long>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}/func/rollback", method = RequestMethod.PUT)
	@ResponseBody
	public void rollbackArtifact(@PathVariable long wsid, @PathVariable long id)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException {
		handler.rollbackArtifact(wsid, id);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}/properties/name={name}/func/commit", method = RequestMethod.POST)
	public ResponseEntity<Long> commitProperty(@PathVariable long wsid, @PathVariable long id,
			@PathVariable String name, @RequestBody(required = false) String message)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					PropertyDoesNotExistException, PropertyConflictException, PropertyNotCommitableException {
		Long result = handler.commitProperty(wsid, id, name, message);
		return new ResponseEntity<Long>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}/properties/name={name}/func/rollback", method = RequestMethod.PUT)
	@ResponseBody
	public void rollbackProperty(@PathVariable long wsid, @PathVariable long id, @PathVariable String name)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			PropertyDoesNotExistException {
		handler.rollbackProperty(wsid, id, name);
	}

	@RequestMapping(value = "/id={wsid}/packages", method = RequestMethod.GET)
	public ResponseEntity<PojoPackage[]> getPackages(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoPackage[] result = handler.getPackages(wsid);
		return new ResponseEntity<PojoPackage[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/packages/id={id}", method = RequestMethod.GET)
	public ResponseEntity<PojoPackage> getPackage(@PathVariable long wsid, @PathVariable long id)
			throws WorkspaceExpiredException, IllegalArgumentException, PackageDoesNotExistException {
		PojoPackage result = handler.getPackage(wsid, id);
		return new ResponseEntity<PojoPackage>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/packages/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoPackage> createPackage(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoPackage result = handler.createPackage(wsid);
		return new ResponseEntity<PojoPackage>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/packages/id={pkgId}&v={pkgVersion}/packages/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoPackage> createPackage(@PathVariable long wsid, @PathVariable long pkgId,
			@PathVariable long pkgVersion) throws WorkspaceExpiredException, IllegalArgumentException,
					PackageDoesNotExistException, VersionDoesNotExistException {
		PojoPackage result = handler.createPackage(wsid, pkgId, pkgVersion);
		return new ResponseEntity<PojoPackage>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={wsid}/packages/func/create/name={name}", method = RequestMethod.POST)
  public ResponseEntity<PojoPackage> createPackage(@PathVariable long wsid, @PathVariable String name)
      throws WorkspaceExpiredException, IllegalArgumentException {
    PojoPackage result = handler.createPackage(wsid, name);
    return new ResponseEntity<PojoPackage>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "/id={wsid}/packages/id={pkgId}&v={pkgVersion}/packages/func/create/name={name}", method = RequestMethod.POST)
  public ResponseEntity<PojoPackage> createPackage(@PathVariable long wsid, @PathVariable long pkgId,
      @PathVariable long pkgVersion, @PathVariable String name) throws WorkspaceExpiredException, IllegalArgumentException,
          PackageDoesNotExistException, VersionDoesNotExistException {
    PojoPackage result = handler.createPackage(wsid, pkgId, pkgVersion, name);
    return new ResponseEntity<PojoPackage>(result, HttpStatus.OK);
  }

	@RequestMapping(value = "/id={wsid}/projects", method = RequestMethod.GET)
	public ResponseEntity<PojoProject[]> getProjects(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoProject[] result = handler.getProjects(wsid);
		return new ResponseEntity<PojoProject[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/projects/id={id}", method = RequestMethod.GET)
	public ResponseEntity<PojoProject> getProject(@PathVariable long wsid, @PathVariable long id)
			throws WorkspaceExpiredException, IllegalArgumentException, ProjectDoesNotExistException {
		PojoProject result = handler.getProject(wsid, id);
		return new ResponseEntity<PojoProject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/projects/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoProject> createProject(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoProject result = handler.createProject(wsid);
		return new ResponseEntity<PojoProject>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={wsid}/projects/func/create/name={name}", method = RequestMethod.POST)
  public ResponseEntity<PojoProject> createProject(@PathVariable long wsid, @PathVariable String name)
      throws WorkspaceExpiredException, IllegalArgumentException {
    PojoProject result = handler.createProject(wsid, name);
    return new ResponseEntity<PojoProject>(result, HttpStatus.OK);
  }

	@Deprecated
	@RequestMapping(value = "/id={wsid}/packages/id={pkgId}&v={pkgVersion}/projects/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoProject> createProject(@PathVariable long wsid, @PathVariable long pkgId,
			@PathVariable long pkgVersion) throws WorkspaceExpiredException, IllegalArgumentException,
					PackageDoesNotExistException, VersionDoesNotExistException {
		PojoProject result = handler.createProject(wsid, pkgId, pkgVersion);
		return new ResponseEntity<PojoProject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/metamodels", method = RequestMethod.GET)
	public ResponseEntity<PojoMetaModel[]> getMetaModels(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoMetaModel[] result = handler.getMetaModels(wsid);
		return new ResponseEntity<PojoMetaModel[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/metamodels/id={id}", method = RequestMethod.GET)
	public ResponseEntity<PojoMetaModel> getMetaModel(@PathVariable long wsid, @PathVariable long id)
			throws WorkspaceExpiredException, IllegalArgumentException, MetaModelDoesNotExistException {
		PojoMetaModel result = handler.getMetaModel(wsid, id);
		return new ResponseEntity<PojoMetaModel>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/aid={aid}/metametamodel", method = RequestMethod.GET)
	public ResponseEntity<PojoMetaModel> getMetaMetaModel(@PathVariable long wsid, @PathVariable long aid)
			throws WorkspaceExpiredException, IllegalArgumentException, MetaModelDoesNotExistException {
		PojoMetaModel result = handler.getMetaMetaModel(wsid, aid);
		return new ResponseEntity<PojoMetaModel>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/metamodels/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoMetaModel> createMetaModel(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoMetaModel result = handler.createMetaModel(wsid);
		return new ResponseEntity<PojoMetaModel>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/packages/id={pkgId}&v={pkgVersion}/metamodels/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoMetaModel> createMetaModel(@PathVariable long wsid, @PathVariable long pkgId,
			@PathVariable long pkgVersion) throws WorkspaceExpiredException, IllegalArgumentException,
					PackageDoesNotExistException, VersionDoesNotExistException {
		PojoMetaModel result = handler.createMetaModel(wsid, pkgId, pkgVersion);
		return new ResponseEntity<PojoMetaModel>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/collectionartifacts", method = RequestMethod.GET)
	public ResponseEntity<PojoCollectionArtifact[]> getCollectionArtifacts(@PathVariable long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		PojoCollectionArtifact[] result = handler.getCollectionArtifacts(wsid);
		return new ResponseEntity<PojoCollectionArtifact[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/collectionartifacts/id={id}", method = RequestMethod.GET)
	public ResponseEntity<PojoCollectionArtifact> getCollectionArtifact(@PathVariable long wsid, @PathVariable long id)
			throws WorkspaceExpiredException, IllegalArgumentException, CollectionArtifactDoesNotExistException {
		PojoCollectionArtifact result = handler.getCollectionArtifact(wsid, id);
		return new ResponseEntity<PojoCollectionArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/collectionartifacts/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoCollectionArtifact> createCollectionArtifact(@PathVariable long wsid,
			@RequestBody(required = true) Boolean containsOnlyArtifacts)
					throws WorkspaceExpiredException, IllegalArgumentException {
		PojoCollectionArtifact result = handler.createCollectionArtifact(wsid, containsOnlyArtifacts);
		return new ResponseEntity<PojoCollectionArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/packages/id={pkgId}&v={pkgVersion}/collectionartifacts/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoCollectionArtifact> createCollectionArtifact(@PathVariable long wsid,
			@PathVariable long pkgId, @PathVariable long pkgVersion,
			@RequestBody(required = true) Boolean containsOnlyArtifacts) throws WorkspaceExpiredException,
					IllegalArgumentException, PackageDoesNotExistException, VersionDoesNotExistException {
		PojoCollectionArtifact result = handler.createCollectionArtifact(wsid, pkgId, pkgVersion,
				containsOnlyArtifacts);
		return new ResponseEntity<PojoCollectionArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/collectionartifacts/func/createwithparams", method = RequestMethod.POST)
	public ResponseEntity<PojoCollectionArtifact> createCollectionArtifact(@PathVariable long wsid,
			@RequestBody PojoCollectionArtifactWithParameters params) throws WorkspaceExpiredException {
		PojoCollectionArtifact result = handler.createCollectionArtifact(wsid, params);
		return new ResponseEntity<PojoCollectionArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/mapartifacts/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoMapArtifact> createMapArtifact(@PathVariable long wsid) throws WorkspaceExpiredException {
		PojoMapArtifact result = handler.createMapArtifact(wsid);
		return new ResponseEntity<PojoMapArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/packages/id={pkgId}&v={pkgVersion}/mapartifacts/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoMapArtifact> createMapArtifact(@PathVariable long wsid, @PathVariable long pkgId,
			@PathVariable long pkgVersion)
					throws WorkspaceExpiredException, PackageDoesNotExistException, VersionDoesNotExistException {
		PojoMapArtifact result = handler.createMapArtifact(wsid, pkgId, pkgVersion);
		return new ResponseEntity<PojoMapArtifact>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/func/commit", method = RequestMethod.POST)
	public ResponseEntity<Long> commit(@PathVariable long wsid, @RequestBody(required = false) String message)
			throws WorkspaceExpiredException, IllegalArgumentException, VersionConflictException {
		Long result = handler.commit(wsid, message);
		return new ResponseEntity<Long>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/func/rollback", method = RequestMethod.PUT)
	@ResponseBody
	public void rollback(@PathVariable long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		handler.rollback(wsid);
	}

	@RequestMapping(value = "/id={wsid}/artifactconflicts", method = RequestMethod.GET)
	public ResponseEntity<PojoArtifact[]> getArtifactConflicts(@PathVariable long wsid,
			@RequestParam(value = "v", required = false) Long version)
					throws WorkspaceExpiredException, IllegalArgumentException {
		PojoArtifact[] result = handler.getArtifactConflicts(wsid, version);
		return new ResponseEntity<PojoArtifact[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/propertyconflicts", method = RequestMethod.GET)
	public ResponseEntity<PojoProperty[]> getPropertyConflicts(@PathVariable long wsid,
			@RequestParam(value = "v", required = false) Long version)
					throws WorkspaceExpiredException, IllegalArgumentException {
		PojoProperty[] result = handler.getPropertyConflicts(wsid, version);
		return new ResponseEntity<PojoProperty[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/func/close", method = RequestMethod.PUT)
	@ResponseBody
	public void close(@PathVariable long wsid)
			throws WorkspaceExpiredException, WorkspaceNotEmptyException, IllegalArgumentException {
		handler.close(wsid);
	}

	@RequestMapping(value = "/id={wsid}/listener/add", method = RequestMethod.POST)
	public ResponseEntity<Long> addListener(@PathVariable long wsid,
			@RequestParam(value = "interval", required = true) long interval) throws WorkspaceExpiredException {
		long result = handler.addListener(wsid, interval);
		return new ResponseEntity<Long>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/listener/id={lid}/remove", method = RequestMethod.DELETE)
	@ResponseBody
	public void removeListener(@PathVariable long wsid, @PathVariable long lid) throws WorkspaceExpiredException {
		handler.removeListener(wsid, lid);
	}

	@RequestMapping(value = "/func/listener/id={lid}/events", method = RequestMethod.GET)
	public ResponseEntity<PojoChange[]> getListenerEvents(@PathVariable long lid) {
		PojoChange[] result = handler.getListenerEvents(lid);
		return new ResponseEntity<PojoChange[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}&v={version}/type", method = RequestMethod.PUT)
	@ResponseBody
	public void setArtifactType(@PathVariable long wsid, @PathVariable long id, @PathVariable long version,
			@RequestBody(required = false) PojoArtifact type)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					VersionDoesNotExistException, ArtifactDeadException {
		handler.setArtifactType(wsid, id, version, type);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}&v={version}/package", method = RequestMethod.PUT)
	@ResponseBody
	public void setArtifactPackage(@PathVariable long wsid, @PathVariable long id, @PathVariable long version,
			@RequestBody(required = false) PojoPackage _package)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					PackageDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		handler.setArtifactPackage(wsid, id, version, _package);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}&v={version}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteArtifact(@PathVariable long wsid, @PathVariable long id, @PathVariable long version)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			VersionDoesNotExistException {
		handler.deleteArtifact(wsid, id, version);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}&v={version}/func/undelete", method = RequestMethod.PUT)
	@ResponseBody
	public void undeleteArtifact(@PathVariable long wsid, @PathVariable long id, @PathVariable long version)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			VersionDoesNotExistException {
		handler.undeleteArtifact(wsid, id, version);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}&v={version}/properties/name={name}", method = RequestMethod.POST)
	public ResponseEntity<PojoProperty> createProperty(@PathVariable long wsid, @PathVariable long id,
			@PathVariable long version, @PathVariable String name)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					VersionDoesNotExistException, ArtifactDeadException {
		PojoProperty result = handler.createProperty(wsid, id, version, name);
		return new ResponseEntity<PojoProperty>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}&v={version}/properties/name={name}/value", method = RequestMethod.PUT)
	@ResponseBody
	public void setPropertyValue(@PathVariable long wsid, @PathVariable long id, @PathVariable long version,
			@PathVariable String name, @RequestBody(required = false) PojoObject value)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					PropertyDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException,
					PropertyDeadException, TypeNotSupportedException {
		handler.setPropertyValue(wsid, id, version, name, value);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}&v={version}/properties/func/setvalues", method = RequestMethod.PUT)
	@ResponseBody
	public void setPropertyValues(@PathVariable long wsid, @PathVariable long id, @PathVariable long version,
			@RequestBody PojoPropertyFilter[] properties) throws ArtifactDoesNotExistException, ArtifactDeadException,
					PropertyDeadException, TypeNotSupportedException {
		handler.setPropertyValues(wsid, id, version, properties);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}&v={version}/properties/name={name}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteProperty(@PathVariable long wsid, @PathVariable long id, @PathVariable long version,
			@PathVariable String name) throws WorkspaceExpiredException, IllegalArgumentException,
					ArtifactDoesNotExistException, PropertyDoesNotExistException, VersionDoesNotExistException {
		handler.deleteProperty(wsid, id, version, name);
	}

	@RequestMapping(value = "/id={wsid}/artifacts/id={id}&v={version}/properties/name={name}/func/undelete", method = RequestMethod.PUT)
	@ResponseBody
	public void undeleteProperty(@PathVariable long wsid, @PathVariable long id, @PathVariable long version,
			@PathVariable String name) throws WorkspaceExpiredException, IllegalArgumentException,
					ArtifactDoesNotExistException, PropertyDoesNotExistException, VersionDoesNotExistException {
		handler.undeleteProperty(wsid, id, version, name);
	}

	@RequestMapping(value = "/id={wsid}/projects/id={proId}&v={proVersion}/artifacts/id={id}&v={version}", method = RequestMethod.PUT)
	@ResponseBody
	public void addArtifactToProject(@PathVariable long wsid, @PathVariable long proId, @PathVariable long proVersion,
			@PathVariable long id, @PathVariable long version)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					ProjectDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		handler.addArtifactToProject(wsid, proId, proVersion, id, version);
	}

	@RequestMapping(value = "/id={wsid}/projects/id={proId}&v={proVersion}/artifacts/id={id}&v={version}", method = RequestMethod.DELETE)
	@ResponseBody
	public void removeArtifactFromProject(@PathVariable long wsid, @PathVariable long proId,
			@PathVariable long proVersion, @PathVariable long id, @PathVariable long version)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					ProjectDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		handler.removeArtifactFromProject(wsid, proId, proVersion, id, version);
	}

	@RequestMapping(value = "/id={wsid}/metamodels/id={mmId}&v={mmVersion}/artifacts/id={id}&v={version}", method = RequestMethod.PUT)
	@ResponseBody
	public void addArtifactToMetamodel(@PathVariable long wsid, @PathVariable long mmId, @PathVariable long mmVersion,
			@PathVariable long id, @PathVariable long version)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					MetaModelDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		handler.addArtifactToMetamodel(wsid, mmId, mmVersion, id, version);
	}

	@RequestMapping(value = "/id={wsid}/metamodels/id={mmId}&v={mmVersion}/artifacts/add", method = RequestMethod.PUT)
	@ResponseBody
	public void addArtifactsToMetamodel(@PathVariable long wsid, @PathVariable long mmId, @PathVariable long mmVersion,
			@RequestBody(required = true) PojoArtifact[] artifacts)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					MetaModelDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		handler.addArtifactsToMetamodel(wsid, mmId, mmVersion, artifacts);
	}

	@RequestMapping(value = "/id={wsid}/metamodels/id={mmId}&v={mmVersion}/artifacts/id={id}&v={version}", method = RequestMethod.DELETE)
	@ResponseBody
	public void removeArtifactFromMetamodel(@PathVariable long wsid, @PathVariable long mmId,
			@PathVariable long mmVersion, @PathVariable long id, @PathVariable long version)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					MetaModelDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		handler.removeArtifactFromMetamodel(wsid, mmId, mmVersion, id, version);
	}

	@RequestMapping(value = "/id={wsid}/containers/id={cId}&v={cVersion}/artifacts/id={id}&v={version}", method = RequestMethod.PUT)
	@ResponseBody
	public void addArtifactToContainer(@PathVariable long wsid, @PathVariable long cId, @PathVariable long cVersion,
			@PathVariable long id, @PathVariable long version) {
		handler.addArtifactToContainer(wsid, cId, cVersion, id, version);
	}

	@RequestMapping(value = "/id={wsid}/containers/id={cId}&v={cVersion}/artifacts/add", method = RequestMethod.PUT)
	@ResponseBody
	public void addArtifactsToContainer(@PathVariable long wsid, @PathVariable long cId, @PathVariable long cVersion,
			@RequestBody(required = true) PojoArtifact[] artifacts) {
		handler.addArtifactsToContainer(wsid, cId, cVersion, artifacts);
	}

	@RequestMapping(value = "/id={wsid}/containers/id={cId}&v={cVersion}/artifacts/id={id}&v={version}", method = RequestMethod.DELETE)
	@ResponseBody
	public void removeArtifactFromContainer(@PathVariable long wsid, @PathVariable long cId,
			@PathVariable long cVersion, @PathVariable long id, @PathVariable long version) {
		handler.removeArtifactFromContainer(wsid, cId, cVersion, id, version);
	}

	@RequestMapping(value = "/id={wsid}/collectionartifacts/id={caId}&v={caVersion}/elements/func/add", method = RequestMethod.PUT)
	@ResponseBody
	public void addElementToCollectionArtifact(@PathVariable long wsid, @PathVariable long caId,
			@PathVariable long caVersion, @RequestBody(required = true) PojoObject element)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					CollectionArtifactDoesNotExistException, ArtifactDeadException, TypeNotSupportedException {
		handler.addElementToCollectionArtifact(wsid, caId, caVersion, element);
	}

	@RequestMapping(value = "/id={wsid}/collectionartifacts/id={caId}&v={caVersion}/elements/func/addElements", method = RequestMethod.PUT)
	@ResponseBody
	public void addElementsToCollectionArtifact(@PathVariable long wsid, @PathVariable long caId,
			@PathVariable long caVersion, @RequestBody(required = true) PojoObject[] elements)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					CollectionArtifactDoesNotExistException, ArtifactDeadException, TypeNotSupportedException {
		handler.addElementsToCollectionArtifact(wsid, caId, caVersion, elements);
	}

	@RequestMapping(value = "/id={wsid}/collectionartifacts/id={caId}&v={caVersion}/elements/func/remove", method = RequestMethod.PUT)
	@ResponseBody
	public void removeElementFromCollectionArtifact(@PathVariable long wsid, @PathVariable long caId,
			@PathVariable long caVersion, @RequestBody(required = true) PojoObject element)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					CollectionArtifactDoesNotExistException, ArtifactDeadException {
		handler.removeElementFromCollectionArtifact(wsid, caId, caVersion, element);
	}

	@RequestMapping(value = "/id={wsid}/collectionartifacts/id={caId}&v={caVersion}/elements/pos={index}", method = RequestMethod.PUT)
	@ResponseBody
	public void insertElementAtInCollectionArtifact(@PathVariable long wsid, @PathVariable long caId,
			@PathVariable long caVersion, @PathVariable long index, @RequestBody(required = true) PojoObject element)
					throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					CollectionArtifactDoesNotExistException, ArtifactDeadException, TypeNotSupportedException {
		handler.insertElementAtInCollectionArtifact(wsid, caId, caVersion, element, index);
	}

	@RequestMapping(value = "/id={wsid}/collectionartifacts/id={caId}&v={caVersion}/elements/pos={index}", method = RequestMethod.DELETE)
	@ResponseBody
	public void removeElementAtInCollectionArtifact(@PathVariable long wsid, @PathVariable long caId,
			@PathVariable long caVersion, @PathVariable long index)
					throws WorkspaceExpiredException, IllegalArgumentException, CollectionArtifactDoesNotExistException,
					ArtifactDeadException, IndexOutOfBoundsException {
		handler.removeElementAtInCollectionArtifact(wsid, caId, caVersion, index);
	}

	@RequestMapping(value = "/id={wsid}/mapartifacts/id={id}&v={version}/func/put", method = RequestMethod.POST)
	public ResponseEntity<PojoObject> putInMapArtifact(@PathVariable long wsid, @PathVariable long id,
			@PathVariable long version, @RequestBody PojoKeyValue keyValue)
					throws WorkspaceExpiredException, MapArtifactDoesNotExistException, VersionDoesNotExistException,
					TypeNotSupportedException, PropertyDeadException {
		PojoObject result = handler.putInMapArtifact(wsid, id, version, keyValue);
		return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/mapartifacts/id={id}&v={version}/func/remove", method = RequestMethod.POST)
	public ResponseEntity<PojoObject> removeInMapArtifact(@PathVariable long wsid, @PathVariable long id,
			@PathVariable long version, @RequestBody PojoObject pojoKey) throws WorkspaceExpiredException,
					MapArtifactDoesNotExistException, VersionDoesNotExistException, TypeNotSupportedException {
		PojoObject result = handler.removeInMapArtifact(wsid, id, version, pojoKey);
		return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/mapartifacts/id={id}&v={version}/func/clear", method = RequestMethod.DELETE)
	@ResponseBody
	public void clearMapArtifact(@PathVariable long wsid, @PathVariable long id, @PathVariable long version)
			throws WorkspaceExpiredException, MapArtifactDoesNotExistException, VersionDoesNotExistException {
		handler.clearMapArtifact(wsid, id, version);
	}

	@RequestMapping(value = "/id={wsid}/mapartifacts/id={id}&v={version}/entries/id={mid}&v={mversion}/value", method = RequestMethod.POST)
	public ResponseEntity<PojoObject> setMapEntryValue(@PathVariable long wsid, @PathVariable long id,
			@PathVariable long version, @PathVariable long mid, @PathVariable long mversion,
			@RequestBody PojoObject value)
					throws WorkspaceExpiredException, ArtifactDoesNotExistException, VersionDoesNotExistException,
					PropertyDoesNotExistException, ArtifactDeadException, PropertyDeadException {
		PojoObject result = handler.setMapEntryValue(wsid, id, version, mid, mversion, value);
		return new ResponseEntity<PojoObject>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/pull", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PropagationType> getPull(@PathVariable long wsid) {
		PropagationType pull = handler.getPull(wsid);
		return new ResponseEntity<PropagationType>(pull, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/pull/all", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<PojoArtifactAndProperties[]> pullAll(@PathVariable long wsid) {
		PojoArtifactAndProperties[] result = handler.pullAll(wsid);
		return new ResponseEntity<PojoArtifactAndProperties[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/pull/v={version}&aid={artifactId}", method = RequestMethod.PUT)
	@ResponseBody
	public void pullArtifact(@PathVariable long wsid, @PathVariable long version, @PathVariable long artifactId) {
		handler.pullArtifact(wsid, version, artifactId);
	}

	@RequestMapping(value = "/id={wsid}/pull/v={version}&aid={artifactId}/name={propName}", method = RequestMethod.PUT)
	@ResponseBody
	public void pullProperty(@PathVariable long wsid, @PathVariable long version, @PathVariable long artifactId,
			@PathVariable String propName) {
		handler.pullProperty(wsid, version, artifactId, propName);
	}

	@RequestMapping(value = "/id={wsid}/push", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PropagationType> getPush(@PathVariable long wsid) {
		PropagationType push = handler.getPush(wsid);
		return new ResponseEntity<PropagationType>(push, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/push/all", method = RequestMethod.PUT)
	@ResponseBody
	public void pushAll(@PathVariable long wsid) {
		handler.pushAll(wsid);
	}

	@RequestMapping(value = "/id={wsid}/push/v={version}&aid={artifactId}", method = RequestMethod.PUT)
	@ResponseBody
	public void pushArtifact(@PathVariable long wsid, @PathVariable long version, @PathVariable long artifactId) {
		handler.pushArtifact(wsid, version, artifactId);
	}

	@RequestMapping(value = "/id={wsid}/push/v={version}&aid={artifactId}/name={propName}", method = RequestMethod.PUT)
	@ResponseBody
	public void pushProperty(@PathVariable long wsid, @PathVariable long version, @PathVariable long artifactId,
			@PathVariable String propName) {
		handler.pushProperty(wsid, version, artifactId, propName);
	}

	@RequestMapping(value = "/id={wsid}/parent", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PojoWorkspace> getParent(@PathVariable long wsid) {
		PojoWorkspace parent = handler.getParent(wsid);
		return new ResponseEntity<PojoWorkspace>(parent, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/setParent", method = RequestMethod.PUT)
	@ResponseBody
	/* letting newParentId be null allows unsetting the parent */
	public void setParent(@PathVariable long wsid, /*@RequestBody(required = true)*/ Long newParentId) { // to unset parent, null must be supported
		handler.setParent(wsid, newParentId);
	}

	@RequestMapping(value = "/id={wsid}/setPush", method = RequestMethod.PUT)
	@ResponseBody
	public void setPush(@PathVariable long wsid, @RequestBody(required = true) PropagationType type) {
		handler.setPush(wsid, type);
	}

	@RequestMapping(value = "/id={wsid}/setPull", method = RequestMethod.PUT)
	@ResponseBody
	public void setPull(@PathVariable long wsid, @RequestBody(required = true) PropagationType type) {
		handler.setPull(wsid, type);
	}

	@RequestMapping(value = "/id={wsid}/children", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PojoWorkspace[]> getWorkspaceChildren(@PathVariable long wsid) {
		PojoWorkspace[] children = handler.getWorkspaceChildren(wsid);
		return new ResponseEntity<PojoWorkspace[]>(children, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/isClosed", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Boolean> isClosed(@PathVariable final long wsid) {
		Boolean isClosed = handler.isClosed(wsid);
		return new ResponseEntity<Boolean>(isClosed, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/resources/id={id}&v={version}/fullqualifiedname", method = RequestMethod.PUT)
	@ResponseBody
	public void setPropertyValue(@PathVariable long wsid, @PathVariable long id, @PathVariable long version,
			@RequestBody(required = false) String uri) throws WorkspaceExpiredException {
		handler.setResourceFullQualifiedName(wsid, id, version, uri);
	}
	
	@RequestMapping(value = "/id={wsid}/resources/func/create", method = RequestMethod.POST)
	public ResponseEntity<PojoResource> createResource(@PathVariable long wsid,
			@RequestBody(required = true) String fullQualifiedName) {
		PojoResource result = handler.createResource(wsid, fullQualifiedName);
		return new ResponseEntity<PojoResource>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={wsid}/resources/func/create2", method = RequestMethod.POST)
	public ResponseEntity<PojoResource> createResource(@PathVariable long wsid, @RequestBody(required = true) PojoStringPackageProjectArtifacts request) {
		PojoResource result = handler.createResource(wsid, request);
		return new ResponseEntity<PojoResource>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/resources/id={id}", method = RequestMethod.GET)
	public ResponseEntity<PojoResource> getResource(@PathVariable long wsid, @PathVariable long id) {
		PojoResource result = handler.getResource(wsid, id);
		return new ResponseEntity<PojoResource>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={wsid}/resources", method = RequestMethod.GET)
	public ResponseEntity<PojoResource[]> getResources(@PathVariable long wsid) {
		PojoResource[] result = handler.getResources(wsid);
		return new ResponseEntity<PojoResource[]>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/id={wsid}/resources", method = RequestMethod.POST)
	public ResponseEntity<PojoResource[]> getResources(@PathVariable long wsid, @RequestBody(required = true) String fullQualifiedName) {
		PojoResource[] result = handler.getResources(wsid, fullQualifiedName);
		return new ResponseEntity<PojoResource[]>(result, HttpStatus.OK);
	}

	// ------------------------------------
	// Exception handling
	// ------------------------------------

	@ExceptionHandler(WorkspaceEmptyException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public String handleWorkspaceEmptyException() {
		return WorkspaceEmptyException.class.getSimpleName();
	}
	
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

	@ExceptionHandler(PropertyDoesNotExistException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handlePropertyDoesNotExistException() {
		return PropertyDoesNotExistException.class.getSimpleName();
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

	@ExceptionHandler(TypeNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleTypeNotSupportedException() {
		return TypeNotSupportedException.class.getSimpleName();
	}

	@ExceptionHandler(ArtifactDeadException.class)
	@ResponseStatus(value = HttpStatus.GONE)
	@ResponseBody
	public String handleArtifactDeadException() {
		return ArtifactDeadException.class.getSimpleName();
	}

	@ExceptionHandler(PropertyDeadException.class)
	@ResponseStatus(value = HttpStatus.GONE)
	@ResponseBody
	public String handlePropertyDeadException() {
		return PropertyDeadException.class.getSimpleName();
	}

	@ExceptionHandler(ArtifactConflictException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handleArtifactConflictException() {
		return ArtifactConflictException.class.getSimpleName();
	}

	@ExceptionHandler(PropertyConflictException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handlePropertyConflictException() {
		return PropertyConflictException.class.getSimpleName();
	}

	@ExceptionHandler(VersionConflictException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handleVersionConflictException() {
		return VersionConflictException.class.getSimpleName();
	}

	@ExceptionHandler(ArtifactNotCommitableException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handleArtifactNotCommitableException() {
		return ArtifactNotCommitableException.class.getSimpleName();
	}

	@ExceptionHandler(PropertyNotCommitableException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handlePropertyNotCommitableException() {
		return PropertyNotCommitableException.class.getSimpleName();
	}

	@ExceptionHandler(WorkspaceNotEmptyException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handleWorkspaceNotEmptyException() {
		return WorkspaceNotEmptyException.class.getSimpleName();
	}

	@ExceptionHandler(WorkspaceConflictException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handleWorkspaceConflictException() {
		return WorkspaceConflictException.class.getSimpleName();
	}
	
	@ExceptionHandler(WorkspaceCycleException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public String handleWorkspaceCycleException() {
	  return WorkspaceCycleException.class.getSimpleName();
	}

	@ExceptionHandler(PropertyNotPushOrPullableException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handlePropertyNotPushOrPullableException() {
		return PropertyNotPushOrPullableException.class.getSimpleName();
	}

	@ExceptionHandler(ArtifactNotPushOrPullableException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public String handleArtifactNotPushOrPullableException() {
		return ArtifactNotPushOrPullableException.class.getSimpleName();
	}
}
