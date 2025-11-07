package com.api.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    private double rating;
    
    @NotNull(message = "comment should be not null")
    @NotEmpty(message = "comment should be not empty")
    private String comment;

    private LocalDate date;
    private LocalTime time;
    
	public Review() {
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReviewer() {
		return reviewer.getId();
	}
	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}
	public Long getSubject() {
		return subject.getId();
	}
	public void setSubject(User subject) {
		this.subject = subject;
	}
	public Long getTrip() {
		return trip.getId();
	}
	public void setTrip(Trip trip) {
		this.trip = trip;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
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

