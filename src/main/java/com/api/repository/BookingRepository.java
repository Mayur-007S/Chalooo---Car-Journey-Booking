package com.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Query(value = "SELECT * FROM bookings WHERE passenger_id = :pid", nativeQuery = true)
	List<Booking> findByPassenger(@Param("pid") long pass_id);
	
}
