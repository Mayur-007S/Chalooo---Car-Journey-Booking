package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
