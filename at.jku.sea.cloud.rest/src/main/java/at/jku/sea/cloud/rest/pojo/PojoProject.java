package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "Project")
public class PojoProject
		extends PojoArtifact {

	public PojoProject() {
		super();
	}

	public PojoProject(
			long id,
			long version) {
		super(id, version);
	}
}
