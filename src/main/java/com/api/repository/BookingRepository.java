package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.model.Booking;
import com.api.model.Trip;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Query(value = "SELECT * FROM bookings WHERE passenger_id = :pid", nativeQuery = true)
	List<Booking> findByPassenger(@Param("pid") long pass_id);
	
	@Query(value = "SELECT * FROM bookings WHERE trip_id =:tripid", nativeQuery = true)
	List<Booking> findByTrip(@Param("tripid") long tripid);
	
	@Query(value = "SELECT b.* FROM bookings b "
			+ "JOIN trips t ON t.id = b.trip_id "
			+ "WHERE b.status = 'CONFIRM' "
			+ "AND t.start_date_time >= NOW() "
			+ "AND t.start_date_time < DATE_ADD(NOW(), INTERVAL 30 MINUTE)", 
			nativeQuery = true)
	List<Booking> findByBookedTripStartTime(@Param("minute") int minute);
}
