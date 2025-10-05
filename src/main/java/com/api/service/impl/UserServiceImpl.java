package com.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.api.controller.UserController;
import com.api.model.User;
import com.api.repository.UserRepository;
import com.api.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;
	
	  private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User addUser(User user) {
		logger.info("Inside addUser method of UserServiceImpl");
		logger.info("Exit from addUser method of UserServiceImpl");
		return userRepository.save(user);
	}


	@Override
	public String verifyUser(User user) {
		logger.info("Inside verifyUser method of UserServiceImpl");
		Authentication authentication =
			authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		if(authentication.isAuthenticated()) {
			logger.info("User is authenticated");
			return jwtService.generateToken(user.getUsername());
		}
		logger.info("Exit from verifyUser method of UserServiceImpl");
		return "Fail";
	}

	@Override
	public User UserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

}
