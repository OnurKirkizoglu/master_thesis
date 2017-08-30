package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "ArtifactRollBack")
public class PojoArtifactRollBack
		extends PojoArtifactChange {

	private boolean decreased;

	public PojoArtifactRollBack() {}

	public PojoArtifactRollBack(long origin,
			PojoArtifact artifact,
			boolean decreased) {
		super(origin, artifact);
		this.decreased = decreased;
	}

	public boolean isDecreased() {
		return decreased;
	}

	public void setDecreased(
			boolean decreased) {
		this.decreased = decreased;
	}

}
