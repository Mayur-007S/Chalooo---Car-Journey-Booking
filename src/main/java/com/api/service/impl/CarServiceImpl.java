package com.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.model.Booking;
import com.api.model.Car;
import com.api.repository.BookingRepository;
import com.api.repository.CarRepository;
import com.api.service.CarService;
import com.api.validation.ObjectValidator;

@Service
public class CarServiceImpl implements CarService {

	@Autowired
	private ObjectValidator<Car> validator;
	
	private CarRepository repository;
	
	private Logger log = LoggerFactory.getLogger(CarServiceImpl.class);
	
	@Override
	public Car addCar(Car car) {
		log.info("Inside add car method");
		validator.validate(car);
		log.info("Validating car object");
		log.info("Call save method");
		log.info("Exit from add car method");
		return null;
	}

	@Override
	public List<Car> getALl() {
		log.info("Inside get all car method");
		log.info("Call get all car method");
		log.info("Exit from get all car method");
		return repository.findAll();
	}

}
