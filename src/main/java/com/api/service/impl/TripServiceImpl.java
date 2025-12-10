package com.api.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
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

import jakarta.annotation.PostConstruct;

@Service
public class TripServiceImpl implements TripService {

	@Autowired private TripRepository tripRepository;

	@Autowired private UserRepository userRepository;

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
		Trip trip = tripRepository.findAll()
				.stream()
				.filter(t -> t.getId() != null && t.getId() == tid)
				.findFirst()
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
		/* List<Trip> trips = tripRepository.findAll(); */
		 List<Trip> trips = tripRepository.findAll();

		if (trips.isEmpty()) {
			throw new NotFoundException("No Trips Found in database. " + "Please try later.");
		}
		
		return tripMapper.tripToTripDto(trips);
	}

	@Override
	public List<TripDTO> GetBySourceAndDestination(String source, String Desti) {
		log.info("Inside get by source and destination trip method");
//		List<Trip> trips = tripRepository.getBySourceAndDestination(source.toLowerCase(), Desti.toLowerCase());
		List<TripDTO> trips = tripRepository.findAll().stream()
				.filter(t -> t.getSource() != null && t.getDestination() != null)
				.filter(t -> t.getSource().equalsIgnoreCase(source) && t.getDestination().equalsIgnoreCase(Desti))
				.map(tripMapper::tripToTripDto)
				.toList();
		
		if (trips.isEmpty()) {
			throw new NotFoundException("No Trips Found with source: " + source + " Destination: " + Desti + " !!!");
		}

		return trips;
	}

	@Override
	public TripDTO getOneTrip(int tripid) {
		log.info("Inside get by id trip method");
		return tripRepository.findAll()
				.stream()
				.filter(t -> t.getId() != null && t.getId() == tripid)
				.findFirst()
				.map(tripMapper::tripToTripDto)
				.orElseThrow(() -> new NotFoundException("Trip Not Found with trip id: "+tripid));

	}

	@Override
	public List<TripDTO> getByDriverName(String driverName) {
		log.info("Inside get By Driver Name Method");
		User user = userRepository.findAll()
				.stream()
				.filter(u -> u.getUsername() != null && u.getUsername().equalsIgnoreCase(driverName))
				.findFirst()
				.orElseThrow(() -> new NotFoundException("Driver Not Found"));

		List<TripDTO> listoftrips = tripRepository.findAll().stream()
				.filter(t -> t.getDriver().getId() != null && t.getDriver().getId() == user.getId())
				.map(tripMapper::tripToTripDto)
				.toList();
		
			return listoftrips;
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

	/*
	 * @Override public Page<TripDTO> searchTrips(String origin, String destination,
	 * LocalDate date, Pageable pageable) { Specification<Trip> spec =
	 * TripSpecification.filterBy(origin, destination, date); return
	 * tripRepository.findAll(spec, pageable).map(tripMapper::toDto); }
	 */
}
