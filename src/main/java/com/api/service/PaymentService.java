package com.api.service;

import java.util.List;

import com.api.dto.PaymentDTO;
import com.api.model.Payment;

public interface PaymentService {

	PaymentDTO addPayment(PaymentDTO paymentdto);

	PaymentDTO getOne(int payment_id);
	
	List<PaymentDTO> getByBookingId(long booking_id);
	
	List<PaymentDTO> getByTripId(int trip_id);
	
	List<PaymentDTO> getPaymentForDriver(int driver_id);
}
