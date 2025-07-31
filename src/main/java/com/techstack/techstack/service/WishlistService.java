package com.techstack.techstack.service;

import com.techstack.techstack.dto.WishlistResponseDTO;
import com.techstack.techstack.entity.Product;
import com.techstack.techstack.entity.User;
import com.techstack.techstack.entity.Wishlist;
import com.techstack.techstack.entity.WishlistItem;
import com.techstack.techstack.repository.ProductRepository;
import com.techstack.techstack.repository.UserRepository;
import com.techstack.techstack.repository.WishlistItemRepository;
import com.techstack.techstack.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<WishlistResponseDTO> getWishlistByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId)
                .map(wishlist -> wishlist.getWishlistItems().stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Transactional
    public List<WishlistResponseDTO> addToWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(user);
                    return wishlistRepository.save(newWishlist);
                });

        // Prevent adding duplicate items
        if (wishlistItemRepository.existsByWishlistIdAndProductId(wishlist.getId(), productId)) {
            return getWishlistByUserId(userId); // Item is already there, return current list
        }

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setWishlist(wishlist);
        wishlistItem.setProduct(product);

        wishlist.getWishlistItems().add(wishlistItem);
        wishlistRepository.save(wishlist);

        return getWishlistByUserId(userId);
    }

    @Transactional
    public List<WishlistResponseDTO> removeFromWishlist(Long userId, Long wishlistItemId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found for user"));

        WishlistItem itemToRemove = wishlistItemRepository.findById(wishlistItemId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        // Security check: ensure the item belongs to the user's wishlist
        if (!itemToRemove.getWishlist().getId().equals(wishlist.getId())) {
            throw new SecurityException("User does not have permission to remove this item");
        }

        // This is handled by orphanRemoval=true in the Wishlist entity
        wishlist.getWishlistItems().remove(itemToRemove);
        wishlistRepository.save(wishlist);

        return getWishlistByUserId(userId);
    }

    private WishlistResponseDTO mapToDto(WishlistItem item) {
        Product product = item.getProduct();
        String imageUrl = null;
        if (product.getImageUrl() != null && product.getImageUrl().length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(product.getImageUrl());
            imageUrl = "data:image/jpeg;base64," + base64Image;
        }

        return new WishlistResponseDTO(
                item.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                imageUrl,
                item.getAddedAt()
        );
    }
}