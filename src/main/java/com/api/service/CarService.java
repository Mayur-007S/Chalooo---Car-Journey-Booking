package com.api.service;

import java.util.List;

import com.api.dto.CarDTO;
import com.api.model.Car;

public interface CarService {

	Car addCar(CarDTO cardto);
	
	List<Car> getALLByOwner(String username);
	
	Car updateCar(long cid,CarDTO car);
	
}
