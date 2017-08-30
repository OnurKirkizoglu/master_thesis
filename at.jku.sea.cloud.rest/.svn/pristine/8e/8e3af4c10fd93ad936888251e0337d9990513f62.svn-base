package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ArtifactAndProperties")
public class PojoArtifactAndProperties {

	private PojoArtifact artifact;
	private PojoProperty[] properties;

	public PojoArtifactAndProperties() {
	}

	public PojoArtifactAndProperties(PojoArtifact artifact,
			PojoProperty[] properties) {
		this.artifact = artifact;
		this.properties = properties;
	}

	public PojoArtifact getArtifact() {
		return artifact;
	}

	public void setArtifact(PojoArtifact artifact) {
		this.artifact = artifact;
	}

	public PojoProperty[] getProperties() {
		return properties;
	}

	public void setProperties(PojoProperty[] properties) {
		this.properties = properties;
	}
}
