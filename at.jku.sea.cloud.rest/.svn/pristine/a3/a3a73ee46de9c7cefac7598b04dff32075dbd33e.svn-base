package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "Resource")
public class PojoResource extends PojoContainer {
	public PojoResource() {
		super();
	}

	public PojoResource(long id, long version) {
		super(id, version);
	}
}
