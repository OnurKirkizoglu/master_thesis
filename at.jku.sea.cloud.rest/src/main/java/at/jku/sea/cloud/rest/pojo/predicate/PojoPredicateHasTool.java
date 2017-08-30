package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoTool;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "HasToolPredicate")
public class PojoPredicateHasTool extends PojoPredicate {
  private String context;
  private PojoTool tool;
  
  public PojoPredicateHasTool() {
  }
  
  public PojoPredicateHasTool(String context, PojoTool tool) {
    this.context = context;
    this.tool = tool;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public PojoTool getTool() {
    return tool;
  }
  
  public void setTool(PojoTool tool) {
    this.tool = tool;
  }
  
}
