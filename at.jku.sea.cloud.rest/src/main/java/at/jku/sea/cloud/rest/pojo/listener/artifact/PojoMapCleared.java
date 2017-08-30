package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "MapCleared")
public class PojoMapCleared
		extends PojoArtifactChange {

	public PojoMapCleared() {}

	public PojoMapCleared(long origin,
			PojoArtifact artifact) {
		super(origin, artifact);
	}
}
