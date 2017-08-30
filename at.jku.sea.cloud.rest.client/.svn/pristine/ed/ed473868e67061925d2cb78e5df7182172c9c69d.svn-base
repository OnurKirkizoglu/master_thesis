package at.jku.sea.cloud.rest.client.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.listeners.CloudListener;
import at.jku.sea.cloud.rest.client.handler.listeners.RestPollCloudListener;
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
import at.jku.sea.cloud.rest.pojo.PojoOwnerToolStringLongParentPushPull;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.PojoProject;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoResource;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.PojoWorkspace;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class CloudHandler extends AbstractHandler {

	private Map<CloudListener, RestPollCloudListener> listeners;

  private static CloudHandler INSTANCE = null;

  public static CloudHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CloudHandler();
    }
    
    return INSTANCE;
  }
  
  protected CloudHandler() {
    listeners = new HashMap<CloudListener, RestPollCloudListener>();
  }
	
	public void addListener(CloudListener listener) {
		String url = CLOUD_ADDRESS + "/listener/add";
		long lid = template.postForEntity(url, null, Long.class).getBody();
		RestPollCloudListener pollListener = new RestPollCloudListener(lid, listener);
		listeners.put(listener, pollListener);
		Thread pollThread = new Thread(pollListener);
		
		// System.exit calls should remove the listener from the cloud
		Runtime.getRuntime().addShutdownHook(new Thread(() -> removeListener(listener)));
		
		pollThread.start();
	}

	public void removeListener(CloudListener listener) {
		RestPollCloudListener poller = listeners.get(listener);

		if (poller != null) {
			String url = String.format(CLOUD_ADDRESS + "/listener/id=%d/remove", poller.getListenerId());
			template.delete(url);
			poller.stopPolling();
		}
	}

	public Owner getOwner(long id) throws OwnerDoesNotExistException {
		String url = String.format(OWNER_ADDRESS + "/id=%d", id);
		PojoOwner pojoOwner = null;
		try {
			pojoOwner = template.getForEntity(url, PojoOwner.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(OwnerDoesNotExistException.class.getSimpleName())) {
				throw new OwnerDoesNotExistException(id);
			}
			throw e;
		}
		return restFactory.createRest(pojoOwner);
	}

	public Tool getTool(long id) throws ToolDoesNotExistException {
		String url = String.format(TOOL_ADDRESS + "/id=%d", id);
		PojoTool pojoTool = null;
		try {
			pojoTool = template.getForEntity(url, PojoTool.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(ToolDoesNotExistException.class.getSimpleName())) {
				throw new ToolDoesNotExistException(id);
			}
			throw e;
		}
		return restFactory.createRest(pojoTool);
	}

	// PojoOwner pojoOwner = pojoFactory.createPojo(owner);
	// PojoTool pojoTool = pojoFactory.createPojo(tool);
	// PojoOwnerToolStringLong options = new PojoOwnerToolStringLong(pojoOwner,
	// pojoTool, identifier, null);
	// String url = WORKSPACE_ADDRESS + "/func/getorcreate";
	// PojoWorkspace pojoWorkspace = null;
	// try {
	// pojoWorkspace = template.postForEntity(url, options,
	// PojoWorkspace.class).getBody();
	// } catch (HttpClientErrorException e) {
	// if
	// (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName()))
	// {
	// throw new WorkspaceExpiredException(0);
	// }
	// throw e;
	// }
	// return restFactory.createRest(pojoWorkspace);
	public Workspace getWorkspace(Owner owner, Tool tool, String identifier) throws WorkspaceExpiredException {
		return this.getWorkspace(owner, tool, identifier, null);
	}

	public Workspace getWorkspace(Owner owner, Tool tool, String identifier, Long baseVersion)
			throws IllegalArgumentException, WorkspaceExpiredException {
		PojoOwner pojoOwner = pojoFactory.createPojo(owner);
		PojoTool pojoTool = pojoFactory.createPojo(tool);
		PojoOwnerToolStringLong options = new PojoOwnerToolStringLong(pojoOwner, pojoTool, identifier, baseVersion);
		String url = CLOUD_ADDRESS + "/workspaces/func/get";
		PojoWorkspace pojoWorkspace = null;
		try {
			pojoWorkspace = template.postForEntity(url, options, PojoWorkspace.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(IllegalArgumentException.class.getSimpleName())) {
				throw new IllegalArgumentException();
			}
			throw e;
		}
		return restFactory.createRest(pojoWorkspace);
	}

	public Workspace getWorkspace(long privateVersionNumber) {
		String url = String.format(CLOUD_ADDRESS + "/workspaces/id=%d", privateVersionNumber);
		PojoWorkspace ws;
		try {
			ws = template.getForEntity(url, PojoWorkspace.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(privateVersionNumber);
			}
			throw e;
		}
		return restFactory.createRest(ws);
	}

	public Workspace createWorkspace(Owner owner, Tool tool, String identifier) throws WorkspaceExpiredException {
		return this.createWS(owner, tool, identifier, null, null, PropagationType.triggered, PropagationType.triggered);
	}

	public Workspace createWorkspace(Owner owner, Tool tool, String identifier, PropagationType push,
			PropagationType pull) throws WorkspaceExpiredException {
		return this.createWS(owner, tool, identifier, null, null, push, pull);
	}

	public Workspace createWorkspace(Owner owner, Tool tool, String identifier, long baseVersion, PropagationType push,
			PropagationType pull) throws WorkspaceExpiredException {
		return this.createWS(owner, tool, identifier, baseVersion, null, push, pull);
	}

	public Workspace createWorkspace(Owner owner, Tool tool, String identifier, Workspace parent, PropagationType push,
			PropagationType pull) throws WorkspaceExpiredException {
		return this.createWS(owner, tool, identifier, null, parent, push, pull);
	}

	private Workspace createWS(Owner owner, Tool tool, String identifier, Long baseVersion, Workspace parent,
			PropagationType push, PropagationType pull) {
		PojoOwner pojoOwner = pojoFactory.createPojo(owner);
		PojoTool pojoTool = pojoFactory.createPojo(tool);
		Long parentId = parent == null ? null : parent.getId();
		PojoOwnerToolStringLongParentPushPull options = new PojoOwnerToolStringLongParentPushPull(pojoOwner, pojoTool,
				identifier, baseVersion, parentId, push, pull);
		String url = WORKSPACE_ADDRESS + "/func/create";
		PojoWorkspace pojoWorkspace = null;
		try {
			pojoWorkspace = template.postForEntity(url, options, PojoWorkspace.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(0); // FIXME: What id ?
			}
			throw e;
		}
		return restFactory.createRest(pojoWorkspace);
	}

	public Version getVersion(long version) throws VersionDoesNotExistException {
		String url = String.format(VERSION_ADDRESS + "/v=%d", version);
		PojoVersion pojoVersion = null;
		try {
			pojoVersion = template.getForEntity(url, PojoVersion.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(version);
			}
			throw e;
		}
		return restFactory.createRest(pojoVersion);
	}

	public Artifact getArtifact(long version, long id) {
		String url = String.format(ARTIFACT_ADDRESS + "/id=%d&v=%d", id, version);
		PojoArtifact art = template.getForEntity(url, PojoArtifact.class).getBody();
		return restFactory.createRest(art);
	}

	public long getHeadVersionNumber() {
		String url = String.format(VERSION_ADDRESS + "/head");
		return template.getForEntity(url, Long.class).getBody();
	}

	public List<Version> getAllVersions() {
		String url = CLOUD_ADDRESS + "/versions";
		PojoVersion[] versions = template.getForEntity(url, PojoVersion[].class).getBody();
		return list(restFactory.createRestArray(versions));
	}

	public Collection<MetaModel> getMetaModels(long versionNumber) {
		String url = String.format(METAMODEL_ADDRESS + "/version=%d", versionNumber);
		PojoMetaModel[] models = template.getForEntity(url, PojoMetaModel[].class).getBody();
		MetaModel[] a = restFactory.createRestArray(models);
		return list(a);
	}

	public Collection<Package> getPackages(long versionNumber) {
		String url = String.format(PACKAGE_ADDRESS + "/version=%d", versionNumber);
		PojoPackage[] pkg = template.getForEntity(url, PojoPackage[].class).getBody();
		return list((Package[]) restFactory.createRestArray(pkg));
	}

	public Collection<Project> getProjects(long versionNumber) {
		String url = String.format(PROJECT_ADDRESS + "/version=%d", versionNumber);
		PojoProject[] prj = template.getForEntity(url, PojoProject[].class).getBody();
		return list((Project[]) restFactory.createRestArray(prj));
	}

	public List<Workspace> getWorkspaces() {
		PojoWorkspace[] ws = template.getForEntity(WORKSPACE_ADDRESS, PojoWorkspace[].class).getBody();
		return list((Workspace[]) restFactory.createRestArray(ws));
	}

	public Collection<Artifact> getArtifacts(long versionNumber, Artifact[] filter) {
		String url = String.format(ARTIFACT_ADDRESS + "/version=%d", versionNumber);
		PojoArtifact[] filters = pojoFactory.createPojoArray(filter);

		PojoArtifact[] art = template.postForEntity(url, filters, PojoArtifact[].class).getBody();
		return list((Artifact[]) restFactory.createRestArray(art));
	}

	public Collection<Artifact> getArtifacts(long versionNumber) {
		String url = String.format(ARTIFACT_ADDRESS + "/version=%d", versionNumber);
		PojoArtifact[] art = template.getForEntity(url, PojoArtifact[].class).getBody();
		return list((Artifact[]) restFactory.createRestArray(art));
	}

	public Owner createOwner() {
		String url = CLOUD_ADDRESS + "/owners/func/create";
		PojoOwner owner = template.postForEntity(url, null, PojoOwner.class).getBody();
		return restFactory.createRest(owner);
	}

//	public Tool createTool() {
//		String url = CLOUD_ADDRESS + "/tools/func/create";
//		PojoTool tool = template.postForEntity(url, null, PojoTool.class).getBody();
//		return restFactory.createRest(tool);
//	}

	public Collection<Artifact> getHeadArtifacts() {
		String url = CLOUD_ADDRESS + "/headartifacts";
		PojoArtifact[] arts = template.getForEntity(url, PojoArtifact[].class).getBody();
		return list((Artifact[]) restFactory.createRestArray(arts));
	}

	public Collection<Artifact> getHeadArtifacts(Artifact... filters) {
		String url = CLOUD_ADDRESS + "/headartifacts";
		PojoArtifact[] pojoFilters = pojoFactory.createPojoArray(filters);

		PojoArtifact[] art = template.postForEntity(url, pojoFilters, PojoArtifact[].class).getBody();
		return list((Artifact[]) restFactory.createRestArray(art));
	}

	public Collection<Artifact> getArtifactsWithProperty(long version, String propertyName, Object propertyValue,
			Artifact[] filters) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(propertyName, propertyValue);

		return getArtifactWithProperty(version, map, filters);
	}

	public Collection<Artifact> getArtifactWithProperty(long version, Map<String, Object> propertyToValue,
			Artifact[] filters) {

		PojoPropertyFilter[] pojoFilterArray = pojoFactory.createPojo(propertyToValue);

		PojoArtifactAndPropertyFilter apf = new PojoArtifactAndPropertyFilter(pojoFactory.createPojoArray(filters),
				pojoFilterArray);

		String url = String.format(CLOUD_ADDRESS + "/artifacts/property/version=%d", version);
		PojoArtifact[] art = template.postForEntity(url, apf, PojoArtifact[].class).getBody();
		return list((Artifact[]) restFactory.createRestArray(art));
	}

	public Collection<Artifact> getHeadArtifactsWithProperty(Map<String, Object> propertyToObject, Artifact[] filters) {
		PojoPropertyFilter[] pfa = new PojoPropertyFilter[propertyToObject.size()];

		int i = 0;
		for (Entry<String, Object> kvp : propertyToObject.entrySet()) {
			PojoObject object = new PojoObject(kvp.getValue());
			pfa[i] = new PojoPropertyFilter(kvp.getKey(), object);
			i++;
		}

		PojoArtifactAndPropertyFilter apf = new PojoArtifactAndPropertyFilter(pojoFactory.createPojoArray(filters),
				pfa);
		String url = CLOUD_ADDRESS + "/headartifacts/version=%d";
		PojoArtifact[] art = template.postForEntity(url, apf, PojoArtifact[].class).getBody();

		return list((Artifact[]) restFactory.createRestArray(art));
	}

	public Collection<Artifact> getHeadArtifactsWithProperty(String propertyName, Object propertyValue,
			Artifact[] filters) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(propertyName, propertyValue);
		return getHeadArtifactsWithProperty(map, filters);
	}

	public Collection<Property> getPropertiesByReference(long version, Artifact artifact) {
		String url = String.format(CLOUD_ADDRESS + "/properties/version=%d", version);
		PojoArtifact art = pojoFactory.createPojo(artifact);

		PojoProperty[] arts = template.postForEntity(url, art, PojoProperty[].class).getBody();
		return list((Property[]) restFactory.createRestArray(arts));
	}

	public Package getPackage(long version, long id) {
		String url = String.format(CLOUD_ADDRESS + "/packages/version=%d/id=%d", version, id);
		PojoPackage pkg = template.getForEntity(url, PojoPackage.class).getBody();
		return restFactory.createRest(pkg);
	}

	public Collection<Package> getHeadPackages() {
		String url = CLOUD_ADDRESS + "/headpackages";
		PojoPackage[] pkgs = template.getForEntity(url, PojoPackage[].class).getBody();
		return list((Package[]) restFactory.createRestArray(pkgs));
	}

	public Project getProject(long version, long id) {
		String url = String.format(CLOUD_ADDRESS + "/projects/version=%d/id=%d", version, id);
		PojoProject prj = template.getForEntity(url, PojoProject.class).getBody();
		return restFactory.createRest(prj);
	}

	public Collection<Project> getHeadProjects() {
		String url = CLOUD_ADDRESS + "/headprojects";
		PojoProject[] prjs = template.getForEntity(url, PojoProject[].class).getBody();
		return list((Project[]) restFactory.createRestArray(prjs));
	}

	public MetaModel getMetaModel(long version, long id) {
		String url = String.format(CLOUD_ADDRESS + "/metamodels/version=%d/id=%d", version, id);
		PojoMetaModel mm = template.getForEntity(url, PojoMetaModel.class).getBody();
		return restFactory.createRest(mm);
	}

	public Collection<MetaModel> getHeadMetaModels() {
		String url = CLOUD_ADDRESS + "/headmetamodels";
		PojoMetaModel[] mm = template.getForEntity(url, PojoMetaModel[].class).getBody();
		return list((MetaModel[]) restFactory.createRestArray(mm));
	}

	public CollectionArtifact getCollectionArtifact(long version, long id) {
		String url = String.format(CLOUD_ADDRESS + "/collectionartifacts/version=%d/id=%d", version, id);
		PojoCollectionArtifact cart = template.getForEntity(url, PojoCollectionArtifact.class).getBody();
		return restFactory.createRest(cart);
	}

	public Collection<CollectionArtifact> getCollectionArtifacts(long version) {
		String url = String.format(CLOUD_ADDRESS + "/collectionartifacts/version=%d", version);
		PojoCollectionArtifact[] carts = template.getForEntity(url, PojoCollectionArtifact[].class).getBody();
		return list((CollectionArtifact[]) restFactory.createRestArray(carts));
	}

	public MapArtifact getMapArtifact(long version, long id) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d", id, version);
		PojoMapArtifact response = null;
		try {
			response = template.getForEntity(url, PojoMapArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(MapArtifactDoesNotExistException.class.getSimpleName())) {
				throw new MapArtifactDoesNotExistException(version, id);
			}
			if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(version);
			}
			throw e;
		}
		return restFactory.createRest(response);
	}

	public Collection<MapArtifact> getMapArtifacts(long version) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/v=%d", version);
		PojoMapArtifact[] response = null;
		try {
			response = template.getForEntity(url, PojoMapArtifact[].class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(version);
			}
			throw e;
		}
		return list((MapArtifact[]) restFactory.createRestArray(response));
	}

	public Collection<CollectionArtifact> getHeadCollectionArtifacts() {
		String url = CLOUD_ADDRESS + "/headcollectionartifacts";
		PojoCollectionArtifact[] carts = template.getForEntity(url, PojoCollectionArtifact[].class).getBody();
		return list((CollectionArtifact[]) restFactory.createRestArray(carts));
	}

	public List<Version> getPrivateVersions() {
		String url = CLOUD_ADDRESS + "/privateversions";
		PojoVersion[] versions = template.getForEntity(url, PojoVersion[].class).getBody();
		return list((Version[]) restFactory.createRestArray(versions));
	}

	public List<Version> getArtifactHistoryVersionNumbers(Artifact artifact) {
		String url = CLOUD_ADDRESS + "/artifacts/history";
		PojoVersion[] art = template.postForEntity(url, pojoFactory.createPojo(artifact), PojoVersion[].class)
				.getBody();
		return list((Version[]) restFactory.createRestArray(art));
	}

	public List<Version> getPropertyHistoryVersionNumbers(Artifact artifact, String name) {
		String url = CLOUD_ADDRESS + "/properties/history/name=" + name;
		PojoVersion[] versions = template.postForEntity(url, pojoFactory.createPojo(artifact), PojoVersion[].class)
				.getBody();
		return list((Version[]) restFactory.createRestArray(versions));
	}

	public Set<Artifact> getArtifactDiffs(long version, long previousVersion) {
		String url = String.format(CLOUD_ADDRESS + "/artifacts/diff/version=%d/previousVersion=%d", version,
				previousVersion);

		PojoArtifact[] art = template.getForEntity(url, PojoArtifact[].class).getBody();
		return new HashSet<Artifact>(Arrays.asList((Artifact[]) restFactory.createRestArray(art)));
	}

	public Map<Artifact, Set<Property>> getDiffs(long version) {
		String url = String.format(CLOUD_ADDRESS + "/artifacts/diff/version=%d", version);
		PojoArtifactAndProperties[] entries = template.getForEntity(url, PojoArtifactAndProperties[].class).getBody();

		return restFactory.createRestMap(entries);
	}

	public Map<Artifact, Set<Property>> getDiffs(long version, long previousVersion) {
		String url = String.format(CLOUD_ADDRESS + "/diff/version=%d/previousVersion=%d", version, previousVersion);
		PojoArtifactAndProperties[] entries = template.getForEntity(url, PojoArtifactAndProperties[].class).getBody();

		return restFactory.createRestMap(entries);
	}

	public boolean isWorkspaceEmpty(long versionNumber) {
		String url = String.format(CLOUD_ADDRESS + "/workspaces/version=%d/isempty", versionNumber);
		return template.getForEntity(url, Boolean.class).getBody();
	}

	public Object[] getArtifactRepresentation(long version, long id) {
		String url = String.format(CLOUD_ADDRESS + "/artifacts/version=%d/id=%d/representation", version, id);

		PojoObject[] pobj = template.getForEntity(url, PojoObject[].class).getBody();

		return restFactory.createRestArray(pobj);
	}

	public Collection<Artifact> getArtifactsWithProperty(long version, Map<String, Object> propertyToValue,
			boolean alive, Artifact[] filters) {
		PojoPropertyFilter[] pojoFilterArray = pojoFactory.createPojo(propertyToValue);

		PojoArtifactAndPropertyFilter apf = new PojoArtifactAndPropertyFilter(pojoFactory.createPojoArray(filters),
				pojoFilterArray);

		String url = String.format(CLOUD_ADDRESS + "/artifacts/property/version=%d/alive=%b", version, alive);
		PojoArtifact[] art = null;

		try {
			art = template.postForEntity(url, apf, PojoArtifact[].class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(version);
			}
			throw e;
		}

		return list((Artifact[]) restFactory.createRestArray(art));
	}

	public Collection<Artifact> getArtifactsWithProperty(long version, String propertyName, Object propertyValue,
			boolean alive, Artifact[] filters) {
		Map<String, Object> props = new HashMap<>();
		props.put(propertyName, propertyValue);

		return getArtifactsWithProperty(version, props, alive, filters);
	}

	public Collection<Artifact> getHeadArtifactsWithProperty(Map<String, Object> propertyToObject, boolean alive,
			Artifact[] filters) {
		PojoPropertyFilter[] pojoFilterArray = pojoFactory.createPojo(propertyToObject);

		PojoArtifactAndPropertyFilter apf = new PojoArtifactAndPropertyFilter(pojoFactory.createPojoArray(filters),
				pojoFilterArray);

		String url = String.format(CLOUD_ADDRESS + "/headartifacts/property/alive=%b", alive);
		PojoArtifact[] art = null;

		art = template.postForEntity(url, apf, PojoArtifact[].class).getBody();

		return list((Artifact[]) restFactory.createRestArray(art));
	}

	public Collection<Artifact> getHeadArtifactsWithProperty(String propertyName, Object propertyValue, boolean alive,
			Artifact[] filters) {
		Map<String, Object> props = new HashMap<>();
		props.put(propertyName, propertyValue);
		return getHeadArtifactsWithProperty(props, alive, filters);
	}

	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long version, Artifact[] filters) {
		return this.getArtifactsAndPropertyMap(version, null, true, filters);
	}

	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long version, String propertyName,
			Object propertyValue, boolean alive, Artifact[] filters) {
		Map<String, Object> map = new HashMap<>();
		map.put(propertyName, propertyValue);
		return this.getArtifactsAndPropertyMap(version, map, alive, filters);
	}

	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long version,
			Map<String, Object> propertyToValue, boolean alive, Artifact[] filters) {
		PojoPropertyFilter[] pojoFilterArray = pojoFactory.createPojo(propertyToValue);

		PojoArtifactAndPropertyFilter apf = new PojoArtifactAndPropertyFilter(pojoFactory.createPojoArray(filters),
				pojoFilterArray);

		String url = String.format(CLOUD_ADDRESS + "/artifacts/property/version=%d/alive=%b/mappings", version, alive);
		PojoArtifactAndProperties[] art = null;
		Map<Artifact, Map<String, Object>> map = new HashMap<Artifact, Map<String, Object>>();
		try {
			art = template.postForEntity(url, apf, PojoArtifactAndProperties[].class).getBody();
			for (PojoArtifactAndProperties pojoMappings : art) {
				PojoProperty[] pojoProperties = pojoMappings.getProperties();
				Map<String, Object> mappings = new HashMap<>();
				for (PojoProperty p : pojoProperties) {
					mappings.put(p.getName(), restFactory.createRest(p.getObject()));
				}
				map.put(restFactory.createRest(pojoMappings.getArtifact()), mappings);
			}
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(version);
			}
			throw e;
		}

		return map;
	}

	public Map<Artifact, Map<String, Object>> getArtifactPropertyMap(long version, Set<Artifact> artifacts,
			Set<String> properties) {
		PojoArtifactAndString pas = new PojoArtifactAndString(
				pojoFactory.createPojoArray(artifacts.toArray(new Artifact[0])), properties);

		String url = String.format(CLOUD_ADDRESS + "/artifacts/property/version=%d/artifactpropertymap", version);
		PojoArtifactAndProperties[] art = null;
		Map<Artifact, Map<String, Object>> map = new HashMap<Artifact, Map<String, Object>>();
		try {
			art = template.postForEntity(url, pas, PojoArtifactAndProperties[].class).getBody();
			for (PojoArtifactAndProperties pojoMappings : art) {
				PojoProperty[] pojoProperties = pojoMappings.getProperties();
				Map<String, Object> mappings = new HashMap<>();
				for (PojoProperty p : pojoProperties) {
					mappings.put(p.getName(), restFactory.createRest(p.getObject()));
				}
				map.put(restFactory.createRest(pojoMappings.getArtifact()), mappings);
			}
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(version);
			}
			throw e;
		}

		return map;
	}

	public Collection<Artifact> getArtifacts(long version, Set<Long> ids) {
		String url = String.format(CLOUD_ADDRESS + "/artifacts/version=%d/func/getwithids", version);
		Artifact[] artifacts = null;
		try {
			PojoArtifact[] pojoArtifacts = template.postForEntity(url, ids, PojoArtifact[].class).getBody();
			artifacts = restFactory.createRestArray(pojoArtifacts);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(version);
			} else if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(version);
			}
			throw e;
		}
		return list(artifacts);
	}

	public Collection<Artifact> getArtifactsWithReference(long version, Artifact artifact, Artifact[] filters) {
		String url = String.format(CLOUD_ADDRESS + "/artifacts/version=%d/func/getwithreference", version);
		Artifact[] artifacts = null;
		PojoArtifactAndFilters paf = new PojoArtifactAndFilters(pojoFactory.createPojo(artifact),
				pojoFactory.createPojoArray(filters));
		try {
			PojoArtifact[] pojoArtifacts = template.postForEntity(url, paf, PojoArtifact[].class).getBody();
			artifacts = restFactory.createRestArray(pojoArtifacts);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(version);
			} else if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(version);
			}
			throw e;
		}
		return list(artifacts);
	}

	public Collection<Workspace> getWorkspaceChildren(Long privateVersion) {
		String url = CLOUD_ADDRESS + "/workspaces/children";

		if (privateVersion != null)
			url += String.format("?privateVersion=%d", privateVersion);

		PojoWorkspace[] wss = null;

		try {
			wss = template.getForEntity(url, PojoWorkspace[].class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName()))
				throw new WorkspaceExpiredException(privateVersion.longValue());
			throw e;
		}

		return list((Workspace[]) restFactory.createRestArray(wss));
	}

	public Resource getResource(long version, long id) {
		String url = String.format(CLOUD_ADDRESS + "/resources/version=%d,id=%d", version, id);
		PojoResource resource = template.getForEntity(url, PojoResource.class).getBody();
		return restFactory.createRest(resource);
	}

	public Collection<Resource> getResources(long version) {
		String url = String.format(CLOUD_ADDRESS + "/resources/version=%d", version);
		PojoResource[] art = template.getForEntity(url, PojoResource[].class).getBody();
		return list((Resource[]) restFactory.createRestArray(art));
	}

	public Collection<Resource> getResource(long version, String fullQualifiedName) {
		String url = String.format(CLOUD_ADDRESS + "/resources/version=%d", version);
		PojoResource[] art = template.postForEntity(url, fullQualifiedName, PojoResource[].class).getBody();
		return list((Resource[]) restFactory.createRestArray(art));
	}

  public Project createProject(String name) {
    String url = String.format(CLOUD_ADDRESS + "/projects/func/create", name);
    PojoProject proj = template.postForEntity(url, name, PojoProject.class).getBody();
    return restFactory.createRest(proj);
  }

	
}
