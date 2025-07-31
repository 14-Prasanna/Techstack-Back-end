package com.techstack.techstack.controller;

import com.techstack.techstack.dto.AddToWishlistRequestDTO;
import com.techstack.techstack.dto.WishlistResponseDTO;
import com.techstack.techstack.entity.User;
import com.techstack.techstack.repository.UserRepository;
import com.techstack.techstack.service.WishlistService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WishlistResponseDTO>> getWishlist() {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WishlistResponseDTO>> addToWishlist(@Valid @RequestBody AddToWishlistRequestDTO request) {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(wishlistService.addToWishlist(userId, request.getProductId()));
    }

    @DeleteMapping("/{wishlistItemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WishlistResponseDTO>> removeFromWishlist(@PathVariable Long wishlistItemId) {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(wishlistService.removeFromWishlist(userId, wishlistItemId));
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database: " + userEmail));
    }
}