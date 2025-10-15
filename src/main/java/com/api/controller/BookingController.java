package com.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.BookRequestDTO;
import com.api.model.Booking;
import com.api.model.User;
import com.api.service.BookingService;
import com.api.service.UserService;

@RestController
@RequestMapping("/api/v1/passenger")
public class BookingController {

	@Autowired
	private BookingService service;
	
	@Autowired
	private UserService userService;
	
	private Logger log = LoggerFactory.getLogger(BookingController.class);
	
	@PostMapping("/bookings/add")
	public ResponseEntity<Booking> addBookings(@RequestBody BookRequestDTO dto){
		
		Booking book = service.addBooking(dto);
		if(book != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(book);
		}
		throw new NullPointerException("The Booking is not added because of internal severm error.");
	}
	
	@GetMapping("/bookings/getall")
	public ResponseEntity<List<Booking>> getAllBookings(){
		List<Booking> listofbookings = service.getAll();
		if(!listofbookings.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(listofbookings);
		}
		throw new NotFoundException("The Booking is not added because of internal severm error.");
	}
	
	@GetMapping("/bookings/getbypassengername")
	public ResponseEntity<List<Booking>> getBookingByPassengerName(
			@RequestParam(value = "name",required = true) String name
			){
		User user = userService.UserByUsername(name);
		List<Booking> listofbooking = service.getBookingByPassengerName(user.getId());
		if(user != null && !listofbooking.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(listofbooking);
		}
		throw new NotFoundException("Booking not foud with name: "+name
				+" The given name that user is not found in database.");
		
	}
}
