package com.api.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.api.customeexceptions.ErrorResponse;
import com.api.customeexceptions.ObjectNotValidateException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ObjectNotValidateException.class)
	public ResponseEntity<String> handleObjectNotValidException(ObjectNotValidateException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessage().toString());
	}
	
}
