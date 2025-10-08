package com.api.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @NotNull(message = "amount should be not null")
    @NotEmpty(message = "amount should be not empty")
    private Double amount;
    @NotNull(message = "status should be not null")
    @NotEmpty(message = "status should be not empty")
    private String status; 
    @NotNull(message = "method should be not null")
    @NotEmpty(message = "method should be not empty")
    private String method; 
    @NotNull(message = "date should be not null")
    @NotEmpty(message = "date should be not empty")
    private LocalDate date;
    @NotNull(message = "time should be not null")
    @NotEmpty(message = "time should be not empty")
    private LocalTime time;
    
	public Payment() {
	}

	public Payment(Long id, Booking booking, 
			Double amount, String status, String method) {
		super();
		this.id = id;
		this.booking = booking;
		this.amount = amount;
		this.status = status;
		this.method = method;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}
    
    
}
