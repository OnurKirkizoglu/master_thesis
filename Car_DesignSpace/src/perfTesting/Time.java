package perfTesting;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.rest.client.RestCloud;

public class Time {

	public static void main(String[] args) {
		Cloud c = RestCloud.getInstance();
		/*User user = MMMGeneral.getOrCreateUser(c);
		Workspace ws = MMMGeneral.getOrCreateWorkspace(c, user);
		
		//measure time here!
		for(int i = 0; i < 50; i++) {
		long startTime = System.nanoTime();
		ws.getArtifact(339); //TODO auslesen aus der mainapp!
		long endTime = System.nanoTime();
		System.err.println(endTime - startTime);
		}*/
		
	}
}
