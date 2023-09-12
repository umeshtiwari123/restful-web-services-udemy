package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UserResource {

	@Autowired
	private UserDaoService service;
	
	public UserResource(UserDaoService service)
	{
		this.service=service;
	}
	//GET /users
	@GetMapping("/users")
	public List<User> retriveAllUsers()
	{
		return service.findAll();
	}
	
	//Get Details for Specific Users
	//http://localhost:8080/users
	
	//EntityModel
	//WebMvcLinkBuilder
	@GetMapping("/users/{id}")
	public EntityModel<User> retriveUser(@PathVariable int id)
	{
		User user = service.findOne(id);
		
		if (user==null)
			throw new UserNotFoundException("id:"+id);
		
		EntityModel<User> entityModel = EntityModel.of(user);
		
		WebMvcLinkBuilder  link = linkTo(methodOn(this.getClass()).retriveAllUsers()); 
		entityModel.add(link.withRel("all-users"));
		
		return entityModel;	
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id)
	{
		service.deleteById(id);
			
	}
	
	//POST /users
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user)
	{
		User savedUser = service.save(user);
		// /users/4 => /users/{id}, user.getID
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(savedUser.getId())
						.toUri(); 
		// location - /users/4
		return ResponseEntity.created(location ).build();
	}
	
	
	
}
