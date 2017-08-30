package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "HasTypePredicate")
public class PojoPredicateHasType extends PojoPredicate {
  private String context;
  private PojoArtifact type;
  
  public PojoPredicateHasType() {
  }
  
  public PojoPredicateHasType(String context, PojoArtifact type) {
    this.context = context;
    this.type = type;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public PojoArtifact getType() {
    return type;
  }
  
  public void setType(PojoArtifact type) {
    this.type = type;
  }
  
}
