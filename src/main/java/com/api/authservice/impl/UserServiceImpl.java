package com.api.authservice.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.api.dto.LoginUserDTO;
import com.api.dto.UserDTO;
import com.api.dto.mapper.UserMapper;
import com.api.model.User;
import com.api.repository.UserRepository;
import com.api.service.UserService;
import com.api.validation.ObjectValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private AuthenticationManager authenticationManager;
	private ObjectValidator<User> userValidator;
	private ObjectValidator<LoginUserDTO> loginUserValidator;
	private JwtService jwtService;
	private UserMapper userMapper;
	private Logger logger;
	
	public UserServiceImpl(UserRepository userRepository, 
			AuthenticationManager authenticationManager,
			ObjectValidator<User> userValidator, 
			ObjectValidator<LoginUserDTO> loginUserValidator,
			JwtService jwtService, UserMapper userMapper
			) 
	{
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.userValidator = userValidator;
		this.loginUserValidator = loginUserValidator;
		this.jwtService = jwtService;
		this.userMapper = userMapper;
		this.logger = LoggerFactory.getLogger(UserServiceImpl.class);
	}

	@Override
	public List<UserDTO> getAllUsers() {
		logger.info("Inside getAllUsers method of UserServiceImpl");
		List<User> users = userRepository.findAll();
		return userMapper.userToUserDTO(users);
	}

	@SuppressWarnings("null")
	@Override
	@CachePut(value = "users", key = "#result.id")
	public User addUser(User user) {
		logger.info("Inside addUser method of UserServiceImpl");
		logger.info("Validating user details");
		userValidator.validate(user);

		return userRepository.save(user);
	}

	@Override
	public String verifyUser(LoginUserDTO user) {
		logger.info("Inside verifyUser method of UserServiceImpl");
		logger.info("Validating user details");
		loginUserValidator.validate(user);

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.username(), user.password()));
		if (authentication.isAuthenticated()) {
			logger.info("User is authenticated");
			return jwtService.generateToken(user.username());
		}
		logger.info("Exit from verifyUser method of UserServiceImpl");
		return "Fail";
	}

	@Override
	@Cacheable(value = "users", key = "#email")
	public UserDTO UserByEmail(String email) {
		logger.info("Inside UserByEmail method of UserServiceImpl");
		var user = userRepository.findByEmail(email.toLowerCase());
		return userMapper.userToUserDTO(user);
	}

	@Override
	@Cacheable(value = "users", key = "#username")
	public UserDTO UserByUsername(String username) {
		logger.info("Inside UserByName method of UserServiceImpl");
		var user = userRepository.findByUsername(username.toLowerCase());
		return userMapper.userToUserDTO(user);
	}

	@Override
	@Cacheable(value = "users", key = "#uid")
	public UserDTO getOneUser(int uid) {
		logger.info("Inside UserById method of UserServiceImpl");
		var user = userRepository.findById(uid);
		return userMapper.userToUserDTO(user);
	}

	@Override
	@Cacheable(value = "users", key = "#uid")
	public Optional<UserDTO> getOneUser(Long uid) {
		logger.info("Inside UserById method of UserServiceImpl");
		var user = userRepository.findById(uid);
		return Optional.of(userMapper.userToUserDTO(user.get()));
	}

	@Override
	public Optional<UserDTO> userByPhoneNo(String phoneno) {
		logger.info("Inside UserByPhone method of UserServiceImpl");
		 var user = userRepository.findByPhone(phoneno);
		 return Optional.of(userMapper.userToUserDTO(user.get()));
	}

	@Override
	public Boolean userExistOrNot(User user) {
		logger.info("Inside user exits or not method of UserServiceImpl");
		Optional<List<User>> userlist = userRepository
				.userExistOrNot(user.getUsername(), user.getEmail(), user.getPhone());
		if(userlist.isPresent()) {
			logger.info("result: true");
			return true;
		}
		logger.info("result: false");
		return false;
	}

	@Override
	public String emailByPassword(String email) {
		logger.info("Inside emailByPassword method of userServiceImpl.");
		return userRepository.findPasswordByEmail(email);
	}
}
