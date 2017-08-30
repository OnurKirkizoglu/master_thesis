package at.jku.sea.cloud.rest.pojo.navigator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ToolAccess")
public class PojoToolAccess extends PojoAccess {
  
  public PojoToolAccess() {
  }
  
}
