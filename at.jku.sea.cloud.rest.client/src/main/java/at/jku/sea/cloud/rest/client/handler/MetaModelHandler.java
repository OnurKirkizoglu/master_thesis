package at.jku.sea.cloud.rest.client.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndProperties;
import at.jku.sea.cloud.rest.pojo.PojoArtifactRepAndPropertiesRep;
import at.jku.sea.cloud.rest.pojo.PojoMetaModel;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoProperty;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class MetaModelHandler extends AbstractHandler {
  
private static MetaModelHandler INSTANCE;
  
  public static MetaModelHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new MetaModelHandler();  
    }
    return INSTANCE;
  }
  
  protected MetaModelHandler() {}

  public void addArtifact(long wsId, long mmId, long mmVersion, long id, long version) throws ArtifactDeadException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/metamodels/id=%d&v=%d/artifacts/id=%d&v=%d", wsId, mmId, mmVersion, id, version);
    try {
      template.put(url, null);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      }
      throw e;
    }
  }

  public void addArtifacts(long wsId, long mmId, long mmVersion, Collection<Artifact> artifacts) {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/metamodels/id=%d&v=%d/artifacts/add", wsId, mmId, mmVersion);
    try {
      template.put(url, pojoFactory.createPojoArray(artifacts.toArray(new Artifact[] {})));
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(mmVersion, mmId);
      }
      throw e;
    }
  }

  public void removeArtifact(long wsId, long mmId, long mmVersion, long id, long version) throws ArtifactDeadException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/metamodels/id=%d&v=%d/artifacts/id=%d&v=%d", wsId, mmId, mmVersion, id, version);
    try {
      template.delete(url);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      }
      throw e;
    }
  }

  public Collection<Artifact> getArtifacts(long id, long version) {
    String url = String.format(METAMODEL_ADDRESS + "/id=%d&v=%d/artifacts", id, version);
    PojoArtifact[] pojoArtifacts = template.getForEntity(url, PojoArtifact[].class).getBody();
    Artifact[] artifacts = restFactory.createRestArray(pojoArtifacts);
    return list(artifacts);
  }

  public MetaModel getMetaModel(long id, long version) {
    String url = String.format(METAMODEL_ADDRESS + "/id=%d&v=%d/metamodel", id, version);
    PojoMetaModel pojoMetaModel = template.getForEntity(url, PojoMetaModel.class).getBody();
    MetaModel metamodel = restFactory.createRest(pojoMetaModel);
    return metamodel;
  }

  public Map<Artifact, Set<Property>> getArtifactAndProperties(long id, long version) {
    String url = String.format(METAMODEL_ADDRESS + "/id=%d&v=%d/artifactandproperties", id, version);
    PojoArtifactAndProperties[] pojoArtifactAndProperties = template.getForEntity(url, PojoArtifactAndProperties[].class).getBody();
    Map<Artifact, Set<Property>> map = restFactory.createRestMap(pojoArtifactAndProperties);
    return map;
  }

  public Collection<Object[]> getArtifactRepresentations(long id, long version) {
    String url = String.format(METAMODEL_ADDRESS + "/id=%d&v=%d/artifactreps", id, version);
    PojoObject[][] pobjs = template.getForEntity(url, PojoObject[][].class).getBody();
    Collection<Object[]> artifacts = new ArrayList<>();
    for (PojoObject[] pobj : pobjs) {
      artifacts.add(restFactory.createRestArray(pobj));
    }
    return artifacts;
  }

  public Map<Object[], Set<Object[]>> getArtifactPropertyMapRepresentations(long id, long version) {
    String url = String.format(METAMODEL_ADDRESS + "/id=%d&v=%d/artifactandpropertymapreps", id, version);
    PojoArtifactRepAndPropertiesRep[] pobjs = template.getForEntity(url, PojoArtifactRepAndPropertiesRep[].class).getBody();
    Map<Object[], Set<Object[]>> map = new HashMap<>();
    for (PojoArtifactRepAndPropertiesRep pobj : pobjs) {
      Object[] artifact = restFactory.createRestArray(pobj.getArtifact());
      Set<Object[]> properties = new HashSet<>();
      for (PojoObject[] prop : pobj.getProperties()) {
        Object[] property = restFactory.createRestArray(prop);
        properties.add(property);
      }
      map.put(artifact, properties);
    }
    return map;
  }

  public Map<Artifact, Map<String, Object>> getArtifactPropertyMap(long id, long version) {
    String url = String.format(METAMODEL_ADDRESS + "/id=%d&v=%d/artifactpropertymap", id, version);
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

}
