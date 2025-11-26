package com.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.CarDTO;
import com.api.model.Booking;
import com.api.model.Car;
import com.api.model.User;
import com.api.repository.BookingRepository;
import com.api.repository.CarRepository;
import com.api.service.CarService;
import com.api.service.UserService;
import com.api.validation.ObjectValidator;

@Service
public class CarServiceImpl implements CarService {

	@Autowired
	private ObjectValidator<CarDTO> validator;
	
	@Autowired
	private CarRepository repository;
	
	@Autowired
	private UserService userService;
	
	private Logger log = LoggerFactory.getLogger(CarServiceImpl.class);
	
	@Override
	public Car addCar(CarDTO cardto) {
		log.info("Inside add car method");
		validator.validate(cardto);
		Car car = new Car();
		car.setModel(cardto.model());
		car.setPlateNo(cardto.plateNo());
		car.setSeats(cardto.seats());
		User user = userService.getOneUser(cardto.owner_id());
		if(user == null) {
			throw new NotFoundException("Owner with id: "+cardto.owner_id()
			+" not found. Please enter valide ownere id. !!!");
		}
		car.setOwner(user);

		
		return repository.save(car);
	}

	@Override
	public List<Car> getALLByOwner(String username) {
		log.info("Inside get all car method");
		User user = userService.UserByUsername(username);
		if(user == null) { throw new NotFoundException("No user found with owner name: "+username);}
		
		return repository.findByOwnerId(user.getId());
	}

	@Override
	public Car updateCar(long cid,CarDTO cardto) {
		log.info("Inside add car method");
		validator.validate(cardto);
		Car car1 = repository.findById(cid)
				.orElseThrow(() -> new NotFoundException("Car with id: "+cid+" "+"not found. !!!"));
		
		car1.setModel(cardto.model());
		car1.setPlateNo(cardto.plateNo());
		car1.setSeats(cardto.seats());
		
		User user = userService.getOneUser(cardto.owner_id());
		if(user == null) {
			throw new NotFoundException("Owner with id: "+cardto.owner_id()
			+" not found. Please enter valide ownere id. !!!");
		}
		car1.setOwner(user);
		
		return repository.save(car1);
	}

	@Override
	public void deleteCarByOwner(String username,long cid) 
	{
		log.info("Inside delete car method");
		User user = userService.UserByUsername(username);
		if(user == null) { throw new NotFoundException("No user found with owner name: "+username);}
		
		Optional<Car> car = repository.findById(cid);
		if(car.isPresent()) {
			repository.deleteByOwnerId(cid, user.getId());
		}else {
			throw new NotFoundException("Car with id: "+cid+" not found. !!!");
		}
	
	}

	@Override
	public Optional<Car> getOneCar(Long cid) {
		log.info("Inside get one car method");
		return repository.findById(cid);
	}
}
