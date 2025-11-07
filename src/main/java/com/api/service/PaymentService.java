package com.api.service;

import java.util.List;

import com.api.dto.PaymentDTO;
import com.api.model.Payment;

public interface PaymentService {

	Payment addPayment(PaymentDTO paymentdto);

	Payment getOne(int payment_id);
	
	List<Payment> getByBookingId(long booking_id);
	
	List<Payment> getByTripId(int trip_id);
}
