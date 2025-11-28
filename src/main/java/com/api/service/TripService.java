package com.api.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.dto.TripDTO;
import com.api.model.Trip;

public interface TripService {

	TripDTO addTrip(TripDTO trip);
	
	List<TripDTO> getALL();

	List<TripDTO> GetBySourceAndDestination(String source, String Desti);

	List<TripDTO> GetBySourceAndDestOrDate(String source, String dest, String date);
	
	TripDTO getOneTrip(int id); 
	
	TripDTO updateTrip(long tid,TripDTO trip);
	
	List<TripDTO> getByDriverName(String driverName);
		
	boolean CancelTrip(long id);
}
