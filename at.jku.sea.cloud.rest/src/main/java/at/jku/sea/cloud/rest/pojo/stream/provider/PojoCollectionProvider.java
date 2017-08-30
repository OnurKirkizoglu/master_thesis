package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoObject;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "CollectionProvider")
public class PojoCollectionProvider extends PojoProvider {
  private PojoObject[] objects;
  
  public PojoCollectionProvider() {
  }
  
  public PojoCollectionProvider(PojoObject[] objects) {
    this.objects = objects;
  }
  
  public PojoObject[] getObjects() {
    return objects;
  }
  
  public void setObjects(PojoObject[] objects) {
    this.objects = objects;
  }
  
}
