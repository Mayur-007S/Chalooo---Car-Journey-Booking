package com.api.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.api.customeexceptions.BookingCanceletionException;
import com.api.customeexceptions.BookingCreationException;
import com.api.customeexceptions.ErrorResponse;
import com.api.customeexceptions.MailExceptions;
import com.api.customeexceptions.NotFoundException;
import com.api.customeexceptions.ObjectNotValidateException;

import jakarta.mail.MessagingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ObjectNotValidateException.class)
	public ResponseEntity<String> handleObjectNotValidException(ObjectNotValidateException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessage().toString());
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e){
		ErrorResponse errorResponse = new ErrorResponse("Not Found Exception", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}
	
	@ExceptionHandler(InternalServerError.class)
	public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerError e){
		ErrorResponse errorResponse = new ErrorResponse("Internal Server Exception", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e){
		ErrorResponse errorResponse = new ErrorResponse("Null Pointe Exception", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	
	@ExceptionHandler(BookingCreationException.class)
	public ResponseEntity<ErrorResponse> handleBookingCreationException(BookingCreationException e){
		ErrorResponse errorResponse = new ErrorResponse("Booking Creation Exception", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	
	@ExceptionHandler(BookingCanceletionException.class)
	public ResponseEntity<ErrorResponse> handleBookingCanceletionException(BookingCanceletionException e){
		ErrorResponse errorResponse = new ErrorResponse("Booking Canceletion Exception", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ErrorResponse> handleMessagingException(MessagingException e){
		ErrorResponse errorResponse = new ErrorResponse("Messaging Exception", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e){
		ErrorResponse errorResponse = new ErrorResponse("Illegal State Exception", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	
	@ExceptionHandler(MailExceptions.class)
	public ResponseEntity<ErrorResponse> handlMailExceptions(MailExceptions e){
		ErrorResponse errorResponse = new ErrorResponse("Mail Exceptions", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	
}
