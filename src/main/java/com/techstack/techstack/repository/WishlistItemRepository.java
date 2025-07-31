package com.techstack.techstack.repository;

import com.techstack.techstack.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    // This helps prevent adding duplicate items
    boolean existsByWishlistIdAndProductId(Long wishlistId, Long productId);
}