package com.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.PaymentDTO;
import com.api.model.Booking;
import com.api.model.Payment;
import com.api.repository.PaymentRepository;
import com.api.service.BookingService;
import com.api.service.PaymentService;
import com.api.validation.ObjectValidator;

@Service
public class PaymentServiceImpl implements PaymentService {

	private Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
	@Autowired
	private PaymentRepository repository;
	@Autowired
	private ObjectValidator<PaymentDTO> validator;
	
	@Autowired
	private BookingService bookingService;
	 
	@Override
	public Payment addPayment(PaymentDTO paymentdto) {
		// TODO Auto-generated method stub
		log.info("Inside add Payment method");
		validator.validate(paymentdto);
		Payment payment = new Payment();
		
		Optional<Booking> book = bookingService.getOne(paymentdto.booking_id()); 
		if(book.isPresent()) {payment.setBooking(book.get());}
		else {throw new NotFoundException("booking is not for the payment"); }
		
		payment.setAmount(paymentdto.amount());
		payment.setStatus(paymentdto.status());
		payment.setMethod(paymentdto.method());
		payment.setDate(paymentdto.date());
		payment.setTime(paymentdto.time());
		
		return repository.save(payment);
	}

	@Override
	public Payment getOne(int payment_id) {
		// TODO Auto-generated method stub
		return repository.findById(payment_id);
	}

	@Override
	public List<Payment> getByBookingId(long booking_id) {
		// TODO Auto-generated method stub
		return repository.findByBookingId(booking_id);
	}

	@Override
	public List<Payment> getByTripId(int trip_id) {
		// TODO Auto-generated method stub
		return repository.findByTripId(trip_id);
	}

}
