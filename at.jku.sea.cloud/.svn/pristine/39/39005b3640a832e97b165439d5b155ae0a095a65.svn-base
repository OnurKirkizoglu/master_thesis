package at.jku.sea.cloud.stream.predicate;

import at.jku.sea.cloud.stream.Stream;

/**
 * Provides functionality to to make a {@link Stream} lazy and convert it into a {@link Predicate}.
 * 
 * @author Florian Weger
 *
 * @param <T>
 *          the type of the stream elements
 */
public interface PredicateStream<T> {
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
   * @return predicate for lazy evaluation
   * @throws NullPointerException
   *           if the {@code context} or {@code p} is {@code null}
   * @throws IllegalArgumentException
   *           if a nested operation uses a non-specified {@code context}
   */
  Predicate<T> anyMatch(String context, Predicate<T> p);

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
   * @return predicate for lazy evaluation
   * @throws NullPointerException
   *           if the {@code context} or {@code p} is {@code null}
   * @throws IllegalArgumentException
   *           if a nested operation uses a non-specified {@code context}
   */
  Predicate<T> allMatch(String context, Predicate<T> p);

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
   * @return predicate for lazy evaluation
   * @throws NullPointerException
   *           if the {@code context} or {@code p} is {@code null}
   * @throws IllegalArgumentException
   *           if a nested operation uses a non-specified {@code context}
   */
  Predicate<T> noneMatch(String context, Predicate<T> p);
}
