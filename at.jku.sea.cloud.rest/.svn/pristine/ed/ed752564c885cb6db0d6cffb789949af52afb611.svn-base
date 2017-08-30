package at.jku.sea.cloud.rest.pojo.listener.artifact;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "ArtifactChange")
@JsonSubTypes({ @Type(value = PojoArtifactAliveSet.class), @Type(value = PojoArtifactCommited.class), @Type(value = PojoArtifactCreated.class), @Type(value = PojoArtifactContainerSet.class),
 @Type(value = PojoArtifactRollBack.class), @Type(value = PojoArtifactTypeSet.class),
    @Type(value = PojoCollectionAddedElement.class), @Type(value = PojoCollectionAddedElements.class), @Type(value = PojoCollectionRemovedElement.class),
	@Type(value = PojoMapCleared.class), @Type(value = PojoMapElementRemoved.class), @Type(value = PojoMapPut.class)})
public class PojoArtifactChange extends PojoChange {
	
	private PojoArtifact artifact;
	private long origin;
	
	public PojoArtifactChange() {
	}
	
	public PojoArtifactChange(long origin, PojoArtifact artifact) {
		this.artifact = artifact;
		this.setOrigin(origin);
	}
	
	public PojoArtifact getArtifact() {
		return artifact;
	}
	
	public void setArtifact(PojoArtifact artifact) {
		this.artifact = artifact;
	}
	
	public long getOrigin() {
		return origin;
	}
	
	public void setOrigin(long origin) {
		this.origin = origin;
	}
}
