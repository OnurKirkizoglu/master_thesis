package at.jku.sea.cloud;

import at.jku.sea.cloud.exceptions.CredentialsException;

/**
 * User of the cloud service.
 * <p>
 * Sometime in the future, cloud access should only be possible if first a
 * User instance is gained by authenticating with the {@link Cloud#getUserByCredentials(String, String)}
 * method.
 * <p>
 * (c) 2016 JKU
 * @author jMayer
 * 
 * 
 *
 */

public interface User {
  /**
   * Returns the User ID
   * @return
   */
  long getId();
  /**
   * Returns the User's name
   * @return
   */
  String getName();
  /**
   * Sets the User name
   * @param name 
   */
  void setName(String name);
  /**
   * @return the user login identifier
   */
  String getLogin();
  /**
   * Changes the login identifier
   * @param login
   * @throws CredentialsException if the new login is already in use
   */
  void setLogin(String login) throws CredentialsException;
  /**
   * The password property is only used for passing in password candidates to check against
   * the hash value in the DataStorage, or on update to supply a new one which is hashed in the DataStorage.
   * The {@link DataStorage} should never give the password to the outside, initializing User instances with null instead.
   * 
   * @return the user password
   */
  String getPassword();
  /**
   * Sets a password to update via {@link Cloud#updateUser(User)}.
   * 
   * @param password
   */
  void setPassword(String password);
  /**
   * 
   * @return artifact ID for the Owner Metadata Artifact.
   */
  long getOwnerId();
  
  /**
   * 
   * @return Owner artifact instance for this user.
   */
  Owner getOwner();
}
