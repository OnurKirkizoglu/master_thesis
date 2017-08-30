package at.jku.sea.cloud.rest.server.handler;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

public class ResourceHandler {

	@Autowired protected Cloud cloud;
	@Autowired protected PojoFactory pojoFactory;

	public String getFullQualifiedName(long id, long version) {
		Resource resource = (Resource) cloud.getArtifact(version, id);
		String uri = resource.getFullQualifiedName();
		return uri;
	}
}
