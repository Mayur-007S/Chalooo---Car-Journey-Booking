package com.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CarDTO
(
	 @NotNull(message = "source should be not null")
     @NotEmpty(message = "source should be not empty")
     String model,
     @NotNull(message = "source should be not null")
     @NotEmpty(message = "source should be not empty")
     String plateNo,
     @NotNull(message = "source should be not null")
     int seats,
     @NotNull(message = "source should be not null")
     int owner_id
) {}
