package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoOwnerToolStringLong;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "WorkspaceWithFilterProvider")
public class PojoWorkspaceWithFilterProvider extends PojoProvider {
  private PojoOwnerToolStringLong workspace;
  private PojoArtifact[] filter;
  
  public PojoWorkspaceWithFilterProvider() {
  }
  
  public PojoWorkspaceWithFilterProvider(PojoOwnerToolStringLong workspace, PojoArtifact[] filter) {
    this.workspace = workspace;
    this.filter = filter;
  }
  
  public PojoOwnerToolStringLong getWorkspace() {
    return workspace;
  }
  
  public void setWorkspace(PojoOwnerToolStringLong workspace) {
    this.workspace = workspace;
  }
  
  public PojoArtifact[] getFilter() {
    return filter;
  }
  
  public void setFilter(PojoArtifact[] filter) {
    this.filter = filter;
  }
  
}
