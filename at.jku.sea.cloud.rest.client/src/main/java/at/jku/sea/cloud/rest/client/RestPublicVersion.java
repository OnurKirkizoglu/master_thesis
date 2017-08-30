package at.jku.sea.cloud.rest.client;

import at.jku.sea.cloud.PublicVersion;

public class RestPublicVersion extends RestVersion implements PublicVersion {
	
	private String message;
	
	protected RestPublicVersion(long version, String message) {
		super(version);
		this.message = message;
	}

	@Override
	public String getCommitMessage() {
		return message;
	}

}
