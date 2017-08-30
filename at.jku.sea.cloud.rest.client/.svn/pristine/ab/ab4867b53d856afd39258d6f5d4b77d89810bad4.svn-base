package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateContainsArtifact;

public class PredicateContainsArtifact<T> extends RestPredicate<T> {

  private final String context;
  private final Artifact a;

  public PredicateContainsArtifact(String context, Artifact a) {
    this.context = context;
    this.a = a;
  }

  public String getContext() {
    return context;
  }

  public Artifact getArtifact() {
    return a;
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateContainsArtifact(context, pojoFactory.createPojo(a));
  }

}
