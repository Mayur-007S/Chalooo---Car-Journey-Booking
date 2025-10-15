package com.api.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.dto.TripRequestDTO;
import com.api.dto.TripResponseDTO;
import com.api.model.Trip;

public interface TripService {

	Trip addTrip(TripRequestDTO trip);
	
	List<TripResponseDTO> getALL();

	@Query(value = "SELECT * FROM chaloo_db.trips"
			+ " WHERE source = :s and destination = :d", nativeQuery = true)
	List<TripResponseDTO> GetBySourceAndDestination(@Param("s") String source, @Param("d") String Desti);
	
	Trip getOneTrip(int id); 
}
