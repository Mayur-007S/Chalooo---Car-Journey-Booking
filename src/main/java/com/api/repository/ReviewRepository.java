package com.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query(value = "SELECT * FROM reviews where trip_id =:tripid", nativeQuery = true)
	List<Review> findByTripId(@Param("tripid") long tripid);
}
