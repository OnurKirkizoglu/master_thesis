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
 * Owner.java created on 12.03.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud;

import java.util.Collection;

import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;

/**
 * A version controlled collection, in which either {@link Artifact}s or other arbitrary objects can be stored. In the
 * second case the collection can contain elements of different types at the same time.
 *
 * The collection can be viewed as ArrayList, where each index is a {@link Property}, referencing the element. If an
 * element is deleted from the collection at index m:<br>
 * for i = m ; i < size; i+++ then<br>
 * <\t>ca[i] = ca[i+1]<br>
 * and the last index is set to dead.
 *
 * If an element is added to the collection at index m:<br>
 * for i = m ; i < size; i+++ then<br>
 * <\t>ca[i+1] = ca[i]<br>
 * ca[m] = new elem
 *
 * If only {@link Artifact} are contained, the collection handles them automatically and correctly.
 *
 * @author alexander noehrer
 */
public interface CollectionArtifact extends Artifact {

  /**
   * Returns {@literal true} if only {@link Artifact}s are contained in the collection.
   *
   * @return {@literal true} if the collection only contains {@link Artifact}s
   */
  public boolean isContainingOnlyArtifacts();

  /**
   * Adds in the {@link Workspace} the specified element to the end of the collection.
   *
   * @param workspace
   *          the workspace in which element should be added to the collection.
   * @param element
   *          the specified element to be added at the end of the collection.
   * @throws ArtifactDeadException
   *           if the collection is dead
   */
  public void addElement(final Workspace workspace, final Object element) throws ArtifactDeadException, TypeNotSupportedException;
  
  public void addElements(final Workspace workspace, final Collection<?> elements) throws ArtifactDeadException, TypeNotSupportedException;

  /**
   * Removes in the {@link Workspace} the specified element from the collection.
   *
   * @param workspace
   *          the workspace in which element should be removed from the collection.
   * @param element
   *          the specified element to be removed from the collection.
   * @throws ArtifactDeadException
   *           if the collection is dead
   */
  public void removeElement(final Workspace workspace, final Object element) throws ArtifactDeadException, TypeNotSupportedException;

  /**
   * Inserts in the {@link Workspace} the element in the specified position.
   *
   * @param workspace
   *          the workspace in which the element should be added to the collection.
   * @param element
   *          the element to be added.
   * @param index
   *          the specified position where the element should be added.
   * @throws ArtifactDeadException
   *           if the collection is dead.
   * @throws IndexOutOfBoundsException
   *           if the specified index >= the size of the collection.
   */
  public void insertElementAt(final Workspace workspace, final Object element, final long index) throws ArtifactDeadException, IndexOutOfBoundsException, TypeNotSupportedException;

  /**
   * Removes in the {@link Workspace} the element in the specified position.
   *
   * @param workspace
   *          the workspace in which the element should be removed from the collection.
   * @param element
   *          the element to be removed.
   * @param index
   *          the specified position where the element should be removed.
   * @throws ArtifactDeadException
   *           if the collection is dead.
   * @throws IndexOutOfBoundsException
   *           if the specified index >= the size of the collection.
   */
  public void removeElementAt(final Workspace workspace, final long index) throws IndexOutOfBoundsException, ArtifactDeadException, IndexOutOfBoundsException;

  /**
   * Returns the element of the collection at the specified position.
   *
   * @param index
   *          the position of the collection which should be retrieved
   * @return the element of the collection at the specified position.
   * @throws IndexOutOfBoundsException
   *           if the collectionartifact is dead
   */
  public Object getElementAt(final long index) throws IndexOutOfBoundsException;

  /**
   * Returns the elements of the collection.
   *
   * @return the elements of the collection.
   */
  public Collection<?> getElements();

  /**
   * Returns {@literal true} if the specified is contained within the collection.
   *
   * @param element
   *          the element which presence should be verified.
   * @return {@literal true} if the element occurs in the collection.
   */
  public boolean existsElement(final Object element);

  /**
   * Returns the size of the collection.
   *
   * @return the size of the collection.
   */
  public Long size();

}
