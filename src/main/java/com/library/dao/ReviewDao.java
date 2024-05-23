package com.library.dao;

import java.util.Optional;

import lombok.Data;

@Data
public class ReviewDao {
    
    private double rating;
    private Long bookId;
    private Optional<String> reviewDescription;
}
