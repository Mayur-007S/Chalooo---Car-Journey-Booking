package com.api.validation;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.api.customeexceptions.ObjectNotValidateException;
import com.api.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;

@Component
public class ObjectValidator<T> {

	private final ValidatorFactory factory = 
			Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();
	
	private Logger logger = LoggerFactory.getLogger(ObjectValidator.class);
	
	public void validate(T objectToValidate) {
		logger.info("Inside ObjectValidator.validate()");
		logger.info("Validating object: {}", objectToValidate);
		Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);
		if(!violations.isEmpty()) {
			logger.info("Get violations: {}", violations);
			var errorMessage = violations
					.stream()
					.map(ConstraintViolation::getMessage) 
					.collect(Collectors.toSet());
			throw new ObjectNotValidateException(errorMessage);
		}
	}

}
