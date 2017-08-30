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
 * Property.java created on 22.07.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud;

import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;

/**
 * Version controlled key-value pair associated with an {@link Artifact}. Each property is obtained for a specific
 * (public or private) {@link Version}.
 *
 * Properties, like {@link Artifact}s, can be deleted (i.e., setting the 'alive' flag to {@literal false}). When
 * deleted, the value can still be obtained. However, setting the value requires the property to be alive.
 *
 * To set either the value or the alive flag a {@link Workspace} is necessary, as only in private versions changes are
 * allowed.
 *
 * @author alexander noehrer
 * @author mriedl
 */
public interface Property extends Data {
  /**
   * Returns {@literal true} if the property is alive in the obtained {@link Version}.
   *
   * @return {@literal true} if this property is alive in the {@link Version} for which it was obtained.
   */
  public boolean isAlive();

  /**
   * Returns {@literal true} if the property's value is an {@link Artifact}.
   *
   * @return {@literal true} if the property's value is an {@link Artifact}.
   */
  public boolean isReference();

  /**
   * Returns the name of the property.
   *
   * @return the name of the property.
   */
  public String getName();

  /**
   * Returns the value of the property in the obtained {@link Version}.
   *
   * @return the value of the property.
   */
  public Object getValue();
  
  /**
   * Returns the value of the property in the obtained {@link Version} with the type T.
   * @param <T> expected type of the return value.
   * @return the value of the property.
   * @throws ClassCastException if the value is not of the expected type.
   */
  @SuppressWarnings("unchecked")
  public default <T> T getTypedValue() throws ClassCastException {
    return (T) getValue();
  }

  /**
   * Sets the value of the property in a {@link Workspace}.
   *
   * @param workspace
   *          the workspace in which the value should be stored.
   * @param value
   *          the value the property should have. {@link Artifact}s can as well be passed, these are correctly
   *          translated to references.
   * @throws PropertyDeadException
   *           if the property is dead -- i.e., alive flag equals {@literal false}.
   * @throws ArtifactDeadException
   *           if the {@link Artifact} to which this property belongs is dead.
   */
  public void setValue(final Workspace workspace, final Object value) throws PropertyDeadException, ArtifactDeadException, TypeNotSupportedException;

  /**
   * Deletes -- i.e., sets the alive flag to {@literal false} -- the property in the specified {@link Workspace}.
   *
   * @param workspace
   *          the workspace in which this property should be deleted.
   */
  public void delete(final Workspace workspace);

  /**
   * Undeletes -- i.e., sets the alive flag to {@literal true} -- the property in the specified {@link Workspace}.
   *
   * @param workspace
   *          the workspace in which this property should be undeleted.
   */
  public void undelete(final Workspace workspace);

  /**
   * Returns the {@link Artifact} to which this Property belongs.
   *
   * @return the {@link Artifact} to which this Property belongs.
   */
  public Artifact getArtifact();
  
  public Object[] getRepresentation();
}
