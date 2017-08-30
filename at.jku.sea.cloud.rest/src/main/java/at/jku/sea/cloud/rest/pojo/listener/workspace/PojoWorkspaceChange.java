package at.jku.sea.cloud.rest.pojo.listener.workspace;

import at.jku.sea.cloud.rest.pojo.PojoWorkspace;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "WorkspaceChange")
@JsonSubTypes({
	@Type(value = PojoVersionCommited.class),
	@Type(value = PojoWorkspaceAdded.class),
	@Type(value = PojoWorkspaceClosed.class),
	@Type(value = PojoWorkspaceRebased.class),
	@Type(value = PojoWorkspaceRollBack.class),
	@Type(value = PojoWorkspaceParentSet.class)
})
public class PojoWorkspaceChange
		extends PojoChange {

	private PojoWorkspace workspace;

	public PojoWorkspaceChange() {}

	public PojoWorkspaceChange(
			PojoWorkspace workspace) {
		this.workspace = workspace;
	}

	public PojoWorkspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(
			PojoWorkspace workspace) {
		this.workspace = workspace;
	}
}
