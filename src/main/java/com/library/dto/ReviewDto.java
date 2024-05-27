package com.library.dto;

import java.util.Optional;

import lombok.Data;

@Data
public class ReviewDto {
    
    private double rating;
    private Long bookId;
    private Optional<String> reviewDescription;
}
