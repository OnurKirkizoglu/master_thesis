package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoContainer;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ContainerWithFilterProvider")
public class PojoContainerWithFilterProvider extends PojoProvider {
  private PojoContainer container;
  private PojoArtifact[] filter;
  
  public PojoContainerWithFilterProvider() {
  }
  
  public PojoContainerWithFilterProvider(PojoContainer container, PojoArtifact[] filter) {
    this.container = container;
    this.filter = filter;
  }
  
  public PojoContainer getContainer() {
    return container;
  }
  
  public void setContainer(PojoContainer container) {
    this.container = container;
  }
  
  public PojoArtifact[] getFilter() {
    return filter;
  }
  
  public void setFilter(PojoArtifact[] filter) {
    this.filter = filter;
  }
  
}
