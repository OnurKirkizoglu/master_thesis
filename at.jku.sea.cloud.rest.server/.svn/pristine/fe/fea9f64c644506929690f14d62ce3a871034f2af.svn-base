package at.jku.sea.cloud.rest.server.handler;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class ToolHandler {

	@Autowired protected Cloud cloud;
	@Autowired protected PojoFactory pojoFactory;

	public PojoArtifact[] getArtifacts(
			long id)
			throws ToolDoesNotExistException {
		Tool tool = cloud.getTool(id);
		Artifact[] artifacts = tool.getArtifacts().toArray(new Artifact[0]);
		return pojoFactory.createPojoArray(artifacts);
	}

	public PojoTool getTool(
			long id)
			throws ToolDoesNotExistException {
		Tool tool = cloud.getTool(id);
		return pojoFactory.createPojo(tool);
	}

	public void deleteTool(long id) {
		cloud.deleteTool(id);
		
	}

	public PojoTool[] getTools() {
		Tool[] tools = cloud.getTools().toArray(new Tool[0]);
		return pojoFactory.createPojoArray(tools);
	}

	public PojoTool createTool(PojoTool pojoTool) {
		Tool tool = cloud.createTool(pojoTool.getName(),pojoTool.getToolVersion());
		return pojoFactory.createPojo(tool);
	}
}
