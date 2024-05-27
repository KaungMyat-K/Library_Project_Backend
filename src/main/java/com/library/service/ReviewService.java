package com.library.service;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.dto.ReviewDto;
import com.library.entity.ReviewEntity;
import com.library.repository.ReviewRepository;

@Service
@Transactional
public class ReviewService {


    @Autowired
    private ReviewRepository reviewRepository;


    public void postReview(String userEmail,ReviewDto reviewDao)throws Exception{
        ReviewEntity validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, reviewDao.getBookId());
        if(validateReview!=null){
            throw new Exception("Review already created");
        }
        ReviewEntity.ReviewEntityBuilder reviewBuilder = ReviewEntity.builder().bookId(reviewDao.getBookId()).rating(reviewDao.getRating()).userEmail(userEmail);

        if(reviewDao.getReviewDescription().isPresent()){
            reviewBuilder.reviewDescription(reviewDao.getReviewDescription().map(
                Object::toString
            ).orElse(null)).build();
        }
        
        reviewBuilder.date(Date.valueOf(LocalDate.now())).build();
        ReviewEntity review = reviewBuilder.build();
        reviewRepository.save(review);
    }

    public Boolean userReviewListed(String userEmail, Long bookId) {
        ReviewEntity validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (validateReview != null) {
            return true;
        } else {
            return false;
        }
    }


}
