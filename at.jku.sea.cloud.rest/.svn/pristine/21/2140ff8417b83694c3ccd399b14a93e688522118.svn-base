package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "IsAlivePredicate")
public class PojoPredicateIsAlive extends PojoPredicate {
  private String context;
  
  public PojoPredicateIsAlive() {
  }
  
  public PojoPredicateIsAlive(String context) {
    this.context = context;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
}
