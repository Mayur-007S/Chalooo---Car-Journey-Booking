package com.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.model.Payment;
import com.api.repository.PaymentRepository;
import com.api.service.PaymentService;
import com.api.validation.ObjectValidator;

@Service
public class PaymentServiceImpl implements PaymentService {

	private Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
	
	private PaymentRepository repository;
	@Autowired
	private ObjectValidator<Payment> validator;
	 
	@Override
	public Payment addPayment(Payment payment) {
		// TODO Auto-generated method stub
		log.info("Inside add Payment method");
		log.info("Validate Object");
		validator.validate(payment);
		log.info("Call payment save method");
		log.info("Exit from payment method");
		return repository.save(payment);
	}

	@Override
	public Payment getOne(int payment_id) {
		// TODO Auto-generated method stub
		return repository.findById(payment_id);
	}

}
