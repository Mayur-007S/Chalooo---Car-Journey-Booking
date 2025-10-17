package com.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.CarDTO;
import com.api.model.Car;
import com.api.service.CarService;

@RestController
@RequestMapping("/api/v1/driver")
public class CarController {

	@Autowired
	private CarService carService;

	private Logger log = LoggerFactory.getLogger(CarController.class);

	@PostMapping("/car/add")
	public ResponseEntity<Car> addCars(@RequestBody CarDTO cardto) {
		log.info("Inside Add Car method");
		Car car1 = carService.addCar(cardto);
		if (car1 != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(car1);
		}
		throw new NullPointerException("Car is not added internal serverl error. !!!");
	}

	@GetMapping("/car/getbyownername")
	public ResponseEntity<List<Car>> getAllCars(@RequestParam(value = "name", required = true) String name) {
		log.info("Inside get all car controller");
		List<Car> listofcars = carService.getALLByOwner(name);
		if (!listofcars.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(listofcars);
		}
		throw new NotFoundException("No cars found with owner name: " + name);
	}

	@PutMapping("/car/update")
	public ResponseEntity<Car> updateCars(
			@RequestBody CarDTO cardto, 
			@RequestParam(value = "cid", required = true) long cid) 
	{
		log.info("Inside Add Car method");
		Car car1 = carService.updateCar(cid,cardto);
		if (car1 != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(car1);
		}
		throw new NullPointerException("Car is not updated internal serverl error. !!!");
	}

}
