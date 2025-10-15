package com.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.TripRequestDTO;
import com.api.dto.TripResponseDTO;
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
	            .orElseThrow(() -> new NotFoundException("Driver not found"));
	    Car car = carRepository.findById(dto.getCar_id())
	            .orElseThrow(() -> new NotFoundException("Car not found"));

	    trip.setDriver(driver);
	    trip.setCar(car);

	    return repository.save(trip);
	}

	@Override
	public List<TripResponseDTO> getALL() {
		log.info("Inside getall trip method");
		List<Trip> trips = repository.findAll();

		List<TripResponseDTO> dto = trips.stream()
		    .map(trip -> new TripResponseDTO(
		        trip.getId(),
		        trip.getSource(),
		        trip.getDestination(),
		        trip.getDateTime(),
		        trip.getTotalSeats(),
		        trip.getAvailableSeats()
		    ))
		    .collect(Collectors.toList());

		
		return dto;
	}

	@Override
	public List<TripResponseDTO> GetBySourceAndDestination(String source, String Desti) {
		log.info("Inside getall trip method");
		List<Trip> trips = repository.findAll();

		List<TripResponseDTO> dto = trips.stream()
		    .map(trip -> new TripResponseDTO(
		        trip.getId(),
		        trip.getSource(),
		        trip.getDestination(),
		        trip.getDateTime(),
		        trip.getTotalSeats(),
		        trip.getAvailableSeats()
		    ))
		    .collect(Collectors.toList());

		return dto;
	}

	@Override
	public Trip getOneTrip(int id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

}
