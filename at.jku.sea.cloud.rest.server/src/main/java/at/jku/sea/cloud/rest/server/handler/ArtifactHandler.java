package at.jku.sea.cloud.rest.server.handler;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class ArtifactHandler {

	@Autowired protected Cloud cloud;
	@Autowired protected PojoFactory pojoFactory;

	public PojoOwner getOwner(
			long id,
			long version)
			throws ArtifactDoesNotExistException, VersionDoesNotExistException {
		Artifact artifact = cloud.getArtifact(version, id);
		Owner owner = artifact.getOwner();
		return pojoFactory.createPojo(owner);
	}

	public PojoTool getTool(
			long id,
			long version)
			throws ArtifactDoesNotExistException, VersionDoesNotExistException {
		Artifact artifact = cloud.getArtifact(version, id);
		Tool tool = artifact.getTool();
		return pojoFactory.createPojo(tool);
	}

	public Boolean isAlive(
			long id,
			long version)
			throws ArtifactDoesNotExistException, VersionDoesNotExistException {
		Artifact artifact = cloud.getArtifact(version, id);
		Boolean isAlive = artifact.isAlive();
		return isAlive;
	}

	public PojoArtifact getType(
			long id,
			long version)
			throws ArtifactDoesNotExistException, VersionDoesNotExistException, NullPointerException {
		Artifact artifact = cloud.getArtifact(version, id);
		Artifact type = artifact.getType();
		if(type == null) {
			throw new NullPointerException();
		}
		return pojoFactory.createPojo(type);
	}

	public PojoPackage getPackage(
			long id,
			long version)
			throws ArtifactDoesNotExistException, VersionDoesNotExistException, NullPointerException {
		Artifact artifact = cloud.getArtifact(version, id);
		Package _package = artifact.getPackage();
//		if (_package == null) {
//			throw new NullPointerException();
//		}
		return pojoFactory.createPojo(_package);
	}

	public PojoArtifact getArtifact(
			long id,
			long version)
			throws ArtifactDoesNotExistException, VersionDoesNotExistException {
		Artifact artifact = cloud.getArtifact(version, id);
		return pojoFactory.createPojo(artifact);
	}

  public PojoObject[] getArtifactRepresentation(long version, long id) {
    Object[] artifact = cloud.getArtifactRepresentation(version, id);
    return pojoFactory.createPojoArray(artifact);
  }
}
