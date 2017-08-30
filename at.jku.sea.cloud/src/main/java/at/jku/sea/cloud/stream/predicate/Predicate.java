package at.jku.sea.cloud.stream.predicate;

import at.jku.sea.cloud.stream.Context;

/**
 * Represents a predicate (boolean-valued function) of a context.
 *
 * @param <T>
 *          the type of the input to the predicate
 *
 */
public interface Predicate<T> {
  /**
   * Evaluates this predicate on the given argument.
   *
   * @param c
   *          current context
   * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
   */
  boolean test(Context c);

  /**
   * Combines <code>this</code> with another {@code Predicate} by a logical AND (&).
   * 
   * @param p
   *          {@code Predicate} to create a logical AND
   * @return a logical AND with <code>this</code> and the given {@code Predicate}
   */
  Predicate<T> and(Predicate<T> p);

  /**
   * Combines <code>this</code> with another {@code Predicate} by a logical OR (|).
   * 
   * @param p
   *          {@code Predicate} to create a logical OR
   * @return a logical OR with <code>this</code> and the given {@code Predicate}
   */
  Predicate<T> or(Predicate<T> p);
}
