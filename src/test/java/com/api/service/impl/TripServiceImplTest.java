package com.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.TripDTO;
import com.api.model.Trip;
import com.api.repository.TripRepository;
import com.api.service.TripService;

@SpringBootTest
class TripServiceImplTest {

	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private TripService serviceImpl;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGetOneTrip() {
		TripDTO trip = serviceImpl.getOneTrip(1);
		assertNotNull(trip);
	}
	
	@Test
	void testGetOneTripThrowException() {
		TripDTO trip = serviceImpl.getOneTrip(1);
		assertThrows(NotFoundException.class, () -> {
			serviceImpl.getOneTrip(12);
		});
	}

}
