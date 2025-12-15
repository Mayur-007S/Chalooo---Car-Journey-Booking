package com.api.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.api.customeexceptions.NotFoundException;
import com.api.dto.TripDTO;
import com.api.model.Car;
import com.api.model.Trip;
import com.api.model.User;
import com.api.repository.CarRepository;
import com.api.repository.UserRepository;
import com.api.service.CarService;
import com.api.service.UserService;

@Component
public class TripMapper {

	@Autowired
	private  CarService carService;
	
	@Autowired
	private  UserService userService;
	
	private  Logger log = LoggerFactory.getLogger(TripMapper.class);
	

	public TripDTO tripToTripDto(Trip trip) {
		log.info("Inside Trip to DTO Method");
		TripDTO tripdto = new TripDTO(
				trip.getId(),
			    trip.getSource(),
			    trip.getDestination(),
			    trip.getStartDateTime(),
			    trip.getDepartureDateTime(),
			    trip.getTotalSeats(),
			    trip.getTotalSeats(),
			    trip.getDriver().getId(),
			    trip.getCar().getId()
				);
			return tripdto;	
	}
	
	public List<TripDTO> tripToTripDto(List<Trip> trips) {
		log.info("Inside list Trip to list DTO Method");
		List<TripDTO> dto = trips.stream()
				.map(trip -> new TripDTO(
						trip.getId(), 
						trip.getSource(), 
						trip.getDestination(), 
						trip.getStartDateTime(),
						trip.getDepartureDateTime(),
						trip.getTotalSeats(), 
						trip.getAvailableSeats(), 
						trip.getDriver().getId(),
						trip.getCar().getId()))
				.collect(Collectors.toList());
			return dto;	
	}

	
	public Trip tripDTOtoTrip(TripDTO dto) {
		log.info("Inside DTO to Trip Method");
		Trip trip = new Trip();
		trip.setSource(dto.source());
		trip.setStartDateTime(dto.startDateTime());
		trip.setDepartureDateTime(dto.departureDateTime());
		trip.setDestination(dto.destination());
		trip.setAvailableSeats(dto.availableSeats());
		trip.setTotalSeats(dto.totalSeats());
		User driver = userService.getOneUser(dto.driver_id())
				.orElseThrow(() -> new NotFoundException("Driver not found with id: " + dto.driver_id()));
		Car car = carService.getOneCar(dto.car_id())
				.orElseThrow(() -> new NotFoundException("Car not found with id: " + dto.car_id()));

		trip.setDriver(driver);
		trip.setCar(car);
		
		return trip;
	}
}
