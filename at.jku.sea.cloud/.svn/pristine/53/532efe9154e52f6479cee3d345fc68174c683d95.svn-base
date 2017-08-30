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
 * SQLjava created on 14.03.2013
 *
 * (c) thomas schartmueller
 */
package at.jku.sea.cloud.implementation.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.exceptions.ArtifactConflictException;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.ArtifactNotCommitableException;
import at.jku.sea.cloud.exceptions.ArtifactNotDeleteableException;
import at.jku.sea.cloud.exceptions.CredentialsException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyConflictException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyNotCommitableException;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionConflictException;
import at.jku.sea.cloud.exceptions.WorkspaceEmptyException;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.exceptions.WorkspaceNotEmptyException;
import at.jku.sea.cloud.implementation.AbstractDataStorage;
import at.jku.sea.cloud.implementation.DefaultTool;
import at.jku.sea.cloud.implementation.DefaultUser;
import at.jku.sea.cloud.implementation.ObjectArrayRepresentationUtils;
import at.jku.sea.cloud.implementation.events.ArtifactEvent;

/**
 * @author thomas schartmueller
 * @author alexander noehrer
 * @author mriedl
 */
public class SQLDataStorage extends AbstractDataStorage implements DataStorage, Serializable {
  private static final long serialVersionUID = 1L;

  private enum Mode {
    SERVER, FILE, MEM
  }

  protected static final String DATA_LOG4J_PROPERTIES = "." + File.separator + "data" + File.separator + "log4j.properties";
  private static Logger logger = LoggerFactory.getLogger(SQLDataStorage.class);
  static final String PRIVATE = "private_ws";
  static final String ARTIFACTS = "artifacts";
  static final String PROPS = "properties";
  static final String USERS = "users";
  static final String TOOLS = "tools";
  static final String VERMSG = "version_messages";
  private static final Mode MODE = Mode.MEM;
  private static final String CREATE_STMNT;

  private static final ObjectArrayRepresentationUtils objectUtils = new ObjectArrayRepresentationUtils();

  static {
    // if (MEMORY_ONLY) {
    CREATE_STMNT = "CREATE TABLE ";
    // } else {
    // CREATE_STMNT = "CREATE CACHED TABLE ";
    // }
  }

  public static void main(final String[] args) throws Exception {
    final SQLDataStorage dataStorage = new SQLDataStorage();
    bindDataStorageInstance(dataStorage);
    final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String input = "";
    while (!"shutdown".equalsIgnoreCase(input)) {
      input = reader.readLine();
    }
    PreparedStatementRepository.shutdown.execute();
    System.exit(0);
  }
  

  protected transient Connection conn;
  protected PasswordEncoder passwordEncoder;

  /**
   * constructor
   */
  public SQLDataStorage() {
    super();
    // java -cp lib/hsqldb.jar org.hsqldb.Server -database.0 file:data/db
    // -dbname.0 xdb
    establishDatabaseConnection();
    this.init();
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  private void establishDatabaseConnection() {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
      switch (MODE) {
        case SERVER:
          this.conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
          break;
        case FILE:
          this.conn = DriverManager.getConnection("jdbc:hsqldb:file:data/db", "sa", "");
          break;
        case MEM:
          this.conn = DriverManager.getConnection("jdbc:hsqldb:mem:xdb", "sa", "");
          break;
      }

      // Class.forName("org.postgresql.Driver");
      // this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/designspace", "ds", "ds");
    } catch (final SQLException e) {
      logger.debug("", e);
    } catch (final ClassNotFoundException e) {
      logger.debug("", e);
    }
  }

  @Override
  public void init() {
    try {
      if (this.conn.isClosed()) {
        establishDatabaseConnection();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
    // @Override
    // public void run() {
    // SQLthis.shutdown();
    // }
    // }));
    PreparedStatement c_artifacts = null;
    try {
      c_artifacts = this.conn.prepareStatement(CREATE_STMNT + ARTIFACTS
          + " (id BIGINT NOT NULL, version BIGINT NOT NULL,owner BIGINT,tool BIGINT, type BIGINT, container BIGINT, alive BOOLEAN,Primary Key (id,version))");
      c_artifacts.execute();

    } catch (final SQLException e) {
      logger.debug("TABLE {} already exists", ARTIFACTS);
    } finally {
      this.closePS(c_artifacts);
    }

    PreparedStatement c_artifacts_scndIdx = null;
    try {
      c_artifacts_scndIdx = this.conn.prepareStatement("CREATE INDEX AIndex ON artifact (version,id)");
      c_artifacts_scndIdx.execute();

    } catch (final SQLException e) {
      logger.debug("Error creating second artifact index");
    } finally {
      this.closePS(c_artifacts_scndIdx);
    }

    PreparedStatement c_artifacts_thirdIdx = null;
    try {
      c_artifacts_thirdIdx = this.conn.prepareStatement("CREATE INDEX AIndex ON artifact (version,id,type)");
      c_artifacts_thirdIdx.execute();

    } catch (final SQLException e) {
      logger.debug("Error creating third artifact index");
    } finally {
      this.closePS(c_artifacts_thirdIdx);
    }

    PreparedStatement c_artifacts_fourthIdx = null;
    try {
      c_artifacts_fourthIdx = this.conn.prepareStatement("CREATE INDEX AIndex ON artifact (version,id,container)");
      c_artifacts_fourthIdx.execute();

    } catch (final SQLException e) {
      logger.debug("Error creating fourth artifact index");
    } finally {
      this.closePS(c_artifacts_fourthIdx);
    }

    PreparedStatement c_artifacts_fifthIdx = null;
    try {
      c_artifacts_fifthIdx = this.conn.prepareStatement("CREATE INDEX AIndex ON artifact (version,id,type,container)");
      c_artifacts_fifthIdx.execute();

    } catch (final SQLException e) {
      logger.debug("Error creating fifth artifact index");
    } finally {
      this.closePS(c_artifacts_fifthIdx);
    }

    PreparedStatement c_artifacts_sixthIdx = null;
    try {
      c_artifacts_sixthIdx = this.conn.prepareStatement("CREATE INDEX AIndex ON artifact (version,id,container,type)");
      c_artifacts_sixthIdx.execute();

    } catch (final SQLException e) {
      logger.debug("Error creating sixth artifact index");
    } finally {
      this.closePS(c_artifacts_sixthIdx);
    }

    PreparedStatement c_properties = null;
    try {
      // c_properties = this.conn.prepareStatement(CREATE_STMNT + PROPS
      // +
      // " (artifactId BIGINT NOT NULL, version BIGINT NOT NULL, owner BIGINT, tool BIGINT, name VARCHAR(100) NOT NULL, value OTHER, alive BOOLEAN, reference BOOLEAN, "
      // + "Primary Key (version,artifactId,name)  )");
      c_properties = this.conn.prepareStatement(CREATE_STMNT + PROPS
          + " (artifactId BIGINT NOT NULL, version BIGINT NOT NULL, owner BIGINT, tool BIGINT, name VARCHAR(100) NOT NULL, value OTHER, alive BOOLEAN, reference BIGINT, "
          + "Primary Key (artifactId,version,name))");
      c_properties.execute();
    } catch (final SQLException e) {
      logger.debug("TABLE {} already exists", PROPS);
    } finally {
      this.closePS(c_properties);
    }

    PreparedStatement c_properties_scndIdx = null;
    try {
      // c_properties = this.conn.prepareStatement(CREATE_STMNT + PROPS
      // +
      // " (artifactId BIGINT NOT NULL, version BIGINT NOT NULL, owner BIGINT, tool BIGINT, name VARCHAR(100) NOT NULL, value OTHER, alive BOOLEAN, reference BOOLEAN, "
      // + "Primary Key (version,artifactId,name)  )");
      c_properties_scndIdx = this.conn.prepareStatement("CREATE INDEX PIndex ON properties (version,artifactId,name)");
      c_properties_scndIdx.execute();
    } catch (final SQLException e) {
      logger.debug("Error creating second properties index");
    } finally {
      this.closePS(c_properties_scndIdx);
    }

    PreparedStatement c_properties_thirdIdx = null;
    try {
      // c_properties = this.conn.prepareStatement(CREATE_STMNT + PROPS
      // +
      // " (artifactId BIGINT NOT NULL, version BIGINT NOT NULL, owner BIGINT, tool BIGINT, name VARCHAR(100) NOT NULL, value OTHER, alive BOOLEAN, reference BOOLEAN, "
      // + "Primary Key (version,artifactId,name)  )");
      c_properties_thirdIdx = this.conn.prepareStatement("CREATE INDEX PRIndex ON properties (name, reference, artifactId, version)");
      c_properties_thirdIdx.execute();
    } catch (final SQLException e) {
      logger.debug("Error creating second properties index");
    } finally {
      this.closePS(c_properties_thirdIdx);
    }

    PreparedStatement c_properties_fourthIdx = null;
    try {
      // c_properties = this.conn.prepareStatement(CREATE_STMNT + PROPS
      // +
      // " (artifactId BIGINT NOT NULL, version BIGINT NOT NULL, owner BIGINT, tool BIGINT, name VARCHAR(100) NOT NULL, value OTHER, alive BOOLEAN, reference BOOLEAN, "
      // + "Primary Key (version,artifactId,name)  )");
      c_properties_fourthIdx = this.conn.prepareStatement("CREATE INDEX PRIndex ON properties (reference, name, version, artifactId)");
      c_properties_fourthIdx.execute();
    } catch (final SQLException e) {
      logger.debug("Error creating fourth properties index");
    } finally {
      this.closePS(c_properties_thirdIdx);
    }

    PreparedStatement c_vermsg = null;
    try {
      c_vermsg = this.conn.prepareStatement(CREATE_STMNT + VERMSG + " (version BIGINT NOT NULL, msg VARCHAR(1000), owner BIGINT, Primary Key (version))");
      c_vermsg.execute();
    } catch (final SQLException e) {
      logger.debug("TABLE {} already exists", VERMSG);
    } finally {
      this.closePS(c_vermsg);
    }

    PreparedStatement c_private = null;
    try {
      c_private = this.conn
          .prepareStatement(CREATE_STMNT
              + PRIVATE
              + "(private_version BIGINT NOT NULL, base_version BIGINT, owner BIGINT, tool BIGINT, identifier VARCHAR(50), parent BIGINT, push VARCHAR(1), pull VARCHAR(1), Primary Key (private_version))");
      c_private.execute();
    } catch (final SQLException e) {
      logger.debug("TABLE {} already exists", PRIVATE);
    } finally {
      this.closePS(c_private);
    }

    PreparedStatement c_private_scndIdx = null;
    try {
      c_private_scndIdx = this.conn.prepareStatement("CREATE INDEX WIndex ON " + PRIVATE + " (parent,private_version)");
      c_private_scndIdx.execute();
    } catch (final SQLException e) {
      logger.debug("Error creating second properties index");
    } finally {
      this.closePS(c_properties_scndIdx);
    }
    
    PreparedStatement c_users = null;
    try {
      c_users = this.conn.prepareStatement(CREATE_STMNT + USERS + "(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,NAME VARCHAR(500),LOGIN VARCHAR(100) UNIQUE, PASSWORD VARCHAR(1000), OWNER BIGINT, PRIMARY KEY(ID))");
      c_users.execute();
    } catch(final SQLException e) {
      logger.debug("TABLE {} already exists", USERS);
    } finally {
      this.closePS(c_users);
    }
    

    try {
      PreparedStatementRepository.prepareStatements(this.conn);
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    try {
      super.init();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.implementation.AbstractDataStorage#getHeadVersionNumber()
   */
  @Override
  public long getHeadVersionNumber() {
    logger.debug("getHeadVersionNumber");
    if (this.versionGenerator != null) {
      logger.debug("getHeadVersionNumber => {}", this.versionGenerator.get());
      return this.versionGenerator.get();
    }
    long headVersion = FIRST_VERSION;
    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      // ps = this.conn.prepareStatement("SELECT MAX(version) FROM " + ARTIFACTS);
      ps = PreparedStatementRepository.getMaxArtifactVersion2;
      synchronized (ps) {
        result = ps.executeQuery();
      }
      if (result.next()) {
        headVersion = Math.max(headVersion, result.getLong(1));
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(result);
    }
    try {
      // ps = this.conn.prepareStatement("SELECT MAX(version) FROM " + PROPS);
      ps = PreparedStatementRepository.getMaxPropertyVersion;
      synchronized (ps) {
        result = ps.executeQuery();
      }
      if (result.next()) {
        headVersion = Math.max(headVersion, result.getLong(1));
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(result);
    }
    logger.debug("getHeadVersionNumber => {}", headVersion);
    return headVersion;
  }

  public void shutdown() {
    logger.debug("shutdown");
    try {
      this.conn.prepareCall("SHUTDOWN").execute();
    } catch (final SQLException e) {
      logger.debug("", e);
    }
  }

  @Override
  protected long getRealArtifactVersionNumber(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getRealArtifactVersionNumber(version={}, id={})", version, id);
    ResultSet result = null;
    PreparedStatement ps = null;
    if (version < 0) {
      try {
        // ps = this.conn.prepareStatement("SELECT COUNT(*) FROM " + ARTIFACTS + " WHERE version = ? AND id = ?");
        ps = PreparedStatementRepository.getArtifactCountByID;
        synchronized (ps) {
          ps.setLong(1, version);
          ps.setLong(2, id);
          result = ps.executeQuery();
        }
        if (result.next()) {
          final long count = result.getLong(1);
          if (count > 0) {
            logger.debug("getRealArtifactVersionNumber(version={}, id={}) => {}", new Object[] { version, id, version });
            return version;
          }
        }
      } catch (final SQLException e) {
        logger.error("", e);
      } finally {
        this.closeRS(result);

      }
      return this.getRealArtifactVersionNumber(this.getWorkspaceBaseVersionNumber(version), id);
    } else {
      try {
        // ps = this.conn.prepareStatement("SELECT MAX(version) FROM " + ARTIFACTS +
        // " WHERE version <= ? AND version > 0 AND id = ?");
        ps = PreparedStatementRepository.getMaxArtifactVersion;
        synchronized (ps) {
          ps.setLong(1, 0);
          ps.setLong(2, version);
          ps.setLong(3, id);
          result = ps.executeQuery();
        }
        if (result.next()) {
          final long realVersion = result.getLong(1);
          if (realVersion > 0) {
            logger.debug("getRealArtifactVersionNumber(version={}, id={}) => {}", new Object[] { version, id, realVersion });
            return realVersion;
          }
        }
      } catch (final SQLException e) {
        logger.error("", e);
      } finally {
        this.closeRS(result);

      }
    }
    return VOID_VERSION;
  }

  protected void closeRS(final ResultSet result) {
    if (result != null) {
      try {
        result.close();
      } catch (final SQLException e) {
        logger.error("", e);
      }
    }
  }

  @Override
  protected long getRealPropertyVersionNumber(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    logger.debug("getRealPropertyVersionNumber(version={}, artifactId={}, propertyName={})", new Object[] { version, artifactId, propertyName });
    // if (!this.existsArtifact(version, artifactId)) {
    // if (lastVersion == version) {
    // throw new ArtifactDoesNotExistException(version, artifactId);
    // }
    // // throw new PropertyDoesNotExistException(lastVersion, artifactId, propertyName);
    // }
    // if (!this.isArtifactAlive(version, artifactId)) {
    // if (lastVersion == version) {
    // throw new ArtifactDeadException(version, artifactId);
    // }
    // }
    // artifactId BIGINT, version BIGINT, name char(100), value BINARY,
    // alive BOOLEAN, reference BOOLEAN )
    ResultSet result = null;
    PreparedStatement ps = null;
    if (version < 0) {
      try {
        // ps = this.conn.prepareStatement("SELECT COUNT(*) FROM " + PROPS +
        // " WHERE version = ? AND artifactId = ? AND name = ?");
        ps = PreparedStatementRepository.getPropertyCountByIDAndName;
        synchronized (ps) {
          ps.setLong(1, version);
          ps.setLong(2, artifactId);
          ps.setString(3, propertyName);
          result = ps.executeQuery();
        }
        if (result.next()) {
          final long count = result.getLong(1);
          if (count > 0) {
            logger.debug("getRealPropertyVersionNumber(version={}, artifactId={}, propertyName={}) => {}", new Object[] { version, artifactId, propertyName, version });
            return version;
          }
        }
      } catch (final SQLException e) {
        logger.debug("", e);
      } finally {
        this.closeRS(result);
      }
      return this.getRealPropertyVersionNumber(this.getWorkspaceBaseVersionNumber(version), artifactId, propertyName);
    } else {
      try {
        // ps = this.conn.prepareStatement("SELECT MAX(version) FROM " + PROPS +
        // " WHERE version <= ? AND version > 0 AND artifactId = ? AND name = ?");
        ps = PreparedStatementRepository.getMaxPropertyVersionByIDAndName;
        synchronized (ps) {
          ps.setLong(1, version);
          ps.setLong(2, 0);
          ps.setLong(3, artifactId);
          ps.setString(4, propertyName);
          result = ps.executeQuery();
        }
        if (result.next()) {
          final long realVersion = result.getLong(1);
          if (realVersion > 0) {
            logger.debug("getRealPropertyVersionNumber(version={}, artifactId={}, propertyName={}) => {}", new Object[] { version, artifactId, propertyName, realVersion });
            return realVersion;
          }
        }
      } catch (final SQLException e) {
        logger.debug("", e);
      } finally {
        this.closeRS(result);

      }
    }
    return VOID_VERSION;
  }

  protected void closePS(final PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
      } catch (final SQLException e) {
        logger.error("", e);
      }
    }
  }

  @Override
  protected Collection<Object[]> getProperties(final long version, final Long minVersion, final long id, final boolean alive) throws ArtifactDoesNotExistException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    final List<Object> parameters = new ArrayList<Object>();
    final Collection<Object[]> properties = new ArrayList<>();
    if (!this.existsArtifact(version, id)) {
      throw new ArtifactDoesNotExistException(version, id);
    }

    try {
      ps = PreparedStatementRepository.getPropertyNamesAndValuesSQLStatement(this, parameters, version, minVersion, id, alive);
      synchronized (ps) {
        for (int i = 0; i < parameters.size(); i++) {
          ps.setObject(1 + i, parameters.get(i));
        }
        rs = ps.executeQuery();
      }
      // artifactId, name, value, alive, reference, version
      while (rs.next()) {
        Object[] result = new Object[8];
        for (int i = 0; i < result.length; i++) {
          result[i] = rs.getObject(i + 1);
        }
        properties.add(result);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);
    }
    return properties;
  }

  @Override
  protected Set<String> getPropertyNames(final long version, final Long minVersion, final long id) throws ArtifactDoesNotExistException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    final Set<String> result = new HashSet<>();
    final List<Object> parameters = new ArrayList<Object>();

    if (!this.existsArtifact(version, id)) {
      throw new ArtifactDoesNotExistException(version, id);
    }

    try {
      ps = PreparedStatementRepository.getPropertyNamesAndValuesSQLStatement(this, parameters, version, minVersion, id, null);
      synchronized (ps) {
        for (int i = 0; i < parameters.size(); i++) {
          ps.setObject(1 + i, parameters.get(i));
        }
        rs = ps.executeQuery();
      }
      while (rs.next()) {
        result.add(rs.getString(Columns.PROPERTY_NAME.getSqlIndex()));
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);
    }
    return result;
  }

  @Override
  protected Set<String> getPropertyNames(final long version, final Long minVersion, final long id, final boolean alive) throws ArtifactDoesNotExistException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    final Set<String> result = new HashSet<>();
    final List<Object> parameters = new ArrayList<Object>();

    if (!this.existsArtifact(version, id)) {
      throw new ArtifactDoesNotExistException(version, id);
    }

    try {
      ps = PreparedStatementRepository.getPropertyNamesAndValuesSQLStatement(this, parameters, version, minVersion, id, alive);
      synchronized (ps) {
        for (int i = 0; i < parameters.size(); i++) {
          ps.setObject(1 + i, parameters.get(i));
        }
        rs = ps.executeQuery();
      }
      while (rs.next()) {
        result.add(rs.getString(Columns.PROPERTY_NAME.getSqlIndex()));
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getWorkspaces()
   */
  @Override
  public List<Long> getWorkspaces() {
    logger.debug("getWorkspaces()");

    final List<Long> result = new ArrayList<Long>();
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      // ps = this.conn.prepareStatement("SELECT private_version FROM " + PRIVATE);
      ps = PreparedStatementRepository.getWorkspaces;
      synchronized (ps) {
        rs = ps.executeQuery();
        while (rs.next()) {
          result.add(rs.getLong(1));
        }
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getWorkspaceRepresentation(long)
   */
  @Override
  public Object[] getWorkspaceRepresentation(final long privateVersion) throws WorkspaceExpiredException {
    logger.debug("getWorkspaceRepresentation(privateVersion={})", new Object[] { privateVersion });
    PreparedStatement ps = null;
    ResultSet rs = null;
    Object[] result = null;

    try {
      // ps = this.conn.prepareStatement("SELECT * FROM " + PRIVATE + " WHERE private_version = ?");
      ps = PreparedStatementRepository.getWorkspaceRepresentation;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        // private_version BIGINT, base_version BIGINT, owner BIGINT, tool BIGINT, identifier char(50)
        result = new Object[8];
        for (int i = 0; i < result.length; i++) {
          result[i] = rs.getObject(i + 1);
        }

        result[Columns.WORKSPACE_PUSH.getIndex()] = ((String) result[Columns.WORKSPACE_PUSH.getIndex()]).compareTo("I") == 0 ? PropagationType.instant : PropagationType.triggered;
        result[Columns.WORKSPACE_PULL.getIndex()] = ((String) result[Columns.WORKSPACE_PULL.getIndex()]).compareTo("I") == 0 ? PropagationType.instant : PropagationType.triggered;
        logger.debug("getWorkspaceRepresentation(...) => (privateVersion={}, baseVersion={}, owner={}, tool={}, identifier={})", result);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    if (result == null) {
      throw new WorkspaceExpiredException(privateVersion);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getPrivateVersionNumber(long, long, java.lang.String)
   */
  @Override
  public Long getWorkspaceVersionNumber(final long owner, final long tool, final String identifier) {
    logger.debug("getWorkspaceVersionNumber(owner={}, tool={}, identifier={})", new Object[] { owner, tool, identifier });
    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      // ps = this.conn.prepareStatement("SELECT private_version FROM " + PRIVATE +
      // " WHERE owner = ? AND identifier = ? AND tool = ?");
      ps = PreparedStatementRepository.getPrivateVersion;
      synchronized (ps) {
        ps.setLong(1, owner);
        ps.setString(2, identifier);
        ps.setLong(3, tool);
        result = ps.executeQuery();
      }
      if (result.next()) {
        logger.debug("getPrivateVersionNumber(owner={}, identifier={}, tool={}) => {}", new Object[] { owner, identifier, tool, result.getLong(1) });
        return result.getLong(1);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    }
    logger.debug("getWorkspaceVersionNumber(owner={}, identifier={}) => {}", new Object[] { owner, identifier, null });
    return null;
  }

  @Override
  public void updateWorkspaceBaseVersionNumber(final long privateVersion, final long newBaseVersion) throws WorkspaceExpiredException {
    logger.debug("setWorkspaceVersionNumber(privateVersion={}, newBaseVersion={})", new Object[] { privateVersion, newBaseVersion });
    PreparedStatement ps = null;
    try {
      // ps = this.conn.prepareStatement("UPDATE " + PRIVATE + " SET base_version = ? WHERE private_version = ?");
      ps = PreparedStatementRepository.updatePrivateWorkspace;
      synchronized (ps) {
        ps.setLong(1, newBaseVersion);
        ps.setLong(2, privateVersion);
        ps.executeUpdate();
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getPrivateHeadVersionNumber()
   */
  @Override
  public long getPrivateHeadVersionNumber() {
    logger.debug("getPrivateHeadVersionNumber()");
    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      // ps = this.conn.prepareStatement("SELECT MIN(private_version) FROM " + PRIVATE);
      ps = PreparedStatementRepository.getMinPrivateVersion;
      synchronized (ps) {
        result = ps.executeQuery();
      }
      if (result.next()) {
        logger.debug("getPrivateHeadVersionNumber() => {}", result.getLong(1));
        return result.getLong(1);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(result);
    }
    logger.debug("getPrivateHeadVersionNumber() => {}", 0);
    return 0;
  }

  @Override
  protected List<Long> getArtifactCompleteHistoryVersionNumbers(final long id) {
    final List<Long> versionNumbers = this.getArtifactHistoryVersionNumbers(id);
    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      // ps = this.conn.prepareStatement("SELECT version FROM " + ARTIFACTS + " WHERE id = ? AND version < 0");
      ps = PreparedStatementRepository.getArtifactVersionLT;
      synchronized (ps) {
        ps.setLong(1, id);
        result = ps.executeQuery();
      }
      while (result.next()) {
        versionNumbers.add(result.getLong(1));
      }
      return versionNumbers;
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(result);
    }
    return versionNumbers;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getArtifactHistoryVersionNumbers(long)
   */
  @Override
  public List<Long> getArtifactHistoryVersionNumbers(final long id) {
    logger.debug("getArtifactHistoryVersionNumbers(id={})", id);
    final List<Long> list = new ArrayList<Long>();

    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      ps = PreparedStatementRepository.getArtifactVersionGT;
      synchronized (ps) {
        ps.setLong(1, id);
        result = ps.executeQuery();
      }
      while (result.next()) {
        list.add(result.getLong(1));
      }
      return list;
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(result);
    }
    logger.debug("getArtifactHistoryVersionNumbers(id={}) => null", id);
    return null;
  }

  @Override
  public long getArtifactMaxVersionNumber(final long id, final long version, final long previousVersion) throws ArtifactDoesNotExistException {
    logger.debug("getArtifactHistoryVersionNumbers(id={})", id);
    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      // ps = this.conn.prepareStatement("SELECT MAX(version) FROM " + ARTIFACTS +
      // " WHERE id = ? AND version > ? AND version <= ?");
      ps = PreparedStatementRepository.getMaxArtifactVersion;
      synchronized (ps) {
        ps.setLong(1, previousVersion);
        ps.setLong(2, version);
        ps.setLong(3, id);
        result = ps.executeQuery();
      }
      if (result.next()) {
        return result.getLong(1);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(result);
    }
    logger.debug("getArtifactHistoryVersionNumbers(id={}) => null", id);
    return -1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getPropertyHistoryVersionNumbers(long, java.lang.String)
   */
  @Override
  public List<Long> getPropertyHistoryVersionNumbers(final long id, final String property) {
    logger.debug("getPropertyHistoryVersionNumbers(id={}, property={})", new Object[] { id, property });
    final List<Long> list = new ArrayList<Long>();

    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      // ps = this.conn.prepareStatement("SELECT version FROM " + PROPS +
      // " WHERE artifactId = ? AND name = ? AND version > 0");
      ps = PreparedStatementRepository.getPropertyHistoryVersionNumbersGT;
      synchronized (ps) {
        ps.setLong(1, id);
        ps.setString(2, property);
        result = ps.executeQuery();
      }
      while (result.next()) {
        list.add(result.getLong(1));
      }

      return list;
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(result);
    }
    logger.debug("getPropertyHistoryVersionNumbers(id={}, property={}) => null", new Object[] { id, property });
    return null;
  }

  @Override
  public long getPropertyMaxHistoryVersionNumber(final long id, final String property, final long version, final long previousVersion) {
    ResultSet result = null;
    PreparedStatement ps = null;
    try {
      // ps = this.conn.prepareStatement("SELECT MAX(version) FROM
      // " + PROPS + " WHERE version <= ? AND version > ? AND artifactId = ? AND name = ?");
      ps = PreparedStatementRepository.getMaxPropertyVersionByIDAndName;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setLong(2, previousVersion);
        ps.setLong(3, id);
        ps.setString(4, property);
        result = ps.executeQuery();
      }
      if (result.next()) {
        final long realVersion = result.getLong(1);
        if (realVersion > 0) {
          logger.debug("getPropertyMaxHistoryVersionNumber(id={}, property={}, version={}, previousVersion={}) => {}", new Object[] { id, property, version, previousVersion, realVersion });
          return realVersion;
        }
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(result);

    }
    return -1;
  }

  @Override
  protected boolean existsPropertyInVersion(final long version, final long artifactId, final String propertyName) {
    ResultSet result = null;
    PreparedStatement ps = null;
    boolean exists = false;
    try {
      // ps = this.conn.prepareStatement("SELECT COUNT(*) FROM " + PROPS +
      // " WHERE version = ? AND artifactId = ? AND name = ?");
      ps = PreparedStatementRepository.getPropertyCountByIDAndName;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setLong(2, artifactId);
        ps.setString(3, propertyName);
        result = ps.executeQuery();
      }
      if (result.next()) {
        final long count = result.getLong(1);
        if (count > 0) {
          exists = true;
        }
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(result);

    }
    return exists;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.implementation.AbstractDataStorage#storeProperties( long, long, long, java.lang.String, java.lang.Object, long)
   */
  @Override
  protected void storeProperty(final long version, final long owner, final long tool, final long artifactId, final String propertyName, Object value, final boolean reference, final boolean alive)
      throws PropertyDeadException {
    // artifactId BIGINT, version BIGINT, owner BIGINT, tool, name
    // char(100), value BINARY, alive BOOLEAN, reference
    // BOOLEAN
    logger.debug("storeProperties(version={}, owner={}, tool={}, artifactId={}, propertyName={}, value={}, reference={}, alive={})", new Object[] { version, owner, tool, artifactId, propertyName,
        value, reference, alive });
    // long start = System.nanoTime();
    Long referenceID = null;
    if (reference) {
      referenceID = (Long) value;
      value = null;
    }
    PreparedStatement ps = PreparedStatementRepository.mergeProperty;

    try {
      synchronized (ps) {
        ps.setLong(1, artifactId);
        ps.setLong(2, version);
        ps.setString(3, propertyName);
        // synchronized (ps) {
        ps.setLong(4, owner);
        if (reference) {
          ps.setNull(5, Types.OTHER);
          ps.setLong(6, referenceID);
        } else {
          ps.setObject(5, value);
          ps.setNull(6, Types.BIGINT);
        }
        ps.setBoolean(7, alive);
        ps.setLong(8, artifactId);
        ps.setLong(9, version);
        ps.setLong(10, owner);
        ps.setLong(11, tool);
        ps.setString(12, propertyName);
        if (reference) {
          ps.setNull(13, Types.OTHER);
          ps.setLong(15, referenceID);
        } else {
          ps.setObject(13, value);
          ps.setNull(15, Types.BIGINT);
        }
        ps.setBoolean(14, alive);
        ps.execute();
      }
    } catch (final SQLException e1) {
      logger.debug("", e1);
    }
  }

  @Override
  protected boolean existsArtifactInVersion(final long version, final long id) {
    ResultSet result = null;
    PreparedStatement ps = null;
    boolean exists = false;
    try {
      // ps = this.conn.prepareStatement("SELECT COUNT(*) FROM " + ARTIFACTS + " WHERE version = ? AND id = ?");
      ps = PreparedStatementRepository.getArtifactCountByID;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setLong(2, id);
        result = ps.executeQuery();
      }
      if (result.next()) {
        final long count = result.getLong(1);
        if (count > 0) {
          exists = true;
        }
      }
    } catch (final SQLException e) {
      logger.error("", e);
    } finally {
      this.closeRS(result);

    }
    return exists;
  }

  @Override
  protected void storeArtifact(final long version, final long owner, final long tool, final long id, final Long type, final Long pkg, final boolean alive) throws ArtifactDeadException {
    logger.debug("storeArtifact(version={}, owner={}, tool={}, id={}, type={}, container={}, alive={})", new Object[] { version, owner, tool, id, type, pkg, alive });
    try {
      PreparedStatement ps = PreparedStatementRepository.mergeArtifact;
      synchronized (ps) {
        ps.setLong(1, id);
        ps.setLong(2, version);
        ps.setLong(3, owner);
        if (type == null) {
          ps.setLong(4, ROOT_TYPE_ID);// java.sql.Types.BIGINT);
        } else {
          ps.setLong(4, type);
        }
        if (pkg == null || pkg == NO_CONTAINER_ID) {
          ps.setNull(5, java.sql.Types.BIGINT);
        } else {
          ps.setLong(5, pkg);
        }
        ps.setBoolean(6, alive);
        ps.setLong(7, id);
        ps.setLong(8, version);
        ps.setLong(9, owner);
        ps.setLong(10, tool);
        if (type == null) {
          ps.setLong(11, ROOT_TYPE_ID);// java.sql.Types.BIGINT);
        } else {
          ps.setLong(11, type);
        }
        if (pkg == null || pkg == NO_CONTAINER_ID) {
          ps.setNull(12, java.sql.Types.BIGINT);
        } else {
          ps.setLong(12, pkg);
        }
        ps.setBoolean(13, alive);
        ps.execute();
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void storeArtifact(final Set<Long> versions, final long owner, final long tool, final long id, final Long type, final Long pkg, final boolean alive) throws ArtifactDeadException {
    logger.debug("storeArtifact(versions={}, owner={}, tool={}, id={}, type={}, container={}, alive={})", new Object[] { versions, owner, tool, id, type, pkg, alive });
    PreparedStatement ps = PreparedStatementRepository.mergeArtifact;
    try {
      synchronized (ps) {
        this.conn.setAutoCommit(false);
        int updates = 0;
        for (Long version : versions) {
          try {
            ps.setLong(1, id);
            ps.setLong(2, version);
            ps.setLong(3, owner);
            if (type == null) {
              ps.setLong(4, ROOT_TYPE_ID);// java.sql.Types.BIGINT);
            } else {
              ps.setLong(4, type);
            }
            if (pkg == null || pkg == NO_CONTAINER_ID) {
              ps.setNull(5, java.sql.Types.BIGINT);
            } else {
              ps.setLong(5, pkg);
            }
            ps.setBoolean(6, alive);
            ps.setLong(7, id);
            ps.setLong(8, version);
            ps.setLong(9, owner);
            ps.setLong(10, tool);
            if (type == null) {
              ps.setLong(11, ROOT_TYPE_ID);// java.sql.Types.BIGINT);
            } else {
              ps.setLong(11, type);
            }
            if (pkg == null || pkg == NO_CONTAINER_ID) {
              ps.setNull(12, java.sql.Types.BIGINT);
            } else {
              ps.setLong(12, pkg);
            }
            ps.setBoolean(13, alive);
            ps.addBatch();
            updates++;

          } catch (final SQLException e) {
            e.printStackTrace();
          } finally {

          }
        }

        if (updates > 0) {
          ps.executeBatch();
          this.conn.commit();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        this.conn.setAutoCommit(true);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  protected void storeProperty(final Set<Long> versions, final long owner, final long tool, final long artifactId, final String propertyName, Object value, final boolean reference, final boolean alive)
      throws PropertyDeadException {
    try {
      this.conn.setAutoCommit(false);
      PreparedStatement ps = PreparedStatementRepository.mergeProperty;
      synchronized (ps) {
        int updates = 0;
        for (Long version : versions) {
          Long referenceID = null;
          if (reference) {
            referenceID = (Long) value;
            value = null;
          }
          try {
            ps.setLong(1, artifactId);
            ps.setLong(2, version);
            ps.setString(3, propertyName);
            ps.setLong(4, owner);
            if (reference) {
              ps.setNull(5, Types.OTHER);
              ps.setLong(6, referenceID);
            } else {
              ps.setObject(5, value);
              ps.setNull(6, Types.BIGINT);
            }
            ps.setBoolean(7, alive);

            ps.setLong(8, artifactId);
            ps.setLong(9, version);
            ps.setLong(10, owner);
            ps.setLong(11, tool);
            ps.setString(12, propertyName);
            if (reference) {
              ps.setNull(13, Types.OTHER);
              ps.setLong(15, referenceID);
            } else {
              ps.setObject(13, value);
              ps.setNull(15, Types.BIGINT);
            }
            ps.setBoolean(14, alive);
            ps.addBatch();
            updates++;
          } catch (final SQLException e1) {
            logger.debug("", e1);
          }
        }

        if (updates > 0) {
          ps.executeBatch();
          conn.commit();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  protected void storePropertyValues(final Set<Long> versions, final long owner, final long tool, final long artifactId, Map<String, Object> properties, final boolean alive)
      throws PropertyDeadException {
    int updates = 0;
    try {
      conn.setAutoCommit(false);
      PreparedStatement ps = PreparedStatementRepository.mergeProperty;
      synchronized (ps) {
        for (final Entry<String, Object> entry : properties.entrySet()) {
          String propertyName = entry.getKey();
          Object value = entry.getValue();
          for (Long version : versions) {
            try {
              ps.setLong(1, artifactId);
              ps.setLong(2, version);
              ps.setString(3, propertyName);
              ps.setLong(4, owner);
              ps.setObject(5, value);
              ps.setNull(6, Types.BIGINT);
              ps.setBoolean(7, alive);

              ps.setLong(8, artifactId);
              ps.setLong(9, version);
              ps.setLong(10, owner);
              ps.setLong(11, tool);
              ps.setString(12, propertyName);
              ps.setObject(13, value);
              ps.setNull(15, Types.BIGINT);
              ps.setBoolean(14, alive);
              ps.addBatch();
              updates++;
            } catch (final SQLException e1) {
              logger.debug("", e1);
            }
          }
        }

        if (updates > 0) {
          ps.executeBatch();
          conn.commit();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  protected void storePropertyReferences(final Set<Long> versions, final long owner, final long tool, final long artifactId, Map<String, Long> properties, final boolean alive)
      throws PropertyDeadException {
    int updates = 0;
    try {
      this.conn.setAutoCommit(false);
      PreparedStatement ps = PreparedStatementRepository.mergeProperty;
      synchronized (ps) {
        for (final Entry<String, Long> entry : properties.entrySet()) {
          String propertyName = entry.getKey();
          Long referenceID = entry.getValue();
          for (Long version : versions) {
            try {
              ps.setLong(1, artifactId);
              ps.setLong(2, version);
              ps.setString(3, propertyName);
              ps.setLong(4, owner);
              ps.setNull(5, Types.OTHER);
              ps.setLong(6, referenceID);
              ps.setBoolean(7, alive);

              ps.setLong(8, artifactId);
              ps.setLong(9, version);
              ps.setLong(10, owner);
              ps.setLong(11, tool);
              ps.setString(12, propertyName);
              ps.setNull(13, Types.OTHER);
              ps.setLong(15, referenceID);

              ps.setBoolean(14, alive);
              ps.addBatch();
              updates++;
            } catch (final SQLException e1) {
              logger.debug("", e1);
            }
          }
        }

        if (updates > 0) {
          ps.executeBatch();
          conn.commit();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#commitArtifact(long, long, java.lang.String)
   */
  @Override
  protected long publishArtifact(final long privateVersion, final long id, final String message) throws ArtifactDoesNotExistException, ArtifactConflictException, WorkspaceEmptyException {
    logger.debug("commitArtifact(id={}, message={})", new Object[] { id, message });
    // TODO add timestamp

    if (privateVersion > 0) {
      throw new IllegalArgumentException("version is not private");
    }

    if (this.isWorkspaceEmpty(privateVersion, id)) {
      throw new WorkspaceEmptyException(id);
    }

    final Object[] representation = this.getArtifactRepresentation(privateVersion, id);

    final Set<Long> conflictingArtifacts = this.getArtifactConflicts(privateVersion, this.getHeadVersionNumber());
    if (conflictingArtifacts.contains(id)) {
      throw new ArtifactConflictException(privateVersion);
    }

    Long type = (Long) representation[Columns.ARTIFACT_TYPE.getIndex()];
    Long container = (Long) representation[Columns.ARTIFACT_CONTAINER.getIndex()];
    if (type != null && this.existsOnlyInPrivateVersion(privateVersion, type)) {
      throw new ArtifactNotCommitableException(privateVersion, id);
    }
    if (container != null && this.existsOnlyInPrivateVersion(privateVersion, container)) {
      throw new ArtifactNotCommitableException(privateVersion, id);
    }

    for (Object[] prop : this.getVersionPropertiesOfArtifact(privateVersion, id)) {
      Long reference = (Long) prop[Columns.PROPERTY_REFERENCE.getIndex()];
      final boolean refOnlyInPrivateVersion = reference != null ? this.existsOnlyInPrivateVersion(privateVersion, reference) : false;
      if (this.existsOnlyInPrivateVersion(privateVersion, id) || refOnlyInPrivateVersion) {
        throw new ArtifactNotCommitableException(privateVersion, id);
      }
    }
    final long version = this.versionGenerator.incrementAndGet();
    PreparedStatement ps = null;
    final ResultSet result = null;

    try {

      ps = PreparedStatementRepository.updateArtifactVersion;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setLong(2, privateVersion);
        ps.setLong(3, id);
        ps.executeUpdate();
      }

      synchronized (ps) {
        ps = PreparedStatementRepository.updateArtifactPropertyVersion;
        ps.setLong(1, version);
        ps.setLong(2, privateVersion);
        ps.setLong(3, id);
        ps.executeUpdate();
      }

      synchronized (ps) {
        ps = PreparedStatementRepository.updatePrivateWorkspace;
        ps.setLong(1, version);
        ps.setLong(2, privateVersion);
        ps.executeUpdate();
      }
      final Long owner = (Long) representation[Columns.OWNER.getIndex()];

      // version BIGINT, msg char(1000), identifiere char(50), Primary Key
      // (version)
      ps = PreparedStatementRepository.insertVersionMessage;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setString(2, message);
        ps.setLong(3, owner);
        ps.execute();
      }
      logger.debug("commitArtifact(id={}, message={}) => {}", new Object[] { id, message, version });
      return version;
    } catch (final SQLException e) {
      this.restorePrivateVersion(version, privateVersion);
      logger.debug("", e);
    } finally {
      this.closeRS(result);
    }
    logger.debug("commitArtifact(id={}, message={}) => {}", new Object[] { id, message, 0 });
    return VOID_VERSION;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#commitProperty(long, long, java.lang.String, java.lang.String)
   */
  @Override
  protected long publishProperty(final long privateVersion, final long id, final String property, final String message) throws ArtifactDoesNotExistException, PropertyDoesNotExistException,
      PropertyConflictException, WorkspaceEmptyException, PropertyNotCommitableException {
    logger.debug("commitProperty(privateVersion={}, id={}, property={}, message={})", new Object[] { privateVersion, id, property, message });
    // TODO add timestamp

    if (privateVersion > 0) {
      throw new IllegalArgumentException("version is not private");
    }

    if (!this.existsArtifact(privateVersion, id)) {
      throw new ArtifactDoesNotExistException(privateVersion, id);
    }

    if (this.isWorkspaceEmpty(privateVersion, id, property)) {
      throw new WorkspaceEmptyException(privateVersion);
    }

    final Map<Long, Set<String>> conflictingProperties = this.getPropertyConflicts(privateVersion);
    if (conflictingProperties.containsKey(id) && conflictingProperties.get(id).contains(property)) {
      throw new PropertyConflictException(privateVersion);
    }

    final Object value = this.getPropertyValue(privateVersion, id, property);
    final boolean refOnlyInPrivateVersion = this.isPropertyReference(privateVersion, id, property) ? this.existsOnlyInPrivateVersion(privateVersion, (Long) value) : false;
    if (this.existsOnlyInPrivateVersion(privateVersion, id) || refOnlyInPrivateVersion) {
      throw new PropertyNotCommitableException(id, property);
    }

    final long version = this.versionGenerator.incrementAndGet();

    PreparedStatement ps = null;
    final ResultSet result = null;

    try {

      ps = PreparedStatementRepository.updatePropertyVersion;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setLong(2, privateVersion);
        ps.setLong(3, id);
        ps.setString(4, property);
        ps.executeUpdate();
      }
      synchronized (ps) {
        ps = PreparedStatementRepository.updatePrivateWorkspace;
        ps.setLong(1, version);
        ps.setLong(2, privateVersion);
        ps.executeUpdate();
      }
      final long owner = this.getPropertyOwner(version, id, property);

      // version BIGINT, msg char(1000), owner BIGINT, Primary Key
      // (version)
      ps = PreparedStatementRepository.insertVersionMessage;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setString(2, message);
        ps.setLong(3, owner);
        ps.execute();
      }
      logger.debug("commitProperty(privateVersion={}, id={}, property={}, message={}) => {}", new Object[] { privateVersion, id, property, message, version });
      return version;
    } catch (final SQLException e) {
      this.restorePrivateVersion(version, privateVersion);
      logger.debug("", e);
    } finally {
      this.closeRS(result);
    }
    logger.debug("commitProperty(privateVersion={}, id={}, property={}, message={}) => {}", new Object[] { privateVersion, id, property, message, 0 });
    return VOID_VERSION;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#deleteArtifact(long, long)
   */
  @Override
  protected void deleteArtifact(final long privateVersion, final long id) throws ArtifactDoesNotExistException, ArtifactNotDeleteableException {
    logger.debug("deleteArtifact(version={}, id={})", new Object[] { privateVersion, id });
    if (privateVersion > 0) {
      throw new IllegalArgumentException("version is not private");
    }

    PreparedStatement ps = null;
    try {
      ps = PreparedStatementRepository.deleteArtifact;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, id);
        ps.executeUpdate();
      }
      ps = PreparedStatementRepository.deletePropertiesOfArtifact;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, id);
        ps.execute();
      }

    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#deleteProperty(long, long, java.lang.String)
   */
  @Override
  protected void deleteProperty(final long privateVersion, final long id, final String property) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    logger.debug("deleteArtifact(version={}, id={}, property={})", new Object[] { privateVersion, id, property });
    if (privateVersion > 0) {
      throw new IllegalArgumentException("version is not private");
    }
    if (!this.existsArtifact(privateVersion, id)) {
      throw new ArtifactDoesNotExistException(privateVersion, id);
    }

    PreparedStatement ps = null;

    try {
      ps = PreparedStatementRepository.deleteProperty;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, id);
        ps.setString(3, property);
        final int deletes = ps.executeUpdate();
        if (deletes < 1) {
          throw new PropertyDoesNotExistException(privateVersion, id, property);
        }
      }

    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

    }
  }

  @Override
  protected List<Long> getPropertyCompleteHistoryVersionNumbers(final long id, final String property) {
    logger.debug("getPropertyHistoryVersionNumbers(id={}, property={})", new Object[] { id, property });
    final List<Long> list = this.getPropertyHistoryVersionNumbers(id, property);

    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      ps = PreparedStatementRepository.getPropertyHistoryVersionNumbersLT;
      synchronized (ps) {
        ps.setLong(1, id);
        ps.setString(2, property);
        result = ps.executeQuery();
      }
      while (result.next()) {
        list.add(result.getLong(1));
      }
      return list;
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(result);
    }
    logger.debug("getPropertyHistoryVersionNumbers(id={}, property={}) => null", new Object[] { id, property });
    return null;
  }

  protected Map<Long, Set<String>> getPropertiesByValue(final long version, final Object value, final boolean isReference) {
    logger.debug("getArtifactsByPropertyValue(version={}, value={})", new Object[] { version, value });
    PreparedStatement ps = null;
    ResultSet rs = null;
    final Map<Long, Set<String>> result = new HashMap<Long, Set<String>>();
    final List<Object> parameters = new ArrayList<Object>();
    final String valueColumn = isReference ? Columns.PROPERTY_REFERENCE.getName() : Columns.PROPERTY_VALUE.getName();
    try {
      ps = PreparedStatementRepository.getPropertiesByPropertyValueSQLStatement(this, parameters, version, null, true, isReference ? (Long) value : null);
      synchronized (ps) {
        for (int i = 0; i < parameters.size(); i++) {
          ps.setObject(1 + i, parameters.get(i));
        }
        rs = ps.executeQuery();
      }
      while (rs.next()) {
        final long artifactId = rs.getLong(Columns.ARTIFACT_ID.getSqlIndex());
        final String propName = rs.getString(Columns.PROPERTY_NAME.getSqlIndex());
        if (value.equals(rs.getObject(valueColumn))) {
          if (!result.containsKey(artifactId)) {
            result.put(artifactId, new HashSet<String>());
          }
          result.get(artifactId).add(propName);
        }
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);
    }
    return result;
  }

  @Override
  protected Map<String, Object[]> getReferencedArtifactsByName(long version, Long artifactId) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    final Map<String, Object[]> result = new HashMap<String, Object[]>();
    final List<Object> parameters = new ArrayList<Object>();
    try {
      ps = PreparedStatementRepository.getArtifactsByReference(this, parameters, version, artifactId);
      synchronized (ps) {
        for (int i = 0; i < parameters.size(); i++) {
          ps.setObject(1 + i, parameters.get(i));
        }
        rs = ps.executeQuery();
      }
      while (rs.next()) {
        Object[] rep = new Object[7];
        for (int i = 0; i < rep.length; i++) {
          rep[i] = rs.getObject(i + 1);
        }
        result.put(rs.getString(8), rep);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#commitVersion(long, java.lang.String)
   */
  @Override
  protected long publishVersion(final long privateVersion, final String message) throws VersionConflictException, WorkspaceEmptyException {
    logger.debug("commitVersion(privateVersion={}, message={})", privateVersion, message);
    // TODO add timestamp

    if (privateVersion > 0) {
      throw new IllegalArgumentException("version is not private");
    }

    if (this.isWorkspaceEmpty(privateVersion)) {
      throw new WorkspaceEmptyException(privateVersion);
    }

    final Set<Long> conflictingArtifacts = this.getArtifactConflicts(privateVersion);
    final Map<Long, Set<String>> conflictingProperties = this.getPropertyConflicts(privateVersion);
    if (!conflictingArtifacts.isEmpty() || !conflictingProperties.isEmpty()) {
      throw new VersionConflictException(privateVersion);
    }

    final long version = this.versionGenerator.incrementAndGet();

    PreparedStatement ps = null;
    final ResultSet result = null;

    try {

      ps = PreparedStatementRepository.updateArtifacts;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setLong(2, privateVersion);
        ps.executeUpdate();
      }

      ps = PreparedStatementRepository.updateProperties;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setLong(2, privateVersion);
        ps.executeUpdate();
      }

      final long owner = this.getWorkspaceOwner(privateVersion);

      // version BIGINT, msg char(1000)
      // ps = this.conn.prepareStatement("INSERT INTO " + VERMSG + " VALUES (?, ?, ?)");
      ps = PreparedStatementRepository.insertVersionMessage;
      ps.setLong(1, version);
      ps.setString(2, message);
      ps.setLong(3, owner);
      ps.execute();
      logger.debug("commitVersion(privateVersion={}, message={}) => {}", new Object[] { privateVersion, message, version });
      return version;
    } catch (final SQLException e) {
      this.restorePrivateVersion(version, privateVersion);
      logger.debug("", e);
    } finally {

      this.closeRS(result);
    }
    throw new RuntimeException("could not commit");
  }

  private void restorePrivateVersion(final long version, final long privateVersion) {
    logger.debug("deleteVersion(version={})", version);
    PreparedStatement ps = null;

    long oldVersion = this.versionGenerator.decrementAndGet();
    try {
      // UPDATE artifacts SET version = ? WHERE version = ?
      ps = PreparedStatementRepository.updateArtifacts;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, version);
        ps.execute();
      }

      // UPDATE properties SET version = ? WHERE version = ?
      ps = PreparedStatementRepository.updateProperties;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, version);
        ps.execute();
      }
      // ps = this.conn.prepareStatement("UPDATE " + PRIVATE + " SET base_version = ? WHERE private_version = ?");
      ps = PreparedStatementRepository.updatePrivateWorkspace;
      synchronized (ps) {
        ps.setLong(1, oldVersion);
        ps.setLong(2, version);
        ps.executeUpdate();
      }
      // ps = this.conn.prepareStatement("DELETE FROM " + VERMSG + " WHERE version = ?");
      ps = PreparedStatementRepository.deleteVersionMessages;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.execute();
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#isWorkspaceEmpty(long)
   */
  @Override
  public boolean isWorkspaceEmpty(final long privateVersion) throws WorkspaceExpiredException {
    logger.debug("isWorkspaceEmpty(privateVersion={})", privateVersion);
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean result = true;
    // this.getWorkspaceRepresentation(privateVersion);
    try {
      // ps = this.conn.prepareStatement("SELECT COUNT(*) FROM artifacts WHERE version=?");
      ps = PreparedStatementRepository.getArtifactCount;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        final long count = rs.getLong(1);
        result = result && (count == 0);
      }

      this.closeRS(rs);
      // ps = this.conn.prepareStatement("SELECT COUNT(*) FROM properties WHERE version=?");
      ps = PreparedStatementRepository.getPropertyCount;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        final long count = rs.getLong(1);
        result = result && (count == 0);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    logger.debug("isWorkspaceEmpty(privateVersion={}) => {}", privateVersion, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#isWorkspaceEmpty(long, long)
   */
  @Override
  public boolean isWorkspaceEmpty(final long privateVersion, final long id) throws WorkspaceExpiredException {
    logger.debug("isWorkspaceEmpty(privateVersion={})", privateVersion);
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean result = true;
    this.getWorkspaceRepresentation(privateVersion);
    try {
      // ps = this.conn.prepareStatement("SELECT COUNT(*) FROM artifacts WHERE version=? and id = ?");
      ps = PreparedStatementRepository.getArtifactCountByID;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, id);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        final long count = rs.getLong(1);
        result = result && (count == 0);
      }

      this.closeRS(rs);
      // ps = this.conn.prepareStatement("SELECT COUNT(*) FROM properties WHERE version=? and artifactid=? ");
      ps = PreparedStatementRepository.getPropertyCountByID;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, id);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        final long count = rs.getLong(1);
        result = result && (count == 0);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    logger.debug("isWorkspaceEmpty(privateVersion={}) => {}", privateVersion, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#isWorkspaceEmpty(long, long, propertyName)
   */
  @Override
  public boolean isWorkspaceEmpty(final long privateVersion, final long id, final String propertyName) throws WorkspaceExpiredException {
    logger.debug("isWorkspaceEmpty(privateVersion={})", privateVersion);
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean result = true;
    this.getWorkspaceRepresentation(privateVersion);
    try {
      // ps = this.conn.prepareStatement("SELECT COUNT(*) FROM artifacts WHERE version=? and id=?");
      ps = PreparedStatementRepository.getArtifactCountByID;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, id);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        final long count = rs.getLong(1);
        result = result && (count == 0);
      }

      this.closeRS(rs);
      // ps =this.conn.prepareStatement("SELECT COUNT(*) FROM properties WHERE version=? and artifactid=? and name=?");
      ps = PreparedStatementRepository.getPropertyCountByIDAndName;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, id);
        ps.setString(3, propertyName);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        final long count = rs.getLong(1);
        result = result && (count == 0);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    logger.debug("isWorkspaceEmpty(privateVersion={}) => {}", privateVersion, result);
    return result;
  }

  @Override
  protected void closeWorkspace(final long privateVersion) throws WorkspaceExpiredException, WorkspaceNotEmptyException {
    if (privateVersion > 0) {
      throw new IllegalArgumentException("version is not private");
    }
    // this.getWorkspaceRepresentation(privateVersion);
    if (!this.isWorkspaceEmpty(privateVersion)) {
      throw new WorkspaceNotEmptyException(privateVersion);
    }
    PreparedStatement ps = null;
    try {
      ps = PreparedStatementRepository.deletePrivateWorkspace;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.execute();
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#deleteVersion(long)
   */
  @Override
  protected void deleteVersion(final long privateVersion) {
    logger.debug("deleteVersion(version={})", privateVersion);
    PreparedStatement ps = null;
    if (privateVersion > 0) {
      throw new IllegalArgumentException("version is not private");
    }

    try {
      // ps = this.conn.prepareStatement("DELETE FROM " + ARTIFACTS + " WHERE version = ?");
      ps = PreparedStatementRepository.deleteArtifacts;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.execute();
      }
      ps = PreparedStatementRepository.deleteProperties;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.execute();
      }

    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getVersionOwner(long)
   */
  @Override
  public Long getVersionOwner(final long version) {
    logger.debug("getVersionOwner(version={})", version);
    Long result = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = PreparedStatementRepository.getOwnerFromVersionMessage;
      synchronized (ps) {
        ps.setLong(1, version);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        result = rs.getLong(1);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);

    }
    logger.debug("getVersionOwner(version={}) => {}", version, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getVersionMessage(long)
   */
  @Override
  public String getVersionMessage(final long version) {
    logger.debug("getVersionMessage(version={})", version);
    String result = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = PreparedStatementRepository.getMessageFromVersionMessage;
      synchronized (ps) {
        ps.setLong(1, version);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        result = rs.getString(1);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);

    }
    logger.debug("getVersionMessage(version={}) => {}", version, result);
    return result;
  }

  // TODO
  @Override
  protected void storePrivateWorkspace(final long privateVersion, final long owner, final long tool, final String identifier, final long baseVersion, final Long parent, final PropagationType push,
      final PropagationType pull) {
    logger.debug("storePrivateWorkspace(privateVersion={}, owner={}, identifier={}, baseVersion={})", new Object[] { privateVersion, owner, identifier, baseVersion });
    PreparedStatement ps = null;
    // private_version BIGINT, owner BIGINT, identifier char(50),
    // base_version BIGINT
    try {
      // ps = this.conn.prepareStatement("INSERT INTO " + PRIVATE + " VALUES (?,?,?,?,?)");
      ps = PreparedStatementRepository.insertPrivateWorkspace;
      synchronized (ps) {
        ps.setLong(1, privateVersion);
        ps.setLong(2, baseVersion);
        ps.setLong(3, owner);
        ps.setLong(4, tool);
        ps.setString(5, identifier);
        if (parent == null) {
          ps.setNull(6, Types.BIGINT);
        } else {
          ps.setLong(6, parent);
        }
        String pushStr = push == PropagationType.instant ? "I" : "T";
        String pullStr = pull == PropagationType.instant ? "I" : "T";
        ps.setString(7, pushStr);
        ps.setString(8, pullStr);
        ps.execute();
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

    }
  }

  @Override
  public long getWorkspaceBaseVersionNumber(final long privateVersion) throws WorkspaceExpiredException {
    logger.debug("getBaseVersionNumber(privateVersion={})", privateVersion);
    final Long result = (Long) this.getWorkspaceRepresentation(privateVersion)[Columns.WORKSPACE_BASE_VERSION.getIndex()];
    logger.debug("getBaseVersionNumber(privateVersion={}) => {}", privateVersion, result);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see at.jku.sea.cloud.DataStorage#getLastId()
   */
  @Override
  public long getMaximumArtifactId() {
    logger.debug("getMaximumArtifactId()()");
    if (this.idGenerator != null) {
      logger.debug("getMaximumArtifactId()() => {}", this.idGenerator.get());
      return this.idGenerator.get();
    }
    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      ps = PreparedStatementRepository.getMaxArtifactID;
      synchronized (ps) {
        result = ps.executeQuery();
      }
      if (result.next()) {
        logger.debug("getMaximumArtifactId()() => {}", result.getLong(1));
        return result.getLong(1);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(result);
    }
    logger.debug("getMaximumArtifactId()() => {}", 0);
    return 0;
  }

  @Override
  public void truncateAll() {
    this.resetGenerators();
    PreparedStatement ps = null;
    try {
      ps = this.conn.prepareStatement("TRUNCATE SCHEMA \"PUBLIC\" AND COMMIT");
      synchronized (ps) {
        ps.execute();
      }
      this.conn.close();
    } catch (final SQLException e) {
      logger.error("", e);
    } finally {
      this.closePS(ps);
    }

  }

  @Override
  protected void finalize() throws Throwable {
    PreparedStatementRepository.shutdown.execute();
  }

  @Override
  protected Set<Object[]> getArtifactReps(final long version, final Long minVersion, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId) {
    logger.debug("getArtifacts(version={}, minVersion={}, alive={}, toolId={}, ownerId={}, typeId={}, containerId={})",
        new Object[] { version, minVersion, alive, toolId, ownerId, typeId, containerId });
    ResultSet rs = null;
    final Set<Object[]> result = new HashSet<Object[]>();
    final List<Object> parameters = new ArrayList<Object>();
    try {
      PreparedStatement ps = PreparedStatementRepository.getArtifactRepsSQLStatement(this, parameters, version, minVersion, alive, toolId, ownerId, typeId, containerId);
      synchronized (ps) {
        for (int i = 0; i < parameters.size(); i++) {
          ps.setObject(1 + i, parameters.get(i));
        }
        rs = ps.executeQuery();
      }
      while (rs.next()) {
        Object[] rep = new Object[7];
        for (int i = 0; i < rep.length; i++) {
          rep[i] = rs.getObject(i + 1);
        }
        result.add(rep);
      }
      return result;
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);
    }
    return null;
  }

  @Override
  protected Set<Object[]> getArtifactReps(final long version, final Long minVersion, final boolean alive, final Long toolId, final Long ownerId, final Long typeId, final Long containerId,
      final String propertyName, final Object value, final Boolean isReference) {
    logger.debug("getArtifacts(version={}, minVersion={}, alive={}, toolId={}, ownerId={}, typeId={}, containerId={}, propertyName={}, propertyValue={}, propertyReference={})", new Object[] {
        version, minVersion, alive, toolId, ownerId, typeId, containerId, propertyName, value, propertyName });
    final Set<Object[]> ids;
    if ((propertyName != null) || (isReference != null) || (value != null)) {
      PreparedStatement ps = null;
      ResultSet rs = null;
      final Set<Object[]> result = new HashSet<Object[]>();
      final List<Object> parameters = new ArrayList<Object>();
      try {
        ps = PreparedStatementRepository.getArtifactsByPropertySQLStatement(this, parameters, version, minVersion, propertyName, alive, isReference ? (Long) value : null, ownerId, toolId, typeId,
            containerId);
        synchronized (ps) {
          for (int i = 0; i < parameters.size(); i++) {
            ps.setObject(1 + i, parameters.get(i));
          }
          rs = ps.executeQuery();
        }
        while (rs.next()) {
          Object[] rep = new Object[7];
          if ((value != null) && !isReference) {
            if (value.equals(rs.getObject(9))) {
              for (int i = 0; i < rep.length; i++) {
                rep[i] = rs.getObject(i + 1);
              }
              result.add(rep);
            }
          } else {
            for (int i = 0; i < rep.length; i++) {
              rep[i] = rs.getObject(i + 1);
            }
            result.add(rep);
          }
        }
      } catch (final SQLException e) {
        logger.debug("", e);
      } finally {
        this.closeRS(rs);
      }
      return result;
    } else {
      ids = this.getArtifactReps(version, minVersion, alive, toolId, ownerId, typeId, containerId);
    }
    return ids;
  }

  @Override
  public Object[] getArtifactRepresentation(final long version, final long id) throws ArtifactDoesNotExistException {
    logger.debug("getArtifactRepresentation(version={}, id={})", version, id);
    Object[] result = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = version < 0 ? PreparedStatementRepository.getArtifactRepresentationPrivate : PreparedStatementRepository.getArtifactRepresentationPublic;
      synchronized (ps) {
        if (version < 0) {
          ps.setLong(1, version);
        } else {
          ps.setLong(1, 0);
        }
        ps.setLong(2, version);
        ps.setLong(3, id);
        ps.setLong(4, id);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        result = new Object[7];
        for (int i = 0; i < result.length; i++) {
          result[i] = rs.getObject(i + 1);
        }
        logger.debug("getArtifactRepresentation(...) => (version={}, id={}, owner={}, tool={}, type={}, container={}, alive={})", result);
      } else {
        throw new ArtifactDoesNotExistException(version, id);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    return result;
  }

  @Override
  public Object[] getPropertyRepresentation(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    logger.debug("getPropertyRepresentation(version={}, id={}, propertyName={})", new Object[] { version, artifactId, propertyName });
    Object[] result = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = version < 0 ? PreparedStatementRepository.getPropertyRepresentationPrivate : PreparedStatementRepository.getPropertyRepresentationPublic;
      synchronized (ps) {
        if (version < 0) {
          ps.setLong(1, version);
        } else {
          ps.setLong(1, 0);
        }
        ps.setLong(2, version);
        ps.setLong(3, artifactId);
        ps.setString(4, propertyName);
        ps.setLong(5, artifactId);
        ps.setString(6, propertyName);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        result = new Object[8];
        for (int i = 0; i < result.length; i++) {
          result[i] = rs.getObject(i + 1);
        }
        logger.debug("getPropertyRepresentation(...) => (artifactId={}, version={}, owner={}, tool={}, propertyName={}, value={}, alive={}, reference={})", result);
      } else {
        throw new PropertyDoesNotExistException(version, artifactId, propertyName);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);
    }
    return result;
  }

  @Override
  public Object[] getPropertyRepresentationOrNull(final long version, final long artifactId, final String propertyName) throws ArtifactDoesNotExistException, PropertyDoesNotExistException {
    logger.debug("getPropertyRepresentation(version={}, id={}, propertyName={})", new Object[] { version, artifactId, propertyName });
    Object[] result = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      ps = version < 0 ? PreparedStatementRepository.getPropertyRepresentationPrivate : PreparedStatementRepository.getPropertyRepresentationPublic;
      synchronized (ps) {
        if (version < 0) {
          ps.setLong(1, version);
        } else {
          ps.setLong(1, 0);
        }
        ps.setLong(2, version);
        ps.setLong(3, artifactId);
        ps.setString(4, propertyName);
        ps.setLong(5, artifactId);
        ps.setString(6, propertyName);
        rs = ps.executeQuery();
      }
      if (rs.next()) {
        result = new Object[8];
        for (int i = 0; i < result.length; i++) {
          result[i] = rs.getObject(i + 1);
        }
        logger.debug("getPropertyRepresentation(...) => (artifactId={}, version={}, owner={}, tool={}, propertyName={}, value={}, alive={}, reference={})", result);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    return result;
  }

  @Override
  protected Set<Object[]> getVersionArtifacts(final long version) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    final Set<Object[]> artifacts = new HashSet<>();
    try {

      // ps = this.conn.prepareStatement("SELECT id FROM " + ARTIFACTS + " WHERE version = ?");
      ps = PreparedStatementRepository.getArtifactsInWS;
      synchronized (ps) {
        ps.setLong(1, version);
        rs = ps.executeQuery();
      }
      while (rs.next()) {
        // id BIGINT, version BIGINT,owner BIGINT,tool BIGINT, type BIGINT, container BIGINT, alive BOOLEAN
        Object[] result = new Object[7];
        for (int i = 0; i < result.length; i++) {
          result[i] = rs.getObject(i + 1);
        }
        artifacts.add(result);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    return artifacts;
  }

  @Override
  protected Set<Object[]> getVersionProperties(final long version) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    final Set<Object[]> properties = new HashSet<>();
    try {
      // ps = this.conn.prepareStatement("SELECT * FROM " + PROPS + " WHERE version = ?");
      ps = PreparedStatementRepository.getPropertiesInWs;
      synchronized (ps) {
        ps.setLong(1, version);
        rs = ps.executeQuery();
      }
      while (rs.next()) {
        // artifactId BIGINT, version BIGINT, owner BIGINT, tool BIGINT, name char(100), value OTHER, alive BOOLEAN,
        // reference BOOLEAN, collection BOOLEAN
        Object[] result = new Object[8];
        for (int i = 0; i < result.length; i++) {
          result[i] = rs.getObject(i + 1);
        }
        properties.add(result);
      }

      this.closeRS(rs);
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    return properties;
  }

  @Override
  protected Set<Object[]> getVersionPropertiesOfArtifact(final long version, final long id) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    final Set<Object[]> properties = new HashSet<>();
    try {
      // ps = this.conn.prepareStatement("SELECT * FROM " + PROPS + " WHERE version = ?");
      ps = PreparedStatementRepository.getPropertiesOfArtifactInWs;
      synchronized (ps) {
        ps.setLong(1, version);
        ps.setLong(1, id);
        rs = ps.executeQuery();
      }
      while (rs.next()) {
        // artifactId BIGINT, version BIGINT, owner BIGINT, tool BIGINT, name char(100), value OTHER, alive BOOLEAN,
        // reference BOOLEAN, collection BOOLEAN
        Object[] result = new Object[8];
        for (int i = 0; i < result.length; i++) {
          result[i] = rs.getObject(i + 1);
        }
        properties.add(result);
      }

      this.closeRS(rs);
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(rs);
    }
    return properties;
  }

  @Override
  public Collection<Object[]> getWorkspaceChildren(final Long parent) {
    logger.debug("getChildWorkspaces(parent={})", new Object[] { parent });
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Object[]> childWorkspaces = new ArrayList<>();
    try {
      // ps = this.conn.prepareStatement("SELECT * FROM " + PRIVATE + " WHERE private_version = ?");
      if (parent != null) {
        ps = PreparedStatementRepository.getChildWorkspaces;
        synchronized (ps) {
          ps.setLong(1, parent);
          rs = ps.executeQuery();
        }
      } else {
        ps = PreparedStatementRepository.getChildWorkspacesParentNull;
        synchronized (ps) {
          rs = ps.executeQuery();
        }
      }
      while (rs.next()) {
        // private_version BIGINT, base_version BIGINT, owner BIGINT, tool BIGINT, identifier char(50)
        Object[] result = new Object[8];
        for (int i = 0; i < result.length; i++) {
          result[i] = rs.getObject(i + 1);
        }

        result[Columns.WORKSPACE_PUSH.getIndex()] = ((String) result[Columns.WORKSPACE_PUSH.getIndex()]).compareTo("I") == 0 ? PropagationType.instant : PropagationType.triggered;
        result[Columns.WORKSPACE_PULL.getIndex()] = ((String) result[Columns.WORKSPACE_PULL.getIndex()]).compareTo("I") == 0 ? PropagationType.instant : PropagationType.triggered;
        childWorkspaces.add(result);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
      this.closeRS(rs);
    }
    return childWorkspaces;
  }

  @Override
  protected void setPrivateVersionParent(final long privateVersion, final Long newParent) {
    PreparedStatement ps = null;
    logger.debug("setWorkspaceParent(privateVersion={}, newParent={})", new Object[] { privateVersion, newParent });
    try {
      ps = PreparedStatementRepository.updateWorkspaceParent;
      synchronized (ps) {
        if (newParent == null) {
          ps.setNull(1, Types.BIGINT);
        } else {
          ps.setLong(1, newParent);
        }
        ps.setLong(2, privateVersion);
        ps.executeUpdate();
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
    }
  }

  @Override
  protected void setPrivateVersionPull(long privateVersion, PropagationType type) {
    PreparedStatement ps = null;
    logger.debug("setWorkspacePull(privateVersion={}, type={})", new Object[] { privateVersion, type });
    try {
      ps = PreparedStatementRepository.updateWorkspacePull;
      synchronized (ps) {
        String pullStr = type == PropagationType.instant ? "I" : "T";
        ps.setString(1, pullStr);
        ps.setLong(2, privateVersion);
        ps.executeUpdate();
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
    }
  }

  @Override
  protected void setPrivateVersionPush(long privateVersion, PropagationType type) {
    PreparedStatement ps = null;
    logger.debug("setWorkspacePush(privateVersion={}, type={})", new Object[] { privateVersion, type });
    try {
      ps = PreparedStatementRepository.updateWorkspacePush;
      synchronized (ps) {
        String pushStr = type == PropagationType.instant ? "I" : "T";
        ps.setString(1, pushStr);
        ps.setLong(2, privateVersion);
        ps.executeUpdate();
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {
    }
  }
  
  public Collection<User> getUsers() {
    Collection<User> result = new ArrayList<>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    logger.debug("getUsers()");
    try {
      ps = PreparedStatementRepository.getUsers;
      synchronized (ps) {
        ps.clearParameters();
        
        rs = ps.executeQuery();
        
        while (rs.next()) {
          result.add(fetchUser(rs));
        }

        ps.clearParameters();
      }
    } catch(final SQLException e) {
      logger.debug("",e);
    } finally {
      this.closeRS(rs);
    }
    
    return result;
  }
  
  public User getUser(long id) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    logger.debug("getUser({})",id);
    try {
      ps = PreparedStatementRepository.getUser;
      synchronized (ps) {
        ps.clearParameters();
        
        ps.setLong(1, id);
        rs = ps.executeQuery();
        
        if (rs.next()) {
          return fetchUser(rs);
        }

        ps.clearParameters();
      }
    } catch(final SQLException e) {
      logger.debug("",e);
    } finally {
      this.closeRS(rs);
    }
    throw new OwnerDoesNotExistException(id);
  }
  
  /**
   * {@inheritDoc}
   * 
   * Implementation note: This method is the only one to actually fetch the value of the password column,
   * and it only uses it to compare a plaintext candidate password against the stored hash.
   * 
   * Nowhere is the password hash actually passed out of the DataStorage, which is intentional.
   */
  @Override
  public User getUserByCredentials(String login, String password)
      throws CredentialsException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    User user = null;
    
    try {
      ps = PreparedStatementRepository.getUserByLogin;
      synchronized (ps) {
        ps.clearParameters();
        
        ps.setString(1, login);
        
        rs = ps.executeQuery();
        
        if (rs.next()) {
          user = fetchUser(rs);
          String hashed = rs.getString(Columns.USER_PASSWORD.getSqlIndex());
          
          boolean validPassword = BCrypt.checkpw(password, hashed);
          
          if (validPassword) {
            return user;
          }
        }
        
        ps.clearParameters();
      }
    } catch(final SQLException e) {
      logger.debug("",e);
    } finally {
      this.closeRS(rs);
    }

    throw new CredentialsException(login);
  }
  
  /**
   * Returns the user for a login name
   * @param login
   * @return null if user doesn't exist
   */
  public User getUserByLogin(String login) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    User user = null;
    
    try {
      ps = PreparedStatementRepository.getUserByLogin;
      synchronized (ps) {
        ps.clearParameters();
        
        ps.setString(1, login);
        // ps.setString(2, password);
        
        rs = ps.executeQuery();

        ps.clearParameters();
        
        if (rs.next()) {
          user = fetchUser(rs);
        } else {
          user = null;
        }
      }
    } catch(final SQLException e) {
      logger.debug("",e);
    } finally {
      this.closeRS(rs);
    }
    
    return user;
  }

  @Override
  public User getUserByOwnerId(long ownerId) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    User user = null;
    
    try {
      ps = PreparedStatementRepository.getUserForOwner;
      synchronized (ps) {
        ps.clearParameters();
        
        ps.setLong(1, ownerId);
        
        rs = ps.executeQuery();

        ps.clearParameters();
        
        if (rs.next()) {
          user = fetchUser(rs);
        } else {
          throw new OwnerDoesNotExistException(ownerId);
        }
      }
    } catch(final SQLException e) {
      logger.debug("",e);
    } finally {
      this.closeRS(rs);
    }
    
    return user;
  }
  
  /**
   * Fetches one user from the {@link ResultSet}.
   * Sets all fields except password.
   * @param rs
   * @return User representation
   * @throws SQLException
   */
  private User fetchUser(ResultSet rs) throws SQLException {
    return new DefaultUser(
        this,
        rs.getLong(Columns.USER_ID.getSqlIndex()),
        rs.getString(Columns.USER_NAME.getSqlIndex()),
        rs.getString(Columns.USER_LOGIN.getSqlIndex()),
        null, // rs.getString(Columns.USER_PASSWORD.getSqlIndex()), // password is not passed to outside!!
        rs.getLong(Columns.USER_OWNER.getSqlIndex())
    );
  }
  
  @Override
  public User createUser(String name, String login, String password) throws CredentialsException {
    
    // the code below is equivalent to
    //    long ownerId = createOwner(ADMIN, ROOT_TOOL_ID);
    // but the event firing is deferred until successful user creation
    
    final long version = this.versionGenerator.incrementAndGet();
    Set<Long> versions = new HashSet<>();
    Collection<ArtifactEvent> events = this.createArtifact(version, versions, ADMIN, ROOT_TOOL_ID, OWNER_TYPE_ID, OWNER_PACKAGE_ID);
    
    final long ownerId = events.iterator().next().id;
    
    // created the owner artifact, now we create the user
    
    User user = null;
    try {
      user = createUser(name, login, password, ownerId);
    } finally {
      if (user == null) {
        // delete the owner artifact since user creation failed
        deleteOwner(ownerId);
        // this never happened... shhhh! 8D
        versionGenerator.decrementAndGet();
      }
    }
    
    // we only fire the event for the owner artifact when the user has been created successfully
    fireArtifactEvent(events);
    
    return user;
  }
  
  @Override
  protected User createUser(String name, String login, String password, long ownerId) {
    String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
    
    PreparedStatement ps = null;
    long id = this.userGenerator.incrementAndGet();
    User user = null;
    logger.debug("insertUser({})");
    int affectedRows = 0;
    try {
      ps = PreparedStatementRepository.insertUser;
      synchronized (ps) {
        ps.clearParameters();
        
        ps.setLong(Columns.USER_ID.getSqlIndex(), id);
        ps.setString(Columns.USER_NAME.getSqlIndex(), name);
        ps.setString(Columns.USER_LOGIN.getSqlIndex(), login);
        ps.setString(Columns.USER_PASSWORD.getSqlIndex(), hashed);
        ps.setLong(Columns.USER_OWNER.getSqlIndex(), ownerId);
        
        affectedRows = ps.executeUpdate();
        
        if (affectedRows == 0) return null;
        
        // Password is not passed to outside!
        user = new DefaultUser(this, id, name, login, null, ownerId);
        // fill in the corresponding Owner artifact as well
        this.initializeOwner(ownerId, id, name, login);
        
        ps.clearParameters();
      }
    } catch(final SQLException e) {
      logger.debug("",e);
      throw new CredentialsException(login);
    } finally {
      // no user was created, so reset id generator
        if (affectedRows == 0) {
          this.userGenerator.decrementAndGet();
        }
    }

    return user;
  }
  
  /**
   * login, password or name can be changed, id and ownerId not.
   * 
   * password should be a plaintext string.
   */
  public User updateUser(User user) {
    final long id = user.getId(); 
    
    User loginUser = this.getUserByLogin(user.getLogin());
    
    // if there is already a user with the desired login different from the one to update
    // throw CredentialsException
    if (loginUser != null && loginUser.getId() != id) {
      throw new CredentialsException(user.getLogin());
    }
    
    // if the password is not set for update, use the existing one, else hash it
    String hashed = user.getPassword() == null ?
        getPasswordHash(id) :
        BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    
    PreparedStatement ps = null;
    logger.debug("updateUser({})");
    
    try {
      ps = PreparedStatementRepository.updateUser;
      synchronized (ps) {
        ps.clearParameters();
        
        ps.setString(1, user.getName());
        ps.setString(2, user.getLogin());
        
        ps.setString(3, hashed);
        
        ps.setLong(4, id);
        
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Updating user failed, no rows affected.");
        }
        
        ps.clearParameters();
      }
    } catch(final SQLException e) {
      logger.debug("",e);
      throw new OwnerDoesNotExistException(id);
    }
    
    // save changes in a new version
    versionGenerator.incrementAndGet();
    
    this.initializeOwner(user.getOwnerId(), id, user.getName(), user.getLogin());
    
    return getUser(id);
  }

  /**
   * fetches the password hash 
   * @param userId
   * @return
   */
  private String getPasswordHash(long userId) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
      ps = PreparedStatementRepository.getUserPasswordHash;
      ps.setLong(1, userId);

      rs = ps.executeQuery();

      if (rs.next()) {
        return rs.getString(1);
      }

    } catch (SQLException t) {

    }
    return null;
  }
  
  /**
   * fills the metadata owner artifact for the user with values
   * @param user
   */
  private void initializeOwner(long owner, long user, String name, String login) {
    // the owner created for this user must be in the current public head version
    final long version = getHeadVersionNumber();

    // only set properties where the value changed 
    String[] properties = {PROPERTY_NAME, PROPERTY_LOGIN, PROPERTY_OWNER_USER};
    Object[] values = {name, login, user};
    
    for (int i=0; i < properties.length; i++) {
      Object[] rep = this.getPropertyRepresentationOrNull(version, owner, properties[i]);
      Object value = rep == null ? null : rep[Columns.PROPERTY_VALUE.getIndex()];
      if (!values[i].equals(value)) {
        this.setPropertyValue(version, ADMIN, ROOT_TOOL_ID, owner, properties[i], values[i]);
      }
    }
    
//    this.setPropertyValue(version, ADMIN, ROOT_TOOL_ID, owner, PROPERTY_NAME, name);
//    this.setPropertyValue(version, ADMIN, ROOT_TOOL_ID, owner, PROPERTY_LOGIN, login);
//    this.setPropertyValue(version, ADMIN, ROOT_TOOL_ID, owner, PROPERTY_OWNER_USER, user);
  }
  
  public void deleteUser(long userId) {
    PreparedStatement ps;
    ResultSet rs = null;
    long ownerId = -1l; 
    
    try {
      ps = PreparedStatementRepository.getOwnerForUser;
      synchronized (ps) {
        ps.clearParameters();
        
        ps.setLong(1, userId);
        
        rs = ps.executeQuery();
        
        if (rs.next()) {
          ownerId = rs.getLong(1);
          
          deleteOwner(ownerId);
        }
      }
    } catch (SQLException e) {
      logger.debug("", e);
    } catch(OwnerDoesNotExistException e) {
      // there was no owner for the user, maybe it was deleted before
    } finally {
      this.closeRS(rs);
    }
    
    
    
    logger.debug("deleteUser({})", userId);
    try {
      ps = PreparedStatementRepository.deleteUser;
      synchronized (ps) {
        ps.clearParameters();
        
        ps.setLong(1,userId);
        
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Deleting user failed, no rows affected.");
        }
        
        ps.clearParameters();
      }
    } catch(final SQLException e) {
      logger.debug("",e);
      throw new OwnerDoesNotExistException(userId);
    }
  }
  
  @Override
  protected void deleteOwner(long ownerId) throws OwnerDoesNotExistException {
    try {
      setArtifactAlive(getHeadVersionNumber(), ADMIN, ROOT_TOOL_ID, ownerId, false);
    } catch (ArtifactDoesNotExistException e) {
      throw (OwnerDoesNotExistException) new OwnerDoesNotExistException(ownerId).initCause(e);
    }
    
//    PreparedStatement ps = null;
//    logger.debug("deleteOwner({})", ownerId);
//    try {
//      ps = PreparedStatementRepository.deletePropertiesUnversioned;
//      synchronized (ps) {
//        ps.clearParameters();
//        
//        ps.setLong(1,ownerId);
//        
//        ps.executeUpdate();
//
//        ps.clearParameters();
//      }
//    } catch(final SQLException e) {
//      logger.debug("",e);
//      throw new OwnerDoesNotExistException(ownerId);
//    }
//    
//    try {
//      ps = PreparedStatementRepository.deleteArtifactUnversioned;
//      synchronized (ps) {
//        ps.clearParameters();
//        
//        ps.setLong(1,ownerId);
//        
//        int affectedRows = ps.executeUpdate();
//
//        if (affectedRows == 0) {
//            throw new SQLException("Deleting user failed, no rows affected.");
//        }
//        
//        ps.clearParameters();
//      }
//    } catch(final SQLException e) {
//      logger.debug("",e);
//      throw new OwnerDoesNotExistException(ownerId);
//    }
  }
  
  @Override
  public long getMaximumUserId() {
    if (this.userGenerator != null) {
      long max = this.userGenerator.get();
      logger.debug("getMaximumUserId() => {}", max);
      return max;
    }
    PreparedStatement ps = null;
    ResultSet result = null;

    try {
      ps = PreparedStatementRepository.getMaxUserId;
      synchronized (ps) {
        result = ps.executeQuery();
      }
      if (result.next()) {
        logger.debug("getMaximumUserId() => {}", result.getLong(1));
        return result.getLong(1);
      }
    } catch (final SQLException e) {
      logger.debug("", e);
    } finally {

      this.closeRS(result);
    }
    logger.debug("getMaximumUserId() => {}", 0);
    return 0;
  }

  @Override
  public Tool createTool(String name, String toolVersion) {
    final long toolId = this.createTool(ADMIN, ROOT_TOOL_ID);
    logger.debug("created Tool(id={})", toolId);
    // the created tool has generated a new version
    final long version = versionGenerator.get();
    
    setPropertyValue(version, ADMIN, ROOT_TOOL_ID, toolId, PROPERTY_NAME, name);
    if (toolVersion != null) {
      setPropertyValue(version, ADMIN, ROOT_TOOL_ID, toolId, PROPERTY_TOOL_VERSION, toolVersion);
    }

    Tool tool = new DefaultTool(this, toolId, version);
    
    return tool;
  }

  @Override
  public void deleteTool(final long id) throws IllegalArgumentException, ToolDoesNotExistException {
    try {
      setArtifactAlive(getHeadVersionNumber(), ADMIN, ROOT_TOOL_ID, id, false);
    } catch (ArtifactDoesNotExistException e) {
      throw (ToolDoesNotExistException) new ToolDoesNotExistException(id).initCause(e);
    }
//    PreparedStatement ps = null;
//    logger.debug("deleteTool({})");
//    try {
//      ps = PreparedStatementRepository.deletePropertiesUnversioned;
//      synchronized (ps) {
//        ps.clearParameters();
//        
//        ps.setLong(1,id);
//        
//        ps.executeUpdate();
//
//        ps.clearParameters();
//      }
//    } catch(final SQLException e) {
//      logger.debug("",e);
//    }
//    
//    try {
//      ps = PreparedStatementRepository.deleteArtifactUnversioned;
//      synchronized (ps) {
//        ps.clearParameters();
//        
//        ps.setLong(1,id);
//        
//        int affectedRows = ps.executeUpdate();
//
//        if (affectedRows == 0) {
//            throw new SQLException("Deleting tool failed, no rows affected.");
//        }
//        
//        ps.clearParameters();
//      }
//    } catch(final SQLException e) {
//      logger.debug("",e);
//      throw new ToolDoesNotExistException(id);
//    }
  }
}
