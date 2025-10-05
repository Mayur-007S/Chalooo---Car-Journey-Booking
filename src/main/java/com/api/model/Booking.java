package com.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private User passenger; // must have role "PASSENGER"
    @NotNull(message = "seatsBooked should be not null")
    @NotEmpty(message = "seatsBooked should be not empty")
    private int seatsBooked;
    @NotNull(message = "status should be not null")
    @NotEmpty(message = "status should be not empty")
    private String status; // REQUESTED, CONFIRMED, CANCELLED

    @OneToOne(mappedBy = "booking")
    private Payment payment;

	public Booking() {
		// TODO Auto-generated constructor stub
	}

	public Booking(Long id, Trip trip, User passenger, int seatsBooked, String status, Payment payment) {
		super();
		this.id = id;
		this.trip = trip;
		this.passenger = passenger;
		this.seatsBooked = seatsBooked;
		this.status = status;
		this.payment = payment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public User getPassenger() {
		return passenger;
	}

	public void setPassenger(User passenger) {
		this.passenger = passenger;
	}

	public int getSeatsBooked() {
		return seatsBooked;
	}

	public void setSeatsBooked(int seatsBooked) {
		this.seatsBooked = seatsBooked;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
    
	
    
}
