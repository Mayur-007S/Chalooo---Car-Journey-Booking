package com.api.dto;

import java.time.LocalDateTime;

public class TripResponseDTO {
	private Long id;
    private String source;
    private String destination;
    private LocalDateTime dateTime;
    private int totalSeats;
    private int availableSeats;
    
	public TripResponseDTO(Long id, String source, String destination, LocalDateTime dateTime,
			int totalSeats,int availableSeats) {
		super();
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.dateTime = dateTime;
		this.totalSeats = totalSeats;
		this.availableSeats = availableSeats;
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
    
}
