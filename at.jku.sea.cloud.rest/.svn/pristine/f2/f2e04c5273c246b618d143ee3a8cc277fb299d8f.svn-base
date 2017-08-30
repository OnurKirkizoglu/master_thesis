package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "PropertyFilter")
public class PojoPropertyFilter {

	private String name;
	private PojoObject value;

	public PojoPropertyFilter() {}

	public PojoPropertyFilter(
			String name,
			PojoObject value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(
			String name) {
		this.name = name;
	}

	public PojoObject getValue() {
		return value;
	}

	public void setValue(
			PojoObject value) {
		this.value = value;
	}
}
