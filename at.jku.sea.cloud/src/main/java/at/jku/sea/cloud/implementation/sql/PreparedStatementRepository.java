package at.jku.sea.cloud.implementation.sql;

import static at.jku.sea.cloud.DataStorage.FIRST_VERSION;
import static at.jku.sea.cloud.DataStorage.NO_CONTAINER_ID;
import static at.jku.sea.cloud.DataStorage.VOID_VERSION;
import static at.jku.sea.cloud.implementation.sql.SQLDataStorage.ARTIFACTS;
import static at.jku.sea.cloud.implementation.sql.SQLDataStorage.PRIVATE;
import static at.jku.sea.cloud.implementation.sql.SQLDataStorage.PROPS;
import static at.jku.sea.cloud.implementation.sql.SQLDataStorage.VERMSG;
import static at.jku.sea.cloud.implementation.sql.SQLDataStorage.USERS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import at.jku.sea.cloud.DataStorage;

public class PreparedStatementRepository {

  static PreparedStatement shutdown;

  /** Artifact **/
  static PreparedStatement updateArtifactVersion;
  static PreparedStatement updateArtifacts;
  static PreparedStatement deleteArtifact;
  static PreparedStatement deleteArtifacts;
  static PreparedStatement getArtifactCount;
  static PreparedStatement getArtifactCountByID;
  static PreparedStatement getMaxArtifactID;
  static PreparedStatement getArtifactVersionGT;
  static PreparedStatement getArtifactVersionLT;
  static PreparedStatement getMaxArtifactVersion;
  static PreparedStatement mergeArtifact;
  static PreparedStatement getArtifactRepresentationPrivate;
  static PreparedStatement getArtifactRepresentationPublic;
  static PreparedStatement getMaxArtifactVersion2;
  static PreparedStatement getArtifactsByPropertySQLStatementPrivate;
  static PreparedStatement getArtifactsByPropertySQLStatementPrivateReference;
  static PreparedStatement getArtifactsByPropertySQLStatementPublic;
  static PreparedStatement getArtifactsByPropertySQLStatementPublicReference;
  static PreparedStatement getArtifactsByReferencePrivate;
  static PreparedStatement getArtifactsByReferencePublic;
  static PreparedStatement getArtifactsPrivate;
  static PreparedStatement getArtifactsPublic;

  /** Property **/
  static PreparedStatement deleteProperty;
  static PreparedStatement deleteProperties;
  static PreparedStatement deletePropertiesOfArtifact;
  static PreparedStatement getPropertyCount;
  static PreparedStatement getPropertyCountByID;
  static PreparedStatement getPropertyCountByIDAndName;
  static PreparedStatement updateArtifactPropertyVersion;
  static PreparedStatement updatePropertyVersion;
  static PreparedStatement updateProperties;
  static PreparedStatement mergeProperty;
  static PreparedStatement getMaxPropertyVersion;
  static PreparedStatement getMaxPropertyVersionByIDAndName;
  static PreparedStatement getPropertiesInWs;
  static PreparedStatement getPropertiesOfArtifactInWs;
  static PreparedStatement getPropertyNamesAndValuesPrivate;
  static PreparedStatement getPropertyNamesAndValuesPublic;
  static PreparedStatement getPropertyNamesAndValuesPrivateAlive;
  static PreparedStatement getPropertyNamesAndValuesPublicAlive;
  static PreparedStatement getPropertiesByValueSQLStatementPrivate;
  static PreparedStatement getPropertiesByValueSQLStatementPrivateReference;
  static PreparedStatement getPropertiesByValueSQLStatementPublic;
  static PreparedStatement getPropertiesByValueSQLStatementPublicReference;
  static PreparedStatement getPropertyHistoryVersionNumbersGT;
  static PreparedStatement getPropertyHistoryVersionNumbersLT;
  static PreparedStatement getPropertyRepresentationPrivate;
  static PreparedStatement getPropertyRepresentationPublic;

  /** Workspace **/
  static PreparedStatement deletePrivateWorkspace;
  static PreparedStatement insertPrivateWorkspace;
  static PreparedStatement updatePrivateWorkspace;
  static PreparedStatement getWorkspaces;
  static PreparedStatement getPrivateVersion;
  static PreparedStatement updateWorkspaceParent;
  static PreparedStatement updateWorkspacePush;
  static PreparedStatement updateWorkspacePull;
  static PreparedStatement getMinPrivateVersion;
  static PreparedStatement getArtifactsInWS;
  static PreparedStatement getWorkspaceRepresentation;
  static PreparedStatement getChildWorkspaces;
  static PreparedStatement getChildWorkspacesParentNull;

  /** Version **/
  static PreparedStatement insertVersionMessage;
  static PreparedStatement deleteVersionMessages;
  static PreparedStatement getOwnerFromVersionMessage;
  static PreparedStatement getMessageFromVersionMessage;

  /** User */
  static PreparedStatement insertUser;
  static PreparedStatement getUser;
  static PreparedStatement updateUser;
  static PreparedStatement deleteUser;
  static PreparedStatement getOwnerForUser;
  static PreparedStatement getUserForOwner;
  static PreparedStatement deleteArtifactUnversioned;
  static PreparedStatement deletePropertiesUnversioned;
  
  static PreparedStatement getUsers;
  static PreparedStatement getMaxUserId;
  static PreparedStatement getUserByLogin;
  static PreparedStatement getUserPasswordHash;
  
  
  static void prepareStatements(Connection conn) throws SQLException {
    deleteArtifactUnversioned = conn.prepareStatement("DELETE FROM " + ARTIFACTS + " WHERE id = ?");
    deletePropertiesUnversioned = conn.prepareStatement("DELETE FROM "+ PROPS + " WHERE artifactId = ?");
    //users statements
    getUsers = conn.prepareStatement("SELECT * FROM " + USERS);
    getUser = conn.prepareStatement("SELECT * FROM " + USERS + " WHERE id = ?");
    insertUser = conn.prepareStatement("INSERT INTO " + USERS + " VALUES(?,?,?,?,?)");
    updateUser = conn.prepareStatement("UPDATE " + USERS + " SET name = ?, login = ?, password = ? WHERE id = ?");
    getMaxUserId = conn.prepareStatement("SELECT max(id) FROM " + USERS);
    deleteUser = conn.prepareStatement("DELETE FROM " + USERS + " WHERE id = ?");
    getUserByLogin = conn.prepareStatement("SELECT * FROM " + USERS + " WHERE login = ?");
    getOwnerForUser = conn.prepareStatement("SELECT owner FROM " + USERS + " WHERE id = ?");
    getUserForOwner = conn.prepareStatement("SELECT * FROM " + USERS + " WHERE owner = ?");
    getUserPasswordHash = conn.prepareStatement("SELECT password FROM " + USERS + " WHERE id = ?");
    //artifacts statements
    shutdown = conn.prepareStatement("SHUTDOWN");
    updateArtifactVersion = conn.prepareStatement("UPDATE " + ARTIFACTS + " SET version = ? WHERE version = ? AND id = ?");
    updatePrivateWorkspace = conn.prepareStatement("UPDATE " + PRIVATE + " SET base_version = ? WHERE private_version = ?");
    insertVersionMessage = conn.prepareStatement("INSERT INTO " + VERMSG + " VALUES (?,?,?)");
    updatePropertyVersion = conn.prepareStatement("UPDATE " + PROPS + " SET version = ? WHERE version = ? AND artifactId = ? AND name = ?");
    deleteArtifact = conn.prepareStatement("DELETE FROM " + ARTIFACTS + " WHERE version = ? AND id = ?");
    deleteProperty = conn.prepareStatement("DELETE FROM " + PROPS + " WHERE version = ? AND artifactId = ? AND name = ?");
    updateArtifacts = conn.prepareStatement("UPDATE " + ARTIFACTS + " SET version = ? WHERE version = ?");
    updateProperties = conn.prepareStatement("UPDATE " + PROPS + " SET version = ? WHERE version = ?");
    updateArtifactPropertyVersion = conn.prepareStatement("UPDATE " + PROPS + " SET version = ? WHERE version = ? AND artifactId=?");
    deleteArtifacts = conn.prepareStatement("DELETE FROM " + ARTIFACTS + " WHERE version = ?");
    deleteProperties = conn.prepareStatement("DELETE FROM " + PROPS + " WHERE version = ?");
    deletePropertiesOfArtifact = conn.prepareStatement("DELETE FROM " + PROPS + " WHERE version = ? AND artifactId=?");
    deletePrivateWorkspace = conn.prepareStatement("DELETE FROM " + PRIVATE + " WHERE private_version = ?");
    deleteVersionMessages = conn.prepareStatement("DELETE FROM " + VERMSG + " WHERE version = ?");
    getArtifactCount = conn.prepareStatement("SELECT COUNT(*) FROM artifacts WHERE version=?");
    getPropertyCount = conn.prepareStatement("SELECT COUNT(*) FROM properties WHERE version=?");
    getArtifactCountByID = conn.prepareStatement("SELECT COUNT(*) FROM " + ARTIFACTS + " WHERE version = ? AND id = ?");
    getPropertyCountByID = conn.prepareStatement("SELECT COUNT(*) FROM properties WHERE version=? and artifactid=? ");
    getPropertyCountByIDAndName = conn.prepareStatement("SELECT COUNT(*), name FROM " + PROPS + " WHERE version = ? AND artifactId = ? AND name = ? GROUP BY name");
    getOwnerFromVersionMessage = conn.prepareStatement("SELECT owner FROM " + VERMSG + " WHERE version=?");
    getMessageFromVersionMessage = conn.prepareStatement("SELECT msg FROM " + VERMSG + " WHERE version=?");
    insertPrivateWorkspace = conn.prepareStatement("INSERT INTO " + PRIVATE + " VALUES (?,?,?,?,?,?,?,?)");
    getMaxArtifactID = conn.prepareStatement("SELECT MAX(id) FROM " + ARTIFACTS);
    getArtifactVersionGT = conn.prepareStatement("SELECT version FROM " + ARTIFACTS + " WHERE id = ? AND version > 0");
    getMaxArtifactVersion = conn.prepareStatement("SELECT MAX(version) FROM " + ARTIFACTS + " WHERE version > ? AND version <= ? AND id = ?");
    getPropertiesInWs = conn.prepareStatement("SELECT  * FROM " + PROPS + " WHERE version = ?");
    getPropertiesOfArtifactInWs = conn.prepareStatement("SELECT  * FROM " + PROPS + " WHERE version = ? and artifactid=? ");
    getArtifactsInWS = conn.prepareStatement("SELECT * FROM " + ARTIFACTS + " WHERE version = ?");
    getMinPrivateVersion = conn.prepareStatement("SELECT MIN(private_version) FROM " + PRIVATE);
    getPropertyHistoryVersionNumbersGT = conn.prepareStatement("SELECT version FROM " + PROPS + " WHERE artifactId = ? AND name = ? AND version > 0");
    getPropertyHistoryVersionNumbersLT = conn.prepareStatement("SELECT version FROM " + PROPS + " WHERE artifactId = ? AND name = ? AND version < 0");
    mergeProperty = conn
        .prepareStatement("MERGE INTO "
            + PROPS
            + " USING (VALUES(?, ?, ?)) AS vals(id, version, name) ON properties.artifactid = vals.id AND properties.version = vals.version AND properties.name = vals.name WHEN MATCHED THEN UPDATE SET owner = ?, value = ?, reference = ?, alive = ? WHEN NOT MATCHED THEN INSERT VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

    mergeArtifact = conn
        .prepareStatement("MERGE INTO "
            + ARTIFACTS
            + " USING (VALUES(?, ?)) AS vals(id, version) ON artifacts.id = vals.id AND artifacts.version = vals.version WHEN MATCHED THEN UPDATE SET owner = ?, type = ?, container=?, alive = ? WHEN NOT MATCHED THEN INSERT VALUES (?, ?, ?, ?, ?, ?, ?)");

    getArtifactRepresentationPrivate = conn.prepareStatement("SELECT * FROM " + ARTIFACTS + " WHERE (version = ? or version = (SELECT MAX(version) FROM " + ARTIFACTS
        + " WHERE version > 0 AND version <= (SELECT base_version FROM private_WS WHERE private_version=?) AND id = ?)) AND id = ? ORDER BY version");
    getArtifactRepresentationPublic = conn.prepareStatement("SELECT * FROM " + ARTIFACTS + " WHERE (version = ? or version = (SELECT MAX(version) FROM " + ARTIFACTS
        + " WHERE version > 0 AND version <= ? AND id = ?)) AND id = ? ORDER BY version");

    getPropertyRepresentationPrivate = conn
        .prepareStatement("SELECT * FROM properties WHERE (version = ? or version = (SELECT MAX(version) FROM properties WHERE version > 0 AND version <= (SELECT base_version FROM private_WS WHERE private_version=?) AND artifactId = ? AND name = ?)) AND artifactId = ? AND name = ? ORDER BY version");
    getPropertyRepresentationPublic = conn
        .prepareStatement("SELECT * FROM properties WHERE (version = ? or version = (SELECT MAX(version) FROM properties WHERE version > 0 AND version <= ? AND artifactId = ? AND name = ?)) AND artifactId = ? AND name = ? ORDER BY version");

    getMaxArtifactVersion2 = conn.prepareStatement("SELECT MAX(version) FROM " + ARTIFACTS);
    getMaxPropertyVersion = conn.prepareStatement("SELECT MAX(version) FROM " + PROPS);
    getMaxPropertyVersionByIDAndName = conn.prepareStatement("SELECT MAX(version) FROM " + PROPS + " WHERE version <= ? AND version > ? AND artifactId = ? AND name = ?");
    getWorkspaceRepresentation = conn.prepareStatement("SELECT * FROM " + PRIVATE + " WHERE private_version = ?");
    getWorkspaces = conn.prepareStatement("SELECT private_version FROM " + PRIVATE);
    getChildWorkspaces = conn.prepareStatement("SELECT * FROM " + PRIVATE + " WHERE parent = ?");
    getChildWorkspacesParentNull = conn.prepareStatement("SELECT * FROM " + PRIVATE + " WHERE parent IS NULL");
    getPrivateVersion = conn.prepareStatement("SELECT private_version FROM " + PRIVATE + " WHERE owner = ? AND identifier = ? AND tool = ?");
    getArtifactVersionLT = conn.prepareStatement("SELECT version FROM " + ARTIFACTS + " WHERE id = ? AND version < 0");
    updateWorkspaceParent = conn.prepareStatement("UPDATE " + PRIVATE + " SET parent = ? WHERE private_version = ?");
    updateWorkspacePush = conn.prepareStatement("UPDATE " + PRIVATE + " SET push = ? WHERE private_version = ?");
    updateWorkspacePull = conn.prepareStatement("UPDATE " + PRIVATE + " SET pull = ? WHERE private_version = ?");

    /**
     * So, you have come across the more complex part of this class. Don't despair, it is not as complicated as it looks.
     * 
     * At first I want to give you a reason why this seems rather complicated and the reason why I didn't do it "simple" (i.e., simple write the whole query and be done with it): A
     * lot of the queries that need to exist are combinations of basic queries (e.g., give me all from a private version combined with the within a range of public versions)
     * Writing these queries thus would result in redundancy, and further, if something needs to be changed, it would be very error prone to maintain.
     * 
     * Markus
     */

    /**
     * These are the building blocks with which more complex queries are built.
     */

    /*
     * Selects all properties within a public version range with the highest revision number. Additionally, filters can be added "%s" (e.g., the property should have a certain
     * name, or only properties from a specific artifact)
     */
    String selectPublic = "SELECT artifactId, name, MAX(version) AS version FROM " + PROPS + " WHERE version >= ? AND version <= ? %s GROUP BY artifactId, name ";
    /*
     * Selects all properties from a private version. Additionally, filters can be added "%s" (e.g., the property should have a certain name, or only properties from a specific
     * artifact)
     */
    String selectPrivate = "SELECT artifactId, name, version FROM " + PROPS + " WHERE version = ? %s GROUP BY artifactId, name, version) ";
    // Used for union of private and public artifacts
    String union = " UNION ";
    // Selects from the union all properties with the minimum version number (either a negative or the maximum positive number)
    String selectFromUnion = "SELECT artifactId, name, MIN(version) AS version FROM (";
    // Groups the union by artifactId and name
    String groupSelectUnion = "GROUP BY artifactId, name";
    /*
     * Makes a join with results from the select of the union, and gets the whole property information Reason: the additional information, like owner, tool, alive, etc. would mess
     * up the previous selects
     */
    String selectProperty = "SELECT artifactId, version, owner, tool, name, value, alive, reference  FROM " + PROPS + " JOIN (";
    // Join on artifactId, propertyName and version
    String join = ") AS x ON properties.artifactId=x.artifactId AND properties.name=x.name AND properties.version=x.version";
    // Additionally, further filters can be added
    String alive = " AND alive=?";
    String addReference = " AND reference=?";
    String filterArtifactId = "AND artifactId=?";
    String filterName = "AND name=?";

    // Exactly the same scheme as for properties
    String selectFromPublicArtifacts = "SELECT id, MAX(version) AS version FROM ARTIFACTS  WHERE version >= ? AND version <= ? GROUP BY id";
    String selectFromPrivateArtifacts = "SELECT id, version FROM ARTIFACTS WHERE version = ? GROUP BY id, version) ";
    String selectFromArtifactUnion = " SELECT id, MIN(version) AS version FROM (";
    String groupById = "GROUP BY id";

    String joinOnArtifactId = "ON lastProperties.artifactId = lastArtifacts.id AND lastProperties.version = lastArtifacts.version";
    String joinOnReferenceId = "ON lastProperties.reference = lastArtifacts.id AND lastProperties.version = lastArtifacts.version";

    String addReferenceFramework = " AND lastProperties.reference=?";

    String privateProperties = selectFromUnion + selectPublic + union + selectPrivate + groupSelectUnion;
    String privateArtifacts = selectFromArtifactUnion + selectFromPublicArtifacts + union + selectFromPrivateArtifacts + groupById;

    getPropertyNamesAndValuesPrivate = conn.prepareStatement(selectProperty + selectFromUnion + String.format(selectPublic, filterArtifactId) + union + String.format(selectPrivate, filterArtifactId)
        + groupSelectUnion + join);
    getPropertyNamesAndValuesPublic = conn.prepareStatement(selectProperty + String.format(selectPublic, filterArtifactId) + join);

    getPropertyNamesAndValuesPrivateAlive = conn.prepareStatement(selectProperty + selectFromUnion + String.format(selectPublic, filterArtifactId) + union
        + String.format(selectPrivate, filterArtifactId) + groupSelectUnion + join + alive);
    getPropertyNamesAndValuesPublicAlive = conn.prepareStatement(selectProperty + String.format(selectPublic, filterArtifactId) + join + alive);

    getPropertiesByValueSQLStatementPrivate = conn.prepareStatement(selectProperty + selectFromUnion + String.format(selectPublic, "") + union + String.format(selectPrivate, "") + groupSelectUnion
        + join + alive);
    getPropertiesByValueSQLStatementPrivateReference = conn.prepareStatement(selectProperty + selectFromUnion + String.format(selectPublic, "") + union + String.format(selectPrivate, "")
        + groupSelectUnion + join + alive + addReference);
    getPropertiesByValueSQLStatementPublic = conn.prepareStatement(selectProperty + String.format(selectPublic, "") + join + alive);
    getPropertiesByValueSQLStatementPublicReference = conn.prepareStatement(selectProperty + String.format(selectPublic, "") + join + alive + addReference);

    /*
     * This is probably the most complex one, this contains everything.
     * 
     * It first selects all artifacts in the desired version (public and private, only public) as arts, then selects all properties in the desired version (public and private, only
     * public) as props.
     * 
     * However, hsqldb has a bug (no subqueries beyond level 3), so the final select with the join of the artifact/property table is done outside the with.
     * 
     * Then, finally, a join between those subsets needs to be done
     */
    String getArtifactsByPropertyFramework = "WITH arts (id, version) AS (%s), props (artifactId, version, name) AS (%s) "
        + "SELECT lastArtifacts.id, ? as version, lastArtifacts.owner, lastArtifacts.tool, lastArtifacts.type, lastArtifacts.container, lastArtifacts.alive, lastProperties.name, lastProperties.value, lastProperties.reference FROM "
        + "(SELECT artifactId, ? as version, name, value, reference FROM PROPERTIES JOIN props ON properties.artifactId=props.artifactId AND properties.name=props.name AND properties.version=props.version) as lastProperties "
        + "JOIN (SELECT id, ? as version, owner, tool, type, container, alive FROM ARTIFACTS JOIN arts ON arts.id = ARTIFACTS.id AND arts.version = ARTIFACTS.version "
        + "WHERE ARTIFACTS.alive = ? AND ARTIFACTS.owner = ISNULL(?, owner)  AND ARTIFACTS.tool = ISNULL(?, tool) AND ARTIFACTS.type = ISNULL(?, type) AND (? IS NULL OR ? = ARTIFACTS.container)) as lastArtifacts ";

    getArtifactsByPropertySQLStatementPrivate = conn.prepareStatement(String.format(getArtifactsByPropertyFramework, privateArtifacts, String.format(privateProperties, filterName, filterName))
        + joinOnArtifactId);
    getArtifactsByPropertySQLStatementPrivateReference = conn.prepareStatement(String.format(getArtifactsByPropertyFramework, privateArtifacts,
        String.format(privateProperties, filterName, filterName))
        + joinOnArtifactId + addReferenceFramework);

    getArtifactsByPropertySQLStatementPublic = conn.prepareStatement(String.format(getArtifactsByPropertyFramework, selectFromPublicArtifacts, String.format(selectPublic, filterName))
        + joinOnArtifactId);
    getArtifactsByPropertySQLStatementPublicReference = conn.prepareStatement(String.format(getArtifactsByPropertyFramework, selectFromPublicArtifacts, String.format(selectPublic, filterName))
        + joinOnArtifactId + addReferenceFramework);

    getArtifactsByReferencePrivate = conn.prepareStatement(String.format(getArtifactsByPropertyFramework, privateArtifacts, String.format(privateProperties, filterArtifactId, filterArtifactId))
        + joinOnReferenceId);
    getArtifactsByReferencePublic = conn.prepareStatement(String.format(getArtifactsByPropertyFramework, selectFromPublicArtifacts, String.format(selectPublic, filterArtifactId)) + joinOnReferenceId);

    String joinArtifacts = ") AS lastArtifacts ON ARTIFACTS.version = lastArtifacts.version AND ARTIFACTS.id = lastArtifacts.id ";

    getArtifactsPrivate = conn.prepareStatement("SELECT * FROM ARTIFACTS JOIN (" + privateArtifacts + joinArtifacts
        + " WHERE alive = ? AND owner = ISNULL(?, owner)  AND tool = ISNULL(?, tool) AND type = ISNULL(?, type) AND (? IS NULL OR ? = container)");
    getArtifactsPublic = conn.prepareStatement("SELECT * FROM ARTIFACTS JOIN (" + selectFromPublicArtifacts + joinArtifacts
        + " WHERE alive = ? AND owner = ISNULL(?, owner)  AND tool = ISNULL(?, tool) AND type = ISNULL(?, type) AND (? IS NULL OR ? = container)");

  }

  static PreparedStatement getPropertyNamesAndValuesSQLStatement(DataStorage storage, final List<Object> parameters, final long version, final Long minVersion, final long artifactId,
      final Boolean alive) {
    if (version < VOID_VERSION) {
      final Long baseVersion = storage.getWorkspaceBaseVersionNumber(version);
      parameters.add(FIRST_VERSION);
      parameters.add(baseVersion);
      parameters.add(artifactId);
      parameters.add(version);
      parameters.add(artifactId);
      if (alive != null) {
        parameters.add(alive);
        return getPropertyNamesAndValuesPrivateAlive;
      } else {
        return getPropertyNamesAndValuesPrivate;
      }
    } else {
      if (minVersion == null) {
        parameters.add(FIRST_VERSION);
      } else {
        parameters.add(minVersion);
      }
      parameters.add(version);
      parameters.add(artifactId);
      if (alive != null) {
        parameters.add(alive);
        return getPropertyNamesAndValuesPublicAlive;
      } else {
        return getPropertyNamesAndValuesPublic;
      }
    }
  }

  static PreparedStatement getArtifactsByPropertySQLStatement(DataStorage storage, final List<Object> parameters, final long version, final Long minVersion, final String property,
      final boolean alive, final Long reference, Long ownerId, Long toolId, Long typeId, Long containerId) {
    if (version < 0) {
      final Long baseVersion = storage.getWorkspaceBaseVersionNumber(version);
      parameters.add(FIRST_VERSION);
      parameters.add(baseVersion);
      parameters.add(version);
      parameters.add(FIRST_VERSION);
      parameters.add(baseVersion);
      parameters.add(property);
      parameters.add(version);
      parameters.add(property);
      parameters.add(version);
      parameters.add(version);
      parameters.add(version);
      parameters.add(alive);
      parameters.add(ownerId);
      parameters.add(toolId);
      parameters.add(typeId);
      if (containerId != null && containerId == NO_CONTAINER_ID) {
        parameters.add(null);
        parameters.add(null);
      } else {
        parameters.add(containerId);
        parameters.add(containerId);
      }
      if (reference != null) {
        parameters.add(reference);
        return getArtifactsByPropertySQLStatementPrivateReference;
      } else {
        return getArtifactsByPropertySQLStatementPrivate;
      }

    } else {
      parameters.add(version);
      if (minVersion == null) {
        parameters.add(FIRST_VERSION);
      } else {
        parameters.add(minVersion);
      }
      if (minVersion == null) {
        parameters.add(FIRST_VERSION);
      } else {
        parameters.add(minVersion);
      }
      parameters.add(version);
      parameters.add(property);
      parameters.add(version);
      parameters.add(version);
      parameters.add(version);
      parameters.add(alive);
      parameters.add(ownerId);
      parameters.add(toolId);
      parameters.add(typeId);
      if (containerId != null && containerId == NO_CONTAINER_ID) {
        parameters.add(null);
        parameters.add(null);
      } else {
        parameters.add(containerId);
        parameters.add(containerId);
      }
      if (reference != null) {
        parameters.add(reference);
        return getArtifactsByPropertySQLStatementPublicReference;
      } else {
        return getArtifactsByPropertySQLStatementPublic;
      }
    }
  }

  static PreparedStatement getArtifactRepsSQLStatement(DataStorage storage, final List<Object> parameters, final long version, final Long minVersion, final boolean alive, final Long toolId,
      final Long ownerId, final Long typeId, final Long containerId) {
    PreparedStatement ps;
    if (version < VOID_VERSION) {
      ps = getArtifactsPrivate;
      final Long baseVersion = storage.getWorkspaceBaseVersionNumber(version);
      parameters.add(FIRST_VERSION);
      parameters.add(baseVersion);
      parameters.add(version);
    } else {
      ps = getArtifactsPublic;
      if (minVersion == null) {
        parameters.add(FIRST_VERSION);
      } else {
        parameters.add(minVersion);
      }
      parameters.add(version);
    }
    parameters.add(alive);
    parameters.add(ownerId);
    parameters.add(toolId);
    parameters.add(typeId);
    if (containerId != null && containerId == NO_CONTAINER_ID) {
      parameters.add(null);
      parameters.add(null);
    } else {
      parameters.add(containerId);
      parameters.add(containerId);
    }
    return ps;
  }

  static PreparedStatement getArtifactsByReference(DataStorage storage, final List<Object> parameters, final long version, final long artifactId) {
    if (version < 0) {
      final Long baseVersion = storage.getWorkspaceBaseVersionNumber(version);
      parameters.add(FIRST_VERSION);
      parameters.add(baseVersion);
      parameters.add(version);
      parameters.add(FIRST_VERSION);
      parameters.add(baseVersion);
      parameters.add(artifactId);
      parameters.add(version);
      parameters.add(artifactId);
      parameters.add(version);
      parameters.add(version);
      parameters.add(version);
      parameters.add(true);
      parameters.add(null);
      parameters.add(null);
      parameters.add(null);
      parameters.add(null);
      parameters.add(null);
      return getArtifactsByReferencePrivate;
    } else {
      parameters.add(FIRST_VERSION);
      parameters.add(version);
      parameters.add(artifactId);
      parameters.add(version);
      parameters.add(version);
      parameters.add(version);
      parameters.add(true);
      parameters.add(null);
      parameters.add(null);
      parameters.add(null);
      parameters.add(null);
      parameters.add(null);
      return getArtifactsByReferencePublic;
    }
  }

  static PreparedStatement getPropertiesByPropertyValueSQLStatement(DataStorage storage, final List<Object> parameters, final long version, final Long minVersion, final boolean alive,
      final Long reference) {
    if (version < 0) {
      final Long baseVersion = storage.getWorkspaceBaseVersionNumber(version);
      parameters.add(FIRST_VERSION);
      parameters.add(baseVersion);
      parameters.add(version);
      parameters.add(alive);
      if (reference != null) {
        parameters.add(reference);
        return getPropertiesByValueSQLStatementPrivateReference;
      } else {
        return getPropertiesByValueSQLStatementPrivate;
      }
    } else {
      if (minVersion == null) {
        parameters.add(FIRST_VERSION);
      } else {
        parameters.add(minVersion);
      }
      parameters.add(version);
      parameters.add(alive);
      if (reference != null) {
        parameters.add(reference);
        return getPropertiesByValueSQLStatementPublicReference;
      } else {
        return getPropertiesByValueSQLStatementPublic;
      }
    }
  }

}
