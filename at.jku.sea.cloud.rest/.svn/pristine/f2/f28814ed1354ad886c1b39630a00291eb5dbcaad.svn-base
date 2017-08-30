package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "Workspace")
public class PojoWorkspace
	extends PojoVersion {

	private long id;

	public PojoWorkspace() {}

	public PojoWorkspace(
			long version,
			long id) {
		super(version);
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(
			long id) {
		this.id = id;
	}
}
