package at.jku.sea.cloud.navigator;

import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.stream.Context;

/**
 * Represents the final path of properties.
 * 
 * @author Florian Weger
 *
 * @param <T>
 *          the type of the result of the operation
 */
public interface TerminalOperation<T> {

  /**
   * Returns the last property of the property path. A property path consists of zero to n properties names.
   * 
   * @return the property value
   * @throws PropertyDoesNotExistException
   *           if the property name does not exist
   */
  T get() throws PropertyDoesNotExistException;

  /**
   * Returns the last property of the property path within a given {@link Context}. A property path consists of zero to
   * n properties names.
   * 
   * @return the property value
   * @throws PropertyDoesNotExistException
   *           if the property name does not exist
   */
  T get(Context context) throws PropertyDoesNotExistException;

}
