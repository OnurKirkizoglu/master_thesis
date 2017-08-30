package at.jku.sea.cloud.rest.client.handler;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.utils.CloudUtils;

public class MapEntryHandler
		extends AbstractHandler {
  
  private static MapEntryHandler INSTANCE;
  
  public static MapEntryHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new MapEntryHandler();  
    }
    return INSTANCE;
  }
  
  protected MapEntryHandler() {}

	public Object getKey(
			long mapId,
			long mapVersion,
			long entryId,
			long entryVersion) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d/entries/id=%d&v=%d/key", mapId, mapVersion, entryId, entryVersion);
		return restFactory.createRest(template.getForEntity(url, PojoObject.class).getBody());
	}

	public Object getValue(
			long mapId,
			long mapVersion,
			long entryId,
			long entryVersion) {
		String url = String.format(MAPARTIFACT_ADDRESS + "/id=%d&v=%d/entries/id=%d&v=%d/value", mapId, mapVersion, entryId, entryVersion);
		return restFactory.createRest(template.getForEntity(url, PojoObject.class).getBody());
	}

	public Object setValue(
			long wsid,
			long mapId,
			long mapVersion,
			long entryId,
			long entryVersion,
			Object value) {
		if (!CloudUtils.isSupportedType(value)) {
			throw new TypeNotSupportedException(value.getClass());
		}
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/mapartifacts/id=%d&v=%d/entries/id=%d&v=%d/value", wsid, mapId, mapVersion, entryId, entryVersion);
		PojoObject request = pojoFactory.createPojo(value);
		PojoObject response = null;
		try {
			response = template.postForEntity(url, request, PojoObject.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(TypeNotSupportedException.class.getSimpleName())) {
				throw new TypeNotSupportedException(value.getClass());
			}
			if (e.getResponseBodyAsString().equals(PropertyDeadException.class.getSimpleName())) {
				throw new PropertyDeadException(entryVersion, entryId, "value");
			}
			throw e;
		}
		return restFactory.createRest(response);
	}

}
