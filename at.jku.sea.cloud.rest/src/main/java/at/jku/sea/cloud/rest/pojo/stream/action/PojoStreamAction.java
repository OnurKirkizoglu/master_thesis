package at.jku.sea.cloud.rest.pojo.stream.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "StreamAction")
@JsonSubTypes({ @Type(value = PojoFilterAction.class), @Type(value = PojoMapAction.class) })
public abstract class PojoStreamAction {
  private String context;
  
  public PojoStreamAction() {
  }
  
  public PojoStreamAction(String context) {
    this.context = context;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
}
