package at.jku.sea.cloud.stream;

/**
 * Context can be seen an universe with named objects. These are used during the pipeline process in a {@link Stream}.
 * 
 * @author Florian Weger
 *
 */
public interface Context {

  /**
   * Returns the named object and cast it to type {@code T}.
   * 
   * @param <T>
   *          the type of the result
   * @param name
   *          name of object in the given Context
   * @return the named object
   */
  <T> T get(String name);

  /**
   * Returns a new Context with the given {@code name} and {@code o} with the current elements in this Context.
   * <p>
   * This implies that Contexts are immutable.
   * 
   * @param name
   *          name for given {@code o}
   * @param o
   *          object to be named
   * @return new Context with additional (name -> o) mapping
   */
  Context put(String name, Object o);

  /**
   * Path is used to encapsulate a Context name.
   * 
   * @author Florian Weger
   *
   */
  interface Path {
    /**
     * Returns the provided name of a named object.
     * 
     * @return contexts name
     */
    String get();
  }

}
