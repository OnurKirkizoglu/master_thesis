package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoOwner;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "OwnerProvider")
public class PojoOwnerProvider extends PojoProvider {
  private PojoOwner owner;
  
  public PojoOwnerProvider() {
  }
  
  public PojoOwnerProvider(PojoOwner owner) {
    this.owner = owner;
  }
  
  public PojoOwner getOwner() {
    return owner;
  }
  
  public void setOwner(PojoOwner owner) {
    this.owner = owner;
  }
  
}
