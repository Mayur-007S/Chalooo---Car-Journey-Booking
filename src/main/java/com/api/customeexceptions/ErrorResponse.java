package com.api.customeexceptions;

import java.util.Set;
import java.util.UUID;

public class ErrorResponse {

	private String id;
	private String message;
	private String details;
	private long timestamp;
	
	public ErrorResponse(String message, String details) {
		this.id = UUID.randomUUID().toString();
		this.message = message;
		this.details = details;
		this.timestamp = System.currentTimeMillis();
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

	public long getTimestamp() {
		return timestamp;
	}
	
	
}
