package at.jku.sea.cloud.rest.pojo.navigator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "PropertyStringAccess")
public class PojoPropertyStringAccess extends PojoAccess {
  
  private String property;
  
  public PojoPropertyStringAccess() {
  }
  
  public PojoPropertyStringAccess(String property) {
    this.property = property;
  }
  
  public String getProperty() {
    return property;
  }
  
  public void setProperty(String property) {
    this.property = property;
  }
  
}
