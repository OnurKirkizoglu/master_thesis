package init.setup;

import at.jku.sea.cloud.*;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.mmm.MMMTypesFactory;
import at.jku.sea.cloud.rest.client.RestCloud;
import init.Constants;
import init.MMMConnection;
import init.MMMHelper;

import java.util.HashMap;
import java.util.Map;

public class Setup {
	
	private Workspace ws;

	private Package setupPackage;

	private Artifact artifactType;
	private Artifact stringType;
	
	private Setup(Workspace ws) {
		this.ws = ws;
		init();
	}
	
	public static void main(String[] args) {
		Cloud c = RestCloud.getInstance();
		User user = MMMConnection.getUser(c, "Onur Kirkizoglu", "oKirkizoglu", "12345");
		Workspace ws = MMMConnection.getWorkspace(c, user);
		new Setup(ws);
	}
	
	private void init() {
		// packages
		setupPackage = ws.createPackage(Constants.PKG_SETUP);
		// data types
		createDataType(Constants.INT);
		createDataType(Constants.BOOLEAN);
		createDataType(Constants.VOID);
		artifactType = ws.getArtifact(DataStorage.COMPLEX_TYPE);
		stringType = createDataType(Constants.STRING);

		// additional necessary types
		createLinkType();
		createDescriptionType();
		
		Map<String, Artifact> features = new HashMap<>();

		// no selection for link-combobox
		MMMHelper.createComplexType(ws, setupPackage, Constants.COMBOBOX_ALL_SELECTION, features);
		
		ws.commitAll("Init done");
		System.out.println("*********** Setup Finished ***********");
	}

	private Artifact createDataType(String name) {
		switch(name) {
		case Constants.BOOLEAN:
			return MMMTypesFactory.createBooleanDataType(ws, setupPackage, name);
		case Constants.INT: 
			return MMMTypesFactory.createIntDataType(ws, setupPackage, name);
		case Constants.STRING:
			return MMMTypesFactory.createStringDataType(ws, setupPackage, name);
		default: 
			return MMMTypesFactory.createDataType(ws, setupPackage, name, false, false, false, false);
		}
	}
	
	private Artifact createLinkType() {
		Map<String, Artifact> features = new HashMap<>();
		features.put(Constants.LINK_DESCRIPTION, artifactType);
		features.put(Constants.LINK_SOURCE, artifactType);
		features.put(Constants.LINK_TARGET, artifactType);

		// create the complex types of links
		return MMMHelper.createComplexType(ws, setupPackage, Constants.TYPE_LINK, features);
	}

	private Artifact createDescriptionType() {
		Map<String, Artifact> features = new HashMap<>();
		features.put(Constants.DESC_DESCRIPTION, stringType);
		return MMMHelper.createComplexType(ws, setupPackage, Constants.TYPE_DESCRIPTION, features);
	}
}
