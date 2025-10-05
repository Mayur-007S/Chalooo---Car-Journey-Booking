package com.api.validation;

import java.util.Set;
import java.util.stream.Collectors;

import com.api.customeexceptions.ObjectNotValidateException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ObjectValidator<T> {

	private final ValidatorFactory factory = 
			Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();
	
	public void validate(T object) {
		Set<ConstraintViolation<T>> violations = validator.validate(object);
		
		if(!violations.isEmpty()) {
			var errormessage = violations.stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.toSet());
			throw new ObjectNotValidateException(errormessage);
		}
	}
	
	
}
