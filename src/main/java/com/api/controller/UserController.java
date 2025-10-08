package com.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.api.authservice.impl.JwtService;
import com.api.model.User;
import com.api.service.UserService;

import java.util.List;

@RestController   
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
    	logger.info("Inside register method of UserController");
    	user.setPassword(encoder.encode(user.getPassword()));
    	logger.info("Call Add User Method");
    	User user1 = userService.addUser(user);
    	if(user1 != null) {
    		return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    	}
    	logger.info("Exit from register method of UserController");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
    	logger.info("Inside login method of UserController");
    	logger.info("Call Verify User Method");
    	
    	String result = userService.verifyUser(user);
    	if(!result.equalsIgnoreCase("Fail")) {
    		return ResponseEntity.status(HttpStatus.CREATED).body(result);
    	}
    	logger.info("Exit from login method of UserController");
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
}
