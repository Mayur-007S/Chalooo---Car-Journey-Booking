package com.api.dto.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.api.dto.BookDTO;
import com.api.model.Booking;

@Component
public class BookingMapper {

	private Logger log = LoggerFactory.getLogger(BookingMapper.class);
	
	/*
	 * public BookDTO EntityToDto(Booking booking) { return new BookDTO(
	 * booking.getId(), booking.getPassenger().getId(), booking.getSeatsBooked(),
	 * booking.getDate(), booking.getTime(), booking.getStatus() ); }
	 */
	

}
