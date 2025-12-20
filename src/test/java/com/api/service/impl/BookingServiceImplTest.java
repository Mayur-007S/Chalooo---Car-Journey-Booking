package com.api.service.impl;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.dto.BookDTO;
import com.api.dto.mapper.BookingMapper;
import com.api.mail.service.MailService;
import com.api.model.Booking;
import com.api.model.Car;
import com.api.model.Trip;
import com.api.model.User;
import com.api.repository.BookingRepository;
import com.api.repository.CarRepository;
import com.api.repository.TripRepository;
import com.api.repository.UserRepository;
import com.api.validation.ObjectValidator;

import jakarta.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private MailService mailService; // ✅ MOCK, not impl

    // If BookingMapper is stateless, just create it
    private BookingMapper bookingMapper = new BookingMapper();
    
    @Mock
    private ObjectValidator<Booking> validator; // ✅ FIX

    @BeforeEach
    void setUp() {
   
    }

    @Test
    void addBookingTest() throws MessagingException {
        long passengerId = 1;
        long driverId = 15;
        long tripId = 152;
        long carId = 124;

        User passenger = new User();
        passenger.setId(passengerId);
        passenger.setEmail("passenger@gmail.com");

        User driver = new User();
        driver.setId(driverId);
        driver.setEmail("driver@gmail.com");

        Car car = new Car();
        car.setId(carId);
        car.setOwner(driver);
        car.setSeats(8);

        Trip trip = new Trip();
        trip.setId(tripId);
        trip.setAvailableSeats(6);
        trip.setCar(car);
        trip.setDriver(driver);
        trip.setStartDateTime(LocalDateTime.now().plusHours(1));

        Booking booking = new Booking();
        booking.setId(12L);
        booking.setPassenger(passenger);
        booking.setTrip(trip);

        when(userRepository.findById(passengerId))
                .thenReturn(Optional.of(passenger));
        when(tripRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(trip));
        when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
                .thenReturn(booking);

        BookDTO bookDTO = new BookDTO(
                152,
                11,
                1,
                LocalDate.now(),
                LocalTime.now(),
                "CONFIRM"
        );

        doNothing().when(validator).validate(ArgumentMatchers.any());
        
        Booking result = bookingService.addBooking(bookDTO);

//        assertNotNull(result);

        verify(mailService, times(1))
                .confirmEmailtoDriver(result);

        verify(mailService, times(1))
                .confirmEmailtoPassenger(
                        passenger.getEmail(), result);
    }
}
