package perfTesting;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.rest.client.RestCloud;

public class Commit {

	public static void main(String[] args) {
		Cloud c = RestCloud.getInstance();
		/*User user = MMMGeneral.getOrCreateUser(c);
		Workspace ws = MMMGeneral.getOrCreateWorkspace(c, user);
		
		new Setup_RequirementsCar(new Setup(ws));
		ws.commitAll("");*/
	}
}
