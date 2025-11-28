package com.api.dto.mapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.PaymentDTO;
import com.api.model.Booking;
import com.api.model.Payment;
import com.api.service.BookingService;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Component
public class PaymentMapper {
	
	@Autowired
	private BookingService bookingService;

	private Logger log = LoggerFactory.getLogger(PaymentMapper.class);
	
	public Payment DTOtoEntity(PaymentDTO paymentdto) {
		log.info("Inside Payment mapper class: DTOtoEntity method ");
		Payment payment = new Payment();
		
		Optional<Booking> book = bookingService.getOne(paymentdto.booking_id()); 
		if(book.isPresent()) {payment.setBooking(book.get());}
		else {throw new NotFoundException("booking is not for the payment"); }
		
		payment.setAmount(paymentdto.amount());
		payment.setMethod(paymentdto.method());
		payment.setStatus(paymentdto.status());
		
		return payment;
	}
	
	public PaymentDTO EntitytoDTO(Payment payment) {
		log.info("Inside Payment mapper class EntitytoDTO method");
		PaymentDTO paymentdto = new PaymentDTO(
				 payment.getBooking().getId(),
			     payment.getAmount(),
			     payment.getStatus(),
			     payment.getMethod(),
			     payment.getDate(),
			     payment.getTime()
				);
		return paymentdto;
	}
	
	public List<PaymentDTO> EntitytoDTO(List<Payment> payment) {
		log.info("Inside Payment mapper class List EntitytoDTO method");
		if(payment.isEmpty()) {
			log.info("No payment found ");
		}
		List<PaymentDTO> paymentdtos = payment.stream()
			.map(pay -> new PaymentDTO
					(pay.getBooking().getId(), 
					pay.getAmount(), 
					pay.getStatus(), 
					pay.getMethod(), 
					pay.getDate(), 
					pay.getTime()))
			.collect(Collectors.toList());
		
		return paymentdtos;
	}
	
}
