package com.api.customeexceptions;

public class BookingCanceletionException extends RuntimeException {	
	public BookingCanceletionException(String message) {
		super(message);
	}
}
