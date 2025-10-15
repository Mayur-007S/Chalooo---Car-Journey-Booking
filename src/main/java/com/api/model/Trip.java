package com.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trips")
public class Trip implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull(message = "source should be not null")
    @NotEmpty(message = "source should be not empty")
    private String source;
    
    @NotNull(message = "destination should be not empty")
    @NotEmpty(message = "destination should be not empty")
    private String destination;
    
    @NotNull(message = "dateTime should be not null")
    private LocalDateTime dateTime;
    
    @NotNull(message = "totalSeats should be not null")
    private int totalSeats;
    
    @NotNull(message = "availableSeats should be not null")
    private int availableSeats;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver; // must have role "DRIVER"

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "trip")
    @JsonManagedReference
    private List<Booking> bookings;

	public Trip() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Trip(Long id, String source, String destination, LocalDateTime dateTime, int totalSeats, int availableSeats,
			User driver, Car car, List<Booking> bookings) {
		super();
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.dateTime = dateTime;
		this.totalSeats = totalSeats;
		this.availableSeats = availableSeats;
		this.driver = driver;
		this.car = car;
		this.bookings = bookings;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
    
    
}
