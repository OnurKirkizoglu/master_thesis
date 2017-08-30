package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoProject;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ProjectProvider")
public class PojoProjectProvider extends PojoProvider {
  private PojoProject project;
  
  public PojoProjectProvider() {
  }
  
  public PojoProjectProvider(PojoProject project) {
    this.project = project;
  }
  
  public PojoProject getProject() {
    return project;
  }
  
  public void setProject(PojoProject project) {
    this.project = project;
  }
  
}
