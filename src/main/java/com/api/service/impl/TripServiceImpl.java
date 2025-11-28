package com.api.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.TripDTO;
import com.api.dto.mapper.TripMapper;
import com.api.model.Car;
import com.api.model.Trip;
import com.api.model.User;
import com.api.repository.CarRepository;
import com.api.repository.TripRepository;
import com.api.repository.UserRepository;
import com.api.service.BookingService;
import com.api.service.TripService;
import com.api.validation.ObjectValidator;

@Service
public class TripServiceImpl implements TripService {

	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private TripMapper mapper;

	@Autowired
	private ObjectValidator<Object> validator;

	private Logger log = LoggerFactory.getLogger(TripServiceImpl.class);

	@Override
	public TripDTO addTrip(TripDTO dto) {
		log.info("Inside add trip method");
		Trip trip = mapper.tripDTOtoTrip(dto);
		validator.validate(trip);
		Trip trip2 = tripRepository.save(trip);
		return mapper.tripToTripDto(trip2);
	}

	@Override
	public List<TripDTO> getALL() {
		log.info("Inside getall trip method");
		List<Trip> trips = tripRepository.findAll();

		if (trips.isEmpty()) {
			throw new NotFoundException("No Trips Found in database. " + "Please try later.");
		}

		List<TripDTO> dto = mapper.tripToTripDto(trips);

		return dto;
	}

	@Override
	public List<TripDTO> GetBySourceAndDestination(String source, String Desti) {
		log.info("Inside get by source and destination trip method");
		List<Trip> trips = tripRepository.getBySourceAndDestination(source.toLowerCase(), Desti.toLowerCase());

		if (trips.isEmpty()) {
			throw new NotFoundException("No Trips Found with source: " + source + " Destination: " + Desti + " !!!");
		}

		List<TripDTO> dto = mapper.tripToTripDto(trips);

		return dto;
	}

	@Override
	public TripDTO getOneTrip(int id) {
		log.info("Inside get by id trip method");
		Trip trip = tripRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Trip not found with trip id: " + id));
		return mapper.tripToTripDto(trip);
	}

	@Override
	public TripDTO updateTrip(long tid, TripDTO dto) {
		log.info("Inside update trip method");
		validator.validate(dto);
		Trip trip = tripRepository.findById(tid)
				.orElseThrow(() -> new NotFoundException("Trip with id: " + tid + " " + "not found exception. !!!"));

		trip = mapper.tripDTOtoTrip(dto);
		Trip trip2 = tripRepository.save(trip);
		return mapper.tripToTripDto(trip2);
	}

	@Override
	public List<TripDTO> getByDriverName(String driverName) {
		log.info("Inside get By Driver Name Method");
		User user = userRepository.findByUsername(driverName);
		if (user == null) {
			throw new NotFoundException("Driver Not Found");
		}

		List<Trip> listoftrips = tripRepository.findByDriverId(user.getId())
				.orElseThrow(() -> new NotFoundException("Driver with name: " + driverName + ". Trips Not Found..."));
		return mapper.tripToTripDto(listoftrips);

	}

	@Override
	public List<TripDTO> GetBySourceAndDestOrDate(String source, String dest, String date) {
		log.info("Inside Get by Source And Destination Or Date Method");
		date = "%" + date + "%";
		log.info(date);

		List<Trip> trips = tripRepository.getBySourceAndDestinationOrDate(source, dest, date);
		return trips.stream().map(mapper::tripToTripDto).toList();
	}

	@Override
	public boolean CancelTrip(long tripId) {
		log.info("Inside CancelTrip method");

		var bookings = bookingService.getByTripId(tripId);
		log.info("" + bookings);

		if (bookings.isEmpty()) {
			try {
				tripRepository.deleteById(tripId);
				log.info("Trip deleted successfully");
				return true;

			} catch (DataIntegrityViolationException e) {
				log.error("Cannot delete trip due to foreign key constraints", e);
				return false;
			}
		}
		
		log.warn("Cannot cancel trip. Bookings exist for tripId: " + tripId);
		return false;
	}

}
