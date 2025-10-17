package com.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TripDTO (
	@NotNull(message = "source should be not null")
	Long tid,
	@NotNull(message = "source should be not null")
    @NotEmpty(message = "source should be not empty")
    String source,
	@NotNull(message = "source should be not null")
    @NotEmpty(message = "source should be not empty")
    String destination,
	@NotNull(message = "source should be not null")
    @JsonFormat(pattern = "yyyy-MM-dd'T'H:mm:ss")
    LocalDateTime dateTime,
	@NotNull(message = "source should be not null")
    int totalSeats,
	@NotNull(message = "source should be not null")
    int availableSeats,
	@NotNull(message = "source should be not null")
    Long driver_id,
	@NotNull(message = "source should be not null")
    Long car_id
) {}    

