package com.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import com.api.model.Booking;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PaymentDTO( 
		 @NotNull(message = "source should be not null")
		 int booking_id,
		 @NotNull(message = "source should be not null")
	     Double amount,
	     @NotNull(message = "source should be not null")
	     @NotEmpty(message = "source should be not empty")
	     String status,
	     @NotNull(message = "source should be not null")
	     @NotEmpty(message = "source should be not empty")
	     String method,
	     LocalDate date,
	     LocalTime time
		) {}
