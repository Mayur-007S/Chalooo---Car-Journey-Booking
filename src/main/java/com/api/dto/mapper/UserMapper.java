package com.api.dto.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.dto.UserDTO;
import com.api.model.User;
import com.api.repository.UserRepository;
import com.api.service.UserService;

@Component
public class UserMapper {
	
	@Autowired
	private UserRepository repository;
	
	public UserDTO userToUserDTO(User user) {
		UserDTO dto = new UserDTO(
				user.getId(),
				user.getEmail(),
				user.getUsername(),
				user.getPhone(),
				user.getRole()
				);
		return dto;
	}
	
	public List<UserDTO> userToUserDTO(List<User> user) {
		List<UserDTO> dto = user.stream()
				.map(us -> new UserDTO(
				us.getId(),
				us.getEmail(),
				us.getUsername(),
				us.getPhone(),
				us.getRole()
				))		
				.toList();
		
		return dto;
	}
	
	public User dtoTOuser(UserDTO dto) {
		var user = new User();
		user.setUsername(dto.username());
		user.setEmail(dto.email());
		user.setPhone(dto.phoneno());
		user.setId(dto.id());
		user.setRole(dto.role());
		user.setPassword(repository.findPasswordByEmail(dto.email()));
		return user;
	}
}
