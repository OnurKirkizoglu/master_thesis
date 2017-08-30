package at.jku.sea.cloud.stream;

import at.jku.sea.cloud.navigator.NavigatorProvider;
import at.jku.sea.cloud.navigator.implementation.NavigatorProviderImpl;
import at.jku.sea.cloud.stream.predicate.PredicateProvider;
import at.jku.sea.cloud.stream.predicate.PredicateProviderImpl;

public final class QueryFactoryImpl implements QueryFactory {
  private static final StreamProvider STREAM_PROVIDER = new StreamProviderImpl();
  private static final PredicateProvider PREDICATE_PROVIDER = new PredicateProviderImpl();
  private static final NavigatorProvider NAVIGATOR_PROVIDER = new NavigatorProviderImpl();

  private static final QueryFactory INSTANCE = new QueryFactoryImpl();

  private QueryFactoryImpl() {
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
