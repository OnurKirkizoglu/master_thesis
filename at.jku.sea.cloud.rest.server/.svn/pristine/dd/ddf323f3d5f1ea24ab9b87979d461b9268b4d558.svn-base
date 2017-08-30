package at.jku.sea.cloud.rest.server.handler;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoUser;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class OwnerHandler {

	@Autowired protected Cloud cloud;
	@Autowired protected PojoFactory pojoFactory;

	public PojoArtifact[] getArtifacts(
			long id)
			throws OwnerDoesNotExistException {
		Owner owner = cloud.getOwner(id);
		Artifact[] artifacts = owner.getArtifacts().toArray(new Artifact[0]);
		return pojoFactory.createPojoArray(artifacts);
	}

	public PojoOwner getOwner(
			long id)
			throws OwnerDoesNotExistException {
		Owner owner = cloud.getOwner(id);
		return pojoFactory.createPojo(owner);
	}
	
	public PojoUser getUserByOwnerId(long id) throws OwnerDoesNotExistException {
	  User user = cloud.getUserByOwnerId(id);
	  return pojoFactory.createPojo(user);
	}
	
	public void deleteUserByOwnerId(long id) throws OwnerDoesNotExistException {
    User user = cloud.getUserByOwnerId(id);
    cloud.deleteUser(user.getId());
  }
}
