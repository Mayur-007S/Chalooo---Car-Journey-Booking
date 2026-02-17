package com.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CarDTO(

	     Long id,

	     @NotBlank(message = "model should not be blank")
	     String model,

	     @NotBlank(message = "plateNo should not be blank")
	     String plateNo,

	     @NotNull(message = "seats should not be null")
	     @Positive(message = "seats must be positive")
	     int seats,

	     @NotNull(message = "ownerId should not be null")
	     Long owner_id

	) {}


