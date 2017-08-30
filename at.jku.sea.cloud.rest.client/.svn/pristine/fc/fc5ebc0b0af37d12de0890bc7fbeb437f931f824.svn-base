package at.jku.sea.cloud.rest.client.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Container.Filter;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndFilters;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndProperties;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPropertyFilter;

public class ContainerHandler extends AbstractHandler {

  private static ContainerHandler INSTANCE;
  
  public static ContainerHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ContainerHandler();  
    }
    return INSTANCE;
  }
  
  protected ContainerHandler() {}
  
  public boolean containsArtifact(long id, long version, Artifact artifact) {
    String url = String.format(CONTAINER_ADDRESS + "/id=%d&v=%d/func/contains", id, version);
    PojoArtifact request = pojoFactory.createPojo(artifact);
    try {
      return template.postForEntity(url, request, Boolean.class).getBody();
    } catch (HttpClientErrorException e) {
      throw e;
    }
  }

  public void addArtifact(long wsId, long id, long version, Artifact artifact) {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/containers/id=%d&v=%d/artifacts/id=%d&v=%d", wsId, id,
        version, artifact.getId(), artifact.getVersionNumber());
    try {
      template.put(url, null);
    } catch (HttpClientErrorException e) {
      throw e;
    }
  }

  public void addArtifacts(long wsId, long id, long version, Collection<Artifact> artifacts) {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/containers/id=%d&v=%d/artifacts/add", wsId, id, version);
    try {
      template.put(url, pojoFactory.createPojoArray(artifacts.toArray(new Artifact[0])));
    } catch (HttpClientErrorException e) {
      throw e;
    }
  }

  public void removeArtifact(long wsId, long id, long version, Artifact artifact) {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/containers/id=%d&v=%d/artifacts/id=%d&v=%d", wsId, id,
        version, artifact.getId(), artifact.getVersionNumber());
    try {
      template.delete(url);
    } catch (HttpClientErrorException e) {
      throw e;
    }
  }

  public Collection<Artifact> getArtifacts(long id, long version) {
    String url = String.format(CONTAINER_ADDRESS + "/id=%d&v=%d/artifacts", id, version);
    try {
      PojoArtifact[] pojoArtifacts = template.getForEntity(url, PojoArtifact[].class).getBody();
      Artifact[] artifacts = restFactory.createRestArray(pojoArtifacts);
      return list(artifacts);
    } catch (HttpClientErrorException e) {
      throw e;
    }
  }

  public Collection<Artifact> getArtifacts(long id, long version, Filter filter) {
    String url = String.format(CONTAINER_ADDRESS + "/id=%d&v=%d/artifacts", id, version);
    PojoArtifact[] request = pojoFactory.convertFilter(filter);
    try {
      PojoArtifact[] pojoArtifacts = template.postForEntity(url, request, PojoArtifact[].class).getBody();
      Artifact[] artifacts = restFactory.createRestArray(pojoArtifacts);
      return list(artifacts);
    } catch (HttpClientErrorException e) {
      throw e;
    }
  }

  public Collection<Artifact> getArtifactsWithProperty(long id, long version, String propertyName, Object propertyValue,
      boolean alive, Filter filter) {
    Map<String, Object> propertyToValue = new HashMap<String, Object>();
    propertyToValue.put(propertyName, propertyValue);
    return getArtifactsWithProperty(id, version, propertyToValue, true, filter);
  }

  public Collection<Artifact> getArtifactsWithProperty(long id, long version, Map<String, Object> propertyToValue,
      boolean alive, Filter filter) {
    PojoArtifact[] filters = pojoFactory.convertFilter(filter);
    PojoPropertyFilter[] pojoFilterArray = pojoFactory.createPojo(propertyToValue);
    PojoArtifactAndPropertyFilter apf = new PojoArtifactAndPropertyFilter(filters, pojoFilterArray);

    String url = String.format(CONTAINER_ADDRESS + "/id=%d&v=%d/artifacts/alive=%b/func/filteredbyproperties", id,
        version, alive);

    try {
      PojoArtifact[] pojoArtifacts = template.postForEntity(url, apf, PojoArtifact[].class).getBody();
      Artifact[] artifacts = restFactory.createRestArray(pojoArtifacts);
      return list(artifacts);
    } catch (HttpClientErrorException e) {
      throw e;
    }
  }

  public Collection<Artifact> getArtifactsWithReference(long id, long version, Artifact artifact, Filter filter) {
    PojoArtifact[] filters = pojoFactory.convertFilter(filter);
    PojoArtifactAndFilters paf = new PojoArtifactAndFilters(pojoFactory.createPojo(artifact), filters);

    String url = String.format(CONTAINER_ADDRESS + "/id=%d&v=%d/artifacts/func/filteredbyreference", id, version);
    try {
      PojoArtifact[] pojoArtifacts = template.postForEntity(url, paf, PojoArtifact[].class).getBody();
      Artifact[] artifacts = restFactory.createRestArray(pojoArtifacts);
      return list(artifacts);
    } catch (HttpClientErrorException e) {
      throw e;
    }
  }

  public Map<Artifact, Set<Property>> getArtifactAndProperties(long id, long version) {
    String url = String.format(CONTAINER_ADDRESS + "/id=%d&v=%d/artifactandproperties", id, version);

    try {
      PojoArtifactAndProperties[] pojoArtifactAndProperties = template
          .getForEntity(url, PojoArtifactAndProperties[].class).getBody();
      return restFactory.createRestMap(pojoArtifactAndProperties);
    } catch (HttpClientErrorException e) {
      throw e;
    }
  }

  public Map<Artifact, Map<String, Object>> getArtifactPropertyMap(long id, long version) {
    String url = String.format(CONTAINER_ADDRESS + "/id=%d&v=%d/artifacts/func/mappings", id, version);
    PojoArtifactAndProperties[] art = null;
    Map<Artifact, Map<String, Object>> map = new HashMap<Artifact, Map<String, Object>>();
    try {
      art = template.getForEntity(url, PojoArtifactAndProperties[].class).getBody();
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

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long id, long version, Filter filter) {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long id, long version, String propertyName,
      Object propertyValue, boolean alive, Filter filter) {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(long id, long version,
      Map<String, Object> propertyToValue, boolean alive, Filter filter) {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection<Object[]> getArtifactRepresentations(long id, long version) {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<Object[], Set<Object[]>> getArtifactPropertyMapRepresentations(long id, long version) {
    // TODO Auto-generated method stub
    return null;
  }
}
