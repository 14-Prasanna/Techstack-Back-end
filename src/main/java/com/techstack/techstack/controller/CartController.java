package com.techstack.techstack.controller;

import com.techstack.techstack.dto.AddToCartRequestDTO;
import com.techstack.techstack.dto.CartResponseDTO;
import com.techstack.techstack.entity.User;
import com.techstack.techstack.repository.UserRepository;
import com.techstack.techstack.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDTO> addToCart(@Valid @RequestBody AddToCartRequestDTO request) {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(cartService.addToCart(userId, request.getProductId(), request.getQuantity()));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDTO> getCart() {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    /**
     * This method is now valid because the corresponding method
     * has been added to the CartService.
     */
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteCart() {
        Long userId = getAuthenticatedUserId();
        cartService.deleteCart(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(@PathVariable Long cartItemId) {
        Long userId = getAuthenticatedUserId();
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, cartItemId));
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database: " + userEmail));
    }
}