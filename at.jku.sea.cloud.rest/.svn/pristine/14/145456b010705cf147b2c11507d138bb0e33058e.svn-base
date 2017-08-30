package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoTool;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ToolProvider")
public class PojoToolProvider extends PojoProvider {
  private PojoTool tool;
  
  public PojoToolProvider() {
  }
  
  public PojoToolProvider(PojoTool tool) {
    this.tool = tool;
  }
  
  public PojoTool getTool() {
    return tool;
  }
  
  public void setTool(PojoTool tool) {
    this.tool = tool;
  }
  
}
