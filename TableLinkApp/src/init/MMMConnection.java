package init;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.Workspace;

//TODO: Whole class interact with design space.
/**
 *
 */
public class MMMConnection {

	private MMMConnection() {} // hide public constructor

	/**
	 * @param c
	 * @param name
	 * @param login
	 * @param password
	 * @return
	 */
	public static User getUser(Cloud c, String name, String login, String password) {
		User user;
		try {
			user = c.createUser(name, login, password);
		} catch (Exception e) {
			user = c.getUserByCredentials(login, password);
		}
		
		return user;
	}

	/**
	 *
	 * @param c
	 * @param user
	 * @return
	 */
	public static Workspace getWorkspace(Cloud c, User user) {
		Workspace ws = c.getWorkspace(user.getOwner(), c.getTool(DataStorage.ECLIPSE_TOOL_ID), "DSAppWorkspace");
		if (ws == null) {
			ws = c.createWorkspace(user.getOwner(), c.getTool(DataStorage.ECLIPSE_TOOL_ID), "DSAppWorkspace");
		}
		
		return ws;
	}
	
}
