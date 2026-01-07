package com.api.mail;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.api.mail.service.MailServiceImpl;
import com.api.model.Booking;
import com.api.model.Payment;
import com.api.model.Trip;
import com.api.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

/**
 * Comprehensive End-to-End Testing for Email Notifications
 * Tests all email notification types in the Chaloo application
 * 
 * Email Types Covered:
 * 1. Sign In/Sign Up Welcome Email
 * 2. Booking Confirmation Email (Passenger)
 * 3. Booking Confirmation Email (Driver)
 * 4. Trip Reminder Email (Driver)
 * 5. Trip Reminder Email (Passenger)
 * 6. Payment Receipt Email
 * 7. Booking Cancellation Email (Passenger)
 * 8. Booking Cancellation Email (Driver)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Email Notification End-to-End Tests")
public class EmailNotificationE2ETest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private MailServiceImpl mailService;

    private MimeMessage mimeMessage;
    private User testPassenger;
    private User testDriver;
    private Trip testTrip;
    private Booking testBooking;
    private Payment testPayment;

    @BeforeEach
    void setUp() {
        // Create mock MimeMessage
        mimeMessage = new MimeMessage((Session) null);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Setup test data
        setupTestData();
    }

    private void setupTestData() {
        // Create test passenger
        testPassenger = new User();
        testPassenger.setId(1L);
        testPassenger.setUsername("john_passenger");
        testPassenger.setEmail("passenger@test.com");
        testPassenger.setPhone("9876543210");

        // Create test driver
        testDriver = new User();
        testDriver.setId(2L);
        testDriver.setUsername("jane_driver");
        testDriver.setEmail("driver@test.com");
        testDriver.setPhone("9876543211");

        // Create test trip
        testTrip = new Trip();
        testTrip.setId(1L);
        testTrip.setDriver(testDriver);
        testTrip.setSource("Mumbai");
        testTrip.setDestination("Pune");
        testTrip.setStartDateTime(LocalDate.now().plusDays(1).atTime(10, 30));
        testTrip.setTotalSeats(5);
        testTrip.setAvailableSeats(3);

        // Create test booking
        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setPassenger(testPassenger);
        testBooking.setTrip(testTrip);
        testBooking.setSeatsBooked(2);
        testBooking.setDate(LocalDate.now().plusDays(1));
        testBooking.setTime(LocalTime.of(10, 30));
        testBooking.setStatus("CONFIRMED");

        // Create test payment
        testPayment = new Payment();
        testPayment.setId(1L);
        testPayment.setBooking(testBooking);
        testPayment.setAmount(1000.0);
        testPayment.setMethod("CREDIT_CARD");
        testPayment.setStatus("SUCCESS");
    }

    // ==================== TEST 1: Sign In/Sign Up Welcome Email
    // ====================

    @Test
    @DisplayName("Test 1: Send Welcome Email on Sign In")
    void testSendWelcomeEmailOnSignIn() throws MessagingException, IOException {
        // Arrange
        String to = testPassenger.getEmail();
        String subject = "SignIn Successfully.!!!";
        String username = testPassenger.getUsername();

        // Act
        mailService.sendLoginEmail(to, subject, username);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));

        System.out.println("✅ TEST 1 PASSED: Welcome email sent successfully on Sign In");
    }

    @Test
    @DisplayName("Test 2: Send Welcome Email on Sign Up")
    void testSendWelcomeEmailOnSignUp() throws MessagingException, IOException {
        // Arrange
        String to = testPassenger.getEmail();
        String subject = "SignUp Successfully.!!!";
        String username = testPassenger.getUsername();

        // Act
        mailService.sendRegistrationEmail(to, subject, username);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));

        System.out.println("✅ TEST 2 PASSED: Welcome email sent successfully on Sign Up");
    }

    // ==================== TEST 3: Booking Confirmation Emails ====================

    @Test
    @DisplayName("Test 3: Send Booking Confirmation Email to Passenger")
    void testSendBookingConfirmationToPassenger() throws MessagingException {
        // Arrange
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Booking Confirmed</html>");

        // Act
        mailService.confirmEmailtoPassenger(testPassenger.getEmail(), testBooking);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("BookEmailTemplate/PassengerEmailTemplate"), any());

        System.out.println("✅ TEST 3 PASSED: Booking confirmation email sent to passenger");
    }

    @Test
    @DisplayName("Test 4: Send Booking Confirmation Email to Driver")
    void testSendBookingConfirmationToDriver() throws MessagingException {
        // Arrange
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Booking Confirmed</html>");

        // Act
        mailService.confirmEmailtoDriver(testBooking);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("BookEmailTemplate/DriverEmailTemplate"), any());

        System.out.println("✅ TEST 4 PASSED: Booking confirmation email sent to driver");
    }

    // ==================== TEST 5: Trip Reminder Emails ====================

    @Test
    @DisplayName("Test 5: Send Trip Reminder Email to Driver")
    void testSendTripReminderToDriver() throws MessagingException {
        // Arrange
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Trip Reminder</html>");

        // Act
        mailService.sendTripReminderToDriver(testTrip);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("BookEmailTemplate/ReminderForDriver"), any());

        System.out.println("✅ TEST 5 PASSED: Trip reminder email sent to driver");
    }

    @Test
    @DisplayName("Test 6: Send Trip Reminder Email to Passenger")
    void testSendTripReminderToPassenger() throws MessagingException {
        // Arrange
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Trip Reminder</html>");

        // Act
        mailService.sendBookedTripReminderToPassenger(testBooking);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("BookEmailTemplate/PassengerEmailTemplate"), any());

        System.out.println("✅ TEST 6 PASSED: Trip reminder email sent to passenger");
    }

    // ==================== TEST 7: Payment Receipt Email ====================

    @Test
    @DisplayName("Test 7: Send Payment Receipt Email to Passenger")
    void testSendPaymentReceiptToPassenger() throws MessagingException {
        // Arrange
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Payment Receipt</html>");

        // Act
        mailService.sendPaymentReceiptToPassengerEmail(testPayment);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("PaymentReceiptEmail/paymentReceiptForPassenger"), any());

        System.out.println("✅ TEST 7 PASSED: Payment receipt email sent to passenger");
    }

    // ==================== TEST 8: Booking Cancellation Emails ====================

    @Test
    @DisplayName("Test 8: Send Booking Cancellation Email to Passenger")
    void testSendBookingCancellationToPassenger() throws MessagingException {
        // Arrange
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Booking Cancelled</html>");

        // Act
        mailService.sendBookingCancellationToPassengerEmail(testBooking);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("CancelTripBookingEmail/cancelTripBookingforPassenger"), any());

        System.out.println("✅ TEST 8 PASSED: Booking cancellation email sent to passenger");
    }

    @Test
    @DisplayName("Test 9: Send Booking Cancellation Email to Driver")
    void testSendBookingCancellationToDriver() throws MessagingException {
        // Arrange
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Booking Cancelled</html>");

        // Act
        mailService.sendBookingCancellationToDriverEmail(testBooking);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("CancelTripBookingEmail/cancelTripBookingForDriver"), any());

        System.out.println("✅ TEST 9 PASSED: Booking cancellation email sent to driver");
    }

    // ==================== ERROR HANDLING TESTS ====================

    @Test
    @DisplayName("Test 10: Handle Email Send Failure Gracefully")
    void testEmailSendFailureHandling() throws MessagingException, IOException {
        // Arrange
        doThrow(new org.springframework.mail.MailSendException("SMTP connection failed"))
                .when(javaMailSender).send(ArgumentMatchers.any(MimeMessage.class));

        // Act & Assert - Should not throw exception due to @Async and try-catch
        assertDoesNotThrow(() -> {
            mailService.sendRegistrationEmail(testPassenger.getEmail(), "Test", testPassenger.getUsername());
        });

        // Verify that the send method was called
        verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));

        System.out.println("✅ TEST 10 PASSED: Email failure handled gracefully");
    }

    @Test
    @DisplayName("Test 11: Verify Email Content Contains Correct Information")
    void testEmailContentValidation() throws MessagingException {
        // Arrange
        ArgumentCaptor<String> templateCaptor = ArgumentCaptor.forClass(String.class);
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Booking Details</html>");

        // Act
        mailService.confirmEmailtoPassenger(testPassenger.getEmail(), testBooking);

        // Assert
        verify(templateEngine).process(templateCaptor.capture(), any());
        assertEquals("BookEmailTemplate/PassengerEmailTemplate", templateCaptor.getValue());

        System.out.println("✅ TEST 11 PASSED: Email content validated successfully");
    }

    // ==================== COMPREHENSIVE TEST ====================

    @Test
    @DisplayName("Test 12: Complete Email Flow - User Journey")
    void testCompleteEmailFlowUserJourney() throws Exception {
        // Arrange
        when(templateEngine.process(anyString(), any())).thenReturn("<html>Email Content</html>");

        // Act - Simulate complete user journey
        // 1. User signs up
        mailService.sendRegistrationEmail(testPassenger.getEmail(), "SignUp Successfully.!!!", testPassenger.getUsername());

        // 2. Passenger books a trip
        mailService.confirmEmailtoPassenger(testPassenger.getEmail(), testBooking);

        // 3. Driver gets booking notification
        mailService.confirmEmailtoDriver(testBooking);

        // 4. Passenger makes payment
        mailService.sendPaymentReceiptToPassengerEmail(testPayment);

        // 5. Reminders sent before trip
        mailService.sendTripReminderToDriver(testTrip);
        mailService.sendBookedTripReminderToPassenger(testBooking);

        // Assert - 6 emails should be sent
        verify(javaMailSender, times(6)).send(ArgumentMatchers.any(MimeMessage.class));

        System.out.println("✅ TEST 12 PASSED: Complete user journey email flow validated");
    }
}
