package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "MapElementRemoved")
public class PojoMapElementRemoved
		extends PojoArtifactChange {

	private PojoObject key, value;

	public PojoMapElementRemoved() {}

	public PojoMapElementRemoved(long origin,
			PojoArtifact artifact,
			PojoObject key,
			PojoObject value) {
		super(origin, artifact);
		this.key = key;
		this.value = value;
	}
	
	public PojoObject getKey() {
		return key;
	}
	
	public void setKey(PojoObject key) {
		this.key = key;
	}

	public PojoObject getValue() {
		return value;
	}

	public void setValue(
			PojoObject value) {
		this.value = value;
	}
}
