package com.api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.model.Trip;

import jakarta.transaction.Transactional;

public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip>{

	@Modifying
	@Query(value = "SELECT * FROM trips WHERE source = :s and destination = :d", nativeQuery = true)
	List<Trip> getBySourceAndDestination(@Param("s") String source, @Param("d") String Desti);
	
	Optional<Trip> findById(int id);
	
	@Modifying
	@Query(value = "SELECT * FROM trips WHERE driver_id = :did", nativeQuery = true)
	Optional<List<Trip>> findByDriverId(@Param(value = "did") long did);
	
	@Query(value = "SELECT * FROM trips WHERE driver_id = :did", nativeQuery = true)
	Page<Trip> findByDriverId(@Param(value = "did") long did, Pageable pageable);
	
	Page<Trip> findAll(Pageable pageable);
	
	@Modifying
	@Query(value = "SELECT * FROM chaloo_db.trips WHERE "
			+ "source =:source AND destination =:dest "
			+ "AND start_date_time LIKE :date;", nativeQuery = true)
	List<Trip> getBySourceAndDestinationOrDate(
			@Param("source") String source, 
			@Param("dest") String dest,
			@Param("date") String date
				);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM trips as t WHERE t.id=:tripId "
			+ "AND t.driver_id=:driverId "
			+ "AND NOT EXISTS (SELECT 1 FROM bookings as b "
			+ "WHERE b.trip_id=:tripId);", nativeQuery = true)
	int deleteOwnTripIfNoBookings(@Param("driverId") long driverId,@Param("tripId") long tripId);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM trips as t WHERE t.id=:tripId "
			+ "AND NOT EXISTS (SELECT 1 FROM bookings as b "
			+ "WHERE b.trip_id=:tripId)", nativeQuery = true)
	int deleteTripIfNoBookings(@Param("tripId") long tripId);
	
	
	
}
