package at.jku.sea.cloud.rest.pojo.listener.property;

import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "PropertyRollBack")
public class PojoPropertyRollBack
		extends PojoPropertyChange {

	private boolean decreased;

	public PojoPropertyRollBack() {}

	public PojoPropertyRollBack(long origin,
			PojoProperty property,
			boolean decreased) {
		super(origin, property);
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
