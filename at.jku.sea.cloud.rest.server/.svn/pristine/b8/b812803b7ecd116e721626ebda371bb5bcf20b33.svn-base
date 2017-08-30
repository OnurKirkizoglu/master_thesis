package at.jku.sea.cloud.rest.server.handler;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.MapArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.implementation.DefaultMapArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.server.factory.DesignSpaceFactory;

public class MapEntryHandler {

	@Autowired protected Cloud cloud;
	@Autowired protected PojoFactory pojoFactory;
	@Autowired protected DesignSpaceFactory dsFactory;

	public PojoObject getKey(
			long id,
			long version, long eid, long eversion)
			throws ArtifactDoesNotExistException, MapArtifactDoesNotExistException, VersionDoesNotExistException, PropertyDoesNotExistException {
		Artifact artifact = cloud.getArtifact(eversion, eid); 
		Object key = artifact.getPropertyValue(DefaultMapArtifact.DefaultEntry.KEY);
		return pojoFactory.createPojo(key);
	}

	public PojoObject getValue(
			long id,
			long version, long eid, long eversion)
			throws ArtifactDoesNotExistException, MapArtifactDoesNotExistException, VersionDoesNotExistException, PropertyDoesNotExistException {
		Artifact artifact = cloud.getArtifact(eversion, eid);
		Object value = artifact.getPropertyValue(DefaultMapArtifact.DefaultEntry.VALUE);
		return pojoFactory.createPojo(value);
	}
}
