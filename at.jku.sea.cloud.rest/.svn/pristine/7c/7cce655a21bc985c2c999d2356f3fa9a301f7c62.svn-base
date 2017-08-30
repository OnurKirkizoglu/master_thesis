package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.User;

/**
 * POJO class for JSON mapping of {@link User} instances.
 * @author jMayer
 *
 */

@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "User")
public class PojoUser {
	private long id;
	private String name, login, password;
	private long ownerId;
	
	/*
	 * The {@code @JsonProperty} annotations on the parameters allow
	 * the JSON mapper to use the constructor instead of setters to initialize the object.
	 */
	public PojoUser(
			@JsonProperty("id") long id,
			@JsonProperty("name") String name,
			@JsonProperty("login") String login,
			@JsonProperty("password") String password,
			@JsonProperty("owner") long ownerId) {
		this.id = id;
		this.name = name;
		this.login = login;
		this.password = password;
		this.ownerId = ownerId;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}
	
	@Override
	public String toString() {
		return "PojoUser [id=" + id + ", name=" + name + ", login=" + login + ", password=" + password + "]";
	}

	public long getOwner() {
		return ownerId;
	}
}
