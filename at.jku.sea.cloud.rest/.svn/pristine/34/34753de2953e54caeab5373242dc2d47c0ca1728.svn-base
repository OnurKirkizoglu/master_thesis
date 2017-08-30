package at.jku.sea.cloud.rest.pojo.navigator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "StreamNavigator")
public class PojoStreamNavigator extends PojoNavigator {
  private String context;
  
  public PojoStreamNavigator() {
  }
  
  public PojoStreamNavigator(String context, PojoAccess[] access) {
    super(access);
    this.context = context;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
}
