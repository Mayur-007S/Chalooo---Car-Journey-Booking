package com.api.service;

import java.util.List;

import com.api.model.Review;

public interface ReviewService {

	Review addReviews(Review review);
	
	List<Review> getAll();
	
	Review getOne(String reviewer);
}
