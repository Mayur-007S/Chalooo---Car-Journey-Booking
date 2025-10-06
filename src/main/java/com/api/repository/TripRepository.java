package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long>{

}
