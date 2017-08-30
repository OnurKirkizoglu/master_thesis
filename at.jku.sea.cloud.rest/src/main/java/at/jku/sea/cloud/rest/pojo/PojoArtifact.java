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
@JsonTypeName(value = "Artifact")
@JsonSubTypes({
	@Type(value = PojoCollectionArtifact.class),
	@Type(value = PojoMapArtifact.class),
	@Type(value = PojoMetaModel.class),
	@Type(value = PojoOwner.class),
	@Type(value = PojoContainer.class),
	@Type(value = PojoProject.class),
	@Type(value = PojoTool.class)
})
public class PojoArtifact {

	private long id;
	private long version;

	public PojoArtifact() {}

	public PojoArtifact(
			long id,
			long version) {
		this.id = id;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(
			long id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(
			long version) {
		this.version = version;
	}
}
