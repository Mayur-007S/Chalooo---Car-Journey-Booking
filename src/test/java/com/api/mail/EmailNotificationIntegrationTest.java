package com.api.mail;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.api.mail.service.MailService;
import com.api.model.Booking;
import com.api.model.Payment;
import com.api.model.Trip;
import com.api.model.User;

/**
 * Integration Tests for Email Notifications
 * 
 * These tests actually send real emails and are disabled by default.
 * To run these tests:
 * 1. Remove @Disabled annotation
 * 2. Set up environment variables for email configuration
 * 3. Update test email addresses to your own
 * 4. Run with: mvn test -Dtest=EmailNotificationIntegrationTest
 * 
 * WARNING: These tests will send actual emails!
 */
@SpringBootTest
@Disabled("Integration tests - sends real emails. Enable manually when needed.")
@DisplayName("Email Notification Integration Tests (Real Email Sending)")
public class EmailNotificationIntegrationTest {

    @Autowired
    private MailService mailService;

    // ⚠️ CHANGE THESE TO YOUR TEST EMAIL ADDRESSES
    private static final String TEST_PASSENGER_EMAIL = "your-test-email@example.com";
    private static final String TEST_DRIVER_EMAIL = "your-driver-test@example.com";

    private User testPassenger;
    private User testDriver;
    private Trip testTrip;
    private Booking testBooking;
    private Payment testPayment;

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    private void setupTestData() {
        // Create test passenger
        testPassenger = new User();
        testPassenger.setId(101L);
        testPassenger.setUsername("integration_test_passenger");
        testPassenger.setEmail(TEST_PASSENGER_EMAIL);
        testPassenger.setPhone("9876543210");

        // Create test driver
        testDriver = new User();
        testDriver.setId(102L);
        testDriver.setUsername("integration_test_driver");
        testDriver.setEmail(TEST_DRIVER_EMAIL);
        testDriver.setPhone("9876543211");

        // Create test trip
        testTrip = new Trip();
        testTrip.setId(101L);
        testTrip.setDriver(testDriver);
        testTrip.setSource("Mumbai Central");
        testTrip.setDestination("Pune Station");
        testTrip.setStartDateTime(LocalDateTime.now());
        testTrip.setDepartureDateTime(LocalDateTime.now().plusHours(2));
        testTrip.setAvailableSeats(3);
        testTrip.setTotalSeats(1);

        // Create test booking
        testBooking = new Booking();
        testBooking.setId(101L);
        testBooking.setPassenger(testPassenger);
        testBooking.setTrip(testTrip);
        testBooking.setSeatsBooked(2);
        testBooking.setStatus("CONFIRMED");

        // Create test payment
        testPayment = new Payment();
        testPayment.setId(101L);
        testPayment.setBooking(testBooking);
        testPayment.setAmount(900.0);
        testPayment.setMethod("UPI");
        testPayment.setStatus("SUCCESS");
    }

    @Test
    @DisplayName("Integration Test 1: Send Real Welcome Email")
    void testSendRealWelcomeEmail() throws Exception {
        System.out.println("\n📧 Sending real welcome email to: " + TEST_PASSENGER_EMAIL);

        mailService.sendEmail(
                TEST_PASSENGER_EMAIL,
                "Welcome to Chaloo - Integration Test",
                testPassenger.getUsername());

        System.out.println("✅ Welcome email sent! Check inbox at: " + TEST_PASSENGER_EMAIL);
        Thread.sleep(2000); // Wait for async processing
    }

    @Test
    @DisplayName("Integration Test 2: Send Real Booking Confirmation to Passenger")
    void testSendRealBookingConfirmationToPassenger() throws Exception {
        System.out.println("\n📧 Sending booking confirmation email to passenger: " + TEST_PASSENGER_EMAIL);

        mailService.confirmEmailtoPassenger(TEST_PASSENGER_EMAIL, testBooking);

        System.out.println("✅ Booking confirmation sent to passenger! Check inbox.");
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Integration Test 3: Send Real Booking Confirmation to Driver")
    void testSendRealBookingConfirmationToDriver() throws Exception {
        System.out.println("\n📧 Sending booking confirmation email to driver: " + TEST_DRIVER_EMAIL);

        mailService.confirmEmailtoDriver(testBooking);

        System.out.println("✅ Booking confirmation sent to driver! Check inbox.");
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Integration Test 4: Send Real Trip Reminder to Driver")
    void testSendRealTripReminderToDriver() throws Exception {
        System.out.println("\n📧 Sending trip reminder to driver: " + TEST_DRIVER_EMAIL);

        mailService.sendTripReminderToDriver(testTrip);

        System.out.println("✅ Trip reminder sent to driver! Check inbox.");
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Integration Test 5: Send Real Trip Reminder to Passenger")
    void testSendRealTripReminderToPassenger() throws Exception {
        System.out.println("\n📧 Sending trip reminder to passenger: " + TEST_PASSENGER_EMAIL);

        mailService.sendBookedTripReminderToPassenger(testBooking);

        System.out.println("✅ Trip reminder sent to passenger! Check inbox.");
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Integration Test 6: Send Real Payment Receipt")
    void testSendRealPaymentReceipt() throws Exception {
        System.out.println("\n📧 Sending payment receipt to passenger: " + TEST_PASSENGER_EMAIL);

        mailService.sendPaymentReceiptToPassengerEmail(testPayment);

        System.out.println("✅ Payment receipt sent! Check inbox.");
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Integration Test 7: Send Real Cancellation Email to Passenger")
    void testSendRealCancellationToPassenger() throws Exception {
        System.out.println("\n📧 Sending cancellation email to passenger: " + TEST_PASSENGER_EMAIL);

        mailService.sendBookingCancellationToPassengerEmail(testBooking);

        System.out.println("✅ Cancellation email sent to passenger! Check inbox.");
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Integration Test 8: Send Real Cancellation Email to Driver")
    void testSendRealCancellationToDriver() throws Exception {
        System.out.println("\n📧 Sending cancellation email to driver: " + TEST_DRIVER_EMAIL);

        mailService.sendBookingCancellationToDriverEmail(testBooking);

        System.out.println("✅ Cancellation email sent to driver! Check inbox.");
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Integration Test 9: Send All Email Types in Sequence")
    void testSendAllEmailTypesInSequence() throws Exception {
        System.out.println("\n🚀 RUNNING COMPLETE EMAIL FLOW TEST");
        System.out.println("=".repeat(60));

        // 1. Welcome Email
        System.out.println("\n1️⃣ Sending Welcome Email...");
        mailService.sendEmail(TEST_PASSENGER_EMAIL, "SignUp Successfully.!!!", testPassenger.getUsername());
        Thread.sleep(3000);

        // 2. Booking Confirmation - Passenger
        System.out.println("2️⃣ Sending Booking Confirmation to Passenger...");
        mailService.confirmEmailtoPassenger(TEST_PASSENGER_EMAIL, testBooking);
        Thread.sleep(3000);

        // 3. Booking Confirmation - Driver
        System.out.println("3️⃣ Sending Booking Confirmation to Driver...");
        mailService.confirmEmailtoDriver(testBooking);
        Thread.sleep(3000);

        // 4. Payment Receipt
        System.out.println("4️⃣ Sending Payment Receipt...");
        mailService.sendPaymentReceiptToPassengerEmail(testPayment);
        Thread.sleep(3000);

        // 5. Trip Reminders
        System.out.println("5️⃣ Sending Trip Reminder to Driver...");
        mailService.sendTripReminderToDriver(testTrip);
        Thread.sleep(3000);

        System.out.println("6️⃣ Sending Trip Reminder to Passenger...");
        mailService.sendBookedTripReminderToPassenger(testBooking);
        Thread.sleep(3000);

        // 7. Cancellation Emails
        System.out.println("7️⃣ Sending Cancellation to Passenger...");
        mailService.sendBookingCancellationToPassengerEmail(testBooking);
        Thread.sleep(3000);

        System.out.println("8️⃣ Sending Cancellation to Driver...");
        mailService.sendBookingCancellationToDriverEmail(testBooking);
        Thread.sleep(3000);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ ALL EMAILS SENT SUCCESSFULLY!");
        System.out.println("📬 Check the following inboxes:");
        System.out.println("   - Passenger: " + TEST_PASSENGER_EMAIL);
        System.out.println("   - Driver: " + TEST_DRIVER_EMAIL);
        System.out.println("=".repeat(60));
    }
}
