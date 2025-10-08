package com.api.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer; // any user

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private User subject; // driver or passenger

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip; // optional: review for specific trip
    @NotNull(message = "rating should be not null")
    @NotEmpty(message = "rating should be not empty")
    private int rating;
    @NotNull(message = "comment should be not null")
    @NotEmpty(message = "comment should be not empty")
    private String comment;
    @NotNull(message = "date should be not null")
    @NotEmpty(message = "date should be not empty")
    private LocalDate date;
    @NotNull(message = "time should be not null")
    @NotEmpty(message = "time should be not empty")
    private LocalTime time;
    
	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Review(Long id, User reviewer, User subject, Trip trip, int rating, String comment) {
		super();
		this.id = id;
		this.reviewer = reviewer;
		this.subject = subject;
		this.trip = trip;
		this.rating = rating;
		this.comment = comment;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getReviewer() {
		return reviewer;
	}
	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}
	public User getSubject() {
		return subject;
	}
	public void setSubject(User subject) {
		this.subject = subject;
	}
	public Trip getTrip() {
		return trip;
	}
	public void setTrip(Trip trip) {
		this.trip = trip;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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

