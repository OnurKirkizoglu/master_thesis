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
 * DefaultDesignspace.java created on 14.03.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.utils.CloudUtils;

/**
 * @author alexander noehrer
 */
class DefaultCollectionArtifact extends DefaultArtifact implements CollectionArtifact {
  protected static final String FAIL_ADD = "Either Artifact added to Collection holding only non Artifact elements, or Object added to Collection holding only Artifacts";
  private static final String FAIL_REMOVE = "Either trying to remove Artifact from Collection holding only non Artifact elements, or trying to remove Object from Collection holding only Artifacts";

  private static final long serialVersionUID = 1L;

  private final Pattern collElement = Pattern.compile(DataStorage.COLLECTION_ELEMENT_PATTERN);

  private Boolean isContainingArtifacts;

  /**
   * @param dataStorage
   * @param id
   * @param version
   */
  public DefaultCollectionArtifact(final DataStorage dataStorage, final long id, final long version) {
    super(dataStorage, id, version);
    this.isContainingArtifacts = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.CollectionArtifact#isContainingArtifacts()
   */
  @Override
  public boolean isContainingOnlyArtifacts() {
    if (this.isContainingArtifacts == null) {
      this.isContainingArtifacts = this.dataStorage.isReferenceCollection(this.version, this.id);
    }
    return this.isContainingArtifacts;
  }

  @Override
  public void addElement(final Workspace workspace, final Object element) throws ArtifactDeadException, TypeNotSupportedException {
    if (!CloudUtils.isSupportedType(element)) {
      throw new TypeNotSupportedException(element.getClass());
    }
    final boolean isArtifact = element instanceof Artifact;
    if (this.isContainingOnlyArtifacts() && isArtifact) {
      this.dataStorage.addElementToCollection(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), ((Artifact) element).getId());
    } else if (!this.isContainingOnlyArtifacts() && !isArtifact) {
      this.dataStorage.addElementToCollection(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), element);
    } else {
      throw new IllegalArgumentException(FAIL_ADD);
    }

  }

  @Override
  public void addElements(Workspace workspace, Collection<?> elements) throws ArtifactDeadException, TypeNotSupportedException {
    boolean onlyArtifacts = this.isContainingOnlyArtifacts();
    for (Object element : elements) {
      if (!CloudUtils.isSupportedType(element)) {
        throw new TypeNotSupportedException(element.getClass());
      }
      if (onlyArtifacts && !(element instanceof Artifact)) {
        throw new IllegalArgumentException(FAIL_ADD);
      }
    }

    if (onlyArtifacts) {
      Collection<Long> artifactIds = new ArrayList<>();
      for (Object element : elements) {
        artifactIds.add(((Artifact) element).getId());
      }
      this.dataStorage.addElementsToCollection(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), artifactIds);
    } else {
      this.dataStorage.addElementsToCollection(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), elements);
    }
  }

  @Override
  public void removeElement(final Workspace workspace, final Object element) throws ArtifactDeadException {
    if (!CloudUtils.isSupportedType(element) || !this.existsElement(element)) {
      return;
    }
    final boolean isArtifact = element instanceof Artifact;
    if (this.isContainingOnlyArtifacts() && isArtifact) {
      this.dataStorage.removeElementFromCollection(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), ((Artifact) element).getId());
    } else if (!this.isContainingOnlyArtifacts() && !isArtifact) {
      this.dataStorage.removeElementFromCollection(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), element);
    } else {
      throw new IllegalArgumentException(FAIL_REMOVE);
    }
  }

  @Override
  public void insertElementAt(final Workspace workspace, final Object element, final long index) throws ArtifactDeadException, TypeNotSupportedException {
    if (!CloudUtils.isSupportedType(element)) {
      throw new TypeNotSupportedException(element.getClass());
    }
    final boolean isArtifact = element instanceof Artifact;
    if (this.isContainingOnlyArtifacts() && isArtifact) {
      this.dataStorage.insertElementInCollectionAt(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), ((Artifact) element).getId(), index);
    } else if (!this.isContainingOnlyArtifacts() && !isArtifact) {
      this.dataStorage.insertElementInCollectionAt(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), element, index);
    } else {
      throw new IllegalArgumentException(FAIL_ADD);
    }
  }

  @Override
  public void removeElementAt(final Workspace workspace, final long index) throws IndexOutOfBoundsException, ArtifactDeadException {
    this.dataStorage.removeElementAtFromCollection(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), index);
  }

  @Override
  public Object getElementAt(final long index) {
    final Object element = this.dataStorage.getElementAtFromCollection(this.version, this.id, index);
    if (this.isContainingOnlyArtifacts()) {
      return ArtifactFactory.getArtifact(this.dataStorage, this.version, (Long) element);
    }
    return element;
  }

  @Override
  public Collection<?> getElements() {
    final Collection<?> collection = this.dataStorage.getElementsFromCollection(this.version, this.id);
    if (this.isContainingOnlyArtifacts()) {
      final Collection<Artifact> result = new ArrayList<Artifact>(collection.size());
      for (final Object id : collection) {
        result.add(ArtifactFactory.getArtifact(this.dataStorage, this.version, (Long) id));
      }
      return result;
    }
    return collection;
  }

  @Override
  public boolean existsElement(final Object element) {
    if (!CloudUtils.isSupportedType(element)) {
      return false;
    }
    final boolean isArtifact = element instanceof Artifact;
    if (this.isContainingOnlyArtifacts() && isArtifact) {
      return this.dataStorage.existsElementInCollection(this.version, this.id, ((Artifact) element).getId());
    } else if (!this.isContainingOnlyArtifacts() && !isArtifact) {
      return this.dataStorage.existsElementInCollection(this.version, this.id, element);
    }
    return false;
  }

  @Override
  public boolean hasProperty(final String name) {
    if (this.collElement.matcher(name).matches()) {
      return false;
    }
    return super.hasProperty(name);
  }

  @Override
  public boolean isPropertyAlive(final String name) throws PropertyDoesNotExistException {
    if (this.collElement.matcher(name).matches()) {
      return false;
    }
    return super.isPropertyAlive(name);
  }

  @Override
  public Collection<String> getAlivePropertyNames() {
    final Collection<String> propertyNames = super.getAlivePropertyNames();
    final Collection<String> nonCollPropNames = new ArrayList<String>();
    for (final String prop : propertyNames) {
      if (!this.collElement.matcher(prop).matches()) {
        nonCollPropNames.add(prop);
      }
    }
    return nonCollPropNames;
  }

  @Override
  public Collection<String> getAllPropertyNames() {
    final Collection<String> propertyNames = super.getAllPropertyNames();
    final Collection<String> nonCollPropNames = new ArrayList<String>();
    for (final String prop : propertyNames) {
      if (!this.collElement.matcher(prop).matches()) {
        nonCollPropNames.add(prop);
      }
    }
    return nonCollPropNames;
  }

  @Override
  public void setPropertyValue(final Workspace workspace, final String name, final Object value) throws ArtifactDeadException {
    if (!this.collElement.matcher(name).matches()) {
      super.setPropertyValue(workspace, name, value);
    }
  }

  @Override
  public void deleteProperty(final Workspace workspace, final String name) throws PropertyDoesNotExistException {
    if (!this.collElement.matcher(name).matches()) {
      super.deleteProperty(workspace, name);
    }
  }

  @Override
  public void undeleteProperty(final Workspace workspace, final String name) throws PropertyDoesNotExistException {
    if (!this.collElement.matcher(name).matches()) {
      super.undeleteProperty(workspace, name);
    }
  }

  @Override
  public Collection<Property> getAliveProperties() {
    return this.getPropertyObjects(this.getAlivePropertyNames());
  }

  @Override
  public Collection<Property> getAllProperties() {
    return this.getPropertyObjects(this.getAllPropertyNames());
  }

  @Override
  public Long size() {
    return (Long) this.dataStorage.getPropertyValue(this.version, this.id, DataStorage.PROPERTY_COLLECTION_NEXT_IDX);
  }

}
