package com.api.customeexceptions;

public class NotFoundException extends RuntimeException{
	public NotFoundException(String message) {
		super(message);
	}
}
