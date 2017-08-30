package at.jku.sea.cloud.rest.client.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.rest.client.handler.stream.StreamHandler;
import at.jku.sea.cloud.rest.client.navigator.RestNavigator;
import at.jku.sea.cloud.rest.client.stream.actions.FilterAction;
import at.jku.sea.cloud.rest.client.stream.actions.MapAction;
import at.jku.sea.cloud.rest.client.stream.actions.StreamAction;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.RestPredicate;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.stream.Stream;
import at.jku.sea.cloud.stream.predicate.Predicate;
import at.jku.sea.cloud.stream.predicate.PredicateStream;
import at.jku.sea.cloud.stream.provider.Provider;

public class RestStream<T> implements Stream<T> {

  private final StreamHandler handler;
  private final RestProvider<T> provider;
  private final List<StreamAction> actions;

  public RestStream(Provider<T> provider) {
    if (provider instanceof RestProvider) {
      this.provider = (RestProvider<T>) provider;
    } else {
      throw new IllegalArgumentException("Provider must be implemented in the REST service.");
    }
    handler = new StreamHandler();
    this.actions = new ArrayList<>();
  }

  @Override
  public Stream<T> filter(String context, Predicate<T> p) {
    if (p instanceof RestPredicate) {
      actions.add(new FilterAction<T>(context, (RestPredicate<T>) p));
      return this;
    } else {
      throw new IllegalArgumentException("Predicate must be implemented in the REST service.");
    }
  }

  @Override
  public <U> Stream<U> map(String context, TerminalOperation<U> navigator) {
    if (navigator instanceof RestNavigator) {
      actions.add(new MapAction<T, U>(context, (RestNavigator<U>) navigator));
    } else {
      throw new IllegalArgumentException("Navigator must be implemented in the REST service.");
    }
    @SuppressWarnings("unchecked")
    Stream<U> result = (Stream<U>) this;
    return result;
  }

  @Override
  public PredicateStream<T> asPredicate() {
    return new RestPredicateStream<T>(this);
  }

  @Override
  public T find(String context, Predicate<T> p) throws NoSuchElementException {
    if (p instanceof RestPredicate) {
      return handler.find(this, context, (RestPredicate<T>) p);
    } else {
      throw new IllegalArgumentException("Predicate must be implemented in the REST service.");
    }
  }

  @Override
  public boolean anyMatch(String context, Predicate<T> p) {
    if (p instanceof RestPredicate) {
      return handler.anyMatch(this, context, (RestPredicate<T>) p);
    } else {
      throw new IllegalArgumentException("Predicate must be implemented in the REST service.");
    }
  }

  @Override
  public boolean allMatch(String context, Predicate<T> p) {
    if (p instanceof RestPredicate) {
      return handler.allMatch(this, context, (RestPredicate<T>) p);
    } else {
      throw new IllegalArgumentException("Predicate must be implemented in the REST service.");
    }
  }

  @Override
  public boolean noneMatch(String context, Predicate<T> p) {
    if (p instanceof RestPredicate) {
      return handler.noneMatch(this, context, (RestPredicate<T>) p);
    } else {
      throw new IllegalArgumentException("Predicate must be implemented in the REST service.");
    }
  }

  @Override
  public List<T> toList() {
    return handler.toList(this);
  }

  @Override
  public long count() {
    return handler.count(this);
  }

  public RestProvider<T> getProvider() {
    return provider;
  }

  public List<StreamAction> getActions() {
    return actions;
  }
}
