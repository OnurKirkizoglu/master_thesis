package at.jku.sea.cloud.rest.client.handler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import at.jku.sea.cloud.rest.client.RestFactory;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public abstract class AbstractHandler {

//  protected final String SERVER_ADDRESS = "http://10.78.115.92:8080";
  protected final String SERVER_ADDRESS = "http://localhost:8080";
  protected final String CLOUD_ADDRESS = SERVER_ADDRESS + "/designspace";
  protected final String WORKSPACE_ADDRESS = CLOUD_ADDRESS + "/workspaces";
  protected final String ARTIFACT_ADDRESS = CLOUD_ADDRESS + "/artifacts";
  protected final String PROPERTY_ADDRESS = CLOUD_ADDRESS + "/artifacts/id=%d&v=%d/properties";
  protected final String VERSION_ADDRESS = CLOUD_ADDRESS + "/versions";
  protected final String OWNER_ADDRESS = CLOUD_ADDRESS + "/owners";
  protected final String TOOL_ADDRESS = CLOUD_ADDRESS + "/tools";
  protected final String PROJECT_ADDRESS = CLOUD_ADDRESS + "/projects";
  protected final String METAMODEL_ADDRESS = CLOUD_ADDRESS + "/metamodels";
  protected final String COLLECTIONARTIFACT_ADDRESS = CLOUD_ADDRESS + "/collectionartifacts";
  protected final String CONTAINER_ADDRESS = CLOUD_ADDRESS + "/containers";
  protected final String PACKAGE_ADDRESS = CLOUD_ADDRESS + "/packages";
  protected final String RESOURCE_ADDRESS = CLOUD_ADDRESS + "/resources";
  protected final String MAPARTIFACT_ADDRESS = CLOUD_ADDRESS + "/mapartifacts";
  protected final String MAPENTRY_ADDRESS = CLOUD_ADDRESS + "/mapentries";
  protected final String USER_ADDRESS = CLOUD_ADDRESS + "/users";

  // Listener - event poll address
  protected final String WORKSPACE_LISTENERPOLL_ADDRESS = WORKSPACE_ADDRESS + "/func/listener/id=%d/events";
  protected final String CLOUD_LISTENERPOLL_ADDRESS = CLOUD_ADDRESS + "/func/listener/id=%d/events";

  // Query language
  protected final String NAVIGATOR_ADDRESS = CLOUD_ADDRESS + "/navigators";
  protected final String STREAM_ADDRESS = CLOUD_ADDRESS + "/streams";

  protected static RestTemplate template = new RestTemplate();
  protected RestFactory restFactory;
  protected PojoFactory pojoFactory;
  
  static {
    // used for testing, but destroys exception handling on the clients
//		template.setErrorHandler(new DefaultResponseErrorHandler() {
//
//			private byte[] getResponseBody(ClientHttpResponse response) {
//			  byte[] ret = new byte[0];
//				try {
//					InputStream responseBody = response.getBody();
//					if (responseBody != null) {
//						ret = FileCopyUtils.copyToByteArray(responseBody);
//					}
//				} catch (IOException ex) {
//					// ignore
//				}
//				return ret;
//			}
//
//			private Charset getCharset(ClientHttpResponse response) {
//				HttpHeaders headers = response.getHeaders();
//				MediaType contentType = headers.getContentType();
//				return contentType != null ? contentType.getCharSet() : null;
//			}
//
//			private String getResponseString(ClientHttpResponse response) {
//				return new String(getResponseBody(response), getCharset(response));
//			}
//
//			@Override
//			public void handleError(ClientHttpResponse response) throws IOException {
//				System.err.println("ERROR: " + response.getStatusCode() + " " + response.getStatusText() + " CONTENT: "
//						+ getResponseString(response));
//				super.handleError(response);
//			}
//		});
  }

  public AbstractHandler() {
    restFactory = RestFactory.getInstance();
    pojoFactory = PojoFactory.getInstance();
  }
  
  /**
   * collects an array into a collection
   * @param elems
   * @return
   */
  protected <T> List<T> list(T[] elems) {
	  return new ArrayList<>(Arrays.asList(elems));
  }
  
  /** collects an array into a set
   * @param elems
   * @return
   */
  protected <T> Set<T> set(T[] elems) {
	  return new HashSet<>(Arrays.asList(elems));
  }
}
