package at.jku.sea.cloud.rest.pojo.listener;

import at.jku.sea.cloud.rest.pojo.listener.artifact.PojoArtifactChange;
import at.jku.sea.cloud.rest.pojo.listener.collection.PojoCollectionChange;
import at.jku.sea.cloud.rest.pojo.listener.property.PojoPropertyChange;
import at.jku.sea.cloud.rest.pojo.listener.workspace.PojoWorkspaceChange;

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
@JsonTypeName(value = "Change")
@JsonSubTypes({
	@Type(value = PojoArtifactChange.class),
	@Type(value = PojoPropertyChange.class),
	@Type(value = PojoWorkspaceChange.class),
	@Type(value = PojoCollectionChange.class)
})
public class PojoChange {
	public PojoChange() {}
}
