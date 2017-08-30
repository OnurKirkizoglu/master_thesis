package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ContainsArtifactPredicate")
public class PojoPredicateContainsArtifact extends PojoPredicate {
  private String context;
  private PojoArtifact artifact;
  
  public PojoPredicateContainsArtifact() {
  }
  
  public PojoPredicateContainsArtifact(String context, PojoArtifact artifact) {
    this.context = context;
    this.artifact = artifact;
  }
  
  public String getContext() {
    return context;
  }
  
  public void setContext(String context) {
    this.context = context;
  }
  
  public PojoArtifact getArtifact() {
    return artifact;
  }
  
  public void setArtifact(PojoArtifact artifact) {
    this.artifact = artifact;
  }
  
}
