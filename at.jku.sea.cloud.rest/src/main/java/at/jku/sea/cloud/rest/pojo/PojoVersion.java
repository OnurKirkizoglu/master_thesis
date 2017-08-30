package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "Version")
@JsonSubTypes({
	@Type(value = PojoPublicVersion.class),
	@Type(value = PojoWorkspace.class)
})
public class PojoVersion {

	private long version;

	public PojoVersion() {}

	public PojoVersion(
			long version) {
		this.version = version;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(
			long version) {
		this.version = version;
	}
}
