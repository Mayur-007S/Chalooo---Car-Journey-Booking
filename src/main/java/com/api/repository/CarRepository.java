package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

}
