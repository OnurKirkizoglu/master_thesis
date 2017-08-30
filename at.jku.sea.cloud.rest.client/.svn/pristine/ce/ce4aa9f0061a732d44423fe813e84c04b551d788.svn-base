package at.jku.sea.cloud.rest.client;

import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.rest.client.handler.ToolHandler;
import at.jku.sea.cloud.utils.StringUtils;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestTool
		extends RestArtifact
		implements Tool {

	private static final long serialVersionUID = 1L;

	private ToolHandler handler;
	
	private String name, toolVersion;

	protected RestTool(
			long id,
			long version,
			String name,
			String toolVersion) {
		super(id, version);
		handler = ToolHandler.getInstance();
		this.name = name;
		this.toolVersion = toolVersion;
	}

	@Override
	public Collection<Artifact> getArtifacts() {
		return handler.getArtifacts(id);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getToolVersion() {
		return toolVersion;
	}
	
	@Override
	public String toString() {
		return StringUtils.printf("%s[id=%s, version=%s, name=%s, toolVersion=%s]", this.getClass().getSimpleName(), this.id, this.getVersionNumber(), this.name, this.toolVersion);
	}
}
