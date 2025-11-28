package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip>{

	@Query(value = "SELECT * FROM trips WHERE source = :s and destination = :d", nativeQuery = true)
	List<Trip> getBySourceAndDestination(@Param("s") String source, @Param("d") String Desti);
	
	Optional<Trip> findById(int id);
	
	@Query(value = "SELECT * FROM trips WHERE driver_id = :did", nativeQuery = true)
	Optional<List<Trip>> findByDriverId(@Param(value = "did") long did);
	
	@Query(value = "SELECT * FROM chaloo_db.trips WHERE "
			+ "source =:source AND destination =:dest "
			+ "AND start_date_time LIKE :date;", nativeQuery = true)
	List<Trip> getBySourceAndDestinationOrDate(
			@Param("source") String source, 
			@Param("dest") String dest,
			@Param("date") String date
				);
	
}
