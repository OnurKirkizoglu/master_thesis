package application;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import javafx.beans.property.SimpleStringProperty;

public class ArtifactPair {
	private SimpleStringProperty complexTypeNameProperty;
	private SimpleStringProperty instanceNameProperty;
	private Artifact complexType;
	private Artifact instance;
	
	public ArtifactPair(Artifact complexType, Artifact instance){
		this.complexType = complexType;
		this.instance = instance;
		// TODO: Interaction with design space
		complexTypeNameProperty = new SimpleStringProperty((String)complexType.getPropertyValue(MMMTypeProperties.NAME));
		instanceNameProperty = new SimpleStringProperty((String)instance.getPropertyValue(MMMTypeProperties.NAME));
	}
	
	public String getComplexTypeNameProperty(){
		return complexTypeNameProperty.get();
	}
	public String getInstanceNameProperty(){
		return instanceNameProperty.get();
	}
	public Artifact getComplexType(){
		return complexType;
	}
	public Artifact getInstance(){
		return instance;
	}
}
