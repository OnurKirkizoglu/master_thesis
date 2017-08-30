/*
 * (C) Johannes Kepler University Linz, Austria, 2005-2013
 * Institute for Systems Engineering and Automation (SEA)
 *
 * The software may only be used for academic purposes (teaching, scientific
 * research). Any redistribution or commercialization of the software program
 * and documentation (or any part thereof) requires prior written permission of
 * the JKU. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * This software program and documentation are copyrighted by Johannes Kepler
 * University Linz, Austria (the JKU). The software program and documentation
 * are supplied AS IS, without any accompanying services from the JKU. The JKU
 * does not warrant that the operation of the program will be uninterrupted or
 * error-free. The end-user understands that the program was developed for
 * research purposes and is advised not to rely exclusively on the program for
 * any reason.
 *
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
 * SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
 * ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE
 * AUTHOR HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. THE AUTHOR
 * SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE AUTHOR HAS
 * NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 */

/*
 * DefaultArtifact.java created on 14.03.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.DataStorage.Columns;
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
import at.jku.sea.cloud.utils.CloudUtils;
import at.jku.sea.cloud.utils.StringUtils;

/**
 * @author alexander noehrer
 */
class DefaultArtifact implements Artifact, Serializable {
  private static final long serialVersionUID = 1L;

  protected transient DataStorage dataStorage;

  protected final long id;
  protected final long version;

  public DefaultArtifact(final DataStorage dataStorage, final long id, final long version) {
    super();
    if (dataStorage == null) {
      throw new IllegalArgumentException("dataStorage == null");
    }
    this.dataStorage = dataStorage;
    this.id = id;
    this.version = version;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#getId()
   */
  @Override
  public long getId() {
    return this.id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#getVersionNumber()
   */
  @Override
  public long getVersionNumber() {
    return this.version;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#getType()
   */
  @Override
  public Artifact getType() {
    final Long id = this.dataStorage.getArtifactType(this.version, this.id);
    if (id == null) {
      return null;
    }
    return ArtifactFactory.getArtifact(this.dataStorage, this.version, id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#getNamespace()
   */
  @Override
  public Package getPackage() {
    final Long id = this.dataStorage.getArtifactContainer(this.version, this.id);
    if (id == null) {
      return null;
    }
    return (Package) ArtifactFactory.getArtifact(this.dataStorage, this.version, id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#isAlive()
   */
  @Override
  public boolean isAlive() {
    return this.dataStorage.isArtifactAlive(this.version, this.id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#hasProperty(java.lang.String)
   */
  @Override
  public boolean hasProperty(final String property) {
    return this.dataStorage.hasProperty(this.version, this.id, property);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#isPropertyAlive(java.lang.String)
   */
  @Override
  public boolean isPropertyAlive(final String property) throws PropertyDoesNotExistException {
    return this.dataStorage.isPropertyAlive(this.version, this.id, property);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#getPropertyValue(java.lang.String)
   */
  @Override
  public Object getPropertyValue(final String property) throws PropertyDoesNotExistException, PropertyDeadException {
    // if (this.dataStorage.isPropertyReference(this.version, this.id, property)) {
    // }
    // return this.dataStorage.getPropertyValue(this.version, this.id, property);
    Object[] propertyRep = this.dataStorage.getPropertyRepresentation(version, id, property);
    return getValueOfProperty(propertyRep);
  }

  @Override
  public Object getPropertyValueOrNull(String name) {
    Object[] propertyRep = this.dataStorage.getPropertyRepresentationOrNull(version, this.id, name);
    Object value = propertyRep == null ? null : getValueOfProperty(propertyRep);
    return value;
  }
  
  private Object getValueOfProperty(final Object[] propertyRep) {
    boolean isReference = propertyRep[Columns.PROPERTY_REFERENCE.getIndex()] != null;
    if (isReference) {
      return ArtifactFactory.getArtifact(this.dataStorage, this.version, (Long) propertyRep[Columns.PROPERTY_REFERENCE.getIndex()]);
    } else {
      return propertyRep[Columns.PROPERTY_VALUE.getIndex()];
    }
  }

  @Override
  public Object[] getPropertyRepresentation(String name) throws PropertyDoesNotExistException {
    return this.dataStorage.getPropertyRepresentation(this.version, this.id, name);
  }

  @Override
  public Object[] getRepresentation() {
    return this.dataStorage.getArtifactRepresentation(this.version, this.id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#getProperties()
   */
  @Override
  public Collection<String> getAlivePropertyNames() {
    return this.dataStorage.getAlivePropertyNames(this.version, this.id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#getAllProperties()
   */
  @Override
  public Collection<String> getAllPropertyNames() {
    return this.dataStorage.getAllPropertyNames(this.version, this.id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#delete(at.jku.sea.cloud.Workspace)
   */
  @Override
  public void delete(final Workspace workspace) {
    this.dataStorage.setArtifactAlive(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.id, false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#undelete(at.jku.sea.cloud.Workspace)
   */
  @Override
  public void undelete(final Workspace workspace) {
    this.dataStorage.setArtifactAlive(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.id, true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#setPropertyValue(at.jku.sea.cloud.Workspace, java.lang.String, java.lang.Object)
   */
  @Override
  public void setPropertyValue(final Workspace workspace, final String property, final Object value) throws ArtifactDoesNotExistException, ArtifactDeadException, TypeNotSupportedException {
    if (!CloudUtils.isSupportedType(value)) {
      throw new TypeNotSupportedException(value.getClass());
    }
    if (value instanceof Artifact) {
      this.dataStorage.setPropertyReference(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.id, property, ((Artifact) value).getId());
    } else {
      this.dataStorage.setPropertyValue(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.id, property, value);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#setPropertyValue(at.jku.sea.cloud.Workspace, java.util.Map)
   */

  @Override
  public void setPropertyValues(final Workspace workspace, final Map<String, Object> properties) throws PropertyDeadException, ArtifactDoesNotExistException, ArtifactDeadException,
      TypeNotSupportedException {
    final Map<String, Object> valueMap = new HashMap<>();
    final Map<String, Long> referenceMap = new HashMap<>();
    for (final Entry<String, Object> entry : properties.entrySet()) {
      if ((entry.getValue() != null) && (entry.getValue() instanceof Artifact)) {
        referenceMap.put(entry.getKey(), ((Artifact) entry.getValue()).getId());
      } else {
        if (!CloudUtils.isSupportedType(entry.getValue())) {
          throw new TypeNotSupportedException(entry.getValue().getClass());
        }
        valueMap.put(entry.getKey(), entry.getValue());
      }
    }
    this.dataStorage.setPropertyValues(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.id, valueMap, referenceMap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#deleteProperty(at.jku.sea.cloud.Workspace, java.lang.String)
   */
  @Override
  public void deleteProperty(final Workspace workspace, final String property) throws PropertyDoesNotExistException {
    this.dataStorage.setPropertyAlive(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.id, property, false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#undeleteProperty(at.jku.sea.cloud.Workspace, java.lang.String)
   */
  @Override
  public void undeleteProperty(final Workspace workspace, final String property) throws PropertyDoesNotExistException {
    this.dataStorage.setPropertyAlive(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.id, property, true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Artifact#setNamespace(at.jku.sea.cloud.Workspace, at.jku.sea.cloud.Designspace)
   */
  @Override
  public void setPackage(final Workspace workspace, final Package pkg) throws ArtifactDeadException {
    Long id;
    if (pkg == null) {
      id = null;
    } else {
      id = pkg.getId();
    }
    this.dataStorage.setArtifactContainer(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.id, id);
  }

  @Override
  public Owner getOwner() {
    final Long id = this.dataStorage.getArtifactOwner(this.version, this.id);
    if (id == null) {
      return null;
    }
    return ArtifactFactory.getArtifact(this.dataStorage, this.version, id);
  }

  @Override
  public Property createProperty(final Workspace workspace, final String property) throws ArtifactDeadException {
    if (!this.dataStorage.hasProperty(this.version, this.id, property)) {
      this.dataStorage.setPropertyValue(this.version, workspace.getOwner().getId(), workspace.getTool().getId(), this.id, property, null);
    }
    return new DefaultProperty(this.dataStorage, this.id, this.version, property);
  }

  @Override
  public Property getProperty(final String property) throws PropertyDoesNotExistException {
    if (this.dataStorage.hasProperty(this.version, this.id, property)) {
      return new DefaultProperty(this.dataStorage, this.id, this.version, property);
    }
    throw new PropertyDoesNotExistException(this.version, this.id, property);
  }

  protected Collection<Property> getPropertyObjects(final Collection<String> propertyNames) {
    final Collection<Property> properties = new ArrayList<Property>(propertyNames.size());
    for (final String name : propertyNames) {
      properties.add(new DefaultProperty(this.dataStorage, this.id, this.version, name));
    }
    return properties;
  }

  @Override
  public Collection<Property> getAliveProperties() {
    return this.getPropertyObjects(this.getAlivePropertyNames());
  }

  @Override
  public Map<String, Object> getAlivePropertiesMap() {
    Collection<Object[]> properties = this.dataStorage.getAllPropertyRepresentations(version, null, this.id, true);
    Map<String, Object> values = new HashMap<>();
    for (Object[] property : properties) {
      String name = (String) property[Columns.PROPERTY_NAME.getIndex()];
      Object value = property[Columns.PROPERTY_VALUE.getIndex()];
      Long reference = (Long) property[Columns.PROPERTY_REFERENCE.getIndex()];
      if (value != null || (value == null && reference == null)) {
        values.put(name, value);
      } else {
        values.put(name, ArtifactFactory.getArtifact(dataStorage, this.version, reference));
      }
    }
    return values;
  }

  @Override
  public Map<String, Object> getDeadPropertiesMap() {
    Collection<Object[]> properties = this.dataStorage.getAllPropertyRepresentations(version, null, this.id, false);
    Map<String, Object> values = new HashMap<>();
    for (Object[] property : properties) {
      String name = (String) property[Columns.PROPERTY_NAME.getIndex()];
      Object value = property[Columns.PROPERTY_VALUE.getIndex()];
      Long reference = (Long) property[Columns.PROPERTY_REFERENCE.getIndex()];
      if (value != null || (value == null && reference == null)) {
        values.put(name, value);
      } else {
        values.put(name, ArtifactFactory.getArtifact(dataStorage, this.version, reference));
      }
    }
    return values;
  }

  @Override
  public Collection<Property> getAllProperties() {
    return this.getPropertyObjects(this.getAllPropertyNames());
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
    final DefaultArtifact other = (DefaultArtifact) obj;
    if (this.id != other.id) {
      return false;
    }
    if (this.version != other.version) {
      return false;
    }
    return true;
  }

  @Override
  public Tool getTool() {
    final Long id = this.dataStorage.getArtifactTool(this.version, this.id);
    if (id == null) {
      return null;
    }
    return (Tool) ArtifactFactory.getArtifact(this.dataStorage, this.version, id);
  }

  @Override
  public void setType(final Workspace workspace, final Artifact type) throws ArtifactDeadException {
    Long id;
    if (type == null) {
      id = null;
    } else {
      id = type.getId();
    }
    this.dataStorage.setArtifactType(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.id, id);
  }

  @Override
  public void addToProject(final Workspace workspace, final Project project) throws ArtifactDeadException {
    project.addArtifact(workspace, this);
  }

  @Override
  public void removeFromProject(final Workspace workspace, final Project project) throws ArtifactDeadException {
    project.removeArtifact(workspace, this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return StringUtils.printf("%s[version=%s, id=%s]", this.getClass().getSimpleName(), this.version, this.id);
  }

}
