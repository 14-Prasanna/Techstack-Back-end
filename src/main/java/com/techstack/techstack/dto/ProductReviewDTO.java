package com.techstack.techstack.dto;

import lombok.Data;

@Data
public class ProductReviewDTO {
    private Long id;
    private Long productId;
    private String reviewerName;
    private Integer rating;
    private String comment;
}