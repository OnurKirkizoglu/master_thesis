package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoContainer;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "InContainerPredicate")
public class PojoPredicateInContainer extends PojoPredicate {
  private String context;
  private PojoContainer container;
  
  public PojoPredicateInContainer() {
  }
  
  public PojoPredicateInContainer(String context, PojoContainer pojoContainer) {
    this.context = context;
    this.container = pojoContainer;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public PojoContainer getContainer() {
    return container;
  }
  
  public void setPckg(PojoContainer container) {
    this.container = container;
  }
  
}
