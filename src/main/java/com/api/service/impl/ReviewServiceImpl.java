package com.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.model.Review;
import com.api.repository.ReviewRepository;
import com.api.service.ReviewService;
import com.api.validation.ObjectValidator;

@Service
public class ReviewServiceImpl implements ReviewService {

	private ReviewRepository repository;
	
	private Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);
	
	@Autowired
	private ObjectValidator<Review> validator;
 	
	@Override
	public Review addReviews(Review review) {
		log.info("Inside getall trip method");
		validator.validate(review);
		log.info("Validating review object");
		log.info("Call save method");
		log.info("Exit from get all method");
		return repository.save(review);
	}

	@Override
	public List<Review> getAll() {
		log.info("Inside get all reviews method");
		log.info("Call get All method");
		log.info("Exit from get all reviews method");
		return repository.findAll();
	}

	@Override
	public Review getOne(String reviewer) {
		// TODO Auto-generated method stub
		return null;
	}

}
