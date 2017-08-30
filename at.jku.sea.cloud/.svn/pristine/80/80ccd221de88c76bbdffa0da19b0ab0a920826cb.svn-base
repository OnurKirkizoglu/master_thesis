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
 * Artifact.java created on 12.03.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud;

import java.util.Collection;
import java.util.Map;

import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;

/**
 * The root interface of the <i>artifact hierarchy</i>. An artifact represents an version controlled <i>entity</i>.
 * Entity in the sense that an artifact has no domain specific semantic associated.<br>
 * Semantics can be associated by :<br>
 * - arbitrary key-value pairs (i.e., {@link Property}), which are independently version controlled<br>
 * - a <i>type</i> artifact, which can be understood as the <i>instanceof</i> concept, (i.e., A1 (represents the concept
 * of [UML::Class], B1[type=A1] is then an instance of [UML::Class])
 *
 * Furthermore, artifacts can be grouped in {@link Package}s (1:n) and associated to {@link Project}s (n:n), if it
 * represents a type object it can be added to {@link MetaModel}s.
 *
 * For convenience, manipulation of {@link Property}s has been added to this interface.
 *
 * Artifacts can be <i>deleted</i> by setting the alive flag to {@literal false} .<br>
 * Consider the version history: A1:V2[alive=true], A1:V6[alive=true];<br>
 * It is not possible to change the publicly available history, therefore, to delete artifacts one has to mark an
 * artifact as being not available anymore (i.e., setting the alive flag to {@literal false}).
 *
 * Artifacts can be obtained in different versions.<br>
 * All get*-methods query the state of the artifact in the requested revision, therefore, do not require a
 * {@link Workspace}. However, adaptations are only allowed in private versions (i.e., a {@link Workspace}) (e.g.,
 * setting the type). Therefore, each state changing method (e.g., delete) requires a {@link Workspace}, even if it was
 * already obtained for a workspace.
 *
 * Adaptations performed on a <i>dead</i> artifact throw a {@link ArtifactDeadException}.
 *
 * @author alexander noehrer
 */
public interface Artifact extends Data {
  /**
   * Returns the type {@link Artifact} which is set in the obtained {@link Version}.
   *
   * @return the type {@link Artifact} in the obtained {@link Version}.
   */
  public Artifact getType();

  /**
   * Returns the {@link Package} to which this artifact belongs in the obtained {@link Version}.
   *
   * @return the {@link Package} in the obtained {@link Version}, can be {@literal null} if no {@link Package} is set.
   */
  public Package getPackage();

  /**
   * Sets the type of the artifact in the specified {@link Workspace}.
   *
   * @param workspace
   *          the workspace in which the artifact should exhibit the new type
   * @param type
   *          the new type
   * @throws ArtifactDeadException
   *           if the artifact for which the type is set is dead
   */
  public void setType(final Workspace workspace, final Artifact type) throws ArtifactDeadException;

  /**
   * Returns {@literal true} if the artifact is alive in the obtained {@link Version}.
   *
   * @return {@literal true} if this artifact is alive in the {@link Version} for which it was obtained.
   */
  public boolean isAlive();

  /**
   * Returns {@literal true} if the artifact owns a {@link Property} with the specified name
   *
   * @param name
   *          the name of {@link Property} whose presence is to be tested
   * @return {@literal true} if the artifact owns a {@link Property} of the specified name
   */
  public boolean hasProperty(final String name);

  /**
   * Returns {@literal true} if the {@link Property} with the specified name is alive
   *
   * @param name
   *          the name of {@link Property}
   * @return {@literal true} if the {@link Property} is alive
   * @throws PropertyDoesNotExistException
   *           if the artifact has no such {@link Property} associated
   */
  public boolean isPropertyAlive(final String name) throws PropertyDoesNotExistException;

  /**
   * Returns the value of the {@link Property} with the specified name
   *
   * @param name
   *          the name of {@link Property}
   * @return the value of the {@link Property} with the specified name
   * @throws PropertyDoesNotExistException
   *           if the artifact has no such {@link Property} associated
   */
  public Object getPropertyValue(final String name) throws PropertyDoesNotExistException;
  
  /**
   * Returns the value of the {@link Property} with the specified name
   * casted to the specified excepted type.
   *
   * @param <T>
   *          the expected type of the return value
   * @param name
   *          the name of the {@link Property}
   * @return the value of the {@link Property} with the specified name
   * @throws PropertyDoesNotExistException
   *         if the artifact has no such {@link Property} associated
   * @throws ClassCastException
   *         if the {@link Property} value is not of type {@code <T>} or a sub type
   */
  @SuppressWarnings("unchecked")
  public default <T> T getTypedPropertyValue(final String name) throws PropertyDoesNotExistException, ClassCastException {
    return (T) getPropertyValue(name);
  }
  
  /**
   * Returns the value of the {@link Property} with the specified name or {@literal null} if non existent
   * 
   * Beware of this function: 
   * One cannot be sure if the value of the property is actually null or it the property does not exist. 
   * 
   * Please use with care, use only if you really do not care whether this property exists or not.
   *
   * @param name the name of {@link Property}
   * @return the value of the {@link Property} with the specified name or {@literal null} if non existent
   */
  public Object getPropertyValueOrNull(final String name);
 
  public Object[] getPropertyRepresentation(final String name) throws PropertyDoesNotExistException;

  /**
   * Returns the collection of {@link Property} <b>names</b> associated with the artifact, for which the alive flag is
   * {@literal true}.
   *
   * @return the collection of alive {@link Property} <b>names</b> associated with the artifact.
   */
  public Collection<String> getAlivePropertyNames();

  /**
   * Returns the collection of {@link Property} <b>names</b> associated with the artifact, regardless of alive or not.
   *
   * @return the collection of {@link Property} <b>names</b> associated with the artifact.
   */
  public Collection<String> getAllPropertyNames();

  /**
   * Deletes -- i.e., sets the alive flag to {@literal false} -- the artifact in the specified {@link Workspace}.
   *
   * @param workspace
   *          the workspace in which this artifact should be deleted.
   */
  public void delete(final Workspace workspace);

  /**
   * Undeletes -- i.e., sets the alive flag to {@literal true} -- the artifact in the specified {@link Workspace}.
   *
   * @param workspace
   *          the workspace in which this artifact should be undeleted.
   */
  public void undelete(final Workspace workspace);

  /**
   * Sets the value of the property in a {@link Workspace}---existing {@link Property}s are overwritten with the new
   * value.
   *
   * @param workspace
   *          the workspace in which the value should be stored.
   * @param name
   *          the desired name of the {@link Property}---existing values are overwritten.
   * @param value
   *          the value the property should have. {@link Artifact}s can as well be passed, these are correctly
   *          translated to references.
   * @throws PropertyDeadException
   *           if the {@link Property} with the specified name is dead.
   * @throws ArtifactDoesNotExistException
   *           if the artifact for which the {@link Property} is set does not exist
   * @throws ArtifactDeadException
   *           if the artifact for which the {@link Property} is set is dead
   */
  public void setPropertyValue(final Workspace workspace, final String name, final Object value) throws PropertyDeadException, ArtifactDoesNotExistException, ArtifactDeadException,
      TypeNotSupportedException;

  /**
   * Sets all properties given in the {@link Map}--- existing {@link Property} s are overwritten with the new values.
   *
   * @param workspace
   *          the workspace in which the properties should be stored.
   * @param properties
   *          the {@link Map} with the properties to store.
   * @throws PropertyDeadException
   *           if the {@link Property} with the specified name is dead.
   * @throws ArtifactDoesNotExistException
   *           if the artifact for which the {@link Property} is set does not exist
   * @throws ArtifactDeadException
   *           if the artifact for which the {@link Property} is set is dead
   */
  public void setPropertyValues(final Workspace workspace, final Map<String, Object> properties) throws ArtifactDoesNotExistException, ArtifactDeadException;

  /**
   * Deletes---i.e., sets the alive flag to {@literal false} -- the {@link Property} in the specified {@link Workspace}.
   *
   * @param workspace
   *          the workspace in which this {@link Property} should be deleted.
   * @param name
   *          the specified name of the {@link Property}.
   * @throws PropertyDoesNotExistException
   *           if the {@link Property} does not exist
   */
  public void deleteProperty(final Workspace workspace, final String name) throws PropertyDoesNotExistException;

  /**
   * Deletes---i.e., sets the alive flag to {@literal true} -- the {@link Property} in the specified {@link Workspace}.
   *
   * @param workspace
   *          the workspace in which this {@link Property} should be undeleted.
   * @param name
   *          the specified name of the {@link Property}.
   * @throws PropertyDoesNotExistException
   *           if the {@link Property} does not exist
   */
  public void undeleteProperty(final Workspace workspace, final String name) throws PropertyDoesNotExistException;

  /**
   * Sets the {@link Package} of the artifact in a {@link Workspace}.
   *
   * @param workspace
   *          the workspace in which the artifact should exhibit the new {@link Package}.
   * @param pkg
   *          the package to be set
   * @throws ArtifactDeadException
   *           if the artifact for which the {@link Package} should be set is dead
   */
  public void setPackage(final Workspace workspace, final Package pkg) throws ArtifactDeadException;

  /**
   * Creates a new {@link Property} with the specified name in the {@link Workspace}. If the {@link Property} does not
   * yet exist the value will be {@link null}. If the {@link Property} does exist, simply the existing {@link Property}
   * is returned.
   *
   * @param workspace
   *          the workspace in which the {@link Property} should be created
   * @param name
   *          the specified name of the {@link Property}
   * @return the {@link Property} with the specified name
   * @throws ArtifactDeadException
   *           if the artifact for which the {@link Property} should be created is dead.
   */
  public Property createProperty(final Workspace workspace, final String name) throws ArtifactDeadException;

  /**
   * Returns the {@link Property} with the specified name.
   *
   * @param property
   *          the name of the property.
   * @return the {@link Property} with the specified name
   * @throws PropertyDoesNotExistException
   *           if the artifact does not have a {@link Property} associated with the specified name
   */
  public Property getProperty(final String property) throws PropertyDoesNotExistException;

  /**
   * Returns the collection of {@link Property}s associated with the artifact, for which the alive flag is
   * {@literal true}.
   *
   * @return the collection of alive {@link Property}s associated with the artifact.
   */
  public Collection<Property> getAliveProperties();

  /**
   * Returns the collection of {@link Property}s associated with the artifact, regardless of alive or not.
   *
   * @return the collection of {@link Property}s associated with the artifact.
   */
  public Collection<Property> getAllProperties();

  /**
   * Returns a key-value map of the currently alive properties associated with the artifact.
   * 
   * @return a key-value map of the currently alive properties associated with the artifact.
   */
  public Map<String, Object> getAlivePropertiesMap();

  /**
   * Returns a key-value map of the currently dead properties associated with the artifact.
   * 
   * @return a key-value map of the currently dead properties associated with the artifact.
   */
  public Map<String, Object> getDeadPropertiesMap();

  /**
   * Adds the artifact to the specified {@link Project}.
   *
   * @param workspace
   *          the workspace in which the artifact should be added to the {@link Project}
   * @param project
   *          the specified {@link Project}.
   * @throws ArtifactDeadException
   *           if the {@link Project} is dead
   */
  public void addToProject(final Workspace workspace, final Project project) throws ArtifactDeadException;

  /**
   * Removes the artifact to the specified {@link Project}.
   *
   * @param workspace
   *          the workspace in which the artifact should be added to the {@link Project}
   * @param project
   *          the specified {@link Project}.
   * @throws ArtifactDeadException
   *           if the {@link Project} is dead
   */
  public void removeFromProject(final Workspace workspace, final Project project) throws ArtifactDeadException;

  public Object[] getRepresentation();

}
