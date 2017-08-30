package at.jku.sea.cloud.rest.client.handler;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.utils.CloudUtils;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class PropertyHandler extends AbstractHandler {

  private static PropertyHandler INSTANCE;
  
  public static PropertyHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PropertyHandler();  
    }
    return INSTANCE;
  }
  
  protected PropertyHandler() {}
  
  public Owner getOwner(long id, long version, String name) {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/owner", id, version, name);
    PojoOwner pojoOwner = template.getForEntity(url, PojoOwner.class).getBody();
    return restFactory.createRest(pojoOwner);
  }

  public Tool getTool(long id, long version, String name) {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/tool", id, version, name);
    PojoTool pojoTool = template.getForEntity(url, PojoTool.class).getBody();
    return restFactory.createRest(pojoTool);
  }

  public boolean isAlive(long id, long version, String name) {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/alive", id, version, name);
    Boolean isAlive = template.getForEntity(url, Boolean.class).getBody();
    return isAlive;
  }

  public boolean isReference(long id, long version, String name) {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/reference", id, version, name);
    Boolean isReference = template.getForEntity(url, Boolean.class).getBody();
    return isReference;
  }

  public Object getValue(long id, long version, String name) {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/value", id, version, name);
    PojoObject pojoObject = template.getForEntity(url, PojoObject.class).getBody();
    return restFactory.createRest(pojoObject);
  }

  public void setValue(long wsId, long id, long version, String name, Object value) throws ArtifactDoesNotExistException, ArtifactDeadException, PropertyDeadException, TypeNotSupportedException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/properties/name=%s/value", wsId, id, version, name);
    if (!CloudUtils.isSupportedType(value)) {
      throw new TypeNotSupportedException(value.getClass());
    }
    PojoObject pojoObject = pojoFactory.createPojo(value);
    try {
      template.put(url, pojoObject);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
        throw new ArtifactDoesNotExistException(version, id);
      } else if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      } else if (e.getResponseBodyAsString().equals(PropertyDeadException.class.getSimpleName())) {
        throw new PropertyDeadException(version, id, name);
      } else if (e.getResponseBodyAsString().equals(TypeNotSupportedException.class.getSimpleName())) {
        throw new TypeNotSupportedException(value.getClass());
      }
      throw e;
    }
  }

  public void delete(long wsId, long id, long version, String name) {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/properties/name=%s", wsId, id, version, name);
    template.delete(url);
  }

  public void undelete(long wsId, long id, long version, String name) {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/artifacts/id=%d&v=%d/properties/name=%s/func/undelete", wsId, id, version, name);
    template.put(url, null);
  }

  public Artifact getArtifact(long id, long version) {
    String url = String.format(ARTIFACT_ADDRESS + "/id=%d&v=%d", id, version);
    PojoArtifact pojoArtifact = template.getForEntity(url, PojoArtifact.class).getBody();
    return restFactory.createRest(pojoArtifact);
  }

  public Object[] getRepresentation(long id, long version, String name) {
    String url = String.format(PROPERTY_ADDRESS + "/name=%s/representation", id, version, name);
    try {
      PojoObject[] pobj = template.getForEntity(url, PojoObject[].class).getBody();
      return restFactory.createRestArray(pobj);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDoesNotExistException.class.getSimpleName())) {
        throw new ArtifactDoesNotExistException(version, id);
      } else if (e.getResponseBodyAsString().equals(PropertyDoesNotExistException.class.getSimpleName())) {
        throw new PropertyDoesNotExistException(version, id, name);
      } 
      throw e;
    }
  }
}
