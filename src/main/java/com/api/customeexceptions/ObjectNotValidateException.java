package com.api.customeexceptions;

import java.util.Set;

@SuppressWarnings("serial")
public class ObjectNotValidateException extends RuntimeException {

	private final Set<String> errorMessage;

	public ObjectNotValidateException(Set<String> errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Set<String> getErrorMessage() {
		return errorMessage;
	}	
}
