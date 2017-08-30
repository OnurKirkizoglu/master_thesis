package init;

import at.jku.sea.cloud.*;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import at.jku.sea.cloud.mmm.MMMTypesFactory;
import at.jku.sea.cloud.rest.client.RestArtifact;
import at.jku.sea.cloud.rest.client.RestCloud;
import init.setup.Link;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Helper class used to support easier creation of complex types and instances.<br>
 * Furthermore holds all design space artifacts that are used by the {@link application.LinkApplication}.
 */
public class MMMHelper {

	private final Workspace ws;

	private Package setupPackage;

	private Artifact intType;
	private Artifact booleanType;
	private Artifact artifactType;
	private Artifact voidType;
	private Artifact stringType;

	private Artifact linkType;
	private Artifact descriptionType;
	
	private Artifact comboBoxNoSelection;
	private final SimpleDateFormat simpleDateFormat;
	

	public MMMHelper(User user) {
		Cloud c = RestCloud.getInstance();
		ws = MMMConnection.getWorkspace(c, user);

		simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		// get all from ds
		loadPackages();
		loadDataTypes();
		loadTypes();
		
	}

	/**
	 * Create a new complex type defined by a map of features.
	 *
	 * @param ws the {@link Workspace}
	 * @param pkg the {@link Package} where the complex type will be created
	 * @param name the name of the complex type
	 * @param features the features of the complex type
	 * @return the new created complex type
	 */
	public static Artifact createComplexType(Workspace ws, Package pkg, String name, Map<String, Artifact> features) {
		Artifact complexType = MMMTypesFactory.createComplexType(ws, pkg, name, false, false);
		if(features != null && !features.isEmpty()) {
			for(Entry<String, Artifact> e : features.entrySet()) {
				MMMTypesFactory.createFeature(ws, e.getKey(), complexType, e.getValue(), false, false, false);
			}
		}

		return complexType;
	}

	/**
	 * Create a new instance from a given complex type
	 *
	 * @param ws the {@link Workspace}
	 * @param pkg the {@link Package} where the instance will be created
	 * @param name the name of the instance
	 * @param complexType the type of the instance
	 * @param instanceValues the instance values, which will be set for the features
	 * @return the new created instance
	 */
	public static Artifact createComplexTypeInstance(Workspace ws, Package pkg, String name, Artifact complexType, Map<String, Object> instanceValues) {
		Artifact instance = MMMTypesFactory.createComplexTypeInstance(ws, name, complexType);
		instance.setPackage(ws, pkg);
		
		if (instanceValues != null && !instanceValues.isEmpty()) {
			for (Entry<String, Object> e : instanceValues.entrySet()) {
				instance.setPropertyValue(ws, e.getKey(), e.getValue());
			}
		}
		
		return instance;
	}

	/**
	 * Creates a new link instance
	 *
	 * @param linkName the name of the instance
	 * @param link the type
	 * @param source the link source
	 * @param target the link target
	 * @param linkPackage the package where it will be created
	 * @return the new link instance
	 */
	public Artifact createLinkInstance(String linkName, Link link, Artifact source, Artifact target, Package linkPackage) {
		Map<String, Object> instanceValues = new HashMap<>();
		instanceValues.put(Constants.LINK_DESCRIPTION, link.getDescription());
		instanceValues.put(Constants.LINK_SOURCE, source);
		instanceValues.put(Constants.LINK_TARGET, target);
		return createComplexTypeInstance(ws, linkPackage, linkName, link.getComplexType(), instanceValues);
	}

	public void commit() {
		ws.commitAll("");
	}
	
	// --------------------- GETTERS --------------------- //
	
	public Workspace getWorkspace() {
		return ws;
	}

	public Artifact getIntType() {
		return intType;
	}

	public Artifact getBooleanType() {
		return booleanType;
	}

	public Artifact getArtifactType() {
		return artifactType;
	}

	public Artifact getVoidType() {
		return voidType;
	}

	public Artifact getStringType() {
		return stringType;
	}

	public Artifact getLinkType() {
		return linkType;
	}

	public Artifact getDescriptionType() {
		return descriptionType;
	}
	
	public Artifact getComboBoxNoSelection(){
		return comboBoxNoSelection;
	}
	// --------------------- private init methods --------------------- //

	private void loadDataTypes() {
		for (Artifact a : setupPackage.getArtifacts()) {
			String name = (String) a.getPropertyValueOrNull(MMMTypeProperties.NAME);
			if (name != null) {
				switch (name) {
				case Constants.INT:
					intType = a;
					break;
				case Constants.STRING:
					stringType = a;
					break;
				case Constants.BOOLEAN:
					booleanType = a;
					break;
				case Constants.VOID:
					voidType = a;
					break;
				case Constants.COMBOBOX_ALL_SELECTION:
					comboBoxNoSelection = a;
					break;
				}
			}
		}
		artifactType = ws.getArtifact(DataStorage.COMPLEX_TYPE);
	}

	private void loadPackages() {
		for (Package p : ws.getPackages()) {
			String name = (String) p.getPropertyValueOrNull(DataStorage.PROPERTY_NAME);
			if(Constants.PKG_SETUP.equals(name))
				setupPackage = p;
		}
	}

	private void loadTypes() {
		for (Artifact a : setupPackage.getArtifacts()) {
			String name = (String) a.getPropertyValueOrNull(MMMTypeProperties.NAME);
			if (Constants.TYPE_LINK.equals(name)) {
				linkType = a;
			} else if (Constants.TYPE_DESCRIPTION.equals(name)){
				descriptionType = a;
			}
		}
	}

}
