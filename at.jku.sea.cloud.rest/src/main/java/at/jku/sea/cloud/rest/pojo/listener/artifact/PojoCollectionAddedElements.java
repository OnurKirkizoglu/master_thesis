package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "CollectionAddedElements")
public class PojoCollectionAddedElements
		extends PojoArtifactChange {

	private PojoObject[] values;

	public PojoCollectionAddedElements() {}

	public PojoCollectionAddedElements(long origin,
			PojoArtifact artifact,
			PojoObject[] values) {
		super(origin, artifact);
		this.values = values;
	}

	public PojoObject[] getValues() {
		return values;
	}

	public void setValues(
			PojoObject[] values) {
		this.values = values;
	}
}
