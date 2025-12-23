package com.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;

import com.api.customeexceptions.NotFoundException;
import com.api.customeexceptions.ObjectNotValidateException;
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

	@Transactional(rollbackFor = { ObjectNotValidateException.class })
	@Override
	@CachePut(value = "payments", key = "#result.id")
	public PaymentDTO addPayment(PaymentDTO paymentdto) {
		log.info("Inside add Payment method");
		validator.validate(paymentdto);
		var payment = paymentMapper.DTOtoEntity(paymentdto);
		var payment2 = repository.save(payment);
		return paymentMapper.EntitytoDTO(payment2);
	}

	@Override
	@Cacheable(value = "payments", key = "#paymentid")
	public PaymentDTO getOne(int paymentid) {
		log.info("Inside getOne booking method");
		Payment payment = repository.findById(paymentid);
		if (payment == null) {
			throw new NotFoundException("No Payment found for id: " + paymentid);
		}
		return paymentMapper.EntitytoDTO(payment);
	}

	@Override
	public List<PaymentDTO> getByBookingId(long bookingid) {
		log.info("Inside getByBookingId booking method");
		List<Payment> payment = repository.findByBookingId(bookingid);
		return paymentMapper.EntitytoDTO(payment);
	}

	@Override
	public List<PaymentDTO> getByTripId(int trip_id) {
		log.info("Inside getByTripId booking method");
		List<Payment> payment = repository.findByTripId(trip_id);
		return paymentMapper.EntitytoDTO(payment);
	}

	@Override
	public List<PaymentDTO> getPaymentForDriver(long driver_id) {
		log.info("Inside getPaymentForDriver booking method");
		List<Payment> payment = repository.findPaymentForDriver(driver_id);
		return paymentMapper.EntitytoDTO(payment);
	}

	@Override
	public List<PaymentDTO> getAllPayment() {
		log.info("Inside get all Payment Method");
		List<Payment> payments = repository.findAll();
		return paymentMapper.EntitytoDTO(payments);
	}

}
