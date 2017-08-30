package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "ArtifactAliveSet")
public class PojoArtifactAliveSet
		extends PojoArtifactChange {

	private boolean alive;

	public PojoArtifactAliveSet() {}

	public PojoArtifactAliveSet(long origin,
			PojoArtifact artifact,
			boolean alive) {
		super(origin, artifact);
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(
			boolean alive) {
		this.alive = alive;
	}
}
