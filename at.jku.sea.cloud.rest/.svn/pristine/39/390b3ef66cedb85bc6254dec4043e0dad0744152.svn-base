package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "InvPredicate")
public class PojoPredicateInv extends PojoPredicate {
  private PojoPredicate predicate;
  
  public PojoPredicateInv() {
  }
  
  public PojoPredicateInv(PojoPredicate predicate) {
    this.predicate = predicate;
  }
  
  public PojoPredicate getPredicate() {
    return predicate;
  }
  
  public void setPredicate(PojoPredicate predicate) {
    this.predicate = predicate;
  }
  
}
