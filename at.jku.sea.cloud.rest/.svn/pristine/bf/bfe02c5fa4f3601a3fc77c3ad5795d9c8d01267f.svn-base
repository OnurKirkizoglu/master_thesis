package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "HasIdPredicate")
public class PojoPredicateHasId extends PojoPredicate {
  private String context;
  private Long id;
  
  public PojoPredicateHasId() {
  }
  
  public PojoPredicateHasId(String context, Long id) {
    this.context = context;
    this.id = id;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
}
