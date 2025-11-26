package com.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.BookDTO;
import com.api.model.Booking;
import com.api.model.User;
import com.api.service.BookingService;
import com.api.service.UserService;

@RestController
@RequestMapping("api/v1/bookings")
public class BookingController {

	@Autowired
	private BookingService service;
	
	@Autowired
	private UserService userService;
	
	private Logger log = LoggerFactory.getLogger(BookingController.class);
	
	@PreAuthorize("hasAnyRole('PASSENGER','ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<Booking> addBookings(@RequestBody BookDTO dto){
		log.info("Inside add bookings method");
		Booking book = service.addBooking(dto);
		if(book != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(book);
		}
		throw new NullPointerException("The Booking is not added because of internal severm error.");
	}
	
	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@GetMapping("/getall")
	public ResponseEntity<List<Booking>> getAllBookings(){
		log.info("Inside get all bookings method");
		List<Booking> listofbookings = service.getAll();
		if(!listofbookings.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(listofbookings);
		}
		throw new NotFoundException("The Booking is not added because of internal severm error.");
	}
	
	@PreAuthorize("hasAnyRole('PASSENGER','DRIVER','ADMIN')")
	@GetMapping("/getbypassengername")
	public ResponseEntity<List<Booking>> getBookingByPassengerName(
			@RequestParam(value = "name",required = true) String name
			){
		log.info("Inside get by passenger name bookings method");
		User user = userService.UserByUsername(name.toLowerCase());
		log.info("User: "+user);
		if(user == null) {
			throw new NotFoundException("Booking not foud with name: "+name
					+" The given name that user is not found in database.");
		}
		log.info("User ");
		List<Booking> listofbooking = service.getBookingByPassengerName(user.getId());
		log.info("listofbookings: "+listofbooking);
		if(listofbooking == null) {
			throw new NotFoundException("Booking not foud with name: "+name
					+" The given name that user is not found in database.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(listofbooking);
	}
	
	@PreAuthorize("hasAnyRole('PASSENGER','ADMIN')")
	@PostMapping("/cancel")
	public ResponseEntity<String> cancelBooking(@RequestParam long bookid){
		log.info("Inside cancel booking controller");
		if(service.cancelBooking(bookid)) {
			return ResponseEntity.status(HttpStatus.OK).body("Booking is cancel successfully. Refund will be get in 24 hours.");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Booking is not cancel due to some issue. Please try again");
	}
	
}
