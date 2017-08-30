package at.jku.sea.cloud.rest.server.controller;

import java.util.Random;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.exceptions.CredentialsException;
import at.jku.sea.cloud.exceptions.OwnerDoesNotExistException;
import at.jku.sea.cloud.implementation.DefaultUser;
import at.jku.sea.cloud.rest.pojo.PojoUser;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

/**
 * @author Jakob Mayer
 */
@Controller
@RequestMapping("/designspace/users")
public class UserController {
	@Autowired Logger logger;
	@Autowired Cloud cloud;
	@Autowired PojoFactory pojo;
	

//  @Autowired
//  UserHandler handler;

	@RequestMapping(value = {"","/all"}, method = RequestMethod.GET)
	public ResponseEntity<PojoUser[]> getUsers() {
		PojoUser[] pojoUsers = pojo.createPojoArray(cloud.getUsers().toArray(new User[0]));
		return new ResponseEntity<PojoUser[]>(pojoUsers, HttpStatus.OK);
	}
	
	@RequestMapping(value = {"","/func/create"}, method = RequestMethod.POST)
	public ResponseEntity<PojoUser> createUser(@RequestBody(required = false) PojoUser user) {
		logger.info("User creation for data " + user);
		
		User inserted = null;
		
		if(user != null && user.getLogin() != null && user.getPassword() != null) {
			inserted = cloud.createUser(user.getName(), user.getLogin(), user.getPassword());
		} else {
			// TODO: probably remove this and throw an exception instead 
			// create placeholder user
			inserted = cloud.createUser("New User", String.valueOf(new Random().nextLong()), "");
		}
		
		logger.info("user created: " + inserted);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Location", "./id=" + inserted.getId());
		
		ResponseEntity<PojoUser> created = new ResponseEntity<>(pojo.createPojo(inserted), headers, HttpStatus.CREATED);
		return created;
	}
	
	@RequestMapping(value = "/id={id}", method = RequestMethod.GET)
	public ResponseEntity<PojoUser> getUser(@PathVariable long id) {
		User user = cloud.getUser(id);
		
		return new ResponseEntity<>(pojo.createPojo(user), HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/id={id}", method = RequestMethod.PUT)
	public ResponseEntity<PojoUser> updateUser(@PathVariable long id, @RequestBody PojoUser puser) {
		User user = cloud.updateUser(new DefaultUser(null,puser.getId(), puser.getName(), puser.getLogin(), puser.getPassword(), puser.getOwner()));
		return new ResponseEntity<>(pojo.createPojo(user),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/id={id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable long id) {
		cloud.deleteUser(id);
	}
	
	@RequestMapping(value = "/login={login}", method = RequestMethod.POST)
	public ResponseEntity<PojoUser> login(@PathVariable String login, @RequestBody String password) {
		User user = cloud.getUserByCredentials(login, password);
		return new ResponseEntity<>(pojo.createPojo(user),HttpStatus.OK);
	}
	
  // ------------------------------------
  // Exception handling
  // ------------------------------------
  
  @ExceptionHandler(CredentialsException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  @ResponseBody
  public String handleCredentialsException() {
    return CredentialsException.class.getSimpleName();
  }
  
  @ExceptionHandler(OwnerDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleOwnerDoesNotExistException() {
    return OwnerDoesNotExistException.class.getSimpleName();
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleNullPointerException() {
    return NullPointerException.class.getSimpleName();
  }

}
