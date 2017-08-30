package at.jku.sea.cloud.stream.predicate;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.stream.Context;
import at.jku.sea.cloud.stream.Stream;

/**
 * Predicates provide some standard predicates for {@link Stream}.
 * 
 * In addition this implementations of {@link Predicate} are guaranteed to be implemented in the REST client.
 * 
 * @author Florian Weger
 */
public interface PredicateProvider {

  /**
   * Wrap in StandardPredicates
   */
  <T> Predicate<T> eq(final Context.Path cp, final T t);

  <T> Predicate<T> eq(final Context.Path cp1, final Context.Path cp2);

  <T> Predicate<T> inv(final Predicate<T> p);

  /**
   * Wrap in ArtifactPredicates
   */
  <T extends Artifact> Predicate<T> hasPropertyValue(final Context.Path cp, final String name, final Object value);

  <T extends Artifact> Predicate<T> hasProperty(final Context.Path cp, final String property);

  <T extends Artifact> Predicate<T> hasId(final Context.Path cp, final long id);

  <T extends Artifact> Predicate<T> hasVersion(final Context.Path cp, final long version);

  <T extends Artifact> Predicate<T> hasOwner(final Context.Path cp, final Owner o);

  <T extends Artifact> Predicate<T> hasTool(final Context.Path cp, final Tool t);

  <T extends Artifact> Predicate<T> hasType(final Context.Path cp, final Artifact type);

  <T extends Artifact> Predicate<T> inContainer(final Context.Path cp, final Container p);

  <T extends Artifact> Predicate<T> isAlive(final Context.Path cp);

  <T extends Artifact> Predicate<T> isPropertyAlive(final Context.Path cp, final String property);

  /**
   * Wrap in CollectionPredicates
   */
  <T extends CollectionArtifact> Predicate<T> existsElement(final Context.Path cp, final Object o);

  /**
   * Wrap in ContainerPredicates
   */
  <T extends Container> Predicate<T> containsArtifact(final Context.Path cp, final Artifact a);

  /**
   * Wrap in MapArtifactPredicates
   */
  <T extends MapArtifact> Predicate<T> containsKey(final Context.Path cp, final Object key);

  <T extends MapArtifact> Predicate<T> containsValue(final Context.Path cp, final Object value);
}
