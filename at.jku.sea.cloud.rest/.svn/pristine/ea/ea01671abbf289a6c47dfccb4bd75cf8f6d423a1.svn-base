package at.jku.sea.cloud.rest.pojo.factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.Container.Filter;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.PublicVersion;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Tool;
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
import at.jku.sea.cloud.rest.pojo.PojoArtifactRepAndPropertiesRep;
import at.jku.sea.cloud.rest.pojo.PojoCollectionArtifact;
import at.jku.sea.cloud.rest.pojo.PojoContainer;
import at.jku.sea.cloud.rest.pojo.PojoKeyValue;
import at.jku.sea.cloud.rest.pojo.PojoMapArtifact;
import at.jku.sea.cloud.rest.pojo.PojoMetaModel;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.PojoProject;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPropertyFilter;
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
public class PojoFactory {

	private static PojoFactory instance;

	public static PojoFactory getInstance() {
		if (instance == null) {
			instance = new PojoFactory();
		}
		return instance;
	}

	private PojoFactory() {
	}

	public PojoWorkspace createPojo(Workspace workspace) {
		if (workspace == null)
			return null;
		return new PojoWorkspace(workspace.getVersionNumber(), workspace.getId());
	}

	public PojoArtifact createPojo(Artifact artifact) {
		PojoArtifact result = null;

		if (artifact instanceof Project) {
			result = createPojo((Project) artifact);
		} else if (artifact instanceof MetaModel) {
			result = createPojo((MetaModel) artifact);
		} else if (artifact instanceof Owner) {
			result = createPojo((Owner) artifact);
		} else if (artifact instanceof Container) {
			result = createPojo((Container) artifact);
		} else if (artifact instanceof CollectionArtifact) {
			result = createPojo((CollectionArtifact) artifact);
		} else if (artifact instanceof Tool) {
			result = createPojo((Tool) artifact);
		} else if (artifact instanceof MapArtifact) {
			result = createPojo((MapArtifact) artifact);
		} else {
			result = new PojoArtifact(artifact.getId(), artifact.getVersionNumber());
		}

		return result;
	}

	public PojoContainer createPojo(Container container) {
		PojoContainer result = null;

		if (container instanceof Package) {
			result = createPojo((Package) container);
		} else if (container instanceof Resource) {
			result = createPojo((Resource) container);
		} else {
			result = new PojoContainer(container.getId(), container.getVersionNumber());
		}

		return result;
	}

	public PojoProperty createPojo(Property property) {
		if (property == null)
			return null;
		return new PojoProperty(property.getId(), property.getVersionNumber(), property.getName(),
				this.createPojo(property.getValue()));
	}

	public PojoVersion createPojo(Version version) {
		PojoVersion result = null;
		if (version instanceof PublicVersion) {
			result = createPojo((PublicVersion) version);
		} else if (version instanceof Workspace) {
			result = createPojo((Workspace) version);
		} else {
			result = new PojoVersion(version.getVersionNumber());
		}
		return result;
	}

	public PojoPublicVersion createPojo(PublicVersion publicVersion) {
		return new PojoPublicVersion(publicVersion.getVersionNumber(), publicVersion.getCommitMessage());
	}

	public PojoOwner createPojo(Owner owner) {
		if (owner == null)
			return null;
		return new PojoOwner(owner.getId(), owner.getVersionNumber());
	}

	public PojoTool createPojo(Tool tool) {
		if (tool == null)
			return null;
		return new PojoTool(tool.getId(), tool.getVersionNumber(), tool.getName(), tool.getToolVersion());
	}

	public PojoPackage createPojo(Package _package) {
		if (_package == null)
			return null;
		return new PojoPackage(_package.getId(), _package.getVersionNumber());
	}

	public PojoResource createPojo(Resource resource) {
		if (resource == null)
			return null;
		return new PojoResource(resource.getId(), resource.getVersionNumber());
	}

	public PojoProject createPojo(Project project) {
		if (project == null)
			return null;
		return new PojoProject(project.getId(), project.getVersionNumber());
	}

	public PojoMetaModel createPojo(MetaModel metamodel) {
		if (metamodel == null)
			return null;
		return new PojoMetaModel(metamodel.getId(), metamodel.getVersionNumber());
	}

	public PojoCollectionArtifact createPojo(CollectionArtifact collectionArtifact) {
		if (collectionArtifact == null)
			return null;
		return new PojoCollectionArtifact(collectionArtifact.getId(), collectionArtifact.getVersionNumber());
	}

	public PojoMapArtifact createPojo(MapArtifact mapArtifact) {
		return new PojoMapArtifact(mapArtifact.getId(), mapArtifact.getVersionNumber());
	}

	public PojoUser createPojo(User user) {
		if (user == null)
			return null;
		return new PojoUser(user.getId(), user.getName(), user.getLogin(), user.getPassword(), user.getOwnerId());
	}

	public PojoObject createPojo(Object object) {
		PojoObject result = null;
		if (object instanceof Artifact) {
			result = new PojoObject(createPojo((Artifact) object));
		} else {
			result = new PojoObject(object);
		}

		return result;
	}

	public PojoChange createPojo(Change change) {
		PojoChange result = null;

		if (change instanceof ArtifactChange) {
			result = createPojo((ArtifactChange) change);
		} else if (change instanceof PropertyChange) {
			result = createPojo((PropertyChange) change);
		} else if (change instanceof WorkspaceChange) {
			result = createPojo((WorkspaceChange) change);
		} else if (change instanceof CollectionChange) {
			result = createPojo((CollectionChange<?>) change);
		} else {
			result = new PojoChange();
		}

		return result;
	}

	public PojoArtifactChange createPojo(ArtifactChange change) {
		PojoArtifactChange result = null;

		if (change instanceof ArtifactAliveSet) {
			result = createPojo((ArtifactAliveSet) change);
		} else if (change instanceof ArtifactCommited) {
			result = createPojo((ArtifactCommited) change);
		} else if (change instanceof ArtifactCreated) {
			result = createPojo((ArtifactCreated) change);
		} else if (change instanceof ArtifactContainerSet) {
			result = createPojo((ArtifactContainerSet) change);
		} else if (change instanceof ArtifactRollBack) {
			result = createPojo((ArtifactRollBack) change);
		} else if (change instanceof ArtifactTypeSet) {
			result = createPojo((ArtifactTypeSet) change);
		} else if (change instanceof CollectionAddedElement) {
			result = createPojo((CollectionAddedElement) change);
		} else if (change instanceof CollectionAddedElements) {
			result = createPojo((CollectionAddedElements) change);
		} else if (change instanceof CollectionRemovedElement) {
			result = createPojo((CollectionRemovedElement) change);
		} else if (change instanceof MapCleared) {
			result = createPojo((MapCleared) change);
		} else if (change instanceof MapElementRemoved) {
			result = createPojo((MapElementRemoved) change);
		} else if (change instanceof MapPut) {
			result = createPojo((MapPut) change);
		} else {
			result = new PojoArtifactChange(change.origin, this.createPojo(change.artifact));
		}

		return result;
	}

	public PojoArtifactAliveSet createPojo(ArtifactAliveSet change) {
		if (change == null)
			return null;
		return new PojoArtifactAliveSet(change.origin, this.createPojo(change.artifact), change.alive);
	}

	public PojoArtifactCommited createPojo(ArtifactCommited change) {
		if (change == null)
			return null;
		return new PojoArtifactCommited(change.origin, this.createPojo(change.artifact), this.createPojo(change.version),
				this.createPojoArray(PojoChange[]::new, change.changes, this::createPojo), change.oldBaseVersion);
	}

	public PojoArtifactCreated createPojo(ArtifactCreated change) {
		if (change == null)
			return null;
		return new PojoArtifactCreated(change.origin, this.createPojo(change.artifact));
	}

	public PojoArtifactContainerSet createPojo(ArtifactContainerSet change) {
		if (change == null)
			return null;
		return new PojoArtifactContainerSet(change.origin, this.createPojo(change.artifact),
				this.createPojo(change.container));
	}

	public PojoArtifactRollBack createPojo(ArtifactRollBack change) {
		if (change == null)
			return null;
		return new PojoArtifactRollBack(change.origin, this.createPojo(change.artifact), change.isDeceased);
	}

	public PojoArtifactTypeSet createPojo(ArtifactTypeSet change) {
		if (change == null)
			return null;
		return new PojoArtifactTypeSet(change.origin, this.createPojo(change.artifact),
				this.createPojo(change.artifactType));
	}

	public PojoCollectionAddedElement createPojo(CollectionAddedElement change) {
		if (change == null)
			return null;
		return new PojoCollectionAddedElement(change.origin, this.createPojo(change.artifact),
				this.createPojo(change.value));
	}

	public PojoCollectionAddedElements createPojo(CollectionAddedElements change) {
		if (change == null)
			return null;
		return new PojoCollectionAddedElements(change.origin, this.createPojo(change.artifact),
				this.createPojoArray(PojoObject[]::new, change.values, this::createPojo));
	}

	public PojoCollectionRemovedElement createPojo(CollectionRemovedElement change) {
		if (change == null)
			return null;
		return new PojoCollectionRemovedElement(change.origin, this.createPojo(change.artifact),
				this.createPojo(change.value));
	}

	public PojoMapCleared createPojo(MapCleared change) {
		if (change == null)
			return null;
		return new PojoMapCleared(change.origin, this.createPojo(change.artifact));
	}

	public PojoMapElementRemoved createPojo(MapElementRemoved change) {
		if (change == null)
			return null;
		return new PojoMapElementRemoved(change.origin, this.createPojo(change.artifact), this.createPojo(change.key),
				this.createPojo(change.value));
	}

	public PojoMapPut createPojo(MapPut change) {
		if (change == null)
			return null;
		return new PojoMapPut(change.origin, this.createPojo(change.artifact), this.createPojo(change.key),
				this.createPojo(change.oldValue), this.createPojo(change.newValue), change.isAdded);
	}

	public PojoPropertyChange createPojo(PropertyChange change) {
		PojoPropertyChange result = null;

		if (change instanceof PropertyAliveSet) {
			result = createPojo((PropertyAliveSet) change);
		} else if (change instanceof PropertyCommited) {
			result = createPojo((PropertyCommited) change);
		} else if (change instanceof PropertyRollBack) {
			result = createPojo((PropertyRollBack) change);
		} else if (change instanceof PropertyValueSet) {
			result = createPojo((PropertyValueSet) change);
		} else {
			result = new PojoPropertyChange(change.origin, this.createPojo(change.property));
		}

		return result;
	}

	public PojoPropertyAliveSet createPojo(PropertyAliveSet change) {
		if (change == null)
			return null;
		return new PojoPropertyAliveSet(change.origin, this.createPojo(change.property), change.alive);
	}

	public PojoPropertyCommited createPojo(PropertyCommited change) {
		if (change == null)
			return null;
		return new PojoPropertyCommited(change.origin, this.createPojo(change.property), this.createPojo(change.version),
				this.createPojoArray(PojoChange[]::new, change.changes, this::createPojo), change.oldBaseVersion);
	}

	public PojoPropertyRollBack createPojo(PropertyRollBack change) {
		if (change == null)
			return null;
		return new PojoPropertyRollBack(change.origin, this.createPojo(change.property), change.isDeceased);
	}

	public PojoPropertyValueSet createPojo(PropertyValueSet change) {
		if (change == null)
			return null;
		return new PojoPropertyValueSet(change.origin, this.createPojo(change.property), this.createPojo(change.value),
				this.createPojo(change.oldValue), change.wasReference);
	}

	public PojoWorkspaceChange createPojo(WorkspaceChange change) {
		PojoWorkspaceChange result = null;

		if (change instanceof VersionCommited) {
			result = createPojo((VersionCommited) change);
		} else if (change instanceof WorkspaceAdded) {
			result = createPojo((WorkspaceAdded) change);
		} else if (change instanceof WorkspaceParentSet) {
			result = createPojo((WorkspaceParentSet) change);
		} else if (change instanceof WorkspaceClosed) {
			result = createPojo((WorkspaceClosed) change);
		} else if (change instanceof WorkspaceRebased) {
			result = createPojo((WorkspaceRebased) change);
		} else if (change instanceof WorkspaceRollBack) {
			result = createPojo((WorkspaceRollBack) change);
		} else {
			return new PojoWorkspaceChange(createPojo(change.workspace));
		}

		return result;
	}

	public PojoVersionCommited createPojo(VersionCommited change) {
		if (change == null)
			return null;
		return new PojoVersionCommited(createPojo(change.workspace), this.createPojo(change.version),
				this.createPojoArray(PojoChange[]::new, change.changes, this::createPojo), change.oldBaseVersion);
	}

	public PojoWorkspaceAdded createPojo(WorkspaceAdded change) {
		if (change == null)
			return null;
		return new PojoWorkspaceAdded(createPojo(change.workspace));
	}

	public PojoWorkspaceClosed createPojo(WorkspaceClosed change) {
		if (change == null)
			return null;
		return new PojoWorkspaceClosed(createPojo(change.workspace));
	}

	public PojoWorkspaceParentSet createPojo(WorkspaceParentSet change) {
		if (change == null)
			return null;
		return new PojoWorkspaceParentSet(createPojo(change.workspace), this.createPojo(change.parent));
	}

	public PojoWorkspaceRebased createPojo(WorkspaceRebased change) {
		if (change == null)
			return null;
		return new PojoWorkspaceRebased(createPojo(change.origin), this.createPojo(change.workspace),
				this.createPojo(change.oldBaseVersion),
				this.createPojoArray(PojoChange[]::new, change.changes, this::createPojo));
	}

	public PojoWorkspaceRollBack createPojo(WorkspaceRollBack change) {
		if (change == null)
			return null;
		return new PojoWorkspaceRollBack(createPojo(change.workspace));
	}

	public PojoCollectionChange<?> createPojo(CollectionChange<?> change) {
		PojoCollectionChange<?> result = null;
		if (change instanceof PropertyMapSet) {
			result = createPojo((PropertyMapSet) change);
		}
		return result;
	}

	public PojoPropertyMapSet createPojo(PropertyMapSet change) {
		if (change == null)
			return null;
		return new PojoPropertyMapSet(change.origin, this.createPojoArray(change.changes));
	}

	public PojoKeyValue createPojo(Object key, Object value) {
		PojoObject pojoKey = createPojo(key);
		PojoObject pojoValue = createPojo(value);
		return new PojoKeyValue(pojoKey, pojoValue);
	}

	public PojoPropertyValueSet[] createPojoArray(Collection<PropertyValueSet> properties) {
		return createPojoArray(PojoPropertyValueSet[]::new, properties, this::createPojo);
	}

	public PojoPropertyFilter createPojo(Entry<String, Object> entry) {
		return new PojoPropertyFilter(entry.getKey(), this.createPojo(entry.getValue()));
	}

	public PojoPropertyFilter[] createPojo(Map<String, Object> properties) {
		return createPojoArray(PojoPropertyFilter[]::new, properties, (Function<Entry<String,Object>,PojoPropertyFilter>)this::createPojo);

//		 PojoPropertyFilter[] pojoFilterArray = new
//		 PojoPropertyFilter[properties.size()];
//		
//		 int i = 0;
//		 for (Entry<String, Object> kvp : properties.entrySet()) {
//		 pojoFilterArray[i++] = createPojo(kvp);
//		 }
//		 return pojoFilterArray;
	}

	public PojoWorkspace[] createPojoArray(Workspace[] workspaces) {
		return createPojoArray(PojoWorkspace[]::new, workspaces, this::createPojo);
	}

	public PojoArtifact[] createPojoArray(Artifact[] artifacts) {
		return createPojoArray(PojoArtifact[]::new, artifacts, this::createPojo);
	}

	public PojoContainer[] createPojoArray(Container[] artifacts) {
		return createPojoArray(PojoContainer[]::new, artifacts, this::createPojo);
	}

	public PojoResource[] createPojoArray(Resource[] artifacts) {
		return createPojoArray(PojoResource[]::new, artifacts, this::createPojo);
	}

	public PojoProperty[] createPojoArray(Property[] properties) {
		return createPojoArray(PojoProperty[]::new, properties, this::createPojo);
	}

	public PojoProperty[] createPojoArray(final Artifact artifact, final Map<String, Object> map) {
		return createPojoArray(PojoProperty[]::new, map, (k, v) -> this.createPojo(artifact, k, v));

		// PojoProperty[] result = new PojoProperty[map.size()];
		// int i = 0;
		// Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		// while (iterator.hasNext()) {
		// Entry<String, Object> next = iterator.next();
		// result[i] = createPojo(artifact, next.getKey(), next.getValue());
		// i++;
		// }
		// return result;
	}

	public PojoProperty createPojo(Artifact artifact, String key, Object value) {
		if (artifact == null)
			return null;
		return new PojoProperty(artifact.getId(), artifact.getVersionNumber(), key, this.createPojo(value));
	}

	public PojoVersion[] createPojoArray(Version[] versions) {
		return createPojoArray(PojoVersion[]::new, versions, this::createPojo);
	}

	public PojoOwner[] createPojoArray(Owner[] owners) {
		return createPojoArray(PojoOwner[]::new, owners, this::createPojo);
	}

	public PojoTool[] createPojoArray(Tool[] tools) {
		return createPojoArray(PojoTool[]::new, tools, this::createPojo);
	}

	public PojoPackage[] createPojoArray(Package[] packages) {
		return createPojoArray(PojoPackage[]::new, packages, this::createPojo);
	}

	public PojoProject[] createPojoArray(Project[] projects) {
		return createPojoArray(PojoProject[]::new, projects, this::createPojo);
	}

	public PojoMetaModel[] createPojoArray(MetaModel[] metamodels) {
		return createPojoArray(PojoMetaModel[]::new, metamodels, this::createPojo);
	}

	public PojoMapArtifact[] createPojoArray(MapArtifact[] maps) {
		return createPojoArray(PojoMapArtifact[]::new, maps, this::createPojo);
	}

	public PojoCollectionArtifact[] createPojoArray(CollectionArtifact[] collectionArtifacts) {
		return createPojoArray(PojoCollectionArtifact[]::new, collectionArtifacts, this::createPojo);
	}

	public PojoUser[] createPojoArray(User[] users) {
		return createPojoArray(PojoUser[]::new, users, this::createPojo);
	}

	public PojoArtifactAndProperties[] createPojoArray(Map<Artifact, Set<Property>> map) {
		return createPojoArray(PojoArtifactAndProperties[]::new, map, (k, v) -> {
			PojoArtifact artifact = createPojo(k);
			PojoProperty[] properties = createPojoArray(PojoProperty[]::new, v, this::createPojo);
			return new PojoArtifactAndProperties(artifact, properties);
		});

		// Artifact[] keys = map.keySet().toArray(new Artifact[0]);
		// PojoArtifactAndProperties[] result = new
		// PojoArtifactAndProperties[map.size()];
		// for (int i = 0; i < map.size(); i++) {
		// PojoArtifact artifact = createPojo(keys[i]);
		// PojoProperty[] properties = createPojoArray(map.get(keys[i]).toArray(new
		// Property[0]));
		// result[i] = new PojoArtifactAndProperties(artifact, properties);
		// }
		// return result;
	}

	public PojoArtifactAndProperties[] createPojoMappings(Map<Artifact, Map<String, Object>> map) {
		return createPojoArray(PojoArtifactAndProperties[]::new, map, (k, v) -> {
			PojoArtifact artifact = createPojo(k);
			PojoProperty[] properties = createPojoArray(k, v);
			return new PojoArtifactAndProperties(artifact, properties);
		});

		// Artifact[] keys = map.keySet().toArray(new Artifact[0]);

		// PojoArtifactAndProperties[] result = new
		// PojoArtifactAndProperties[map.size()];
		// for (int i = 0; i < map.size(); i++) {
		// PojoArtifact artifact = createPojo(keys[i]);
		// PojoProperty[] properties = createPojoArray(keys[i], map.get(keys[i]));
		// result[i] = new PojoArtifactAndProperties(artifact, properties);
		// }
		// return result;
	}

	public PojoArtifact[] createPojoArray(MapArtifact.Entry[] entries) {
		return createPojoArray(PojoArtifact[]::new, entries, this::createPojo);
	}

	public PojoObject[] createPojoArray(Object[] objects) {
		return createPojoArray(PojoObject[]::new, objects, this::createPojo);
	}

	public PojoChange[] createPojoArray(Change[] changes) {
		return createPojoArray(PojoChange[]::new, changes, this::createPojo);
	}

	public PojoArtifact[] convertFilter(Filter filter) {
		if (filter == null)
			return null;
		PojoArtifact[] result = new PojoArtifact[5];
		if (filter.getOwner() != null) {
			result[0] = createPojo(filter.getOwner());
		}
		if (filter.getTool() != null) {
			result[1] = createPojo(filter.getTool());
		}
		if (filter.getArtifact() != null) {
			result[2] = createPojo(filter.getArtifact());
		}
		if (filter.getCollection() != null) {
			result[3] = createPojo(filter.getCollection());
		}
		if (filter.getProject() != null) {
			result[4] = createPojo(filter.getProject());
		}

		return result;
	}

	public PojoArtifactRepAndPropertiesRep[] createPojoArrayRep(Map<Object[], Set<Object[]>> map) {
		return createPojoArray(PojoArtifactRepAndPropertiesRep[]::new, map, (k, v) -> {
			PojoObject[] pojoArtifacts = this.createPojoArray(k);

			PojoObject[][] pojoProperties = this.createPojoArray(PojoObject[][]::new, v, this::createPojoArray);

			return new PojoArtifactRepAndPropertiesRep(pojoArtifacts, pojoProperties);
		});

		// PojoArtifactRepAndPropertiesRep[] result = new
		// PojoArtifactRepAndPropertiesRep[map.keySet().size()];
		// Iterator<Entry<Object[], Set<Object[]>>> iterator =
		// map.entrySet().iterator();
		// int i = 0;
		// while (iterator.hasNext()) {
		// Entry<Object[], Set<Object[]>> next = iterator.next();
		// PojoObject[] pojoArtifact = this.createPojoArray(next.getKey());
		// Set<Object[]> properties = next.getValue();
		// PojoObject[][] pojoProperties = new PojoObject[properties.size()][];
		// int s = 0;
		// for (Object[] property : properties) {
		// pojoProperties[s] = this.createPojoArray(property);
		// }
		// result[i] = new PojoArtifactRepAndPropertiesRep(pojoArtifact,
		// pojoProperties);
		// i++;
		// }
		// return result;
	}

	// -----------------------------
	// create Pojo Array / Collection generic implementations
	// (c) jMayer
	// -----------------------------

	/**
	 * Mapping implementation to POJO
	 * 
	 * @param supplier
	 *          Constructor reference for the result array
	 * @param elements
	 *          elements collection
	 * @param mapper
	 *          Function reference for the matching createPojo(...)
	 * @param <T>
	 *          Source element type
	 * @param <R>
	 *          Result element type
	 * 
	 * @return POJO Array
	 */
	public <T, R> R[] createPojoArray(IntFunction<R[]> supplier, Collection<T> elements,
			Function<? super T, ? extends R> mapper) {
		if (elements == null)
			return null;
		return elements.stream().map(mapper).toArray(supplier);
	}

	/**
	 * 
	 * 
	 * @param supplier
	 *          Constructor reference for the result array
	 * @param elements
	 *          elements array
	 * @param mapper
	 *          Function reference for the matching createPojo(...)
	 * @param <T>
	 *          Source element type
	 * @param <R>
	 *          Result element type
	 * 
	 * @return POJO Array
	 */
	public <T, R> R[] createPojoArray(IntFunction<R[]> supplier, T[] elements,
			Function<? super T, ? extends R> mapper) {
		if (elements == null)
			return null;
		return Arrays.stream(elements).map(mapper).toArray(supplier);
	}

	/**
	 * Creates an array of type {@code R} from the {@link Map#entrySet() entry
	 * set} of the {@code elements} {@link Map} using the {@code mapper}. The
	 * mapper is passed key and value of the {@link Map.Entry entry} and returns
	 * an instance of type {@code R}.
	 * 
	 * @param supplier
	 *          Constructor reference for the result array
	 * @param elements
	 *          elements map
	 * @param mapper
	 *          Function reference for the matching createPojo(...) consuming
	 *          key and value
	 * 
	 * @param <K>
	 *          Source key type
	 * @param <V>
	 *          Source value type
	 * 
	 * @param <R>
	 *          Result element type
	 * 
	 * @return POJO Array
	 */
	public <K, V, R> R[] createPojoArray(IntFunction<R[]> supplier, Map<K, V> elements,
			BiFunction<? super K, ? super V, ? extends R> mapper) {
		return this.createPojoArray(supplier, elements, e -> mapper.apply(e.getKey(), e.getValue()));
	}

	/**
	 * Creates an array of type {@code R} from the {@link Map#entrySet() entry
	 * set} of the {@code elements} {@link Map} using the {@code mapper}. The
	 * mapper is passed an {@link Map.Entry entry} and returns an instance of
	 * type {@code R}.
	 * 
	 * @param supplier
	 *          Constructor reference for the result array
	 * @param elements
	 *          elements map
	 * @param mapper
	 *          Function reference for the matching createPojo(...) consuming
	 *          map entry
	 * 
	 * @param <K>
	 *          Source key type
	 * @param <V>
	 *          Source value type
	 * 
	 * @param <R>
	 *          Result element type
	 * 
	 * @return POJO Array
	 */
	public <K, V, R> R[] createPojoArray(IntFunction<R[]> supplier, Map<K, V> elements,
			Function<Map.Entry<K, V>, ? extends R> mapper) {
		if (elements == null)
			return null;
		return elements.entrySet().stream().map(mapper).toArray(supplier);
	}
}
