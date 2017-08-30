package at.jku.sea.cloud.rest.client;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.client.handler.ArtifactHandler;
import at.jku.sea.cloud.utils.StringUtils;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestArtifact implements Artifact {

  private static final long serialVersionUID = 1L;

  protected long id;
  protected long version;

  private ArtifactHandler handler;

  protected RestArtifact(long id, long version) {
    this.id = id;
    this.version = version;
    handler = ArtifactHandler.getInstance();
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public long getVersionNumber() {
    return version;
  }

  @Override
  public Owner getOwner() {
    return handler.getOwner(id, version);
  }

  @Override
  public Tool getTool() {
    return handler.getTool(id, version);
  }

  @Override
  public Artifact getType() {
    return handler.getType(id, version);
  }

  @Override
  public Package getPackage() {
    return handler.getPackage(id, version);
  }

  @Override
  public void setType(Workspace workspace, Artifact type) throws ArtifactDeadException {
    handler.setType(workspace.getId(), id, version, type);
  }

  @Override
  public boolean isAlive() {
    return handler.isAlive(id, version);
  }

  @Override
  public boolean hasProperty(String name) {
    return handler.hasProperty(id, version, name);
  }

  @Override
  public boolean isPropertyAlive(String name) throws PropertyDoesNotExistException {
    return handler.isPropertyAlive(id, version, name);
  }

  @Override
  public Object getPropertyValue(String name) throws PropertyDoesNotExistException {
    return handler.getPropertyValue(id, version, name);
  }
  

  @Override
  public Object getPropertyValueOrNull(String name) {
    return handler.getPropertyValueOrNull(id, version, name);
  }

  @Override
  public Object[] getPropertyRepresentation(String name) throws PropertyDoesNotExistException {
    return handler.getPropertyRepresentation(id, version, name);
  }

  @Override
  public Object[] getRepresentation() {
    return handler.getRepresentation(id, version);
  }

  @Override
  public Collection<String> getAlivePropertyNames() {
    return handler.getAlivePropertyNames(id, version);
  }

  @Override
  public Collection<String> getAllPropertyNames() {
    return handler.getAllPropertyNames(id, version);
  }

  @Override
  public Map<String, Object> getAlivePropertiesMap() {
    return handler.getAlivePropertyMap(id, version);
  }

  @Override
  public Map<String, Object> getDeadPropertiesMap() {
    return handler.getDeadPropertyMap(id, version);
  }

  @Override
  public void delete(Workspace workspace) {
    handler.delete(workspace.getId(), id, version);
  }

  @Override
  public void undelete(Workspace workspace) {
    handler.undelete(workspace.getId(), id, version);
  }

  @Override
  public void setPropertyValue(Workspace workspace, String name, Object value) throws ArtifactDoesNotExistException, ArtifactDeadException, PropertyDeadException, TypeNotSupportedException {
    handler.setPropertyValue(workspace.getId(), id, version, name, value);
  }

  @Override
  public void setPropertyValues(Workspace workspace, Map<String, Object> properties) throws ArtifactDoesNotExistException, ArtifactDeadException, PropertyDeadException, TypeNotSupportedException {
    handler.setPropertyValues(workspace.getId(), id, version, properties);
  }

  @Override
  public void deleteProperty(Workspace workspace, String name) throws PropertyDoesNotExistException {
    handler.deleteProperty(workspace.getId(), id, version, name);
  }

  @Override
  public void undeleteProperty(Workspace workspace, String name) throws PropertyDoesNotExistException {
    handler.undeleteProperty(workspace.getId(), id, version, name);
  }

  @Override
  public void setPackage(Workspace workspace, Package pkg) throws ArtifactDeadException {
    handler.setPackage(workspace.getId(), id, version, pkg);
  }

  @Override
  public Property createProperty(Workspace workspace, String name) throws ArtifactDeadException {
    return handler.createProperty(workspace.getId(), id, version, name);
  }

  @Override
  public Property getProperty(String name) throws PropertyDoesNotExistException {
    return handler.getProperty(id, version, name);
  }

  @Override
  public Collection<Property> getAliveProperties() {
    return handler.getAliveProperties(id, version);
  }

  @Override
  public Collection<Property> getAllProperties() {
    return handler.getAllProperties(id, version);
  }

  @Override
  public void addToProject(Workspace workspace, Project project) throws ArtifactDeadException {
    handler.addToProject(workspace.getId(), project.getId(), project.getVersionNumber(), id, version);
  }

  @Override
  public void removeFromProject(Workspace workspace, Project project) throws ArtifactDeadException {
    handler.removeFromProject(workspace.getId(), project.getId(), project.getVersionNumber(), id, version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.version, this.id);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final RestArtifact other = (RestArtifact) obj;
    if (this.id != other.id) {
      return false;
    }
    if (this.version != other.version) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return StringUtils.printf("%s[version=%s, id=%s]", this.getClass().getSimpleName(), this.version, this.id);
  }


}
