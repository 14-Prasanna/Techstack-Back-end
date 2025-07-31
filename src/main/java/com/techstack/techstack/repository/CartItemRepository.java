package com.techstack.techstack.repository;

import com.techstack.techstack.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * CORRECTED: This new method provides an efficient way to find a specific product
     * within a specific cart. Spring Data JPA will automatically generate the query
     * based on the method name.
     *
     * @param cartId The ID of the cart to search within.
     * @param productId The ID of the product to find.
     * @return An Optional containing the CartItem if it exists.
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}