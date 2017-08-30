package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "Container")
@JsonSubTypes({
	@Type(value = PojoPackage.class),
	@Type(value = PojoResource.class)
})
public class PojoContainer extends PojoArtifact {
	public PojoContainer() {
		super();
	}

	public PojoContainer(long id, long version) {
		super(id, version);
	}
}
