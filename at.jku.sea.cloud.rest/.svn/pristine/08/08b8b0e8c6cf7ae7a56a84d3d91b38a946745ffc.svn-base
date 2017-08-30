package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "Object")
public class PojoObject {

	@JsonTypeInfo(use=Id.NAME, property = "__type")
	@JsonSubTypes({
		@Type(value = Boolean.class),
		@Type(value = String.class),
		@Type(value = Byte.class),
		@Type(value = Short.class),
		@Type(value = Character.class),
		@Type(value = Integer.class),
		@Type(value = Long.class),
		@Type(value = Float.class),
		@Type(value = Double.class),
		@Type(value = PojoArtifact.class)
	})
	private Object object;

	public PojoObject() {}

	public PojoObject(
			Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(
			Object object) {
		this.object = object;
	}
}
