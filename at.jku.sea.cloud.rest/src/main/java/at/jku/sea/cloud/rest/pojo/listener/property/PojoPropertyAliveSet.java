package at.jku.sea.cloud.rest.pojo.listener.property;

import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "PropertyAliveSet")
public class PojoPropertyAliveSet
		extends PojoPropertyChange {

	private boolean alive;

	public PojoPropertyAliveSet() {}

	public PojoPropertyAliveSet(long origin,
			PojoProperty property,
			boolean alive) {
		super(origin, property);
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
