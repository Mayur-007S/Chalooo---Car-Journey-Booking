package com.api.model;

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
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull(message = "Model should be not null")	@NotEmpty(message = "Model should be not empty")
    private String model;
    
    @NotNull(message = "PlateNo should be not empty")
	@NotEmpty(message = "PlateNo should be not empty")
    private String plateNo;
    
    @NotNull(message = "Seates should be not null")
    @NotEmpty(message = "Seates should be not empty")
    private int seats;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

	public Car() {
	}

	public Car(Long id, String model, String plateNo, int seats, User owner) {
		super();
		this.id = id;
		this.model = model;
		this.plateNo = plateNo;
		this.seats = seats;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

    
}
