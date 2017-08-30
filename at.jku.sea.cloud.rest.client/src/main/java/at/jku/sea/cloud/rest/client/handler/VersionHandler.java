package at.jku.sea.cloud.rest.client.handler;

import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoTool;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class VersionHandler
		extends AbstractHandler {
  
  private static VersionHandler INSTANCE;
  
  public static VersionHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new VersionHandler();  
    }
    return INSTANCE;
  }
  
  protected VersionHandler() {}

	public Owner getOwner(
			long version) {
		String url = String.format(VERSION_ADDRESS + "/v=%d/owner", version);
		PojoOwner pojoOwner = template.getForEntity(url, PojoOwner.class).getBody();
		return restFactory.createRest(pojoOwner);
	}

	public Tool getTool(
			long version) {
		String url = String.format(VERSION_ADDRESS + "/v=%d/tool", version);
		PojoTool pojoTool = template.getForEntity(url, PojoTool.class).getBody();
		return restFactory.createRest(pojoTool);
	}

	public String getIdentifier(
			long version) {
		String url = String.format(VERSION_ADDRESS + "/v=%d/identifier", version);
		String identifier = template.getForEntity(url, String.class).getBody();
		return identifier;
	}
}
