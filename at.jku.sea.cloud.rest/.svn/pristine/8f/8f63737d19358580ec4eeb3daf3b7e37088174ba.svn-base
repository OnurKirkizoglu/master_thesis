package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "IsPropertyAlivePredicate")
public class PojoPredicateIsPropertyAlive extends PojoPredicate {
  private String context;
  private String property;
  
  public PojoPredicateIsPropertyAlive() {
  }
  
  public PojoPredicateIsPropertyAlive(String context, String property) {
    this.context = context;
    this.property = property;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public String getProperty() {
    return property;
  }
  
  public void setProperty(String property) {
    this.property = property;
  }
  
}
