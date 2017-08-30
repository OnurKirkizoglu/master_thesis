package at.jku.sea.cloud.rest.server.handler;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.exceptions.VersionDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class VersionHandler {

	@Autowired protected Cloud cloud;
	@Autowired protected PojoFactory pojoFactory;

	public String getIdentifier(
			long version)
			throws VersionDoesNotExistException {
		Version v = cloud.getVersion(version);
		String identifier = v.getIdentifier();
		return identifier;
	}

	public PojoOwner getOwner(
			long version)
			throws VersionDoesNotExistException {
		Version v = cloud.getVersion(version);
		Owner owner = v.getOwner();
		return pojoFactory.createPojo(owner);
	}

	public PojoTool getTool(
			long version)
			throws VersionDoesNotExistException {
		Version v = cloud.getVersion(version);
		Tool tool = v.getTool();
		return pojoFactory.createPojo(tool);
	}

	public PojoVersion getVersion(
			long version)
			throws VersionDoesNotExistException {
		Version v = cloud.getVersion(version);
		return pojoFactory.createPojo(v);
	}

	public Long getHeadVersion() {
		long head = cloud.getHeadVersionNumber();
		return head;
	}
}
