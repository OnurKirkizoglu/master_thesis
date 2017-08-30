package at.jku.sea.cloud.stream.predicate;

import java.util.Objects;

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
public class PredicateProviderImpl implements PredicateProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> Predicate<T> eq(final Context.Path cp, final T t) {
    Objects.requireNonNull(cp);
    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return Objects.equals(c.<T> get(cp.get()), t);
      }
    };
  }

  @Override
  public <T> Predicate<T> eq(final Context.Path cp1, final Context.Path cp2) {
    Objects.requireNonNull(cp1);
    Objects.requireNonNull(cp2);
    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return Objects.equals(c.<T> get(cp1.get()), c.<T> get(cp2.get()));
      }
    };
  }

  @Override
  public <T> Predicate<T> inv(final Predicate<T> p) {
    Objects.requireNonNull(p);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return !p.test(c);
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends Artifact> Predicate<T> hasPropertyValue(final Context.Path cp, final String name, final Object value) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(name);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return Objects.equals(c.<T> get(cp.get()).getPropertyValue(name), value);
      }
    };
  }

  @Override
  public <T extends Artifact> Predicate<T> hasProperty(final Context.Path cp, final String property) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(property);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return c.<T> get(cp.get()).hasProperty(property);
      }
    };
  }

  @Override
  public <T extends Artifact> Predicate<T> hasId(final Context.Path cp, final long id) {
    Objects.requireNonNull(cp);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return c.<T> get(cp.get()).getId() == id;
      }
    };
  }

  @Override
  public <T extends Artifact> Predicate<T> hasVersion(final Context.Path cp, final long version) {
    Objects.requireNonNull(cp);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return c.<T> get(cp.get()).getVersionNumber() == version;
      }
    };
  }

  @Override
  public <T extends Artifact> Predicate<T> hasOwner(final Context.Path cp, final Owner o) {
    Objects.requireNonNull(cp);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return Objects.equals(c.<T> get(cp.get()).getOwner(), o);
      }
    };
  }

  @Override
  public <T extends Artifact> Predicate<T> hasTool(final Context.Path cp, final Tool t) {
    Objects.requireNonNull(cp);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return Objects.equals(c.<T> get(cp.get()), t);
      }
    };
  }

  @Override
  public <T extends Artifact> Predicate<T> hasType(final Context.Path cp, final Artifact type) {
    Objects.requireNonNull(cp);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return Objects.equals(c.<T> get(cp.get()).getType(), type);
      }
    };
  }

  @Override
  public <T extends Artifact> Predicate<T> inContainer(final Context.Path cp, final Container p) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(p);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return p.containsArtifact(c.<T> get(cp.get()));
      }
    };
  }

  @Override
  public <T extends Artifact> Predicate<T> isAlive(final Context.Path cp) {
    Objects.requireNonNull(cp);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return c.<T> get(cp.get()).isAlive();
      }
    };
  }

  @Override
  public <T extends Artifact> Predicate<T> isPropertyAlive(final Context.Path cp, final String property) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(property);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return c.<T> get(cp.get()).isPropertyAlive(property);
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends CollectionArtifact> Predicate<T> existsElement(final Context.Path cp, final Object o) {
    Objects.requireNonNull(cp);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return c.<T> get(cp.get()).existsElement(o);
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends Container> Predicate<T> containsArtifact(final Context.Path cp, final Artifact a) {
    Objects.requireNonNull(cp);
    Objects.requireNonNull(a);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return c.<T> get(cp.get()).containsArtifact(a);
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends MapArtifact> Predicate<T> containsKey(final Context.Path cp, final Object key) {
    Objects.requireNonNull(cp);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return c.<T> get(cp.get()).containsKey(key);
      }
    };
  }

  @Override
  public <T extends MapArtifact> Predicate<T> containsValue(final Context.Path cp, final Object value) {
    Objects.requireNonNull(cp);

    return new AbstractPredicate<T>() {
      @Override
      public boolean test(Context c) {
        return c.<T> get(cp.get()).containsValue(value);
      }
    };
  }
}
