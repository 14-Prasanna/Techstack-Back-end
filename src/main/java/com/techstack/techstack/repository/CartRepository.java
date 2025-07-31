package com.techstack.techstack.repository;

import com.techstack.techstack.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Finds a user's cart based on their user ID and the cart's current status.
     * This is used to find the single "ACTIVE" cart for a user.
     *
     * @param userId The ID of the user.
     * @param status The status of the cart (e.g., ACTIVE, CHECKED_OUT).
     * @return An Optional containing the Cart if found.
     */
    Optional<Cart> findByUserIdAndStatus(Long userId, Cart.Status status);
}