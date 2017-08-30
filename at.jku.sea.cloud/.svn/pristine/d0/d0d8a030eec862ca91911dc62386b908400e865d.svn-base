package at.jku.sea.cloud.stream;

import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
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

/**
 * Used to set up a Stream. This class provides every possible supplier for a query.
 * 
 * @author Florian Weger
 */
public interface StreamProvider {
  /**
   * Wraps a collection into a Stream.
   * 
   * @param collection
   *          supplier for Stream
   * @return Stream which consist elements of {@code collection}
   * 
   * @throws TypeNotSupportedException
   *           if one element in the {@link Collection} is not allowed in the {@link Cloud}
   * @throws NullPointerException
   *           if <code>collection</code> is <code>null</code>
   */
  <T> Stream<T> of(final Collection<T> collection);

  /**
   * Wraps an array into a Stream.
   * 
   * @param array
   *          supplier for Stream
   * @return Stream which consist elements of array
   * @throws NullPointerException
   *           if <code>array</code> is <code>null</code>
   */
  @SuppressWarnings("unchecked")
  <T> Stream<T> of(final T... array);

  /**
   * Wraps the result of {@link Workspace#getArtifacts()} or if filters are provided of
   * {@link Workspace#getArtifacts(Artifact...)} into a Stream.
   * 
   * @param ws
   *          target workspace
   * @param filters
   *          variable arguments array
   * @return Stream which consist elements of array
   * @throws NullPointerException
   *           if <code>ws</code> is <code>null</code>
   */
  Stream<Artifact> of(final Workspace ws, final Artifact... filters);

  /**
   * Wraps the keys of an {@link MapArtifact} into a Stream.
   * 
   * @param map
   *          supplier for Stream
   * @return Stream which consist keys of {@link MapArtifact}
   * @throws NullPointerException
   *           if <code>map</code> is <code>null</code>
   */
  Stream<Object> keys(final MapArtifact map);

  /**
   * Wraps the values of an {@link MapArtifact} into a Stream.
   * 
   * @param map
   *          supplier for Stream
   * @return Stream which consist keys of {@link MapArtifact}
   * @throws NullPointerException
   *           if <code>map</code> is <code>null</code>
   */
  Stream<Object> values(final MapArtifact map);

  /**
   * Wraps the values of an {@link CollectionArtifact} which only consists of {@link Artifact} into a Stream.
   * 
   * @param collectionArtifact
   *          supplier for Stream
   * @return Stream which consists only {@link Artifact} from {@link CollectionArtifact}
   * @throws NullPointerException
   *           if <code>collectionArtifact</code> is <code>null</code>
   */
  Stream<Artifact> onlyArtifacts(final CollectionArtifact collectionArtifact);

  /**
   * Wraps the values of an {@link CollectionArtifact} which does not consists of {@link Artifact} into a Stream.
   * 
   * @param collectionArtifact
   *          supplier for Stream
   * @return Stream which does not consists {@link Artifact} from {@link CollectionArtifact}
   * @throws NullPointerException
   *           if <code>collectionArtifact</code> is <code>null</code>
   */
  Stream<Object> of(final CollectionArtifact collectionArtifact);

  /**
   * Wraps elements of a {@link Container} into a Stream.
   * 
   * @param container
   *          supplier for Stream
   * @return Stream which consist elements of container
   * @throws NullPointerException
   *           if <code>container</code> is <code>null</code>
   */
  Stream<Artifact> of(final Container container);

  /**
   * Wraps elements of a {@link Resource} into a Stream.
   * 
   * @param resource
   *          supplier for Stream
   * @return Stream which consist elements of resource
   * @throws NullPointerException
   *           if <code>resource</code> is <code>null</code>
   */
  Stream<Artifact> of(final Resource resource);

  /**
   * Wraps elements of a {@link Container} with the specified {@link Filter} into a Stream.
   * 
   * @param container
   *          supplier for Stream
   * @param filter
   *          filter for container
   * @return Stream which consist elements of container
   * @throws NullPointerException
   *           if <code>container</code> or <code>filter</code> is <code>null</code>
   */
  Stream<Artifact> of(final Container container, final Filter filter);

  /**
   * Wraps elements of a {@link Project} into a Stream.
   * 
   * @param project
   *          supplier for Stream
   * @return Stream which consist elements of project
   * @throws NullPointerException
   *           if <code>project</code> is <code>null</code>
   */
  Stream<Artifact> of(final Project project);

  /**
   * Wraps the packages of a {@link Project} into a Stream.
   * 
   * @param project
   *          supplier for Stream
   * @return Stream which consist packages of project
   * @throws NullPointerException
   *           if <code>project</code> is <code>null</code>
   */
  Stream<Package> packages(final Project project);

  /**
   * Wraps elements of a {@link MetaModel} into a Stream.
   * 
   * @param metaModel
   *          supplier for Stream
   * @return Stream which consist elements of metaModel
   * @throws NullPointerException
   *           if <code>metaModel</code> is <code>null</code>
   */
  Stream<Artifact> of(final MetaModel metaModel);

  /**
   * Wraps elements of an {@link Owner} into a Stream.
   * 
   * @param owner
   *          supplier for Stream
   * @return Stream which consist elements of owner
   * @throws NullPointerException
   *           if <code>owner</code> is <code>null</code>
   */
  Stream<Artifact> of(final Owner owner);

  /**
   * Wraps elements of a {@link Tool} into a Stream.
   * 
   * @param tool
   *          supplier for Stream
   * @return Stream which consist elements of tool
   * @throws NullPointerException
   *           if <code>tool</code> is <code>null</code>
   */
  Stream<Artifact> of(final Tool tool);
}
