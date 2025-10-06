package com.api.service;

import java.util.List;

import com.api.model.Booking;

public interface BookingService {

	Booking addBooking(Booking booking);
	
	List<Booking> getAll();
	
	Booking getBookingByTrip(String trip_name);
	
}
