package at.jku.sea.cloud.rest.client.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.utils.CloudUtils;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class ArtifactHandler extends AbstractHandler {
  
  private static ArtifactHandler INSTANCE;
  
  public static ArtifactHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ArtifactHandler();  
    }
    return INSTANCE;
  }
  
  protected ArtifactHandler() {}

  public Owner getOwner(long id, long version) {
    String url = String.format(ARTIFACT_ADDRESS + "/id=%d&v=%d/owner", id, version);
    PojoOwner pojoOwner = template.getForEntity(url, PojoOwner.class).getBody();
    return restFactory.createRest(pojoOwner);
  }

  public Tool getTool(long id, long version) {
    String url = String.format(ARTIFACT_ADDRESS + "/id=%d&v=%d/tool", id, version);
    PojoTool pojoTool = template.getForEntity(url, PojoTool.class).getBody();
    return restFactory.createRest(pojoTool);
  }

  public Artifact getType(long id, long version) {
    String url = String.format(ARTIFACT_ADDRESS + "/id=%d&v=%d/type", id, version);
    PojoArtifact pojoArtifact = template.getForEntity(url, PojoArtifact.class).getBody();
    return restFactory.createRest(pojoArtifact);
  }

  public Package getPackage(long id, long version) {
    String url = String.format(ARTIFACT_ADDRESS + "/id=%d&v=%d/package", id, version);
    PojoPackage pojoPackage = null;
    try {
      pojoPackage = template.getForEntity(url, PojoPackage.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(NullPointerException.class.getSimpleName())) {
        return null;
      }
      throw e;
    }
    return restFactory.createRest(pojoPackage);
  }

  public void setType(long wsId, long id, long version, Artifact type) throws ArtifactDeadException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/type", wsId, id, version);
    PojoArtifact pojoArtifact = null;
    if (type != null) {
      pojoArtifact = pojoFactory.createPojo(type);
    }
    try {
      template.put(url, pojoArtifact);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      }
      throw e;
    }
  }

  public boolean isAlive(long id, long version) {
    String url = String.format(ARTIFACT_ADDRESS + "/id=%d&v=%d/alive", id, version);
    Boolean isAlive = template.getForEntity(url, Boolean.class).getBody();
    return isAlive;
  }

  public boolean hasProperty(long id, long version, String name) {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/func/exists", id, version, name);
    Boolean hasProperty = template.getForEntity(url, Boolean.class).getBody();
    return hasProperty;
  }

  public boolean isPropertyAlive(long id, long version, String name) throws PropertyDoesNotExistException {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/alive", id, version, name);
    Boolean isAlive;
    try {
      isAlive = template.getForEntity(url, Boolean.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
        throw new PropertyDoesNotExistException(version, id, name);
      }
      throw e;
    }
    return isAlive;
  }
  
  public Object getPropertyValue(long id, long version, String name) throws PropertyDoesNotExistException {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/value", id, version, name);
    PojoObject pojoObject = null;
    try {
      pojoObject = template.getForEntity(url, PojoObject.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
        throw new PropertyDoesNotExistException(version, id, name);
      }
      throw e;
    }
    return restFactory.createRest(pojoObject);
  }
  
  public Object getPropertyValueOrNull(long id, long version, String name) {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/valueornull", id, version, name);
    PojoObject pojoObject = null;
    try {
      pojoObject = template.getForEntity(url, PojoObject.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
        throw new PropertyDoesNotExistException(version, id, name);
      }
      throw e;
    }
    return restFactory.createRest(pojoObject);
  }

  public Collection<String> getAlivePropertyNames(long id, long version) {
    String url = String.format(PROPERTY_ADDRESS + "/func/names?onlyalive=true", id, version);
    String[] propertyNames = template.getForEntity(url, String[].class).getBody();
    return list(propertyNames);
  }

  public Collection<String> getAllPropertyNames(long id, long version) {
    String url = String.format(PROPERTY_ADDRESS + "/func/names", id, version);
    String[] propertyNames = template.getForEntity(url, String[].class).getBody();
    return list(propertyNames);
  }

  public void delete(long wsId, long id, long version) {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d", wsId, id, version);
    template.delete(url);
  }

  public void undelete(long wsId, long id, long version) {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/func/undelete", wsId, id, version);
    template.put(url, null);
  }

  public void setPropertyValue(long wsId, long id, long version, String name, Object value) throws ArtifactDoesNotExistException, ArtifactDeadException, PropertyDeadException,
      TypeNotSupportedException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/properties/name=%s/value", wsId, id, version, name);
    if (!CloudUtils.isSupportedType(value)) {
      throw new TypeNotSupportedException(value.getClass());
    }
    PojoObject pojoObject = null;
    if (value != null) {
      pojoObject = pojoFactory.createPojo(value);
    }
    try {
      template.put(url, pojoObject);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
        throw new ArtifactDoesNotExistException(version, id);
      } else if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      } else if (e.getResponseBodyAsString().equals(PropertyDeadException.class.getSimpleName())) {
        throw new PropertyDeadException(version, id, name);
      } else if (e.getResponseBodyAsString().equals(TypeNotSupportedException.class.getSimpleName())) {
        throw new TypeNotSupportedException(value.getClass());
      }
      throw e;
    }
  }

  public void setPropertyValues(long wsId, long id, long version, Map<String, Object> properties) throws ArtifactDoesNotExistException, ArtifactDeadException, TypeNotSupportedException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/properties/func/setvalues", wsId, id, version);
    PojoPropertyFilter[] props = new PojoPropertyFilter[properties.size()];
    int i = 0;
    for (Entry<String, Object> entry : properties.entrySet()) {
      if (!CloudUtils.isSupportedType(entry.getValue())) {
        throw new TypeNotSupportedException(entry.getValue().getClass());
      }
      props[i++] = pojoFactory.createPojo(entry);
    }
    try {
      template.put(url, props);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
        throw new ArtifactDoesNotExistException(version, id);
      } else if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      } else if (e.getResponseBodyAsString().equals(PropertyDeadException.class.getSimpleName())) {
        throw new PropertyDeadException("One property in map is dead.");
      } else if (e.getResponseBodyAsString().equals(TypeNotSupportedException.class.getSimpleName())) {
        throw new TypeNotSupportedException();
      }
      throw e;
    }
  }

  public Object[] getPropertyRepresentation(long id, long version, String name) {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/representation", id, version, name);
    try {
      PojoObject[] pobj = template.getForEntity(url, PojoObject[].class).getBody();
      return restFactory.createRestArray(pobj);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
        throw new ArtifactDoesNotExistException(version, id);
      } else if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
        throw new PropertyDoesNotExistException(version, id, name);
      } 
      throw e;
    }
  }

  public Object[] getRepresentation(long id, long version) {
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

  public void deleteProperty(long wsId, long id, long version, String name) throws PropertyDoesNotExistException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/properties/name=%s", wsId, id, version, name);
    try {
      template.delete(url);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
        throw new PropertyDoesNotExistException(version, id, name);
      }
      throw e;
    }
  }

  public void undeleteProperty(long wsId, long id, long version, String name) throws PropertyDoesNotExistException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/properties/name=%s/func/undelete", wsId, id, version, name);
    try {
      template.put(url, null);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
        throw new PropertyDoesNotExistException(version, id, name);
      }
      throw e;
    }
  }

  public void setPackage(long wsId, long id, long version, Package _package) throws ArtifactDeadException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/package", wsId, id, version);
    PojoPackage pojoPackage = null;
    if (_package != null) {
      pojoPackage = pojoFactory.createPojo(_package);
    }
    try {
      template.put(url, pojoPackage);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      }
      throw e;
    }
  }

  public Property createProperty(long wsId, long id, long version, String name) throws ArtifactDeadException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/properties/name=%s", wsId, id, version, name);
    PojoProperty pojoProperty = null;
    try {
      pojoProperty = template.postForEntity(url, null, PojoProperty.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      }
      throw e;
    }
    return restFactory.createRest(pojoProperty);
  }

  public Property getProperty(long id, long version, String name) throws PropertyDoesNotExistException {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s", id, version, name);
    PojoProperty pojoProperty = null;
    try {
      pojoProperty = template.getForEntity(url, PojoProperty.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
        throw new PropertyDoesNotExistException(version, id, name);
      }
      throw e;
    }
    return restFactory.createRest(pojoProperty);
  }

  public Collection<Property> getAliveProperties(long id, long version) {
    String url = String.format(PROPERTY_ADDRESS + "?onlyalive=true", id, version);
    PojoProperty[] pojoProperties = template.getForEntity(url, PojoProperty[].class).getBody();
    Property[] properties = restFactory.createRestArray(pojoProperties);
    return list(properties);
  }

  public Collection<Property> getAllProperties(long id, long version) {
    String url = String.format(PROPERTY_ADDRESS, id, version);
    PojoProperty[] pojoProperties = template.getForEntity(url, PojoProperty[].class).getBody();
    Property[] properties = restFactory.createRestArray(pojoProperties);
    return list(properties);
  }

  // /designspace/artifacts/id=%d&v=%d/properties
  public Map<String, Object> getAlivePropertyMap(long id, long version) {
    String url = String.format(PROPERTY_ADDRESS + "/mappings", id, version);
    PojoPropertyFilter[] properties = template.getForEntity(url, PojoPropertyFilter[].class).getBody();
    Map<String, Object> propertyMap = new HashMap<>();
    for (PojoPropertyFilter pojoProperty : properties) {
      Object obj = restFactory.createRest(pojoProperty.getValue());
      propertyMap.put(pojoProperty.getName(), obj);
    }
    return propertyMap;
  }

  public Map<String, Object> getDeadPropertyMap(long id, long version) {
    String url = String.format(PROPERTY_ADDRESS + "/mappings?alive=%b", id, version, false);
    PojoPropertyFilter[] properties = template.getForEntity(url, PojoPropertyFilter[].class).getBody();
    Map<String, Object> propertyMap = new HashMap<>();
    for (PojoPropertyFilter pojoProperty : properties) {
      Object obj = restFactory.createRest(pojoProperty.getValue());
      propertyMap.put(pojoProperty.getName(), obj);
    }
    return propertyMap;
  }

  public void addToProject(long wsId, long proId, long proVersion, long id, long version) throws ArtifactDeadException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/projects/id=%d&v=%d/artifacts/id=%d&v=%d", wsId, proId, proVersion, id, version);
    try {
      template.put(url, null);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      }
      throw e;
    }
  }

  public void removeFromProject(long wsId, long proId, long proVersion, long id, long version) throws ArtifactDeadException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/projects/id=%d&v=%d/artifacts/id=%d&v=%d", wsId, proId, proVersion, id, version);
    try {
      template.delete(url);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      }
      throw e;
    }
  }

}
