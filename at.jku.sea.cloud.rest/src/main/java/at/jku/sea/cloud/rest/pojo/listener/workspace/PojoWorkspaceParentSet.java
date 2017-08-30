package at.jku.sea.cloud.rest.pojo.listener.workspace;

import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoWorkspace;

@JsonTypeName(value = "WorkspaceParentSet")
public class PojoWorkspaceParentSet extends PojoWorkspaceChange {
	private PojoWorkspace parent;

	public PojoWorkspaceParentSet() {}
	
	public PojoWorkspaceParentSet(PojoWorkspace workspace, PojoWorkspace parent) {
		super(workspace);
		this.parent = parent;
	}

	public PojoWorkspace getParent() {
		return parent;
	}
}
