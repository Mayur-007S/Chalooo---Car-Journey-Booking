package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    @Query(value = "SELECT * FROM users WHERE username = :u", nativeQuery = true)
    User findByUsername(@Param("u") String u);
	
	User findByEmail(String email);
	
	User findById(int id);
	
	Optional<User> findById(Long id);
	
	Optional<User> findByPhone(String phone);
	
	@Query(value = "SELECT * FROM users WHERE username = :username OR "
			+ "email = :email OR phone = :phone", nativeQuery = true)
	Optional<List<User>> userExistOrNot(
			@Param("username") String username, 
			@Param("email") String email, 
			@Param("phone") String phone);
 
	@Query(value = "SELECT password FROM users WHERE email = :email", nativeQuery = true)
	String findPasswordByEmail(@Param("email") String email);
}
