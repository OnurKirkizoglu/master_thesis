package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "CollectionArtifact")
public class PojoCollectionArtifact
		extends PojoArtifact {

	public PojoCollectionArtifact() {
		super();
	}

	public PojoCollectionArtifact(
			long id,
			long version) {
		super(id, version);
	}
}
