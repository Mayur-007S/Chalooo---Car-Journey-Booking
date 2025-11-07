package com.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.BookRequestDTO;
import com.api.model.Booking;
import com.api.model.Payment;
import com.api.model.Trip;
import com.api.model.User;
import com.api.repository.BookingRepository;
import com.api.repository.UserRepository;
import com.api.service.BookingService;
import com.api.service.TripService;
import com.api.service.UserService;
import com.api.validation.ObjectValidator;

@Service
public class BookingServiceImpl implements BookingService {

	private Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

	@Autowired
	private ObjectValidator<Object> validator;

	@Autowired
	private BookingRepository repository;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private TripService tripService;

	@Override
	public Booking addBooking(BookRequestDTO dto) {
		log.info("Inside add booking method");
		validator.validate(dto);
		Booking book = new Booking();
		book.setSeatsBooked(dto.seatsBooked());
		book.setDate(dto.date());
		book.setTime(dto.time());
		book.setStatus(dto.status().toUpperCase());
		
		log.info("trip_id: "+dto.trip_id());
		log.info("passenger_id: "+dto.passenger_id());
		
		User user = userService.getOneUser(dto.passenger_id());
		if(user != null) {
			log.info(""+user);
			book.setPassenger(user);
		}
		else { throw new NotFoundException("User not found with passenger id: "+dto.passenger_id()
		+" Please enter available.");}
		
		Trip trip = tripService.getOneTrip(dto.trip_id());
		if(trip != null) {
			log.info(""+trip);
			book.setTrip(trip);
		}
		else { throw new NotFoundException("User not found with passenger id: "
		+dto.trip_id()+ " " +"Please enter available.");}		

	return repository.save(book);

	}

	@Override
	public List<Booking> getAll() {
		log.info("Inside getall bookings method");
		return repository.findAll();
	}

	@Override
	public List<Booking> getBookingByPassengerName(long id) {
		log.info("Inside get Booking By Trip method");
		return repository.findByPassenger(id);
	}

	@Override
	public Optional<Booking> getOne(long pid) {
		// TODO Auto-generated method stub
		return repository.findById(pid);
	}

}
