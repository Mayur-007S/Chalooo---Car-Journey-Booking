package com.api.service;

import com.api.model.Payment;

public interface PaymentService {

	Payment addPayment(Payment payment);

	Payment getOne(int payment_id);
}
