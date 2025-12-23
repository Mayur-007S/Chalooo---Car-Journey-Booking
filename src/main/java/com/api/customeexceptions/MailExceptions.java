package com.api.customeexceptions;

public class MailExceptions extends RuntimeException{
	public MailExceptions(String message) {
		super(message);
	}
}
