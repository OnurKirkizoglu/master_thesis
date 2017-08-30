package at.jku.sea.cloud.rest.pojo.listener.workspace;

import at.jku.sea.cloud.rest.pojo.PojoWorkspace;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "WorkspaceAdded")
public class PojoWorkspaceAdded
		extends PojoWorkspaceChange {

	public PojoWorkspaceAdded() {}

	public PojoWorkspaceAdded(
			PojoWorkspace workspace) {
		super(workspace);
	}
}
