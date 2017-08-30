package at.jku.sea.cloud.rest.server.handler;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.exceptions.CollectionArtifactDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.server.factory.DesignSpaceFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class CollectionArtifactHandler {

	@Autowired protected Cloud cloud;
	@Autowired protected PojoFactory pojoFactory;
	@Autowired protected DesignSpaceFactory dsFactory;

	public Boolean containsOnlyArtifacts(
			long id,
			long version)
			throws CollectionArtifactDoesNotExistException {
		CollectionArtifact ca = cloud.getCollectionArtifact(version, id);
		Boolean containsOnlyArtifacts = ca.isContainingOnlyArtifacts();
		return containsOnlyArtifacts;
	}

	public PojoObject[] getElements(
			long id,
			long version)
			throws CollectionArtifactDoesNotExistException {
		CollectionArtifact ca = cloud.getCollectionArtifact(version, id);
		Object[] objects = ca.getElements().toArray(new Object[0]);
		return pojoFactory.createPojoArray(objects);
	}

	public PojoObject getElementAt(
			long id,
			long version,
			long index)
			throws CollectionArtifactDoesNotExistException {
		CollectionArtifact ca = cloud.getCollectionArtifact(version, id);
		Object object = ca.getElementAt(index);
		return pojoFactory.createPojo(object);
	}

	public Boolean existsElement(
			long id,
			long version,
			PojoObject element)
			throws CollectionArtifactDoesNotExistException {
		CollectionArtifact ca = cloud.getCollectionArtifact(version, id);
		Object e = dsFactory.createDS(element);
		Boolean exists = ca.existsElement(e);
		return exists;
	}

	public Long size(
			long id,
			long version)
			throws CollectionArtifactDoesNotExistException {
		CollectionArtifact ca = cloud.getCollectionArtifact(version, id);
		Long size = ca.size();
		return size;
	}
}
