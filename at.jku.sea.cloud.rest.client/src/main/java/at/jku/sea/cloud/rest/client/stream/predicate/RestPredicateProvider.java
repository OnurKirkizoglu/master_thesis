package at.jku.sea.cloud.rest.client.stream.predicate;

import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateContainsArtifact;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateContainsKey;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateContainsValue;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateEq;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateEqContext;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateExistsElement;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateHasId;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateHasOwner;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateHasProperty;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateHasPropertyValue;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateHasTool;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateHasType;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateHasVersion;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateInContainer;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateInv;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateIsAlive;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.PredicateIsPropertyAlive;
import at.jku.sea.cloud.stream.Context;
import at.jku.sea.cloud.stream.predicate.Predicate;
import at.jku.sea.cloud.stream.predicate.PredicateProvider;

public class RestPredicateProvider implements PredicateProvider {

  @Override
  public <T> Predicate<T> eq(final Context.Path cp, final T t) {
    Objects.requireNonNull(cp);
    return new PredicateEq<T>(cp.get(), t);
  }

  @Override
  public <T> Predicate<T> eq(final Context.Path cp1, final Context.Path cp2) {
    Objects.requireNonNull(cp1);
    Objects.requireNonNull(cp2);
    return new PredicateEqContext<T>(cp1.get(), cp2.get());
  }

  @Override
  public <T> Predicate<T> inv(final Predicate<T> p) {
    Objects.requireNonNull(p);
    return new PredicateInv<T>(p);
  }

  @Override
  public <T extends Artifact> Predicate<T> hasProperty(final Context.Path cp, final String property) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(property);

    return new PredicateHasProperty<T>(cp.get(), property);
  }

  @Override
  public <T extends Artifact> Predicate<T> hasPropertyValue(final Context.Path cp, final String name,
      final Object value) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(name);

    return new PredicateHasPropertyValue<T>(cp.get(), name, value);
  }

  @Override
  public <T extends Artifact> Predicate<T> hasId(final Context.Path cp, final long id) {
    Objects.requireNonNull(cp);

    return new PredicateHasId<T>(cp.get(), id);
  }

  @Override
  public <T extends Artifact> Predicate<T> hasVersion(final Context.Path cp, final long version) {
    Objects.requireNonNull(cp);

    return new PredicateHasVersion<T>(cp.get(), version);
  }

  @Override
  public <T extends Artifact> Predicate<T> hasOwner(final Context.Path cp, final Owner o) {
    Objects.requireNonNull(cp);

    return new PredicateHasOwner<T>(cp.get(), o);
  }

  @Override
  public <T extends Artifact> Predicate<T> hasTool(final Context.Path cp, final Tool t) {
    Objects.requireNonNull(cp);

    return new PredicateHasTool<T>(cp.get(), t);
  }

  @Override
  public <T extends Artifact> Predicate<T> hasType(final Context.Path cp, final Artifact type) {
    Objects.requireNonNull(cp);

    return new PredicateHasType<T>(cp.get(), type);
  }

  @Override
  public <T extends Artifact> Predicate<T> inContainer(final Context.Path cp, final Container c) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(c);

    return new PredicateInContainer<T>(cp.get(), c);
  }

  @Override
  public <T extends Artifact> Predicate<T> isAlive(final Context.Path cp) {
    Objects.requireNonNull(cp);

    return new PredicateIsAlive<T>(cp.get());
  }

  @Override
  public <T extends Artifact> Predicate<T> isPropertyAlive(final Context.Path cp, final String property) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(property);

    return new PredicateIsPropertyAlive<T>(cp.get(), property);
  }

  @Override
  public <T extends CollectionArtifact> Predicate<T> existsElement(final Context.Path cp, final Object o) {
    Objects.requireNonNull(cp);

    return new PredicateExistsElement<T>(cp.get(), o);
  }

  @Override
  public <T extends Container> Predicate<T> containsArtifact(final Context.Path cp, final Artifact a) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(a);

    return new PredicateContainsArtifact<T>(cp.get(), a);
  }

  @Override
  public <T extends MapArtifact> Predicate<T> containsKey(final Context.Path cp, final Object key) {
    Objects.requireNonNull(cp);

    return new PredicateContainsKey<T>(cp.get(), key);
  }

  @Override
  public <T extends MapArtifact> Predicate<T> containsValue(final Context.Path cp, final Object value) {
    Objects.requireNonNull(cp);

    return new PredicateContainsValue<T>(cp.get(), value);
  }
}