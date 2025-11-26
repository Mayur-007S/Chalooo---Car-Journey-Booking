package com.api.authservice.impl;

import java.util.List;
import java.util.Optional;

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
import com.api.validation.ObjectValidator;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ObjectValidator<User> userValidator;

	@Autowired
	private JwtService jwtService;
	
	  private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public List<User> getAllUsers() {
		logger.info("Inside getAllUsers method of UserServiceImpl");
		return userRepository.findAll();
	}

	@Override
	public User addUser(User user) {
		logger.info("Inside addUser method of UserServiceImpl");
		logger.info("Validating user details");
		userValidator.validate(user);
		
		return userRepository.save(user);
	}


	@Override
	public String verifyUser(User user) {
		logger.info("Inside verifyUser method of UserServiceImpl");
		logger.info("Validating user details");
		userValidator.validate(user);
		
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
		logger.info("Inside UserByEmail method of UserServiceImpl");
		logger.info("Exit from UserByEmail method of UserServiceImpl");
		return userRepository.findByEmail(email.toLowerCase());
	}

	@Override
	public User UserByUsername(String username) {
		logger.info("Inside UserByName method of UserServiceImpl");
		return userRepository.findByUsername(username.toLowerCase());
	}

	@Override
	public User getOneUser(int uid) {
		logger.info("Inside UserById method of UserServiceImpl");
		return userRepository.findById(uid);
	}

	@Override
	public Optional<User> getOneUser(Long uid) {
		logger.info("Inside UserById method of UserServiceImpl");
		return userRepository.findById(uid);
	}
}
