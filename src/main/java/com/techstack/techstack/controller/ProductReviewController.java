package com.techstack.techstack.controller;

import com.techstack.techstack.dto.ProductReviewDTO;
import com.techstack.techstack.service.ProductReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class ProductReviewController {

    private final ProductReviewService reviewService;


    @PostMapping
    public ResponseEntity<ProductReviewDTO> createReview(
            @PathVariable Long productId,
            @Valid @RequestBody ProductReviewDTO reviewDTO) {
        ProductReviewDTO createdReview = reviewService.createReview(productId, reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ProductReviewDTO>> getReviewsByProductId(@PathVariable Long productId) {
        List<ProductReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }
}