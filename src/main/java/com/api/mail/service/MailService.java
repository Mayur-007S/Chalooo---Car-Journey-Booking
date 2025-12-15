package com.api.mail.service;

import java.io.IOException;

import com.api.model.Booking;

import jakarta.mail.MessagingException;

public interface MailService {

	public void sendEmail(String to, String subject, String username) throws MessagingException, IOException;
	
	public void confirmEmailtoPassenger(String passenger_email, Booking book) throws MessagingException ;
}
