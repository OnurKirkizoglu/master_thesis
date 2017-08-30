package at.jku.sea.cloud.rest.client.handler;

import java.util.Collection;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.utils.CloudUtils;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class CollectionArtifactHandler extends AbstractHandler {
  
private static CollectionArtifactHandler INSTANCE;
  
  public static CollectionArtifactHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CollectionArtifactHandler();  
    }
    return INSTANCE;
  }
  
  protected CollectionArtifactHandler() {}

  public boolean isContainingOnlyArtifacts(long id, long version) {
    String url = String.format(COLLECTIONARTIFACT_ADDRESS + "/id=%d&v=%d/containsonlyartifacts", id, version);
    Boolean containsOnlyArtifacts = template.getForEntity(url, Boolean.class).getBody();
    return containsOnlyArtifacts;
  }

  public void addElement(long wsId, long id, long version, Object element) throws ArtifactDeadException, TypeNotSupportedException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/collectionartifacts/id=%d&v=%d/elements/func/add", wsId, id, version);
    if (!CloudUtils.isSupportedType(element)) {
      throw new TypeNotSupportedException(element.getClass());
    }
    try {
      template.put(url, pojoFactory.createPojo(element));
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      } else if (e.getResponseBodyAsString().equals(TypeNotSupportedException.class.getSimpleName())) {
        throw new TypeNotSupportedException(element.getClass());
      }
      throw e;
    }
  }

  public void addElements(long wsId, long id, long version, Collection<?> elements) {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/collectionartifacts/id=%d&v=%d/elements/func/addElements", wsId, id, version);
    for (Object element : elements) {
      if (!CloudUtils.isSupportedType(element)) {
        throw new TypeNotSupportedException(element.getClass());
      }
    }
    try {
      template.put(url, pojoFactory.createPojoArray(elements.toArray(new Object[] {})));
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      } else if (e.getResponseBodyAsString().equals(TypeNotSupportedException.class.getSimpleName())) {
        throw new TypeNotSupportedException(Object.class);
      }
      throw e;
    }
  }

  public void removeElement(long wsId, long id, long version, Object element) throws ArtifactDeadException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/collectionartifacts/id=%d&v=%d/elements/func/remove", wsId, id, version);
    if (!CloudUtils.isSupportedType(element)) {
      return;
    }
    try {
      template.put(url, pojoFactory.createPojo(element));
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      }
      throw e;
    }
  }

  public void insertElementAt(long wsId, long id, long version, long index, Object element) throws ArtifactDeadException, TypeNotSupportedException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/collectionartifacts/id=%d&v=%d/elements/pos=%d", wsId, id, version, index);
    if (!CloudUtils.isSupportedType(element)) {
      throw new TypeNotSupportedException(element.getClass());
    }
    try {
      template.put(url, pojoFactory.createPojo(element));
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      } else if (e.getResponseBodyAsString().equals(TypeNotSupportedException.class.getSimpleName())) {
        throw new TypeNotSupportedException(element.getClass());
      }
      throw e;
    }
  }

  public void removeElementAt(long wsId, long id, long version, long index) throws ArtifactDeadException, IndexOutOfBoundsException {
    String url = String.format(WORKSPACE_ADDRESS + "/id=%d/collectionartifacts/id=%d&v=%d/elements/pos=%d", wsId, id, version, index);
    try {
      template.delete(url);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
        throw new ArtifactDeadException(version, id);
      } else if (e.getResponseBodyAsString().equals(IndexOutOfBoundsException.class.getSimpleName())) {
        throw new IndexOutOfBoundsException();
      }
      throw e;
    }
  }

  public Object getElementAt(long id, long version, long index) {
    String url = String.format(COLLECTIONARTIFACT_ADDRESS + "/id=%d&v=%d/elements/pos=%d", id, version, index);
    PojoObject pojoObject = template.getForEntity(url, PojoObject.class).getBody();
    return restFactory.createRest(pojoObject);
  }

  public Collection<?> getElements(long id, long version) {
    String url = String.format(COLLECTIONARTIFACT_ADDRESS + "/id=%d&v=%d/elements", id, version);
    Object[] objects = null;
    try {
      PojoObject[] pojoObjects = template.getForEntity(url, PojoObject[].class).getBody();
      objects = restFactory.createRestArray(pojoObjects);
    } catch (HttpClientErrorException e) {
      if (e.getResponseBodyAsString().equals(CollectionArtifactDoesNotExistException.class.getSimpleName())) {
        throw new CollectionArtifactDoesNotExistException(version, id);
      }
      throw e;
    }
    return list(objects);
  }

  public boolean existsElement(long id, long version, Object element) {
    String url = String.format(COLLECTIONARTIFACT_ADDRESS + "/id=%d&v=%d/elements/func/exists", id, version);
    if (!CloudUtils.isSupportedType(element)) {
      return false;
    }
    Boolean exists = template.postForEntity(url, pojoFactory.createPojo(element), Boolean.class).getBody();
    return exists;
  }

  public long size(long id, long version) {
    String url = String.format(COLLECTIONARTIFACT_ADDRESS + "/id=%d&v=%d/size", id, version);
    long size = template.getForEntity(url, Long.class).getBody();
    return size;
  }

}
