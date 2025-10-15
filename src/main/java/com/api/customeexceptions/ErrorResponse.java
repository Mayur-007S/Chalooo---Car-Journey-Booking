package com.api.customeexceptions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ErrorResponse {

	private String id;
	private String message;
	private String details;
	private String timestamp;
	
	public ErrorResponse(String message, String details) {
		this.id = UUID.randomUUID().toString();
		this.message = message;
		this.details = details;
		this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	
}
