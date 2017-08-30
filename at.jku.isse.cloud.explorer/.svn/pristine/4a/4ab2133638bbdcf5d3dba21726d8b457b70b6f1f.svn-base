package application;

import javafx.beans.property.SimpleStringProperty;

public class ArtifactInspectorEntry {

	private final SimpleStringProperty propertyName;
	private final SimpleStringProperty propertyValue;
	
	public ArtifactInspectorEntry(String propertyName, String propertyValue)
	{
		this.propertyName = new SimpleStringProperty(propertyName);
		this.propertyValue = new SimpleStringProperty(propertyValue);
	}

	public String getPropertyName()
	{
		return propertyName.get();
		
	}
	
	public String getPropertyValue()
	{
		return propertyValue.get();
	}
}
