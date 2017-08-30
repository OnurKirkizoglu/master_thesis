package at.jku.sea.cloud.rest.client.stream;

import at.jku.sea.cloud.navigator.NavigatorProvider;
import at.jku.sea.cloud.rest.client.navigator.RestNavigatorProvider;
import at.jku.sea.cloud.rest.client.stream.predicate.RestPredicateProvider;
import at.jku.sea.cloud.stream.QueryFactory;
import at.jku.sea.cloud.stream.StreamProvider;
import at.jku.sea.cloud.stream.predicate.PredicateProvider;

public class RestQueryFactory implements QueryFactory {

  private static final StreamProvider STREAM_PROVIDER = new RestStreamProvider();
  private static final PredicateProvider PREDICATE_PROVIDER = new RestPredicateProvider();
  private static final NavigatorProvider NAVIGATOR_PROVIDER = new RestNavigatorProvider();

  private static final QueryFactory INSTANCE = new RestQueryFactory();

  private RestQueryFactory() {
  }

  public static QueryFactory getInstance() {
    return INSTANCE;
  }

  @Override
  public StreamProvider streamProvider() {
    return STREAM_PROVIDER;
  }

  @Override
  public PredicateProvider predicateProvider() {
    return PREDICATE_PROVIDER;
  }

  @Override
  public NavigatorProvider navigatorProvider() {
    return NAVIGATOR_PROVIDER;
  }

}
