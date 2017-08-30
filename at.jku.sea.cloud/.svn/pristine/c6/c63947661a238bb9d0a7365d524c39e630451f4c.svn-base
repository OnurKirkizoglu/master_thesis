package at.jku.sea.cloud.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.Container.Filter;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.stream.provider.Provider;
import at.jku.sea.cloud.utils.CloudUtils;

/**
 * Used to set up a Stream. This class provides every possible supplier for a query.
 * 
 * @author Florian Weger
 */
public class StreamProviderImpl implements StreamProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> Stream<T> of(final Collection<T> collection) {
    Objects.requireNonNull(collection);

    for (T elem : collection) {
      if (!CloudUtils.isSupportedType(elem)) {
        throw new TypeNotSupportedException(elem.getClass());
      }
    }

    return new AbstractStream.HeadStream<>(new Provider<T>() {
      @Override
      public Iterable<T> get() {
        return collection;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> Stream<T> of(final T... array) {
    Objects.requireNonNull(array);

    return of(Arrays.asList(array));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Artifact> of(final Workspace ws, final Artifact... filters) {
    Objects.requireNonNull(ws);

    return new AbstractStream.HeadStream<>(new Provider<Artifact>() {
      @Override
      public Iterable<Artifact> get() {
        return filters == null ? ws.getArtifacts() : ws.getArtifacts(filters);
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Object> keys(final MapArtifact map) {
    Objects.requireNonNull(map);

    return new AbstractStream.HeadStream<>(new Provider<Object>() {
      @Override
      public Iterable<Object> get() {
        return map.keySet();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Object> values(final MapArtifact map) {
    Objects.requireNonNull(map);

    return new AbstractStream.HeadStream<>(new Provider<Object>() {
      @Override
      public Iterable<Object> get() {
        return map.values();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Artifact> onlyArtifacts(final CollectionArtifact collectionArtifact) {
    Objects.requireNonNull(collectionArtifact);

    return new AbstractStream.HeadStream<>(new Provider<Artifact>() {
      @Override
      public Iterable<Artifact> get() {
        @SuppressWarnings("unchecked")
        Collection<Artifact> wrap = (Collection<Artifact>) collectionArtifact.getElements();
        return wrap;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Object> of(final CollectionArtifact collectionArtifact) {
    Objects.requireNonNull(collectionArtifact);

    return new AbstractStream.HeadStream<>(new Provider<Object>() {
      @Override
      public Iterable<Object> get() {
        @SuppressWarnings("unchecked")
        Collection<Object> wrap = (Collection<Object>) collectionArtifact.getElements();
        return wrap;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Artifact> of(final Container container) {
    Objects.requireNonNull(container);

    return new AbstractStream.HeadStream<>(new Provider<Artifact>() {
      @Override
      public Iterable<Artifact> get() {
        return container.getArtifacts();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Artifact> of(final Resource resource) {
    Objects.requireNonNull(resource);

    return new AbstractStream.HeadStream<>(new Provider<Artifact>() {
      @Override
      public Iterable<Artifact> get() {
        return resource.getArtifacts();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Artifact> of(final Container container, final Filter filter) {
    Objects.requireNonNull(container);
    Objects.requireNonNull(filter);

    return new AbstractStream.HeadStream<>(new Provider<Artifact>() {
      @Override
      public Iterable<Artifact> get() {
        return container.getArtifacts(filter);
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Artifact> of(final Project project) {
    Objects.requireNonNull(project);

    return new AbstractStream.HeadStream<>(new Provider<Artifact>() {
      @Override
      public Iterable<Artifact> get() {
        return project.getArtifacts();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Package> packages(final Project project) {
    Objects.requireNonNull(project);

    return new AbstractStream.HeadStream<>(new Provider<Package>() {
      @Override
      public Iterable<Package> get() {
        return project.getPackages();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Artifact> of(final MetaModel metaModel) {
    Objects.requireNonNull(metaModel);

    return new AbstractStream.HeadStream<>(new Provider<Artifact>() {
      @Override
      public Iterable<Artifact> get() {
        return metaModel.getArtifacts();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Artifact> of(final Owner owner) {
    Objects.requireNonNull(owner);

    return new AbstractStream.HeadStream<>(new Provider<Artifact>() {
      @Override
      public Iterable<Artifact> get() {
        return owner.getArtifacts();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Artifact> of(final Tool tool) {
    Objects.requireNonNull(tool);

    return new AbstractStream.HeadStream<>(new Provider<Artifact>() {
      @Override
      public Iterable<Artifact> get() {
        return tool.getArtifacts();
      }
    });
  }
}
