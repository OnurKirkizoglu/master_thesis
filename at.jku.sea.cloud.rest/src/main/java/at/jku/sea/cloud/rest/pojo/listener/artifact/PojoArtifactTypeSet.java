package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "ArtifactTypeSet")
public class PojoArtifactTypeSet
		extends PojoArtifactChange {

	private PojoArtifact type;

	public PojoArtifactTypeSet() {}

	public PojoArtifactTypeSet(long origin,
			PojoArtifact artifact,
			PojoArtifact type) {
		super(origin, artifact);
		this.type = type;
	}

	public PojoArtifact getType() {
		return type;
	}

	public void setType(
			PojoArtifact type) {
		this.type = type;
	}
}
