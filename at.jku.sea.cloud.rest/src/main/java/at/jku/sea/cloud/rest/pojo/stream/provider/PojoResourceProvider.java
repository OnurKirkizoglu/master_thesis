package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoResource;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ResourceProvider")
public class PojoResourceProvider extends PojoProvider {
  private PojoResource resource;

  public PojoResourceProvider() {
  }

  public PojoResourceProvider(PojoResource resource) {
    this.resource = resource;
  }

  public PojoResource getResource() {
    return resource;
  }

  public void setResource(PojoResource resource) {
    this.resource = resource;
  }

}
