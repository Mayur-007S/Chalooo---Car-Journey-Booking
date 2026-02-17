package com.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.api.dto.LoginUserDTO;
import com.api.mail.service.MailService;
import com.api.model.User;
import com.api.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

	private UserService userService;
	private MailService mailService;
	@Email(message = "Email not correct please enter correct one!!!")
	private String email;
	private String phoneno;
	private Logger logger;
	private BCryptPasswordEncoder encoder;

	public UserController(UserService userService, MailService mailService) {
		this.userService = userService;
		this.mailService = mailService;
		this.logger = LoggerFactory.getLogger(UserController.class);
		this.encoder = new BCryptPasswordEncoder(12);
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) throws MessagingException, IOException {
		logger.info("Inside register method of UserController");

		email = user.getEmail();

		if (!userService.userExistOrNot(user)) {
			logger.info("username, email and phone no are alredy exist. " + "please login to continue!!!");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("username, email and phone no are alredy exist." + " please login to continue!!!");
		}

		user.setPassword(encoder.encode(user.getPassword()));

		User user1 = userService.addUser(user);
		if (user1 != null) {
			mailService.sendRegistrationEmail(user.getEmail(), "SignIn Successfully.!!!", user.getUsername());
			return ResponseEntity.status(HttpStatus.CREATED).body(user1);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginUserDTO loginuser) throws MessagingException, IOException {
		logger.info("Inside login method of UserController");

		String result = userService.verifyUser(loginuser);
		if (!result.equalsIgnoreCase("Fail")) {
			var user = userService.UserByUsername(loginuser.username());
			mailService.sendLoginEmail(user.email(), "SignIn Successfully.!!!", user.username());
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		}
		logger.info("Exit from login method of UserController");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER', 'ADMIN')")
	@GetMapping("/getByEmail")
	public ResponseEntity<?> getUserByEmail(@RequestParam(name = "email", required = true) String emailInput) {
		logger.info("Inside get user by email user controller.");
		email = emailInput;
		var user = userService.UserByEmail(emailInput);
		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}
		return ResponseEntity.status(HttpStatus.OK).body("Error: User not found with user " + emailInput);

	}

	@PreAuthorize("hasAnyRole('PASSENGER','DRIVER','ADMIN')")
	@GetMapping("/getByUsername")
	public ResponseEntity<?> getUserByUsername(@RequestParam(name = "username", required = true) String username) {
		logger.info("Inside get user by username in controller.");
		var user = userService.UserByUsername(username);
		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: User not found with user " + username);
	}
}
