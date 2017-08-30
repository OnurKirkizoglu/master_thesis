package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "PublicVersion")
public class PojoPublicVersion
		extends PojoVersion {

	private String message;

	public PojoPublicVersion() {}

	public PojoPublicVersion(
			long version,
			String message) {
		super(version);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(
			String message) {
		this.message = message;
	}
}
