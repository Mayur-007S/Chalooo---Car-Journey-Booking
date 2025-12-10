package com.api.service;

import java.util.List;

import com.api.dto.ReviewDTO;
import com.api.model.Review;

public interface ReviewService {

	Review addReviews(ReviewDTO review);
	
	List<Review> getAll();
	
	Review getOne(long reviewId);
	
	List<Review> getByTripId(int tripId);
	
	void deleteReview(long reviewId);
	
	void deleteOwnReview(long reviewId, long reviewerId);
}
