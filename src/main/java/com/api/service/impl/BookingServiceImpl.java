package com.api.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.BookDTO;

import com.api.dto.mapper.TripMapper;
import com.api.dto.mapper.UserMapper;
import com.api.mail.service.MailService;
import com.api.model.Booking;

import com.api.model.Trip;
import com.api.model.User;
import com.api.repository.BookingRepository;
import com.api.repository.TripRepository;
import com.api.repository.UserRepository;
import com.api.service.BookingService;
import com.api.service.TripService;
import com.api.service.UserService;
import com.api.validation.ObjectValidator;

import jakarta.mail.MessagingException;

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
	private TripRepository tripRepository;
	
	@Autowired
	private TripService tripService;

	@Autowired
	private TripMapper mapper;
	
	@Autowired
	private MailService mailService;
	
	@Autowired private UserMapper userMapper;

	
	@Transactional(rollbackFor = { IllegalStateException.class, NotFoundException.class })
	@CachePut(cacheNames = {"bookings", "trips"}, key = "#result.id")
	@Override
	public Booking addBooking(BookDTO dto) throws MessagingException {
		log.info("Inside add booking method");
		validator.validate(dto);

		// ✅ Fix: Fetch trip directly from repository instead of mapping from DTO
		Trip trip = tripRepository.findById(dto.trip_id())
				.orElseThrow(() -> new NotFoundException("Trip not found for id: " + dto.trip_id()));

		if (trip.getDepartureDateTime() != null && trip.getDepartureDateTime().isBefore(LocalDateTime.now())) {
	        throw new IllegalStateException("Cannot book past trips");
	    }

	    if (trip.getAvailableSeats() < dto.seatsBooked()) {
	        throw new IllegalStateException("Not enough seats available");
	    }


		var user = userService.getOneUser(dto.passenger_id());
		if (user == null) {
			throw new NotFoundException("User not found with passenger id: " + 
						dto.passenger_id());
		}

		Booking book = new Booking();
		book.setTrip(trip);
		book.setSeatsBooked(dto.seatsBooked());
		book.setDate(dto.date());
		book.setTime(dto.time());
		book.setStatus(dto.status().toUpperCase());
		book.setPassenger(userMapper.dtoTOuser(user));

		trip.setAvailableSeats(trip.getAvailableSeats() - dto.seatsBooked());

		tripRepository.save(trip);
		
		sendNotification(book);
		
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
	@Cacheable(value = "bookings", key = "#bid")
	public Optional<Booking> getOne(long bid) {
		log.info("Inside get one Booking method");
		return repository.findById(bid);
	}

	@Override
	@Transactional
	@CacheEvict(value = "bookings", key = "#book_id")
	public boolean cancelBooking(long book_id) {
		log.info("Inside cancel booking method");
		Booking book = repository.findById(book_id).get();
		if (book != null && book.getStatus().equalsIgnoreCase("CONFIRM")) {
			book.setStatus("CANCEL");
			repository.save(book);
			return true;
		}
		return false;
	}

	@Override
	public List<Booking> getByTripId(long tripId) {
		log.info("Inside getByTripId method");
		return repository.findByTrip(tripId);
	}
	
	public void sendNotification(Booking book) throws MessagingException {
		mailService.confirmEmailtoPassenger(book.getPassenger().getEmail(), book);
		mailService.confirmEmailtoDriver(book);
	}

}
