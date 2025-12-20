package com.api.mail.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.api.model.Booking;
import com.api.model.Trip;
import com.api.repository.BookingRepository;
import com.api.repository.TripRepository;

import jakarta.mail.MessagingException;

@Component
public class EmailScheduling {

	private static final Logger logger = LoggerFactory.getLogger(EmailScheduling.class);

	@Autowired
	private MailService mailService;

	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private BookingRepository bookingRepository;

	@Scheduled(cron = "0 30 * * * *")
	public void sendTripReminderToDriver() {

		logger.info("⏰ Running Trip Reminder Scheduler (Driver)");

		List<Trip> trips = tripRepository.findByInTimeMinutes(30);
		

		if (trips.isEmpty()) {
			logger.info("No trips starting in next 30 minutes.");
			return;
		}

		for (Trip trip : trips) {
			try {
				mailService.sendTripReminderToDriver(trip);
				logger.info("📨 Reminder sent for tripId={}", trip.getId());
			} catch (MessagingException ex) {
				logger.error("❌ Failed to send reminder for TripId={}", trip.getId(), ex);
			}
		}
	}
	
	@Scheduled(cron = "0 30 * * * *")
	public void sendBookedTripReminderToPassenger() {

		logger.info("⏰ Running Trip Reminder Scheduler (Passenger)");

		List<Booking> booking = bookingRepository.findByBookedTripStartTime(30);
		

		if (booking.isEmpty()) {
			logger.info("No trips starting in next 30 minutes.");
			return;
		}

		for (Booking book : booking) {
			try {
				mailService.sendBookedTripReminderToPassenger(book);
				logger.info("📨 Reminder sent for BookingId={}", book.getId());
			} catch (MessagingException ex) {
				logger.error("❌ Failed to send reminder for BookingId={}", book.getId(), ex);
			}
		}
	}
}
