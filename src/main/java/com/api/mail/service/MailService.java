package com.api.mail.service;

import java.io.IOException;

import com.api.model.Booking;
import com.api.model.Payment;
import com.api.model.Trip;

import jakarta.mail.MessagingException;

public interface MailService {

	public void sendEmail(String to, String subject, String username) throws MessagingException, IOException;

	public void confirmEmailtoPassenger(String passenger_email, Booking book) throws MessagingException;

	public void confirmEmailtoDriver(Booking book) throws MessagingException;

	public void sendTripReminderToDriver(Trip Trip) throws MessagingException;

	public void sendBookedTripReminderToPassenger(Booking booking) throws MessagingException;

	public void sendPaymentReceiptToPassengerEmail(Payment payment) throws MessagingException;

	public void sendBookingCancellationToDriverEmail(Booking booking) throws MessagingException;
	
	public void sendBookingCancellationToPassengerEmail(Booking booking) throws MessagingException;
}
