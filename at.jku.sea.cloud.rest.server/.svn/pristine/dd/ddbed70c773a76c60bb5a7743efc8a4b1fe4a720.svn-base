package at.jku.sea.cloud.rest.server.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.server.handler.ArtifactHandler;
import at.jku.sea.cloud.rest.server.handler.PropertyHandler;

@Controller
@RequestMapping("/designspace/viewer")
public class ViewerController {
	Logger logger = Logger.getGlobal();
	
	  @Autowired
	  ArtifactHandler handler;
	  
	  @Autowired
	  PropertyHandler propHandler;
	  
	  @Autowired
	  Cloud cloud;
	  
	  final static String TYPE_INFO = "typeInfo";
	  final static String STYLING_INFO = "stylingInfo";
	  final static String DELIMITER = ";";
	  final static String DB_URL = "jdbc:hsqldb:mem:xdb";
	  final static String DB_USER = "sa";
	  final static String DB_PASSWORD = "";
	  
	  /**
	   * *** FOR DEMO ONLY ***
	   * 
	   * Provide an interface for simple creation of Properties
	   * This method is not secure and should only be used for demo purpose. It directly accesses
	   * 	and modifies the In-Memory-Database with minimal checking. Open for different kinds
	   * 	of SQL-injection etc.
	   * 
	   * @param id
	   * @param version
	   * @param owner
	   * @param tool
	   * @param name
	   * @param value
	   * @param alive
	   * @param reference
	   * @return
	   */
	  @RequestMapping(value = "/property/create_complex", method = RequestMethod.POST)
	  public @ResponseBody ResponseEntity<String> createPropertyComplex(@RequestParam("id") long id,
			  @RequestParam("version") long version, @RequestParam("owner") long owner, @RequestParam("tool") long tool,
			  @RequestParam("name") String name, @RequestParam("aid") long aid, @RequestParam("aversion") long aversion,
			  @RequestParam("alive") String alive, @RequestParam("reference") long reference) {
		  logger.warning("Using demo-only-method 'createProperty'.");
		  
		  // set default response message
		  String response = "SQL-Exception: see server log for details";
		  
		  try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			
			// Check whether Property with this name already exists
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM properties WHERE artifactId = " + id + " AND version = " + version + " AND name = '" + name + "'");
			ResultSet result = stmt.executeQuery();
			
			if(result.next()) {		// do nothing
				response = "Fail: Property '" + name + "' with A-ID '" + id + "' and Version '" + version + "' already exists";
				
			} else {				// insert property in DB
				String sref = (reference == -1) ? "NULL" : ("" + reference );		
				stmt = conn.prepareStatement("INSERT INTO properties VALUES("
						+ id + "," + version + "," + owner + "," + tool + "," + "'" + name + "',"
						+ "?,"					// Artifact has to be injected as type "JAVA_OBJECT"
						+ alive + "," + sref + ")");
				
				Artifact artifact = cloud.getArtifact(aversion, aid);
				
				// Inject Artifact and execute query
				stmt.setObject(1, artifact, Types.OTHER);
				stmt.executeUpdate();
				
				response = "Success: Property '" + name + "' with Artifact-Value '" + aid + "', A-ID '" + id + "' and A-Version '" + version + "' created";
			}
			
			logger.info(response);
			conn.close();
			
		  } catch(SQLException e) {
			  response = "Fail: Could not insert Property '" + name + "' with Artifact-Value '" + aid + "', A-ID '" + id + "' and A-Version '" + version + "'";
			  e.printStackTrace();
		  } catch(ArtifactDoesNotExistException e) {
			  response = "Fail: Could not insert Property '" + name + "' with Artifact-Value '" + aid + "', A-ID '" + id + "' and A-Version '" + version + "' because Artifact does not yet exist.";
			  e.printStackTrace();
		  }
		  
		  response = "{\"status\":\"" + response + "\"}";
		  
		return new ResponseEntity<String>(response, getCORSHeaders(), HttpStatus.OK);
	  }

	  /**
	   * *** FOR DEMO ONLY ***
	   * 
	   * Provide an interface for simple creation of Properties
	   * This method is not secure and should only be used for demo purpose. It directly accesses
	   * 	and modifies the In-Memory-Database with minimal checking. Open for different kinds
	   * 	of SQL-injection etc.
	   * 
	   * @param id
	   * @param version
	   * @param owner
	   * @param tool
	   * @param name
	   * @param value
	   * @param alive
	   * @param reference
	   * @return
	   */
	  @RequestMapping(value = "/property/create_simple", method = RequestMethod.POST)
	  public @ResponseBody ResponseEntity<String> createPropertySimple(@RequestParam("id") long id,
			  @RequestParam("version") long version, @RequestParam("owner") long owner, @RequestParam("tool") long tool,
			  @RequestParam("name") String name, @RequestParam("value") String value, @RequestParam("alive") String alive,
			  @RequestParam("reference") long reference) {
		  logger.warning("Using demo-only-method 'createProperty'.");
		  
		  // set default response message
		  String response = "SQL-Exception: see server log for details";
		  
		  try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			
			// Check whether Property with this name already exists
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM properties WHERE artifactId = " + id + " AND version = " + version + " AND name = '" + name + "'");
			ResultSet result = stmt.executeQuery();
			
			if(result.next()) {		// do nothing
				response = "Fail: Property '" + name + "' with A-ID '" + id + "' and Version '" + version + "' already exists";
				
			} else {				// insert property in DB
				String sref = (reference == -1) ? "NULL" : ("" + reference );		
				stmt = conn.prepareStatement("INSERT INTO properties VALUES("
						+ id + "," + version + "," + owner + "," + tool + "," + "'" + name + "',"
						+ "?,"					// has to be injected as type "JAVA_OBJECT"
						+ alive + "," + sref	+ ")");
				
				// inject value as Java-Object and execute query
				stmt.setObject(1, value, Types.JAVA_OBJECT);
				stmt.executeUpdate();
				
				response = "Success: Property '" + name + "' with Value '" + value + "', A-ID '" + id + "' and A-Version '" + version + "' created";
			}
			
			logger.info(response);
			conn.close();
			
		  } catch(SQLException e) {
			  response = "Fail: Could not insert Property '" + name + "' with Value '" + value + "', A-ID '" + id + "' and A-Version '" + version + "'";
			  e.printStackTrace();
		  }
		  
		  response = "{\"status\":\"" + response + "\"}";
		  
		return new ResponseEntity<String>(response, getCORSHeaders(), HttpStatus.OK);
	  }
	  
	  /**
	   * *** FOR DEMO ONLY ***
	   * 
	   * Provide an interface for simple creation of Artifacts
	   * This method is not secure and should only be used for demo purpose. It directly accesses
	   * 	and modifies the In-Memory-Database with minimal checking. Open for different kinds
	   * 	of SQL-injection etc.
	   * 
	   * @param id
	   * @param version
	   * @param tool
	   * @return
	   */
	  @RequestMapping(value = "/artifact/create", method = RequestMethod.POST)
	  public @ResponseBody ResponseEntity<String> createArtifact(@RequestParam("id") long id,
			  @RequestParam("version") long version, @RequestParam("owner") long owner, @RequestParam("tool") long tool,
			  @RequestParam("type") long type, @RequestParam("package") long pkg, @RequestParam("alive") String alive) {
		  logger.warning("Using demo-only-method 'createArtifact'.");
		  System.out.println("Uploading demo data");
		  // set default response message
		  String response = "SQL-Exception: see server log for details";

		  try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			
			// check whether Artifact with given attributes already exists
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM artifacts WHERE ID = " + id + " AND VERSION = " + version);
			ResultSet result = stmt.executeQuery();
			
			if(result.next()) {		// do nothing
				response = "Fail: Artifact with ID '" + id + "' and Version '" + version + "' already exists";
				
			} else {				// insert artifact in DB
				stmt = conn.prepareStatement("INSERT INTO ARTIFACTS VALUES("
					+ id + "," + version + "," + owner + "," + tool + "," + type + "," + pkg + "," + alive
					+ ")");
				
				stmt.executeUpdate();
				
				response = "Success: Artifact with ID '" + id + "' and Version '" + version + "' created";
			}
			
			logger.info(response);
			
			conn.close();
			
		  } catch(SQLException e) {
			  response = "Fail: Could not insert Artifact with ID '" + id + "' and Version '" + version + "'";
			  e.printStackTrace();
		  }
		  
		  response = "{\"status\":\"" + response + "\"}";
		  
		return new ResponseEntity<String>(response, getCORSHeaders(), HttpStatus.OK);
	  }
	  
	  /**
	   * Return a list of Artifacts, Properties and Linkages based on a specified Artifact
	   * 
	   * The list of Artifacts consists of
	   * 	- the requested Artifacts itself
	   * 	- all Property-Artifacts of the requested Artifact
	   * 	- all Property-Artifacts of the Property Artifacts (recursive)
	   * 
	   * The list of Properties consists of all Properties of the requested Artifact as well as of the Sub-Artifacts
	   * 
	   * Two limits can be passed as parameter to prevent too big results
	   * 	- level: Maximum level of depth to search for Artifacts
	   * 		The requested Artifact itself is level 0,
	   * 		the Property-Artifact of the requested Artifact is level 1 and so on..
	   * 		Stop searching after the specified level has been reached
	   * 
	   * 	- maxArtifacts: Maximum number of Artifacts to be sent
	   * 		Stop searching after the specified number of Artifacts has been reached
	   * 
	   * If the actual number of Artifacts / level of depth exceeds the allowed result size, let the
	   * 	client know by setting either 'reachedEnd' or 'reachedMaxArtifacts' to true in response
	   * 	object. (Default for both: false)
	   * 
	   * @param id
	   * @param version
	   * @param level
	   * @param maxArtifacts
	   * @return
	   * @throws ArtifactDoesNotExistException
	   * @throws VersionDoesNotExistException
	   */
	  @RequestMapping(value = "/artifact/id={id}&v={version}&l={level}&a={maxArtifacts}", method = RequestMethod.GET)
	  public @ResponseBody ResponseEntity<Object> getArtifacts(@PathVariable long id, @PathVariable long version, @PathVariable int level, @PathVariable int maxArtifacts) throws ArtifactDoesNotExistException, VersionDoesNotExistException {
		  // get root artifact from parameters containing properties / sub-artifacts in a tree structure
	    
		  List<ViewerArtifact> artifacts = new ArrayList<>();
		  List<ViewerLinks> links = new ArrayList<>();
		  ViewerStatus status = new ViewerStatus(false, false);	// Default is assumed: {reachedEnd: false, reachedMaxArtifacts: false}
	    
		  traverseArtifact(id, version, level, maxArtifacts, artifacts, links, status);
		  
		  ViewerWrapper result = new ViewerWrapper(artifacts.toArray(new ViewerArtifact[0]),	// Array of Artifacts
				  									links.toArray(new ViewerLinks[0]),			// Array of Links
				  									"" + status.getReachedEnd(),				// Stringified Boolean Flag whether max depth level has been reached
				  									"" + status.getReachedMaxArtifacts()); 		// Stringified Boolean Flag whether max number of Artifacts has been reached
	    
		  return new ResponseEntity<Object>(result, getCORSHeaders(), HttpStatus.OK);
	  }
	  
	  /**
	   * Helper method to recursively traverse an Artifact
	   * Collects:
	   * 	- the Artifact itself and all of its sub-Artifacts (= Artifacts contained via its Properties)
	   * 	- the Properties of the Artifacts are directly contained in the "ViewerArtifacts"
	   * 	- the relation between the Artifacts (e.g. A1 contains A2 via one of its Properties) 
	   * 
	   * Keeps track of:
	   * 	- whether maximum level of depth of the graph search is reached
	   * 	- whether there exist more artifacts than the passed maximum
	   * 
	   * Filters:
	   * 	- all Properties according to Type information (given as Property)
	   * 		- look for Type information in the Artifact itself
	   * 		- if not found, look for Type information in its Meta-Artifact
	   * 
	   * @param id:				ID of the Artifact
	   * @param version:		Version of the Artifact
	   * @param level: 			Maximum level of depth to search in the Graph
	   * @param maxArtifacts:	Maximum number of Artifacts that should be sent back
	   * @param artifacts:		Container for the found Artifacts
	   * @param links:			Container for the found Links
	   * @param status:			Keeps track of whether the 2 max values have been reached
	   */
	  private void traverseArtifact(long id, long version, int level, int maxArtifacts, List<ViewerArtifact> artifacts, List<ViewerLinks> links, ViewerStatus status) {

		  // check if maximum number of transferable artifacts has already been reached
		  if(maxArtifacts <= artifacts.size()) {
			  logger.info("Maxim number of Artifacts has been reached - stop searching.");
			  status.setReachedMaxArtifacts(true);
			  return;
		  }
		  
		  List<ViewerProperty> viewProps = new ArrayList<>();
		  PojoProperty[] props = propHandler.getProperties(id, version, true);
		  
		  // Determine whether Type and Styling are present in current or in Meta-Artifact
		  TypeInfo info = getTypeInfo(props, id, version);
		  boolean useStyling = info.hasInfo();
		  
		  if(!useStyling) {	// Type and Styling not available 
			  logger.warning("No Styling found - switching to no-style-mode.");
		  }
		  
		  Map<String, String> stylings = new java.util.HashMap<>();
		  
		  if(useStyling) {
			  String[] keys = info.getTypeInfo().split(DELIMITER);
			  String[] values = info.getStylingInfo().split(DELIMITER);
			  
			  for(int i = 0; i < keys.length; i++) {
				  stylings.put(keys[i], values[i]);
			  }
		  }
		  
		  // iterate over all "LEAVE-PROPERTIES" of current level 
		  for(PojoProperty prop : props) {
			  if(!(prop.getObject().getObject() instanceof PojoArtifact)) {
				  logger.info("Resolve Value Property with Name: '" + prop.getName() + "' and Value: '" + prop.getObject().getObject() + "'");
				  
				  // if a styling has been found (useStyling:true) only add the property if it is found in the styling list
				  // if no styling has been found (useStyling:false) add the property with default styling
				  if(!useStyling || stylings.containsKey(prop.getName())) {
					  
					  String styling = (useStyling) ? stylings.get(prop.getName()) : "xxx";
					  
					  // Object could be null
					  Object tmpProp = prop.getObject().getObject();
					  if(tmpProp == null) {
						  viewProps.add(new ViewerProperty(prop.getName(), "null", styling));
					  } else {
						  viewProps.add(new ViewerProperty(prop.getName(), prop.getObject().getObject().toString(), styling));
					  }
				  }
			  }
		  }
		  
		  logger.info("Resolve Artifact with ID '" + id + "', Version '" + version + "' and " + viewProps.size() + " Properties");
		  
		  // add the current artifact with its leave properties to global result list
		  artifacts.add(new ViewerArtifact(id, version, viewProps.toArray(new ViewerProperty[0])));
		  
		  // iterate over all "ARTIFACTS-PROPERTY" in the next level
		  if(0 < level) {
			  for(PojoProperty prop : props) {
				  if(prop.getObject().getObject() instanceof PojoArtifact) {
					  PojoArtifact tmp = (PojoArtifact) prop.getObject().getObject();

					  logger.info("Remember a link from Artifact '" + id + "_v" + version + "' to Artifact " + tmp.getId() + "_v" + tmp.getVersion() + "'");
					  
					  // add a link from the current artifact to its property artifact
					  links.add(new ViewerLinks(id, version, tmp.getId(), tmp.getVersion(), prop.getName(), "xxx"));
					  /**
					   * TODO: The "xxx" can be replaced by arbitrary information that should later be displayed at the client
					   * 		(The 2 Strings are displayed at the beginning and at the end of a relation in an ER-diagram) 
					   */
					  
					  // check whether this property artifact is already in the list to prevent circular traversing 
					  boolean alreadyIn = false;
					  for(ViewerArtifact current : artifacts) {
						  if(current.getId() == tmp.getId() && current.getVersion() == tmp.getVersion()) {
							  alreadyIn = true;
						  }
					  }
					  
					  if(!alreadyIn) {
						  // go one level deeper in the graph and traverse the property artifact
						  traverseArtifact(tmp.getId(), tmp.getVersion(), level - 1, maxArtifacts, artifacts, links, status);
					  }
				  }
			  }
		  } else {
			  //check if end has been reached
			  for(PojoProperty prop : props) {
				  if(prop.getObject().getObject() instanceof PojoArtifact) {
					  logger.info("Maximum level of depth has been reached - stop searching.");
					  status.setReachedEnd(true);
				  }
			  }
		  }
	  }
	  
	  /**
	   * Determine whether Styling and Type Info is present in an Array of Properties
	   * If not, try searching in Artifact's Meta-Artifact
	   * 
	   * Return a result Object containing information of
	   * 	- boolean: whether both Infos could be found
	   * 	- String: the Styling itself
	   * 	- String: the Type itself
	   * 
	   * @param props
	   * @return
	   */
	  private TypeInfo getTypeInfo(PojoProperty[] props, long id, long version) {
		  TypeInfo result = getTypeInfoFromArray(props);
		  
		  if(!result.hasInfo()) {
			  // Style and Type could not (or just partially) be found, search in Meta-Artifact
			  logger.info("No info about styling found - start searching in Meta-Artifact");
			  
			  PojoArtifact meta = handler.getType(id, version);
			  PojoProperty[] metaProps = propHandler.getProperties(meta.getId(), meta.getVersion(), false);
			  
			  result = getTypeInfoFromArray(metaProps);
		  }
		  
		  return result;
	  }
	  
	  /**
	   * Determine whether Styling and Type Info is present in a single Array of Properties
	   * 
	   * Return a result Object containing information of
	   * 	- boolean: whether both Infos could be found
	   * 	- String: the Styling itself
	   * 	- String: the Type itself
	   * 
	   * @param props
	   * @return
	   */
	  private TypeInfo getTypeInfoFromArray(PojoProperty[] props) {
		  TypeInfo result = new TypeInfo();
		  
		  // iterate over all Properties and look for Type and Styling Info
		  for(PojoProperty prop : props) {
			  if(prop.getName().equals(TYPE_INFO)) {
				  result.setTypeInfo(prop.getObject().getObject().toString());
			  }
			  
			  if(prop.getName().equals(STYLING_INFO)) {
				  result.setStylingInfo(prop.getObject().getObject().toString());
			  }
		  }
		  
		  // IFF both infos could be found, set 'hasInfo' to true
		  if(!result.getStylingInfo().equals("") && !result.getTypeInfo().equals("")) {
			  result.setHasInfo(true);
		  }
		  
		  return result;
	  }
	  
	  /**
	   * Helper function for creating CORS headers
	   * To enable REST services (especially for different domains / ports) it is necessary 
	   * to pass additional header information.
	   * Note: this can be replaced in newer versions of Spring by '@CrossOrigin'
	   * 
	   * @return HttpHeaders
	   */
	  private HttpHeaders getCORSHeaders() {
		  HttpHeaders headers = new HttpHeaders();
		  headers.set("Access-Control-Allow-Origin", "*");
		  headers.set("Content-Type", "application/json; charset=UTF-8");
		  return headers;
	  }
}

class TypeInfo {
	private String typeInfo;
	private String stylingInfo;
	private boolean bothPresent;

	public TypeInfo() {
		super();
		this.typeInfo = "";
		this.stylingInfo = "";
		this.bothPresent = false;
	}
	
	public String getTypeInfo() {
		return typeInfo;
	}
	public void setTypeInfo(String typeInfo) {
		this.typeInfo = typeInfo;
	}
	public String getStylingInfo() {
		return stylingInfo;
	}
	public void setStylingInfo(String stylingInfo) {
		this.stylingInfo = stylingInfo;
	}
	public boolean hasInfo() {
		return bothPresent;
	}
	public void setHasInfo(boolean hasInfo) {
		this.bothPresent = hasInfo;
	}
	
}

/**
 *	Artifact entity to be sent to the client viewer
 *	Contains Artifact's ID, Version and an Array of ViewerProperties
 */
class ViewerArtifact {
	private long id;
	private long version;
	private ViewerProperty[] properties;
	
	public ViewerArtifact() {
		super();
	}
	
	public ViewerArtifact(long id, long version, ViewerProperty[] properties) {
		super();
		this.id = id;
		this.version = version;
		this.properties = properties;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public ViewerProperty[] getProperties() {
		return properties;
	}
	public void setProperties(ViewerProperty[] properties) {
		this.properties = properties;
	}
	
}

/**
 *	Property entity to be sent to the client viewer
 *	Contains the Property's name, value and some additional information
 *	Additional information is for the displaying of the viewer only, so
 *	the information could be a RGB value or an image name..
 */
class ViewerProperty {
	private String name;
	private String value;
	private String info;
	
	public ViewerProperty() {
		super();
	}

	public ViewerProperty(String name, String value, String info) {
		super();
		this.name = name;
		this.value = value;
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}

/**
 * 	Link entity to be sent to the client viewer
 *	Represents the linkage between 2 Artifacts (the "line" between 2 entities in an ER diagram)
 *	The ID and the Version are used at client side to identify each Artifact
 *	Furthermore, 2 information strings can be passed to display additional information at
 *	client side (e.g. kind of relation like "1" and "0..n" for ER diagram) 
 */
class ViewerLinks {
	private long fromID;
	private long fromVersion;
	private long toID;
	private long toVersion;
	private String fromInfo;
	private String toInfo;
	
	public ViewerLinks() {
		super();
	}
	
	public ViewerLinks(long fromID, long fromVersion, long toID, long toVersion, String fromInfo, String toInfo) {
		super();
		this.fromID = fromID;
		this.fromVersion = fromVersion;
		this.toID = toID;
		this.toVersion = toVersion;
		this.fromInfo = fromInfo;
		this.toInfo = toInfo;
	}

	public long getFromID() {
		return fromID;
	}

	public void setFromID(long fromID) {
		this.fromID = fromID;
	}

	public long getFromVersion() {
		return fromVersion;
	}

	public void setFromVersion(long fromVersion) {
		this.fromVersion = fromVersion;
	}

	public long getToID() {
		return toID;
	}

	public void setToID(long toID) {
		this.toID = toID;
	}

	public long getToVersion() {
		return toVersion;
	}

	public void setToVersion(long toVersion) {
		this.toVersion = toVersion;
	}

	public String getFromInfo() {
		return fromInfo;
	}

	public void setFromInfo(String fromInfo) {
		this.fromInfo = fromInfo;
	}
	
	public String getToInfo() {
		return toInfo;
	}

	public void setToInfo(String toInfo) {
		this.toInfo = toInfo;
	}
	
}

/**
 * 	Status entity NOT directly sent to the client viewer -
 *	Used to keep track of whether the maximum level of depth passed 	
 *	by the client has been reached ("reachedEnd":true) or if the
 *	maximum number of Artifacts passed by the client has been reached
 *	("reachedMaxArtifacts":true)
 */
class ViewerStatus {
	private boolean reachedEnd;
	private boolean reachedMaxArtifacts;
	
	public ViewerStatus() {
		super();
	}
	
	public ViewerStatus(boolean reachedEnd, boolean reachedMaxArtifacts) {
		super();
		this.reachedEnd = reachedEnd;
		this.reachedMaxArtifacts = reachedMaxArtifacts;
	}

	public boolean getReachedEnd() {
		return reachedEnd;
	}

	public void setReachedEnd(boolean reachedEnd) {
		this.reachedEnd = reachedEnd;
	}

	public boolean getReachedMaxArtifacts() {
		return reachedMaxArtifacts;
	}

	public void setReachedMaxArtifacts(boolean reachedMaxArtifacts) {
		this.reachedMaxArtifacts = reachedMaxArtifacts;
	}
	
}

/**
 * 	Wrapper entity to be sent to the client viewer
 *	Contains:
 *	- an Array of Artifacts (requested Artifacts + sub-Artifacts)
 *	- an Array of Properties (Properties of requested Artifact + sub-Artifacts)
 *	- 2 String flags for describing whether max depth level or max Artifact number
 *		has been reached. (Strings contain stringified boolean value) 
 */
class ViewerWrapper {
	private ViewerArtifact[] artifacts;
	private ViewerLinks[] links;
	private String reachedEnd;
	private String reachedMaxArtifacts;
	
	public ViewerWrapper() {
		super();
	}
	
	public ViewerWrapper(ViewerArtifact[] artifacts, ViewerLinks[] links, String reachedEnd, String reachedMaxArtifacts) {
		super();
		this.artifacts = artifacts;
		this.links = links;
		this.reachedEnd = reachedEnd;
		this.reachedMaxArtifacts = reachedMaxArtifacts;
	}

	public ViewerArtifact[] getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(ViewerArtifact[] artifacts) {
		this.artifacts = artifacts;
	}

	public ViewerLinks[] getLinks() {
		return links;
	}

	public void setLinks(ViewerLinks[] links) {
		this.links = links;
	}
	
	public String getReachedEnd() {
		return reachedEnd;
	}

	public void setReachedEnd(String reachedEnd) {
		this.reachedEnd = reachedEnd;
	}
	
	public String getReachedMaxArtifacts() {
		return reachedMaxArtifacts;
	}

	public void setReachedMaxArtifacts(String reachedMaxArtifacts) {
		this.reachedMaxArtifacts = reachedMaxArtifacts;
	}
	
}
