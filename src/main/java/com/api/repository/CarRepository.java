package com.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

	@Query(value = "SELECT * FROM cars where owner_id=:id", nativeQuery = true)
	List<Car> findByOwnerId(@Param("id") long id);
	
}
