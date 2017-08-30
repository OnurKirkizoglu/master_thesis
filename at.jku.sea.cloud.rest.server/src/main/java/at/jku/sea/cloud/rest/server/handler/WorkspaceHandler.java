package at.jku.sea.cloud.rest.server.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
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
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
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
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.exceptions.VersionConflictException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceEmptyException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.exceptions.WorkspaceNotEmptyException;
import at.jku.sea.cloud.implementation.DefaultMapArtifact;
import at.jku.sea.cloud.listeners.events.Change;
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
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;
import at.jku.sea.cloud.rest.server.factory.DesignSpaceFactory;
import at.jku.sea.cloud.rest.server.listeners.BufferListenerManager;
import at.jku.sea.cloud.rest.server.listeners.WorkspaceBufferListener;
import at.jku.sea.cloud.utils.CloudUtils;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class WorkspaceHandler {

	@Autowired
	protected Cloud cloud;
	@Autowired
	protected PojoFactory pojoFactory;
	@Autowired
	protected DesignSpaceFactory dsFactory;

	Map<Long, BufferListenerManager> managers = new HashMap<Long, BufferListenerManager>();

	public PojoWorkspace getOrCreateWorkspace(PojoOwnerToolStringLongParentPushPull options)
			throws WorkspaceExpiredException, OwnerDoesNotExistException, ToolDoesNotExistException {
		PojoOwner pojoOwner = options.getOwner();
		Owner owner = cloud.getOwner(pojoOwner.getId());
		PojoTool pojoTool = options.getTool();
		Tool tool = cloud.getTool(pojoTool.getId());
		String identifier = options.getString();
		Long version = options.getLong();
		PropagationType push = options.getPush();
		PropagationType pull = options.getPull();
		Workspace parent = options.getParent() == null ? null : cloud.getWorkspace(options.getParent());
		Workspace workspace = null;
		if (version == null) {
			workspace = cloud.createWorkspace(owner, tool, identifier, parent, push, pull);
		} else {
			workspace = cloud.createWorkspace(owner, tool, identifier, version, push, pull);
		}
		return pojoFactory.createPojo(workspace);
	}

	public String getIdentifier(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		String identifier = workspace.getIdentifier();
		return identifier;
	}

	public PojoOwner getOwner(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Owner owner = workspace.getOwner();
		return pojoFactory.createPojo(owner);
	}

	public PojoTool getTool(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Tool tool = workspace.getTool();
		return pojoFactory.createPojo(tool);
	}

	public PojoVersion getBaseVersion(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Version version = workspace.getBaseVersion();
		return pojoFactory.createPojo(version);
	}

	public PojoArtifactAndProperties[] rebase(long wsid, Long version)
			throws WorkspaceExpiredException, IllegalArgumentException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Map<Artifact, Set<Property>> result;
		if (version == null) {
			result = workspace.rebaseToHeadVersion();
		} else {
			Version v = cloud.getVersion(version);
			result = workspace.rebase(v);
		}
		return pojoFactory.createPojoArray(result);
	}

	public PojoArtifact[] getArtifacts(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact[] artifacts = workspace.getArtifacts().toArray(new Artifact[0]);
		return pojoFactory.createPojoArray(artifacts);
	}

	public PojoArtifact[] getArtifacts(long wsid, PojoArtifactAndPropertyFilter options)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, CollectionArtifactDoesNotExistException,
			MetaModelDoesNotExistException, PackageDoesNotExistException, ProjectDoesNotExistException,
			OwnerDoesNotExistException, ToolDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		PojoArtifact[] pojoArtifacts = options.getArtifacts();
		Artifact[] artifactFilters = new Artifact[pojoArtifacts.length];
		for (int i = 0; i < pojoArtifacts.length; i++) {
			artifactFilters[i] = workspace.getArtifact(pojoArtifacts[i].getId());
		}

		Artifact[] artifacts = null;
		if (options.getPropertyFilters() == null) {
			artifacts = workspace.getArtifacts(artifactFilters).toArray(new Artifact[0]);
		} else {
			PojoPropertyFilter[] propertyFilters = options.getPropertyFilters();
			Map<String, Object> propertyToValue = new HashMap<String, Object>();
			for (int i = 0; i < propertyFilters.length; i++) {
				String name = propertyFilters[i].getName();
				PojoObject pojoObject = propertyFilters[i].getValue();
				propertyToValue.put(name, dsFactory.createDS(pojoObject));
			}
			artifacts = workspace.getArtifactsWithProperty(propertyToValue, artifactFilters).toArray(new Artifact[0]);
		}

		return pojoFactory.createPojoArray(artifacts);
	}

	public PojoArtifact getArtifact(long wsid, long id)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = workspace.getArtifact(id);
		return pojoFactory.createPojo(artifact);
	}

	public PojoArtifact createArtifact(long wsid, PojoArtifact type) throws WorkspaceExpiredException,
			IllegalArgumentException, ArtifactDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = null;
		if (type == null) {
			artifact = workspace.createArtifact();
		} else {
			Artifact t = cloud.getArtifact(type.getVersion(), type.getId());
			artifact = workspace.createArtifact(t);
		}
		return pojoFactory.createPojo(artifact);
	}

	public PojoArtifact createArtifact(long wsid, PojoArtifactWithParameters params) {

		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact type = null;
		Container container = null;
		MetaModel metamodel = null;
		Project project = null;

		if (params.getType() != null) {
			type = workspace.getArtifact(params.getType().getId());
		}
		if (params.getContainer() != null) {
			container = (Container) workspace.getArtifact(params.getContainer().getId());
		}
		if (params.getMetamodel() != null) {
			metamodel = workspace.getMetaModel(params.getMetamodel().getId());
		}
		if (params.getProject() != null) {
			project = workspace.getProject(params.getProject().getId());
		}
		Map<String, Object> properties = new HashMap<>();
		if (params.getPropertyMap() != null) {
			for (Entry<String, PojoObject> entry : params.getPropertyMap().entrySet()) {
				if (entry.getValue() != null && entry.getValue().getObject() instanceof PojoArtifact) {
					Artifact propertyArtifact = workspace
							.getArtifact(((PojoArtifact) entry.getValue().getObject()).getId());
					properties.put(entry.getKey(), propertyArtifact);
				} else {
					properties.put(entry.getKey(), entry.getValue().getObject());
				}
			}
		}
		Artifact artifact = workspace.createArtifact(type, container, metamodel, project, properties);
		return pojoFactory.createPojo(artifact);
	}

	public PojoArtifact createArtifact(long wsid, long pkgId, long pkgVersion, PojoArtifact type)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			PackageDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package pkg = cloud.getPackage(pkgVersion, pkgId);
		Artifact artifact = null;
		if (type == null) {
			artifact = workspace.createArtifact(pkg);
		} else {
			Artifact t = cloud.getArtifact(type.getVersion(), type.getId());
			artifact = workspace.createArtifact(t, pkg);
		}
		return pojoFactory.createPojo(artifact);
	}

	public Long commitArtifact(long wsid, long id, String message) throws WorkspaceExpiredException,
			IllegalArgumentException, ArtifactDoesNotExistException, ArtifactConflictException, WorkspaceEmptyException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = workspace.getArtifact(id);
		Long result = null;
		result = workspace.commitArtifact(artifact, message);
		return result;
	}

	public void rollbackArtifact(long wsid, long id)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = workspace.getArtifact(id);
		workspace.rollbackArtifact(artifact);
	}

	public Long commitProperty(long wsid, long id, String name, String message)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			PropertyDoesNotExistException, PropertyConflictException, PropertyNotCommitableException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = workspace.getArtifact(id);
		Property property = artifact.getProperty(name);
		Long result = null;
		result = workspace.commitProperty(property, message);
		return result;
	}

	public void rollbackProperty(long wsid, long id, String name) throws WorkspaceExpiredException,
			IllegalArgumentException, ArtifactDoesNotExistException, PropertyDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = workspace.getArtifact(id);
		Property property = artifact.getProperty(name);
		workspace.rollbackProperty(property);
	}

	public PojoPackage[] getPackages(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package[] packages = workspace.getPackages().toArray(new Package[0]);
		return pojoFactory.createPojoArray(packages);
	}

	public PojoPackage getPackage(long wsid, long id)
			throws WorkspaceExpiredException, IllegalArgumentException, PackageDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package _package = workspace.getPackage(id);
		return pojoFactory.createPojo(_package);
	}

	public PojoPackage createPackage(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package _package = workspace.createPackage();
		return pojoFactory.createPojo(_package);
	}

	public PojoPackage createPackage(long wsid, long pkgId, long pkgVersion) throws WorkspaceExpiredException,
			IllegalArgumentException, PackageDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package pkg = cloud.getPackage(pkgVersion, pkgId);
		Package _package = workspace.createPackage(pkg);
		return pojoFactory.createPojo(_package);
	}
	
	public PojoPackage createPackage(long wsid, String name) throws WorkspaceExpiredException, IllegalArgumentException {
    Workspace workspace = cloud.getWorkspace(wsid);
    Package _package = workspace.createPackage(name);
    return pojoFactory.createPojo(_package);
  }

  public PojoPackage createPackage(long wsid, long pkgId, long pkgVersion, String name) throws WorkspaceExpiredException,
      IllegalArgumentException, PackageDoesNotExistException, VersionDoesNotExistException {
    Workspace workspace = cloud.getWorkspace(wsid);
    Package pkg = cloud.getPackage(pkgVersion, pkgId);
    Package _package = workspace.createPackage(pkg, name);
    return pojoFactory.createPojo(_package);
  }

	public PojoProject[] getProjects(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Project[] projects = workspace.getProjects().toArray(new Project[0]);
		return pojoFactory.createPojoArray(projects);
	}

	public PojoProject getProject(long wsid, long id)
			throws WorkspaceExpiredException, IllegalArgumentException, ProjectDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Project project = workspace.getProject(id);
		return pojoFactory.createPojo(project);
	}

	public PojoProject createProject(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Project project = workspace.createProject();
		return pojoFactory.createPojo(project);
	}
	
	public PojoProject createProject(long wsid, String name) throws WorkspaceExpiredException, IllegalArgumentException {
    Workspace workspace = cloud.getWorkspace(wsid);
    Project project = workspace.createProject(name);
    return pojoFactory.createPojo(project);
  }

	public PojoProject createProject(long wsid, long pkgId, long pkgVersion) throws WorkspaceExpiredException,
			IllegalArgumentException, PackageDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package pkg = cloud.getPackage(pkgVersion, pkgId);
		Project project = workspace.createProject(pkg);
		return pojoFactory.createPojo(project);
	}

	public PojoMetaModel[] getMetaModels(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		MetaModel[] metamodels = workspace.getMetaModels().toArray(new MetaModel[0]);
		return pojoFactory.createPojoArray(metamodels);
	}

	public PojoMetaModel getMetaModel(long wsid, long id)
			throws WorkspaceExpiredException, IllegalArgumentException, MetaModelDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		MetaModel metamodel = workspace.getMetaModel(id);
		return pojoFactory.createPojo(metamodel);
	}

	public PojoMetaModel getMetaMetaModel(long wsid, long aid) {
		Workspace workspace = cloud.getWorkspace(wsid);
		MetaModel metamodel = workspace.getMetaMetaModel(workspace.getArtifact(aid));
		return pojoFactory.createPojo(metamodel);
	}

	public PojoMetaModel createMetaModel(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		MetaModel metamodel = workspace.createMetaModel();
		return pojoFactory.createPojo(metamodel);
	}

	public PojoMetaModel createMetaModel(long wsid, long pkgId, long pkgVersion) throws WorkspaceExpiredException,
			IllegalArgumentException, PackageDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package pkg = cloud.getPackage(pkgVersion, pkgId);
		MetaModel metamodel = workspace.createMetaModel(pkg);
		return pojoFactory.createPojo(metamodel);
	}

	public PojoCollectionArtifact[] getCollectionArtifacts(long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		CollectionArtifact[] collectionArtifacts = workspace.getCollectionArtifacts()
				.toArray(new CollectionArtifact[0]);
		return pojoFactory.createPojoArray(collectionArtifacts);
	}

	public PojoCollectionArtifact getCollectionArtifact(long wsid, long id)
			throws WorkspaceExpiredException, IllegalArgumentException, CollectionArtifactDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		CollectionArtifact collectionArtifact = workspace.getCollectionArtifact(id);
		return pojoFactory.createPojo(collectionArtifact);
	}

	public PojoCollectionArtifact createCollectionArtifact(long wsid, Boolean containsOnlyArtifacts)
			throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		CollectionArtifact collectionArtifact = workspace.createCollection(containsOnlyArtifacts);
		return pojoFactory.createPojo(collectionArtifact);
	}

	public PojoCollectionArtifact createCollectionArtifact(long wsid, long pkgId, long pkgVersion,
			Boolean containsOnlyArtifacts) throws WorkspaceExpiredException, IllegalArgumentException,
					PackageDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package pkg = cloud.getPackage(pkgVersion, pkgId);
		CollectionArtifact collectionArtifact = workspace.createCollection(containsOnlyArtifacts, pkg);
		return pojoFactory.createPojo(collectionArtifact);
	}

	public PojoCollectionArtifact createCollectionArtifact(long wsid, PojoCollectionArtifactWithParameters params) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package pckg = null;

		if (params.getPackage() != null) {
			pckg = workspace.getPackage(params.getPackage().getId());
		}
		Map<String, Object> properties = new HashMap<>();
		if (params.getPropertyMap() != null) {
			for (Entry<String, PojoObject> entry : params.getPropertyMap().entrySet()) {
				if (entry.getValue() != null && entry.getValue().getObject() instanceof PojoArtifact) {
					Artifact propertyArtifact = workspace
							.getArtifact(((PojoArtifact) entry.getValue().getObject()).getId());
					properties.put(entry.getKey(), propertyArtifact);
				} else {
					properties.put(entry.getKey(), entry.getValue().getObject());
				}
			}
		}
		Collection<Object> elements = new ArrayList<>();
		if (params.getElements() != null) {
			for (PojoObject pObj : params.getElements()) {
				elements.add(dsFactory.createDS(pObj));
			}
		}
		CollectionArtifact artifact = workspace.createCollection(params.isOnlyContainsArtifacts(), pckg, elements,
				properties);
		return pojoFactory.createPojo(artifact);
	}

	public PojoMapArtifact createMapArtifact(long wsid) throws WorkspaceExpiredException {
		Workspace workspace = cloud.getWorkspace(wsid);
		MapArtifact map = workspace.createMap();
		return pojoFactory.createPojo(map);
	}

	public PojoMapArtifact createMapArtifact(long wsid, long pkgId, long pkgVersion)
			throws WorkspaceExpiredException, PackageDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Package pkg = cloud.getPackage(pkgVersion, pkgId);
		MapArtifact map = workspace.createMap(pkg);
		return pojoFactory.createPojo(map);
	}

	public Long commit(long wsid, String message)
			throws WorkspaceExpiredException, IllegalArgumentException, VersionConflictException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Long result = null;
		result = workspace.commitAll(message);
		return result;
	}

	public void rollback(long wsid) throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		workspace.rollbackAll();
	}

	public PojoArtifact[] getArtifactConflicts(long wsid, Long version)
			throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact[] artifacts = null;
		if (version == null) {
			artifacts = workspace.getArtifactConflicts().toArray(new Artifact[0]);
		} else {
			artifacts = workspace.getArtifactConflicts(version).toArray(new Artifact[0]);
		}
		return pojoFactory.createPojoArray(artifacts);
	}

	public PojoProperty[] getPropertyConflicts(long wsid, Long version)
			throws WorkspaceExpiredException, IllegalArgumentException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Property[] properties = null;
		if (version == null) {
			properties = workspace.getPropertyConflicts().toArray(new Property[0]);
		} else {
			properties = workspace.getPropertyConflicts(version).toArray(new Property[0]);
		}
		return pojoFactory.createPojoArray(properties);
	}

	public void close(long wsid)
			throws WorkspaceExpiredException, IllegalArgumentException, WorkspaceNotEmptyException {
		Workspace workspace = cloud.getWorkspace(wsid);
		workspace.close();
	}

	public void setArtifactType(long wsid, long id, long version, PojoArtifact type)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			VersionDoesNotExistException, ArtifactDeadException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, id);
		Artifact t = null;
		if (type != null) {
			t = cloud.getArtifact(type.getVersion(), type.getId());
		}
		artifact.setType(workspace, t);
	}

	public void setArtifactPackage(long wsid, long id, long version, PojoPackage _package)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			PackageDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, id);
		Package pkg = null;
		if (_package != null) {
			pkg = cloud.getPackage(_package.getVersion(), _package.getId());
		}
		artifact.setPackage(workspace, pkg);
	}

	public void deleteArtifact(long wsid, long id, long version) throws WorkspaceExpiredException,
			IllegalArgumentException, ArtifactDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, id);
		artifact.delete(workspace);
	}

	public void undeleteArtifact(long wsid, long id, long version) throws WorkspaceExpiredException,
			IllegalArgumentException, ArtifactDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, id);
		artifact.undelete(workspace);
	}

	public PojoProperty createProperty(long wsid, long id, long version, String name)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			VersionDoesNotExistException, ArtifactDeadException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, id);
		Property property = artifact.createProperty(workspace, name);
		return pojoFactory.createPojo(property);
	}

	public void setPropertyValue(long wsid, long id, long version, String name, PojoObject value)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			PropertyDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException, PropertyDeadException,
			TypeNotSupportedException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, id);
		Object v = null;
		if (value != null) {
			v = dsFactory.createDS(value);
		}
		if (!CloudUtils.isSupportedType(v)) {
			throw new TypeNotSupportedException(v.getClass());
		}
		artifact.setPropertyValue(workspace, name, v);

	}

	public void setPropertyValues(long wsid, long id, long version, PojoPropertyFilter[] properties)
			throws ArtifactDoesNotExistException, ArtifactDeadException, TypeNotSupportedException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, id);
		Map<String, Object> propertyMap = new HashMap<>();
		if (properties != null) {
			for (PojoPropertyFilter pojoProperty : properties) {
				Object obj = dsFactory.createDS(pojoProperty.getValue());
				if (!CloudUtils.isSupportedType(obj)) {
					throw new TypeNotSupportedException(obj.getClass());
				}
				propertyMap.put(pojoProperty.getName(), obj);
			}
		}
		artifact.setPropertyValues(workspace, propertyMap);
	}

	public void deleteProperty(long wsid, long id, long version, String name)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			PropertyDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, id);
		Property property = artifact.getProperty(name);
		property.delete(workspace);
	}

	public void undeleteProperty(long wsid, long id, long version, String name)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			PropertyDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, id);
		Property property = artifact.getProperty(name);
		property.undelete(workspace);
	}

	public void addArtifactToProject(long wsid, long proId, long proVersion, long id, long version)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			ProjectDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Project project = cloud.getProject(proVersion, proId);
		Artifact artifact = cloud.getArtifact(version, id);
		project.addArtifact(workspace, artifact);
	}

	public void removeArtifactFromProject(long wsid, long proId, long proVersion, long id, long version)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			ProjectDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		Workspace workspace = cloud.getWorkspace(wsid);
		Project project = cloud.getProject(proVersion, proId);
		Artifact artifact = cloud.getArtifact(version, id);
		project.removeArtifact(workspace, artifact);
	}

	public void addArtifactToMetamodel(long wsid, long mmId, long mmVersion, long id, long version)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			MetaModelDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		Workspace workspace = cloud.getWorkspace(wsid);
		MetaModel metamodel = cloud.getMetaModel(mmVersion, mmId);
		Artifact artifact = cloud.getArtifact(version, id);
		metamodel.addArtifact(workspace, artifact);
	}

	public void addArtifactsToMetamodel(long wsid, long mmId, long mmVersion, PojoArtifact[] pojoArtifacts) {
		Workspace workspace = cloud.getWorkspace(wsid);
		MetaModel metamodel = cloud.getMetaModel(mmVersion, mmId);
		Collection<Artifact> artifacts = new ArrayList<>();
		for (PojoArtifact pojoArtifact : pojoArtifacts) {
			artifacts.add(workspace.getArtifact(pojoArtifact.getId()));
		}
		metamodel.addArtifacts(workspace, artifacts);
	}

	public void removeArtifactFromMetamodel(long wsid, long mmId, long mmVersion, long id, long version)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			MetaModelDoesNotExistException, VersionDoesNotExistException, ArtifactDeadException {
		Workspace workspace = cloud.getWorkspace(wsid);
		MetaModel metamodel = cloud.getMetaModel(mmVersion, mmId);
		Artifact artifact = cloud.getArtifact(version, id);
		metamodel.removeArtifact(workspace, artifact);
	}

	public void addElementToCollectionArtifact(long wsid, long caId, long caVersion, PojoObject element)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			CollectionArtifactDoesNotExistException, ArtifactDeadException, TypeNotSupportedException {
		CollectionArtifact ca = cloud.getCollectionArtifact(caVersion, caId);
		Workspace workspace = cloud.getWorkspace(wsid);
		Object e = dsFactory.createDS(element);
		if (!CloudUtils.isSupportedType(e)) {
			throw new TypeNotSupportedException(e.getClass());
		}
		ca.addElement(workspace, e);
	}

	public void addElementsToCollectionArtifact(long wsid, long caId, long caVersion, PojoObject[] elements) {
		CollectionArtifact ca = cloud.getCollectionArtifact(caVersion, caId);
		Workspace workspace = cloud.getWorkspace(wsid);
		Collection<Object> collection = new ArrayList<>();
		for (PojoObject element : elements) {
			Object e = dsFactory.createDS(element);
			if (!CloudUtils.isSupportedType(e)) {
				throw new TypeNotSupportedException(e.getClass());
			}
			collection.add(e);
		}
		ca.addElements(workspace, collection);
	}

	public void removeElementFromCollectionArtifact(long wsid, long caId, long caVersion, PojoObject element)
			throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
			CollectionArtifactDoesNotExistException, ArtifactDeadException {
		CollectionArtifact ca = cloud.getCollectionArtifact(caVersion, caId);
		Workspace workspace = cloud.getWorkspace(wsid);
		Object e = dsFactory.createDS(element);
		ca.removeElement(workspace, e);
	}

	public void insertElementAtInCollectionArtifact(long wsid, long caId, long caVersion, PojoObject element,
			long index) throws WorkspaceExpiredException, IllegalArgumentException, ArtifactDoesNotExistException,
					CollectionArtifactDoesNotExistException, ArtifactDeadException, TypeNotSupportedException {
		CollectionArtifact ca = cloud.getCollectionArtifact(caVersion, caId);
		Workspace workspace = cloud.getWorkspace(wsid);
		Object e = dsFactory.createDS(element);
		if (!CloudUtils.isSupportedType(e)) {
			throw new TypeNotSupportedException(e.getClass());
		}
		ca.insertElementAt(workspace, e, index);
	}

	public void removeElementAtInCollectionArtifact(long wsid, long caId, long caVersion, long index)
			throws WorkspaceExpiredException, IllegalArgumentException, CollectionArtifactDoesNotExistException,
			ArtifactDeadException, IndexOutOfBoundsException {
		CollectionArtifact ca = cloud.getCollectionArtifact(caVersion, caId);
		Workspace workspace = cloud.getWorkspace(wsid);
		ca.removeElementAt(workspace, index);
	}

	public PojoObject putInMapArtifact(long wsid, long id, long version, PojoKeyValue keyValue)
			throws WorkspaceExpiredException, MapArtifactDoesNotExistException, VersionDoesNotExistException,
			TypeNotSupportedException, PropertyDeadException {
		Workspace workspace = cloud.getWorkspace(wsid);
		MapArtifact map = cloud.getMapArtifact(version, id);
		Object key = dsFactory.createDS(keyValue.getKey());
		Object value = dsFactory.createDS(keyValue.getValue());
		Object result = map.put(workspace, key, value);
		return pojoFactory.createPojo(result);
	}

	public PojoObject removeInMapArtifact(long wsid, long id, long version, PojoObject pojoKey)
			throws WorkspaceExpiredException, MapArtifactDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		MapArtifact map = cloud.getMapArtifact(version, id);
		Object key = dsFactory.createDS(pojoKey);
		if (!CloudUtils.isSupportedType(key)) {
			throw new TypeNotSupportedException(key.getClass());
		}
		Object result = map.remove(workspace, key);
		return pojoFactory.createPojo(result);
	}

	public void clearMapArtifact(long wsid, long id, long version)
			throws WorkspaceExpiredException, MapArtifactDoesNotExistException, VersionDoesNotExistException {
		Workspace workspace = cloud.getWorkspace(wsid);
		MapArtifact map = cloud.getMapArtifact(version, id);
		map.clear(workspace);
	}

	public PojoObject setMapEntryValue(long wsId, long id, long version, long mid, long mversion, PojoObject pojoValue)
			throws WorkspaceExpiredException, ArtifactDoesNotExistException, VersionDoesNotExistException,
			PropertyDoesNotExistException, ArtifactDeadException, PropertyDeadException {
		Workspace workspace = cloud.getWorkspace(wsId);
		Artifact artifact = cloud.getArtifact(mversion, mid);
		Object oldValue = artifact.getPropertyValue(DefaultMapArtifact.DefaultEntry.VALUE);
		Object value = dsFactory.createDS(pojoValue);
		artifact.setPropertyValue(workspace, DefaultMapArtifact.DefaultEntry.VALUE, value);
		return pojoFactory.createPojo(oldValue);
	}

	public long addListener(long wsid, long interval) throws WorkspaceExpiredException {
		WorkspaceBufferListener wbl = new WorkspaceBufferListener();
		Workspace workspace = cloud.getWorkspace(wsid);
		BufferListenerManager blm = new BufferListenerManager(workspace, wbl, interval);
		workspace.addListener(wbl);
		long lid = wbl.hashCode();
		managers.put(lid, blm);
		return lid;
	}

	public void removeListener(long wsid, long lid) throws WorkspaceExpiredException {
		BufferListenerManager blm = managers.remove(lid);
		if (blm != null) {
			blm.cancel();
			Workspace workspace = cloud.getWorkspace(wsid);
			workspace.removeListener(blm.getListener());
		}
	}

	public PojoChange[] getListenerEvents(long lid) {
		BufferListenerManager blm = managers.get(lid);
		WorkspaceBufferListener wbl = blm.getListener();
		blm.resetTimer();
		Queue<Change> events = wbl.getEvents();
		return pojoFactory.createPojoArray(events.toArray(new Change[events.size()]));
	}

	public PojoArtifactAndProperties[] pullAll(long wsid) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Map<Artifact, Set<Property>> map = workspace.pullAll();
		return pojoFactory.createPojoArray(map);
	}

	public void pullArtifact(long wsid, long version, long artifactId) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, artifactId);
		workspace.pullArtifact(artifact);
	}

	public void pullProperty(long wsid, long version, long artifactId, String propName) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, artifactId);
		Property property = artifact.getProperty(propName);
		workspace.pullProperty(property);
	}

	public void pushAll(long wsid) {
		Workspace workspace = cloud.getWorkspace(wsid);
		workspace.pushAll();
	}

	public void pushArtifact(long wsid, long version, long artifactId) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, artifactId);
		workspace.pushArtifact(artifact);
	}

	public void pushProperty(long wsid, long version, long artifactId, String propName) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Artifact artifact = cloud.getArtifact(version, artifactId);
		Property property = artifact.getProperty(propName);
		workspace.pushProperty(property);
	}

	public PojoWorkspace getParent(long wsid) throws WorkspaceExpiredException {
		Workspace workspace = cloud.getWorkspace(wsid);
		return pojoFactory.createPojo(workspace.getParent());
	}

	public Boolean isClosed(long wsid) {
		Workspace workspace = cloud.getWorkspace(wsid);
		return workspace.isClosed();
	}

	public PropagationType getPull(long wsid) {
		Workspace workspace = cloud.getWorkspace(wsid);
		return workspace.getPull();
	}

	public PropagationType getPush(long wsid) {
		Workspace workspace = cloud.getWorkspace(wsid);
		return workspace.getPush();
	}

	public PojoWorkspace[] getWorkspaceChildren(long wsid) {
		Collection<Workspace> workspaces = cloud.getWorkspace(wsid).getChildren();
		return pojoFactory.createPojoArray(workspaces.toArray(new Workspace[workspaces.size()]));
	}

	public void setParent(long wsid, Long newParentId) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Workspace parent = newParentId == null ? null : cloud.getWorkspace(newParentId);
		workspace.setParent(parent);

	}

	public void setPush(long wsid, PropagationType type) {
		Workspace workspace = cloud.getWorkspace(wsid);
		workspace.setPush(type);
	}

	public void setPull(long wsid, PropagationType type) {
		Workspace workspace = cloud.getWorkspace(wsid);
		workspace.setPull(type);
	}
	
	public void setResourceFullQualifiedName(long wsid, long id, long version, String uri) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Resource resource = (Resource) cloud.getArtifact(version, id);
		resource.setFullQualifiedName(workspace, uri);
	}

	public void addArtifactToContainer(long wsid, long cId, long cVersion, long id, long version) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Container container = (Container) cloud.getArtifact(cVersion, cId);
		Artifact artifact = cloud.getArtifact(version, id);
		container.addArtifact(workspace, artifact);
	}

	public void addArtifactsToContainer(long wsid, long cId, long cVersion, PojoArtifact[] pojoArtifacts) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Container container = (Container) cloud.getArtifact(cVersion, cId);
		Collection<Artifact> artifacts = new ArrayList<>();
		for (PojoArtifact pojoArtifact : pojoArtifacts) {
			artifacts.add(workspace.getArtifact(pojoArtifact.getId()));
		}
		container.addArtifacts(workspace, artifacts);
	}

	public void removeArtifactFromContainer(long wsid, long cId, long cVersion, long id, long version) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Container container = (Container) cloud.getArtifact(cVersion, cId);
		Artifact artifact = cloud.getArtifact(version, id);
		container.removeArtifact(workspace, artifact);
	}

	public PojoResource createResource(long wsid, String fullQualifiedName) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Resource resource = workspace.createResource(fullQualifiedName);
		return pojoFactory.createPojo(resource);
	}

	public PojoResource[] getResources(long wsid) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Resource[] resources = workspace.getResources().toArray(new Resource[0]);
		return pojoFactory.createPojoArray(resources);
	}

	public PojoResource[] getResources(long wsid, String fullQualifiedName) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Resource[] resources = workspace.getResources(fullQualifiedName).toArray(new Resource[0]);
		return pojoFactory.createPojoArray(resources);
	}

	public PojoResource getResource(long wsid, long id) {
		Workspace workspace = cloud.getWorkspace(wsid);
		Resource resource = workspace.getResource(id);
		return pojoFactory.createPojo(resource);
	}

	public PojoResource createResource(long wsid, PojoStringPackageProjectArtifacts request) {
		String fullQualifiedName = request.getFullQualifiedName();
		Package pkg = cloud.getPackage(request.getPkg().getVersion(), request.getPkg().getId());
		Project project = cloud.getProject(request.getProject().getVersion(), request.getProject().getId());
		Collection<Artifact> artifacts = new ArrayList<Artifact>();
		for (PojoArtifact artifact : request.getArtifacts()) {
			artifacts.add(cloud.getArtifact(artifact.getVersion(), artifact.getId()));
		}
		
		Workspace workspace = cloud.getWorkspace(wsid);
		Resource resource = workspace.createResource(fullQualifiedName, pkg, project, artifacts);
		return pojoFactory.createPojo(resource);
	}
}
