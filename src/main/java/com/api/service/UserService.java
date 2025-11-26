package com.api.service;

import java.util.List;
import java.util.Optional;

import com.api.model.User;

public interface UserService {
	
//	public String getUserRole(Long userId);
	
	public List<User> getAllUsers();
	
	public User addUser(User user);

	public User UserByEmail(String email);
	
	public User UserByUsername(String username);
	
	public String verifyUser(User user);
	
	public User getOneUser(int uid);
	
	public Optional<User> getOneUser(Long uid);
}
