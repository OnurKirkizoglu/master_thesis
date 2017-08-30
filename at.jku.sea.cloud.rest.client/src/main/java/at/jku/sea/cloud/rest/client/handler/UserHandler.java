package at.jku.sea.cloud.rest.client.handler;

import java.util.List;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.exceptions.CredentialsException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoUser;

public class UserHandler
	extends AbstractHandler {
	
	private static UserHandler INSTANCE = null;

	
	public static UserHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UserHandler();
		}
		
		return INSTANCE;
	}
	
	private UserHandler() {
	  
	}
	
	public List<User> getUsers() {
		String url = USER_ADDRESS;
		PojoUser[] users = template.getForEntity(url, PojoUser[].class).getBody();
		return restFactory.createRestList(users, restFactory::createRest);
	}

	public User getUser(long id) {
		String url = String.format(USER_ADDRESS + "/id=%d", id);
		try {
			PojoUser user = template.getForEntity(url, PojoUser.class).getBody();
			return restFactory.createRest(user);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(OwnerDoesNotExistException.class.getSimpleName()))
				throw new OwnerDoesNotExistException(id);
			throw e;
		}
	}

	public User insertUser(String name, String login, String password) throws CredentialsException {
		String url = USER_ADDRESS;
		try {
			PojoUser user = template.postForEntity(url, new PojoUser(0,name,login,password,0), PojoUser.class).getBody();
			return restFactory.createRest(user);
		} catch (HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
			if (e.getResponseBodyAsString().equals(CredentialsException.class.getSimpleName()))
				throw new CredentialsException(login);
			throw e;
		}
	}

	public User updateUser(User user) throws CredentialsException, OwnerDoesNotExistException {
	  String url = String.format(USER_ADDRESS + "/id=%d", user.getId());
		try {
			template.put(url, new PojoUser(user.getId(),user.getName(),user.getLogin(),user.getPassword(),user.getOwnerId()));
			return getUser(user.getId());
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(OwnerDoesNotExistException.class.getSimpleName())){
				throw new OwnerDoesNotExistException(user.getId());
			}
			if (e.getResponseBodyAsString().equals(CredentialsException.class.getSimpleName())) {
				throw new CredentialsException(user.getLogin());
			}
			throw e;
		}
	}

	public void deleteUser(long userId) {
		String url = String.format(USER_ADDRESS + "/id=%d", userId);
		try {
			template.delete(url);
		} catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(OwnerDoesNotExistException.class.getSimpleName()))
				throw new OwnerDoesNotExistException(userId);
			throw e;
		}
	}

	public User getUserByCredentials(String login, String password) throws CredentialsException {
		String url = String.format(USER_ADDRESS + "/login=%s", login);
		try {
			PojoUser puser = template.postForEntity(url, password, PojoUser.class).getBody();
			return restFactory.createRest(puser);
		} catch (HttpClientErrorException e) {
		  String responseBody = e.getResponseBodyAsString();
		  
			System.err.println(responseBody);
			
			if (CredentialsException.class.getSimpleName().equals(responseBody)) {
				throw new CredentialsException(login);
			}
			throw e;
		}
	}
	
	public Owner getOwner(long ownerId) {
	  return CloudHandler.getInstance().getOwner(ownerId);
	}
	
  public User getUserByOwnerId(long ownerId) {
    String url = String.format(OWNER_ADDRESS + "/id=%d/user", ownerId);
    try {
      PojoUser puser = template.getForEntity(url, PojoUser.class).getBody();
      return restFactory.createRest(puser);
    } catch (HttpClientErrorException e) {
      String responseBody = e.getResponseBodyAsString();
      
      System.err.println(responseBody);
      
      if (OwnerDoesNotExistException.class.getSimpleName().equals(responseBody)) {
        throw new OwnerDoesNotExistException(ownerId);
      }
      throw e;
    }
  }
}
