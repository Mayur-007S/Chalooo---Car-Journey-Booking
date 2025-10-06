package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
