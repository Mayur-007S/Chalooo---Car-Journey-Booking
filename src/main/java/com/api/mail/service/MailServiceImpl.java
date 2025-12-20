package com.api.mail.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.api.model.Booking;
import com.api.model.Payment;
import com.api.model.Trip;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

	private JavaMailSender javaMailSender;
	private SpringTemplateEngine templateEngine;
	private Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

	public MailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;
	}

	@Override
	public void sendEmail(String to, String subject, String username) throws MessagingException, IOException {
		log.info("Inside send email method.!!!");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(to);
		helper.setSubject(subject);

		/*
		 * String htmlContext = new String(Files.readAllBytes(Paths.get(
		 * "D:\\mayur111\\FullStackWebDevelopment" +
		 * "\\NEW_JAVA2025\\SpringBootMailSender\\src" +
		 * "\\main\\resources\\templates\\index.html")));
		 */
		String context = """
								Loging Successfully.
				Welcome aboard! Your Chaloo App account is ready.
						{username}, Welcome to Chaloo App.

				We're absolutely thrilled to welcome you to the
				Chaloo App family. Your account registration
				is complete, and you are officially ready to start.

				Click the button below to jump straight into your
				dashboard and explore all the powerful
				tools designed just for you.

				We're here to support you every step of the way!
				If you have any questions or run into trouble,
				please don't hesitate to reply directly to this
				email.

				Cheers,
				      The Chaloo Team
						""";

		context.replace("{username}", username);

		helper.setText(context, true);

		javaMailSender.send(message);
	}

	@Override
	public void confirmEmailtoPassenger(String passenger_email, Booking book) throws MessagingException {
		log.info("Inside confirm Email to Passenger method");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		Map<String, String> template = Map.of("title", "Trip Confirmed – Chaloo", "mainheading",
				"Chaloo Ride Sharing Platform", "heading", "Confirmed & On the Way", "describtion",
				"We are pleased to inform you that your trip has been <strong>successfully\r\n"
						+ "									confirmed</strong>. At <strong>Chaloo</strong>, every journey is treated\r\n"
						+ "								with care, responsibility, and trust.",
				"footerdescribtion",
				" May your ride be smooth and timely. Should you need assistance,\r\n"
						+ " our support team remains available — attentive, dependable, and" + "	ready.",
				"warning", " If you are unable to take this trip, inform us immediately.", "buttontext",
				" Login to Chaloo ");

		Context context = new Context();
		context.setVariable("book", book);
		context.setVariable("template", template);

		String html = templateEngine.process("BookEmailTemplate/PassengerEmailTemplate", context);

		helper.setTo(passenger_email);
		helper.setSubject("Trip Confirmed – Chaloo");
		helper.setText(html, true);
		log.info("Email has sent to Passenger: {}", passenger_email);
		javaMailSender.send(message);
	}

	@Override
	public void confirmEmailtoDriver(Booking book) throws MessagingException {
		log.info("Inside confirm Email to Driver method");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		String driver_email = book.getTrip().getDriver().getEmail();
		Context context = new Context();
		context.setVariable("book", book);

		String html = templateEngine.process("BookEmailTemplate/DriverEmailTemplate", context);

		helper.setTo(driver_email);
		helper.setSubject("Trip Booked Confirmed – Chaloo");
		helper.setText(html, true);
		log.info("Email has sent to Driver: {}", driver_email);
		javaMailSender.send(message);
	}

	@Override
	public void sendTripReminderToDriver(Trip trip) throws MessagingException {
		log.info("Inside confirm Email to Driver method");

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		String driver_email = trip.getDriver().getEmail();

		Map<String, String> template = Map.of("title", "Trip Reminder – Chaloo", "mainheading", "⏰ Trip Reminder",
				"heading", "Upcoming Trip Scheduled", "describtion",
				"This is a reminder for your upcoming trip.\r\n"
						+ " Please review the details below and ensure timely availability.",
				"footerdescribtion",
				" Please arrive at the pickup location at least 10 minutes early.\r\n"
						+ " Ensure your vehicle and documents are ready.",
				"warning", " If you are unable to take this trip, inform us immediately.", "buttontext",
				" View Trip Details");

		Context context = new Context();
		context.setVariable("trip", trip);
		context.setVariable("template", template);

		String html = templateEngine.process("BookEmailTemplate/ReminderForDriver", context);

		helper.setTo(driver_email);
		helper.setSubject("Trip Reminder – Chaloo");
		helper.setText(html, true);
		log.info("Email has sent to Driver: {}", driver_email);
		javaMailSender.send(message);

	}

	@Override
	public void sendBookedTripReminderToPassenger(Booking booking) throws MessagingException {
		log.info("Inside send Booked Trip Reminder To Passenger method");

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		String driver_email = booking.getTrip().getDriver().getEmail();

		Map<String, String> template = Map.of("title", "Trip Reminder – Chaloo", "mainheading", "⏰ Trip Reminder",
				"heading", "Upcoming Trip Scheduled", "describtion",
				"This is a reminder for your upcoming trip.\r\n"
						+ " Please review the details below and ensure timely availability.",
				"footerdescribtion",
				" Please arrive at the pickup location at least 10 minutes early.\r\n"
						+ " Ensure your vehicle and documents are ready.",
				"warning", " If you are unable to take this trip, inform us immediately.", "buttontext",
				" View Trip Details");

		Context context = new Context();
		context.setVariable("book", booking);
		context.setVariable("template", template);

		String html = templateEngine.process("BookEmailTemplate/PassengerEmailTemplate", context);

		helper.setTo(driver_email);
		helper.setSubject("Trip Reminder – Chaloo");
		helper.setText(html, true);
		log.info("Email has sent to Driver: {}", driver_email);
		javaMailSender.send(message);

	}

	@Override
	public void sendPaymentReceiptToPassengerEmail(Payment receipt) throws MessagingException {
		log.info("Inside Send Payment Reciept Email Mathod.");

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		String email = receipt.getBooking().getPassenger().getEmail();

		Context context = new Context();
		context.setVariable("receipt", receipt);

		String html = templateEngine.process("PaymentReceiptEmail/paymentReceiptForPassenger", context);

		helper.setTo(email);
		helper.setSubject("Payment Receipt – Chaloo");
		helper.setText(html, true);
		log.info("Email has sent to Passengers: {}", email);
		javaMailSender.send(message);
	}

	@Override
	public void sendBookingCancellationToPassengerEmail(Booking booking) throws MessagingException {
		log.info("Inside send Booking Cancellation Email Mathod.");

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		String email = booking.getPassenger().getEmail();

		Context context = new Context();
		context.setVariable("booking", booking);

		String html = templateEngine.process("CancelTripBookingEmail/cancelTripBookingforPassenger", context);

		helper.setTo(email);
		helper.setSubject("Booking Cancel – Chaloo");
		helper.setText(html, true);
		log.info("Email has sent to Passengers: {}", email);
		javaMailSender.send(message);

	}
	
	@Override
	public void sendBookingCancellationToDriverEmail(Booking booking) throws MessagingException {
		log.info("Inside send Booking Cancellation Email Mathod.");

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		String email = booking.getPassenger().getEmail();

		Context context = new Context();
		context.setVariable("booking", booking);

		String html = templateEngine.process("CancelTripBookingEmail/cancelTripBookingForDriver", context);

		helper.setTo(email);
		helper.setSubject("Booking Cancel – Chaloo");
		helper.setText(html, true);
		log.info("Email has sent to Driver: {}", email);
		javaMailSender.send(message);

	}

}
