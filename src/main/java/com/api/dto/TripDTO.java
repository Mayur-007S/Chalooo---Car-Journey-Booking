package com.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TripDTO (
	Long tid,
    String source,
    String destination,
    LocalDateTime startDateTime,
    LocalDateTime departureDateTime,
    int totalSeats,
    int availableSeats,
    Long driver_id,
    Long car_id
) {}    

