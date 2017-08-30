package at.jku.sea.cloud.rest.client.handler;

import java.util.List;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.ToolDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoTool;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 * @author Jakob Mayer
 */
public class ToolHandler
		extends AbstractHandler {
	
	private static ToolHandler INSTANCE = null;
	
	public static ToolHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ToolHandler();
		}
		
		return INSTANCE;
	}
	
	protected ToolHandler() {}

	public List<Artifact> getArtifacts(
			long id) {
		String url = String.format(TOOL_ADDRESS + "/id=%d/artifacts", id);
		PojoArtifact[] pojoArtifacts = template.getForEntity(url, PojoArtifact[].class).getBody();
		return restFactory.createRestList(pojoArtifacts, restFactory::createRest);
	}
	
	
	
	public void deleteTool(long id) throws ToolDoesNotExistException {
		String url = String.format(TOOL_ADDRESS + "/id=%d", id);
		try {
			template.delete(url);
		} catch (HttpClientErrorException e) {
			if (ToolDoesNotExistException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
				throw new ToolDoesNotExistException(id);
			}
		}
	}
	
//	public void updateTool(Tool tool) {
//		long id = tool.getId();
//		String url = String.format(TOOL_ADDRESS + "/id=%d", id);
//		try {
//			PojoTool pojoTool = pojoFactory.createPojo(tool);
//			template.put(url,  pojoTool);
//		} catch (HttpClientErrorException e) {
//			if (ToolDoesNotExistException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
//				throw new ToolDoesNotExistException(id);
//			}
//		}
//	}

	public List<Tool> getTools() {
		String url = TOOL_ADDRESS;
		PojoTool[] pojoTools = template.getForEntity(url, PojoTool[].class).getBody();
		
		return restFactory.createRestList(pojoTools, restFactory::createRest);
	}

	public Tool createTool(String name, String toolVersion) {
		String url = TOOL_ADDRESS;
		
		PojoTool tool = new PojoTool(0, 0, name, toolVersion);
		
		tool = template.postForEntity(url, tool, PojoTool.class).getBody();
		
		return restFactory.createRest(tool);
	}
}
