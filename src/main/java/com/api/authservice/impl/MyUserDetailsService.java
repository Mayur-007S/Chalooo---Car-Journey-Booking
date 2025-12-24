package com.api.authservice.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.controller.UserController;
import com.api.model.User;
import com.api.model.UserPrinciple;
import com.api.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	private Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Attempting to load user by username: " + username);
		User user = repository.findByUsername(username);
		if (user == null) {
			logger.info("User Not found : " + username);
			throw new UsernameNotFoundException("User not found");
		}
		logger.info("Exit loadUserByUsername with user: " + user.getUsername());
		return new UserPrinciple(user);
	}

}
