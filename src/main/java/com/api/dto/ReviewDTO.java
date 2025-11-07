package com.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReviewDTO(
		    int reviewer_id,
		    int subject_id,
		    int trip_id,
		    double rating,
		    String comment,
		    LocalDate date,
		    LocalTime time
		) 
{}
