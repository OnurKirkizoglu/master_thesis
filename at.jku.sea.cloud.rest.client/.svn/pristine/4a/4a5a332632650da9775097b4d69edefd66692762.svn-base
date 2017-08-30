package at.jku.sea.cloud.rest.client;

import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.exceptions.CredentialsException;
import at.jku.sea.cloud.implementation.DefaultUser;
import at.jku.sea.cloud.rest.client.handler.UserHandler;

public class RestUser extends DefaultUser implements User {
	
	UserHandler handler;
	
	public RestUser(long id, String name, String login, String password, long ownerId) {
		super(null, id, name, login, password, ownerId);
		
		handler = UserHandler.getInstance();
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setLogin(String login) throws CredentialsException {
		this.login = login;
	}
	
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public Owner getOwner() {
	  return this.handler.getOwner(ownerId);
	}
	
	@Override
	public String toString() {
	  return "RestUser [id=" + id + ", name=" + name + ", login=" + login + "]";
	}
}
