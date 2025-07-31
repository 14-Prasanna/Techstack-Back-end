package com.techstack.techstack.service;

import com.techstack.techstack.dto.ProductReviewDTO;
import com.techstack.techstack.entity.Product;
import com.techstack.techstack.entity.ProductReview;
import com.techstack.techstack.repository.ProductRepository;
import com.techstack.techstack.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    /**
     * Creates and saves a new review for a given product.
     * @param productId The ID of the product to review.
     * @param reviewDTO The review data transfer object.
     * @return The created review as a DTO.
     */
    @Transactional
    public ProductReviewDTO createReview(Long productId, ProductReviewDTO reviewDTO) {
        // 1. Find the product that this review belongs to.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // 2. Create a new ProductReview entity from the DTO.
        ProductReview review = new ProductReview();
        review.setProduct(product);
        review.setReviewerName(reviewDTO.getReviewerName());
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        // 3. Save the new review entity to the database.
        ProductReview savedReview = reviewRepository.save(review);

        // 4. Return the saved review as a DTO.
        return mapToDTO(savedReview);
    }

    /**
     * Retrieves all reviews for a specific product.
     * @param productId The ID of the product.
     * @return A list of review DTOs.
     */
    public List<ProductReviewDTO> getReviewsByProductId(Long productId) {
        // 1. Check if the product exists to avoid unnecessary database calls.
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found with id: " + productId);
        }

        // 2. Fetch all reviews for the given product ID.
        List<ProductReview> reviews = reviewRepository.findByProductId(productId);

        // 3. Map the list of entities to a list of DTOs and return it.
        return reviews.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to map a ProductReview entity to its DTO representation.
     * @param review The ProductReview entity.
     * @return The corresponding ProductReviewDTO.
     */
    private ProductReviewDTO mapToDTO(ProductReview review) {
        ProductReviewDTO dto = new ProductReviewDTO();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setReviewerName(review.getReviewerName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        return dto;
    }
}