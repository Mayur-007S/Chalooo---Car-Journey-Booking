package com.api.service.impl;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.TripDTO;
import com.api.dto.mapper.TripMapper;
import com.api.model.Trip;
import com.api.repository.TripRepository;

@ExtendWith(MockitoExtension.class)
class TripServiceImplTest {

	@Mock
	private TripRepository tripRepository;

	@Mock
	private TripMapper tripMapper;

	@InjectMocks
	private TripServiceImpl tripService; // ← MUST be implementation class

	long id = 10;
	long driver_id = 100;
	long car_id = 2;

	@Test
	void testGetOneTrip() {
		Trip trip = new Trip("Pune", "Ahilyanagar",
				LocalDateTime.now(), LocalDateTime.now(), 8, 6);

		TripDTO tripDTO = new TripDTO(
				id,
				"Pune",
				"Ahilyanagar",
				LocalDateTime.now(),
				LocalDateTime.now(),
				8, 7,
				driver_id, car_id);

		when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
		when(tripMapper.tripToTripDto(trip)).thenReturn(tripDTO);

		TripDTO result = tripService.getOneTrip(1);

		verify(tripRepository).findById(1L);
		verify(tripMapper).tripToTripDto(trip);
	}

}
