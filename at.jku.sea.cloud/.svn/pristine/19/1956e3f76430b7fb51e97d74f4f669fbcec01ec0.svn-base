package at.jku.sea.cloud.implementation;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.exceptions.CredentialsException;

public class DefaultUser implements User {

  protected long id;
  protected String name, login, password;
  protected long ownerId;
  private DataStorage storage;
  
  public DefaultUser(
      final DataStorage st,
      final long id,
      final String name,
      final String login,
      final String password,
      final long ownerId) {
    this.id = id;
    this.name = name;
    this.login = login;
    this.password = password;
    this.ownerId = ownerId;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public void setName(String name) {
    this.name = name;
    this.storage.updateUser(this);
  }
  
  @Override
  public String getLogin() {
    return login;
  }
  
  @Override
  public void setLogin(String login) throws CredentialsException {
    this.login = login;
    this.storage.updateUser(this);
  }

  @Override
  public String getPassword() {
    return password;
  }
  
  @Override
  public void setPassword(String password) {
    this.password = password;
    this.storage.updateUser(this);
  }

  @Override
  public String toString() {
    return "DefaultUser [id=" + id + ", name=" + name + ", login=" + login + "]";
  }
  
  @Override
  public long getOwnerId() {
    return ownerId;
  }
  
  @Override
  public Owner getOwner() {
    return ArtifactFactory.getArtifact(this.storage, this.storage.getHeadVersionNumber(), ownerId);
  }
  
  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof User) {
      return id == ((User)obj).getId();
    }
    return false;
  }
}
