package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.stream.PojoStream;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "StreamPredicate")
@JsonSubTypes({ @Type(value = PojoPredicateAllMatch.class), @Type(value = PojoPredicateAnyMatch.class),
    @Type(value = PojoPredicateNoneMatch.class) })
public abstract class PojoPredicateStreamPredicate extends PojoPredicate {
  private PojoStream stream;
  private String context;
  private PojoPredicate predicate;
  
  public PojoPredicateStreamPredicate() {
  }
  
  public PojoPredicateStreamPredicate(PojoStream stream, String context, PojoPredicate predicate) {
    this.stream = stream;
    this.context = context;
    this.predicate = predicate;
  }
  
  public PojoStream getStream() {
    return stream;
  }
  
  public void setStream(PojoStream stream) {
    this.stream = stream;
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
