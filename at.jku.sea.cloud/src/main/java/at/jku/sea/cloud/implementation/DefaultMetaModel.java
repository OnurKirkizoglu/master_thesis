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
 * DefaultMetaModel.java created on 02.10.2013
 * 
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.mmm.MMMTypeProperties;

/**
 * @author alexander noehrer
 */
class DefaultMetaModel extends DefaultCollectionArtifact implements MetaModel {
  private static final long serialVersionUID = 1L;

  /**
   * @param dataStorage
   * @param id
   * @param version
   */
  public DefaultMetaModel(final DataStorage dataStorage, final long id, final long version) {
    super(dataStorage, id, version);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Project#addArtifact(at.jku.sea.cloud.Workspace, at.jku.sea.cloud.Artifact)
   */
  @Override
  public void addArtifact(final Workspace workspace, final Artifact artifact) throws ArtifactDeadException {
    this.addElement(workspace, artifact);
  }

  @Override
  public void addArtifacts(Workspace workspace, Collection<Artifact> artifacts) throws ArtifactDeadException {
    Collection<Object> artifactIds = new ArrayList<>();
    for (Artifact element : artifacts) {
      artifactIds.add(element.getId());
    }
    this.dataStorage.addElementsToCollection(workspace.getVersionNumber(), this.id, workspace.getOwner().getId(), workspace.getTool().getId(), artifactIds);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Project#removeArtifact(at.jku.sea.cloud.Workspace, at.jku.sea.cloud.Artifact)
   */
  @Override
  public void removeArtifact(final Workspace workspace, final Artifact artifact) throws ArtifactDeadException {
    this.removeElement(workspace, artifact);
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Project#getArtifacts()
   */
  @SuppressWarnings("unchecked")
  @Override
  public Collection<Artifact> getArtifacts() {
    return (Collection<Artifact>) this.getElements();
  }

  public MetaModel getMetaModel() throws ArtifactDeadException {
    MetaModel mm = (MetaModel) this.getPropertyValueOrNull(MMMTypeProperties.METAMODEL);
    if (mm != null) {
      return mm;
    }

    if (this.size() > 0) {
      Long aid = (Long) this.dataStorage.getElementAtFromCollection(this.version, this.id, 0);
      Long type = this.dataStorage.getArtifactType(this.version, aid);
      Set<Object[]> artifacts = this.dataStorage.getArtifacts(this.id, true, null, null, DataStorage.META_MODEL_TYPE_ID, null);
      for (Object[] artifactRep : artifacts) {
        Long mmId = (Long) artifactRep[DataStorage.Columns.ARTIFACT_ID.getIndex()];
        if (this.dataStorage.existsElementInCollection(this.id, mmId, type)) {
          return (MetaModel) ArtifactFactory.getArtifact(dataStorage, this.version, artifactRep);
        }
      }
    }
    return null;
  }

  @Override
  public Map<Artifact, Set<Property>> getArtifactAndProperties() {
    Collection<Artifact> artifacts = (Collection<Artifact>) this.getElements();
    return GetArtifactsWithFilterUtils.createArtifactAndPropertyMap(dataStorage, this.version, artifacts);
  }

  @Override
  public Collection<Object[]> getArtifactRepresentations() {
    return this.dataStorage.getArtifactsFromCollection(version, this.id);
  }

  @Override
  public Map<Object[], Set<Object[]>> getArtifactPropertyMapRepresentations() {
    Collection<Object[]> artifacts = this.dataStorage.getArtifactsFromCollection(version, this.id);
    return GetArtifactsWithFilterUtils.createArtifactAndPropertyObjectMap(dataStorage, version, artifacts);
  }

  @Override
  public Map<Artifact, Map<String, Object>> getArtifactPropertyMap() {
    Collection<Artifact> artifacts = (Collection<Artifact>) this.getElements();
    return GetArtifactsWithFilterUtils.createArtifactAndStringObjectMap(dataStorage, this.version, artifacts);
  }
}
