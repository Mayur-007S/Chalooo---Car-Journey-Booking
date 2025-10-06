package com.api.service;

import java.util.List;

import com.api.model.Car;

public interface CarService {

	Car addCar(Car car);
	
	List<Car> getALl();
	
}
