package com.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.User;
import com.api.service.UserService;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

	@Autowired
	private UserService userService;
			
	private Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<User>> getUsers(){
		logger.info("Inside get all users method");
		List<User> users = userService.getAllUsers();
		if(!users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(users);
		}
		throw new UsernameNotFoundException("No users found in database.");
	}
	
}
