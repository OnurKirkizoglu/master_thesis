package at.jku.sea.cloud.rest.client.stream.predicate.implementation;

import at.jku.sea.cloud.rest.client.handler.util.Stream2PojoFactory;
import at.jku.sea.cloud.rest.client.stream.RestStream;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateAllMatch;

public class PredicateAllMatch<T> extends PredicateStreamPredicate<T> {

  public PredicateAllMatch(RestStream<T> stream, String context, RestPredicate<T> predicate) {
    super(stream, context, predicate);
  }

  @Override
  public PojoPredicate map(PojoFactory pojoFactory) {
    return new PojoPredicateAllMatch(Stream2PojoFactory.map(pojoFactory, getStream()), getContext(),
        getPredicate().map(pojoFactory));
  }

}
