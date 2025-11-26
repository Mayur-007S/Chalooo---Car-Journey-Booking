package com.api.service;

import java.util.List;
import java.util.Optional;

import com.api.dto.CarDTO;
import com.api.model.Car;

public interface CarService {

	Car addCar(CarDTO cardto);
	
	List<Car> getALLByOwner(String username);
	
	Car updateCar(long cid,CarDTO car);
	
	void deleteCarByOwner(String username, long cid);
	
	Optional<Car> getOneCar(Long cid);
	
}
