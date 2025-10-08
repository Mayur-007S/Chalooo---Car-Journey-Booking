package com.api.repository;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long>{

	@Query(value = "SELECT * FROM trips WHERE source = :s and destination = :d", nativeQuery = true)
	List<Trip> getBySourceAndDestination(@Param("s") String source, @Param("d") String Desti);
	
}
