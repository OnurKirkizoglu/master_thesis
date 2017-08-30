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
 * DefaultTool.java created on 14.07.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.implementation;

import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.utils.CloudUtils;

/**
 * @author alexander noehrer
 */
class DefaultProperty implements Property {

  private static final long serialVersionUID = 1L;

  private final DataStorage dataStorage;
  private final long artifactId;
  private final long version;
  private final String name;

  public DefaultProperty(final DataStorage dataStorage, final long artifactId, final long version, final String property) {
    super();
    if (dataStorage == null) {
      throw new IllegalArgumentException("dataStorage == null");
    }
    this.dataStorage = dataStorage;
    this.artifactId = artifactId;
    this.version = version;
    this.name = property;
  }

  @Override
  public long getVersionNumber() {
    return this.version;
  }

  @Override
  public long getId() {
    return this.artifactId;
  }

  @Override
  public Owner getOwner() {
    return new DefaultOwner(this.dataStorage, this.dataStorage.getPropertyOwner(this.version, this.artifactId, this.name), DataStorage.FIRST_VERSION);
  }

  @Override
  public Tool getTool() {
    long toolId = this.dataStorage.getPropertyTool(this.version, this.artifactId, this.name);
    return new DefaultTool(this.dataStorage, toolId, this.version);
  }

  @Override
  public boolean isAlive() {
    return this.dataStorage.isPropertyAlive(this.version, this.artifactId, this.name);
  }

  @Override
  public boolean isReference() {
    return this.dataStorage.isPropertyReference(this.version, this.artifactId, this.name);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Object getValue() {
    if (this.isReference()) {
      final Long propertyReference = this.dataStorage.getPropertyReference(this.version, this.artifactId, this.name);
      return ArtifactFactory.getArtifact(this.dataStorage, this.version, propertyReference);
    }
    return this.dataStorage.getPropertyValue(this.version, this.artifactId, this.name);
  }

  @Override
  public void setValue(final Workspace workspace, final Object value) throws ArtifactDoesNotExistException, ArtifactDeadException, TypeNotSupportedException {
    if (!CloudUtils.isSupportedType(value)) {
      throw new TypeNotSupportedException(value.getClass());
    }
    if (value instanceof Artifact) {
      this.dataStorage.setPropertyReference(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.artifactId, this.name, ((Artifact) value).getId());
    } else {
      this.dataStorage.setPropertyValue(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.artifactId, this.name, value);
    }
  }
  
  @Override
  public Object[] getRepresentation() {
    return this.dataStorage.getPropertyRepresentation(version, artifactId, name);
  }

  @Override
  public void delete(final Workspace workspace) {
    this.dataStorage.setPropertyAlive(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.artifactId, this.name, false);
  }

  @Override
  public void undelete(final Workspace workspace) {
    this.dataStorage.setPropertyAlive(workspace.getVersionNumber(), workspace.getOwner().getId(), workspace.getTool().getId(), this.artifactId, this.name, true);
  }

  @Override
  public Artifact getArtifact() {
    return ArtifactFactory.getArtifact(this.dataStorage, this.version, this.artifactId);
  }

  @Override
  public String toString() {
    return "Property[" + this.artifactId + ", " + this.version + ", " + this.name + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.version, this.artifactId, this.name);
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
    final DefaultProperty other = (DefaultProperty) obj;
    if (this.artifactId != other.artifactId) {
      return false;
    }
    if (this.name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!this.name.equals(other.name)) {
      return false;
    }
    if (this.version != other.version) {
      return false;
    }
    return true;
  }

}
