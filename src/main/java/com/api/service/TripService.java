package com.api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import com.api.dto.TripDTO;
import com.api.model.Trip;

public interface TripService {

	TripDTO addTrip(TripDTO trip);
	
	TripDTO updateTrip(long tid,TripDTO trip);
	
	TripDTO updateOwnTrip(long tid,long driverId,TripDTO trip);
	
	List<TripDTO> getALL();
	
	Page<TripDTO> getALL(int size, int page, String sortBy, String direction);

	List<TripDTO> GetBySourceAndDestination(String source, String Desti);

	List<TripDTO> GetBySourceAndDestOrDate(String source, String dest, String date);
	
	TripDTO getOneTrip(int id); 
	
	List<TripDTO> getByDriverName(String driverName);
	
	Page<Trip> getByDriverId(long driverId, int page, int size,String sortBy, String direction);
		
	boolean deleteTrip(long id);
	
	boolean deleteOwnTrip(long driverId, long tripId);

	/*
	 * public Page<TripDTO> searchTrips(String origin, String destination, LocalDate
	 * date, Pageable pageable);
	 */}
