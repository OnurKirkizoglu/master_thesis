package at.jku.sea.cloud.rest.server.handler;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.implementation.DefaultMapArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.server.factory.DesignSpaceFactory;

public class MapArtifactHandler {
	
	@Autowired protected Cloud cloud;
	@Autowired protected PojoFactory factory;
	@Autowired protected DesignSpaceFactory dsFactory;

	public Long size(long id, long version) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
	  MapArtifact map = cloud.getMapArtifact(version, id);
	  return map.size();
  }

	public Boolean containsKey(long id, long version, PojoObject key) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		MapArtifact map = cloud.getMapArtifact(version, id);
		Object o = dsFactory.createDS(key);
	  return map.containsKey(o);
  }

	public Boolean containsValue(long id, long version, PojoObject value) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		MapArtifact map = cloud.getMapArtifact(version, id);
		Object o = dsFactory.createDS(value);
	  return map.containsValue(o);
  }

	public PojoObject get(long id, long version, PojoObject key) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		MapArtifact map = cloud.getMapArtifact(version, id);
		Object o = dsFactory.createDS(key);
		Object result = map.get(o);
	  return factory.createPojo(result);
  }

	public PojoObject[] keySet(long id, long version) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		MapArtifact map = cloud.getMapArtifact(version, id);
		Set<Object> result = map.keySet();
	  return factory.createPojoArray(result.toArray());
  }

	public PojoObject[] values(long id, long version) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
		MapArtifact map = cloud.getMapArtifact(version, id);
		Collection<Object> result = map.values();
	  return factory.createPojoArray(result.toArray());
  }

	public PojoArtifact[] entrySet(long id, long version) throws MapArtifactDoesNotExistException, VersionDoesNotExistException {
	  MapArtifact map = cloud.getMapArtifact(version, id);
	  Set<MapArtifact.Entry> entries = map.entrySet();
	  return factory.createPojoArray(entries.toArray(new DefaultMapArtifact.DefaultEntry[entries.size()]));
  }
	
}
