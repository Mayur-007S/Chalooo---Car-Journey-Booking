package com.api.dto;

public record UserDTO(
		Long id,
		String email,
		String username,
		String phoneno,
		String role
		) {

}
