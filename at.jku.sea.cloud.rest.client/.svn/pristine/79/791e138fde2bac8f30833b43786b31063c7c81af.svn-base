package at.jku.sea.cloud.rest.client.handler;

import java.util.Collection;
import java.util.Set;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoKeyValue;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.utils.CloudUtils;

public class MapArtifactHandler extends AbstractHandler {
  
  private static MapArtifactHandler INSTANCE;
  
  public static MapArtifactHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new MapArtifactHandler();  
    }
    return INSTANCE;
  }
  
  protected MapArtifactHandler() {}

	public long size(long id, long version) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d/size", id, version);
		Long size = template.getForEntity(url, Long.class).getBody();
		return size;
  }

	public boolean containsKey(long id, long version, Object key) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d/func/containskey", id, version);
		PojoObject pojoObject = pojoFactory.createPojo(key);
	  return template.postForEntity(url, pojoObject, Boolean.class).getBody();
  }

	public boolean constainsValue(long id, long version, Object value) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d/func/containsvalue", id, version);
		PojoObject pojoObject = pojoFactory.createPojo(value);
	  return template.postForEntity(url, pojoObject, Boolean.class).getBody();
  }

	public Object get(long id, long version, Object key) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d/func/get", id, version);
		PojoObject pojoObject = pojoFactory.createPojo(key);
	  return restFactory.createRest(template.postForEntity(url, pojoObject, PojoObject.class).getBody());
  }

	public Object put(long wsid, long id, long version, Object key, Object value) throws PropertyDeadException, TypeNotSupportedException {
		if(!CloudUtils.isSupportedType(key)) {
			throw new TypeNotSupportedException(key.getClass());
		}
		if(!CloudUtils.isSupportedType(value)) {
			throw new TypeNotSupportedException(value.getClass());
		}
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/mapartifacts/id=%d&v=%d/func/put", wsid, id, version);
		PojoKeyValue pojoKeyValue = pojoFactory.createPojo(key, value);
		Object result = null;
		try {
			result = restFactory.createRest(template.postForEntity(url, pojoKeyValue, PojoObject.class).getBody());
		}
		catch(HttpClientErrorException e) {
			if(e.getResponseBodyAsString().equals(PropertyDeadException.class.getSimpleName())) {
				throw new PropertyDeadException(version, id, "value");
			}
			if(e.getResponseBodyAsString().equals(TypeNotSupportedException.class.getSimpleName())) {
				if(!CloudUtils.isSupportedType(key)) {
					throw new TypeNotSupportedException(key.getClass());
				}
				else {
					throw new TypeNotSupportedException(value.getClass());
				}
			}
		}
	  return result;
  }

	public Object remove(long wsid, long id, long version, Object key) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/mapartifacts/id=%d&v=%d/func/remove", wsid, id, version);
		if(!CloudUtils.isSupportedType(key)) {
			throw new TypeNotSupportedException(key.getClass());
		}
		PojoObject pojoKey = pojoFactory.createPojo(key);
		Object result = null;
		try {
			result = restFactory.createRest(template.postForEntity(url, pojoKey, PojoObject.class).getBody());
		}
		catch(HttpClientErrorException e) {
			if(e.getResponseBodyAsString().equals(TypeNotSupportedException.class.getSimpleName())) {
				throw new TypeNotSupportedException(key.getClass());
			}
			throw e;
		}
		return result;
  }

	public void clear(long wsid, long id, long version) {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/mapartifacts/id=%d&v=%d/func/clear", wsid, id, version);
		template.delete(url);
  }

	public Set<Object> keySet(long id, long version) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d/keyset", id, version);
	  return restFactory.createRestSet(template.getForEntity(url, PojoObject[].class).getBody());
  }

	public Collection<Object> values(long id, long version) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d/values", id, version);
	  return restFactory.createRestCollection(template.getForEntity(url, PojoObject[].class).getBody());
  }

	public Set<MapArtifact.Entry> entrySet(long id, long version) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d/entries", id, version);
	  return restFactory.createRestEntrySet(id, version, template.getForEntity(url, PojoArtifact[].class).getBody());
	}
}
