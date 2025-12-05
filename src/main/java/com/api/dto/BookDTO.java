package com.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookDTO(
		@NotNull(message = "source should be not null")  int trip_id,
		@NotNull(message = "source should be not null")  int passenger_id,
		@NotNull(message = "source should be not null")  int seatsBooked,
		@NotNull(message = "source should be not null")  LocalDate date,
		@NotNull(message = "source should be not null")  LocalTime time,
		@NotNull(message = "source should be not null") 
		@NotEmpty(message = "source should be not empty") String status) {
}
