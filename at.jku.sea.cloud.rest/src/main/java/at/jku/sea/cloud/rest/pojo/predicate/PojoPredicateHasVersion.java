package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "HasVersionPredicate")
public class PojoPredicateHasVersion extends PojoPredicate {
  private String context;
  private Long version;
  
  public PojoPredicateHasVersion() {
  }
  
  public PojoPredicateHasVersion(String context, Long version) {
    this.context = context;
    this.version = version;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public Long getVersion() {
    return version;
  }
  
  public void setVersion(Long version) {
    this.version = version;
  }
  
}
