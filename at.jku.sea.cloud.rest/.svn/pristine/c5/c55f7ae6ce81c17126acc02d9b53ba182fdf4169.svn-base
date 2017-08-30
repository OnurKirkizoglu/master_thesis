package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "OrPredicate")
public class PojoPredicateOr extends PojoPredicate {
  private PojoPredicate predicate1;
  private PojoPredicate predicate2;

  public PojoPredicateOr() {
  }

  public PojoPredicateOr(PojoPredicate predicate1, PojoPredicate predicate2) {
    this.predicate1 = predicate1;
    this.predicate2 = predicate2;
  }

  public PojoPredicate getPredicate1() {
    return predicate1;
  }

  public PojoPredicate getPredicate2() {
    return predicate2;
  }

  public void setPredicate1(PojoPredicate predicate1) {
    this.predicate1 = predicate1;
  }

  public void setPredicate2(PojoPredicate predicate2) {
    this.predicate2 = predicate2;
  }

}
