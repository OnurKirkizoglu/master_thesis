package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "MapPut")
public class PojoMapPut
		extends PojoArtifactChange {

	private PojoObject key, oldValue, newValue;
	private boolean isAdded;

	public PojoMapPut() {}

	public PojoMapPut(long origin,
			PojoArtifact artifact,
			PojoObject key,
			PojoObject oldValue,
			PojoObject newValue,
			boolean isAdded) {
		super(origin, artifact);
		this.key = key;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.isAdded = isAdded;
	}

	public PojoObject getKey() {
		return key;
	}

	public void setKey(PojoObject key) {
		this.key = key;
	}

	public PojoObject getOldValue() {
		return oldValue;
	}

	public void setOldValue(PojoObject oldValue) {
		this.oldValue = oldValue;
	}

	public PojoObject getNewValue() {
		return newValue;
	}

	public void setNewValue(PojoObject newValue) {
		this.newValue = newValue;
	}

	public boolean isAdded() {
		return isAdded;
	}

	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
	}

}
