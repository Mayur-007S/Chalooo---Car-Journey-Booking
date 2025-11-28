package com.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.customeexceptions.NotFoundException;
import com.api.dto.ReviewDTO;
import com.api.dto.TripDTO;
import com.api.dto.mapper.TripMapper;
import com.api.model.Review;
import com.api.model.Trip;
import com.api.model.User;
import com.api.repository.ReviewRepository;
import com.api.repository.TripRepository;
import com.api.service.ReviewService;
import com.api.service.TripService;
import com.api.service.UserService;
import com.api.validation.ObjectValidator;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository repository;

	@Autowired
	private UserService userService;

	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private TripMapper mapper;

	private Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

	@Autowired
	private ObjectValidator<ReviewDTO> validator;

	public Review addReviews(ReviewDTO reviewdto) {
	    log.info("Inside add reviews method");
	    validator.validate(reviewdto);

	    Review review = new Review();
	    review.setRating(reviewdto.rating());
	    review.setComment(reviewdto.comment());
	    review.setDate(reviewdto.date());
	    review.setTime(reviewdto.time());

	    User reviewer = userService.getOneUser(reviewdto.reviewer_id());
	    if (reviewer == null) {
	        throw new NotFoundException("Reviewer not found for id: " + reviewdto.reviewer_id());
	    }
	    review.setReviewer(reviewer);

	    User subject = userService.getOneUser(reviewdto.subject_id());
	    if (subject == null) {
	        throw new NotFoundException("Subject not found for id: " + reviewdto.subject_id());
	    }
	    review.setSubject(subject);

	    Trip trip = tripRepository.findById(reviewdto.trip_id())
           .orElseThrow(() -> new NotFoundException("Trip not found for id: " 
            		+ reviewdto.trip_id()));
	    
	    review.setTrip(trip);

	    return repository.save(review);
	}


	@Override
	public List<Review> getAll() {
		log.info("Inside get all reviews method");
		return repository.findAll();
	}

	@Override
	public Review getOne(long reviewerId) {
		log.info("Inside get one review method");
		return repository.findById(reviewerId)
				.orElseThrow(() -> new NotFoundException("Review not found for id: " + reviewerId));
	}

	@Override
	public List<Review> getByTripId(int tripId) {
		log.info("Inside getByTripId review method");
		return repository.findByTripId(tripId);
	}

}
