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
 * DefaultVersion.java created on Sep 17, 2013
 * 
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.implementation;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.utils.StringUtils;

/**
 * @author alexander noehrer
 */
abstract class DefaultVersion implements Version {

  protected transient DataStorage dataStorage;

  protected final long versionNumber;

  /**
   * @param dataStorage
   * 
   */
  public DefaultVersion(final DataStorage dataStorage, final long versionNumber) {
    super();
    if (dataStorage == null) {
      throw new IllegalArgumentException("dataStorage == null");
    }
    this.dataStorage = dataStorage;
    this.versionNumber = versionNumber;
  }

  @Override
  public long getVersionNumber() {
    return this.versionNumber;
  }

  protected boolean isWorkspace() {
    return (this.versionNumber < 0);
  }

  @Override
  public Owner getOwner() {
    if (this.versionNumber == DataStorage.FIRST_VERSION) {
      return ArtifactFactory.getArtifact(this.dataStorage, DataStorage.FIRST_VERSION, DataStorage.ADMIN);
    }
    if (this.isWorkspace()) {
      return ArtifactFactory.getArtifact(this.dataStorage, this.versionNumber, this.dataStorage.getWorkspaceOwner(this.versionNumber));
    }
    return ArtifactFactory.getArtifact(this.dataStorage, this.versionNumber, this.dataStorage.getVersionOwner(this.versionNumber));
  }

  @Override
  public Tool getTool() {
    if (this.isWorkspace()) {
      return ArtifactFactory.getArtifact(this.dataStorage, DataStorage.FIRST_VERSION, this.dataStorage.getWorkspaceTool(this.versionNumber));
    }
    return null;
  }

  @Override
  public String getIdentifier() {
    if (this.isWorkspace()) {
      return this.dataStorage.getWorkspaceIdentifier(this.versionNumber);
    }
    return null;
  }

  @Override
  public String toString() {
    return StringUtils.printf("%s[version=%s]", this.getClass().getSimpleName(), this.versionNumber);
  }
}
