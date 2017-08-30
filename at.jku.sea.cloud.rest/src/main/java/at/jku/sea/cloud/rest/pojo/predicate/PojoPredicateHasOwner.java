package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoOwner;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "HasOwnerPredicate")
public class PojoPredicateHasOwner extends PojoPredicate {
  private String context;
  private PojoOwner owner;
  
  public PojoPredicateHasOwner() {
  }
  
  public PojoPredicateHasOwner(String context, PojoOwner owner) {
    this.context = context;
    this.owner = owner;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public PojoOwner getOwner() {
    return owner;
  }
  
  public void setOwner(PojoOwner owner) {
    this.owner = owner;
  }
  
}
