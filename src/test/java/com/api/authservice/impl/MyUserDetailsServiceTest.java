package com.api.authservice.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.api.model.User;
import com.api.model.UserPrinciple;
import com.api.repository.UserRepository;

class MyUserDetailsServiceTest {

	@Mock
	private UserRepository repository;

	@InjectMocks
	private MyUserDetailsService detailsService;

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testLoadUserByUsername() {
		User user = new User(Long.parseLong("17"), "mayurmyanan111@.com",
				"$2a$12$3h/hUjs2ww5oZEqo0rh8I.A/L9hTauCn2wNJCRfeX95MH7.yQ4Xae", "mayur", "9011781933", "ROLE_ADMIN");
		
		when(repository.findByUsername(ArgumentMatchers.anyString())).thenReturn(user);
		UserDetails userDetails = detailsService.loadUserByUsername("mayur");

		assertNotNull(userDetails);
		assertEquals(userDetails.getUsername(), user.getUsername());
	}
	@Test
	void testLoadUserByUsernameThrowsException() {
		
		when(repository.findByUsername(ArgumentMatchers.anyString())).thenReturn(null);
		UserDetails userDetails = detailsService.loadUserByUsername("mayur");

		assertNotNull(userDetails);
		assertThrows(UsernameNotFoundException.class, () -> {
			detailsService.loadUserByUsername("mayur");
		});
	}

}
