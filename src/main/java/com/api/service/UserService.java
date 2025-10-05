package com.api.service;

import java.util.List;

import com.api.model.User;

public interface UserService {
	
//	public String getUserRole(Long userId);
	
	public List<User> getAllUsers();
	
	public User addUser(User user);

	public User UserByEmail(String email);
	
	public String verifyUser(User user);
}
