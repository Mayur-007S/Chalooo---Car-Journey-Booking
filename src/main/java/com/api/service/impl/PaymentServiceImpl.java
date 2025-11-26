package com.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.dto.PaymentDTO;
import com.api.dto.mapper.PaymentMapper;
import com.api.model.Payment;
import com.api.repository.PaymentRepository;
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
	private PaymentMapper paymentMapper;

	@Override
	public PaymentDTO addPayment(PaymentDTO paymentdto) {
		log.info("Inside add Payment method");
		validator.validate(paymentdto);
		var payment = paymentMapper.DTOtoEntity(paymentdto);
		var payment2 = repository.save(payment);
		return paymentMapper.EntitytoDTO(payment2);
	}

	@Override
	public PaymentDTO getOne(int payment_id) {
		log.info("Inside getOne booking method");
		var payment = repository.findById(payment_id);
		return paymentMapper.EntitytoDTO(payment);
	}

	@Override
	public List<PaymentDTO> getByBookingId(long booking_id) {
		log.info("Inside getByBookingId booking method");
		List<Payment> payment = repository.findByBookingId(booking_id);
		return paymentMapper.EntitytoDTO(payment);
	}

	@Override
	public List<PaymentDTO> getByTripId(int trip_id) {
		log.info("Inside getByTripId booking method");
		List<Payment> payment = repository.findByTripId(trip_id);
		return paymentMapper.EntitytoDTO(payment);
	}

	@Override
	public List<PaymentDTO> getPaymentForDriver(int driver_id) {
		log.info("Inside getPaymentForDriver booking method");
		List<Payment> payment = repository.findPaymentForDriver(driver_id);
		if(payment.isEmpty()) {
			log.info("Not found payment");
		}
		return paymentMapper.EntitytoDTO(payment);
	}

}
