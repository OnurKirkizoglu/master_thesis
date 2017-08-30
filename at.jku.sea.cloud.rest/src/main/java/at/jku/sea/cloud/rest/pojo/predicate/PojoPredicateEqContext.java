package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "EqContextPredicate")
public class PojoPredicateEqContext extends PojoPredicate {
  private String context1;
  private String context2;
  
  public PojoPredicateEqContext() {
  }
  
  public PojoPredicateEqContext(String context1, String context2) {
    this.context1 = context1;
    this.context2 = context2;
  }
  
  public String getContext1() {
    return context1;
  }
  
  public void setContext1(String context1) {
    this.context1 = context1;
  }
  
  public String getContext2() {
    return context2;
  }
  
  public void setContext2(String context2) {
    this.context2 = context2;
  }
  
}
