package carpackage.setup;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.rest.client.RestCloud;
import init.MMMConnection;
import init.MMMHelper;

public class Example {

	private Example() {
		init();
	}
	
	// -------------------- private methods -------------------- //
	
	private void init(){
		Cloud c = RestCloud.getInstance();
		User user = MMMConnection.getUser(c, "Onur Kirkizoglu", "oKirkizoglu", "12345");
//		User user = MMMConnection.getUser(c, "Onur Test", "oTest", "12345");
		MMMHelper helper = new MMMHelper(user);

		// MetaModel - UML
		new MetaModel(helper);

		// Code
		new Code(helper);

		// Requirement
		new Requirement(helper);

		helper.commit();

		System.out.println("*********** Example Finished ***********");
	}
	
	public static void main(String[] args) {
		new Example();
	}
	
}
