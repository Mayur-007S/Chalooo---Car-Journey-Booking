package com.api.controller;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.web.PageableDefault;
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

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/trip")
public class TripController {

	@Autowired
	private TripService tripService;

	private Logger log = LoggerFactory.getLogger(TripController.class);


	/*
	 * @GetMapping("/search") public ResponseEntity<List<TripDTO>> search(
	 * 
	 * @RequestParam(required = false) String source,
	 * 
	 * @RequestParam(required = false) String destination,
	 * 
	 * @RequestParam(required = false) @DateTimeFormat(iso =
	 * DateTimeFormat.ISO.DATE) LocalDate date,
	 * 
	 * @PageableDefault(size = 20, sort = "departureTime") Pageable pageable ) {
	 * List<TripDTO> trips = tripService.searchTrips(source, destination, date,
	 * pageable); return ResponseEntity.status(HttpStatus.OK).body(trips); }
	 */
	
	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<TripDTO> addTrip(@RequestBody TripDTO dto) {
		log.info("Inside add Trip Controller");
		System.out.println("Trip: " + dto);
		TripDTO dto2 = tripService.addTrip(dto);
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
		List<TripDTO> trips = tripService.getALL();
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
		List<TripDTO> trips = tripService.GetBySourceAndDestination(source, dest);
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
		List<TripDTO> trips1 = tripService.GetBySourceAndDestOrDate(source, dest, date);
		if (trips1.isEmpty()) {
			throw new NotFoundException("Not Found Trip");
		}
		return ResponseEntity.status(HttpStatus.OK).body(trips1);

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<TripDTO> updateTrip(@RequestParam(value = "tid", required = true) long tid,
			@RequestBody TripDTO dto) {
		log.info("Inside add Trip Controller");
		log.info("Trip DTO", dto);
		TripDTO dto2 = tripService.updateTrip(tid, dto);

		if (dto2 != null) {
			return ResponseEntity.status(HttpStatus.OK).body(dto2);
		}
		throw new NullPointerException("Trip Not Added Object is null. " + "Please enter information properly" + dto2);
	}

	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@PutMapping("/updateowntrip")
	public ResponseEntity<TripDTO> updateOwnTrip(@RequestParam(value = "tid", required = true) long tid,
			@RequestParam(value = "driverid", required = true) long driverid, @RequestBody TripDTO dto) {
		log.info("Inside add Trip Controller");
		log.info("Trip DTO", dto);
		TripDTO dto2 = tripService.updateOwnTrip(tid, driverid, dto);

		if (dto2 != null) {
			return ResponseEntity.status(HttpStatus.OK).body(dto2);
		}
		throw new NullPointerException("Trip Not Added Object is null. " + 
		"Please enter information properly" + dto2);
	}

	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@GetMapping("/getByDriverName")
	public ResponseEntity<List<TripDTO>> getByDriverName(
			@RequestParam(value = "drivername", required = true) String drivername) {
		log.info("Inside Get By Drivername Controller");
		List<TripDTO> listoftrips = tripService.getByDriverName(drivername);
		if (!listoftrips.isEmpty()) {
			return ResponseEntity.ok(listoftrips);
		}
		throw new NotFoundException("Driver with name: " + drivername + ". Not Found Trips. !!!");
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/deletetrip")
	public ResponseEntity<String> deleteTrips(@RequestParam(value = "tripid") long tripid) {
		log.info("Inside Delete Trip Controller");
		if (tripService.deleteTrip(tripid)) {
			return ResponseEntity.status(HttpStatus.OK).body("Trip Deleted Successfully.!!!");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("Trips Can't be delete because trip has book someone.!!!");
		}
	}

	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@PostMapping("/deleteowntrip")
	public ResponseEntity<String> deleteOwnTrips(@RequestParam(value = "tripid") long tripid,
			@RequestParam(value = "driverid") long driverid) {
		log.info("Inside delete Own Trip Controller");
		if (tripService.deleteOwnTrip(driverid, tripid)) {
			return ResponseEntity.status(HttpStatus.OK).body("Trips Deleted Successfully.!!!");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("Trips Can't be delete becaus trip has book someone.!!!");
		}
	}

}
