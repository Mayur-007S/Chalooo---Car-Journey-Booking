package com.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.api.model.User;
import com.api.service.UserService;
import com.api.service.impl.JwtService;

import java.util.List;

@RestController   // Base path for all user-related APIs
public class UserController {

    @Autowired
    private UserService userService;
    
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public User register(@RequestBody User user) {
    	logger.info("Inside register method of UserController");
    	user.setPassword(encoder.encode(user.getPassword()));
    	logger.info("Call Add User Method");
    	logger.info("Exit from register method of UserController");
        return userService.addUser(user);
    }
    
    @PostMapping("/login")
    public String login(@RequestBody User user) {
    	logger.info("Inside login method of UserController");
    	logger.info("Call Verify User Method");
    	logger.info("Exit from login method of UserController");
    	return userService.verifyUser(user);
    }
    
    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
