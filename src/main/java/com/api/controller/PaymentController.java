package com.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.PaymentDTO;
import com.api.model.Payment;
import com.api.service.PaymentService;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	private Logger log = LoggerFactory.getLogger(PaymentController.class);
	
	@PreAuthorize("hasAnyRole('PASSENGER','ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<PaymentDTO> addPayment(@RequestBody PaymentDTO paymentDTO)
	{
		log.info("Adding new payment with details: {}", paymentDTO);
		PaymentDTO payment = paymentService.addPayment(paymentDTO);
		if(payment != null) {
			return ResponseEntity.ok(payment);
		}
		throw new NullPointerException("Failed to add payment with the provided details.");
	}
	
	@PreAuthorize("hasAnyRole('PASSENGER','DRIVER','ADMIN')")
    @GetMapping("/byTripId")
	public ResponseEntity<List<PaymentDTO>> getByTripId(
			@RequestParam(value = "tripid", required = true) int tripid)
    {
		log.info("Fetching payment details for tripId: {}", tripid);
		List<PaymentDTO> payment = paymentService.getByTripId(tripid);
		if (payment != null) {
			return ResponseEntity.ok(payment);
		}
		
		throw new NotFoundException("No payment found for the given trip ID.");
	}
    
	@PreAuthorize("hasAnyRole('PASSENGER','DRIVER','ADMIN')")
    @GetMapping("/byBookId")
	public ResponseEntity<List<PaymentDTO>> getByBookId(
			@RequestParam(value = "bookid", required = true) long bookid)
    {
		log.info("Fetching payment details for BookId: {}", bookid);
		List<PaymentDTO> payment = paymentService.getByBookingId(bookid);
		if (payment != null) {
			return ResponseEntity.ok(payment);
		}
		
		throw new NotFoundException("No payment found for the given book ID.");
	}
	
	@PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
	@GetMapping("/byDriver")
	public ResponseEntity<List<PaymentDTO>> getByDriver(@RequestParam int driverId){
		log.info("Inside get By Driver controller");
		List<PaymentDTO> payment = paymentService.getPaymentForDriver(driverId);
		
		if(!payment.isEmpty()) {
			ResponseEntity.status(HttpStatus.OK).body(payment);
		}
		throw new NotFoundException("Not found Payment for driver:");
	}
}
