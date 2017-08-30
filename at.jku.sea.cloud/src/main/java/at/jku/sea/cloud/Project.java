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
 * Project.java created on 22.07.2013
 * 
 * (c) alexander noehrer
 */
package at.jku.sea.cloud;

import java.util.Collection;

import at.jku.sea.cloud.exceptions.ArtifactDeadException;

/**
 * A collection of {@link Artifact}s that belong in the same organizational unit. An {@link Artifact} can be contained
 * in multiple projects (Project n:n Artifact).
 * 
 * A project is not a {@link CollectionArtifact} due to the fact that a {@link CollectionArtifact} is a version
 * controlled collection of arbitrary objects (which coincidentally and not necessarily can be {@link Artifact}s).
 * 
 * @author alexander noehrer
 */
public interface Project extends Artifact {

  /**
   * Adds in a {@link Workspace} the specified {@link Artifact} to the project.
   * 
   * @param workspace
   *          the {@link Workspace} in which the {@link Artifact} should be added to the project.
   * @param artifact
   *          the specified {@link Artifact} to be added
   * @throws ArtifactDeadException
   *           if the project is dead
   */
  public void addArtifact(final Workspace workspace, final Artifact artifact) throws ArtifactDeadException;

  /**
   * Returns true if the {@link Artifact} is contained top-level --non-recursive-- in the Project.
   * @param artifact
   * @return
   */
  public boolean containsArtifact(final Artifact artifact);
  
  /**
   * Removes in a {@link Workspace} the specified {@link Artifact} from the project.
   * 
   * @param workspace
   *          the {@link Workspace} in which the {@link Artifact} should be removed from the project.
   * @param artifact
   *          the specified {@link Artifact} to be removed
   * @throws ArtifactDeadException
   *           if the project is dead
   */
  public void removeArtifact(final Workspace workspace, final Artifact artifact) throws ArtifactDeadException;

  /**
   * Returns all top-level--non-recursive-- {@link Package}s contained in the project
   * 
   * @return the top-level {@link Package}s in the project
   */
  public Collection<Package> getPackages();

  /**
   * Returns all {@link Artifact}s of the project--non-recursive.
   * 
   * @return all non-recursive {@link Artifact}s of the project.
   */
  public Collection<Artifact> getArtifacts();
}
