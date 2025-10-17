package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

//    @Query(value = "SELECT * FROM users WHERE username = :u", nativeQuery = true)
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User findById(int id);

}
