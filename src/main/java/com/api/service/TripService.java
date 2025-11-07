package com.api.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.api.dto.TripDTO;
import com.api.model.Trip;

public interface TripService {

	Trip addTrip(TripDTO trip);
	
	List<TripDTO> getALL();

	@Query(value = "SELECT * FROM chaloo_db.trips"
			+ " WHERE source = :s and destination = :d", nativeQuery = true)
	List<TripDTO> GetBySourceAndDestination(@Param("s") String source, @Param("d") String Desti);
	
	Trip getOneTrip(int id); 
	
	Trip updateTrip(long tid,TripDTO trip);
	
	List<Trip> getByDriverName(String driverName);
}
