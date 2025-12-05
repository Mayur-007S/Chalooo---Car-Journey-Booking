package com.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.customeexceptions.NotFoundException;
import com.api.dto.TripDTO;
import com.api.dto.mapper.TripMapper;
import com.api.model.Trip;
import com.api.model.User;
import com.api.repository.BookingRepository;
import com.api.repository.TripRepository;
import com.api.repository.UserRepository;
import com.api.service.TripService;
import com.api.validation.ObjectValidator;

@Service
public class TripServiceImpl implements TripService {

	@Autowired private TripRepository tripRepository;

	@Autowired private UserRepository userRepository;
	
	@Autowired private BookingRepository bookingRepository;

	@Autowired private TripMapper tripMapper;

	@Autowired private ObjectValidator<Object> validator;

	private Logger log = LoggerFactory.getLogger(TripServiceImpl.class);

	@Override
	public TripDTO addTrip(TripDTO dto) {
		log.info("Inside add trip method");
		Trip trip = tripMapper.tripDTOtoTrip(dto);
		validator.validate(trip);
		Trip trip2 = tripRepository.save(trip);
		return tripMapper.tripToTripDto(trip2);
	}

	@Override
	public TripDTO updateTrip(long tid, TripDTO dto) {
		log.info("Inside update trip method");
		validator.validate(dto);
		Trip trip = tripRepository.findById(tid)
				.orElseThrow(() -> new NotFoundException("Trip with id: " + tid + " " + "not found exception. !!!"));

		trip.setStartDateTime(dto.startDateTime());
		trip.setDepartureDateTime(dto.departureDateTime());
		trip.setTotalSeats(dto.totalSeats());
		trip.setAvailableSeats(dto.availableSeats());
		
		Trip trip2 = tripRepository.save(trip);
		return tripMapper.tripToTripDto(trip2);
	}
	
	@Override
	public TripDTO updateOwnTrip(long tripid, long driverId, TripDTO tripdto) {
		log.info("Inside update trip method");
		validator.validate(tripdto);
		
		Trip trip = tripRepository.findByDriverId(driverId).get().stream()
					.filter(t -> t.getId() == tripid)
					.findFirst()
					.orElseThrow(() -> new NotFoundException("No Trip Found by Driver"));
		
		if(trip == null) { throw new NotFoundException("No Trip For Driver with id: "+driverId); }
		
		trip.setStartDateTime(tripdto.startDateTime());
		trip.setDepartureDateTime(tripdto.departureDateTime());
		trip.setTotalSeats(tripdto.totalSeats());
		trip.setAvailableSeats(tripdto.availableSeats());
		
		
		Trip trip2 = tripRepository.save(trip);
		return tripMapper.tripToTripDto(trip2);
	}
	
	@Override
	public List<TripDTO> getALL() {
		log.info("Inside getall trip method");
		List<Trip> trips = tripRepository.findAll();

		if (trips.isEmpty()) {
			throw new NotFoundException("No Trips Found in database. " + "Please try later.");
		}

		List<TripDTO> dto = tripMapper.tripToTripDto(trips);

		return dto;
	}

	@Override
	public List<TripDTO> GetBySourceAndDestination(String source, String Desti) {
		log.info("Inside get by source and destination trip method");
		List<Trip> trips = tripRepository.getBySourceAndDestination(source.toLowerCase(), Desti.toLowerCase());

		if (trips.isEmpty()) {
			throw new NotFoundException("No Trips Found with source: " + source + " Destination: " + Desti + " !!!");
		}

		List<TripDTO> dto = tripMapper.tripToTripDto(trips);

		return dto;
	}

	@Override
	public TripDTO getOneTrip(int id) {
		log.info("Inside get by id trip method");
		Trip trip = tripRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Trip not found with trip id: " + id));
		return tripMapper.tripToTripDto(trip);
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
		return tripMapper.tripToTripDto(listoftrips);

	}

	@Override
	public List<TripDTO> GetBySourceAndDestOrDate(String source, String dest, String date) {
		log.info("Inside Get by Source And Destination Or Date Method");
		date = "%" + date + "%";
		log.info(date);

		List<Trip> trips = tripRepository.getBySourceAndDestinationOrDate(source, dest, date);
		return trips.stream().map(tripMapper::tripToTripDto).toList();
	}

	@Override
	public boolean deleteTrip(long tripId) {
		log.info("Inside CancelTrip method");
		
		int rows = tripRepository.deleteTripIfNoBookings(tripId);
		log.info("Rows effected: "+rows);
		if(rows > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean deleteOwnTrip(long driverId, long tripId) {
		log.info("Inside deleteOwnTrip method");
		
		int rows = tripRepository.deleteOwnTripIfNoBookings(driverId, tripId);
		log.info("Rows effected: "+rows);
		if(rows > 0) {
			return true;
		}else {
			return false;
		}
		
	}

}
