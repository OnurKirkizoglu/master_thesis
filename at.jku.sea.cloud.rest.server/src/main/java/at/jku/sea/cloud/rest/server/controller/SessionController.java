package at.jku.sea.cloud.rest.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.exceptions.CredentialsException;

@Controller
@RequestMapping("/designspace/session")
public class SessionController {
	@Autowired Cloud cloud;
	
	
	
	
	@RequestMapping("/login/id={id}")
	public ResponseEntity<String> createSession(@PathVariable long id, @RequestBody String password) {
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("X-Auth-Session", "dummy");
		
		return new ResponseEntity<String>("", headers, HttpStatus.OK);
	}
	
	public boolean checkSession(HttpHeaders headers) throws CredentialsException {
		List<String> authHeader = headers.get("X-Auth-Session");
		
		
		
		return authHeader.size() == 1 && authHeader.get(0).equals("dummy");
	}
	
	@RequestMapping("/logout/")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> logout() {
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("X-Auth-Session", "dummy");
		
		return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
	}
}
