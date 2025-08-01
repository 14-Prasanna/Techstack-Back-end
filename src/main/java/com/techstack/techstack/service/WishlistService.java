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

        if (wishlistItemRepository.existsByWishlistIdAndProductId(wishlist.getId(), productId)) {
            return getWishlistByUserId(userId);
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

        if (!itemToRemove.getWishlist().getId().equals(wishlist.getId())) {
            throw new SecurityException("User does not have permission to remove this item");
        }

        wishlist.getWishlistItems().remove(itemToRemove);
        wishlistRepository.save(wishlist);

        return getWishlistByUserId(userId);
    }

    private WishlistResponseDTO mapToDto(WishlistItem item) {
        Product product = item.getProduct();
        String imageUrl = null;
        // --- THIS IS THE CORRECTED LINE ---
        byte[] imageData = product.getImageData();
        if (imageData != null && imageData.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(imageData);
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