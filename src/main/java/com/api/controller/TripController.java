package com.api.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.api.customeexceptions.NotFoundException;
import com.api.dto.TripDTO;
import com.api.service.TripService;

@RestController
@RequestMapping("/api/v1/trip")
public class TripController {

	@Autowired
	private TripService service;

	private Logger log = LoggerFactory.getLogger(TripController.class);

	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<TripDTO> addTrip(@RequestBody TripDTO dto) {
		log.info("Inside add Trip Controller");
		System.out.println("Trip: " + dto);
		TripDTO dto2 = service.addTrip(dto);
		System.out.println("Trip: " + dto2);
		if (dto2 != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(dto2);
		}
		throw new NullPointerException("Trip Not Added Object is null. " + "Please enter information properly" + dto);
	}

	@PreAuthorize("hasAnyRole('PASSENGER','DRIVER','ADMIN')")
	@GetMapping("/getall")
	public ResponseEntity<List<TripDTO>> getAllTrips() {
		log.info("Inside get all Trip Controller");
		List<TripDTO> trips = service.getALL();
		if (!trips.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(trips);
		}
		throw new NotFoundException("No Trips found in database. | " + "please retry again after 5 minutes");
	}

	@PreAuthorize("hasAnyRole('PASSENGER','DRIVER','ADMIN')")
	@GetMapping("/getSD")
	public ResponseEntity<List<TripDTO>> getTripBySourceAndDestination(
			@RequestParam(value = "source", required = true) String source,
			@RequestParam(value = "dest", required = true) String dest) {
		log.info("Inside get Trip by source and destination Controller");
		List<TripDTO> trips = service.GetBySourceAndDestination(source, dest);
		if (!trips.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(trips);
		}
		throw new NotFoundException("No Trips found in database with source: " + source + " " + "Destination: " + dest
				+ " | " + "Please enter available source and destination");
	}

	@PreAuthorize("hasAnyRole('PASSENGER','DRIVER','ADMIN')")
	@GetMapping("/sourceAndDestDate")
	public ResponseEntity<List<TripDTO>> getBySourceAndDestOrDate(@RequestParam(required = false) String source,
			@RequestParam(required = false) String dest,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date) {
		log.info("Inside get by source and dest or date controller");
		List<TripDTO> trips1 = service.GetBySourceAndDestOrDate(source, dest, date);
		if (trips1.isEmpty()) {
			throw new NotFoundException("Not Found Trip");
		}
		return ResponseEntity.status(HttpStatus.OK).body(trips1);

	}

	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<TripDTO> updateTrip(@RequestParam(value = "tid", required = true) long tid,
			@RequestBody TripDTO dto) {
		log.info("Inside add Trip Controller");
		log.info("Trip DTO", dto);
		TripDTO dto2 = service.updateTrip(tid, dto);

		if (dto2 != null) {
			return ResponseEntity.status(HttpStatus.OK).body(dto2);
		}
		throw new NullPointerException("Trip Not Added Object is null. " + "Please enter information properly" + dto2);
	}

	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@GetMapping("/getByDriverName")
	public ResponseEntity<List<TripDTO>> getByDriverName(
			@RequestParam(value = "drivername", required = true) String drivername) {
		log.info("Inside Get By Drivername Controller");
		List<TripDTO> listoftrips = service.getByDriverName(drivername);
		if (!listoftrips.isEmpty()) {
			return ResponseEntity.ok(listoftrips);
		}
		throw new NotFoundException("Driver with name: " + drivername + ". Not Found Trips. !!!");
	}
	
	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@PostMapping("/cancelTrip")
	public ResponseEntity<String> cancelTrips(@RequestParam long tripid){
		log.info("Inside Cancel Trip Controller");
		if(service.CancelTrip(tripid)) {
			return ResponseEntity.status(HttpStatus.OK).body("Cancel Trips Successfully.!!!");
		}else {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Trips Can't be cancel because internal server error.!!!");
		}
	}
	
	
}
