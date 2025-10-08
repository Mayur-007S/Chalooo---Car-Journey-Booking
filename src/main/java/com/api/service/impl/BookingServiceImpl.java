package com.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.model.Booking;
import com.api.model.User;
import com.api.repository.BookingRepository;
import com.api.repository.UserRepository;
import com.api.service.BookingService;
import com.api.validation.ObjectValidator;

@Service
public class BookingServiceImpl implements BookingService {

	private Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
	
	@Autowired
	private ObjectValidator<Booking> validator;
	
	private BookingRepository repository;
	
	private UserRepository userRepo;
	
	@Override
	public Booking addBooking(Booking booking) {
		log.info("Inside add booking method");
		validator.validate(booking);
		log.info("Validating booking object");
		log.info("Call save method");
		log.info("Exit from add booking method");
		return repository.save(booking);
	}

	@Override
	public List<Booking> getAll() {
		log.info("Inside getall bookings method");
		log.info("Call get all bookings method");
		log.info("Exit from get all bookings method");
		return repository.findAll();
	}

	@Override
	public Booking getBookingByPassengerName(String passenger_name) {
		log.info("Inside get Booking By Trip method");
		log.info("Call get all method");
		log.info("Exit from get all method");
		User user = userRepo.findByUsername(passenger_name);
		return repository.findByPassenger(user.getId());
	}

}
