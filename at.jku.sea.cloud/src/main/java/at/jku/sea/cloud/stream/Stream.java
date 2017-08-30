package at.jku.sea.cloud.stream;

import java.util.List;
import java.util.NoSuchElementException;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.stream.predicate.Predicate;
import at.jku.sea.cloud.stream.predicate.PredicateStream;
import at.jku.sea.cloud.utils.CloudUtils;

/**
 * To perform a computation, stream operations are composed into a <em>stream pipeline</em>. A stream pipeline consists
 * of a source (which might be an array, a collection, artifact container), zero or more
 * <em>intermediate operations</em> (which transform a stream into another stream, such as
 * {@link Stream#filter(String, Predicate)}), and a <em>terminal operation</em> (which produces a result, such as
 * {@link Stream#count()} or {@link Stream#toList()}). Streams are lazy; computation on the source data is only
 * performed when the terminal operation is initiated, and source elements are consumed only as needed.
 * 
 * <p>
 * A stream pipeline, can be viewed as a <em>query</em> on the stream source.
 * </p>
 * 
 * <p>
 * Every query should have its own instance of a Stream. A pipeline which is already referenced throws an
 * {@link IllegalArgumentException}. It is possible to store a <em>query</em> and only perform
 * <em>terminal operations</em> on it but this is not recommended.
 * </p>
 * 
 * @author Florian Weger
 *
 * @param <T>
 *          the type of the stream elements which are allowed in the Cloud
 * @see CloudUtils#isSupportedType(Object)
 * @see Predicate
 * @see TerminalOperation
 */
public interface Stream<T> {

  /**
   * Returns a stream consisting of the elements of this stream that match the given predicate.
   *
   * <p>
   * This is an <em>intermediate operation</em>.
   * </p>
   *
   * @param context
   *          a name to build up a context in nested Stream
   * @param p
   *          a predicate to apply to each element to determine if it should be included
   * @return the new stream
   * @throws IllegalArgumentException
   */
  Stream<T> filter(String context, Predicate<T> p) throws IllegalArgumentException;

  /**
   * Returns a stream consisting of the results of applying the given function to the artifacts of this stream.
   *
   * <p>
   * This is an <em>intermediate operation</em>.
   * </p>
   *
   * @param <U>
   *          The element type of the new stream
   * @param context
   *          a name to build up a context in nested Stream
   * @param navigator
   *          a route to an {@link Artifact} property
   * @return the new stream
   * @throws IllegalArgumentException
   *           if the context is not recognized in sub queries
   * @throws PropertyDoesNotExistException
   *           if a <em>route</em> to property does not exist
   */
  <U> Stream<U> map(String context, TerminalOperation<U> navigator) throws IllegalArgumentException, PropertyDoesNotExistException;

  /**
   * Transform {@code this} Stream to perform lazy evaluations.
   * 
   * @return a {@link PredicateStream} Stream
   */
  PredicateStream<T> asPredicate();

  /**
   * Returns a element described by the provided {@link Predicate} or throws {@link NoSuchElementException} if there
   * isn't any.
   * <p>
   * Returns the first element of this stream which fulfills the provided {@link Predicate}s condition, or throws an
   * {@link NoSuchElementException}.
   *
   * @param context
   *          a name to build up a context in nested Stream
   * @param p
   *          a predicate to find the first element with this conditions
   * @return an element which meet the condition of the {@link Predicate}
   * @throws NoSuchElementException
   *           if no element was found
   * @throws IllegalArgumentException
   *           if a nested operation uses a non-specified {@code context}
   * @throws NullPointerException
   *           if the {@code context} or {@code p} is {@code null}
   */
  T find(String context, Predicate<T> p) throws NoSuchElementException, IllegalArgumentException;

  /**
   * Returns whether any elements of this stream match the provided predicate. May not evaluate the predicate on all
   * elements if not necessary for determining the result. If the stream is empty then {@code false} is returned and the
   * predicate is not evaluated.
   *
   * <p>
   * This is a <em>short-circuiting terminal operation</em>.
   *
   * @param context
   *          a name to build up a context in nested Stream
   * @param predicate
   *          a predicate to apply to elements of this stream
   * @return {@code true} if any elements of the stream match the provided predicate, otherwise {@code false}
   * @throws NullPointerException
   *           if the {@code context} or {@code p} is {@code null}
   * @throws IllegalArgumentException
   *           if a nested operation uses a non-specified {@code context}
   */
  boolean anyMatch(String context, Predicate<T> p) throws IllegalArgumentException;

  /**
   * Returns whether all elements of this stream match the provided predicate. May not evaluate the predicate on all
   * elements if not necessary for determining the result. If the stream is empty then {@code true} is returned and the
   * predicate is not evaluated.
   *
   * <p>
   * This is a <em>short-circuiting terminal operation</em>.
   *
   * @param context
   *          a name to build up a context in nested Stream
   * @param predicate
   *          a predicate to apply to elements of this stream
   * @return {@code true} if either all elements of the stream match the provided predicate or the stream is empty,
   *         otherwise {@code false}
   * @throws NullPointerException
   *           if the {@code context} or {@code p} is {@code null}
   * @throws IllegalArgumentException
   *           if a nested operation uses a non-specified {@code context}
   */
  boolean allMatch(String context, Predicate<T> p) throws IllegalArgumentException;

  /**
   * Returns whether no elements of this stream match the provided predicate. May not evaluate the predicate on all
   * elements if not necessary for determining the result. If the stream is empty then {@code true} is returned and the
   * predicate is not evaluated.
   *
   * <p>
   * This is a <em>short-circuiting terminal operation</em>.
   * 
   * @param context
   *          a name to build up a context in nested Stream
   * @param predicate
   *          a predicate to apply to elements of this stream
   * @return {@code true} if either no elements of the stream match the provided predicate or the stream is empty,
   *         otherwise {@code false}
   * @throws NullPointerException
   *           if the {@code context} or {@code p} is {@code null}
   * @throws IllegalArgumentException
   *           if a nested operation uses a non-specified {@code context}
   */
  boolean noneMatch(String context, Predicate<T> p) throws IllegalArgumentException;

  /**
   * Returns a {@link List} of elements of the Stream.
   * 
   * <p>
   * This is a <em>terminal operation</em>.
   * 
   * @return a {@link List} of elements
   */
  List<T> toList();

  /**
   * Returns the size of the Stream.
   * 
   * <p>
   * This is a <em>terminal operation</em>.
   * 
   * @return size of Stream
   */
  long count();
}
