package at.jku.sea.cloud.rest.server.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoPropertyFilter;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class PropertyHandler {

  @Autowired
  protected Cloud cloud;
  @Autowired
  protected PojoFactory pojoFactory;

  public PojoOwner getOwner(long id, long version, String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, VersionDoesNotExistException {
    Artifact artifact = cloud.getArtifact(version, id);
    Property property = artifact.getProperty(name);
    Owner owner = property.getOwner();
    return pojoFactory.createPojo(owner);
  }

  public PojoTool getTool(long id, long version, String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, VersionDoesNotExistException {
    Artifact artifact = cloud.getArtifact(version, id);
    Property property = artifact.getProperty(name);
    Tool tool = property.getTool();
    return pojoFactory.createPojo(tool);
  }

  public Boolean isAlive(long id, long version, String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, VersionDoesNotExistException {
    Artifact artifact = cloud.getArtifact(version, id);
    Property property = artifact.getProperty(name);
    Boolean isAlive = property.isAlive();
    return isAlive;
  }

  public Boolean isReference(long id, long version, String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, VersionDoesNotExistException {
    Artifact artifact = cloud.getArtifact(version, id);
    Property property = artifact.getProperty(name);
    Boolean isReference = property.isReference();
    return isReference;
  }

  public PojoObject getValue(long id, long version, String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, VersionDoesNotExistException {
    Artifact artifact = cloud.getArtifact(version, id);
    Property property = artifact.getProperty(name);
    Object value = property.getValue();
    return pojoFactory.createPojo(value);
  }
  
  public PojoObject getValueOrNull(long id, long version, String name) {
    Artifact artifact = cloud.getArtifact(version, id);
    Object value = artifact.getPropertyValueOrNull(name);
    return pojoFactory.createPojo(value);
  }

  public PojoProperty[] getProperties(long id, long version, Boolean onlyAlive) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    Artifact artifact = cloud.getArtifact(version, id);
    Property[] properties = null;
    if (onlyAlive) {
      properties = artifact.getAliveProperties().toArray(new Property[0]);
    } else {
      properties = artifact.getAllProperties().toArray(new Property[0]);
    }
    return pojoFactory.createPojoArray(properties);
  }

  public String[] getPropertyNames(long id, long version, Boolean onlyAlive) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    Artifact artifact = cloud.getArtifact(version, id);
    String[] names = null;
    if (onlyAlive) {
      names = artifact.getAlivePropertyNames().toArray(new String[0]);
    } else {
      names = artifact.getAllPropertyNames().toArray(new String[0]);
    }
    return names;
  }

  public Boolean exists(long id, long version, String name) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
    Artifact artifact = cloud.getArtifact(version, id);
    Boolean exists = artifact.hasProperty(name);
    return exists;
  }

  public PojoProperty getProperty(long id, long version, String name) throws ArtifactDoesNotExistException, PropertyDoesNotExistException, VersionDoesNotExistException {
    Artifact artifact = cloud.getArtifact(version, id);
    Property property = artifact.getProperty(name);
    return pojoFactory.createPojo(property);
  }

  public PojoPropertyFilter[] getPropertyMappings(long id, long version, Boolean alive) {
    Artifact artifact = cloud.getArtifact(version, id);
    Map<String, Object> mappings = null;
    if (alive) {
      mappings = artifact.getAlivePropertiesMap();
    } else {
      mappings = artifact.getDeadPropertiesMap();
    }
    return pojoFactory.createPojo(mappings);
  }

  public PojoObject[] getPropertyRepresentation(long id, long version, String name) {
    Object[] property = cloud.getArtifact(version, id).getPropertyRepresentation(name);
    return pojoFactory.createPojoArray(property);
  }

}
