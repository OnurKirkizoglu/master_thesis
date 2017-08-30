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
import java.util.stream.Collectors;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;

/**
 * @author alexander noehrer
 */
class DefaultProject extends DefaultCollectionArtifact implements Project {
  private static final long serialVersionUID = 1L;

  /**
   * @param dataStorage
   * @param id
   * @param version
   */
  public DefaultProject(final DataStorage dataStorage, final long id, final long version) {
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
  public boolean containsArtifact(Artifact artifact) {
    return this.existsElement(artifact);
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

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.Project#getPackages()
   */
  @Override
  public Collection<Package> getPackages() {
    return this.getArtifacts().stream().filter(Package.class::isInstance).map(Package.class::cast).collect(Collectors.toList());
  }
}
