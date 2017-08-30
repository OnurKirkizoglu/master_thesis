package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "Owner")
public class PojoOwner
		extends PojoArtifact {

	public PojoOwner() {
		super();
	}

	public PojoOwner(
			long id,
			long version) {
		super(id, version);
	}

}
