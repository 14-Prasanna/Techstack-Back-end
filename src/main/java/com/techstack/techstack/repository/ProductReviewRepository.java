package com.techstack.techstack.repository;

import com.techstack.techstack.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    /**
     * Finds all reviews associated with a specific product, identified by its ID.
     * @param productId The ID of the product.
     * @return A list of product reviews.
     */
    List<ProductReview> findByProductId(Long productId);
}