package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.stream.PojoStream;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "StreamAllMatchPredicate")
public class PojoPredicateAllMatch extends PojoPredicateStreamPredicate {
  
  public PojoPredicateAllMatch() {
  }
  
  public PojoPredicateAllMatch(PojoStream stream, String context, PojoPredicate predicate) {
    super(stream, context, predicate);
  }
  
}
