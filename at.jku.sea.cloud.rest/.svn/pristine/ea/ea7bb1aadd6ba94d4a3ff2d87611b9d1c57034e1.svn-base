package at.jku.sea.cloud.rest.pojo.stream;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoStreamAction;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "MatchStream")
public class PojoMatchStream extends PojoStream {
  private String context;
  private PojoPredicate predicate;
  
  public PojoMatchStream() {
  }
  
  public PojoMatchStream(PojoProvider provider, PojoStreamAction[] actions, String context, PojoPredicate predicate) {
    super(provider, actions);
    this.context = context;
    this.predicate = predicate;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public PojoPredicate getPredicate() {
    return predicate;
  }
  
  public void setPredicate(PojoPredicate predicate) {
    this.predicate = predicate;
  }
}
