package com.api.service;

import java.util.List;
import java.util.Optional;

import com.api.dto.LoginUserDTO;
import com.api.dto.UserDTO;
import com.api.model.User;

public interface UserService {

	// public String getUserRole(Long userId);

	public List<UserDTO> getAllUsers();

	public User addUser(User user);

	public UserDTO UserByEmail(String email);

	public UserDTO UserByUsername(String username);

	public String verifyUser(LoginUserDTO user);

	public UserDTO getOneUser(int uid);

	public Optional<UserDTO> getOneUser(Long uid);
	
	public Optional<UserDTO> userByPhoneNo(String phoneno);
	
	public Boolean userExistOrNot(User user);
	
	public String emailByPassword(String email);
}
