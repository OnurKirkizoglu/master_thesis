package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoObject;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "EqPredicate")
public class PojoPredicateEq extends PojoPredicate {
  private String context;
  private PojoObject object;
  
  public PojoPredicateEq() {
  }
  
  public PojoPredicateEq(String context, PojoObject object) {
    this.context = context;
    this.object = object;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public PojoObject getObject() {
    return object;
  }
  
  public void setObject(PojoObject object) {
    this.object = object;
  }
  
}
