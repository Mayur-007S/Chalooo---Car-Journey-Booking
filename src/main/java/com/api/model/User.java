package com.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Email should be not null")
    @NotEmpty(message = "Email should be not empty")
    @Column(unique=true)
    private String email;
    @NotNull(message = "Password should be not null")
    @NotEmpty(message = "Password should be not empty")
    private String password;
    @NotNull(message = "username should be not null")
    @NotEmpty(message = "username should be not empty")
    private String username;
    @NotNull(message = "Phone should be not null")
    @NotEmpty(message = "Phone should be not empty")
    private String phone;

    // "DRIVER" or "PASSENGER"
    private String role;

    // Relations:
    @OneToMany(mappedBy = "owner")
    private List<Car> cars;       // If DRIVER

    @OneToMany(mappedBy = "driver")
    private List<Trip> trips;     // If DRIVER

    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings; // If PASSENGER

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(Long id, String email, String password, 
			String username, String phone, String role) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.username = username;
		this.phone = phone;
		this.role = role;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}