package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ArtifactAndProperitesFilter")
public class PojoArtifactAndPropertyFilter {

	PojoArtifact[] artifacts;
	PojoPropertyFilter[] propertyFilters;

	public PojoArtifactAndPropertyFilter() {}

	public PojoArtifactAndPropertyFilter(
			PojoArtifact[] artifacts,
			PojoPropertyFilter[] propertyFilters) {
		this.artifacts = artifacts;
		this.propertyFilters = propertyFilters;
	}

	public PojoArtifact[] getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(
			PojoArtifact[] artifacts) {
		this.artifacts = artifacts;
	}

	public PojoPropertyFilter[] getPropertyFilters() {
		return propertyFilters;
	}

	public void setPropertyFilters(
			PojoPropertyFilter[] propertyFilters) {
		this.propertyFilters = propertyFilters;
	}
}
