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
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.exceptions.ArtifactDeadException;

/**
 * A collection of {@link Artifact}s that represent type objects (in the sense of e.g., ECore::EClass, UML::Class). Each
 * metamodel is obtained for a specific {@link Version}.
 * 
 * A metamodel is not a {@link CollectionArtifact} due to the fact that a {@link CollectionArtifact} is a version
 * controlled collection of arbitrary objects (which coincidentally and not necessarily can be {@link Artifact}s).
 * 
 * 
 * @author alexander noehrer
 * @author mriedl
 */
public interface MetaModel extends Artifact {

  /**
   * Adds in a {@link Workspace} an {@link Artifact} to the metamodel.
   * 
   * @param workspace
   *          the {@link Workspace} in which the {@link Artifact} should be added to the metamodel.
   * @param artifact
   *          the specified {@link Artifact} to be added.
   * @throws ArtifactDeadException
   *           if the metamodel is dead.
   */
  public void addArtifact(final Workspace workspace, final Artifact artifact) throws ArtifactDeadException;

  public void addArtifacts(final Workspace workspace, final Collection<Artifact> artifacts) throws ArtifactDeadException;

  /**
   * Removes in a {@link Workspace} an {@link Artifact} from the metamodel.
   * 
   * @param workspace
   *          the {@link Workspace} in which the {@link Artifact} should be removed from the metamodel.
   * @param artifact
   *          the {@link Artifact} to be removed.
   * @throws ArtifactDeadException
   *           if the metamodel is dead.
   */
  public void removeArtifact(final Workspace workspace, final Artifact artifact) throws ArtifactDeadException;

  public MetaModel getMetaModel() throws ArtifactDeadException;

  /**
   * Returns the collection of {@link Artifact}s contained in the metamodel (for the {@link Version} it was obtained}.
   * 
   * @return the collection of {@link Artifact}s contained in the metamodel.
   */
  public Collection<Artifact> getArtifacts();

  public Map<Artifact, Set<Property>> getArtifactAndProperties();

  public Map<Artifact, Map<String, Object>> getArtifactPropertyMap();

  public Collection<Object[]> getArtifactRepresentations();

  public Map<Object[], Set<Object[]>> getArtifactPropertyMapRepresentations();
}
