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
 * DataStorage.java created on 13.03.2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.ArtifactIsNotACollectionException;
import at.jku.sea.cloud.exceptions.ArtifactNotDeleteableException;
import at.jku.sea.cloud.exceptions.ArtifactNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.CredentialsException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyNotPushOrPullableException;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionConflictException;
import at.jku.sea.cloud.exceptions.WorkspaceEmptyException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.exceptions.WorkspaceNotEmptyException;
import at.jku.sea.cloud.listeners.DataStorageListener;
import at.jku.sea.cloud.listeners.EventSource;

/**
 * @author alexander noehrer
 * @author mriedl
 */

public interface DataStorage extends EventSource<DataStorageListener> {
  public static final long RESERVERD_IDS = 120L;
  public static final Long VOID_VERSION = 0l;
  public static final long FIRST_VERSION = 1L;

  // artifact id constants
  public static final long PROJECT_TYPE_ID = 0L;
  public static final long PACKAGE_TYPE_ID = 1L;
  public static final long OWNER_TYPE_ID = 2L;
  public static final long TOOL_TYPE_ID = 3L;
  public static final long ROOT_TYPE_ID = 4L;
  public static final long COLLECTION_TYPE_ID = 5L;
  public static final long META_MODEL_TYPE_ID = 6L;
  public static final long MAP_TYPE_ID = 7L;
  public static final long RESOURCE_TYPE_ID = 8L;
  public static final long CONTAINER_TYPE_ID = 9L;

  // package id constants
  public static final long AT_PACKAGE_ID = 10L;
  public static final long JKU_PACKAGE_ID = 11L;
  public static final long SEA_PACKAGE_ID = 12L;
  public static final long DESIGNSPACE_PACKAGE_ID = 13L;
  public static final long MMM_PACKAGE_ID = 14L;
  public static final long NO_CONTAINER_ID = 15L;
  public static final long MAPPING_PACKAGE_ID = 16L;

  // owner id constants
  public static final long ADMIN = 20L;
  public static final long DEMO = 21L;

  // tool id constants
  public static final long ROOT_TOOL_ID = 30L;
  public static final long RSA_TOOL_ID = 31L;
  public static final long PROE_TOOL_ID = 32L;
  public static final long ECLIPSE_TOOL_ID = 33L;
  public static final long EXCEL_TOOL_ID = 34L;
  public static final long MA_TOOL_ID = 35L;
  public static final long JUNIT_TOOL_ID = 36L;
  public static final long EDITOR_TOOL_ID = 37L;
  public static final long EPLAN_TOOL_ID = 38L;

  public static final long DESIGNSPACE_META_MODEL = 40L;
  public static final long LINK_META_MODEL_ID = 41L;

  // Model/Analyzer specific meta meta model
  public static final long MMM_META_MODEL = 49L;
  public static final long TYPE = 50L;
  public static final long TYPE_PARAMETER = 51L;
  public static final long DATA_TYPE = 52L;
  public static final long COMPLEX_TYPE = 53L;
  public static final long ENUMM = 54L;
  public static final long ENUMM_LITERAL = 55L;

  public static final long TYPED_ELEMENT = 56L;
  public static final long FEATURE = 57L;
  public static final long PARAMETER = 58L;
  public static final long OPERATION = 59L;

  public static final long CONSTRAINT = 61L;
  public static final long QUERY = 62L;
  public static final long INSTANCE = 63L;

  // Model/Analyzer specific meta meta model
  public static final long MMM_MAPPING = 70L;

  public static final long PROE_DIMENSION = 75L;
  public static final long PROE_PARAMETER = 76L;
  public static final long PROE_PACKAGE = 77L;
  public static final long PROE_PACKAGE_DIMENSION = 78L;
  public static final long PROE_PACKAGE_PARAMETER = 79L;
  public static final long PROE_PACKAGE_DIMENSION_FEATURE = 80L;
  public static final long PROE_PACKAGE_PARAMETER_FEATURE = 81L;

  public static final long TRACE_LINK = 90L;
  public static final long TRACE_LINK_PACKAGE_ID = 91L;
  public static final long TRACE_LINK_TYPE_PACKAGE_ID = 92L;
  public static final long TRACE_LINK_TYPE_FEATURE_PACKAGE_ID = 93L;

  public static final long MODELANALYZER_PACKAGE_ID = 98L;

  // Java object MetaModel
  public static final long JAVA_METAMODEL = 100L;
  public static final long JAVA_OBJECT_TYPE = 101L;

  public static final long TEST_PROJECT_ID = 105;

  // EPLAN
  public static final long EPLAN_PCKG = 110L;
  public static final long EPLAN_FUNCTION_TYPE = 111L;
  public static final long EPLAN_CONNECTION_TYPE = 112L;
  public static final long EPLAN_FUNCTION_TYPE_PCKG = 113L;
  public static final long EPLAN_CONNECTION_TYPE_PCKG = 114L;
  public static final long EPLAN_FUNCTION_TYPE_FEATURE_PCKG = 115L;
  public static final long EPLAN_CONNECTION_TYPE_FEATURE_PCKG = 116L;
  public static final long EPLAN_METAMODEL = 117L;
  

  // owner package
  public static final long OWNER_PACKAGE_ID = 118L;
  // tool package
  public static final long TOOL_PACKAGE_ID = 119L;

  // property constants
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_TOOL_VERSION = "toolVersion";
  public static final String PROPERTY_FULL_QUALIFIED_NAME = "fullName";
  public static final String PROPERTY_COLLECTION_NEXT_IDX = "collNextIdx";
  public static final String PROPERTY_COLLECTION_REFERENCES = "collection_references";
  public static final String PROPERTY_MAP_COLLECTION = "mapcollection";
  public static final String PROPERTY_OWNER_USER = "user";
  public static final String PROPERTY_LOGIN = "login";

  // collection
  public static final String COLLECTION_ELEMENT_PATTERN = "\\d{50}";

  public enum Columns {
    ARTIFACT_ID(0, "id"), WORKSPACE_VERSION(0, "private_version"), VERSION(1, "version"), WORKSPACE_BASE_VERSION(1, "base_version"), OWNER(2, "owner"), TOOL(3, "tool"), ARTIFACT_TYPE(4, "type"), PROPERTY_NAME(
        4, "name"), WORKSPACE_IDENTIFIER(4, "identifier"), WORKSPACE_PARENT(5, "parent"), WORKSPACE_PUSH(6, "push"), WORKSPACE_PULL(7, "pull"), ARTIFACT_CONTAINER(5, "container"), PROPERTY_VALUE(5,
        "value"), ALIVE(6, "alive"), PROPERTY_REFERENCE(7, "reference"),
    USER_ID(0,"id"), USER_NAME(1, "name"), USER_LOGIN(2, "login"), USER_PASSWORD(3, "password"), USER_OWNER(4, "owner");

    private final int index;
    private final int sqlIndex;
    private final String name;

    private Columns(final int index, final String name) {
      this.index = index;
      this.sqlIndex = index + 1;
      this.name = name;
    }

    public int getIndex() {
      return this.index;
    }

    public String getName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.name;
    }

    public int getSqlIndex() {
      return sqlIndex;
    }
  }

  // traceability artifacts

  /**
   * creates an Owner and implicitly a new version, containing only the owner
   *
   * @param owner
   *          id of the creating owner
   * @param tool
   *          id of the creating tool
   * @return
   */
  public long createOwner(final long owner, final long tool);

  /**
   * creates a Tool and implicitly a new version, containing only the tool
   *
   * @param owner
   *          id of the creating owner
   * @param tool
   *          id of the creating tool
   * @return
   */
  public long createTool(final long owner, final long tool);

  /**
   * Creates a new artifact in the specified private version.
   *
   * @param version
   *          the private version in which the artifact should be created.
   * @param owner
   *          the owner creating the artifact.
   * @param tool
   *          the tool creating the artifact.
   * @param type
   *          the type, the created artifact should exhibit.
   * @param container
   *          the container in which the artifact is to be placed.
   * @return the id of the new artifact
   */
  public long createArtifact(final long version, final long owner, final long tool, final Long type, Long container);

  public long createArtifact(final long version, final long owner, final long tool, final Long type, Long container, Long metamodel, Long project, Map<String, Object> valueMap,
      Map<String, Long> referenceMap);

  /**
   * Checks whether an artifact exists for the given version and id,
   *
   * @param version
   *          the either public or private version the artifact should exist in.<br>
   *          if (version > 0) then check for the existence of the artifact between the FIRST_VERSION and version<br>
   *          if (version < 0) then check for the existence of the artifact in the private workspace and in the base version of the private version
   * @param id
   *          the artifact id.
   * @return {@literal true} if the artifact exists in the specified version.
   */
  public boolean existsArtifact(final long version, final long id);

  /**
   * Returns {@literal true} if the artifact is alive in the specified version.
   *
   * @param version
   *          the either public or private version.<br>
   *          if (version > 0) the alive flag between the FIRST_VERSION and version<br>
   *          if (version < 0) the alive flag either in the private workspace (if overwritten) or in the base version
   * @param id
   *          the artifact id.
   * @return {@literal true} if the artifact is alive in the specified version.
   * @throws ArtifactDoesNotExistException
   *           if the artifact id does not exist in the version history.
   */
  public boolean isArtifactAlive(final long version, final long id) throws ArtifactDoesNotExistException;

  /**
   * Set the artifact alive-flag to the specified value.
   *
   * @param version
   *          the private version in which the value has to be set.
   * @param owner
   *          the owner which performs this change.
   * @param tool
   *          the tool which performs this change.
   * @param id
   *          the artifact for which the value should be set.
   * @param alive
   *          the new alive value.
   * @throws ArtifactDoesNotExistException
   *           if the artifact id does not exist in the version history.
   * @throws ArtifactDeadException
   *           if the artifact's alive flag equals {@literal false}, and therefore cannot be changed.
   */
  public void setArtifactAlive(final long version, final long owner, final long tool, final long id, final boolean alive) throws ArtifactDoesNotExistException;

  public void setArtifactType(final long version, final long owner, final long tool, final long id, final Long type) throws ArtifactDoesNotExistException, ArtifactDeadException;

  public Long getArtifactType(final long version, final long id) throws ArtifactDoesNotExistException;

  public void setArtifactContainer(final long version, final long owner, final long tool, final long id, final Long containerId) throws ArtifactDoesNotExistException, ArtifactDeadException;

  public Long getArtifactContainer(final long version, final long id) throws ArtifactDoesNotExistException;

  public Long getArtifactOwner(final long version, final long id) throws ArtifactDoesNotExistException;

  public Long getArtifactTool(final long version, final long id) throws ArtifactDoesNotExistException;

  /**
   * returns the representation of the requested artifact from the storage at once as an Object[]<br>
   * exists so that in certain situations performance can be increased, e.g. the owner, tool and type information are needed at the same time, so instead of making three API calls one is sufficient
   *
   * @param version
   *          the version of the artifact requested
   * @param id
   *          the id of the artifact requested
   * @return an Object[] containing the property representation in the following order<br>
   *         [0]: Long id<br>
   *         [1]: Long version<br>
   *         [2]: Long owner<br>
   *         [3]: Long tool<br>
   *         [4]: Long type<br>
   *         [5]: Long container<br>
   *         [6]: Boolean alive
   * @throws ArtifactDoesNotExistException
   */
  public Object[] getArtifactRepresentation(final long version, final long id) throws ArtifactDoesNotExistException;

  // create and manipulate properties
  public void setPropertyValue(final long version, final long owner, final long tool, final long artifactId, final String propertyName, final Object value) throws ArtifactDoesNotExistException,
      ArtifactDeadException;

  public void setPropertyReference(final long version, final long owner, final long tool, final long artifactId, final String propertyName, final long referenceId)
      throws ArtifactDoesNotExistException, ArtifactDeadException;

  public void setPropertyValues(final long version, final long owner, final long tool, final long artifactId, final Map<String, Object> valueMap, final Map<String, Long> referenceMap)
      throws ArtifactDoesNotExistException, PropertyDeadException;

  public boolean hasProperty(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException;

  public boolean isPropertyAlive(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public void setPropertyAlive(final long version, final long owner, final long tool, final long artifactId, final String propertyName, final boolean alive) throws ArtifactDoesNotExistException;

  public Long getPropertyOwner(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public Long getPropertyTool(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public boolean isPropertyReference(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public Collection<Object[]> getAllPropertyRepresentations(final long version, final Long minVersion, final long artifactId, boolean alive) throws ArtifactDoesNotExistException,
      PropertyDoesNotExistException;

  public Object getPropertyValue(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public Long getPropertyReference(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  /**
   * returns the representation of the requested property from the storage at once as an Object[]<br>
   * exists so that in certain situations performance can be increased, e.g. the owner, tool and information are needed at the same time, so instead of making three API calls one is sufficient
   *
   * @param version
   *          the version of the property requested
   * @param artifactId
   *          the id of the artifact the property belongs to
   * @param propertyName
   *          the name of the property
   * @return an Object[] containing the property representation in the following order<br>
   *         [0]: Long artifactId<br>
   *         [1]: Long version<br>
   *         [2]: Long owner<br>
   *         [3]: Long tool<br>
   *         [4]: String propertyName<br>
   *         [5]: Object value<br>
   *         [6]: Boolean alive<br>
   *         [7]: Boolean reference<br>
   * @throws ArtifactDoesNotExistException
   * @throws PropertyDoesNotExistException
   */
  public Object[] getPropertyRepresentation(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public Object[] getPropertyRepresentationOrNull(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public long createCollection(final long version, final long owner, final long tool, final Long container, final boolean containsOnlyReferences);

  public long createCollection(final long version, final long owner, final long tool, final Long container, final boolean containsOnlyReferences, Collection<?> elements, Map<String, Object> valueMap,
      Map<String, Long> referenceMap);

  public long createResource(final long version, final long owner, final long tool, final Long container, final Long projectId, Map<String, Object> valueMap, Map<String, Long> referenceMap,
      Collection<Long> artifacts);

  public long createMap(final long version, final long owner, final long tool, Long container);

  public long createProject(final long version, final long owner, final long tool, final String name);
  
  public long createPackage(final long version, final long owner, final long tool, final String name, final Long parent);

  public long createMetaModel(final long version, final long owner, final long tool, final Long container);

  public boolean isCollection(final long version, final long id) throws ArtifactDoesNotExistException;

  public boolean isReferenceCollection(final long version, final long id) throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException, PropertyDoesNotExistException;

  public void addElementToCollection(final long version, final long id, final long owner, long tool, final Object element) throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException,
      ArtifactDeadException;

  public void addElementsToCollection(final long version, final long id, final long owner, long tool, final Collection<?> elements) throws ArtifactDoesNotExistException,
      ArtifactIsNotACollectionException, ArtifactDeadException;

  public void insertElementInCollectionAt(final long version, final long id, final long owner, long tool, final Object element, final long index) throws ArtifactDoesNotExistException,
      ArtifactIsNotACollectionException, ArtifactDeadException;

  public void removeElementFromCollection(final long version, final long id, final long owner, long tool, final Object element) throws ArtifactDoesNotExistException,
      ArtifactIsNotACollectionException, ArtifactDeadException;

  public void removeElementAtFromCollection(final long version, final long id, long owner, long tool, long index) throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException,
      IndexOutOfBoundsException, ArtifactDeadException;

  public Object getElementAtFromCollection(final long version, final long id, long index) throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException, IndexOutOfBoundsException;

  public Collection<?> getElementsFromCollection(final long version, final long id) throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException;

  public Collection<Object[]> getArtifactsFromCollection(final long version, final long id) throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException;

  public boolean existsElementInCollection(final long version, final long id, final Object element) throws ArtifactDoesNotExistException, ArtifactIsNotACollectionException;

  public void clearMap(long version, long mapId, long owner, long tool) throws ArtifactDeadException, ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public void putInMap(long version, long mapId, long owner, long tool, String keyName, Object key, boolean isKeyReference, String valueName, Object value, boolean isValueReference);

  public void removeFromMap(long version, long mapId, long owner, long tool, String keyName, Object key, boolean isKeyReference, String valueName);

  // get artifact ids methods
  public Set<Object[]> getAliveArtifacts(final long version);

  public Set<Object[]> getAliveArtifacts(final long version, final long minVersion);

  public Set<Object[]> getAllArtifacts(final long version);

  public Set<Object[]> getAllArtifacts(final long version, final long minVersion);

  /**
   * get artifact ids filtered by the following criteria
   *
   * @param version
   *          the version of the artifacts requested
   * @param alive
   *          a boolean indicating if the requested artifacts should be alive
   * @param toolId
   *          the id of the tool artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any tool set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no tool set (tool == NULL)
   * @param ownerId
   *          the id of the owner artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any owner set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no owner set (owner == NULL)
   * @param typeId
   *          the id of the type artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any type set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no type set (type == NULL)
   * @param containerId
   *          the id of the container artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any container set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no container set (container == NULL)<br>
   *          providing a <b>value == NO_CONTAINER_ID</b> returns artifacts created/modified where there is no container set (container == NULL and container == NO_CONTAINER_ID) and that are themselves not containers (type <> CONTAINER_TYPE_ID)
   * @return the ids of the requested artifacts
   */
  public Set<Object[]> getArtifacts(final long version, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId);

  /**
   * get artifact ids filtered by the following criteria
   *
   * @param version
   *          the version of the artifacts requested
   * @param minVersion
   *          the minimum version of the artifacts requested
   * @param alive
   *          a boolean indicating if the requested artifacts should be alive
   * @param toolId
   *          the id of the tool artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any tool set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no tool set (tool == NULL)
   * @param ownerId
   *          the id of the owner artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any owner set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no owner set (owner == NULL)
   * @param typeId
   *          the id of the type artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any type set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no type set (type == NULL)
   * @param containerId
   *          the id of the container artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any container set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no container set (container == NULL)<br>
   *          providing a <b>value == NO_CONTAINER_ID</b> returns artifacts created/modified where there is no container set (container == NULL and container == NO_CONTAINER_ID) and that are themselves not containers (type <> CONTAINER_TYPE_ID)
   * @return the ids of the requested artifacts
   */
  public Set<Object[]> getArtifacts(final long version, final long minVersion, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId);

  public Set<Object[]> getArtifacts(final long version, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId, final String propertyName,
      final Object propertyValue, final Boolean propertyReference);

  /**
   * get artifact ids filtered by the following criteria
   *
   * @param version
   *          the version of the artifacts requested
   * @param minVersion
   *          the minimum version of the artifacts requested
   * @param alive
   *          a boolean indicating if the requested artifacts should be alive
   * @param toolId
   *          the id of the tool artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any tool set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no tool set (tool == NULL)
   * @param ownerId
   *          the id of the owner artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any owner set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no owner set (owner == NULL)
   * @param typeId
   *          the id of the type artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any type set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no type set (type == NULL)
   * @param containerId
   *          the id of the container artifact the artifacts should be created/modified with in the requested version<br>
   *          providing a <b>null</b> value returns artifacts created/modified with any container set<br>
   *          providing a <b>value &lt; 0</b> returns artifacts created/modified where there is no container set (container == NULL)<br>
   *          providing a <b>value == NO_containerER_ID</b> returns artifacts created/modified where there is no container set (container == NULL and container == NO_CONTAINER_ID) and that are themselves not containers (type <> CONTAINER_TYPE_ID)
   * @param propertyName
   *          the name of the property the artifacts should have<br>
   *          providing a <b>null</b> value returns artifacts with any properties<br>
   *          will be combined with <b>propertyValue</b> and <b>propertyReference</b> if values other than <b>null</b> are provided
   * @param propertyValue
   *          the value of any property the artifacts should have<br>
   *          providing a <b>null</b> value returns artifacts with any values<br>
   *          will be combined with <b>propertyName</b> and <b>propertyReference</b> if values other than <b>null</b> are provided
   * @param propertyReference
   *          a boolean indicating if any property is a reference the artifacts should have<br>
   *          providing a <b>null</b> value returns artifacts with and without references<br>
   *          will be combined with <b>propertyName</b> and <b>propertyValue</b> if values other than <b>null</b> are provided
   * @return the ids of the requested artifacts
   */
  public Set<Object[]> getArtifacts(final long version, final long minVersion, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId,
      final String propertyName, final Object propertyValue, final Boolean propertyReference);

  public Set<Object[]> getArtifactsByType(final long version, final long id);

  public Set<Object[]> getArtifactsByType(final long version, final long minVersion, final long id);

  public Set<Object[]> getArtifactsByContainer(final long version, final long id);

  public Set<Object[]> getArtifactsByContainer(final long version, final long minVersion, final long id);

  /***
   * Returns the properties that exhibit the specified value.
   *
   * @param version
   *          the version for which the query should be performed. <br>
   *          if (version > 0) the alive flag between the FIRST_VERSION and version<br>
   *          if (version < 0) the alive flag either in the private workspace (if overwritten) or in the base version
   * @param value
   *          the value the property should exhibit
   * @return the properties that exhibit the value.
   */
  public Map<Long, Set<String>> getPropertiesByValue(final long version, final Object value);

  /***
   * Returns the properties that reference the specified artifact.
   *
   * @param version
   *          the version for which the query should be performed. <br>
   *          if (version > 0) the alive flag between the FIRST_VERSION and version<br>
   *          if (version < 0) the alive flag either in the private workspace (if overwritten) or in the base version
   * @param id
   *          the artifact the property should reference.
   * @return the properties that exhibit the value.
   */
  public Map<Long, Set<String>> getPropertiesByReference(final long version, final Long id);

  public Set<Object[]> getArtifactsByPropertyValue(final long version, final String property, final Object value);

  public Set<Object[]> getArtifactsByPropertyValue(final long version, final long minVersion, final String property, final Object value);

  public Set<Object[]> getArtifactsByPropertyReference(final long version, final String property, final long id);

  public Set<Object[]> getArtifactsByPropertyReference(final long version, long minVersion, final String property, final long id);

  public Set<Object[]> getArtifactsWithProperty(final long version, final String property);

  public Set<Object[]> getArtifactsWithProperty(final long version, final boolean alive, final String property);

  public Set<Object[]> getArtifactsWithProperty(final long version, long minVersion, final String property);

  public Set<Object[]> getArtifactsWithProperty(final long version, long minVersion, final boolean alive, final String property);

  public Set<String> getAlivePropertyNames(final long version, final long id) throws ArtifactDoesNotExistException;

  public Set<String> getAlivePropertyNames(final long version, long minVersion, final long id) throws ArtifactDoesNotExistException;

  public Set<String> getAllPropertyNames(final long version, final long id) throws ArtifactDoesNotExistException;

  public Set<String> getAllPropertyNames(final long version, long minVersion, final long id) throws ArtifactDoesNotExistException;

  // create and get information about workspaces
  public long createWorkspace(final long owner, final long tool, final String identifier, final long baseVersion, final Long parent, final PropagationType push, final PropagationType pull);

  public void setWorkspaceParent(final long privateVersion, final Long newParent);

  public Long getWorkspaceVersionNumber(final long owner, final long tool, final String identifier);

  public List<Object[]> getWorkspaceParents(final long privateVersion);

  public Collection<Object[]> getWorkspaceChildren(final Long privateVersion);

  public List<Long> getWorkspaces();

  public boolean isWorkspaceEmpty(final long privateVersion) throws WorkspaceExpiredException;

  public boolean isWorkspaceEmpty(final long privateVersion, final long id) throws WorkspaceExpiredException;

  public boolean isWorkspaceEmpty(final long privateVersion, final long id, final String propertyName) throws WorkspaceExpiredException;

  public void deleteWorkspace(final long privateVersion) throws WorkspaceExpiredException, WorkspaceNotEmptyException;

  public long getWorkspaceBaseVersionNumber(final long privateVersion) throws WorkspaceExpiredException;

  public void setWorkspaceBaseVersionNumber(final long privateVersion, final long newBaseVersion) throws WorkspaceExpiredException;

  public long getWorkspaceOwner(final long privateVersion) throws WorkspaceExpiredException;

  public long getWorkspaceTool(final long privateVersion) throws WorkspaceExpiredException;

  public Long getWorkspaceParent(final long privateVersion) throws WorkspaceExpiredException;

  public PropagationType getWorkspacePull(final long privateVersion) throws WorkspaceExpiredException;

  public void setWorkspacePull(final long privateVersion, final PropagationType type);

  public PropagationType getWorkspacePush(final long privateVersion) throws WorkspaceExpiredException;

  public void setWorkspacePush(final long privateVersion, final PropagationType type);

  public String getWorkspaceIdentifier(final long privateVersion) throws WorkspaceExpiredException;

  /**
   * returns the representation of the requested workspace from the storage at once as an Object[]<br>
   * exists so that in certain situations performance can be increased, e.g. the owner, tool and identifier are needed at the same time, so instead of making three API calls one is sufficient
   *
   * @param privateVersion
   *          the privateVersion of the workspace requested
   * @return an Object[] containing the property representation in the following order<br>
   *         [0]: Long privateVersion<br>
   *         [1]: Long baseVersion<br>
   *         [2]: Long owner<br>
   *         [3]: Long tool<br>
   *         [4]: String identifier<br>
   * @throws WorkspaceExpiredException
   */
  public Object[] getWorkspaceRepresentation(final long privateVersion) throws WorkspaceExpiredException;

  // browse history
  public List<Long> getArtifactHistoryVersionNumbers(final long id) throws ArtifactDoesNotExistException;

  public long getArtifactMaxVersionNumber(final long id, final long version, final long previousVersion) throws ArtifactDoesNotExistException;

  public List<Long> getPropertyHistoryVersionNumbers(final long id, final String property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public long getPropertyMaxHistoryVersionNumber(final long id, final String property, final long version, final long previousVersoin) throws ArtifactDoesNotExistException,
      PropertyDoesNotExistException;

  public Long getVersionOwner(final long version);

  public String getVersionMessage(final long version);

  // commit and delete artifact/properties/versions
  public long commitArtifact(final long privateVersion, final long id, final String message) throws ArtifactDoesNotExistException, WorkspaceEmptyException;

  public void rollbackArtifact(final long privateVersion, final long id) throws ArtifactDoesNotExistException, ArtifactNotDeleteableException;

  public long commitProperty(final long privateVersion, final long id, final String property, final String message) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      WorkspaceEmptyException;

  public void rollbackProperty(final long privateVersion, final long id, final String property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException;

  public long commitVersion(final long privateVersion, final String message) throws VersionConflictException, WorkspaceEmptyException;

  public void rollbackVersion(final long privateVersion);

  // conflicting artifacts/properties for a private version, with respect to a public version
  /***
   *
   * see {@link DataStorage#getArtifactConflicts(long, long)} <br>
   * This method determines the conflicts with the current head version number.
   */
  public Set<Long> getArtifactConflicts(final long privateVersion);

  /**
   * Returns the set of artifact ids that are in conflict. <br>
   * A conflict arises if a newer version than the private versions base version exists, and the private version contains changes to artifacts that were as well edited in the range ]baseVersion:publicVersion].
   *
   * @param privateVersion
   *          the private version for which conflicts should be determined.
   * @param publicVersion
   *          the public version for which the conflicts should be determined.
   * @return the set of conflicting artifact ids.
   */
  public Set<Long> getArtifactConflicts(final long privateVersion, final long publicVersion);

  /**
   * see {@link DataStorage#getPropertyConflicts(long, long)} <br>
   * This method determines the conflicts with the current head version number.
   */
  public Map<Long, Set<String>> getPropertyConflicts(final long privateVersion);

  /**
   * Returns the properties (artifactid to propertie names) that are in conflict. <br>
   * A conflict arises if a newer version than the private versions base version exists, and the private version contains changes to properties that were as well edited in the range ]baseVersion:publicVersion].
   *
   * @param privateVersion
   *          the private version for which conflicts should be determined.
   * @param publicVersion
   *          the public version for which the conflicts should be determined.
   * @return the map of conflicting artifact ids to property names.
   */
  public Map<Long, Set<String>> getPropertyConflicts(final long privateVersion, final long publicVersion);

  // differing artifact/properties of two public versions
  /**
   * Returns the set of artifact ids that were change in the specified range of versions.
   *
   * @param version
   *          the specified upper version.
   * @param previousVersion
   *          the specified lower version.
   * @return the set of artifact ids that were change in the range [previousVersion:version].
   */
  public Set<Long> getArtifactDiffs(final long version, final long previousVersion);

  /**
   * Returns the artifact ids and properties that were edited exactly in the specified version.
   *
   * @param version
   *          the version for which the set of changes need to be obtained.
   * @return artifact ids and its properties that were edited.
   */
  public Map<Long, Set<String>> getArtifactPropertyDiffs(final long version);

  /**
   * Returns the artifact ids and properties that were edited in specified range of versions.
   *
   * @param version
   *          the specified upper version.
   * @param previousVersion
   *          the specified lower version.
   * @return the set of artifact ids and properties that were changed in the range [previosVersion:version].
   */
  public Map<Long, Set<String>> getArtifactPropertyDiffs(final long version, final long previousVersion);

  public Map<Long, Set<String>> getWorkspaceDiffs(final long privateVersionOne, final long privateVersionTwo);

  /**
   * Pushes the specified artifact (artifactId) and all its properties from the child (childId) to the parent (parentId).
   * 
   * @param childId
   *          the id of the child version
   * @param parentId
   *          the id of the parent version
   * @param artifactId
   *          the artifact to be pushed
   * @throws ArtifactDoesNotExistException
   *           if the artifact does not exist in the child
   * @throws ArtifactNotPushOrPullableException
   *           if the artifact or its properties references (container, project, type, reference) another artifact not contained within the parent
   * @throws IllegalArgumentException
   *           if either child or parent are not private versions
   */
  public void pushArtifact(final long childId, final long parentId, final long artifactId) throws ArtifactDoesNotExistException, ArtifactNotPushOrPullableException, IllegalArgumentException;

  /**
   * Pushes the specified property (artifactId, property) from the child (childId) to the parent (parentId).
   * 
   * @param childId
   *          the id of the child version
   * @param parentId
   *          the id of the parent version
   * @param artifactId
   *          the artifact to which the property belongs
   * @param property
   *          the property to be pushed
   * @throws ArtifactDoesNotExistException
   *           if the artifact does not exist in the child version
   * @throws PropertyDoesNotExistException
   *           if the property does not exist in the child version
   * @throws PropertyNotPushOrPullableException
   *           if the property references (the artifact it belongs to or its reference) an artifact not contained within the parent version
   * @throws IllegalArgumentException
   *           if either child or parent are not private versions
   */
  public void pushProperty(final long childId, final long parentId, final long artifactId, final String property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      PropertyNotPushOrPullableException, IllegalArgumentException;

  /**
   * Pushes all changes from the child (childId) to the parent (parentId).
   * 
   * @param childId
   *          the id of the child version
   * @param parentId
   *          the id of the parent version
   * @throws IllegalArgumentException
   *           if either child or parent are not private versions
   */
  public void pushAll(final long childId, final long parentId) throws IllegalArgumentException;

  /**
   * Pulls the specified artifact (artifactId) and all its properties from the parent (parentId) to the child (childId).
   * 
   * 
   * @param childId
   *          the id of the child version
   * @param parentId
   *          the id of the parent version
   * @param artifactId
   *          the artifact to be pulled
   * @throws ArtifactDoesNotExistException
   *           if the artifact does not exist in the child
   * @throws ArtifactNotPushOrPullableException
   *           if the artifact or its properties references (container, project, type, reference) another artifact not contained within the child
   * @throws IllegalArgumentException
   *           if either child or parent are not private versions
   */
  public void pullArtifact(final long childId, final long parentId, final long artifactId) throws ArtifactDoesNotExistException, ArtifactNotPushOrPullableException, IllegalArgumentException;

  /**
   * Pulls the specified property (artifactId, property) from the parent (parentId) to the child (childId).
   * 
   * @param childId
   *          the id of the child version
   * @param parentId
   *          the id of the parent version
   * @param artifactId
   *          the artifact to which the property belongs
   * @param property
   *          the property to be pulled
   * @throws ArtifactDoesNotExistException
   *           if the artifact does not exist in the parent version
   * @throws PropertyDoesNotExistException
   *           if the property does not exist in the parent version
   * @throws PropertyNotPushOrPullableException
   *           if the property references (the artifact it belongs to or its reference) an artifact not contained within the chidl version
   * @throws IllegalArgumentException
   *           if either child or parent are not private versions
   */
  public void pullProperty(final long childId, final long parentId, final long artifactId, final String property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      PropertyNotPushOrPullableException, IllegalArgumentException;

  /**
   * Pulls all changes from the parent (parentId) to the child (childId).
   * 
   * @param childId
   *          the id of the child version
   * @param parentId
   *          the id of the parent version
   * @throws IllegalArgumentException
   *           if either child or parent are not private versions
   */
  public void pullAll(final long childId, final long parentId) throws IllegalArgumentException;

  // listener methods
  public void addListener(final DataStorageListener listener);

  public void removeListener(final DataStorageListener listener);

  // get maximum/head version numbers and id
  public long getMaximumArtifactId();

  public long getHeadVersionNumber();

  public long getPrivateHeadVersionNumber();

  public void init();

  public void truncateAll();
  
  // user management methods
  // (c) jMayer 2016
  
  /**
   * gets a user by its login and password, for establishing a session
   * 
   * @param name
   * @param password
   * @return
   * @throws CredentialsException if there is no user with these credentials 
   */
  public User getUserByCredentials(String login, String password) throws CredentialsException;

  /**
   * gets a user by its Owner instance
   * @param ownerId
   * @return
   */
  public User getUserByOwnerId(long ownerId) throws OwnerDoesNotExistException;

  /**
   * gets a user by its ID
   * @param id
   * @return
   * @throws OwnerDoesNotExistException if the user with the ID does not exist
   */
  public User getUser(long id) throws OwnerDoesNotExistException;
  
  /**
   * creates a user and returns an object for it
   * @param name
   * @param login
   * @param password
   * @return          the created user
   * @throws CredentialsException if the login existed already
   */
  public User createUser(String name, String login, String password) throws CredentialsException;
  
  /**
   * Updates a user's DB fields and returns a new User instance for the changes.
   * 
   * The corresponding properties of the Owner artifact are changed as well and saved in a new public version.
   * 
   * @param user
   * @return
   * @throws CredentialsException       if the login property was changed, but there was already a user with the desired login
   * @throws IllegalArgumentException   if any property of the change are invalid, e.g. attempt to change immutable fields like ownerId
   * @throws OwnerDoesNotExistException if the user with given id does not exist
   */
  public User updateUser(User user) throws CredentialsException, IllegalArgumentException, OwnerDoesNotExistException;
  
  /**
   * deletes a user by ID
   * @param id
   * @throws OwnerDoesNotExistException if the user with given id does not exist.
   */
  public void deleteUser(long id) throws OwnerDoesNotExistException;

  /**
   * returns a list with all existing users.
   * @return
   */
  public Collection<User> getUsers();
  
  // tools
  
  /**
   * Creates a new tool with name and toolVersion properties already set.
   * @param name
   * @param toolVersion
   * @return created Tool instance
   */
  public Tool createTool(String name, String toolVersion);
  
  /**
   * Deletes a Tool by id.
   * @param id
   * @throws ToolDoesNotExistException if there was no tool with this id.
   */
  public void deleteTool(long id) throws ToolDoesNotExistException;

}
