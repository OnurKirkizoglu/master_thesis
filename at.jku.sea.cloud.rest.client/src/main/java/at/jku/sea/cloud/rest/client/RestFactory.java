package at.jku.sea.cloud.rest.client;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactAliveSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactChange;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCommited;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactContainerSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCreated;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactRollBack;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactTypeSet;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElement;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElements;
import at.jku.sea.cloud.listeners.events.artifact.CollectionRemovedElement;
import at.jku.sea.cloud.listeners.events.artifact.MapCleared;
import at.jku.sea.cloud.listeners.events.artifact.MapElementRemoved;
import at.jku.sea.cloud.listeners.events.artifact.MapPut;
import at.jku.sea.cloud.listeners.events.collection.CollectionChange;
import at.jku.sea.cloud.listeners.events.property.PropertyAliveSet;
import at.jku.sea.cloud.listeners.events.property.PropertyChange;
import at.jku.sea.cloud.listeners.events.property.PropertyCommited;
import at.jku.sea.cloud.listeners.events.property.PropertyMapSet;
import at.jku.sea.cloud.listeners.events.property.PropertyRollBack;
import at.jku.sea.cloud.listeners.events.property.PropertyValueSet;
import at.jku.sea.cloud.listeners.events.workspace.VersionCommited;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceAdded;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceChange;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceClosed;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceParentSet;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRebased;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRollBack;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndProperties;
import at.jku.sea.cloud.rest.pojo.PojoCollectionArtifact;
import at.jku.sea.cloud.rest.pojo.PojoContainer;
import at.jku.sea.cloud.rest.pojo.PojoMapArtifact;
import at.jku.sea.cloud.rest.pojo.PojoMetaModel;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.PojoProject;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPublicVersion;
import at.jku.sea.cloud.rest.pojo.PojoResource;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.PojoUser;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.PojoWorkspace;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoArtifactAliveSet;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoArtifactChange;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoArtifactCommited;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoArtifactContainerSet;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoArtifactCreated;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoArtifactRollBack;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoArtifactTypeSet;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoCollectionAddedElement;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoCollectionAddedElements;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoCollectionRemovedElement;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoMapCleared;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoMapElementRemoved;
import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoMapPut;
import at.jku.sea.cloud.rest.pojo.listener.collection.PojoCollectionChange;
import at.jku.sea.cloud.rest.pojo.listener.property.PojoPropertyAliveSet;
import at.jku.sea.cloud.rest.pojo.listener.property.PojoPropertyChange;
import at.jku.sea.cloud.rest.pojo.listener.property.PojoPropertyCommited;
import at.jku.sea.cloud.rest.pojo.listener.property.PojoPropertyMapSet;
import at.jku.sea.cloud.rest.pojo.listener.property.PojoPropertyRollBack;
import at.jku.sea.cloud.rest.pojo.listener.property.PojoPropertyValueSet;
import at.jku.sea.cloud.rest.pojo.listener.workspace.PojoVersionCommited;
import at.jku.sea.cloud.rest.pojo.listener.workspace.PojoWorkspaceAdded;
import at.jku.sea.cloud.rest.pojo.listener.workspace.PojoWorkspaceChange;
import at.jku.sea.cloud.rest.pojo.listener.workspace.PojoWorkspaceClosed;
import at.jku.sea.cloud.rest.pojo.listener.workspace.PojoWorkspaceParentSet;
import at.jku.sea.cloud.rest.pojo.listener.workspace.PojoWorkspaceRebased;
import at.jku.sea.cloud.rest.pojo.listener.workspace.PojoWorkspaceRollBack;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestFactory {

	private static RestFactory instance;

	public static RestFactory getInstance() {
		if (instance == null) {
			instance = new RestFactory();
		}
		return instance;
	}

	private RestFactory() {
	}

	public RestWorkspace createRest(PojoWorkspace pojoWorkspace) {
		if (pojoWorkspace != null) {
			return new RestWorkspace(pojoWorkspace.getId(), pojoWorkspace.getVersion());
		} else {
			return null;
		}
	}

	public RestArtifact createRest(PojoArtifact pojoArtifact) {
		RestArtifact result;

		if (pojoArtifact instanceof PojoProject) {
			result = createRest((PojoProject) pojoArtifact);
		} else if (pojoArtifact instanceof PojoMetaModel) {
			result = createRest((PojoMetaModel) pojoArtifact);
		} else if (pojoArtifact instanceof PojoOwner) {
			result = createRest((PojoOwner) pojoArtifact);
		} else if (pojoArtifact instanceof PojoContainer) {
			result = createRest((PojoContainer) pojoArtifact);
		} else if (pojoArtifact instanceof PojoCollectionArtifact) {
			result = createRest((PojoCollectionArtifact) pojoArtifact);
		} else if (pojoArtifact instanceof PojoTool) {
			result = createRest((PojoTool) pojoArtifact);
		} else if (pojoArtifact instanceof PojoMapArtifact) {
			result = createRest((PojoMapArtifact) pojoArtifact);
		} else {
			result = new RestArtifact(pojoArtifact.getId(), pojoArtifact.getVersion());
		}

		return result;
	}

	public RestContainer createRest(PojoContainer pojoContainer) {
		RestContainer result;

		if (pojoContainer instanceof PojoPackage) {
			result = createRest((PojoPackage) pojoContainer);
		} else if (pojoContainer instanceof PojoResource) {
			result = createRest((PojoResource) pojoContainer);
		} else {
			result = new RestContainer(pojoContainer.getId(), pojoContainer.getVersion());
		}

		return result;
	}

	public RestProperty createRest(PojoProperty pojoProperty) {
		return new RestProperty(pojoProperty.getId(), pojoProperty.getVersion(), pojoProperty.getName());
	}

	public RestVersion createRest(PojoVersion pojoVersion) {
		RestVersion result = null;
		if (pojoVersion instanceof PojoPublicVersion) {
			result = createRest((PojoPublicVersion) pojoVersion);
		} else if (pojoVersion instanceof PojoWorkspace) {
			result = createRest((PojoWorkspace) pojoVersion);
		} else {
			result = new RestVersion(pojoVersion.getVersion());
		}
		return result;
	}

	public RestPublicVersion createRest(PojoPublicVersion pojoPublicVersion) {
	  if (pojoPublicVersion == null) {
      return null;
    }
		return new RestPublicVersion(pojoPublicVersion.getVersion(), pojoPublicVersion.getMessage());
	}

	public RestOwner createRest(PojoOwner pojoOwner) {
	  if (pojoOwner == null) {
      return null;
    }
		return new RestOwner(pojoOwner.getId(), pojoOwner.getVersion());
	}

	public RestTool createRest(PojoTool pojoTool) {
	  if (pojoTool == null) {
      return null;
    }
		return new RestTool(pojoTool.getId(), pojoTool.getVersion(), pojoTool.getName(), pojoTool.getToolVersion());
	}

	public RestPackage createRest(PojoPackage pojoPackage) {
	  if (pojoPackage == null) {
	    return null;
	  }
		return new RestPackage(pojoPackage.getId(), pojoPackage.getVersion());
	}

	public RestResource createRest(PojoResource pojoResource) {
	  if (pojoResource == null) {
      return null;
    }
	  return new RestResource(pojoResource.getId(), pojoResource.getVersion());
	}

	public RestProject createRest(PojoProject pojoProject) {
		return new RestProject(pojoProject.getId(), pojoProject.getVersion());
	}

	public RestMetaModel createRest(PojoMetaModel pojoMetamodel) {
		if (pojoMetamodel != null) {
			return new RestMetaModel(pojoMetamodel.getId(), pojoMetamodel.getVersion());
		}
		return null;
	}

	public RestCollectionArtifact createRest(PojoCollectionArtifact pojoCollectionArtifact) {
		return new RestCollectionArtifact(pojoCollectionArtifact.getId(), pojoCollectionArtifact.getVersion());
	}

	public RestMapArtifact createRest(PojoMapArtifact pojoMapArtifact) {
		return new RestMapArtifact(pojoMapArtifact.getId(), pojoMapArtifact.getVersion());
	}

	public Change createRest(PojoChange change) {
		Change result = null;

		if (change instanceof PojoArtifactChange) {
			result = createRest((PojoArtifactChange) change);
		} else if (change instanceof PojoPropertyChange) {
			result = createRest((PojoPropertyChange) change);
		} else if (change instanceof PojoWorkspaceChange) {
			result = createRest((PojoWorkspaceChange) change);
		} else if (change instanceof PojoCollectionChange) {
			result = createRest((PojoCollectionChange<?>) change);
		} else {
			throw new IllegalArgumentException("PojoChange type undefined.");
		}

		return result;
	}

	public ArtifactChange createRest(PojoArtifactChange change) {
		ArtifactChange result = null;

		if (change instanceof PojoArtifactAliveSet) {
			result = createRest((PojoArtifactAliveSet) change);
		} else if (change instanceof PojoArtifactCommited) {
			result = createRest((PojoArtifactCommited) change);
		} else if (change instanceof PojoArtifactCreated) {
			result = createRest((PojoArtifactCreated) change);
		} else if (change instanceof PojoArtifactContainerSet) {
			result = createRest((PojoArtifactContainerSet) change);
		} else if (change instanceof PojoArtifactRollBack) {
			result = createRest((PojoArtifactRollBack) change);
		} else if (change instanceof PojoArtifactTypeSet) {
			result = createRest((PojoArtifactTypeSet) change);
		} else if (change instanceof PojoCollectionAddedElement) {
			result = createRest((PojoCollectionAddedElement) change);
		} else if (change instanceof PojoCollectionAddedElements) {
			result = createRest((PojoCollectionAddedElements) change);
		} else if (change instanceof PojoCollectionRemovedElement) {
			result = createRest((PojoCollectionRemovedElement) change);
		} else if (change instanceof PojoMapCleared) {
			result = createRest((PojoMapCleared) change);
		} else if (change instanceof PojoMapElementRemoved) {
			result = createRest((PojoMapElementRemoved) change);
		} else if (change instanceof PojoMapPut) {
			result = createRest((PojoMapPut) change);
		} else {
			throw new IllegalArgumentException("PojoArtifactChange type undefined.");
		}

		return result;
	}

	public ArtifactAliveSet createRest(PojoArtifactAliveSet change) {
		return new ArtifactAliveSet(change.getOrigin(), createRest(change.getArtifact()), change.isAlive());
	}

	public ArtifactCommited createRest(PojoArtifactCommited change) {
		return new ArtifactCommited(change.getOrigin(), createRest(change.getArtifact()), createRest(change.getVersion()),
				createRestList(change.getChanges(), this::createRest), change.getOldBaseVersion());
	}

	public ArtifactCreated createRest(PojoArtifactCreated change) {
		return new ArtifactCreated(change.getOrigin(), createRest(change.getArtifact()));
	}

	public ArtifactContainerSet createRest(PojoArtifactContainerSet change) {
		return new ArtifactContainerSet(change.getOrigin(), createRest(change.getArtifact()),
				createRest(change.getContainer()));
	}

	public ArtifactRollBack createRest(PojoArtifactRollBack change) {
		return new ArtifactRollBack(change.getOrigin(), createRest(change.getArtifact()), change.isDecreased());
	}

	public ArtifactTypeSet createRest(PojoArtifactTypeSet change) {
		return new ArtifactTypeSet(change.getOrigin(), createRest(change.getArtifact()), createRest(change.getType()));
	}

	public CollectionAddedElement createRest(PojoCollectionAddedElement change) {
		return new CollectionAddedElement(change.getOrigin(), createRest(change.getArtifact()),
				createRest(change.getValue()));
	}

	public CollectionAddedElements createRest(PojoCollectionAddedElements change) {
		return new CollectionAddedElements(change.getOrigin(), createRest(change.getArtifact()),
				createRestList(change.getValues(), this::createRest));
	}

	public CollectionRemovedElement createRest(PojoCollectionRemovedElement change) {
		return new CollectionRemovedElement(change.getOrigin(), createRest(change.getArtifact()),
				createRest(change.getValue()));
	}

	public MapCleared createRest(PojoMapCleared change) {
		return new MapCleared(change.getOrigin(), createRest(change.getArtifact()));
	}

	public MapElementRemoved createRest(PojoMapElementRemoved change) {
		return new MapElementRemoved(change.getOrigin(), createRest(change.getArtifact()), createRest(change.getKey()),
				createRest(change.getValue()));
	}

	public MapPut createRest(PojoMapPut change) {
		return new MapPut(change.getOrigin(), createRest(change.getArtifact()), createRest(change.getKey()),
				createRest(change.getOldValue()), createRest(change.getNewValue()), change.isAdded());
	}

	public PropertyChange createRest(PojoPropertyChange change) {
		PropertyChange result = null;

		if (change instanceof PojoPropertyAliveSet) {
			result = createRest((PojoPropertyAliveSet) change);
		} else if (change instanceof PojoPropertyCommited) {
			result = createRest((PojoPropertyCommited) change);
		} else if (change instanceof PojoPropertyRollBack) {
			result = createRest((PojoPropertyRollBack) change);
		} else if (change instanceof PojoPropertyValueSet) {
			result = createRest((PojoPropertyValueSet) change);
		} else {
			throw new IllegalArgumentException("PojoPropertyChange type undefined.");
		}

		return result;
	}

	public PropertyAliveSet createRest(PojoPropertyAliveSet change) {
		return new PropertyAliveSet(change.getOrigin(), createRest(change.getProperty()), change.isAlive());
	}

	public PropertyCommited createRest(PojoPropertyCommited change) {
		return new PropertyCommited(change.getOrigin(), createRest(change.getProperty()), createRest(change.getVersion()),
				createRestList(change.getChanges(), this::createRest), change.getOldBaseVersion());
	}

	public PropertyRollBack createRest(PojoPropertyRollBack change) {
		return new PropertyRollBack(change.getOrigin(), createRest(change.getProperty()), change.isDecreased());
	}

	public PropertyValueSet createRest(PojoPropertyValueSet change) {
		return new PropertyValueSet(change.getOrigin(), createRest(change.getProperty()), createRest(change.getValue()),
				createRest(change.getOldValue()), change.isWasReference());
	}

	public WorkspaceChange createRest(PojoWorkspaceChange change) {
		WorkspaceChange result = null;

		if (change instanceof PojoVersionCommited) {
			result = createRest((PojoVersionCommited) change);
		} else if (change instanceof PojoWorkspaceAdded) {
			result = createRest((PojoWorkspaceAdded) change);
		} else if (change instanceof PojoWorkspaceClosed) {
			result = createRest((PojoWorkspaceClosed) change);
		} else if (change instanceof PojoWorkspaceRebased) {
			result = createRest((PojoWorkspaceRebased) change);
		} else if (change instanceof PojoWorkspaceRollBack) {
			result = createRest((PojoWorkspaceRollBack) change);
		} else if (change instanceof PojoWorkspaceParentSet) {
			result = createRest((PojoWorkspaceParentSet) change);
		} else {
			throw new IllegalArgumentException("PojoWorkspaceChange type undefined.");
		}

		return result;
	}

	public VersionCommited createRest(PojoVersionCommited change) {
		return new VersionCommited(createRest(change.getWorkspace()), createRest(change.getVersion()),
				createRestList(change.getChanges(), this::createRest), change.getOldBaseVersion());
	}

	public WorkspaceAdded createRest(PojoWorkspaceAdded change) {
		return new WorkspaceAdded(createRest(change.getWorkspace()));
	}

	public WorkspaceClosed createRest(PojoWorkspaceClosed change) {
		return new WorkspaceClosed(createRest(change.getWorkspace()));
	}

	public WorkspaceParentSet createRest(PojoWorkspaceParentSet change) {
		return new WorkspaceParentSet(createRest(change.getParent()), createRest(change.getWorkspace()));
	}

	public WorkspaceRebased createRest(PojoWorkspaceRebased change) {
		return new WorkspaceRebased(createRest(change.getOrigin()), createRest(change.getWorkspace()),
				createRest(change.getVersion()), createRestList(change.getChanges(), this::createRest));
	}

	public WorkspaceRollBack createRest(PojoWorkspaceRollBack change) {
		return new WorkspaceRollBack(createRest(change.getWorkspace()));
	}

	public CollectionChange<?> createRest(PojoCollectionChange<?> change) {
		CollectionChange<?> result = null;
		if (change instanceof PojoPropertyMapSet) {
			result = createRest((PojoPropertyMapSet) change);
		}
		return result;
	}

	public PropertyMapSet createRest(PojoPropertyMapSet change) {
		return new PropertyMapSet(change.getOrigin(), createRestList(change.getChanges(), this::createRest));
	}

	public User createRest(PojoUser user) {
		return new RestUser(user.getId(), user.getName(), user.getLogin(), user.getPassword(), user.getOwner());
	}

	public Object createRest(PojoObject pojoObject) {
		Object object = null;
		if (pojoObject != null) {
			object = pojoObject.getObject();
			if (object instanceof PojoArtifact) {
				object = createRest((PojoArtifact) object);
			}
		}

		return object;
	}

	// --------------
	// Array
	// --------------

	public RestWorkspace[] createRestArray(PojoWorkspace[] pojoWorkspaces) {
		return createRestArray(RestWorkspace[]::new, pojoWorkspaces, this::createRest);
	}

	public RestArtifact[] createRestArray(PojoArtifact[] pojoArtifacts) {
		return createRestArray(RestArtifact[]::new, pojoArtifacts, this::createRest);
	}

	public RestResource[] createRestArray(PojoResource[] pojoResources) {
		return createRestArray(RestResource[]::new, pojoResources, this::createRest);
	}

	public RestProperty[] createRestArray(PojoProperty[] pojoProperties) {
		return createRestArray(RestProperty[]::new, pojoProperties, this::createRest);
	}

	public Version[] createRestArray(PojoVersion[] pojoVersions) {
		return createRestArray(RestVersion[]::new, pojoVersions, this::createRest);
	}

	public RestOwner[] createRestArray(PojoOwner[] pojoOwners) {
		return createRestArray(RestOwner[]::new, pojoOwners, this::createRest);
	}

	public RestTool[] createRestArray(PojoTool[] pojoTools) {
		return createRestArray(RestTool[]::new, pojoTools, this::createRest);
	}

	public RestPackage[] createRestArray(PojoPackage[] pojoPackages) {
		return createRestArray(RestPackage[]::new, pojoPackages, this::createRest);
	}

	public RestProject[] createRestArray(PojoProject[] pojoProjects) {
		return createRestArray(RestProject[]::new, pojoProjects, this::createRest);
	}

	public RestMetaModel[] createRestArray(PojoMetaModel[] pojoMetamodels) {
		return createRestArray(RestMetaModel[]::new, pojoMetamodels, this::createRest);
	}

	public RestCollectionArtifact[] createRestArray(PojoCollectionArtifact[] pojoCollectionArtifacts) {
		return createRestArray(RestCollectionArtifact[]::new, pojoCollectionArtifacts, this::createRest);
	}

	public Map<Artifact, Set<Property>> createRestMap(PojoArtifactAndProperties[] array) {
		return createRestMap(HashMap::new, array, paap -> {
			Artifact a = createRest(paap.getArtifact());
			Set<Property> p = createRestCollection(HashSet::new, paap.getProperties(), this::createRest);

			return new SimpleImmutableEntry<>(a, p);
		});

		// Map<Artifact, Set<Property>> result = new HashMap<Artifact,
		// Set<Property>>();
		// for (int i = 0; i < array.length; i++) {
		// Artifact artifact = createRest(array[i].getArtifact());
		// Property[] properties = createRestArray(array[i].getProperties());
		// result.put(artifact, new HashSet<Property>());
		// for (int j = 0; j < properties.length; j++) {
		// result.get(artifact).add(properties[j]);
		// }
		// }
		// return result;
	}

	public Set<Object> createRestSet(PojoObject[] pojoObjectArray) {
		return createRestCollection(HashSet::new, pojoObjectArray, this::createRest);
	}

	public Set<MapArtifact.Entry> createRestEntrySet(long id, long version, PojoArtifact[] pojoArtifactArray) {
		return createRestCollection(HashSet::new, pojoArtifactArray,
				p -> new RestMapArtifact.RestEntry(id, version, p.getId(), p.getVersion()));
	}

	public List<Object> createRestCollection(PojoObject[] pojoObjectArray) {
		return createRestCollection(ArrayList::new, pojoObjectArray, this::createRest);
	}

	public RestMapArtifact[] createRestArray(PojoMapArtifact[] pojoMap) {
		return createRestArray(RestMapArtifact[]::new, pojoMap, this::createRest);
	}

	public Change[] createRestArray(PojoChange[] pojoChanges) {
		return createRestArray(Change[]::new, pojoChanges, this::createRest);
	}

	public Object[] createRestArray(PojoObject[] pojoObjects) {
		return createRestArray(Object[]::new, pojoObjects, this::createRest);
	}

	public User[] createRestArray(PojoUser[] pojoUsers) {
		return createRestArray(User[]::new, pojoUsers, this::createRest);
	}

	// -----------------------------
	// List
	// -----------------------------

	public List<Workspace> createRest(PojoWorkspace[] pojoWorkspaces) {
		return createRestList(pojoWorkspaces, this::createRest);
	}

	public List<Artifact> createRestList(PojoArtifact[] pojoArtifacts) {
		return createRestList(pojoArtifacts, this::createRest);
	}

	// -----------------------------
	// create Rest Array / Collection generic implementations
	// (c) jMayer
	// -----------------------------

	/**
	 * 
	 * @param pojo
	 *          POJO elements
	 * @param mapper
	 *          Function reference for the matching createRest(...) or constructor
	 *          mapping the POJO to the REST instance
	 * 
	 * @param <P>
	 *          POJO instance type
	 * @param <R>
	 *          REST instance type
	 * 
	 * @return REST List
	 */
	public <P, R> List<R> createRestList(P[] pojo, Function<? super P, ? extends R> mapper) {
		if (pojo == null)
			return null;
		return Arrays.stream(pojo).map(mapper).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param supplier
	 *          Constructor reference for the array
	 * @param pojo
	 *          POJO elements
	 * @param mapper
	 *          Function reference for the matching createRest(...) or constructor
	 *          mapping the POJO to the REST instance
	 * @param <P>
	 *          POJO instance type
	 * @param <R>
	 *          REST instance type
	 * 
	 * @return REST Array
	 */
	public <P, R> R[] createRestArray(IntFunction<R[]> supplier, P[] pojo, Function<? super P, ? extends R> mapper) {
		if (pojo == null)
			return null;
		return Arrays.stream(pojo).map(mapper).toArray(supplier);
	}

	/**
	 * Creates a collection of type R with a capacity of {@code pojo.length} and
	 * the elements of {@code pojo}. Preserves order.
	 * @param supplier
	 *          Constructor reference for the collection with size parameter
	 * @param pojo
	 *          POJO elements
	 * @param mapper
	 *          Function reference for the matching createRest(...) or constructor
	 *          mapping the POJO to the REST instance
	 * 
	 * @param <P>
	 *          POJO instance type
	 * @param <T>
	 *          REST instance type
	 * @param <R>
	 *          Collection type
	 * 
	 * @return REST collection
	 */
	public <P, T, R extends Collection<? super T>> R createRestCollection(IntFunction<R> supplier, P[] pojo,
			Function<? super P, ? extends T> mapper) {
		if (pojo == null)
			return null;
		R ret = supplier.apply(pojo.length);
		Arrays.stream(pojo).map(mapper).forEachOrdered(ret::add);
		return ret;
	}

	/**
   * Creates a map of type M with a capacity of {@code pojo.length} and
   * the elements of {@code pojo} converted to map entries. Preserves order.
   * @param supplier
   *          Constructor reference for the collection with size parameter
   * @param pojo
   *          POJO elements
   * @param mapper
   *          Function reference for the matching createRest(...) or constructor
   *          mapping the POJO to the REST instance
   * 
   * @param <P>
   *          POJO instance type
   * @param <K>
   *          REST key type
   * @param <V>
   *          REST value type
   * @param <M>
   *          REST map type
   * 
   * @return REST collection
   */
	public <P, K, V, M extends Map<K, V>> M createRestMap(IntFunction<M> supplier, P[] pojo,
			Function<P, Map.Entry<K, V>> mapper) {
		if (pojo == null)
			return null;
		M ret = supplier.apply(pojo.length);
		Arrays.stream(pojo).map(mapper).forEachOrdered(e -> ret.put(e.getKey(), e.getValue()));
		return ret;
	}
}
