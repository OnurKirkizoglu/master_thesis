package at.jku.sea.cloud.rest.client.stream;

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
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.CollectionArtifactOnlyArtifactProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.CollectionArtifactProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.CollectionProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.ContainerProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.ContainerWithFilterProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.MapArtifactKeyProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.MapArtifactValueProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.MetaModelProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.OwnerProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.ProjectPackageProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.ProjectProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.ResourceProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.ToolProvider;
import at.jku.sea.cloud.rest.client.stream.provider.implementation.WorkspaceWithFilterProvider;
import at.jku.sea.cloud.stream.Stream;
import at.jku.sea.cloud.stream.StreamProvider;
import at.jku.sea.cloud.utils.CloudUtils;

public class RestStreamProvider implements StreamProvider {

  /**
   * Wrapper
   */
  @Override
  public <T> Stream<T> of(final Collection<T> collection) {
    Objects.requireNonNull(collection);
    for (T elem : collection) {
      if (!CloudUtils.isSupportedType(elem)) {
        throw new TypeNotSupportedException(elem.getClass());
      }
    }

    return createStream(new CollectionProvider<>(collection));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Stream<T> of(final T... array) {
    Objects.requireNonNull(array);

    return of(Arrays.asList(array));
  }

  /**
   * Workspace wrapper
   */

  @Override
  public Stream<Artifact> of(final Workspace ws, final Artifact... filters) {
    Objects.requireNonNull(ws);

    return createStream(new WorkspaceWithFilterProvider(ws, filters));
  }

  /**
   * MapArtifact wrapper
   */

  @Override
  public Stream<Object> keys(final MapArtifact map) {
    Objects.requireNonNull(map);

    return createStream(new MapArtifactKeyProvider(map));
  }

  @Override
  public Stream<Object> values(final MapArtifact map) {
    Objects.requireNonNull(map);

    return createStream(new MapArtifactValueProvider(map));
  }

  /**
   * CollectionArtifact wrapper
   */

  @Override
  public Stream<Artifact> onlyArtifacts(final CollectionArtifact collectionArtifact) {
    Objects.requireNonNull(collectionArtifact);

    return createStream(new CollectionArtifactOnlyArtifactProvider(collectionArtifact));
  }

  @Override
  public Stream<Object> of(final CollectionArtifact collectionArtifact) {
    Objects.requireNonNull(collectionArtifact);

    return createStream(new CollectionArtifactProvider(collectionArtifact));
  }

  /**
   * Container wrapper
   */

  @Override
  public Stream<Artifact> of(final Container container) {
    Objects.requireNonNull(container);

    return createStream(new ContainerProvider(container));
  }

  @Override
  public Stream<Artifact> of(final Container container, final Filter filter) {
    Objects.requireNonNull(container);
    Objects.requireNonNull(filter);

    return createStream(new ContainerWithFilterProvider(container, filter));
  }

  /**
   * Project wrapper
   */

  @Override
  public Stream<Artifact> of(final Project project) {
    Objects.requireNonNull(project);

    return createStream(new ProjectProvider(project));
  }

  @Override
  public Stream<Package> packages(final Project project) {
    Objects.requireNonNull(project);

    return createStream(new ProjectPackageProvider(project));
  }

  /**
   * MetaModel wrapper
   */

  @Override
  public Stream<Artifact> of(final MetaModel metaModel) {
    Objects.requireNonNull(metaModel);

    return createStream(new MetaModelProvider(metaModel));
  }

  /**
   * Owner wrapper
   */

  @Override
  public Stream<Artifact> of(final Owner owner) {
    Objects.requireNonNull(owner);

    return createStream(new OwnerProvider(owner));
  }

  /**
   * Tool wrapper
   */

  @Override
  public Stream<Artifact> of(final Tool tool) {
    Objects.requireNonNull(tool);

    return createStream(new ToolProvider(tool));
  }

  @Override
  public Stream<Artifact> of(Resource resource) {
    Objects.requireNonNull(resource);

    return createStream(new ResourceProvider(resource));
  }

  private <T> Stream<T> createStream(RestProvider<T> provider) {
    return new RestStream<T>(provider);
  }
}
