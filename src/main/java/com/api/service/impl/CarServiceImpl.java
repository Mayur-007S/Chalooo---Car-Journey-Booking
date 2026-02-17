package com.api.service.impl;

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
import com.api.dto.CarDTO;
import com.api.dto.mapper.CarMapper;
import com.api.dto.mapper.UserMapper;
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
	
	@Autowired private UserMapper userMapper;
	
	@Autowired private CarMapper carMapper;

	private Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

	@Transactional(rollbackFor = { NotFoundException.class })
	@Override
	public Car addCar(CarDTO cardto) {
		log.info("Inside add car method");
		validator.validate(cardto);
		Car car = new Car();
		car.setModel(cardto.model());
		car.setPlateNo(cardto.plateNo());
		car.setSeats(cardto.seats());
		var user = userService.getOneUser(cardto.owner_id());
		if (user == null) {
			throw new NotFoundException("Owner with id: " + cardto.owner_id()
					+ " not found. Please enter valide ownere id. !!!");
		}
		car.setOwner(userMapper.dtoTOuser(user.get()));

		return repository.save(car);
	}

	@Override
	public List<Car> getALLByOwner(String username) {
		log.info("Inside get all car method");
		var userdto = userService.UserByUsername(username);
		if (userdto == null) {
			throw new NotFoundException("No user found with owner name: " + username);
		}

		var car = repository.findByOwnerId(userdto.id());
		car.stream().map(c -> userMapper.userToUserDTO(c.getOwner()))
		.toList();
		return car;
	}

	@Transactional(rollbackFor = { NotFoundException.class })
	@Override
	@CachePut(value = "cars", key = "#cid")
	public Car updateCar(long cid, CarDTO cardto) {
		log.info("Inside add car method");
		validator.validate(cardto);
		Car car1 = repository.findById(cid)
				.orElseThrow(() -> new NotFoundException("Car with id: " + cid + " " + "not found. !!!"));

		car1.setModel(cardto.model());
		car1.setPlateNo(cardto.plateNo());
		car1.setSeats(cardto.seats());

		var user = userService.getOneUser(cardto.owner_id());
		if (user == null) {
			throw new NotFoundException("Owner with id: " + cardto.owner_id()
					+ " not found. Please enter valide ownere id. !!!");
		}
		car1.setOwner(userMapper.dtoTOuser(user.get()));

		return repository.save(car1);
	}

	@Transactional(rollbackFor = { NotFoundException.class })
	@Override
	@CacheEvict(value = "cars", key = "#cid")
	public void deleteCarByOwner(String username, long cid) {
		log.info("Inside delete car method");
		var user = userService.UserByUsername(username);
		if (user == null) {
			throw new NotFoundException("No user found with owner name: " + username);
		}

		if (repository.existsById(cid)) {
			repository.deleteByOwnerId(cid, user.id());
		} else {
			throw new NotFoundException("Car with id: " + cid + " not found. !!!");
		}

	}

	@Override
	@Cacheable(value = "cars", key = "#cid")
	public Optional<Car> getOneCar(Long cid) {
		log.info("Inside get one car method");
		return repository.findById(cid);
	}

	@Override
	public List<CarDTO> getAll() {
		log.info("Inside get all method in car service impl.");
		var car = repository.findAll();
		return carMapper.carTOdto(car);
	}
}
