package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.rest.client.RestPackage;

public class ArtifactTreeItem implements Cloneable {

	String propertyName;
	String rawName;
	String displayName;
	long parentID;
	Artifact artifact;
	Collection<ArtifactInspectorEntry> properties;
	Collection<String> predicateList;

	public ArtifactTreeItem(Artifact artifact, String displayName) {
		this.artifact = artifact;
		this.propertyName = this.rawName = this.displayName = displayName;
		initArtifactTreeItem();
	}

	public ArtifactTreeItem(Artifact artifact, boolean showName) {
		this.artifact = artifact;
		initDisplayName(artifact, showName);
		initArtifactTreeItem();
	}

	public void initDisplayName(Artifact a, boolean showName) {
		this.propertyName = this.rawName = this.displayName = a.toString();

		if (artifact != null) {
			Collection<Property> properties = artifact.getAliveProperties();
			for (Property p : properties) {

				String n = p.getName();

				if (n.equals("name") || n.equals("fullName")) {
					Object o = p.getValue();
					if (o != null) {
						propertyName = o.toString();
					}
				}
			}

		}

		if (showName) {
			displayName = propertyName;
		} else {
			displayName = rawName;
		}
	}

	public void initArtifactTreeItem() {
		properties = new ArrayList<ArtifactInspectorEntry>();
		predicateList = new ArrayList<String>();

		if (artifact != null) {
			predicateList.add("" + artifact.getId());
			predicateList.add("id=" + artifact.getId());
			Map<String, Object> propertiesMap = artifact.getAlivePropertiesMap();
			for (Entry<String, Object> e : propertiesMap.entrySet()) {
				if (e.getValue() == null) {
					predicateList.add(e.getKey());
					properties.add(new ArtifactInspectorEntry(e.getKey(), "null"));
				} else {
					predicateList.add(e.getKey());
					predicateList.add(e.getValue().toString());
					properties.add(new ArtifactInspectorEntry(e.getKey(), e.getValue().toString()));
				}
			}
		}
		
		if(artifact != null)
		{
			Artifact pkg = artifact.getPackage();
			if(pkg != null)
			{
				parentID = pkg.getId();
			}else{
				parentID = -1;
			}			
		}
	}

	public void toggleDisplayName(boolean showName) {
		if (showName) {
			displayName = propertyName;
		} else {
			displayName = rawName;
		}

	}

	public String toString() {
		return displayName;
	}

	public Collection<ArtifactInspectorEntry> getProperties() {
		return properties;
	}
}
