package at.jku.sea.cloud.stream;

import at.jku.sea.cloud.navigator.NavigatorProvider;
import at.jku.sea.cloud.stream.predicate.PredicateProvider;

/**
 * Starting point for the query language.
 * <p>
 * Purpose of this interface is to hide implementation details to use either local or RESTful implementation.
 * 
 * @author Florian Weger
 */
public interface QueryFactory {
  /**
   * Returns an implementation of the {@code StreamProvider} depending on the implementation of the {@code Cloud}.
   * 
   * @return {@code StreamProvider} implementation
   */
  StreamProvider streamProvider();

  /**
   * Returns an implementation of the {@code PredicateProvider} depending on the implementation of the {@code Cloud}.
   * 
   * @return {@code PredicateProvider} implementation
   */
  PredicateProvider predicateProvider();

  /**
   * Returns an implementation of the {@code NavigatorProvider} depending on the implementation of the {@code Cloud}.
   * 
   * @return {@code NavigatorProvider} implementation
   */
  NavigatorProvider navigatorProvider();
}
