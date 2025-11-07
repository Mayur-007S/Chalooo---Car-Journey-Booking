package com.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.customeexceptions.NotFoundException;
import com.api.dto.TripDTO;
import com.api.model.Car;
import com.api.model.Trip;
import com.api.model.User;
import com.api.repository.CarRepository;
import com.api.repository.TripRepository;
import com.api.repository.UserRepository;
import com.api.service.TripService;
import com.api.validation.ObjectValidator;

@Service
public class TripServiceImpl implements TripService {

	@Autowired
	private TripRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private ObjectValidator<Object> validator;

	private Logger log = LoggerFactory.getLogger(TripServiceImpl.class);

	@Override
	public Trip addTrip(TripDTO dto) {
		log.info("Inside add trip method");
		Trip trip = new Trip();
		trip.setSource(dto.source().toLowerCase());
		trip.setDestination(dto.destination().toLowerCase());
		trip.setDateTime(dto.dateTime());
		trip.setTotalSeats(dto.totalSeats());
		trip.setAvailableSeats(dto.availableSeats());

		User driver = userRepository.findById(dto.driver_id())
				.orElseThrow(() -> new NotFoundException("Driver not found"));
		Car car = carRepository.findById(dto.car_id()).orElseThrow(() -> new NotFoundException("Car not found"));

		trip.setDriver(driver);
		trip.setCar(car);

		validator.validate(trip);

		return repository.save(trip);
	}

	@Override
	public List<TripDTO> getALL() {
		log.info("Inside getall trip method");
		List<Trip> trips = repository.findAll();

		if (trips.isEmpty()) {
			throw new NotFoundException("No Trips Found in database. " + "Please try later.");
		}

		List<TripDTO> dto = trips.stream()
				.map(trip -> new TripDTO(trip.getId(), trip.getSource(), trip.getDestination(), trip.getDateTime(),
						trip.getTotalSeats(), trip.getAvailableSeats(), trip.getCar().getId(),
						trip.getDriver().getId()))
				.collect(Collectors.toList());

		return dto;
	}

	@Override
	public List<TripDTO> GetBySourceAndDestination(String source, String Desti) {
		log.info("Inside get by source and destination trip method");
		List<Trip> trips = repository.getBySourceAndDestination(source.toLowerCase(), Desti.toLowerCase());

		if (trips.isEmpty()) {
			throw new NotFoundException("No Trips Found with source: " + source + " Destination: " + Desti + " !!!");
		}

		List<TripDTO> dto = trips.stream()
				.map(trip -> new TripDTO(trip.getId(), trip.getSource(), trip.getDestination(), trip.getDateTime(),
						trip.getTotalSeats(), trip.getAvailableSeats(), trip.getCar().getId(),
						trip.getDriver().getId()))
				.collect(Collectors.toList());

		return dto;
	}

	@Override
	public Trip getOneTrip(int id) {
		log.info("Inside get by id trip method");
		return repository.findById(id);
	}

	@Override
	public Trip updateTrip(long tid, TripDTO dto) {
		log.info("Inside update trip method");
		validator.validate(dto);
		Trip trip = repository.findById(tid)
				.orElseThrow(() -> new NotFoundException("Trip with id: " + tid + " " + "not found exception. !!!"));
		trip.setSource(dto.source().toLowerCase());
		trip.setDestination(dto.destination().toLowerCase());
		trip.setDateTime(dto.dateTime());
		trip.setTotalSeats(dto.totalSeats());
		trip.setAvailableSeats(dto.availableSeats());

		User driver = userRepository.findById(dto.driver_id())
				.orElseThrow(() -> new NotFoundException("Driver not found"));
		Car car = carRepository.findById(dto.car_id()).orElseThrow(() -> new NotFoundException("Car not found"));

		trip.setDriver(driver);
		trip.setCar(car);

		return repository.save(trip);
	}

	@Override
	public List<Trip> getByDriverName(String driverName) {
		log.info("Inside get By Driver Name Method");
		User user = userRepository.findByUsername(driverName);
		if (user == null) {
			throw new NotFoundException("Driver Not Found");
		}
		List<Trip> listoftrips = repository.findByDriverId(user.getId());
		if (!listoftrips.isEmpty()) {
			return listoftrips;
		}
		throw new NotFoundException("Driver with name: " + driverName + ". Trips Not Found");
	}

}
