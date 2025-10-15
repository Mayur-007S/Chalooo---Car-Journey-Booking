package com.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.TripRequestDTO;
import com.api.dto.TripResponseDTO;
import com.api.model.Trip;
import com.api.service.TripService;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/trip")
public class TripController {

	@Autowired
	private TripService service;
	
	private Logger log = LoggerFactory.getLogger(TripController.class);
	
	@PostMapping("/add")
	public ResponseEntity<TripRequestDTO> addTrip(@RequestBody TripRequestDTO dto){
		log.info("Inside add Trip Controller");
		Trip t = service.addTrip(dto);
		if(t != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
		}
		throw new NullPointerException("Trip Not Added Object is null. "
				+ "Please enter information properly"+dto);
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<TripResponseDTO>> getAllTrips(){
		log.info("Inside get all Trip Controller");
		List<TripResponseDTO> trips = service.getALL();
		if(!trips.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(trips);
		}
		throw new NotFoundException("No Trips found in database. | "
				+ "please retry again after 5 minutes");
	}
	
	@GetMapping("/getSD")
	public ResponseEntity<List<TripResponseDTO>> getTripBySourceAndDestination(
			@RequestParam(value = "source", required = true) String source,
			@RequestParam(value = "dest", required = true) String dest
			){
		log.info("Inside get Trip by source and destination Controller");
		List<TripResponseDTO> trips = service.GetBySourceAndDestination(source, dest);
		if(!trips.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(trips);
		}
		throw new NotFoundException("No Trips found in database with source: "+source+" "
		+"Destination: "+dest+" | "+"Please enter available source and destination");
	}
	
	
}
