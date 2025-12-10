package com.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.ReviewDTO;
import com.api.model.Review;
import com.api.service.ReviewService;
import com.api.validation.ObjectValidator;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	private Logger log = LoggerFactory.getLogger(ReviewController.class);
	
	@Autowired
	private ObjectValidator<Review> validator;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getAll")
	public ResponseEntity<List<Review>> getAllReviews(){
		log.info("Inside get all reviews method");
		List<Review> listofreviews = reviewService.getAll();
		if(!listofreviews.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(listofreviews);
		}
		throw new NotFoundException("Reviews: Not found");
	}
	
	@PreAuthorize("hasAnyRole('PASSENGER','ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<Review> addReviews(@RequestBody ReviewDTO review){
		log.info("Inside add review controller");
		
		Review reviews2 = reviewService.addReviews(review);
		if(reviews2 != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(reviews2);		
		}
		throw new NullPointerException("Review Not Added. !!!");
	}
	
	@PreAuthorize("hasAnyRole('PASSENGER','DRIVER','ADMIN')")
	@GetMapping("/getByTripId")
	public ResponseEntity<List<Review>> getByTripId(
			@RequestParam(value="tripid", required = true) int tripId)
	{
		log.info("Inside get reviews by trip id method");
		List<Review> listofreviews = reviewService.getByTripId(tripId);
		if(!listofreviews.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(listofreviews);
		}
		throw new NotFoundException("Reviews: Not found for trip id: " + tripId);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/deleteReview")
	public ResponseEntity<String> deleteReview(@RequestParam("reviewId") long reviewId){
		log.info("Inside delete review ");
		reviewService.deleteReview(reviewId);
		return ResponseEntity.status(HttpStatus.OK).body("Review Deleted Successfully.!!!");
	}
	
	@PreAuthorize("hasAnyRole('PASSENGER','ADMIN')")
	@PostMapping("/deleteOwnReview")
	public ResponseEntity<String> deleteOwnReview(
			@RequestParam("reviewId") long reviewId,
			@RequestParam("userId") long userId
			){
		log.info("Inside delete review ");
		reviewService.deleteOwnReview(reviewId, userId);
		return ResponseEntity.status(HttpStatus.OK).body("Review Deleted Successfully.!!!");
	}
}
