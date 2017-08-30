package at.jku.sea.cloud.rest.client;

import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.rest.client.handler.ResourceHandler;

public class RestResource extends RestContainer implements Resource {

	private static final long serialVersionUID = 1L;

	private ResourceHandler handler;
	
	protected RestResource(long id, long version) {
		super(id, version);
		handler = ResourceHandler.getInstance();
	}

	@Override
	public String getFullQualifiedName() {
		return handler.getFullQualifiedName(id, version);
	}

	@Override
	public void setFullQualifiedName(Workspace workspace, String fullQualifiedName) {
		handler.setFullQualifiedName(workspace.getId(), id, version, fullQualifiedName);
	}
}
