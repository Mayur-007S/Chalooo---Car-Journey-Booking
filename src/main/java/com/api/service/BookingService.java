package com.api.service;

import java.util.List;

import com.api.dto.BookRequestDTO;
import com.api.model.Booking;

public interface BookingService {

	Booking addBooking(BookRequestDTO dto);
	
	List<Booking> getAll();
	
	List<Booking> getBookingByPassengerName(long id);
	
}
