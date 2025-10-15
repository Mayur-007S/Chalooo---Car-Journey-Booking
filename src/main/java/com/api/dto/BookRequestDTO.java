package com.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookRequestDTO(
     int trip_id,
     int passenger_id,
     int seatsBooked,
     LocalDate date,
     LocalTime time,
     String status
) {}

    

