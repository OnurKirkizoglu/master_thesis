package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "CollectionAddedElement")
public class PojoCollectionAddedElement
		extends PojoArtifactChange {

	private PojoObject value;

	public PojoCollectionAddedElement() {}

	public PojoCollectionAddedElement(long origin,
			PojoArtifact artifact,
			PojoObject value) {
		super(origin, artifact);
		this.value = value;
	}

	public PojoObject getValue() {
		return value;
	}

	public void setValue(
			PojoObject value) {
		this.value = value;
	}
}
