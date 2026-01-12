package com.api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
import com.api.dto.mapper.TripMapper;
import com.api.mail.service.MailService;
import com.api.model.Booking;
import com.api.model.Car;
import com.api.model.Trip;
import com.api.model.User;
import com.api.repository.BookingRepository;
import com.api.repository.CarRepository;
import com.api.repository.TripRepository;
import com.api.repository.UserRepository;
import com.api.service.TripService;
import com.api.service.UserService;
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
	private ObjectValidator<Object> validator;

	@Mock
	private UserService userService;

	@Mock
	private TripService tripService;

	@Mock
	private TripMapper mapper;

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

		when(userService.getOneUser((int) passengerId)).thenReturn(passenger);
		when(tripRepository.findById((int) tripId)).thenReturn(Optional.of(trip));
		when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		BookDTO bookDTO = new BookDTO((int) tripId, (int) passengerId, 1, LocalDate.now(), LocalTime.now(), "CONFIRM");

		doNothing().when(validator).validate(ArgumentMatchers.any());

		Booking result = bookingService.addBooking(bookDTO);

		// assertNotNull(result);

		verify(mailService, times(1)).confirmEmailtoDriver(result);

		verify(mailService, times(1)).confirmEmailtoPassenger(passenger.getEmail(), result);
	}

	@Test
	void getAllTest() {
	    // Arrange
	    Booking booking = new Booking();
	    booking.setId(10L);
	    booking.setDate(LocalDate.now());
	    booking.setPassenger(new User());
	    booking.setPayment(List.of());
	    booking.setSeatsBooked(8);
	    booking.setStatus("CONFIRM");
	    booking.setTime(LocalTime.now());
	    booking.setTrip(new Trip());

	    when(bookingRepository.findAll())
	            .thenReturn(List.of(booking));

	    // Act
	    List<Booking> book = bookingService.getAll();

	    // Assert (interaction)
	    verify(bookingRepository, times(1)).findAll();

	    // Assert (state)
	    assertThat(book).isNotNull();
	    assertThat(book).hasSize(1);
	    assertThat(book.getFirst()).isEqualTo(booking);
	}


	@Test
	void getOneTest() {
		// Arrange
		Booking booking = new Booking();
		booking.setId(10L);
		booking.setDate(LocalDate.now());
		booking.setPassenger(new User());
		booking.setPayment(List.of());
		booking.setSeatsBooked(8);
		booking.setStatus("CONFIRM");
		booking.setTime(LocalTime.now());
		booking.setTrip(new Trip());

		when(bookingRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(booking));

		// Act
		Optional<Booking> result = bookingService.getOne(10L);

		// Assert (interaction)
		verify(bookingRepository, times(1)).findById(10L);

		// Assert (result)
		assertThat(result).isPresent();
		assertThat(result.get().getId()).isEqualTo(10L);
	}

	@Test
	void getBookingByPassengerNameTest() {
		Booking booking = new Booking();
		booking.setId(10L);
		booking.setDate(LocalDate.now());
		booking.setPassenger(new User());
		booking.setPayment(List.of());
		booking.setSeatsBooked(8);
		booking.setStatus("CONFIRM");
		booking.setTime(LocalTime.now());
		booking.setTrip(new Trip());

		when(bookingRepository.findByPassenger(ArgumentMatchers.anyLong())).thenReturn(List.of(booking));

		List<Booking> result = bookingService.getBookingByPassengerName(10L);

		verify(bookingRepository, times(1)).findByPassenger(10L);

		assertThat(result).isNotNull();
		assertThat(result.getFirst()).isEqualTo(booking);
		assertThat(result.getFirst().getPassenger().getId()).isEqualTo(10L);

	}

	@Test
	void cancelBookingTest() {
		Booking booking = new Booking();
		booking.setId(10L);
		booking.setStatus("CANCELLED");
		
		when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
		when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));
		
		Boolean book = bookingService.cancelBooking(10L);	
		
		assertThat(book).isTrue();
		
        verify(bookingRepository).findById(10L);
        verify(bookingRepository).save(booking);
	}

	@Test
	void getByTripIdTest() {
		Trip trip = new Trip();
		trip.setId(10L);
		
		Booking booking = new Booking();
		booking.setTrip(trip);
		
		when(bookingRepository.findByTrip(10L)).thenReturn((List<Booking>) booking);
		
		List<Booking> result = bookingService.getByTripId(10L);
		
		assertThat(result).isNotNull();
		assertThat(result.getFirst().getTrip().getId()).isEqualTo(10L);
		
        verify(bookingRepository).findByTrip(10L);
	}
}
