package at.jku.sea.cloud.rest.client.handler;

import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class OwnerHandler
		extends AbstractHandler {
  
  private static OwnerHandler INSTANCE;
  
  public static OwnerHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new OwnerHandler();  
    }
    return INSTANCE;
  }
  
  protected OwnerHandler() {}

	public Collection<Artifact> getArtifacts(
			long id) {
		String url = String.format(OWNER_ADDRESS + "/id=%d/artifacts", id);
		PojoArtifact[] pojoArtifacts = template.getForEntity(url, PojoArtifact[].class).getBody();
		Artifact[] artifacts = restFactory.createRestArray(pojoArtifacts);
		return list(artifacts);
	}
}
