package com.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.dto.TripRequestDTO;
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
	private ObjectValidator<Trip> objectValidator;
	
	private Logger log = LoggerFactory.getLogger(TripServiceImpl.class);
	
	@Override
	public Trip addTrip(TripRequestDTO dto) {
		Trip trip = new Trip();
	    trip.setSource(dto.getSource());
	    trip.setDestination(dto.getDestination());
	    trip.setDateTime(dto.getDateTime());
	    trip.setTotalSeats(dto.getTotalSeats());
	    trip.setAvailableSeats(dto.getAvailableSeats());

	    User driver = userRepository.findById(dto.getDriver_id())
	            .orElseThrow(() -> new RuntimeException("Driver not found"));
	    Car car = carRepository.findById(dto.getCar_id())
	            .orElseThrow(() -> new RuntimeException("Car not found"));

	    trip.setDriver(driver);
	    trip.setCar(car);

	    return repository.save(trip);
	}

	@Override
	public List<Trip> getALL() {
		log.info("Inside getall trip method");
		return repository.findAll();
	}

	@Override
	public List<Trip> GetBySourceAndDestination(String source, String Desti) {
		log.info("Inside getall trip method");
		return repository.getBySourceAndDestination(source, Desti);
	}

}
