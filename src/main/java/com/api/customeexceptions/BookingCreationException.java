package com.api.customeexceptions;

public class BookingCreationException extends RuntimeException {

	public BookingCreationException(String message) {
		super(message);
	}

}
