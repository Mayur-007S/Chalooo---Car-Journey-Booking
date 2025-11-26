package com.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Payment findById(int id);

	@Query(value = "SELECT * FROM chaloo_db.payments as " + "p where p.booking_id=:bookingId", nativeQuery = true)
	List<Payment> findByBookingId(@Param("bookingId") long booking_id);

	@Query(value = "SELECT b.id,b.amount,b.method,b.status,b.date,b.time,b.booking_id "
			+ "FROM bookings as p, payments as b "
			+ "where p.id = b.booking_id and p.trip_id= :tripid", nativeQuery = true)
	List<Payment> findByTripId(@Param("tripid") long trip_id);

	@Query(value = "SELECT p.* FROM payments as p "
			+ "join bookings as b on p.booking_id = b.id "
			+ "join trips as t on b.trip_id = t.id "
			+ "where t.driver_id =:driverid", nativeQuery = true)
	List<Payment> findPaymentForDriver(@Param("driverid") int driverid);
}
