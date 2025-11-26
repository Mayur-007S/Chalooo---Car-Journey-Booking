package com.api.service;

import java.util.List;
import java.util.Optional;

import com.api.dto.BookDTO;
import com.api.model.Booking;

public interface BookingService {

	Booking addBooking(BookDTO dto);
	
	List<Booking> getAll();
	
	Optional<Booking> getOne(long pid);
	
	List<Booking> getBookingByPassengerName(long id);
	
	boolean cancelBooking(long book_id);
}
