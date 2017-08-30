package at.jku.sea.cloud.rest.pojo.stream.action;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "StreamFilterAction")
public class PojoFilterAction extends PojoStreamAction {
  private PojoPredicate predicate;
  
  public PojoFilterAction() {
  }
  
  public PojoFilterAction(String context, PojoPredicate predicate) {
    super(context);
    this.predicate = predicate;
  }
  
  public PojoPredicate getPredicate() {
    return predicate;
  }
  
  public void setPredicate(PojoPredicate predicate) {
    this.predicate = predicate;
  }
  
}
