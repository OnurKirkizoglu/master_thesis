package at.jku.sea.cloud.exceptions;

/**
 * Exception class for User interface.
 * 
 * This exception should be thrown when a User is trying to login,
 * but the provided credentials don't match any stored,
 * when trying to create a new User with a login that already exists,
 * or when trying to change their login to that of another user.
 * 
 * @author jMayer
 *
 */
public class CredentialsException extends RuntimeException  {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public CredentialsException(final String login) {
    super(String.format("Credentials for user with login %s are incorrect.", login));
  }
  
  public CredentialsException(final String attempt, final String old) {
    super(String.format("Changing the login from %s to %s is not permitted.", old, attempt));
  }
}
