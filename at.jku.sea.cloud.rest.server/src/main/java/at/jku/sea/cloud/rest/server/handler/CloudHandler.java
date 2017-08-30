package at.jku.sea.cloud.rest.server.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.listeners.events.Change;
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
import at.jku.sea.cloud.rest.pojo.PojoPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoResource;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.PojoWorkspace;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;
import at.jku.sea.cloud.rest.server.listeners.CloudBufferListener;

public class CloudHandler {

  @Autowired
  protected Cloud cloud;
  @Autowired
  protected PojoFactory pojoFactory;

  private Map<Long, CloudBufferListener> listeners = new HashMap<Long, CloudBufferListener>();

  public long addListener() {
    CloudBufferListener cbl = new CloudBufferListener();

    cloud.addListener(cbl);
    long id = cbl.hashCode();
    listeners.put(id, cbl);
    return id;
  }

  public PojoArtifact getArtifact(long version, long id) {
    Artifact art = cloud.getArtifact(version, id);
    return PojoFactory.getInstance().createPojo(art);
  }

  public void removeListener(long listenerId) {
    CloudBufferListener cbl = listeners.remove(listenerId);

    if (cbl != null)
      cloud.removeListener(cbl);
  }

  public PojoChange[] getListenerEvents(long lid) {
    CloudBufferListener cbl = listeners.get(lid);
    Queue<Change> events = cbl == null ? new LinkedList<Change>() : cbl.getEvents();
    return pojoFactory.createPojoArray(events.toArray(new Change[events.size()]));
  }

  public PojoVersion[] getAllVersions() {
    List<Version> versions = cloud.getAllVersions();
    return pojoFactory.createPojoArray(versions.toArray(new Version[0]));
  }

  public PojoMetaModel[] getMetaModels(long version) {
    Collection<MetaModel> models = cloud.getMetaModels(version);
    return pojoFactory.createPojoArray(models.toArray(new MetaModel[0]));
  }

  public PojoPackage[] getPackages(long version) {
    Collection<Package> pkg = cloud.getPackages(version);
    return pojoFactory.createPojoArray(pkg.toArray(new Package[0]));
  }

  public PojoProject[] getProjects(long version) {
    Collection<Project> prj = cloud.getProjects(version);
    return pojoFactory.createPojoArray(prj.toArray(new Project[0]));
  }

  public PojoWorkspace[] getWorkspaces() {
    List<Workspace> ws = cloud.getWorkspaces();
    return pojoFactory.createPojoArray(ws.toArray(new Workspace[0]));
  }

  private Artifact[] convert(PojoArtifact[] artifacts) {
    Artifact[] art = new Artifact[artifacts.length];

    for (int i = 0; i < artifacts.length; i++)
      art[i] = cloud.getArtifact(artifacts[i].getVersion(), artifacts[i].getId());

    return art;
  }

  public PojoArtifact[] getArtifact(long version, PojoArtifact[] filters) {
    Artifact[] art = convert(filters);
    Collection<Artifact> result = cloud.getArtifacts(version, art);
    return pojoFactory.createPojoArray(result.toArray(new Artifact[0]));
  }

  public PojoArtifact[] getArtifacts(long version) {
    Collection<Artifact> art = cloud.getArtifacts(version);
    return pojoFactory.createPojoArray(art.toArray(new Artifact[0]));
  }

  public PojoOwner createOwner() {
    Owner owner = cloud.createOwner();
    return pojoFactory.createPojo(owner);
  }

//  public PojoTool createTool() {
//    Tool tool = cloud.createTool(null, null);
//    return pojoFactory.createPojo(tool);
//  }

  public PojoWorkspace getWorkspace(long privateVersionNumber) {
    Workspace ws = cloud.getWorkspace(privateVersionNumber);
    return pojoFactory.createPojo(ws);
  }

  public PojoWorkspace getWorkspace(PojoOwnerToolStringLong pojo) {
    Owner owner = (Owner) cloud.getArtifact(pojo.getOwner().getVersion(), pojo.getOwner().getId());
    Tool tool = (Tool) cloud.getArtifact(pojo.getTool().getVersion(), pojo.getTool().getId());
    String identifier = pojo.getString();
    Long baseVersion = pojo.getLong();
    Workspace workspace;
    if (baseVersion == null) {
      workspace = cloud.getWorkspace(owner, tool, identifier);
    } else {
      workspace = cloud.getWorkspace(owner, tool, identifier, baseVersion);
    }
    return pojoFactory.createPojo(workspace);
  }

  public PojoArtifact[] getHeadArtifacts() {
    Collection<Artifact> art = cloud.getHeadArtifacts();
    return pojoFactory.createPojoArray(art.toArray(new Artifact[0]));
  }

  public PojoArtifact[] getHeadArtifacts(PojoArtifact[] filters) {
    Artifact[] filt = convert(filters);
    Collection<Artifact> art = cloud.getHeadArtifacts(filt);
    return pojoFactory.createPojoArray(art.toArray(new Artifact[0]));
  }

  public PojoArtifact[] getArtifacts(long wid, PojoArtifactAndPropertyFilter filter) {
    Artifact[] artFilters = convert(filter.getArtifacts());

    PojoPropertyFilter[] propertyFilters = filter.getPropertyFilters();
    Map<String, Object> propertyToValue = new HashMap<String, Object>();

    for (int i = 0; i < propertyFilters.length; i++) {
      String name = propertyFilters[i].getName();
      PojoObject pojoObject = propertyFilters[i].getValue();
      propertyToValue.put(name, pojoObject.getObject());
    }

    Collection<Artifact> art = cloud.getArtifactsWithProperty(wid, propertyToValue, artFilters);
    return pojoFactory.createPojoArray(art.toArray(new Artifact[0]));
  }

  public PojoArtifact[] getHeadArtifactsWithProperty(PojoArtifactAndPropertyFilter filter) {
    Artifact[] artFilters = convert(filter.getArtifacts());

    PojoPropertyFilter[] pf = filter.getPropertyFilters();
    Map<String, Object> propertyToValue = new HashMap<String, Object>();

    for (int i = 0; i < pf.length; i++) {
      String name = pf[i].getName();
      PojoObject pojoObject = pf[i].getValue();
      propertyToValue.put(name, pojoObject.getObject());
    }

    Collection<Artifact> art = cloud.getHeadArtifactsWithProperty(propertyToValue, artFilters);
    return pojoFactory.createPojoArray(art.toArray(new Artifact[0]));
  }

  public PojoProperty[] getPropertiesByReference(long version, PojoArtifact artifact) {
    Artifact art = cloud.getArtifact(artifact.getVersion(), artifact.getId());
    Collection<Property> props = cloud.getPropertiesByReference(version, art);

    return pojoFactory.createPojoArray(props.toArray(new Property[0]));
  }

  public PojoPackage getPackage(long version, long id) {
    Package pkg = cloud.getPackage(version, id);

    return pojoFactory.createPojo(pkg);
  }

  public PojoPackage[] getHeadPackages() {
    Collection<Package> pkgs = cloud.getHeadPackages();

    return pojoFactory.createPojoArray(pkgs.toArray(new Package[0]));
  }

  public PojoProject getProject(long version, long id) {
    if (version < 0) {
      Workspace ws = cloud.getWorkspace(version);
      return pojoFactory.createPojo(ws.getProject(id));
    } else
      return pojoFactory.createPojo(cloud.getProject(version, id));
  }

  public PojoMetaModel getMetaModel(long version, long id) {
    if (version < 0) {
      Workspace ws = cloud.getWorkspace(version);
      return pojoFactory.createPojo(ws.getMetaModel(id));
    } else
      return pojoFactory.createPojo(cloud.getMetaModel(version, id));
  }

  public PojoMetaModel[] getHeadMetaModels() {
    Collection<MetaModel> mm = cloud.getHeadMetaModels();
    return pojoFactory.createPojoArray(mm.toArray(new MetaModel[0]));
  }

  public PojoCollectionArtifact getCollectionArtifact(long version, long id) {
    if (version < 0) {
      Workspace ws = cloud.getWorkspace(version);
      return pojoFactory.createPojo(ws.getCollectionArtifact(id));
    } else
      return pojoFactory.createPojo(cloud.getCollectionArtifact(version, id));
  }

  public PojoCollectionArtifact[] getCollectionArtifacts(long version) {
    if (version < 0) {
      Workspace ws = cloud.getWorkspace(version);
      Collection<CollectionArtifact> carts = ws.getCollectionArtifacts();
      return pojoFactory.createPojoArray(carts.toArray(new CollectionArtifact[0]));
    } else {
      Collection<CollectionArtifact> carts = cloud.getCollectionArtifacts(version);
      return pojoFactory.createPojoArray(carts.toArray(new CollectionArtifact[0]));
    }
  }

  public PojoCollectionArtifact[] getHeadCollectionArtifacts() {
    Collection<CollectionArtifact> carts = cloud.getHeadCollectionArtifacts();
    return pojoFactory.createPojoArray(carts.toArray(new CollectionArtifact[0]));
  }

  public PojoVersion[] getPrivateVersions() {
    List<Version> versions = cloud.getPrivateVersions();
    return pojoFactory.createPojoArray(versions.toArray(new Version[0]));
  }

  public PojoVersion[] getArtifactHistoryVersionNumbers(PojoArtifact artifact) {
    Artifact art = cloud.getArtifact(artifact.getVersion(), artifact.getId());
    List<Version> versions = cloud.getArtifactHistoryVersionNumbers(art);
    return pojoFactory.createPojoArray(versions.toArray(new Version[0]));
  }

  public PojoVersion[] getPropertyHistoryVersionNumbers(PojoArtifact artifact, String name) {
    Artifact art = cloud.getArtifact(artifact.getVersion(), artifact.getId());
    List<Version> versions = cloud.getPropertyHistoryVersionNumbers(art, name);
    return pojoFactory.createPojoArray(versions.toArray(new Version[0]));
  }

  public PojoArtifact[] getArtifactDiffs(long version, long previousVersion) {
    Set<Artifact> art = cloud.getArtifactDiffs(version, previousVersion);
    return pojoFactory.createPojoArray(art.toArray(new Artifact[0]));
  }

  public PojoArtifactAndProperties[] getDiffs(long version) {
    Map<Artifact, Set<Property>> arts = cloud.getDiffs(version);
    return pojoFactory.createPojoArray(arts);
  }

  public PojoArtifactAndProperties[] getDiffs(long version, long previousVersion) {
    Map<Artifact, Set<Property>> arts = cloud.getDiffs(version, previousVersion);
    return pojoFactory.createPojoArray(arts);
  }

  public PojoProject[] getHeadProjects() {
    Collection<Project> prj = cloud.getHeadProjects();
    return pojoFactory.createPojoArray(prj.toArray(new Project[0]));
  }

  // public boolean isWorkspaceEmpty(long versionNumber) {
  // return cloud.isWorkspaceEmpty(versionNumber);
  // }
  //
  // public Object[] getArtifactRepresentation(long version, long id) {
  // return cloud.getArtifactRepresentation(version, id);
  // }

  public PojoMapArtifact getMapArtifact(long id, long version) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
    MapArtifact map = cloud.getMapArtifact(version, id);
    return pojoFactory.createPojo(map);
  }

  public PojoMapArtifact[] getMapArtifacts(long version) throws VersionDoesNotExistException {
    Collection<MapArtifact> maps = cloud.getMapArtifacts(version);
    return pojoFactory.createPojoArray(maps.toArray(new MapArtifact[maps.size()]));
  }

  public PojoArtifact[] getArtifactsWithProperty(long version, PojoArtifactAndPropertyFilter filter, boolean alive) {
    Artifact[] artFilters = convert(filter.getArtifacts());

    PojoPropertyFilter[] propertyFilters = filter.getPropertyFilters();
    Map<String, Object> propertyToValue = new HashMap<String, Object>();

    for (int i = 0; i < propertyFilters.length; i++) {
      String name = propertyFilters[i].getName();
      PojoObject pojoObject = propertyFilters[i].getValue();
      propertyToValue.put(name, pojoObject.getObject());
    }

    Collection<Artifact> art = cloud.getArtifactsWithProperty(version, propertyToValue, alive, artFilters);
    return pojoFactory.createPojoArray(art.toArray(new Artifact[0]));
  }

  public PojoArtifact[] getHeadArtifactsWithProperty(PojoArtifactAndPropertyFilter filter, boolean alive) {
    Artifact[] artFilters = convert(filter.getArtifacts());

    PojoPropertyFilter[] pf = filter.getPropertyFilters();
    Map<String, Object> propertyToValue = new HashMap<String, Object>();

    for (int i = 0; i < pf.length; i++) {
      String name = pf[i].getName();
      PojoObject pojoObject = pf[i].getValue();
      propertyToValue.put(name, pojoObject.getObject());
    }

    Collection<Artifact> art = cloud.getHeadArtifactsWithProperty(propertyToValue, alive, artFilters);
    return pojoFactory.createPojoArray(art.toArray(new Artifact[0]));
  }

  public PojoObject[] getArtifactRepresentation(long version, long id) {
    Object[] ob = cloud.getArtifactRepresentation(version, id);
    return pojoFactory.createPojoArray(ob);
  }

  public PojoArtifactAndProperties[] getArtifactsAndPropertyMap(long version, PojoArtifactAndPropertyFilter filter, boolean alive) {
    Artifact[] artFilters = convert(filter.getArtifacts());

    PojoPropertyFilter[] propertyFilters = filter.getPropertyFilters();
    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap = null;
    if (propertyFilters == null) {
      artifactsAndPropertyMap = cloud.getArtifactsAndPropertyMap(version, artFilters);
    } else {
      Map<String, Object> propertyToValue = new HashMap<String, Object>();
      for (int i = 0; i < propertyFilters.length; i++) {
        String name = propertyFilters[i].getName();
        PojoObject pojoObject = propertyFilters[i].getValue();
        propertyToValue.put(name, pojoObject.getObject());
      }
      artifactsAndPropertyMap = cloud.getArtifactsAndPropertyMap(version, propertyToValue, alive, artFilters);
    }

    return pojoFactory.createPojoMappings(artifactsAndPropertyMap);
  }

  public PojoArtifact[] getArtifactsWithReference(long version, PojoArtifactAndFilters artifactAndFilter) {
    Artifact[] artifacts = convert(artifactAndFilter.getFilters());
    PojoArtifact pojoArtifact = artifactAndFilter.getArtifact();
    Collection<Artifact> result = cloud.getArtifactsWithReference(version, cloud.getArtifact(pojoArtifact.getVersion(), pojoArtifact.getId()), artifacts);
    return pojoFactory.createPojoArray(result.toArray(new Artifact[] {}));
  }

  public PojoArtifactAndProperties[] getArtifactsPropertyMap(long version, PojoArtifactAndString artifactsAndProps) {
    Set<String> properties = artifactsAndProps.getProperties();
    Artifact[] artifacts = convert(artifactsAndProps.getArtifacts());

    Map<Artifact, Map<String, Object>> artifactsAndPropertyMap;
    if (properties != null) {
      artifactsAndPropertyMap = cloud.getArtifactsPropertyMap(version, new HashSet<>(Arrays.asList(artifacts)), properties);
    } else {
      artifactsAndPropertyMap = cloud.getArtifactsPropertyMap(version, new HashSet<>(Arrays.asList(artifacts)));
    }
    return pojoFactory.createPojoMappings(artifactsAndPropertyMap);
  }

  public PojoWorkspace[] getWorkspaceChildren(Long privateVersion) throws WorkspaceExpiredException {
    Collection<Workspace> wss = cloud.getWorkspaceChildren(privateVersion);
    return pojoFactory.createPojoArray(wss.toArray(new Workspace[0]));
  }

  public PojoArtifact[] getArtifacts(long version, Set<Long> ids) {
    Collection<Artifact> artifacts = cloud.getArtifacts(version, ids);
    return pojoFactory.createPojoArray(artifacts.toArray(new Artifact[artifacts.size()]));
  }

  public PojoResource getResource(long version, long id) {
    Resource resource = cloud.getResource(version, id);
    return pojoFactory.createPojo(resource);
  }

  public PojoResource[] getResources(long version) {
	  Collection<Resource> resources = cloud.getResources(version);
	  return pojoFactory.createPojoArray(resources.toArray(new Resource[0]));
  }

  public PojoResource[] getResources(long version, String fullQualifiedName) {
	  Collection<Resource> resources = cloud.getResources(version, fullQualifiedName);
	  return pojoFactory.createPojoArray(resources.toArray(new Resource[0]));
  }
}
