package at.jku.sea.cloud.rest.client;

import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.rest.client.handler.OwnerHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestOwner
		extends RestArtifact
		implements Owner {

	private static final long serialVersionUID = 1L;

	private OwnerHandler handler;

	protected RestOwner(
			long id,
			long version) {
		super(id, version);
		handler = OwnerHandler.getInstance();
	}

	@Override
	public Collection<Artifact> getArtifacts() {
		return handler.getArtifacts(id);
	}
}
