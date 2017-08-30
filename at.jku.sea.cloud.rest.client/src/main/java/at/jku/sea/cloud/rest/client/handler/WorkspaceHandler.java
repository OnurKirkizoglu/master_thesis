package at.jku.sea.cloud.rest.client.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
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
import at.jku.sea.cloud.exceptions.ArtifactConflictException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.ArtifactNotCommitableException;
import at.jku.sea.cloud.exceptions.ArtifactNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MetaModelDoesNotExistException;
import at.jku.sea.cloud.exceptions.PackageDoesNotExistException;
import at.jku.sea.cloud.exceptions.ProjectDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyConflictException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyNotCommitableException;
import at.jku.sea.cloud.exceptions.PropertyNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.VersionConflictException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceConflictException;
import at.jku.sea.cloud.exceptions.WorkspaceCycleException;
import at.jku.sea.cloud.exceptions.WorkspaceEmptyException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.exceptions.WorkspaceNotEmptyException;
import at.jku.sea.cloud.listeners.WorkspaceListener;
import at.jku.sea.cloud.rest.client.RestResource;
import at.jku.sea.cloud.rest.client.handler.listeners.RestPollWorkspaceListener;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndFilters;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndProperties;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndString;
import at.jku.sea.cloud.rest.pojo.PojoArtifactWithParameters;
import at.jku.sea.cloud.rest.pojo.PojoCollectionArtifact;
import at.jku.sea.cloud.rest.pojo.PojoCollectionArtifactWithParameters;
import at.jku.sea.cloud.rest.pojo.PojoContainer;
import at.jku.sea.cloud.rest.pojo.PojoMapArtifact;
import at.jku.sea.cloud.rest.pojo.PojoMetaModel;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.PojoProject;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoResource;
import at.jku.sea.cloud.rest.pojo.PojoStringPackageProjectArtifacts;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.PojoWorkspace;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class WorkspaceHandler extends AbstractHandler {
  
  private static WorkspaceHandler INSTANCE;
  
  public static WorkspaceHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new WorkspaceHandler();  
    }
    return INSTANCE;
  }
  
  protected WorkspaceHandler() {
    listeners = new HashMap<WorkspaceListener, RestPollWorkspaceListener>();
  }

	Map<WorkspaceListener, RestPollWorkspaceListener> listeners;

	public Owner getOwner(long wsId) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/owner", wsId);
		PojoOwner pojoOwner = template.getForEntity(url, PojoOwner.class).getBody();
		return restFactory.createRest(pojoOwner);
	}

	public Tool getTool(long wsId) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/tool", wsId);
		PojoTool pojoTool = template.getForEntity(url, PojoTool.class).getBody();
		return restFactory.createRest(pojoTool);
	}

	public String getIdentifier(long wsId) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/identifier", wsId);
		String identifier = template.getForEntity(url, String.class).getBody();
		return identifier;
	}

	public Version getBaseVersion(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/baseversion", wsId);
		PojoVersion pojoVersion;
		try {
			pojoVersion = template.getForEntity(url, PojoVersion.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoVersion);
	}

	public Map<Artifact, Set<Property>> rebase(long wsId, Version version) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/func/rebase?v=%d", wsId, version.getVersionNumber());
		PojoArtifactAndProperties[] pojoResult = null;
		try {
			pojoResult = template.postForEntity(url, null, PojoArtifactAndProperties[].class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRestMap(pojoResult);
	}

	public Map<Artifact, Set<Property>> rebaseToHead(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/func/rebase", wsId);
		PojoArtifactAndProperties[] pojoResult = null;
		try {
			pojoResult = template.postForEntity(url, null, PojoArtifactAndProperties[].class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRestMap(pojoResult);
	}

	public Package createPackage(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/func/create", wsId);
		PojoPackage pojoPackage = null;
		try {
			pojoPackage = template.postForEntity(url, null, PojoPackage.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoPackage);
	}
	
	public Package createPackage(long wsId, String name) throws WorkspaceExpiredException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/func/create/name=%s", wsId, name);
    PojoPackage pojoPackage = null;
    try {
      pojoPackage = template.postForEntity(url, null, PojoPackage.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
        throw new WorkspaceExpiredException(wsId);
      }
      throw e;
    }
    return restFactory.createRest(pojoPackage);
  }

	public Package createPackage(long wsId, long pkgId, long pkgVersion) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/id=%d&v=%d/packages/func/create", wsId, pkgId,
				pkgVersion);
		PojoPackage pojoPackage = null;
		try {
			pojoPackage = template.postForEntity(url, null, PojoPackage.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoPackage);
	}
	
	public Package createPackage(long wsId, long pkgId, long pkgVersion, String name) throws WorkspaceExpiredException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/id=%d&v=%d/packages/func/create/name=%s", wsId, pkgId,
        pkgVersion, name);
    PojoPackage pojoPackage = null;
    try {
      pojoPackage = template.postForEntity(url, null, PojoPackage.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
        throw new WorkspaceExpiredException(wsId);
      }
      throw e;
    }
    return restFactory.createRest(pojoPackage);
  }

	public Project createProject(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/projects/func/create", wsId);
		PojoProject pojoProject = null;
		try {
			pojoProject = template.postForEntity(url, null, PojoProject.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoProject);
	}
	
	public Project createProject(long wsId, String name) throws WorkspaceExpiredException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/projects/func/create/name=%s", wsId, name);
    PojoProject pojoProject = null;
    try {
      pojoProject = template.postForEntity(url, null, PojoProject.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
        throw new WorkspaceExpiredException(wsId);
      }
      throw e;
    }
    return restFactory.createRest(pojoProject);
  }

	public Project createProject(long wsId, long pkgId, long pkgVersion) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/id=%d&v=%d/projects/func/create", wsId, pkgId,
				pkgVersion);
		PojoProject pojoProject = null;
		try {
			pojoProject = template.postForEntity(url, null, PojoProject.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoProject);
	}

	public MetaModel createMetaModel(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/metamodels/func/create", wsId);
		PojoMetaModel pojoMetamodel = null;
		try {
			pojoMetamodel = template.postForEntity(url, null, PojoMetaModel.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoMetamodel);
	}

	public MetaModel createMetaModel(long wsId, long pkgId, long pkgVersion) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/id=%d&v=%d/metamodels/func/create", wsId, pkgId,
				pkgVersion);
		PojoMetaModel pojoMetamodel = null;
		try {
			pojoMetamodel = template.postForEntity(url, null, PojoMetaModel.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoMetamodel);
	}

	public Artifact createArtifact(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/func/create", wsId);
		PojoArtifact pojoArtifact = null;
		try {
			pojoArtifact = template.postForEntity(url, null, PojoArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoArtifact);
	}

	public Artifact createArtifact(long wsId, Artifact type) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/func/create", wsId);
		PojoArtifact pojoType = pojoFactory.createPojo(type);
		PojoArtifact pojoArtifact = null;
		try {
			pojoArtifact = template.postForEntity(url, pojoType, PojoArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoArtifact);
	}

	public Artifact createArtifact(long wsId, long pkgId, long pkgVersion) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/id=%d&v=%d/artifacts/func/create", wsId, pkgId,
				pkgVersion);
		PojoArtifact pojoArtifact = null;
		try {
			pojoArtifact = template.postForEntity(url, null, PojoArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoArtifact);
	}

	public Artifact createArtifact(long wsId, long pkgId, long pkgVersion, Artifact type)
			throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/id=%d&v=%d/artifacts/func/create", wsId, pkgId,
				pkgVersion);
		PojoArtifact pojoType = pojoFactory.createPojo(type);
		PojoArtifact pojoArtifact = null;
		try {
			pojoArtifact = template.postForEntity(url, pojoType, PojoArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoArtifact);
	}

	public Artifact createArtifact(long wsId, Artifact type, Container container, MetaModel metamodel, Project project,
			Map<String, Object> properties) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/func/createwithparams", wsId);
		PojoArtifact pojoType = type == null ? null : pojoFactory.createPojo(type);
		PojoContainer pojoContainer = container == null ? null : pojoFactory.createPojo(container);
		PojoMetaModel pojoMetaModel = metamodel == null ? null : pojoFactory.createPojo(metamodel);
		PojoProject pojoProject = project == null ? null : pojoFactory.createPojo(project);
		Map<String, PojoObject> pojoPropertyMap = new HashMap<>();
		if (properties != null) {
			for (Entry<String, Object> entry : properties.entrySet()) {
				pojoPropertyMap.put(entry.getKey(), pojoFactory.createPojo(entry.getValue()));
			}
		}

		PojoArtifactWithParameters pojoArtifactOptions = new PojoArtifactWithParameters(pojoType, pojoContainer,
				pojoMetaModel, pojoProject, pojoPropertyMap);
		PojoArtifact pojoArtifact = null;
		try {
			pojoArtifact = template.postForEntity(url, pojoArtifactOptions, PojoArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}

		return restFactory.createRest(pojoArtifact);
	}

	public CollectionArtifact createCollection(long wsId, boolean containsOnlyArtifacts)
			throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/collectionartifacts/func/create", wsId);
		PojoCollectionArtifact pojoCollectionArtifact = null;
		try {
			pojoCollectionArtifact = template.postForEntity(url, containsOnlyArtifacts, PojoCollectionArtifact.class)
					.getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoCollectionArtifact);
	}

	public CollectionArtifact createCollection(long wsId, long pkgId, long pkgVersion, boolean containsOnlyArtifacts)
			throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/id=%d&v=%d/collectionartifacts/func/create",
				wsId, pkgId, pkgVersion);
		PojoCollectionArtifact pojoCollectionArtifact = null;
		try {
			pojoCollectionArtifact = template.postForEntity(url, containsOnlyArtifacts, PojoCollectionArtifact.class)
					.getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoCollectionArtifact);
	}

	public CollectionArtifact createCollection(long wsId, Package pckg, boolean containsOnlyArtifacts,
			Collection<?> elements, Map<String, Object> properties) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/collectionartifacts/func/createwithparams", wsId);
		PojoPackage pojoPackage = pckg == null ? null : pojoFactory.createPojo(pckg);
		Map<String, PojoObject> pojoPropertyMap = new HashMap<>();
		if (properties != null) {
			for (Entry<String, Object> entry : properties.entrySet()) {
				pojoPropertyMap.put(entry.getKey(), pojoFactory.createPojo(entry.getValue()));
			}
		}
		PojoObject[] pojoElements = pojoFactory.createPojoArray(elements.toArray(new Object[elements.size()]));

		PojoCollectionArtifactWithParameters pojoArtifactOptions = new PojoCollectionArtifactWithParameters(pojoPackage,
				pojoElements, pojoPropertyMap, containsOnlyArtifacts);
		PojoCollectionArtifact pojoArtifact = null;
		try {
			pojoArtifact = template.postForEntity(url, pojoArtifactOptions, PojoCollectionArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(IllegalArgumentException.class.getSimpleName())) {
				final String msg;
				if (containsOnlyArtifacts) {
					msg = "CollectionArtifact should contain only Artifacts but at least one other Type was found to be added";
				} else {
					msg = "CollectionArtifact should contain anything except Artifacts but at least one Artifact was found to be added";
				}
				throw new IllegalArgumentException(msg);
			}
			throw e;
		}

		return restFactory.createRest(pojoArtifact);
	}

	public MapArtifact createMap(long wsId) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/mapartifacts/func/create", wsId);
		PojoMapArtifact response = null;
		try {
			response = template.postForEntity(url, null, PojoMapArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(response);
	}

	public MapArtifact createMap(long wsId, long pkgId, long pkgVersion) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/id=%d&v=%d/mapartifacts/func/create", wsId,
				pkgId, pkgVersion);
		PojoMapArtifact response = null;
		try {
			response = template.postForEntity(url, null, PojoMapArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(response);
	}

	public Artifact getArtifact(long wsId, long id) throws WorkspaceExpiredException, ArtifactDoesNotExistException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d", wsId, id);
		PojoArtifact pojoArtifact = null;
		try {
			pojoArtifact = template.getForEntity(url, PojoArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
				throw new ArtifactDoesNotExistException(wsId, id);
			}
			throw e;
		}
		return restFactory.createRest(pojoArtifact);
	}

	public Object[] getArtifactRepresentation(long version, long id) {
		String url = String.format(ARTIFACT_ADDRESS + "/id=%d&v=%d/representation", id, version);
		try {
			PojoObject[] pobj = template.getForEntity(url, PojoObject[].class).getBody();
			return restFactory.createRestArray(pobj);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
				throw new ArtifactDoesNotExistException(version, id);
			}
			throw e;
		}
	}

	public Collection<Artifact> getArtifacts(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts", wsId);
		Artifact[] artifacts = null;
		try {
			PojoArtifact[] pojoArtifacts = template.getForEntity(url, PojoArtifact[].class).getBody();
			artifacts = restFactory.createRestArray(pojoArtifacts);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return list(artifacts);
	}

	public Collection<Artifact> getArtifacts(long wsId, Set<Long> ids) {
		String url = String.format(CLOUD_ADDRESS + "/artifacts/version=%d/func/getwithids", wsId);
		Artifact[] artifacts = null;
		try {
			PojoArtifact[] pojoArtifacts = template.postForEntity(url, ids, PojoArtifact[].class).getBody();
			artifacts = restFactory.createRestArray(pojoArtifacts);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return list(artifacts);
	}

	public Collection<Artifact> getArtifacts(long wsId, Artifact[] filters) throws WorkspaceExpiredException {
		return getArtifacts(wsId, filters, null);
	}

	public Collection<Artifact> getArtifacts(long wsId, Artifact[] filters, String propertyName, Object propertyValue)
			throws WorkspaceExpiredException {
		Map<String, Object> propertyToValue = new HashMap<String, Object>();
		propertyToValue.put(propertyName, propertyValue);
		return getArtifacts(wsId, filters, propertyToValue);
	}

	public Collection<Artifact> getArtifacts(long wsId, Artifact[] filters, Map<String, Object> propertyToValue)
			throws WorkspaceExpiredException {
		PojoArtifact[] artifactFilters = pojoFactory.createPojoArray(filters);
		PojoPropertyFilter[] propertyFilters = null;
		if (propertyToValue != null) {
			propertyFilters = new PojoPropertyFilter[propertyToValue.size()];
			int i = 0;
			for (Entry<String, Object> entry : propertyToValue.entrySet()) {
				propertyFilters[i] = new PojoPropertyFilter(entry.getKey(), pojoFactory.createPojo(entry.getValue()));
				i++;
			}
		}
		PojoArtifactAndPropertyFilter options = new PojoArtifactAndPropertyFilter(artifactFilters, propertyFilters);
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/func/getwithfilters", wsId);
		Artifact[] artifacts = null;
		try {
			PojoArtifact[] pojoArtifacts = template.postForEntity(url, options, PojoArtifact[].class).getBody();
			artifacts = restFactory.createRestArray(pojoArtifacts);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return list(artifacts);
	}

	public Package getPackage(long wsId, long id) throws WorkspaceExpiredException, PackageDoesNotExistException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages/id=%d", wsId, id);
		PojoPackage pojoPackage = null;
		try {
			pojoPackage = template.getForEntity(url, PojoPackage.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(PackageDoesNotExistException.class.getSimpleName())) {
				throw new PackageDoesNotExistException(wsId, id);
			}
			throw e;
		}
		return restFactory.createRest(pojoPackage);
	}

	public Collection<Package> getPackages(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/packages", wsId);
		Package[] packages = null;
		try {
			PojoPackage[] pojoPackages = template.getForEntity(url, PojoPackage[].class).getBody();
			packages = restFactory.createRestArray(pojoPackages);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return list(packages);
	}

	public Project getProject(long wsId, long id) throws WorkspaceExpiredException, ProjectDoesNotExistException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/projects/id=%d", wsId, id);
		PojoProject pojoProject = null;
		try {
			pojoProject = template.getForEntity(url, PojoProject.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(ProjectDoesNotExistException.class.getSimpleName())) {
				throw new ProjectDoesNotExistException(wsId, id);
			}
			throw e;
		}
		return restFactory.createRest(pojoProject);
	}

	public Collection<Project> getProjects(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/projects", wsId);
		Project[] projects = null;
		try {
			PojoProject[] pojoProjects = template.getForEntity(url, PojoProject[].class).getBody();
			projects = restFactory.createRestArray(pojoProjects);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return list(projects);
	}

	public MetaModel getMetaModel(long wsId, long id) throws WorkspaceExpiredException, MetaModelDoesNotExistException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/metamodels/id=%d", wsId, id);
		PojoMetaModel pojoMetamodel = null;
		try {
			pojoMetamodel = template.getForEntity(url, PojoMetaModel.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(MetaModelDoesNotExistException.class.getSimpleName())) {
				throw new MetaModelDoesNotExistException(wsId, id);
			}
			throw e;
		}
		return restFactory.createRest(pojoMetamodel);
	}

	public Collection<MetaModel> getMetaModels(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/metamodels", wsId);
		MetaModel[] metamodels = null;
		try {
			PojoMetaModel[] pojoMetamodels = template.getForEntity(url, PojoMetaModel[].class).getBody();
			metamodels = restFactory.createRestArray(pojoMetamodels);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return list(metamodels);
	}

	public MetaModel getMetaMetaModel(long wsId, Artifact artifact) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/aid=%d/metametamodel", wsId, artifact.getId());
		MetaModel metamodel = null;
		try {
			PojoMetaModel pojoMetamodel = template.getForEntity(url, PojoMetaModel.class).getBody();
			metamodel = restFactory.createRest(pojoMetamodel);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return metamodel;
	}

	public CollectionArtifact getCollectionArtifact(long wsId, long id)
			throws WorkspaceExpiredException, CollectionArtifactDoesNotExistException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/collectionartifacts/id=%d", wsId, id);
		PojoCollectionArtifact pojoCollectionArtifact = null;
		try {
			pojoCollectionArtifact = template.getForEntity(url, PojoCollectionArtifact.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else
				if (e.getResponseBodyAsString().equals(CollectionArtifactDoesNotExistException.class.getSimpleName())) {
				throw new CollectionArtifactDoesNotExistException(wsId, id);
			}
			throw e;
		}
		return restFactory.createRest(pojoCollectionArtifact);
	}

	public Collection<CollectionArtifact> getCollectionArtifacts(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/collectionartifacts", wsId);
		CollectionArtifact[] collectionArtifacts = null;
		try {
			PojoCollectionArtifact[] pojoCollectionArtifacts = template
					.getForEntity(url, PojoCollectionArtifact[].class).getBody();
			collectionArtifacts = restFactory.createRestArray(pojoCollectionArtifacts);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return list(collectionArtifacts);
	}

	public long commitArtifact(long wsId, long id, String message)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, ArtifactConflictException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d/func/commit", wsId, id);
		Long result = null;
		try {
			result = template.postForEntity(url, message, Long.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(WorkspaceEmptyException.class.getSimpleName())) {
				throw new WorkspaceEmptyException(wsId);
			} else if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
				throw new ArtifactDoesNotExistException(wsId, id);
			} else if (e.getResponseBodyAsString().equals(ArtifactConflictException.class.getSimpleName())) {
				throw new ArtifactConflictException(wsId);
			} else if (e.getResponseBodyAsString().equals(ArtifactNotCommitableException.class.getSimpleName())) {
				throw new ArtifactNotCommitableException(wsId, id);
			}
			throw e;
		}
		return result;
	}

	public void rollbackArtifact(long wsId, long id) throws WorkspaceExpiredException, ArtifactDoesNotExistException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d/func/rollback", wsId, id);
		try {
			template.put(url, null);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
				throw new ArtifactDoesNotExistException(wsId, id);
			}
			throw e;
		}
	}

	public long commitProperty(long wsId, long id, String name, String message)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, PropertyDoesNotExistException,
			PropertyConflictException, PropertyNotCommitableException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d/properties/name=%s/func/commit", wsId,
				id, name);
		Long result = null;
		try {
			result = template.postForEntity(url, message, Long.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
				throw new ArtifactDoesNotExistException(wsId, id);
			} else if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
				throw new PropertyDoesNotExistException(wsId, id, name);
			} else if (e.getResponseBodyAsString().equals(PropertyConflictException.class.getSimpleName())) {
				throw new PropertyConflictException(wsId);
			} else if (e.getResponseBodyAsString().equals(PropertyNotCommitableException.class.getSimpleName())) {
				throw new PropertyNotCommitableException(id, name);
			}
			throw e;
		}
		return result;
	}

	public void rollbackProperty(long wsId, long id, String name)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, PropertyDoesNotExistException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d/properties/name=%s/func/rollback", wsId,
				id, name);
		try {
			template.put(url, null);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
				throw new ArtifactDoesNotExistException(wsId, id);
			} else if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
				throw new PropertyDoesNotExistException(wsId, id, name);
			}
			throw e;
		}
	}

	public long commitAll(long wsId, String message) throws WorkspaceExpiredException, VersionConflictException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/func/commit", wsId);
		Long result = null;
		try {
			result = template.postForEntity(url, message, Long.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(VersionConflictException.class.getSimpleName())) {
				throw new VersionConflictException(wsId);
			} else if (e.getResponseBodyAsString().equals(WorkspaceEmptyException.class.getSimpleName())) {
			  throw new WorkspaceEmptyException(wsId);
			}
			throw e;
		}
		return result;
	}

	public void rollbackAll(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/func/rollback", wsId);
		try {
			template.put(url, null);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(WorkspaceEmptyException.class.getSimpleName())) {
        throw new WorkspaceEmptyException(wsId);
      }
			throw e;
		}
	}

	public Set<Artifact> getArtifactConflicts(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifactconflicts", wsId);
		Artifact[] artifacts = null;
		try {
			PojoArtifact[] pojoArtifacts = template.getForEntity(url, PojoArtifact[].class).getBody();
			artifacts = restFactory.createRestArray(pojoArtifacts);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return set(artifacts);
	}

	public Set<Artifact> getArtifactConflicts(long wsId, long version)
			throws WorkspaceExpiredException, IllegalArgumentException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifactconflicts?v=%d", wsId, version);
		Artifact[] artifacts = null;
		try {
			PojoArtifact[] pojoArtifacts = template.getForEntity(url, PojoArtifact[].class).getBody();
			artifacts = restFactory.createRestArray(pojoArtifacts);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(IllegalArgumentException.class.getSimpleName())) {
				throw new IllegalArgumentException();
			}
			throw e;
		}
		return set(artifacts);
	}

	public Set<Property> getPropertyConflicts(long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/propertyconflicts", wsId);
		Property[] properties = null;
		try {
			PojoProperty[] pojoProperties = template.getForEntity(url, PojoProperty[].class).getBody();
			properties = restFactory.createRestArray(pojoProperties);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return set(properties);
	}

	public Set<Property> getPropertyConflicts(long wsId, long version)
			throws WorkspaceExpiredException, IllegalArgumentException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/propertyconflicts?v=%d", wsId, version);
		Property[] properties = null;
		try {
			PojoProperty[] pojoProperties = template.getForEntity(url, PojoProperty[].class).getBody();
			properties = restFactory.createRestArray(pojoProperties);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(IllegalArgumentException.class.getSimpleName())) {
				throw new IllegalArgumentException();
			}
			throw e;
		}
		return set(properties);
	}

	public void close(long wsId) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/func/close", wsId);

		try {
			template.put(url, null);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			} else if (e.getResponseBodyAsString().equals(WorkspaceNotEmptyException.class.getSimpleName())) {
				throw new WorkspaceNotEmptyException(wsId);
			}
			throw e;
		}
	}

	public void addListener(long wsId, WorkspaceListener listener) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/listener/add?interval=%d", wsId,
				RestPollWorkspaceListener.POLL_INTERVAL);
		long lid = template.postForEntity(url, null, Long.class).getBody();
		RestPollWorkspaceListener pollListener = new RestPollWorkspaceListener(lid, listener);
		listeners.put(listener, pollListener);
		Thread poll = new Thread(pollListener);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> removeListener(wsId, listener)));
		
		poll.start();
	}

	public void removeListener(long wsId, WorkspaceListener listener) {
		RestPollWorkspaceListener pollListener = listeners.remove(listener);
		if (pollListener != null) {
			String url = String.format(WORKSPACE_ADDRESS + "/id=%d/listener/id=%d/remove", wsId,
					pollListener.getListenerId());
			template.delete(url);
			pollListener.stopPolling();
		}
	}

	public Collection<Artifact> getArtifactsWithProperty(long id, String propertyName, Object propertyValue,
			boolean alive, Artifact[] filters) {
		Map<String, Object> props = new HashMap<>();
		props.put(propertyName, propertyValue);

		return getArtifactsWithProperty(id, props, alive, filters);
	}

	public Collection<Artifact> getArtifactsWithProperty(long id, Map<String, Object> propertyToValue, boolean alive,
			Artifact[] filters) {
		PojoPropertyFilter[] pojoFilterArray = pojoFactory.createPojo(propertyToValue);

		PojoArtifactAndPropertyFilter apf = new PojoArtifactAndPropertyFilter(pojoFactory.createPojoArray(filters),
				pojoFilterArray);

		String url = String.format(CLOUD_ADDRESS + "/artifacts/property/version=%d/alive=%b", id, alive);
		PojoArtifact[] art = null;

		try {
			art = template.postForEntity(url, apf, PojoArtifact[].class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(id);
			}
			throw e;
		}

		return list((Artifact[]) restFactory.createRestArray(art));
	}

	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long id, Artifact[] filters) {
		return this.getArtifactsAndPropertyMap(id, null, true, filters);
	}

	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long id, String propertyName,
			Object propertyValue, boolean alive, Artifact[] filters) {
		Map<String, Object> map = new HashMap<>();
		map.put(propertyName, propertyValue);
		return this.getArtifactsAndPropertyMap(id, map, alive, filters);
	}

	public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long id, Map<String, Object> propertyToValue,
			boolean alive, Artifact[] filters) {
		PojoPropertyFilter[] pojoFilterArray = null;
		if (propertyToValue != null) {
			pojoFilterArray = pojoFactory.createPojo(propertyToValue);
		}

		PojoArtifactAndPropertyFilter apf = new PojoArtifactAndPropertyFilter(pojoFactory.createPojoArray(filters),
				pojoFilterArray);
		String url = String.format(CLOUD_ADDRESS + "/artifacts/property/version=%d/alive=%b/mappings", id, alive);
		PojoArtifactAndProperties[] art = null;
		Map<Artifact, Map<String, Object>> map = new HashMap<Artifact, Map<String, Object>>();
		try {
			art = template.postForEntity(url, apf, PojoArtifactAndProperties[].class).getBody();
			for (PojoArtifactAndProperties pojoMappings : art) {
				Map<String, Object> mappings = new HashMap<>();
				if (pojoMappings.getProperties().length != 0) {
					PojoProperty[] pojoProperties = pojoMappings.getProperties();
					for (PojoProperty p : pojoProperties) {
						mappings.put(p.getName(), restFactory.createRest(p.getObject()));
					}
				}
				map.put(restFactory.createRest(pojoMappings.getArtifact()), mappings);
			}
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(VersionDoesNotExistException.class.getSimpleName())) {
				throw new VersionDoesNotExistException(id);
			}
			throw e;
		}

		return map;
	}

	public Map<Artifact, Map<String, Object>> getArtifactsPropertyMap(long id, Collection<Artifact> artifacts,
			Set<String> properties) {
		PojoArtifactAndString pas = new PojoArtifactAndString(
				pojoFactory.createPojoArray(artifacts.toArray(new Artifact[0])), properties);
		String url = String.format(CLOUD_ADDRESS + "/artifacts/property/version=%d/artifactpropertymap", id);
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
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(id);
			}
			throw e;
		}

		return map;
	}

	public Collection<Artifact> getArtifactsWithReference(long id, Artifact artifact, Artifact[] filters) {
		String url = String.format(CLOUD_ADDRESS + "/artifacts/version=%d/func/getwithreference", id);
		Artifact[] artifacts = null;
		PojoArtifactAndFilters paf = new PojoArtifactAndFilters(pojoFactory.createPojo(artifact),
				pojoFactory.createPojoArray(filters));
		try {
			PojoArtifact[] pojoArtifacts = template.postForEntity(url, paf, PojoArtifact[].class).getBody();
			artifacts = restFactory.createRestArray(pojoArtifacts);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(id);
			}
			throw e;
		}
		return list(artifacts);
	}

	public Map<Artifact, Set<Property>> pullAll(final long id) {
		PojoArtifactAndProperties[] art = null;
		Map<Artifact, Set<Property>> map = new HashMap<Artifact, Set<Property>>();
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/pull/all", id);
		try {
			// template.put(url, null);
			art = template.postForEntity(url, null, PojoArtifactAndProperties[].class).getBody();
			for (PojoArtifactAndProperties pojoMappings : art) {
				Artifact artifact = restFactory.createRest(pojoMappings.getArtifact());
				map.put(artifact, new HashSet<Property>());
				if (pojoMappings.getProperties().length != 0) {
					PojoProperty[] pojoProperties = pojoMappings.getProperties();
					for (PojoProperty p : pojoProperties) {
						map.get(artifact).add(restFactory.createRest(p));
					}
				}
			}
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(0);
			} else if (e.getResponseBodyAsString().equals(ArtifactNotPushOrPullableException.class.getSimpleName())) {
				throw new ArtifactNotPushOrPullableException(0);
			} else if (e.getResponseBodyAsString().equals(PropertyNotPushOrPullableException.class.getSimpleName())) {
				throw new PropertyNotPushOrPullableException(0, "");
			}
			throw e;
		}
		return map;
	}

	public void pullProperty(final long id, Property property) {
		try {
			String url = String.format(WORKSPACE_ADDRESS + "/id=%d/pull/v=%d&aid=%d/name=%s", id,
					property.getVersionNumber(), property.getId(), property.getName());
			template.put(url, null);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(PropertyNotPushOrPullableException.class.getSimpleName())) {
				throw new PropertyNotPushOrPullableException(id, property.getName());
			}
			throw e;
		}
	}

	public void pullArtifact(final long id, Artifact artifact) {
		try {
			String url = String.format(WORKSPACE_ADDRESS + "/id=%d/pull/v=%d&aid=%d", id, artifact.getVersionNumber(),
					artifact.getId());
			template.put(url, null);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(ArtifactNotPushOrPullableException.class.getSimpleName())) {
				throw new ArtifactNotPushOrPullableException(id);
			}
			throw e;
		}
	}

	public void pushAll(final long id) {
		try {
			String url = String.format(WORKSPACE_ADDRESS + "/id=%d/push/all", id);
			template.put(url, null);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(0);
			} else if (e.getResponseBodyAsString().equals(ArtifactNotPushOrPullableException.class.getSimpleName())) {
				throw new ArtifactNotPushOrPullableException(0);
			} else if (e.getResponseBodyAsString().equals(PropertyNotPushOrPullableException.class.getSimpleName())) {
				throw new PropertyNotPushOrPullableException(0, "");
			}
			throw e;
		}
	}

	public void pushProperty(final long id, Property property) {
		try {
			String url = String.format(WORKSPACE_ADDRESS + "/id=%d/push/v=%d&aid=%d/name=%s", id,
					property.getVersionNumber(), property.getId(), property.getName());
			template.put(url, null);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(PropertyNotPushOrPullableException.class.getSimpleName())) {
				throw new PropertyNotPushOrPullableException(id, property.getName());
			} else if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(0);
			}
			throw e;
		}
	}

	public void pushArtifact(final long id, Artifact artifact) {
		try {
			String url = String.format(WORKSPACE_ADDRESS + "/id=%d/push/v=%d&aid=%d", id, artifact.getVersionNumber(),
					artifact.getId());
			template.put(url, null);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(ArtifactNotPushOrPullableException.class.getSimpleName())) {
				throw new ArtifactNotPushOrPullableException(artifact.getId());
			} else if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(0);
			}
			throw e;
		}
	}

	public PropagationType getPull(final long id) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/pull", id);
		PropagationType pull = template.getForEntity(url, PropagationType.class).getBody();
		return pull;
	}

	public PropagationType getPush(final long id) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/push", id);
		PropagationType push = template.getForEntity(url, PropagationType.class).getBody();
		return push;
	}

	public Workspace getParent(final long wsId) throws WorkspaceExpiredException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/parent", wsId);
		PojoWorkspace pojoWorkspace = null;
		try {
			pojoWorkspace = template.getForEntity(url, PojoWorkspace.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(wsId);
			}
			throw e;
		}
		return restFactory.createRest(pojoWorkspace);
	}

	public Collection<Workspace> getChildren(long wsId) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/children", wsId);
		PojoWorkspace[] pojoWorkspaces = template.getForEntity(url, PojoWorkspace[].class).getBody();
		return restFactory.createRest(pojoWorkspaces);
	}

	public boolean isClosed(final long wsId) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/isClosed", wsId);
		Boolean isClosed = template.getForEntity(url, Boolean.class).getBody();
		return isClosed;
	}

	public void setParent(long wsId, Long newParentId) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/setParent", wsId);
		try {
			template.put(url, newParentId);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceConflictException.class.getSimpleName())) {
				throw new WorkspaceConflictException(wsId, newParentId);
			} else if (e.getResponseBodyAsString().equals(WorkspaceExpiredException.class.getSimpleName())) {
				throw new WorkspaceExpiredException(0);
			} else if (e.getResponseBodyAsString().equals(WorkspaceCycleException.class.getSimpleName())) {
				throw new WorkspaceCycleException(wsId, newParentId);
			} else if (e.getResponseBodyAsString().equals(IllegalArgumentException.class.getSimpleName())) {
				throw new IllegalArgumentException();
			}
			throw e;
		}
	}

	public void setPush(long wsId, PropagationType type) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/setPush", wsId);
		try {
			template.put(url, type);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(WorkspaceConflictException.class.getSimpleName())) {
				throw new WorkspaceConflictException(wsId, 0);
			}
			throw e;
		}

	}

	public void setPull(long wsId, PropagationType type) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/setPull", wsId, type);
		template.put(url, type);
	}

	public Resource createResource(long wsId, String fullQualifiedName) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/resources/func/create", wsId);
		try {
			PojoResource pojoResult = template.postForEntity(url, fullQualifiedName, PojoResource.class).getBody();
			return restFactory.createRest(pojoResult);
		} catch (HttpClientErrorException e) {
			throw e;
		}
	}

	public Resource createResource(long wsId, String fullQualifiedName, Package pckg, Project project,
			Collection<Artifact> artifacts) {
		PojoStringPackageProjectArtifacts request = new PojoStringPackageProjectArtifacts();
		request.setFullQualifiedName(fullQualifiedName);
		request.setPkg(pojoFactory.createPojo(pckg));
		request.setProject(pojoFactory.createPojo(project));
		request.setArtifacts(pojoFactory.createPojoArray(artifacts.toArray(new Artifact[0])));

		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/resources/func/create", wsId);
		try {
			PojoResource pojoResult = template.postForEntity(url, request, PojoResource.class).getBody();
			return restFactory.createRest(pojoResult);
		} catch (HttpClientErrorException e) {
			throw e;
		}
	}

	public Collection<Resource> getResources(long wsId) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/resources", wsId);
		try {
			PojoResource[] pojoResource = template.getForEntity(url, PojoResource[].class).getBody();
			RestResource[] resources = restFactory.createRestArray(pojoResource);
			return this.<Resource>list(resources);
		} catch (HttpClientErrorException e) {
			throw e;
		}
	}

	public Collection<Resource> getResources(long wsId, String fullQualifiedName) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/resources", wsId);
		try {
			PojoResource[] pojoResource = template.postForEntity(url, fullQualifiedName, PojoResource[].class)
					.getBody();
			RestResource[] resources = restFactory.createRestArray(pojoResource);
			return this.<Resource>list(resources);
		} catch (HttpClientErrorException e) {
			throw e;
		}
	}

	public Resource getResource(long wsId, long id) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/resources/id=%d", wsId, id);
		try {
			PojoResource pojoResource = template.getForEntity(url, PojoResource.class).getBody();
			return restFactory.createRest(pojoResource);
		} catch (HttpClientErrorException e) {
			throw e;
		}
	}
	
	
	private void handleExceptions(HttpClientErrorException httpRestException, Long wsId, Long parentId) throws RuntimeException {
	  String body = httpRestException.getResponseBodyAsString();
	  
	  dis:
	  if (body == null || body.isEmpty()) {
	    break dis;
	  } else {
	    if (body.equals(WorkspaceConflictException.class.getSimpleName())) {
	      throw new WorkspaceConflictException(wsId, parentId);
	    }
	  }
	  
	  throw httpRestException;
	}
}
