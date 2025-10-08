package com.api.service;

import java.util.List;

import com.api.dto.TripRequestDTO;
import com.api.model.Trip;

public interface TripService {

	Trip addTrip(TripRequestDTO trip);
	
	List<Trip> getALL();
	
	List<Trip> GetBySourceAndDestination(String source, String Desti);
	
}
