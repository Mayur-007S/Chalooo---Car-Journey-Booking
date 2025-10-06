package com.api.service;

import java.util.List;

import com.api.model.Trip;

public interface TripService {

	Trip addTrip(Trip trip);
	
	List<Trip> getALL();
	
	Trip GetBySourceAndDestination(String source, String Desti);
	
}
