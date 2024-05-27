package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.Utils.ExtractJwt;
import com.library.dto.ReviewDto;
import com.library.service.ReviewService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ExtractJwt extractJwt;

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value="Authorization")String token,@RequestBody ReviewDto reviewDao)throws Exception {
        String userEmail = extractJwt.payloadJWTExtraction(token,"\"sub\"");
        if(userEmail==null){
            throw new Exception("User email is missing");
        }
        reviewService.postReview(userEmail, reviewDao);
    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@RequestHeader(value="Authorization") String token,@RequestParam Long bookId) throws Exception {
        String userEmail = extractJwt.payloadJWTExtraction(token, "\"sub\"");

        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        return reviewService.userReviewListed(userEmail, bookId);
    }
    
}
