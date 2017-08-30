package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoContainer;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ContainerProvider")
public class PojoContainerProvider extends PojoProvider {
  private PojoContainer container;
  
  public PojoContainerProvider() {
  }
  
  public PojoContainerProvider(PojoContainer container) {
    this.container = container;
  }
  
  public PojoContainer getContainer() {
    return container;
  }
  
  public void setContainer(PojoContainer container) {
    this.container = container;
  }
  
}
