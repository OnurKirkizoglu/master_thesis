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
 * ArtifactFactory.java created on Aug 22, 2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.DataStorage.Columns;

/**
 * @author alexander noehrer
 */
class ArtifactFactory {

  /*
   * 
   * DO NOT think about creating an object cache it is simply not worth the hassle (slow compared to object creation--the jvm is fast with this stuff, and you won't get it correct).
   */
  /**
   * 
   * @param dataStorage
   * @param version
   * @param id
   * @param <A> expected type of the Artifact returned
   * @return
   */
  @SuppressWarnings("unchecked")
  protected static <A extends Artifact> A getArtifact(final DataStorage dataStorage, final long version, final long id) {
    return (A) getArtifact(dataStorage, version, dataStorage.getArtifactRepresentation(version, id));
  }

  @SuppressWarnings("unchecked")
  protected static <A extends Artifact> A getArtifact(final DataStorage dataStorage, final long version, Object[] representation) {
    Long id = (Long) representation[Columns.ARTIFACT_ID.getIndex()];
    Long type = (Long) representation[Columns.ARTIFACT_TYPE.getIndex()];
    return (A) getArtifact(dataStorage, version, id, type);
  }

  protected static Artifact getArtifact(final DataStorage dataStorage, final long version, final long id, final Long type) {
    if (type == DataStorage.OWNER_TYPE_ID) {
      return new DefaultOwner(dataStorage, id, version);
    }
    if (type == DataStorage.TOOL_TYPE_ID) {
      return new DefaultTool(dataStorage, id, version);
    }
    if (type == DataStorage.COLLECTION_TYPE_ID) {
      return new DefaultCollectionArtifact(dataStorage, id, version);
    }
    if (type == DataStorage.PROJECT_TYPE_ID) {
      return new DefaultProject(dataStorage, id, version);
    }
    if (type == DataStorage.META_MODEL_TYPE_ID) {
      return new DefaultMetaModel(dataStorage, id, version);
    }
    if (type == DataStorage.MAP_TYPE_ID) {
      return new DefaultMapArtifact(dataStorage, id, version);
    }
    if (type == DataStorage.RESOURCE_TYPE_ID) {
      return new DefaultResource(dataStorage, id, version);
    }
    if (type == DataStorage.PACKAGE_TYPE_ID) {
      return new DefaultPackage(dataStorage, id, version);
    }
    return new DefaultArtifact(dataStorage, id, version);
  }

}
